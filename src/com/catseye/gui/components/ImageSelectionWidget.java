package com.catseye.gui.components;

import com.quickdrawProcessing.display.InteractiveDisplayObject;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class ImageSelectionWidget extends InteractiveDisplayObject{

	protected static final int HANDLE_SIZE = 15;
	protected static final int EDGE_SIZE = 2;
	
	public static final int MARQUEE = 0;
	public static final int TRIANGULAR = 1;
	
	protected PVector topLeftBound, bottomRightBound;
	
	protected SelectionHandle[] handles;
	
	protected PVector pMouse;
	
	public ImageSelectionWidget(PVector i_position, PVector i_size){
		super(i_position, i_size);
	}
	
	public boolean isOver(PVector i_position){
		return false;
	}
	
	public PVector[] getTexCoords(){
		return new PVector[0];
	}
	
	public void draw(PGraphics i_context){
		PGraphics context = preDraw(i_context);		
		postDraw(context);
	}
	
	public PVector getBoundedRegion(){
		return null;
	}
	
	public void setBounds(PVector i_topLeft, PVector i_bottomRight){}

	public void mousePressed(PVector i_mousePos){
		pMouse = i_mousePos.get();
	}
	
	public void mouseDragged(PVector i_mousePos){
		
		
		PVector moved = PVector.sub(i_mousePos, pMouse); 
		
		for(int i = 0; i < handles.length; ++i){
			handles[i].move(moved);
			handles[i].constrain(topLeftBound, bottomRightBound, true);
		}
		
		pMouse = i_mousePos.get();
		
	}
	
	public boolean insideInteractionBounds(PVector i_position){
		PVector localPos = globalToLocal(i_position);
		return localPos.x > -HANDLE_SIZE && localPos.x < getSize().x + HANDLE_SIZE && localPos.y > -HANDLE_SIZE && localPos.y < getSize().y+HANDLE_SIZE;
	}
	
	
}