package com.quickdrawProcessing.display;

import java.util.ArrayList;

import controlP5.Controller;
import processing.core.PGraphics;
import processing.core.PVector;

public abstract class InteractiveDisplayObject{
	
	protected static Stage stage;
	
	protected PGraphics canvas;
	protected boolean cacheAsBitmap = false;
	
	protected InteractiveDisplayObject parent;
	protected ArrayList<InteractiveDisplayObject> children;
	
	protected boolean redraw;
	
	protected boolean proportionalSize;
	private PVector localPosition;
	private PVector globalPosition;
	protected PVector size;
	protected float scale;
	protected int clearColor;
	
	protected boolean mouseIsOver;
	protected boolean selected;
	
	
	//------------------------------------CONSTRUCTORS------------------------------------------
	
	protected InteractiveDisplayObject(PVector i_position, PVector i_size){
				
		children = new ArrayList<InteractiveDisplayObject>();
		
		localPosition = i_position;
	
		size = i_size;
		scale = 1;
		proportionalSize = size.x < 1 || size.y < 1;
		
		mouseIsOver = false;
		redraw = true;
	}
	
	//------------------------------------GETTERS/SETTERS-----------------------------------------
	
	//---getters---
	
	public PVector getSize(){
		return size;
	}
	
	public PVector getPosition(){
		return localPosition;
	}
	
	public PVector getGlobalPosition(){
		return globalPosition;
	}
	
	public Stage getStage(){
		return stage;
	}
	
	public InteractiveDisplayObject getObjectAtPoint(PVector i_position){
		return getChildAtPoint(i_position);
	}
	
	public PGraphics getContext(){
		
		if(canvas == null)
			System.out.println("you must call cacheAsBitmap(true) to set up the drawing context");
		
		return canvas;
	}
	
	//---setters---
	
	public void setPositionFromGlobal(PVector i_globalPosition){
		if(parent != null){
			localPosition = PVector.sub(i_globalPosition.get(), parent.getGlobalPosition());
			globalPosition = i_globalPosition.get();
		}else{
			localPosition = i_globalPosition.get();
			globalPosition = i_globalPosition.get();			
		}
	}
	
	public void setPositionFromLocal(PVector i_localPosition){
		if(parent != null){
			localPosition = i_localPosition.get();
			globalPosition = PVector.add(i_localPosition, parent.getGlobalPosition());
		}else{
			localPosition = i_localPosition.get();
			globalPosition = i_localPosition.get();
		}
	}
	
	
	public void setSize(){
		
	}
	
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
	
	public void cacheAsBitmap(boolean i_cacheAsBitmap){
		cacheAsBitmap = i_cacheAsBitmap;
		
		if(cacheAsBitmap)
			canvas = Stage.p5.createGraphics((int)size.x, (int)size.y);
	}
	
	//-----------------------------------PUBLIC METHODS------------------------------------
	
	public void addChild(InteractiveDisplayObject i_child){
		children.add(i_child);
		i_child.setParent(this);
		i_child.addedToStage();
	}
	
	public void addChildAt(int i_index, InteractiveDisplayObject i_child){
		children.add(i_index, i_child);
		i_child.setParent(this);
		i_child.addedToStage();
	}
	
	public void draw(){
		draw(Stage.getStageContext());
	}
	
	public boolean inBounds(PVector i_position){
		PVector localPos = globalToLocal(i_position);
		return localPos.x > 0 && localPos.x < size.x && localPos.y > 0 && localPos.y < size.y;
	}
	
	public void setUpdateDraw(boolean i_doUpdate){
			
		InteractiveDisplayObject currObject = this;
		
		if(i_doUpdate){
			while(currObject != null){
				currObject.redraw = true;
				currObject = currObject.parent;
			}
		}else
			redraw = false;
	}
	
	public PVector localToGlobal(PVector i_local){
		return PVector.add(i_local, globalPosition);
	}
	
	public PVector globalToLocal(PVector i_global){
		return PVector.sub(i_global, globalPosition);
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
				
				for(InteractiveDisplayObject child : children){
					child.updateMouse(i_mousePos);
				}
			}
			
			mouseIsOver = false;
		}
		
		if(insideInteractionBounds(i_mousePos)){
			for(InteractiveDisplayObject child : children){
				child.updateMouse(i_mousePos);
			}	
		}
		
		
		
	}
	
	public void addCP5Control(Controller i_control, PVector i_position){
		i_control.setPosition(PVector.add(i_position, globalPosition));
	}
	
	public void addCP5Control(Controller i_control, PVector i_position, String i_function){
		i_control.setPosition(PVector.add(i_position, globalPosition));
		i_control.plugTo(this, i_function);
	}

	//-----------------------------------PROTECTED METHODS------------------------------------
	
	protected void clear(PGraphics i_drawContext){
		i_drawContext.noStroke();
		i_drawContext.fill(clearColor);
		i_drawContext.rect(0, 0, size.x, size.y);
	}
	
	protected PGraphics preDraw(PGraphics i_drawContext){
		
		PGraphics currContext = cacheAsBitmap ? canvas : i_drawContext;
		
		currContext.pushMatrix();
		currContext.translate(localPosition.x, localPosition.y);
		currContext.scale(scale);
		
		return currContext;
	}
	
	protected void postDraw(PGraphics i_drawContext){
		
		drawChildren(i_drawContext);
		i_drawContext.popMatrix();
		
	}
	
	protected void drawChildren(PGraphics i_context){
		
		PGraphics currentContext = cacheAsBitmap ? canvas : i_context;
		
		for(InteractiveDisplayObject child : children){
			if(child.redraw){
				child.update();
				child.draw(currentContext);
			}
		}
		
	}
	
	protected InteractiveDisplayObject getChildAtPoint(PVector i_position){
		
		if(this.insideInteractionBounds(i_position)){
			
			for(int i = children.size()-1; i >= 0; --i){
				
					InteractiveDisplayObject selectedChild = children.get(i).getChildAtPoint(i_position);
				
					if(selectedChild != null)
						return selectedChild;
					
			}
			
		}
		
		if(this.isOver(i_position))
			return this;
		
		return null;
		
		
		
	}
	
	public void select(){
		selected = true;
	}
	
	public void deselect(){
		selected = false;
	}
	

	//--------------------------------METHODS TO OVERRIDE IN CHILD CLASSES-------------------------------------------
	
	protected void addedToStage(){}
	
	public void update(){};
	
	public void draw(PGraphics i_context){
		preDraw(i_context);
		postDraw(i_context);
	}
	
	public abstract boolean isOver(PVector i_position);
	
	public boolean insideInteractionBounds(PVector i_position){
		PVector localPos = globalToLocal(i_position);
		return localPos.x > 0 && localPos.x < size.x && localPos.y > 0 && localPos.y < size.y;
	}
	
	public void mouseEnter(PVector i_mousePos){};
	
	public void mouseExit(PVector i_mousePos){};
	
	public void click(PVector i_mousePos){};

	public void mousePressed(PVector i_mousePos){};

	public void mouseReleased(PVector i_mousePos){};

	public void mouseDragged(PVector i_mousePos){};
	
	public void keyPressed(char key){};
	
	public void actionHook(InteractiveDisplayObject child, int i_action){}


	
	
}
