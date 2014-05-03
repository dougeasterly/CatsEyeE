package com.catseye.gui.windows;


import java.io.File;

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
  
  private float svgScale;
  
  private int clipBoxWidth, clipBoxHeight, clipBoxX, clipBoxY;
  private float scaleFactor;
  
  private Numberbox box_width, box_height, box_x, box_y;
  private float controlHandleRadius = 15; 
  
  private boolean dragging, resizing;
  private PVector mouseDragOffsetTL, mouseDragOffsetBR;
  
  private Toggle triSelectBtn;
  private boolean useTriSelect = false;
  private int triSelectedCorner;
  private PVector[] triangularSelection = {new PVector(0,0), new PVector(0,1), new PVector(1,1)};
  
  
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
    box_x.setValue(clipBoxX);
    box_y.setValue(clipBoxY);
    box_width.setValue(clipBoxWidth);
    box_height.setValue(clipBoxHeight); 
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
    box_x.setValue(clipBoxX);
    box_y.setValue(clipBoxY);
    
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
      
      PImage chosenImage; 
      String path = selection.getAbsolutePath();
      String suffix = path.substring(path.length()-3);
  
      if (suffix.equals("svg")) {
        PShape iFile =  loadShape(path);
        SVGLoader svgTile = new SVGLoader(iFile, svgScale);
        chosenImage = svgTile.drawTile();
      }
      else {
        chosenImage = loadImage(path);
      }
  
      this.setImage(chosenImage);
     
    }
  }
  
  
  /*
  *   controlEvent() registers changes to the GUI number sliders and changes the selection box accordingly
  */
  
  public void controlEvent(ControlEvent theEvent) {
    if(theEvent.isFrom(box_x) || theEvent.isFrom(box_y) || theEvent.isFrom(box_width) || theEvent.isFrom(box_height)) {
       setTextureClipRect(new PVector(clipBoxX,clipBoxY,0), new PVector(clipBoxX+clipBoxWidth,clipBoxY+clipBoxHeight,0));
       constrainTextureClipRect();
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
  
  private void createGuiControls(){
    cp5 = new ControlP5(this);
        
    cp5.addButton("LoadImage")
    .setPosition(20,20)
      .plugTo(this, "loadImageEvent");
    
    cp5.addButton("generate")
    .plugTo(parent,"generate")
      .setPosition(100,20);
    
    cp5.addButton("randomize")
    .setPosition(180,20)
      .plugTo(this, "randomizeMarqueeSelection");
    
    box_width = cp5.addNumberbox("box_width")
    .setPosition(280, 20)
      .setSize(45, 14)
        .setScrollSensitivity(1.1f)
          .setValue(0)
            .setRange(0,10000)
              .plugTo(this,"clipBoxWidth");
            
              
    box_height = cp5.addNumberbox("box_height")
    .setPosition(350, 20)
      .setSize(45, 14)
        .setScrollSensitivity(1.1f)
          .setValue(0)
            .setRange(0,10000)
              .plugTo(this,"clipBoxHeight");
              
    box_x = cp5.addNumberbox("box_x")
    .setPosition(450, 20)
      .setSize(45, 14)
        .setScrollSensitivity(1.1f)
          .setValue(0)
            .setRange(0,10000)
               .plugTo(this,"clipBoxX");

    box_y = cp5.addNumberbox("box_y")
    .setPosition(520, 20)
      .setSize(45, 14)
        .setScrollSensitivity(1.1f)
          .setValue(0)
            .setRange(0,10000)
              .plugTo(this,"clipBoxY"); 
              
    triSelectBtn = cp5.addToggle("useTriSelect")
      .setPosition(this.width-60, this.height-60)
        .setSize(20, 20)
          .hide();
    
  }
  
}
//--------------------------------------------------------------------------------------------------------------------------

