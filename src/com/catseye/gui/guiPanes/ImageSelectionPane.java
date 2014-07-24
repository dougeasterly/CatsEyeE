package com.catseye.gui.guiPanes;

import java.io.File;

import processing.core.PImage;
import processing.core.PVector;

import com.catseye.gui.components.ImageSelectionTool;
import com.catseye.gui.components.ImageSelectionWidget;
import com.quickdrawProcessing.display.DisplayPane;
import com.quickdrawProcessing.display.Stage;

public class ImageSelectionPane extends DisplayPane {

	public ImageSelectionPane(PVector i_position, PVector i_size) {
		super(i_position, i_size);
	}

	public void loadImage(){
	    Stage.p5.selectInput("Select an image", "loadTextureImage");
	}
	

	public void loadTextureImage(File selection) {
	    
	    if (selection == null) {
	      System.out.println("Window was closed or the user hit cancel.");
	    } 
	    else {
	  
	      String path = selection.getAbsolutePath();
	      PImage chosenImage = Stage.p5.loadImage(path);
	      
	      ImageSelectionTool selector = new ImageSelectionTool(new PVector(0,0), new PVector(size.x, size.y), chosenImage, ImageSelectionWidget.MARQUEE);
	      addChild(selector);
	      
	    }
	  }
	
	
}
