package com.catseye.patternComponents.gridGenerators;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;

import com.catseye.CatsEye;
import com.catseye.gui.components.SavedStateBar;
import com.catseye.patternComponents.polygonGenerators.Java2DNgonGenerator;
import com.catseye.patternComponents.polygonGenerators.NGonGenerator;
import com.catseye.patternComponents.polygonGenerators.P2DIrregularPolygonGenerator;
/*---------------------------------------------------------------------------------------------
 *
 *    TileGrid
 *    
 *    Base class for various grid generators (hexGrid, triGrid, squareGrid etc..)
 *    Do not instantiate this class directly, rather use one of the subclasses that have the generate()
 *    function implemented.
 *
 *    Ben Jack 6/4/2014 
 *
 *---------------------------------------------------------------------------------------------*/
import com.catseye.patternComponents.polygonGenerators.P2DNgonGenerator;

public class TileGrid {

  //----------------- CLASS VARIABLES---------------------

  protected PGraphics renderContext, gridContext;
  protected PImage render, previewImage, gridPreviewImage;
  protected PImage textureImage; 
  protected PImage maskImage;

  protected NGonGenerator ngonGenerator;

  protected boolean generated = false;
  protected boolean gridGenerated = false;
  protected float missingOdds = 0; //a number between 0 and 1 that represents a random chance a tile will be missing 
  protected float cellRadius;
  protected PVector cellSize;
  protected PVector renderSize, previewSize;
  protected PVector[] texCoords;

  protected boolean useMask;

  protected String renderMode = PApplet.P2D;  
  
  //----------------------CONSTRUCTORS------------------------

  //Don't create a TileGrid directly, use a subclass (hexGrid, triGrid, squareGrid)
  protected TileGrid() {

    //default settings
    renderSize = new PVector(CatsEye.p5.width, CatsEye.p5.height);
    previewSize = new PVector(CatsEye.p5.width, CatsEye.p5. height);

    texCoords = new PVector[4];
    texCoords[0] = new PVector(0, 1);
    texCoords[1] = new PVector(1, 1);
    texCoords[2] = new PVector(1, 0);
    texCoords[3] = new PVector(1, 1);
    
    cellRadius = 100;
    previewImage = CatsEye.p5.createGraphics(100, 100);
    ((PGraphics)previewImage).beginDraw();
    ((PGraphics)previewImage).background(0, 0);
    ((PGraphics)previewImage).endDraw();

    textureImage = CatsEye.p5.createGraphics(100, 100);
    ((PGraphics)textureImage).beginDraw();
    ((PGraphics)textureImage).background(0, 0);
    ((PGraphics)textureImage).endDraw();
    
  }

  protected TileGrid(TileGrid i_toCopy) {

    //default settings
    renderSize = i_toCopy.getRenderSize();
    previewSize = i_toCopy.getPreviewSize();
    cellRadius = i_toCopy.getCellRadius();
    maskImage = i_toCopy.getMaskImage();
    useMask = i_toCopy.isUsingMask();
    missingOdds = i_toCopy.getMissingOdds();
    setTextureCoords(i_toCopy.getTextureCoords());
    renderMode = i_toCopy.getRenderMode();

    previewImage = CatsEye.p5.createGraphics(100, 100);
    ((PGraphics)previewImage).beginDraw();
    ((PGraphics)previewImage).background(0, 0);
    ((PGraphics)previewImage).endDraw();

    textureImage = CatsEye.p5.createGraphics(100, 100);
    ((PGraphics)textureImage).beginDraw();
    ((PGraphics)textureImage).background(0, 0);
    ((PGraphics)textureImage).endDraw();
    
  }  


  //----------------------------------SETTERS/GETTERS-----------------------------------------


  //----setters-----

  public void setTexture(PImage i_texture) {
    textureImage = i_texture.get();
  }
  

  public void setTextureCoords(PVector[] i_texCoords) {

    texCoords = new PVector[i_texCoords.length];
    for(int i = 0; i < i_texCoords.length; ++i)
    	texCoords[i] = i_texCoords[i];
    
  } 

  public void setRender(PImage i_render){
	  render = i_render;
  }
  
  public void setMask(PImage i_mask) {
    maskImage = i_mask.get();
  }

  public void setCellRadius(float i_cellRad) {
    cellRadius = i_cellRad;
    gridGenerated = false;
  }

  public void setRenderSize(PVector i_size) {
    renderSize = i_size.get();
    gridGenerated = false;
  }

  public void setMissingOdds(float i_odds) {
    missingOdds = i_odds;
  }


  public void useMask(boolean i_useMask) {
    useMask = i_useMask;
  }

  public void setPreviewSize(PVector i_size) {
    previewSize = i_size;
  }
  
