package com.catseye.gui.components;

import processing.core.PGraphics;
import processing.core.PVector;

import com.catseye.HandlerActions;
import com.quickdrawProcessing.display.InteractiveDisplayObject;

public class CloseButton extends InteractiveDisplayObject {
	
	protected CloseButton(PVector i_position, PVector i_size) {
		super(i_position, i_size);
	}
	
	@Override
	public void draw(PGraphics i_context){
		PGraphics context = preDraw(i_context);
		
			if(mouseIsOver)
				context.fill(255,150,150);				
			else
				context.fill(255,0,0);
			
			context.noStroke();
			context.rect(0, 0, size.x, size.y);
			context.stroke(255);
			context.strokeWeight(3);
			context.line(2, 2, size.x-2, size.y-2);
			context.line(size.x-2, 2, 2, size.y-2);
			
		postDraw(context);
	}
	
	@Override
	public void click(PVector i_position){
		interactionHandler.actionHook(this, HandlerActions.CLOSE);
	}

	@Override
	public boolean isOver(PVector i_position) {
		return inBounds(i_position);
	}

}
