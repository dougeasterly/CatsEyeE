package com.catseye.gui.components;

import com.quickdrawProcessing.display.InteractiveDisplayObject;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class ImageSelectionWidget extends InteractiveDisplayObject{

	public static final int MARQUEE = 0;
	public static final int TRIANGULAR = 1;
	
	protected PVector bounds;
	protected PVector[] TextureCoordinates;
	
	public ImageSelectionWidget(PVector i_position, PVector i_size){
		super(i_position, i_size);
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
	
	
}