package com.quickdrawProcessing.processing;

import java.awt.Frame;

import com.quickdrawProcessing.display.Stage;

import processing.core.PApplet;
import processing.core.PVector;

public class QuickdrawProcessing extends PApplet {

	public static final long serialVersionUID = 1363560403441783735L;
	
	public static Stage mainStage;

	public void startQuickdraw(){};
	public void drawQuickdraw(){};
	
	public void init() {
		
		super.init();
		
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            frame.setMenuBar(null);
            frame.pack();
        }
        
    }
	
	public void setup(){
		
	  size(displayWidth, displayHeight, P2D);
	  mainStage = new Stage(this);
	  System.out.println("started");
	  
	  startQuickdraw();
	}
	
	public void draw(){
		mainStage.updateMouse(new PVector(mouseX, mouseY));
		drawQuickdraw();
		mainStage.draw();
	}
	
	public void mousePressed(){
		mainStage.mousePressed(new PVector(mouseX, mouseY));
	}
	
	public void mouseReleased(){
		mainStage.mouseReleased(new PVector(mouseX, mouseY));
	}
	
	public void mouseClicked(){
		mainStage.click(new PVector(mouseX, mouseY));
	}
	
	public void keyPressed(){
		mainStage.keyPressed(key);
	}
	
}
