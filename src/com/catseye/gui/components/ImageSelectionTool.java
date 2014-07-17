package com.catseye.gui.components;

import com.quickdrawProcessing.display.InteractiveDisplayObject;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class ImageSelectionTool extends InteractiveDisplayObject{

	private PVector bounds, imagePosition;
	private PImage image;
	
	private ImageSelectionWidget grabber;
	
	public ImageSelectionTool(PVector i_position, PVector i_size, PImage i_image, int selectionMethod){
		super(i_position, i_size);
		
		bounds = i_size.get();
		image = i_image;
		
		if(bounds.x > bounds.y)
			image.resize((int)(bounds.x) , 0);
		else
			image.resize(0 , (int)(bounds.y));
		
		imagePosition = new PVector(i_size.x/2.0f-image.width/2.0f, i_size.y/2.0f-image.height/2.0f);
		PVector imageSize = new PVector(image.width, image.height);
		
		if(selectionMethod == ImageSelectionWidget.MARQUEE)
			grabber = new MarqueeSelectionWidget(imagePosition, imageSize);
		else
			grabber = new TriangularSelectionWidget(imagePosition, imageSize);
		
		this.addChild(grabber);
	}
	
	public PVector[] getTextureCoordinates(){
		return grabber.getTexCoords();
	}
	
	public void draw(PGraphics i_context){
		PGraphics context = preDraw(i_context);
		
		context.image(image, imagePosition.x, imagePosition.y);
		
		postDraw(i_context);
	}
	
	public PVector getSize(){
		return new PVector(image.width, image.height);
	}

}
