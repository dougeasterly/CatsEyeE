package com.catseye.gui.windows;


import java.io.File;
import java.awt.Color;

import com.catseye.CatsEye;
import com.catseye.gui.GUI;
import com.catseye.util.SVGLoader;

import processing.core.*;
import controlP5.*;



/*---------------------------------------------------------------------------------------------
*
*    ImageSelectionWindow
*    
*    PApplet class that implements the image cropping and selection window
*
*    Ben Jack 12/4/2014 
*
*---------------------------------------------------------------------------------------------*/

public class ImageSelectionApp extends GUIApp {
	
  GUI parent;
  ControlP5 cp5;
  
  PImage previewImage, textureImage;
  
  private PVector textureClipRectTL, textureClipRectBR;
  private PVector imageOffset;
  private PImage chosenImage; 
  SVGLoader svgTile;
  
  private float svgScale;
  
  private int clipBoxWidth, clipBoxHeight, clipBoxX, clipBoxY;
  private float scaleFactor;
  
 // private Numberbox box_width, box_height, box_x, box_y;
  //private  CallbackListener cb;
  private float controlHandleRadius = 15; 
  
  private DropdownList svgList;
  private int currentSvgChild;
  private Button bgColorButton,fillColorButton, strokeColorButton;
  private Numberbox strokeWeightBox;
  public float sw;// cp5 variable automatically updated by changes to strokeWeightBox
  private int SVGlistSize;
  
  private boolean dragging, resizing;
  private boolean svgControls = false;
  private boolean svgInit = false;
  private PVector mouseDragOffsetTL, mouseDragOffsetBR;
  
  private Button triSelectBtn;
  private boolean useTriSelect = false;
  private int triSelectedCorner;
  private PVector[] triangularSelection = {new PVector(0,0), new PVector(0,1), new PVector(1,1)};
  //private int SVGlistSize;
  
  //------------------------------CONSTRUCTORS/SETUP-----------------------------------
  
  @SuppressWarnings("unused")
  private ImageSelectionApp() {
  }


  public ImageSelectionApp(GUI i_Parent, int i_width, int i_height) {
    parent = i_Parent;
    this.appWidth = i_width;
    this.appHeight = i_height;
  }

  
  public void setup() {
    size(this.appWidth, this.appHeight);
    frameRate(25);
   
    imageOffset = new PVector(0,0);
   
    createGuiControls();    
    setTextureClipRect(new PVector(0,0), new PVector(0,0));
    
  }
  
  
  
  //--------------------------------PUBLIC METHODS---------------------------------------
 
  public void setImage(PImage i_img){
     
    textureImage = i_img.get();
    previewImage = textureImage.get();
    
    if(previewImage.width > previewImage.height){
       previewImage.resize(this.appWidth-40, 0);
    }
    else{
       previewImage.resize(0,this.appHeight-150);
    }
   
    scaleFactor = previewImage.width/(i_img.width+0.0f); 
    
    imageOffset = new PVector();
    imageOffset.x = this.appWidth/2-previewImage.width/2;
    imageOffset.y = this.appHeight/2-previewImage.height/2;
    
    setTextureClipRect(new PVector(0,0), new PVector(previewImage.width, previewImage.height, 0));
    defaultTriangularSelection();

  }
  
  
  /*
  *   show and hide the toggle for triangular selection
  */
  
  public void showTriSelectButton(){
    triSelectBtn.show();
  }
  
  public void hideTriSelectButton(){
    triSelectBtn.hide();
    useTriSelect = false;
  }
  
  public void toggleTriSelect(){
	  useTriSelect = !useTriSelect;
  }
  
  
  /*
  *   returns a PImage that is cropped by the current selection tool from the currently loaded texture image
  */
  
  public PImage getCropSection(){
   
    if(textureImage != null){
      if(!useTriSelect)
        return textureImage.get((int)(textureClipRectTL.x/scaleFactor), (int)(textureClipRectTL.y/scaleFactor), (int)((textureClipRectBR.x - textureClipRectTL.x)/scaleFactor), (int)((textureClipRectBR.y - textureClipRectTL.y)/scaleFactor));
      else
        return textureImage;
    }else{
     PGraphics out = createGraphics(1,1);
     out.beginDraw();
     out.background(0, 0);
     out.endDraw();
     return out.get(); 
    }
      
  }
  
  
  /*
  *   returns a PVector Array with a length of three of texture coordinates from current selection tool
  */
  
