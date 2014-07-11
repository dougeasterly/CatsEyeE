package com.catseye.gui;

import processing.core.*;
import processing.data.JSONObject;

import com.catseye.CatsEye;
import com.catseye.gui.components.SavedStateBar;
import com.catseye.gui.displayTreeObjects.PaneSplitter;
import com.catseye.gui.displayTreeObjects.Stage;
import com.catseye.gui.guiPanes.GUIWindowManager;
import com.catseye.gui.guiPanes.GridSelectionApp;
import com.catseye.gui.guiPanes.ImageDisplayPane;
import com.catseye.gui.guiPanes.ImageSelectionApp;
import com.catseye.gui.guiPanes.VoronoiDelaunayApp;
import com.catseye.patternComponents.gridGenerators.*;
import com.catseye.patternComponents.gridGenerators.irregularGrids.VoronoiDelaunayGrid;
import com.catseye.patternComponents.gridGenerators.regularGrids.GridType;
import com.catseye.patternComponents.gridGenerators.regularGrids.HexGrid;
import com.catseye.patternComponents.gridGenerators.regularGrids.SquareGrid;
import com.catseye.patternComponents.gridGenerators.regularGrids.TriGrid;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.io.File;

import controlP5.*;


  
public class GUI{
  
  //--------------------GLOBAL VARIABLES-------------------

  Stage mainStage;
  ImageDisplayPane imageDisplay;
 
  private TileGrid gridGenerator;
  
  

  GUIWindowManager windowManager;
	  
  Frame ImageWindowFrame, vdGUIFrame;
  
  private ControlP5 cp5;
  
  private ImageSelectionApp imageWindow;
  private VoronoiDelaunayApp voronoiDelaunayWindow;
  private GridSelectionApp gridSelector;
  
  Textfield printWidthField, printHeightField;
  Group globalControls, gridControls;
  
  private int printWidthValue = 1000;
  private int printHeightValue = 1000;

  private boolean triggerGeneration = false;
  
	
  private String savePath;
  
  
  
  
  //-----------------------------------PUBLIC METHODS--------------------------------------
  
  
  /*
  *   initialize GUI
  */
  
  public GUI() {
    cp5 = new ControlP5(CatsEye.p5);
  
    mainStage = new Stage();
    imageDisplay = new ImageDisplayPane(new PVector(0, 0), new PVector(Stage.p5.width, Stage.p5.height));
    mainStage.addChild(imageDisplay);
    
    globalControlGroup();
    
	gridGenerator = new HexGrid();
    
	imageWindow = new ImageSelectionApp(this, 600, 700);
	
//	SavedStateBar saveLoader = new SavedStateBar(imageWindow);
//	imageWindow.setSaveBar(saveLoader);
	
	voronoiDelaunayWindow = new VoronoiDelaunayApp(this, 600, 600);
	gridSelector = new GridSelectionApp(this, 600, 600);
	
    windowManager = new GUIWindowManager();
    windowManager.addWindow("image selection", imageWindow, true);
    windowManager.addWindow("grid selection", gridSelector, true);
    windowManager.addWindow("voronoi controls", voronoiDelaunayWindow);
        
    
    setupSavePath();
  }
  
  
  public TileGrid getGrid(){
	return gridSelector.getTileGrid();
  }
  
 
  /*
  *   draw GUI and images (main draw function)
  */
  
  public void drawGui(){
        
	update();
	  
	mainStage.draw();
   
    CatsEye.p5.fill(180, 200);
    CatsEye.p5.noStroke();
    CatsEye.p5.rect(0,104, 160, CatsEye.p5.height-104);
    
    PImage previewImage = gridGenerator.getUnitImage();
    CatsEye.p5.image(previewImage, 20, 124);
    
  }
  
  
  
  /*
  *   flag program to generate the pattern. This is seperated from
  *   the actual code to generate the pattern because openGL calls
  *   can only be in the main thread and the GUI button calls run in
  *   a second thread.
  */
  
