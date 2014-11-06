package com.catseye.gui.components;

import com.catseye.gui.guiPanes.ImageSelectionPane;
import com.quickdrawProcessing.display.InteractiveDisplayObject;

import processing.core.PGraphics;
import processing.core.PVector;

public class MarqueeSelectionWidget extends ImageSelectionWidget {

	public static final int TOP_LEFT = 0;
	public static final int TOP_RIGHT = 1;
	public static final int BOTTOM_LEFT = 2;
	public static final int BOTTOM_RIGHT = 3;
		
	PVector TL, TR, BL, BR;

	public MarqueeSelectionWidget( PVector i_position, PVector i_size) {
		super(i_position, i_size);
		TL = new PVector(0,0);
		BR = i_size.get();
		BL = new PVector(TL.x, BR.y);
		TR = new PVector(BR.x, TL.y);
		
		setBounds(TL, BR);
	}
	
	protected void addedToStage(){
		handles = new SelectionHandle[4];
		handles[0] = new SelectionHandle(TL.get(), new PVector(HANDLE_SIZE, HANDLE_SIZE));
		handles[1] = new SelectionHandle(TR.get(), new PVector(HANDLE_SIZE, HANDLE_SIZE));
		handles[2] = new SelectionHandle(BR.get(), new PVector(HANDLE_SIZE, HANDLE_SIZE));
		handles[3] = new SelectionHandle(BL.get(), new PVector(HANDLE_SIZE, HANDLE_SIZE));
		
		handles[0].setCornerVectors(TL, BL, TR, this);
		handles[0].setLabel(TOP_LEFT);
		handles[1].setCornerVectors(TR, BR, TL, this);
		handles[1].setLabel(TOP_RIGHT);
		handles[2].setCornerVectors(BR, TR, BL, this);
		handles[2].setLabel(BOTTOM_RIGHT);
		handles[3].setCornerVectors(BL, TL, BR, this);
		handles[3].setLabel(BOTTOM_LEFT);
		
		addChild(handles[0]);
		addChild(handles[2]);
		addChild(handles[1]);
		addChild(handles[3]);
	}
	
	public PVector[] getTexCoords(){
		PVector[] out = new PVector[4];
		
		float xSize = bottomRightBound.x-topLeftBound.x;
		float ySize = bottomRightBound.y-topLeftBound.y;
		
		out[0] = TL.get();
		out[0].x /= xSize;
		out[0].y /= ySize;
		

		out[1] = TR.get();
		out[1].x /= xSize;
		out[1].y /= ySize;
				

		out[2] = BR.get();
		out[2].x /= xSize;
		out[2].y /= ySize;
		
		out[3] = new PVector(ImageSelectionWidget.MARQUEE, ImageSelectionWidget.MARQUEE);
		
		return out;
	}
	
	public void setBounds(PVector i_topLeft, PVector i_bottomRight){
		topLeftBound = i_topLeft.get();
		bottomRightBound = i_bottomRight.get();
		
		TL = topLeftBound.get();
		BR = bottomRightBound.get();
		BL = new PVector(TL.x, BR.y);
		TR = new PVector(BR.x, TL.y);
	}
	
	public boolean isOver(PVector i_mousePosition){
		
		PVector localPosition = globalToLocal(i_mousePosition);
		
		float left   = Math.min(TL.x, TR.x);	
		float right  = Math.max(TL.x, TR.x);
		float top    = Math.min(TL.y, BL.y);
		float bottom = Math.max(TL.y, BL.y);
		return localPosition.x > left && localPosition.x < right && localPosition.y > top && localPosition.y < bottom;
	}
	
	public void draw(PGraphics i_context){		
		
		if(!selected)
			i_context.noFill();
		else
			i_context.fill(255,255,150,50);
		
		i_context.strokeWeight(EDGE_SIZE);
		i_context.stroke(0);
		i_context.rect(TL.x, TL.y, (BR.x-TL.x), (BR.y-TL.y));
		
	}
	
	public void actionHook(InteractiveDisplayObject i_child, int label){
		SelectionHandle handle = (SelectionHandle)i_child;
		
		handle.constrain(topLeftBound, bottomRightBound);
	}

	@Override
	public void setTexCoords(PVector[] i_texCoords) {

		float xSize = bottomRightBound.x-topLeftBound.x;
		float ySize = bottomRightBound.y-topLeftBound.y;
		
		TL.x = i_texCoords[0].x*xSize;
		TL.y = i_texCoords[0].y*ySize;
		
		TR.x = i_texCoords[1].x*xSize;
		TR.y = i_texCoords[1].y*ySize;
		
		BR.x = i_texCoords[2].x*xSize;
		BR.y = i_texCoords[2].y*ySize;
		
		BL.x = i_texCoords[0].x*xSize;
		BL.y = i_texCoords[2].y*ySize;		
	}
	
	@Override
	public int getType(){
		return ImageSelectionWidget.MARQUEE;
	}




}
