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
	
	//call from draw method of child classes
	public void drawBorder(PGraphics i_context){
		i_context.noFill();
		i_context.stroke(0);
		i_context.strokeWeight(1);
		i_context.rect(0,0,size.x,size.y);
		i_context.rect(2,2,size.x-4,size.y-4);
	}
	
	
	public void startResize(){}
	public void resize(){}
	public void endResize(){}
	
}
