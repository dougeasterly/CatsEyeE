package com.catseye.gui.components;

import processing.core.*;

public class GridSelectionButton {
	
	private PApplet parent;
	
	private PVector size, position;
	private PGraphics buttonImage;
	private String gridType;
	
	public GridSelectionButton(PApplet i_parent, String i_gridType, PVector i_size, PVector i_position, PImage i_previewImage){
		
		parent = i_parent;
		gridType = i_gridType;
		position = i_position;

		size = i_size;
		buttonImage = i_parent.createGraphics((int)i_size.x, (int)i_size.y);
		i_previewImage.resize((int)i_size.x, (int)i_size.y);
		buttonImage.beginDraw();
		buttonImage.image(i_previewImage, 0, 0);
		buttonImage.noFill();
		buttonImage.stroke(0);
		buttonImage.strokeWeight(2);
		buttonImage.rect(1, 1, i_size.x-2, i_size.y-2);
		buttonImage.endDraw();
		
	}
	
	public String getType(){
		return gridType;
	}
	
	public boolean isPtOver(PVector i_testPoint){
		return isPtOver(i_testPoint, new PVector(0,0));
	}
	
	public boolean isPtOver(PVector i_testPoint, PVector i_offset){
		PVector rPos = PVector.add(position, i_offset);
		return i_testPoint.x > rPos.x && i_testPoint.x < (rPos.x+size.x) && i_testPoint.y > rPos.y && i_testPoint.y < (rPos.y+size.y);
	}
	
	public void draw(){
		draw(new PVector(0,0));
	}
	
	public void draw(PVector i_offset){
		
		parent.image(buttonImage, position.x+i_offset.x, position.y+i_offset.y);
		
	}
	
}
