package com.catseye.gui.components;

import processing.core.PGraphics;
import processing.core.PVector;

import com.quickdrawProcessing.display.InteractiveDisplayObject;

public class SelectionHandle extends InteractiveDisplayObject {
	
	private float radius;
	private String label;
	
	private boolean highlight;
	
	public SelectionHandle(PVector i_position, PVector i_size, InteractiveDisplayObject i_parent) {
		super(i_position, i_size);
		radius = i_size.x/2.0f;
		highlight = false;
	}
	
	public void setLabel(String i_label){
		label = i_label;
	}
	
	public boolean isOver(PVector i_position){
		PVector locPos = globalToLocal(i_position);
		return PVector.dist(locPos, new PVector(0,0)) < radius;
	}
	
	public void draw(PGraphics i_context){
		
		PGraphics context = preDraw(i_context);
		
		if(!highlight)
			context.fill(255,0,0);
		else
			context.fill(255,100,100);
		
		context.noStroke();
		context.ellipse(0, 0, radius*2, radius*2);
		postDraw(i_context);
	}
	
	public void mouseEnter(PVector i_mousePos){
		highlight = true;
	}

	public void mouseExit(PVector i_mousePos){
		highlight = false;
	}

	
	public void mouseDragged(PVector i_mousePos){
		
		setPositionFromGlobal(i_mousePos);
		parent.actionHook(label);
	}
	
	public void mousePressed(PVector i_mousePos){

		System.out.println("PRESSED");

	}
	
	

}
