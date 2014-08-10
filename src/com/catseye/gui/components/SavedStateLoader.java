package com.catseye.gui.components;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONObject;

import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.display.Stage;

public class SavedStateLoader extends InteractiveDisplayObject{
	
	public static final int LOADGRID = 0; 
	
	private TileGrid tileGrid;
	private PGraphics miniPreview;
	
	public SavedStateLoader(PVector i_position, PVector i_size, JSONObject i_json) {
		
		super(i_position, i_size);
		
		tileGrid = TileGrid.fromJson(i_json);
		tileGrid.generate();
		
		miniPreview = Stage.p5.createGraphics((int)size.x, (int)size.y, PApplet.JAVA2D);
		miniPreview.clear();
		miniPreview.beginDraw();
		
		PImage gridImage = tileGrid.getRender().get();
		
		if(size.x/tileGrid.getRenderSize().x < size.y/tileGrid.getRenderSize().y)
			gridImage.resize((int)(size.x), 0);
		else
			gridImage.resize(0, (int)size.y);
		
		miniPreview.image(gridImage,0,0);
		miniPreview.stroke(0);
		miniPreview.noFill();
		miniPreview.strokeWeight(2);
		miniPreview.rect(0, 0, size.x, size.y);
		
	}
	
	@Override
	public void draw(PGraphics i_context){
		PGraphics context = preDraw(i_context);
		context.image(miniPreview, 0, 0);
		
		if(mouseIsOver){
			context.fill(255, 255, 200, 50);
			context.noStroke();context.rect(0, 0, size.x, size.y);
		}
		
		postDraw(context);
	}
	
	@Override
	public void click(PVector i_mousePosition){
		interactionHandler.actionHook(this, LOADGRID);
	}
	
	public PImage getImage(){
		return miniPreview.get();
	}
	
	public TileGrid getGrid(){
		return tileGrid;
	}

	@Override
	public boolean isOver(PVector i_position) {
		return inBounds(i_position);
	}
	
	
	
}
