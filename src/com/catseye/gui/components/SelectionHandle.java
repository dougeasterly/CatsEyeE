package com.catseye.gui.components;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.display.Stage;

public class SelectionHandle extends InteractiveDisplayObject {
	
	private float radius;
	private int label;
	private boolean highlight;
	
	private InteractiveDisplayObject interactionParent;
	private PVector mainVec, maintainX, maintainY; 
	
	
	
	public SelectionHandle(PVector i_position, PVector i_size) {
		super(i_position, i_size);
		radius = i_size.x/2.0f;
		highlight = false;
	}
	
	public void setLabel(int i_label){
		label = i_label;
	}
	
	public void setCornerVector(PVector i_primary, InteractiveDisplayObject i_interactionParent){
		mainVec = i_primary;
		maintainX = null;
		maintainY = null;
		interactionParent = i_interactionParent;
	}
	
	public void setCornerVectors(PVector i_primary, PVector i_maintainX, PVector i_maintainY, InteractiveDisplayObject i_interactionParent){
		mainVec = i_primary;
		maintainX = i_maintainX;
		maintainY = i_maintainY;
		interactionParent = i_interactionParent;
	}
	
	public boolean isOver(PVector i_position){
		PVector locPos = globalToLocal(i_position);
		return PVector.dist(locPos, new PVector(0,0)) < radius || selected;
	}
	
	public void update(){
		
		if(!mainVec.equals(getPosition())){
			setPositionFromLocal(mainVec);
		}
		
	}
	
	public void move(PVector i_amount){
		if(mainVec != null){
			mainVec.add(i_amount);
			setPositionFromLocal(mainVec);
		}
	}
	
	public void draw(PGraphics i_context){
		
		PGraphics context = preDraw(i_context);
		
		if(!mouseIsOver)
			context.fill(255,0,0);
		else
			context.fill(100,255,100);
		
		context.noStroke();
		context.ellipse(0, 0, radius*2, radius*2);
		postDraw(i_context);
	}
	
	public void mouseDragged(PVector i_mousePos){
		setPositionFromGlobal(i_mousePos);
		
		if(interactionParent != null){
		
			PVector parentPosition = interactionParent.globalToLocal(i_mousePos);
			
			if(mainVec != null){
				mainVec.x = parentPosition.x;
				mainVec.y = parentPosition.y;
			}
			
			if(maintainX != null){
				maintainX.x = parentPosition.x;
			}
			
			if (maintainY != null){
				maintainY.y = parentPosition.y;
			}
			
			parent.actionHook(this, label);
			
		}
			
	}
	
	public void constrain(PVector i_tl, PVector i_br){
		constrain(i_tl, i_br, false);
	}
	
	public void constrain(PVector i_tl, PVector i_br, boolean seperateHandles){
		
		mainVec.x = PApplet.constrain(mainVec.x, i_tl.x, i_br.x);
		mainVec.y = PApplet.constrain(mainVec.y, i_tl.y, i_br.y);
		
		if(!seperateHandles){
			if(maintainX != null){
				maintainX.x = PApplet.constrain(mainVec.x, i_tl.x, i_br.x);
			}
			
			if (maintainY != null){
				maintainY.y = PApplet.constrain(mainVec.y, i_tl.y, i_br.y);
			}
		}
		
	}
	
	public void mousePressed(PVector i_mousePos){
	}
	
	

}
