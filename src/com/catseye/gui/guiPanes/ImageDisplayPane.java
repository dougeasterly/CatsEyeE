package com.catseye.gui.guiPanes;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import com.catseye.gui.displayTreeObjects.DisplayPane;
import com.catseye.gui.displayTreeObjects.Stage;

public class ImageDisplayPane extends DisplayPane {

	private PVector imageOffset;
	private PImage backgroundCheckers, mainImage, gridImage;
	private boolean drawGrid;
	
	public ImageDisplayPane(PVector i_position, PVector i_size){
		super(i_position, i_size);
		backgroundCheckers = createCheckerBackground();
	}
	
	public void setImages(PImage i_mainImage, PImage i_gridImage){
		
		mainImage = i_mainImage.get();
		gridImage = i_gridImage.get();
		
		if(i_mainImage.width < i_mainImage.height){
			mainImage.resize((int)this.size.x, 0);
			gridImage.resize((int)this.size.x, 0);
		}else{	
			mainImage.resize(0, (int)this.size.y);
			gridImage.resize(0, (int)this.size.y);
		}
		
		imageOffset = new PVector(this.size.x/2.0f - mainImage.width/2.0f, this.size.y/2.0f - mainImage.height/2.0f);
		
	}
	
	public void drawGrid(boolean i_drawGrid){
		drawGrid = i_drawGrid;
	}
	
	public void draw(){
		preDraw();
		
		clear();
		Stage.p5.image(backgroundCheckers, 0, 0);
		
		if(mainImage != null)
			Stage.p5.image(mainImage, imageOffset.x, imageOffset.y);
		
		if(gridImage != null && drawGrid)
			Stage.p5.image(gridImage, imageOffset.x, imageOffset.y);
		
		postDraw();
	}
	
	
	private PImage createCheckerBackground(){
			   
	    int checkerCount = 80;
	    PGraphics checkerGfx = Stage.p5.createGraphics((int)this.size.x, (int)this.size.y);
	    
	    float boxWidth = size.x/(checkerCount+0.0f);
	    
	    checkerGfx.beginDraw();
	    checkerGfx.background(255);
	    checkerGfx.fill(200);
	    checkerGfx.noStroke();
	    
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

	
}