  public void generate() { 
    triggerGeneration = true;
  }
  
  
  /* 
  *   returns current render mode. possible values are currently JAVA2D or P2D.
  */
  
  public String getRenderMode(){
    return gridGenerator.getRenderMode();
  }
  
  // Doug's addition
  public String toggleRenderMode(){
	 // CatsEye.p5.println("toggling");
	  
	  String t_renderMode = getRenderMode();
	 
	  if(t_renderMode.equals(PApplet.JAVA2D)){
		  gridGenerator.setRenderMode(PApplet.P2D); 
	      imageWindow.showTriSelectButton();
	      t_renderMode = PApplet.P2D;
	  }else{
		  gridGenerator.setRenderMode(PApplet.JAVA2D); 
		  imageWindow.hideTriSelectButton();
	      t_renderMode = PApplet.JAVA2D;
	  }
	  
	  return t_renderMode;
  }
  
  public void setRenderState(String i_renderMode){
	 // CatsEye.p5.println("toggling");
	 
	  if(i_renderMode.equals(PApplet.P2D)){
	      imageWindow.showTriSelectButton();
	      imageWindow.setRenderState(PApplet.P2D);
	  }else{
		  imageWindow.hideTriSelectButton();
		  imageWindow.setRenderState(PApplet.JAVA2D);
	  }
	  
  }
  
  
  /* 
  *   image save. Images are saved in nested folders ordered by date and time
  */
  
  private void setupSavePath(){
	 
	  String[] savePathFile = CatsEye.p5.loadStrings("SavePath.txt"); 
	  
	  if(savePathFile != null){
		  savePath = savePathFile[0];
	  }else{
		  CatsEye.p5.selectFolder("Select a folder to save images into:", "saveFolderSelected", null, this);
	  }
  }
  
  public void saveFolderSelected(File selection){
	     
	  if (selection == null) {
		CatsEye.p5.selectFolder("Select a folder to save images into:", "saveFolderSelected", null, this);
	  } else {
		String[] path = new String[1];
		path[0] = selection.getAbsolutePath();
		CatsEye.p5.saveStrings("SavePath.txt", path);
		
		savePath = path[0];
		System.out.println(savePath);
	  }
	  
  }
  
  String[] months = {
      "january", "febuary", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"
  };
  
  public void saveImage(int guiJunk) { 
    String path = savePath+"/images/"+PApplet.year()+"/"+months[PApplet.month()-1]+"/"+PApplet.day()+"/images/"+PApplet.hour()+"_"+PApplet.minute()+"_"+PApplet.second()+"--"+GridType.typeArray[0];
    gridGenerator.saveImage(path, ".png", drawGrid);
  }  
  
  
  /* 
  *   tile save. Images are saved in nested folders ordered by date and time
  */
  
  public void saveTile() { 
    
	String path = savePath+"/images/"+PApplet.year()+"/"+months[PApplet.month()-1]+"/"+PApplet.day()+"/tiles/"+PApplet.hour()+"_"+PApplet.minute()+"_"+PApplet.second()+"--"+GridType.typeArray[0];
    gridGenerator.saveTile(path+".png");

  } 
  
  

  /*
  *   open mask selection filepicker
  */
  
  public void loadMask(int val) {
	File file = new File("");
    CatsEye.p5.selectInput("Select an image", "maskSelected", file, this);
  }
  
  
  public void loadTileGridIntoGUI(TileGrid grid){
	  
	  imageWindow.setImage(grid.getTextureImage());
	  imageWindow.setTextureCoords(grid.getTextureCoords());
	  setRenderState(grid.getRenderMode());
	  printWidthField.setValue(grid.getRenderSize().x);
	  printHeightField.setValue(grid.getRenderSize().y);
	  gridSelector.setCellSize(grid.getCellRadius());
	  gridSelector.setMissingOdds(grid.getMissingOdds());
	  gridSelector.setGrid(grid);
	  
	  PImage mskImg = grid.getMaskImage();
	  
	  if(mskImg != null)
		  gridSelector.setMaskImage(mskImg);
	  
	  gridSelector.setUseMask(grid.getUseMask());
	  
	  generate();
  }
  
  
  
