package com.catseye.gui.components;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONObject;

import com.catseye.CatsEye;
import com.catseye.patternComponents.gridGenerators.TileGrid;

public class SavedStateLoader {

	public static final int PREVIEW_SIZE = 100;
	
	TileGrid tileGrid;
	PGraphics miniPreview;
	
	public SavedStateLoader(JSONObject i_json) {
		
		tileGrid = TileGrid.fromJson(i_json);
		
		miniPreview = CatsEye.p5.createGraphics(PREVIEW_SIZE, PREVIEW_SIZE, PApplet.JAVA2D);
		miniPreview.clear();
		miniPreview.beginDraw();
		
		PImage gridImage = tileGrid.getPrintImage().get();
		
		if(tileGrid.getRenderSize().x > tileGrid.getRenderSize().y)
			gridImage.resize(PREVIEW_SIZE, 0);
		else
			gridImage.resize(0, PREVIEW_SIZE);
		
		miniPreview.image(gridImage,0,0);
		miniPreview.stroke(0);
		miniPreview.noFill();
		miniPreview.strokeWeight(2);
		miniPreview.rect(0, 0, PREVIEW_SIZE, PREVIEW_SIZE);
		
	}
	
}
