package com.catseye.gui.components;

import com.quickdrawProcessing.display.InteractiveDisplayObject;

import processing.core.PGraphics;
import processing.core.PVector;

public class TriangularSelectionWidget extends ImageSelectionWidget{

	PVector c1, c2, c3;
	
	public TriangularSelectionWidget(PVector i_position, PVector i_size) {
		super(i_position, i_size);
		
		c1 = new PVector(0,0);
		c2 = i_size.get();
		c3 = new PVector(0, c2.y);
		
		setBounds(c1, c2);
	}

	
	//So, any point p where [B-A] cross [p-A] does not point in the same direction as [B-A] cross [C-A] isn't inside the triangle. 
	public boolean isOver(PVector i_mousePosition){
		PVector mouseLocal = globalToLocal(i_mousePosition);		
		return pointInTriangle(mouseLocal, c1, c2, c3);
	}
	

	private boolean sameSide(PVector p1 , PVector p2 , PVector a , PVector b){
		
		PVector cp1 = new PVector();
		PVector.cross(PVector.sub(b,a), PVector.sub(p1,a), cp1);
		
		PVector cp2 = new PVector();
		PVector.cross(PVector.sub(b,a), PVector.sub(p2,a), cp2);

		if (PVector.dot(cp1, cp2) >= 0)
			return true;
	    else
	    	return false;
	}
	
	private boolean pointInTriangle(PVector p, PVector a, PVector b, PVector c){
	    if (sameSide(p,a, b,c) && sameSide(p,b, a,c) && sameSide(p,c, a,b))
	    	return true;
	    else
	    	return false;
	}
	
	protected void addedToStage(){
		handles = new SelectionHandle[3];
		handles[0] = new SelectionHandle(c1.get(), new PVector(HANDLE_SIZE, HANDLE_SIZE));
		handles[1] = new SelectionHandle(c2.get(), new PVector(HANDLE_SIZE, HANDLE_SIZE));
		handles[2] = new SelectionHandle(c3.get(), new PVector(HANDLE_SIZE, HANDLE_SIZE));
		
		handles[0].setCornerVector(c1, this);
		handles[1].setCornerVector(c2, this);
		handles[2].setCornerVector(c3, this);
		
		addChild(handles[0]);
		addChild(handles[1]);
		addChild(handles[2]);
	}
	
	public PVector[] getTexCoords(){
		PVector[] out = new PVector[4];
		
		float xSize = bottomRightBound.x-topLeftBound.x;
		float ySize = bottomRightBound.y-topLeftBound.y;
		
		out[0] = c1.get();
		out[0].x /= xSize;
		out[0].y /= ySize;
		

		out[1] = c2.get();
		out[1].x /= xSize;
		out[1].y /= ySize;
				

		out[2] = c3.get();
		out[2].x /= xSize;
		out[2].y /= ySize;
		
		out[3] = new PVector(ImageSelectionWidget.TRIANGULAR, ImageSelectionWidget.TRIANGULAR);
		
		return out;
	}
	
	public void setBounds(PVector i_topLeft, PVector i_bottomRight){
		topLeftBound = i_topLeft.get();
		bottomRightBound = i_bottomRight.get();
		
		c1 = topLeftBound.get();
		c2 = bottomRightBound.get();
		c3 = new PVector(c1.x, c2.y);
		
	}
	
	public void draw(PGraphics i_context){		
		PGraphics context = preDraw(i_context);
		
		if(!selected)
			context.noFill();
		else
			context.fill(255,255,150,50);
		
		context.strokeWeight(EDGE_SIZE);
		context.stroke(0);
		context.triangle(c1.x, c1.y, c2.x, c2.y, c3.x, c3.y);

		
		postDraw(context);
	}
	
	public void actionHook(InteractiveDisplayObject i_child, int label){
		SelectionHandle handle = (SelectionHandle)i_child;
		
		handle.constrain(topLeftBound, bottomRightBound);
	}


	@Override
	public void setTexCoords(PVector[] i_texCoords) {
		
		float xSize = bottomRightBound.x-topLeftBound.x;
		float ySize = bottomRightBound.y-topLeftBound.y;
		
		c1.x = i_texCoords[0].x * xSize;
		c1.y = i_texCoords[0].y * ySize;
		
		c2.x = i_texCoords[1].x * xSize;
		c2.y = i_texCoords[1].y * ySize;
		
		c3.x = i_texCoords[2].x * xSize;
		c3.y = i_texCoords[2].y * ySize;
	}
	
	@Override 
	public int getType(){
		return ImageSelectionWidget.TRIANGULAR; 
	}

}
