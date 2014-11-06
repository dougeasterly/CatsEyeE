package com.catseye.gui.components;

import java.io.File;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONObject;

import com.catseye.HandlerActions;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.display.Stage;

public class SavedStateLoader extends InteractiveDisplayObject{
		
	private String filePath;
	private JSONObject json;
	private PGraphics miniPreview;
	private CloseButton deleteBtn;
	
	public SavedStateLoader(PVector i_position, PVector i_size, JSONObject i_json) {
		
		super(i_position, i_size);
		
		json = i_json;
		
		filePath = i_json.getString("savePath"); 
		PImage previewImage = Stage.p5.loadImage(i_json.getString("savePath")+"previewImage.png");
		
		miniPreview = Stage.p5.createGraphics((int)size.x, (int)size.y, PApplet.JAVA2D);
		miniPreview.clear();
		miniPreview.beginDraw();
				
		if(size.x/(previewImage.width+0.0) < size.y/(previewImage.height+0.0))
			previewImage.resize((int)(size.x), 0);
		else
			previewImage.resize(0, (int)size.y);
		
		miniPreview.image(previewImage, size.x/2.0f - previewImage.width/2.0f,  size.y/2.0f - previewImage.height/2.0f);
		miniPreview.stroke(0);
		miniPreview.noFill();
		miniPreview.strokeWeight(2);
		miniPreview.rect(0, 0, size.x, size.y);
		miniPreview.endDraw();
	}
	
	@Override
	public void addedToStage(){
		deleteBtn = new CloseButton(new PVector(size.x-20,0), new PVector(20,20)); 
		deleteBtn.setInteractionHandler(this);
		addChild(deleteBtn);
	}
	
	@Override
	public void draw(PGraphics i_context){
		PGraphics context = preDraw(i_context);
		context.image(miniPreview, 0, 0);
		
		if(mouseIsOver && !deleteBtn.mouseIsOver()){
			context.fill(255, 255, 200, 50);
			context.noStroke();
			context.rect(0, 0, size.x, size.y);
		}
		
		postDraw(context);
	}
	
	@Override
	public void click(PVector i_mousePosition){
		interactionHandler.actionHook(this, HandlerActions.LOADSETTINGS);
	}
	
	public PImage getImage(){
		return miniPreview.get();
	}
	
	public TileGrid getGrid(){
		TileGrid tileGrid = TileGrid.fromJson(json);
		tileGrid.generate();
		return tileGrid;
	}

	@Override
	public boolean isOver(PVector i_position) {
		return inBounds(i_position);
	}
	
	@Override
	public void actionHook(InteractiveDisplayObject i_obj, int action){
		
		if(action == HandlerActions.CLOSE){
			File delete = new File(filePath);
			File[] innerFiles = delete.listFiles();
			
			for(int i = 0; i < innerFiles.length; ++i){
				innerFiles[i].delete();
			}
			
			delete.delete();
			parent.actionHook(this, HandlerActions.CLOSE);
		}
		
	}
	
	
	
}