  public void overwritePreviewImage(PImage i_preview){
	  previewImage = i_preview;
  }

  /***     
   *      This changes between rendering modes. The options are JAVA2D and P2D 
   ***/
  public void setRenderMode(String i_mode) {
    renderMode = i_mode;
  }

  //-----getters-----

  public PImage getRender() {
    return render.get();
  }

  public PImage getPreviewImage() {  
    return previewImage;
  }

  public PImage getGridImage() {

    if (!gridGenerated) {
      regenerateGrid();
    } 

    return gridPreviewImage;
  }
  
  public PImage getTextureImage(){
	  return textureImage;
  }
  
  //this method can be overwritten if child classes need to be setup in order to generate grid preview
  public PImage getMiniGridImage(PVector i_size) {
	return getGridImage(); 
  }
  
  
  @SuppressWarnings("unchecked")
  public static TileGrid getGridFromClassString(String i_subClassName){
	 
	  try{
			 Class<TileGrid> myClass = (Class<TileGrid>)Class.forName(i_subClassName);
	      	 Constructor<TileGrid> construct = myClass.getConstructor();
	      	 TileGrid g = (TileGrid)construct.newInstance();
	      	 
	      	 return g;
		  }
		  catch(ClassNotFoundException e){System.out.println(e.getMessage());}
		  catch(NoSuchMethodException e){System.out.println(e.getMessage());}
		  catch(IllegalAccessException e){System.out.println(e.getMessage());}
		  catch(InstantiationException e){System.out.println(e.getMessage());}
		  catch(InvocationTargetException e){System.out.println(e.getMessage());}
	  
	  return null;
  }
  
  @SuppressWarnings("unchecked")
  public static TileGrid getGridFromClassString(String i_subClassName, TileGrid i_oldGrid){
	 
	  try{
			 Class<TileGrid> tileGridClass = (Class<TileGrid>)Class.forName("com.catseye.patternComponents.gridGenerators.TileGrid");
		     Class<TileGrid> myClass = (Class<TileGrid>)Class.forName(i_subClassName);
	      	 
			 Constructor<TileGrid> construct = myClass.getConstructor(tileGridClass);
	      	 TileGrid g = (TileGrid)construct.newInstance(i_oldGrid);
	      	 
	      	 return g;
		  }
		  catch(ClassNotFoundException e){System.out.println(e.getMessage());}
		  catch(NoSuchMethodException e){System.out.println(e.getMessage());}
		  catch(IllegalAccessException e){System.out.println(e.getMessage());}
		  catch(InstantiationException e){System.out.println(e.getMessage());}
		  catch(InvocationTargetException e){System.out.println(e.getMessage());}
	  
	  return null;
  }
  

  
  public static PImage getGridMiniPreview(String i_subClassName, PVector i_size){
		 
      TileGrid g = getGridFromClassString(i_subClassName);
      
      if(g != null){
    	g.setRenderMode(PApplet.JAVA2D);
    	g.setCellRadius(i_size.x/4.0f);
      	g.setRenderSize(i_size);
      	g.setPreviewSize(i_size);
      	return g.getMiniGridImage(i_size);
      }else{
	 
		  PGraphics broken = CatsEye.p5.createGraphics((int) i_size.x, (int) i_size.y);
		  broken.beginDraw();
		  broken.stroke(255,255,0);
		  
		  for(int i = 0; i < PApplet.max(i_size.x, i_size.y)*2 ; i+=5)
			  broken.line(0, i, i, 0);
		  
		  broken.endDraw();
		  return broken; 
      }  
  }
  
  public void regenerateGrid(){
    generate(true);
    gridPreviewImage = gridContext.get();
    gridPreviewImage.resize(gridContext.width >= gridContext.height ? (int)previewSize.x : 0, gridContext.height > gridContext.width ? (int)previewSize.y : 0);
  }

  public PImage getUnitImage() {

    if (generated)
      return ngonGenerator.getUnitImage();
    else
      return CatsEye.p5.createImage(1, 1, PApplet.ARGB);
  }

  public PVector getRenderSize() {
    return renderSize.get();
  }

  public PVector getPreviewSize() {
    return previewSize.get();
  }

  public PVector[] getTextureCoords() {

    PVector[] copy = new PVector[texCoords.length];
    
    for(int i = 0; i < copy.length; ++i){
    	copy[i] = texCoords[i].get();
    }
    
    return copy;
  } 

  public float getCellRadius() {
    return cellRadius;
  }

  public PImage getMaskImage() {
    return maskImage;
  }  

  public boolean isUsingMask() {
    return useMask;
  }

  public float getMissingOdds() {
    return missingOdds;
  }

  public String getRenderMode() {
    return renderMode;
  }
  