  public PVector[] getTextureCoords(){
   return useTriSelect ? getTriTexCoords() : getMarqueeTexCoords();
  }
  
  
  
  
  
  //--------------------------------------PRIVATE METHODS----------------------------------------
  //although some of these are public due to controlP5 needs or otherwise, they shouldn't be used 
  
  
  
  /*
  *   draw functions
  */

  public void draw() {
      background(180);
      // need some kind of listener created w svgList group
//      if(svgList.isOpen() == true){
//    	  dragging = false;
//      }
    	  
      if(previewImage != null){
       image(previewImage, imageOffset.x, imageOffset.y); 

       
      }
      
      if(!useTriSelect)
        drawMarquee();
      else
        drawTriangularSelection();
      
      textSize(14);
      fill(0);

      if(parent.getRenderMode() == P2D)
        text("P2D rendering", 20, this.height-48);
      else
        text("JAVA2D rendering", 20, this.height-48);
              
  }
  
  private void drawMarquee(){
    
      noFill();
      strokeWeight(2);
      stroke(255,0,0);
      rect(textureClipRectTL.x+imageOffset.x, textureClipRectTL.y+imageOffset.y, textureClipRectBR.x - textureClipRectTL.x, textureClipRectBR.y - textureClipRectTL.y );
      
      fill(255,0,0, 200);
      noStroke();
      ellipse(textureClipRectBR.x+imageOffset.x, textureClipRectBR.y+imageOffset.y, controlHandleRadius, controlHandleRadius);
      
  }
  
  private void drawTriangularSelection(){
    
     noFill();
     strokeWeight(2);
     stroke(255,0,0);
     triangle(triangularSelection[0].x+imageOffset.x, triangularSelection[0].y+imageOffset.y, triangularSelection[1].x+imageOffset.x, triangularSelection[1].y+imageOffset.y, triangularSelection[2].x+imageOffset.x, triangularSelection[2].y+imageOffset.y);
      
     fill(255,0,0, 200);
     noStroke();
     ellipse(triangularSelection[0].x+imageOffset.x, triangularSelection[0].y+imageOffset.y, controlHandleRadius, controlHandleRadius);
     ellipse(triangularSelection[1].x+imageOffset.x, triangularSelection[1].y+imageOffset.y, controlHandleRadius, controlHandleRadius);
     ellipse(triangularSelection[2].x+imageOffset.x, triangularSelection[2].y+imageOffset.y, controlHandleRadius, controlHandleRadius);
    
  }
  
  
  
  /*
  *   user interaction functions for setting the various selection tools
  */
  
  private PVector getOffsetMousePos(){
   
   PVector offsetMouse = new PVector(mouseX, mouseY);
   offsetMouse.sub(imageOffset);
   return offsetMouse; 
    
  }
  
  public void mousePressed(){
    if(textureImage != null){
               
      PVector mousePos = getOffsetMousePos();
      
      if(!useTriSelect)
        marqueeSelect(mousePos);
      else
        triSelection(mousePos);
         
    }
  }
  
  private void marqueeSelect(PVector mousePos){
    
   if(mouseButton == LEFT){
        if(PVector.dist(textureClipRectBR, mousePos) < controlHandleRadius){
          resizing = true;
          dragging = false; 
        }else if(mousePos.x > textureClipRectTL.x && mousePos.y > textureClipRectTL.y && mousePos.x < textureClipRectBR.x && mousePos.y < textureClipRectBR.y){
          dragging = true;
          resizing = false;
          mouseDragOffsetTL = new PVector(mousePos.x - textureClipRectTL.x, mousePos.y - textureClipRectTL.y);
          mouseDragOffsetBR = new PVector(mousePos.x - textureClipRectBR.x, mousePos.y - textureClipRectBR.y);
        }
      }else{
        if(mousePos.x > 0 && mousePos.y > 0 && mousePos.x < previewImage.width && mousePos.y < previewImage.height ){
          
          setTextureClipRect(mousePos, mousePos);
          constrainTextureClipRect();

          resizing = true;
          dragging = false; 
        }
      } 
  }
  
  private void triSelection(PVector mousePos){
    for(int i = 0; i < triangularSelection.length; ++i){
      if(PVector.dist(triangularSelection[i], mousePos) < controlHandleRadius){
        triSelectedCorner = i;
      }
    }
  }
  
