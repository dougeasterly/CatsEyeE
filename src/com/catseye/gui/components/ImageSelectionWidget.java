package com.catseye.gui.components;

import com.catseye.gui.displayTreeObjects.InteractiveDisplayObject;

import processing.core.PVector;

public class ImageSelectionWidget extends InteractiveDisplayObject{

	protected PVector bounds;
	protected PVector[] TextureCoordinates;
	
	public ImageSelectionWidget(){
		
	}
	
	public PVector[] getTexCoords(){
		return new PVector[0];
	}
	
	
}