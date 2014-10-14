package com.catseye.gui.guiPanes;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import com.catseye.HandlerActions;
import com.catseye.gui.p5Plugs.ImageDisplayControls;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.DisplayPane;
import com.quickdrawProcessing.display.Stage;

public class ImageDisplayPane extends DisplayPane {

	private ImageDisplayControls displayCtls;
	
	private PVector imageOffset;
	private PImage backgroundCheckers, mainImage, gridImage;
	private boolean drawGrid;
		
	public ImageDisplayPane(PVector i_position, PVector i_size){
		super(i_position, i_size);
		drawBorder = true;
	}
	
	@Override
	public void addedToStage(){
		backgroundCheckers = createCheckerBackground();
		displayCtls = new ImageDisplayControls(this, Stage.cp5);
		redrawNow();
		redraw = false;
	}
	
	public void setImages(PImage i_mainImage, PImage i_gridImage){
		
		mainImage = i_mainImage.get();
		gridImage = i_gridImage.get();
		
		if(size.x/(i_mainImage.width+0.0) < size.y/(i_mainImage.height+0.0)){
			mainImage.resize((int)this.size.x, 0);
			gridImage.resize((int)this.size.x, 0);
		}else{	
			mainImage.resize(0, (int)this.size.y);
			gridImage.resize(0, (int)this.size.y);
		}
		
		imageOffset = new PVector(this.size.x/2.0f - mainImage.width/2.0f, this.size.y/2.0f - mainImage.height/2.0f);
		redrawNow();
	}
	
	public void drawGrid(boolean i_drawGrid){
		drawGrid = i_drawGrid;
		redrawNow();
	}
	
	public void draw(PGraphics i_context){
				
		clear(i_context);
		
		i_context.image(backgroundCheckers, 0, 0);
		
		if(mainImage != null)
			i_context.image(mainImage, imageOffset.x, imageOffset.y);
		
		if(gridImage != null && drawGrid)
			i_context.image(gridImage, imageOffset.x, imageOffset.y);
		
		i_context.fill(0, 150);
		i_context.noStroke();
		i_context.rect(0, 0, size.x, 60);
		
	}
	
	public void setSettingsFromGrid(TileGrid i_grid){
		setImages(i_grid.getRender(), i_grid.getGridImage());
	}
	
	
	private PImage createCheckerBackground(){
			   
	    int checkerCount = 80;
	    PGraphics checkerGfx = Stage.p5.createGraphics((int)this.size.x, (int)this.size.y, Stage.p5.P2D);
	    
	    float boxWidth = size.x/(checkerCount+0.0f);
	    
	    checkerGfx.beginDraw();
	    checkerGfx.clear();
	    checkerGfx.noStroke();
	    checkerGfx.rect(0,0,size.x,size.y);
	    checkerGfx.fill(200);
	    
	    for(int i = 0; i < checkerCount; ++i){
	      for(int j = 0; j < checkerCount; ++j){
	       if((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)){
	         checkerGfx.rect(i*boxWidth, j*boxWidth, boxWidth, boxWidth);
	       }
	      }
	    }
	    
	    checkerGfx.endDraw();  
	    
	    return checkerGfx;
	}
	
	public void saveSettings(){
		interactionHandler.actionHook(this, HandlerActions.SAVESETTINGS);
	}
	
	public void saveImage(){
		interactionHandler.actionHook(this, HandlerActions.SAVEIMAGE);
	}

	
}