  public void mouseDragged(){
    if(textureImage != null){
      
      PVector mousePos = getOffsetMousePos();

      if(!useTriSelect)
        dragMarquee(mousePos);
      else
        dragTriangleSelection(mousePos);
       
    }
  }
  
  private void dragMarquee(PVector mousePos){
      
    if(dragging){
        
        setTextureClipRect(PVector.sub(mousePos, mouseDragOffsetTL), PVector.sub(mousePos, mouseDragOffsetBR));
        
    }else if(resizing){
        
        setTextureClipRect(textureClipRectTL, mousePos);
      
    }

    constrainTextureClipRect();

    clipBoxWidth = (int)abs(textureClipRectTL.x-textureClipRectBR.x);
    clipBoxHeight = (int)abs(textureClipRectTL.y-textureClipRectBR.y);
    clipBoxX = (int)min(textureClipRectTL.x,textureClipRectBR.x);
    clipBoxY = (int)min(textureClipRectTL.y,textureClipRectBR.y);

  }
  
  private void dragTriangleSelection(PVector mousePos){
      
    if(triSelectedCorner != -1){
      triangularSelection[triSelectedCorner] = mousePos.get();
      constrainTriCorner(triangularSelection[triSelectedCorner]);
    }
    
  }
  
  public void mouseReleased(){
    resizing = false;
    dragging = false;
    triSelectedCorner = -1;
  }
  
 
  
  
  /*
  *   helper function to set marquee tool (clipRect) 
  */
  
  private void setTextureClipRect(PVector i_TL, PVector i_BR){
    textureClipRectTL = i_TL;
    textureClipRectBR = i_BR;
  }
  
  
  /*
  *   the following two functions limit the selection to be within the image's bounds 
  */
  
  private void constrainTextureClipRect(){
    
   textureClipRectTL.x = max(textureClipRectTL.x, 0);
   textureClipRectTL.y = max(textureClipRectTL.y, 0);
   textureClipRectTL.x = min(this.width-imageOffset.x-(textureClipRectBR.x - textureClipRectTL.x), textureClipRectTL.x);
   textureClipRectTL.y = min(this.height-imageOffset.y-(textureClipRectBR.y - textureClipRectTL.y), textureClipRectTL.y);
    
   textureClipRectBR.x = max(textureClipRectBR.x, textureClipRectTL.x+10);
   textureClipRectBR.y = max(textureClipRectBR.y, textureClipRectTL.y+10);
   textureClipRectBR.x = min(previewImage.width, textureClipRectBR.x);
   textureClipRectBR.y = min(previewImage.height, textureClipRectBR.y);
   
  }
  
  private void constrainTriCorner(PVector i_corner){
     i_corner.x = min(i_corner.x, previewImage.width); 
     i_corner.x = max(i_corner.x, 0);  
     i_corner.y = min(i_corner.y, previewImage.height); 
     i_corner.y = max(i_corner.y, 0);  
  }
  
  
  
  
  /*
  *   randomizes the current marquee selection and triggers pattern generation.
  */  
   
   public void randomizeMarqueeSelection(int value){
    
    float wdth = clipBoxWidth > 0 ? clipBoxWidth : random(previewImage.width);
    float hght = clipBoxHeight > 0 ? clipBoxHeight : random(previewImage.height);
    float x = random((previewImage.width)-wdth);
    float y = random((previewImage.height)-hght);
    

    clipBoxX = (int)(x);
    clipBoxY = (int)(y);

    
    setTextureClipRect(new PVector(x,y), new PVector(x+wdth, y+hght));
    
    parent.generate();

  }
  
 
 
  /*
  *   helper methods for getting the texture coords of various selection tools 
  */
  
  private PVector[] getMarqueeTexCoords(){
   
   PVector[] out = {
     new PVector(0, 1),
     new PVector(1, 1),
     new PVector(1, 0)
   };
    
   return out;
 
  } 
  
  private PVector[] getTriTexCoords(){
   PVector[] out = new PVector[3];
   
   out[0] = new PVector(triangularSelection[0].x/previewImage.width, triangularSelection[0].y/previewImage.height);
   out[1] = new PVector(triangularSelection[1].x/previewImage.width, triangularSelection[1].y/previewImage.height);
   out[2] = new PVector(triangularSelection[2].x/previewImage.width, triangularSelection[2].y/previewImage.height);
    
   return out;
   
  } 
  
  
  
  /*
  *   loadImageEvent is triggered by the loadImage gui button, loadTextureImage is the callback to the filepicker
  */
  
