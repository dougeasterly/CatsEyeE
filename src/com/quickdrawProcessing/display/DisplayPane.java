package com.quickdrawProcessing.display;

import processing.core.PGraphics;
import processing.core.PVector;

public class DisplayPane extends InteractiveDisplayObject {
	
	protected boolean drawBorder;
	
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
		i_context.clear();
	}
	
	@Override
	public void drawOverChildren(PGraphics i_context){
		if(drawBorder){
			i_context.stroke(0);
			i_context.strokeWeight(1);
			i_context.fill(255,0,0);
			i_context.noFill();
			i_context.rect(0,0,size.x,size.y);
			i_context.stroke(100);
			i_context.line(1, 1, size.x-1, 1);
			i_context.line(size.x-1, 1, size.x-1, size.y);
			i_context.stroke(130);
			i_context.line(1, 2, size.x-2, 2);
			i_context.line(size.x-2, 2, size.x-2, size.y);
		}
	}
	
	
	public void startResize(){}
	public void resize(){}
	public void endResize(){}
	
}
