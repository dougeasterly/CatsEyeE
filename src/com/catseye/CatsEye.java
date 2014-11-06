package com.catseye;

import com.catseye.gui.guiPanes.GridSelectPane;
import com.catseye.gui.guiPanes.ImageDisplayPane;
import com.catseye.gui.guiPanes.ImageSelectionPane;
import com.quickdrawProcessing.processing.QuickdrawProcessing;

import processing.core.PApplet;
import processing.core.PVector;

public class CatsEye extends QuickdrawProcessing{

	CatsEyeController controller;
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "com.catseye.CatsEye"});
	}
	
	public void startQuickdraw(){	   
		controller = new CatsEyeController(new PVector(0,0), new PVector(width, height));
		mainStage.addChild(controller);
	}
	  
	  
	public void drawQuickdraw(){
	}

}
