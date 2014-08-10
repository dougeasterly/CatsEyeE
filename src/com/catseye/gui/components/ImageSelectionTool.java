package com.catseye.gui.components;

import com.quickdrawProcessing.display.InteractiveDisplayObject;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class ImageSelectionTool extends InteractiveDisplayObject{

	private PVector bounds, imagePosition;
	private PImage displayImage, image;
	
	private ImageSelectionWidget grabber;
	private int selectionMethod;
	
	public ImageSelectionTool(PVector i_position, PVector i_size, PImage i_image, int i_selectionMethod){
		super(i_position, i_size);
		
		bounds = i_size.get();
		image = i_image.get();
		displayImage = i_image.get();
		
		if(bounds.x/displayImage.width < bounds.y/displayImage.height)
			displayImage.resize((int)(bounds.x-ImageSelectionWidget.HANDLE_SIZE) , 0);
		else
			displayImage.resize(0 , (int)(bounds.y-ImageSelectionWidget.HANDLE_SIZE));
		
		selectionMethod = i_selectionMethod;
		
	}
	
	public void addedToStage(){
		
		imagePosition = new PVector(size.x/2.0f-displayImage.width/2.0f, size.y/2.0f-displayImage.height/2.0f);
		PVector imageSize = new PVector(displayImage.width, displayImage.height);
		
		if(selectionMethod == ImageSelectionWidget.MARQUEE)
			grabber = new MarqueeSelectionWidget(imagePosition, imageSize);
		else
			grabber = new TriangularSelectionWidget(imagePosition, imageSize);
		
		this.addChild(grabber);
		
	}
	
	public void changeSelectionTool(int toolType){
		
		switch(toolType){
			case ImageSelectionWidget.MARQUEE:
				grabber = new MarqueeSelectionWidget(imagePosition, new PVector(displayImage.width, displayImage.height));
				break;
			case ImageSelectionWidget.TRIANGULAR:
				grabber = new TriangularSelectionWidget(imagePosition, new PVector(displayImage.width, displayImage.height));
				break;
			default:
				grabber = new MarqueeSelectionWidget(imagePosition, new PVector(displayImage.width, displayImage.height));
		}
		
	}
	
	public boolean isOver(PVector i_position){
		return inBounds(i_position);
	}
	
	public PVector[] getTextureCoordinates(){
		return grabber.getTexCoords();
	}
	
	public void draw(PGraphics i_context){
		PGraphics context = preDraw(i_context);
			context.image(displayImage, imagePosition.x, imagePosition.y);
		postDraw(i_context);
	}
	
	public void mousePressed(PVector i_mousePos){
		System.out.println("toolhit");
	}
	
	public PVector getSize(){
		return new PVector(image.width, image.height);
	}
	
	public PImage getImage(){
		return image;
	}

}
