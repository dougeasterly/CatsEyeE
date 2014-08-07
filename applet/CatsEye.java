package com.catseye;

import java.awt.Frame;

import com.catseye.gui.GUI;
import com.catseye.patternComponents.*;

import processing.core.*;

public class CatsEye extends PApplet{

	private static final long serialVersionUID = 9088465710966395469L;

	public static PApplet p5;
	
	public GUI gui;
	
	
	public void init() {
		
		super.init();
		
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            frame.setMenuBar(null);
            frame.pack();
        }
        
    }
	
	
	public void setup(){
	  
	  p5 = this;
		
	  size(1000, 1000, P2D);
	 
	  gui = new GUI();
	  
	  
	}
	
	public void draw(){
	 
	  background(180);
	  gui.drawGui();
	
	}

}