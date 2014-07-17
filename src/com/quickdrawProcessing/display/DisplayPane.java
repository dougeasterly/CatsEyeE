package com.quickdrawProcessing.display;

import processing.core.PVector;

public class DisplayPane extends InteractiveDisplayObject {
	
	public DisplayPane(PVector i_position, PVector i_size){
		super(i_position, i_size);
		cacheAsBitmap(true);
	}
	
}
