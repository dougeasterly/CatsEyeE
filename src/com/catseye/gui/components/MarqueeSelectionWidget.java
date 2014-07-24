package com.catseye.gui.components;

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
		addChild(handles[1]);
		addChild(handles[2]);
		addChild(handles[3]);
	}
	
	public PVector[] getTexCoords(){
		PVector[] out = new PVector[4];
		
		out[0] = TL.get();
		out[0].x /= bottomRightBound.x-topLeftBound.x;
		out[0].y /= bottomRightBound.y-topLeftBound.y;
		

		out[1] = TR.get();
		out[1].x /= bottomRightBound.x-topLeftBound.x;
		out[1].y /= bottomRightBound.y-topLeftBound.y;
				

		out[2] = BR.get();
		out[2].x /= bottomRightBound.x-topLeftBound.x;
		out[2].y /= bottomRightBound.y-topLeftBound.y;
		
		out[3] = new PVector(-1, -1);
		
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
		PGraphics context = preDraw(i_context);
		
		if(!selected)
			context.noFill();
		else
			context.fill(255,255,150,50);
		
		context.strokeWeight(EDGE_SIZE);
		context.stroke(0);
		context.rect(TL.x, TL.y, (BR.x-TL.x), (BR.y-TL.y));

		postDraw(context);
	}
	
	public void actionHook(InteractiveDisplayObject i_child, int label){
		SelectionHandle handle = (SelectionHandle)i_child;
		
		handle.constrain(topLeftBound, bottomRightBound);
	}


}
