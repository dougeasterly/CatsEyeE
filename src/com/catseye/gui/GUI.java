package com.catseye.gui;

import processing.core.*;

import com.catseye.CatsEye;
import com.catseye.gui.windows.GUIWindowManager;
import com.catseye.gui.windows.ImageSelectionApp;
import com.catseye.gui.windows.VoronoiDelaunayApp;
import com.catseye.gui.windows.GridSelectionApp;
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

  GUIWindowManager windowManager;
	
  public int currentGridType;
  
  Frame ImageWindowFrame, vdGUIFrame;
  
  private ControlP5 cp5;
  
  private ImageSelectionApp imageWindow;
  private VoronoiDelaunayApp voronoiDelaunayWindow;
  private GridSelectionApp gridSelector;
  
  RadioButton gridTypeButton;
  Textfield printWidthField, printHeightField;
  Button maskImageBtn;
  Group globalControls, gridControls;
  
  private int printWidthValue = 1000;
  private int printHeightValue = 1000;
  private float cellRadius = 100;
  private boolean drawGrid = false;
  private boolean triggerGeneration = false;
  
  private PImage maskImage, backgroundImage;
  
  private TileGrid gridGenerator;
  private VoronoiDelaunayGrid irregularGridGenerator;
	
	
  
  
  
  
  //-----------------------------------PUBLIC METHODS--------------------------------------
  
  
  /*
  *   initialize GUI
  */
  
  public GUI() {
    cp5 = new ControlP5(CatsEye.p5);
  
    globalControlGroup();
    
	gridGenerator = new HexGrid();
	irregularGridGenerator = new VoronoiDelaunayGrid();
    
	imageWindow = new ImageSelectionApp(this, 600, 600);
	voronoiDelaunayWindow = new VoronoiDelaunayApp(this, 600, 600);
	gridSelector = new GridSelectionApp(this, 600, 600);
	
    windowManager = new GUIWindowManager();
    windowManager.addWindow("image selection", imageWindow, true);
    windowManager.addWindow("grid selection", gridSelector, true);
    windowManager.addWindow("voronoi controls", voronoiDelaunayWindow);
    
    toggleWindows(0);
    
    backgroundImage = createCheckerBackground();
    
  }
  
 
  /*
  *   draw GUI and images (main draw function)
  */
  
  public void drawGui(){
    
    update();
    
    CatsEye.p5.image(backgroundImage, 0, 0);
    PImage patternImage = gridGenerator.getPreviewImage();
    CatsEye.p5.image(patternImage, (CatsEye.p5.width-patternImage.width)/2,  (CatsEye.p5.height-patternImage.height)/2);
   
    if(drawGrid){
      PImage gridImage = gridGenerator.getGridImage();  
      CatsEye.p5.image(gridImage, (CatsEye.p5.width-gridImage.width)/2,  (CatsEye.p5.height-gridImage.height)/2);
    }
   
    CatsEye.p5.fill(180, 200);
    CatsEye.p5.noStroke();
    CatsEye.p5.rect(0,490, 160, CatsEye.p5.height-490);
    
    PImage previewImage = gridGenerator.getUnitImage();
    CatsEye.p5.image(previewImage, 20, 500);
    
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
  *   set whether to use a mask or not (1=use mask, 0=dont use. not boolean due to controlP5 limitations). 
  */
  
  public void useMask(int i_useMask){
    gridGenerator.useMask(i_useMask==1);
  }
  
  
  
  /* 
  *   set missing odds (chance a grid tile will not be drawn)
  */
  
  public void missingOdds(float i_odds){
   gridGenerator.setMissingOdds(i_odds); 
  }
  
  
  
  /* 
  *   returns current render mode. possible values are currently JAVA2D or P2D.
  */
  
  public String getRenderMode(){
    return gridGenerator.getRenderMode();
  }
  
  
  
  /* 
  *   image save. Images are saved in nested folders ordered by date and time
  */
  
  String[] months = {
      "january", "febuary", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"
  };
  
  public void saveImage(int guiJunk) { 
    String path = "images/"+PApplet.year()+"/"+months[PApplet.month()-1]+"/"+PApplet.day()+"/images/"+PApplet.hour()+"_"+PApplet.minute()+"_"+PApplet.second()+"--"+GridType.typeArray[0];
    gridGenerator.saveImage(path, ".png", drawGrid);
  }  
  
  
  /* 
  *   tile save. Images are saved in nested folders ordered by date and time
  */
  
  public void saveTile() { 
    
	String path = "images/"+PApplet.year()+"/"+months[PApplet.month()-1]+"/"+PApplet.day()+"/tiles/"+PApplet.hour()+"_"+PApplet.minute()+"_"+PApplet.second()+"--"+GridType.typeArray[0];
    gridGenerator.saveTile(path+".png");

  } 
  
  
  
  /*
  *   choose grid type. current possibilities are HEXAGONGRID, TRIANGLEGRID, SQUAREGRID, VORONOIGRID, or DELAUNAYGRID
  */
  
  public void changeGridType(int i_type) {
  
    toggleWindows(i_type);
    
    switch(i_type) {
    case 0:
      gridGenerator = new HexGrid(gridGenerator);
      break;
  
    case 1:
      gridGenerator = new TriGrid(gridGenerator);
      break;
  
    case 2:
      gridGenerator = new SquareGrid(gridGenerator);
      break;
      
    case 3:
      irregularGridGenerator = new VoronoiDelaunayGrid(gridGenerator);
      gridGenerator = irregularGridGenerator;
      irregularGridGenerator.setType(irregularGridGenerator.VORONOI);
      voronoiDelaunayWindow.setGridType(GridType.VORONOI);
      imageWindow.showTriSelectButton();
      break;

    case 4:
      irregularGridGenerator = new VoronoiDelaunayGrid(gridGenerator);
      gridGenerator = irregularGridGenerator;
      irregularGridGenerator.setType(irregularGridGenerator.DELAUNAY);
      voronoiDelaunayWindow.setGridType(GridType.DELAUNAY);
      imageWindow.showTriSelectButton();
      break;
  
    default:
      gridGenerator = new HexGrid(gridGenerator);
      break;
    }
   
    currentGridType = i_type;
    
  }
  
  
  
  
  /*
  *   data update function
  */
  
  public void update(){
    
    if(triggerGeneration){
      
       if(currentGridType == 3 || currentGridType == 4){
         irregularGridGenerator.clearPoints();
         irregularGridGenerator.addNormalizedPoints(gridGenerator.getRenderSize(), voronoiDelaunayWindow.getNormalizedPoints());
       }
       
       generatePattern(); 
    }
   
  }
  
  

  /*
  *   open mask selection filepicker
  */
  
  public void loadMask(int val) {
	File file = new File("");
    CatsEye.p5.selectInput("Select an image", "maskSelected", file, this);
  }
  
  
  
  //--------------------------------------PRIVATE METHODS----------------------------------------
  //although some of these are public due to controlP5 needs or otherwise, they shouldn't be used 
  
  
  
  
  /*
  *   callback function for mask selection filepicker
  */
  
  public void maskSelected(File selection) {
    if (selection == null) {
      System.out.println("Window was closed or the user hit cancel.");
    } 
    else {
      maskImage = CatsEye.p5.loadImage(selection.getAbsolutePath());
      gridGenerator.setMask(maskImage);
    }
  
    PImage tempImg = maskImage.get();
    if (tempImg.width > tempImg.height)
      tempImg.resize(120, 0);
    else
      tempImg.resize(0, 120);
  
    maskImageBtn.setImage(tempImg);
  }  
  
  

  
  /*
  *   draws a grey and white checker pattern to a PGraphics object
  */
  
  private PImage createCheckerBackground(){
   
    int checkerCount = 100;
    PGraphics checkerGfx = CatsEye.p5.createGraphics(CatsEye.p5.width, CatsEye.p5.height);
    
    float boxWidth = CatsEye.p5.width/(checkerCount+0.0f);
    
    checkerGfx.beginDraw();
    checkerGfx.background(255);
    checkerGfx.fill(200);
    checkerGfx.noStroke();
    
    for(int i = 0; i < checkerCount; ++i){
      for(int j = 0; j < checkerCount; ++j){
       if((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)){
         checkerGfx.rect(i*boxWidth, j*boxWidth, boxWidth, boxWidth);
       }
      }
    }
    
    checkerGfx.endDraw();  
    
    return checkerGfx;
  }
  
  
  
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
  
  
  private void toggleWindows(int i_type){
   
   switch(i_type){
    case 0:
    case 1:
    case 2:
        windowManager.setVisible("voronoi controls",false);
    break;
    
    case 3:
    case 4:
    	windowManager.setVisible("voronoi controls",true);
    	break;
    default:
    break;
    
   } 
    
  }
  
  
  
  //------------------------------------------ACTUAL GUI CREATION------------------------------------------
  
  void globalControlGroup() {
        
    cp5.addToggle("drawGrid")
      .setPosition(1000-40, 20)
        .setSize(20, 20)
           .plugTo(this);

  
    globalControls = cp5.addGroup("globalControls")
      .setPosition(0, 10)
        .setBackgroundHeight(210)
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
      .setPosition(20, 600)
        .setSize(50, 20)
        .plugTo(this,"saveTile");

        
    cp5.addButton("save Image")
      .setPosition(90, 600)
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

