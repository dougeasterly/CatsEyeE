package com.catseye.gui;
/*---------------------------------------------------------------------------------------------
*
*    GUI FILE, due to limitations with the processing IDE this hasn't been put into a class as of yet
*    
*    creates GUI and controls 
*
*    Ben Jack 12/4/2014 
*
*---------------------------------------------------------------------------------------------*/


import processing.core.*;

import com.catseye.CatsEye;
import com.catseye.patternComponents.gridGenerators.*;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.io.File;

import controlP5.*;


  
public class GUI{
  
  //--------------------GLOBAL VARIABLES-------------------

  public int currentGridType;
  
  Frame ImageWindowFrame, vdGUIFrame;
  
  private ControlP5 cp5;
  private ImageSelectionWindow imageWindow;
  private VoronoiDelaunayGUIWindow voronoiDelaunayWindow;
  
  RadioButton gridTypeButton;
  Textfield printWidthField, printHeightField;
  Button maskImageBtn;
  Group globalControls, gridControls;
  
  private int printWidthValue = 1000;
  private int printHeightValue = 1000;
  private float cellRadius = 100;
  private boolean drawGrid = false;
  private boolean triggerGeneration = false;
  
  private PImage textureImage, maskImage, backgroundImage;
  
  private TileGrid gridGenerator;
  private VoronoiDelaunayGrid irregularGridGenerator;
	
	
  
  
  
  
  //-----------------------------------PUBLIC METHODS--------------------------------------
  
  
  /*
  *   initialize GUI
  */
  
  public GUI() {
    cp5 = new ControlP5(CatsEye.p5);
  
    gridControlGroup();
    globalControlGroup();
  
    imageWindow = addImageWindow("patternImage", 600, 600);
    voronoiDelaunayWindow = addVdGUIWindow("voronoi controls", 600, 600);
    toggleWindows(0);
    
    backgroundImage = createCheckerBackground();
    
	gridGenerator = new HexGrid();
	irregularGridGenerator = new VoronoiDelaunayGrid();
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
  *   creates image selection GUI window
  */

  private ImageSelectionWindow addImageWindow(String theName, int theWidth, int theHeight) {
    ImageWindowFrame = new Frame(theName);
    ImageSelectionWindow p = new ImageSelectionWindow(this, theWidth, theHeight);
    ImageWindowFrame.add(p);
    p.init();
    ImageWindowFrame.setTitle(theName);
    ImageWindowFrame.setSize(theWidth, theHeight);
    ImageWindowFrame.setLocation(0, 0);
    ImageWindowFrame.setResizable(false);
    ImageWindowFrame.setVisible(true);
    return p;
  }
  
  
  /*
  *   creates voronoi/delaunay grid interface window
  */

  private VoronoiDelaunayGUIWindow addVdGUIWindow(String theName, int theWidth, int theHeight) {
    vdGUIFrame = new Frame(theName);
    VoronoiDelaunayGUIWindow p = new VoronoiDelaunayGUIWindow(this, irregularGridGenerator, theWidth, theHeight);
    vdGUIFrame.add(p);
    p.init();
    vdGUIFrame.setTitle(theName);
    vdGUIFrame.setSize(theWidth, theHeight);
    vdGUIFrame.setLocation(0, 650);
    vdGUIFrame.setResizable(false);
    vdGUIFrame.setVisible(true);
    return p;
  }
  
  
  
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
   
    printWidthField.submit();
    printHeightField.submit();
    gridGenerator.setTexture(imageWindow.getCropSection());
    gridGenerator.setTextureCoords(imageWindow.getTextureCoords());
    gridGenerator.setCellRadius(cellRadius);
  
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
        vdGUIFrame.setVisible(false);
    break;
    
    case 3:
    case 4:
        vdGUIFrame.setVisible(true);
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
  
    gridTypeButton = cp5.addRadioButton("changeGridType")
      .setPosition(20, 80)
        .setSize(10, 10)
          .setItemsPerRow(3)
            .setSpacingColumn(30)
              .setSpacingRow(20)
                .addItem("Hex", 0)
                  .addItem("Tri", 1)
                    .addItem("Square", 2)
                      .addItem("Voronoi", 3)
                        .addItem("Delaunay", 4)
                          .setGroup(globalControls)
                             .plugTo(this);

  
    gridTypeButton.activate(0);
  
    cp5.addNumberbox("cellRadius")
      .setPosition(20, 140)
        .setSize(50, 14)
          .setScrollSensitivity(1.1f)
            .setValue(100)
              .setRange(10, 5000)
                .setGroup(globalControls)
                   .plugTo(this);

  
    cp5.addNumberbox("svgScale")
      .setPosition(90, 140)
        .setSize(50, 14)
          .setRange(0.1f, 50)
            .setMultiplier(0.1f)
              .setScrollSensitivity(0.1f)
                .setValue(1.0f)
                  .setGroup(globalControls)
                     .plugTo(this);

  
    cp5.addButton("generate")
      .setPosition(20, 180)
        .setSize(50, 20)
          .setGroup(globalControls)
             .plugTo(this);

  
    cp5.addButton("saveImage")
      .setPosition(90, 180)
        .setSize(50, 20)
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
  
  
  //GRID GUI SETUP
  void gridControlGroup() {
  
    gridControls = cp5.addGroup("gridControls")
      .setPosition(0, 230)
        .setBackgroundHeight(250)
          .setWidth(160)
            .setBackgroundColor(CatsEye.p5.color(0, 90));
  
  
    cp5.addSlider("missingOdds")
      .setPosition(20, 20)
        .setSize(80, 20)
          .setRange(0, 1)
            .setGroup(gridControls)
            	.plugTo(this);
  
  
    cp5.addToggle("useMask")
      .setPosition(20, 60)
        .setSize(20, 20)
          .setGroup(gridControls)
          	.plugTo(this);
  
  
    maskImageBtn = cp5.addButton("loadMask")
      .setPosition(20, 100)
        .setSize(120, 120)
          .setGroup(gridControls)
          	.plugTo(this);
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

