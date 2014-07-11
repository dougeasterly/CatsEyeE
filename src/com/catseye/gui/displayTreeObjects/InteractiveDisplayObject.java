package com.catseye.gui.displayTreeObjects;

import java.util.ArrayList;

import controlP5.Controller;
import processing.core.PApplet;
import processing.core.PVector;

public class InteractiveDisplayObject{
	
	protected static Stage stage;
	
	protected InteractiveDisplayObject parent;
	protected ArrayList<InteractiveDisplayObject> children;
	
	protected boolean proportionalSize;
	protected PVector localPosition;
	protected PVector globalPosition;
	protected PVector size;
	protected float scale;
	protected int clearColor;
	
	protected boolean mouseIsOver;
	
	
	//------------------------------------CONSTRUCTORS------------------------------------------
	
	protected InteractiveDisplayObject(){
		
		children = new ArrayList<InteractiveDisplayObject>();

		localPosition = new PVector(0,0);
		size = new PVector(0,0);
		scale = 1;
		
		mouseIsOver = false;
	}
	
	protected InteractiveDisplayObject(PVector i_position, PVector i_size){
				
		children = new ArrayList<InteractiveDisplayObject>();
		
		localPosition = i_position;
		size = i_size;
		scale = 1;
		proportionalSize = size.x < 1 || size.y < 1;
		
		mouseIsOver = false;
	}
	
	//------------------------------------GETTERS/SETTERS-----------------------------------------
	
	//---getters---
	
	public Stage getStage(){
		return stage;
	}
	
	public PVector getGlobalPosition(){
		return globalPosition;
	}
	
	public InteractiveDisplayObject getObjectAtPoint(PVector i_position){
		return getChildAtPoint(i_position);
	}
	
	//---setters---
	
	public void setParent(InteractiveDisplayObject i_parent){
		parent = i_parent;
		globalPosition = PVector.add(localPosition, parent.getGlobalPosition());
		clearColor = Stage.p5.color(180);
	}
	
	public void setClearColor(float r, float g, float b){
		clearColor = Stage.p5.color(r, g, b);
	}
	
	public void setClearColor(float g){
		clearColor = Stage.p5.color(g);
	}
	
	//-----------------------------------PUBLIC METHODS------------------------------------
	
	public boolean isOver(PVector i_position){
		return false;
	}
	
	public void addChild(InteractiveDisplayObject i_child){
		children.add(i_child);
		i_child.setParent(this);
	}
	
	public void addChildAt(int i_index, InteractiveDisplayObject i_child){
		children.add(i_index, i_child);
		i_child.setParent(this);
	}
	
	public void draw(){
		preDraw();
		postDraw();
	}
	
	public void updateMouse(PVector i_mousePos){
		
		if(isOver(i_mousePos)){
			
			if(!mouseIsOver){
				mouseEnter(i_mousePos);
			}
			
			mouseIsOver = true;
			
		}else{
			
			if(mouseIsOver){
				mouseExit(i_mousePos);
			}
			
			mouseIsOver = false;
		}
		
	}
	
	public void addCP5Control(Controller i_control, PVector i_position){
		i_control.setPosition(PVector.add(i_position, globalPosition));
	}

	//-----------------------------------PROTECTED METHODS------------------------------------
	
	protected void clear(){
		Stage.p5.noStroke();
		Stage.p5.fill(clearColor);
		Stage.p5.rect(0, 0, size.x, size.y);
	}
	
	protected void preDraw(){
		Stage.p5.pushMatrix();
		Stage.p5.translate(localPosition.x, localPosition.y);
		Stage.p5.scale(scale);
	}
	
	protected void postDraw(){
		
		for(InteractiveDisplayObject child : children){
			child.draw();
		}
		
		Stage.p5.popMatrix();
		
	}
	
	protected InteractiveDisplayObject getChildAtPoint(PVector i_position){
		
		for(int i = children.size()-1; i > 0; --i){
			
				InteractiveDisplayObject selectedChild =  children.get(i).getChildAtPoint(i_position);
			
				if(selectedChild != null)
					return selectedChild;
				
		}
	
		if(this.isOver(i_position))
			return this;
		else 
			return null;
		
	}
	

	//--------------------------------METHODS TO OVERRIDE IN CHILD CLASSES-------------------------------------------
	
	protected void mouseEnter(PVector i_mousePos){
	}
	
	protected void mouseExit(PVector i_mousePos){
	}
	
	protected void click(PVector i_mousePos) {
	}

	protected void mousePressed(PVector i_mousePos) {		
	}

	protected void mouseReleased(PVector i_mousePos) {
	}

	protected void mouseDragged(PVector i_mousePos) {
	}
	
	
}
