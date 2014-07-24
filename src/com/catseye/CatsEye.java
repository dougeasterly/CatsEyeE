package com.catseye;

import java.applet.Applet;
import java.io.File;

import com.catseye.gui.components.ImageSelectionTool;
import com.catseye.gui.components.ImageSelectionWidget;
import com.catseye.gui.components.MarqueeSelectionWidget;
import com.catseye.gui.components.SelectionHandle;
import com.catseye.gui.components.TriangularSelectionWidget;
import com.catseye.util.SVGLoader;
import com.quickdrawProcessing.display.Stage;
import com.quickdrawProcessing.processing.QuickdrawProcessing;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class CatsEye extends QuickdrawProcessing{

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "com.catseye.CatsEye"});
	}
	
	public void startQuickdraw(){	   
		loadImage();
	}
	  
	
	public void drawQuickdraw(){
	
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
	      PImage chosenImage = loadImage(path);
	      
	      ImageSelectionTool selector = new ImageSelectionTool(new PVector(0,0), new PVector(width/3.0f, height/2.0f), chosenImage, ImageSelectionWidget.MARQUEE);
	      mainStage.addChild(selector);
	      
	    }
	  }

}
