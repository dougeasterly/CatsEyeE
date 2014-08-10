package com.catseye.gui.guiPanes;

import java.io.File;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import com.catseye.CatsEyeController;
import com.catseye.gui.components.ImageSelectionTool;
import com.catseye.gui.components.ImageSelectionWidget;
import com.catseye.gui.p5Plugs.ImageSelectionControls;
import com.quickdrawProcessing.display.DisplayPane;
import com.quickdrawProcessing.display.Stage;

public class ImageSelectionPane extends DisplayPane {

	ImageSelectionControls ctls;
	ImageSelectionTool selector;
	
	public ImageSelectionPane(PVector i_position, PVector i_size) {
		super(i_position, i_size);
	}
	
	@Override
	protected void addedToStage(){
		ctls = new ImageSelectionControls(this, Stage.cp5);
	}
	
	public void draw(PGraphics i_context){
		PGraphics currentContext = preDraw(i_context);
			currentContext.fill(clearColor);
			currentContext.noStroke();
			currentContext.rect(0, 0, size.x, size.y);
		postDraw(currentContext);
	}

	public void loadImage(){
		File imageFile = new File("");
	    Stage.p5.selectInput("select image", "loadTextureImage", imageFile, this);
	}

	public void loadTextureImage(File selection) {
	    
	    if (selection == null) {
	      System.out.println("Window was closed or the user hit cancel.");
	    } 
	    else {
	  
	      String path = selection.getAbsolutePath();
	      PImage chosenImage = Stage.p5.loadImage(path);
	      
	      removeChild(selector);
	      
	      selector = new ImageSelectionTool(new PVector(0,70), new PVector(size.x, size.y-70), chosenImage, ImageSelectionWidget.MARQUEE);
	      
	      if(!containsChild(selector))
	    	  addChild(selector);
	    }
	  }
	
	public void generate(){
		interactionHandler.actionHook(this,  CatsEyeController.GENERATE);
	}
	
	public PImage getTexture(){
		return selector.getImage();
	}
	
	public PVector[] getTexCoords(){
		return selector.getTextureCoordinates();
	}
	
	
}
