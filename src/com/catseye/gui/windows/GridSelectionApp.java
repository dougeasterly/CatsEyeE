package com.catseye.gui.windows;

import java.io.File;

import com.catseye.CatsEye;
import com.catseye.gui.GUI;
import com.catseye.gui.components.selectionPanes.GridSelectionPane;
import com.catseye.gui.components.selectionPanes.StandardSelectionPane;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.catseye.patternComponents.gridGenerators.regularGrids.HexGrid;

import processing.core.*;
import processing.event.MouseEvent;
import controlP5.*;

public class GridSelectionApp extends GUIApp {
	
	 private ControlP5 cp5;
	
	 protected GridSelectionPane[] gridSelections;
	 protected GridSelectionPane currentGridSelection;

	 protected float cellSize;
	 protected boolean useMask;
	 protected float missingOdds;
	 
	 protected Button maskImageButton;
	 protected TileGrid currentGrid;
	 
	 protected boolean scrolling;

	 //------------------------------CONSTRUCTORS/SETUP-----------------------------------
	  
	  @SuppressWarnings("unused")
	  private GridSelectionApp(){
		  
	  }
	
	  public GridSelectionApp(GUI i_Parent, int i_width, int i_height) {
	    this.appWidth = i_width;
	    this.appHeight = i_height;
	    scrolling = false;
	  }
	
	  public void setup() {
	    size(this.appWidth, this.appHeight);
	    frameRate(25);
	    createGuiControls();
	   
	    currentGrid = new HexGrid();
	    
	  }
	  
	  public TileGrid getTileGrid(){
		  return currentGrid;
	  }
	  
	  public void draw(){
		  
		   	background(255);
		   	
		   	pushMatrix();
		   	translate(0,180);
		   	currentGridSelection.drawButtons();
		   	popMatrix();
		   	
		   	noStroke();
		   	fill(255);
		   	rect(0,0,appWidth, 180);
	  }
	  

	  
	  /*
	   *   open mask selection filepicker
	   */
	   
	  public void loadMask(int GUI_JUNK) {
	 	File file = new File("");
	    CatsEye.p5.selectInput("Select an image", "maskSelected", file, this);
	   }
	   
	   
	   
	   //--------------------------------------PRIVATE METHODS----------------------------------------
	   //although some of these are public due to controlP5 needs or otherwise, they shouldn't be used 
	   
	   
	   
	   
	   /*
	   *   callback function for mask selection filepicker
	   */
	   
	   public void maskSelected(File selection) {
		 PImage maskImage;
		   
		 if (selection == null) {
	       System.out.println("Window was closed or the user hit cancel.");
		 } 
	     else {
	       maskImage = CatsEye.p5.loadImage(selection.getAbsolutePath());
	       currentGrid.setMask(maskImage);
	       
	       PImage tempImg = maskImage.get();
		     if (tempImg.width > tempImg.height)
		       tempImg.resize(80, 0);
		     else
		       tempImg.resize(0, 80);
		   
		     maskImageButton.setImage(tempImg);
	       
	     }
	     
	   }  
	  
	//--------------------------------------PRIVATE METHODS----------------------------------------
	//although some of these are public due to controlP5 needs or otherwise, they shouldn't be used 
	  
	   
	   
	   
	   private void setCellSize(float i_cellSize){
		   cellSize = i_cellSize;
		   currentGrid.setCellRadius(cellSize);
	   }
	   
	   private void setUseMask(boolean i_useMask){
		   useMask = i_useMask;
		   currentGrid.useMask(useMask);
	   }

	   private void setMissingOdds(float i_missingOdds){
		   missingOdds = i_missingOdds;
		   currentGrid.setMissingOdds(missingOdds);
	   }
	   
	   public void mouseWheel(MouseEvent e){
		  currentGridSelection.scroll(new PVector(0, e.getAmount()*6));
	   }
	  
	   public void mousePressed(){
		  
		  String selectedGrid = currentGridSelection.click(new PVector(mouseX, mouseY - 180));
		  println(selectedGrid);
		  
		  if(selectedGrid != null){
			  currentGrid = TileGrid.getGridFromClassString(selectedGrid, currentGrid);
		  }else{
			  scrolling = true;
		  }
		  
	   }

	   public void mouseDragged(){
		  if(scrolling){
			  currentGridSelection.scroll(new PVector(0, mouseY - pmouseY));
		  }
	   }
	  
	   public void mouseReleased(){
			  scrolling = false;
	   }
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	private void createGuiControls(){
		  
		  cp5 = new ControlP5(this);

		  cp5.getTab("default")
		     .activateEvent(true)
		     .setLabel("standard")
		     .setSize(appWidth/4, 20)
		     .setId(1);
		  
		  cp5.addTab("irregular")
		     .activateEvent(true)
		     .setSize(appWidth/4, 20)
		     .setId(2);
		  
		  cp5.addTab("gridIndex")
		  	 .setLabel("grid index")
		  	 .setSize(appWidth/4, 20)
		     .activateEvent(true)
		     .setId(2);
		  
		  cp5.addTab("custom")
		  	 .setLabel("custom")
		  	 .setSize(appWidth/4, 20)
		     .activateEvent(true)
		     .setId(2);
		  
		 createGlobalControls();
		 currentGridSelection = new StandardSelectionPane(this, new PVector(this.appWidth, this.appHeight-180), cp5);
	}
	
	private void createGlobalControls(){
	
	  cp5.addGroup("globalControls")
	     .setPosition(0, 30)
	  	 .setLabel("global controls")
	  	 .setWidth(this.width/2)
	  	 .setBackgroundHeight(150)
	  	 .setBackgroundColor(color(0, 90))
	  	 .moveTo("global");
	  
	  cp5.addNumberbox("cellsize")
	  	.setPosition(20, 20)
	  	.setSize(80, 20)
	  	.setLabel("Cell Size")
	  	.setMin(10)
	  	.setMax(1000)
	  	.setValue(150)
	  	.setGroup("globalControls")
	  	.plugTo(this, "setCellSize");
	  
	  cp5.addSlider("missingOdds")
	     .setPosition(120, 20)
	     .setSize(80, 20)
	     .setRange(0, 1)
	     .setGroup("globalControls")
	     .plugTo(this, "setMissingOdds");
	  
	  cp5.addToggle("useMask")
	  	.setPosition(20, 60)
	  	.setSize(20, 20)
	  	.setGroup("globalControls")
	  	.plugTo(this, "setUseMask");
	  
	  maskImageButton = cp5.addButton("loadMask")
		.setPosition(120, 60)
		.setSize(80, 80)
		.setGroup("globalControls")
		.plugTo(this, "loadMask");
		
	}
	

	  
	
}