  //--------------------------------------PRIVATE METHODS----------------------------------------
  //although some of these are public due to controlP5 needs or otherwise, they shouldn't be used 
  
  

  
  /*
  *   draws a grey and white checker pattern to a PGraphics object
  */
  

  
  
  
  /*
  *   the function that actually generates the pattern. This is
  *   seperated from generate() because openGL calls can only be
  *   in the main thread and the GUI button calls run in a second thread.
  */
  
  private void generatePattern(){
   
	gridGenerator = gridSelector.getTileGrid();
	  
    printWidthField.submit();
    printHeightField.submit();
    gridGenerator.setTexture(imageWindow.getCropSection());
    gridGenerator.setTextureCoords(imageWindow.getTextureCoords());
  
    gridGenerator.generate();
    
    triggerGeneration = false;
    
    imageDisplay.setImages(gridGenerator.getRender(), gridGenerator.getGridImage());
    
  }
  
  /*
  *   helper functions to turn textbox input into integers
  */
  
  private void printWidth(String i_value) {
    printWidthValue = Integer.parseInt(i_value);  
    gridGenerator.setRenderSize(new PVector(printWidthValue, printHeightValue));
  }
  
  private void printHeight(String i_value) {
    printHeightValue = Integer.parseInt(i_value);
    gridGenerator.setRenderSize(new PVector(printWidthValue, printHeightValue));
  }
  
  private void update(){
	  if(triggerGeneration)
		  generatePattern();
  }
  
  
  //------------------------------------------ACTUAL GUI CREATION------------------------------------------
  
  void globalControlGroup() {
        
    cp5.addToggle("drawGrid")
      .setPosition(1000-40, 20)
        .setSize(20, 20)
           .plugTo(this);

  
    globalControls = cp5.addGroup("globalControls")
      .setPosition(0, 10)
        .setBackgroundHeight(94)
          .setWidth(160)
            .setBackgroundColor(CatsEye.p5.color(0, 90));
  
    printWidthField = cp5.addTextfield("printWidth")
      .setPosition(20, 20)
        .setSize(50, 14)
          .setValue("1000")
            .setAutoClear(false)
              .setGroup(globalControls)
                .setInputFilter(ControlP5.INTEGER)
                   .plugTo(this);
  
    printHeightField = cp5.addTextfield("printHeight")
      .setPosition(90, 20)
        .setSize(50, 14)
          .setValue("1000")
            .setAutoClear(false)
              .setGroup(globalControls)
                .setInputFilter(ControlP5.INTEGER)
                   .plugTo(this);

  
    cp5.addButton("generate")
      .setPosition(20, 54)
        .setSize(120, 20)
          .setGroup(globalControls)
             .plugTo(this);

  
    cp5.addButton("save Tile")
      .setPosition(20, 224)
        .setSize(50, 20)
        .plugTo(this,"saveTile");

        
    cp5.addButton("save Image")
      .setPosition(90, 224)
        .setSize(50, 20)
          .plugTo(this,"saveImage");
  }
  
  
  
  
  
  void keyPressed(){
	    
	   if(CatsEye.p5.key == 'r'){
	     
	     if(getRenderMode() == PApplet.P2D){
	      gridGenerator.setRenderMode(PApplet.JAVA2D); 
	      imageWindow.hideTriSelectButton();
	     }else{
	      gridGenerator.setRenderMode(PApplet.P2D); 
	      imageWindow.showTriSelectButton();
	     }
	     
	   }
	   
  }
  
  
}

