package com.quickdrawProcessing.display;

import processing.core.PGraphics;
import processing.core.PVector;

public class DisplayPane extends InteractiveDisplayObject {
	
	public DisplayPane(PVector i_position, PVector i_size){
		super(i_position, i_size);
		cacheAsBitmap(true);
	}
	
	@Override
	public boolean isOver(PVector i_position) {
		return inBounds(i_position);
	}

	@Override
	public void draw(PGraphics i_context){
		
		PGraphics context = preDraw(i_context);
		context.clear();
		postDraw(context);
		
	}
	
	
	public void startResize(){}
	public void resize(){}
	public void endResize(){}
	
}