  public void loadImageEvent(int i_GUIJunk) {
    println("loading image");
    selectInput("Select an image", "loadTextureImage");
    
  }
  
  public void loadTextureImage(File selection) {
    
    if (selection == null) {
      println("Window was closed or the user hit cancel.");
    } 
    else {
      
  
      String path = selection.getAbsolutePath();
      String suffix = path.substring(path.length()-3);
  
      // if the file is svg, load the image and initialize svg controls
      if (suffix.equals("svg")) {
    	  svgInit = false;
    	//  svgInit = false;// reset to false, in case an svg was already loaded
        PShape iFile =  loadShape(path);
        svgTile = new SVGLoader(iFile, svgScale);
        SVGlistSize = svgTile.getChildrenNum();
        
        createGuiControlsSVG(SVGlistSize);        
        chosenImage = svgTile.drawRaw();         
      }
      else {
        chosenImage = loadImage(path);
       if(svgList!=null)// remove the svg controls if they exist
        removeGuiControlsSVG();
      }
  setImage(chosenImage);

    }
  }
  
  
  /*
  *   controlEvent() registers changes to the GUI number sliders and changes the selection box accordingly
  */
  
  public void controlEvent(ControlEvent theEvent) {
	  //strokeWeightBox update has to go here - behave differently than buttons
	 if(theEvent.isFrom(strokeWeightBox)){
	  CatsEye.p5.println("what"); // establish layer
	    svgTile.setStrokeWeight(sw, currentSvgChild);
  	    chosenImage = svgTile.updateSvgView(); // get updated SVG
 	    setImage(chosenImage); // draw updated svg
	 }
	  // check for cp5 event coming from the svgList dropdown menu
	  // init greyscale here, because original svg color is destroyed when the controls are activated
	  if (theEvent.isFrom(svgList)){
		  //initialize - grayscale image and buttons
		  if(svgInit == false){
		    	 currentSvgChild = (int)(theEvent.getGroup().getValue()); // establish layer
				 chosenImage = svgTile.init(); // get the original SVG
				 // setup SVG controls
				 bgColorButton.setColorBackground((int)(svgTile.getBGcolor()));
			     final Color ff = svgTile.getFillColor(currentSvgChild);
			     final int tff = CatsEye.p5.color(ff.getRed(), ff.getGreen(), ff.getBlue());
				 fillColorButton.setColorBackground((int)(tff));
				 final Color sf = svgTile.getStrokeColor(currentSvgChild);
			     final int tsf = CatsEye.p5.color(sf.getRed(), sf.getGreen(), sf.getBlue());
				  strokeColorButton.setColorBackground((int)(tsf));
		    	 setImage(chosenImage); // draw the original svg
		    	 svgInit = true;
		  }else{ // for resetting dropdown menu and buttons based on child layer color
		    	 currentSvgChild = (int)(theEvent.getGroup().getValue()); // establish layer
				 //based on the dropdown, get and set the current layer values for the svg gui 
		    	 bgColorButton.setColorBackground((int)(svgTile.getBGcolor()));
			     final Color ff = svgTile.getFillColor(currentSvgChild);
			     final int tff = CatsEye.p5.color(ff.getRed(), ff.getGreen(), ff.getBlue());
				 fillColorButton.setColorBackground((int)(tff));
				 final Color sf = svgTile.getStrokeColor(currentSvgChild);
			     final int tsf = CatsEye.p5.color(sf.getRed(), sf.getGreen(), sf.getBlue());
				  strokeColorButton.setColorBackground((int)(tsf));
		  }
	    	
	  }else if(theEvent.isFrom(triSelectBtn)){
      defaultTriangularSelection();
    } 
  }

  

  
  private void defaultTriangularSelection(){
   
   triangularSelection[0] = new PVector(textureClipRectTL.x, textureClipRectTL.y);
   triangularSelection[1] = new PVector(textureClipRectTL.x, textureClipRectBR.y);
   triangularSelection[2] = new PVector(textureClipRectBR.x, textureClipRectBR.y);
    
  }
  
  //--------------------------------------------ACTUAL GUI CREATION--------------------------------------------------------
 

  @SuppressWarnings("deprecation")

  private void removeGuiControlsSVG(){
		if(svgList!=null){   
			svgList.remove();
			svgList = null;
	    	bgColorButton.remove();
	    	fillColorButton.remove();
	    	strokeColorButton.remove();
	    	strokeWeightBox.remove();
	    	svgControls = false;
		}
  }
  
