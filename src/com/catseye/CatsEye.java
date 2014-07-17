package com.catseye;

import java.applet.Applet;

import com.catseye.gui.components.SelectionHandle;
import com.quickdrawProcessing.display.Stage;
import com.quickdrawProcessing.processing.QuickdrawProcessing;

import processing.core.PApplet;
import processing.core.PVector;

public class CatsEye extends QuickdrawProcessing{

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "com.catseye.CatsEye"});
	}
	
	public void startQuickdraw(){
		
		SelectionHandle hnd = new SelectionHandle(new PVector(width/2, height/2), new PVector(55,55), mainStage);
		mainStage.addChild(hnd);
		
		for(int i = 0; i < 5; ++i){
			SelectionHandle hnd2 = new SelectionHandle(new PVector(0,0), new PVector(500-i*90,500-i*90), mainStage);
			hnd.addChild(hnd2);
			hnd = hnd2;
		}
		
	}
	
	public void drawQuickdraw(){
	
	}

}
