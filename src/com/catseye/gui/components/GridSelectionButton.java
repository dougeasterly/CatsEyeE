package com.catseye.gui.components;

import com.quickdrawProcessing.display.DisplayPane;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.display.Stage;

import processing.core.*;

public class GridSelectionButton extends DisplayPane{
	
	private static PGraphics highlight;
	private PGraphics buttonImage;
	private String gridType;
	
	private boolean isCurrent;
	
	public GridSelectionButton(PVector i_position, PVector i_size, String i_gridType, PImage i_previewImage){
		
		super(i_position, i_size);

		
		if(highlight == null){
			highlight = Stage.p5.createGraphics((int)i_size.x, (int)i_size.y);
			highlight.beginDraw();
			highlight.fill(0,255,0,40);
			highlight.stroke(0, 255, 0);
			highlight.strokeWeight(3);
			highlight.rect(1.5f, 1.5f, i_size.x-3f, i_size.y-3f);
			highlight.endDraw();			
		}
		
		gridType = i_gridType;
		selected = false;

		buttonImage = Stage.p5.createGraphics((int)i_size.x, (int)i_size.y);
		i_previewImage.resize((int)i_size.x, (int)i_size.y);
		buttonImage.beginDraw();
		buttonImage.image(i_previewImage, 0, 0);
		buttonImage.noFill();
		buttonImage.stroke(0);
		buttonImage.strokeWeight(2);
		buttonImage.rect(1, 1, i_size.x-2, i_size.y-2);
		buttonImage.endDraw();
		
	}
	
	@Override
	public void addedToStage(){
	}
	
	public String getType(){
		return gridType;
	}
	
	@Override
	public void draw(PGraphics i_context){
		
		PGraphics context = preDraw(i_context);
		
		context.clear();
		context.image(buttonImage, 0, 0);
		
		if(mouseIsOver || isCurrent){
			context.image(highlight, 0, 0);
		}
		
		postDraw(context);
	}
	
	public void current(boolean i_isCurrent){
		isCurrent = i_isCurrent;
	}
	
	@Override
	public void click(PVector i_mousePos){
		interactionHandler.actionHook(this, 0);
	}

	@Override
	public boolean isOver(PVector i_position) {
		return inBounds(i_position);
	}
	
}
