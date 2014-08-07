package com.catseye;

import java.applet.Applet;
import java.io.File;

import com.catseye.gui.components.ImageSelectionTool;
import com.catseye.gui.components.ImageSelectionWidget;
import com.catseye.gui.components.MarqueeSelectionWidget;
import com.catseye.gui.components.SelectionHandle;
import com.catseye.gui.components.TriangularSelectionWidget;
import com.catseye.gui.guiPanes.GridSelectPane;
import com.catseye.gui.guiPanes.ImageDisplayPane;
import com.catseye.gui.guiPanes.ImageSelectionPane;
import com.catseye.util.SVGLoader;
import com.quickdrawProcessing.display.Stage;
import com.quickdrawProcessing.processing.QuickdrawProcessing;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class CatsEye extends QuickdrawProcessing{

	ImageSelectionPane selector;
	ImageDisplayPane patternDisplay;
	GridSelectPane gridPane;
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "com.catseye.CatsEye"});
	}
	
	public void startQuickdraw(){	   
	     selector = new ImageSelectionPane(new PVector(0,0), new PVector(width/3.0f, height/2.0f));
	     mainStage.addChild(selector);
	     
	     gridPane = new GridSelectPane(new PVector(0,height/2.0f), new PVector(width/3.0f, height/2.0f));
	     mainStage.addChild(gridPane);
	     
	     patternDisplay = new ImageDisplayPane(new PVector(width/3.0f, 0), new PVector(2.0f*(width/3.0f), height));
	     mainStage.addChild(patternDisplay);
	}
	  
	  
	public void drawQuickdraw(){
	
	}

}