  public void SVGfillColour() {
	    String t_fn = fillColorButton.getName();
	    svgTile.colorSelect(t_fn, currentSvgChild);
    	final Color ff = svgTile.getFillColor(currentSvgChild);
    	final int tff = CatsEye.p5.color(ff.getRed(), ff.getGreen(), ff.getBlue());
	    fillColorButton.setColorBackground((int)(tff));
	    chosenImage = svgTile.updateSvgView(); // get updated SVG
   	    setImage(chosenImage); // draw updated svg
	    }
  
  public void SVGstrokeColour() {
	  CatsEye.p5.println(strokeColorButton.getName());
	    String t_sn = strokeColorButton.getName();
	    svgTile.colorSelect(t_sn, currentSvgChild);
    	final Color fs = svgTile.getStrokeColor(currentSvgChild);
    	final int tfs = CatsEye.p5.color(fs.getRed(), fs.getGreen(), fs.getBlue());
	    strokeColorButton.setColorBackground((int)(tfs));
	    chosenImage = svgTile.updateSvgView(); // get updated SVG
   	    setImage(chosenImage); // draw updated svg
	    }

private void clearSvgData(){
	try{
	
    svgList.clear();
	} catch(ArrayIndexOutOfBoundsException e ) {
		System.out.println("caught svg error");
	}
	
    bgColorButton.setColorBackground(CatsEye.p5.color(7,38,62));
    fillColorButton.setColorBackground(CatsEye.p5.color(7,38,62));
    strokeColorButton.setColorBackground(CatsEye.p5.color(7,38,62));
}
  
private void createGuiControlsSVG(int t_listSize){
	//if (svgControls == false){
if(svgList != null){	
    CatsEye.p5.println(svgList != null);
	clearSvgData();
}else{
	 svgList = cp5.addDropdownList("SVGcontrol")
	            .setPosition(354, 41)
	            .setSize(75,200)
	            .setItemHeight(20)
		        .setBarHeight(20)
	            ;
	    svgList.captionLabel().style().marginTop = 5;
	    svgList.captionLabel().style().marginLeft = 3;
	    svgList.valueLabel().style().marginTop = 3;
	    svgList.scroll(0);
	    bgColorButton = cp5.addButton("bg")
	    	    .setPosition(430,20)
	    	     .setSize(20, 20)
	    	     ;
	    fillColorButton = cp5.addButton("fc")
	    	    .setPosition(452,20)
	    	     .setSize(20, 20)
	    	     .plugTo(this, "SVGfillColour");
	    
	    strokeColorButton = cp5.addButton("sc")
	    	    .setPosition(474,20)
	    	     .setSize(20, 20)
	    	     .plugTo(this, "SVGstrokeColour");
	    
	    strokeWeightBox = cp5.addNumberbox("sw")
	     .setPosition(498,20)
	     .setSize(26,20)
	     .setRange(0,10)
	     .setScrollSensitivity(40)
	     .setMultiplier(.1f)
	     .setValue(0)
	     ;

	} 
   // svgControls = true;
	for (int i=0;i<t_listSize;i++) {
		svgList.addItem("child "+i, i);
	}
	  
}

  @SuppressWarnings("deprecation")
private void createGuiControls(){
    cp5 = new ControlP5(this);
        
    cp5.addButton("LoadImage")
    .setPosition(20,20)
     .setSize(49, 20)
      .plugTo(this, "loadImageEvent");
    
    cp5.addButton("generate")
      .setPosition(77,20)
          .setSize(46, 20)
             .plugTo(parent,"generate");

    cp5.addButton("randomize")
    .setPosition(130,20)
     .setSize(49, 20)
      .plugTo(this, "randomizeMarqueeSelection");
   
    triSelectBtn = cp5.addButton("tri/rect")
    .setPosition(186,20)
    .setSize(45, 20)
    .plugTo(this, "toggleTriSelect")
    .hide();
    
    cp5.addButton("save crop")
    .setPosition(238,20)
     .setSize(49, 20)
     // .plugTo(this, "randomizeMarqueeSelection")
     ;
    
    cp5.addButton("load crop")
    .setPosition(295,20)
     .setSize(49, 20)
     // .plugTo(this, "randomizeMarqueeSelection")
     ;

    
  }
  
}
//--------------------------------------------------------------------------------------------------------------------------