  public boolean getUseMask(){
	  return useMask;
  }

  public JSONObject saveAsJSON(){
	   
	  if(generated){
		   JSONObject json = new JSONObject();
		   
		   json.setString("class", this.getClass().getName());
		   
		   json.setFloat("renderX", renderSize.x);
		   json.setFloat("renderY", renderSize.y);
		   json.setFloat("previewX", previewSize.x);
		   json.setFloat("previewY", previewSize.y);
		   json.setFloat("cellRadius", cellRadius);
		   json.setFloat("missingOdds", missingOdds);
		   json.setBoolean("useMask", useMask);
		   json.setString("renderMode", renderMode);
		   
		   
		   JSONArray texCoordsArr = new JSONArray();
		   
		   for(int i = 0; i < texCoords.length; ++i){
			   JSONObject vec = new JSONObject();
			   vec.setFloat("x", texCoords[i].x);
			   vec.setFloat("y", texCoords[i].y);
			   
			   texCoordsArr.append(vec);
		   }
		   
		   json.setJSONArray("texCoords", texCoordsArr);
	
		   String savePath = SavedStateBar.SAVEPATH+(new Date().getTime())+"/";
		   File savePathFile = new File(savePath) ;
		   
		   if(!savePathFile.exists())
			   savePathFile.mkdirs();
		   
		   System.out.println(savePathFile.getAbsolutePath());
		   
		   if(textureImage != null)
			   textureImage.save(savePathFile.getAbsolutePath()+"/textureImage.png");
		   
		   if(maskImage != null)
			   maskImage.save(savePathFile.getAbsolutePath()+"/maskImage.png");
		   
		   if(render != null)
			   render.save(savePathFile.getAbsolutePath()+"/previewImage.png");
		   
		   
		   json.setString("savePath", savePathFile.getAbsolutePath()+"/");
		   CatsEye.p5.saveJSONObject(json, savePathFile.getAbsolutePath()+"/saveData.json");
		   
		   return json;
	  }

	  return null;
	  
  }


  //---------------------------------METHODS--------------------------------------
  

  public void saveImage(String i_path) {
    saveImage(i_path, ".png", false);
  }

  public void saveImage(String i_path, String i_fileSuffix, boolean i_saveWithGrid) {

    if (!generated) {
      generate();
    }

    PGraphics gfx = CatsEye.p5.createGraphics((int)renderSize.x, (int)renderSize.y);   
    gfx.beginDraw();
    gfx.noSmooth();
    gfx.background(255,0);
    gfx.image(renderContext,0,0);
    
    if(i_saveWithGrid){
      regenerateGrid();
      gfx.image(gridContext,0,0);
    }
      
    gfx.save(i_path+(i_saveWithGrid ? "_GRID" : "")+i_fileSuffix);
  }
  
  
  public void saveTile(String i_path) {
    PImage tempTile = getUnitImage();

    //this is a hack for a bug that won't save images in JAVA2D mode if PImage.save() is used
    PGraphics tile = CatsEye.p5.createGraphics(tempTile.width, tempTile.height);
    tile.beginDraw();
    tile.background(0,0);
    tile.image(tempTile, 0, 0);
    tile.endDraw();
    //---------------------------------------------------------------------------------------
    
    tile.save(i_path + ".png");
  }
  
  public static TileGrid fromJson(JSONObject json){
	  
	  TileGrid g = getGridFromClassString(json.getString("class"));
      
      if(g != null){
    	  	  
   	   	g.setRenderSize(new PVector(json.getFloat("renderX"), json.getFloat("renderY")));
   	   	g.setPreviewSize(new PVector(json.getFloat("previewX"), json.getFloat("previewY")));
   	   	g.setCellRadius(json.getFloat("cellRadius"));
   	   	g.setMissingOdds(json.getFloat("missingOdds"));
   	   	boolean useMaskBool = json.getBoolean("useMask");
   	   	g.useMask(useMaskBool);
   	   	g.setRenderMode(json.getString("renderMode"));
   	   	
   	   	JSONArray arr = json.getJSONArray("texCoords");
   	   	PVector[] loadedTexCoords = new PVector[arr.size()];  
   	   	for(int i = 0; i < arr.size(); ++i){
   	   		JSONObject o = arr.getJSONObject(i);
   	   		loadedTexCoords[i] = new PVector(o.getFloat("x"), o.getFloat("y"));
   	   	}
   	   	
   	   	g.setTextureCoords(loadedTexCoords);

   	   	
   	   	PImage texImg = CatsEye.p5.loadImage(json.getString("savePath")+"textureImage.png");
   	   	if(texImg != null)
   	   		g.setTexture(texImg);
   	   	
   	   	PImage prevImg = CatsEye.p5.loadImage(json.getString("savePath")+"previewImage.png");
   	   	if(prevImg != null)
   	   		g.overwritePreviewImage(prevImg);
   	   	
   	   	PImage maskImg = CatsEye.p5.loadImage(json.getString("savePath")+"maskImage.png");
   	   	if(maskImg != null)
   	   		g.setMask(maskImg);
   	   	
      }
	  
	  return g;
  }
  
