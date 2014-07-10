package com.catseye.gui.components;

import processing.core.PVector;

public class ImageSelectionWidget {

	protected PVector bounds;
	protected PVector[] TextureCoordinates;
	
	public ImageSelectionWidget(){
		
	}
	
	public PVector[] getTexCoords(){
		return new PVector[0];
	}
	
	
}