  public void generate() {
    generate(false);
  }

  public void generate(boolean i_outlines) {
    /*Implement this function in a subclass
     see the helper methods below*/
  }


  //-----------------HELPER METHODS FOR USE IN SUBLCLASS GENERATE() IMPLEMENTATIONS-------------------

  /***
   *     when implementing a new tilegrid subclass use this function to draw the ngongeneratior
   *     This function implements the mask and missingodds features and also takes care
   *     of only drawing outlines if an outline grid is wanted.
   ***/
  protected void drawNgonAt(float i_x, float i_y, PGraphics i_currentContext) {

    if (i_currentContext == gridContext) {
      ngonGenerator.drawOutlinesAt(i_x, i_y, i_currentContext);
    }
    else if (CatsEye.p5.random(1) > missingOdds && !isMaskedAt(i_x, i_y)) {
      ngonGenerator.drawAt(i_x, i_y, i_currentContext);
    }
  }


  /***
   *     when implementing a new tilegrid subclass with regular polygons always call this at the very beginning 
   *     of the generate() function. It sets up the correct ngon generator.
   ***/
  protected NGonGenerator setupRegularNgonGenerator(int cellSides){
   
    if (renderMode == PApplet.JAVA2D)
      ngonGenerator = new Java2DNgonGenerator(cellSides, cellRadius, textureImage, texCoords);
    else
      ngonGenerator = new P2DNgonGenerator(cellSides, cellRadius, textureImage, texCoords);  
   
    cellSize = new PVector(ngonGenerator.cellWidth(), ngonGenerator.cellHeight());
    
    return ngonGenerator;
  }
  
   /***
   *     when implementing a new tilegrid subclass with irregular polygons always call this at the very beginning 
   *     of the generate() function. It sets up the correct polygon generator.
   ***/
  protected P2DIrregularPolygonGenerator setupIrregularNgonGenerator(){
   
    P2DIrregularPolygonGenerator generator = new P2DIrregularPolygonGenerator(textureImage, texCoords);
    ngonGenerator = generator;

    if (renderMode != PApplet.P2D)
      System.out.println("you can only have irregular polygons while in the P2D render mode");
      
      return generator; 

  }

  /***
   *     when implementing a new tilegrid subclass always call right after setting up 
   *     your polygon generator. It sets up and returns the correct drawing context.
   ***/
  protected PGraphics initGeneration(boolean i_outlines) {
    if (i_outlines) {
      gridContext = CatsEye.p5.createGraphics((int)renderSize.x, (int)renderSize.y);
      gridContext.beginDraw();
      gridContext.background(0, 0);
      return gridContext;
    }
    else {
      renderContext = CatsEye.p5.createGraphics((int)renderSize.x, (int)renderSize.y, renderMode);
      renderContext.beginDraw();
      renderContext.background(0, 0);
      return renderContext;
    }
  }

  /***
   *     when implementing a new tilegrid subclass always call this at the very end 
   *     of the generate() function. It correctly closes the correct drawing context
   *     and flags the tilegrid as generated. 
   ***/
  protected void completeGeneration(boolean i_outlines) {

    if (i_outlines) {
      gridContext.endDraw();
      gridGenerated = true;
    }
    else {
      renderContext.endDraw();
      generated = true;

      render = renderContext.get();
      previewImage = renderContext.get();
      previewImage.resize(renderContext.width >= renderContext.height ? (int)previewSize.x : 0, renderContext.height > renderContext.width ? (int)previewSize.y : 0);
    }
    
  }

  /***
   *    this implements image masking, you need not use it in a subclass if you are using drawNgonAt.
   ***/
  protected boolean isMaskedAt(float i_x, float i_y) {

    if (!useMask || maskImage == null)
      return false;

    try {
      maskImage.loadPixels();

      double xMaskScaleFactor = maskImage.width/(renderContext.width+0.0);
      double yMaskScaleFactor = maskImage.height/(renderContext.height+0.0);

      int xMask = (int)(PApplet.abs(i_x)*xMaskScaleFactor);
      int yMask = (int)(PApplet.abs(i_y)*yMaskScaleFactor);
      int col = maskImage.pixels[yMask*maskImage.width + xMask]&255;

      return col > 128;
    }
    catch(ArrayIndexOutOfBoundsException e) {
      return false;
    }
  }
}

