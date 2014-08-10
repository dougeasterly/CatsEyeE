package com.quickdrawProcessing.display;

import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Stage extends InteractiveDisplayObject{
	
	
	
	public static PApplet p5;
	public static ControlP5 cp5;
	
	private boolean dragging;
	private InteractiveDisplayObject selectedChild;

	private static PGraphics stageContext;
	
	
	public Stage(PApplet i_parentApp){
		super(new PVector(0,0), new PVector(i_parentApp.width, i_parentApp.height));
		
		p5 = i_parentApp;
		cp5 = new ControlP5(i_parentApp);
		
		setPositionFromGlobal(new PVector(0,0));
		
		selectedChild = null;
		stage = this;
		parent = null;
		
		stageContext = p5.createGraphics(p5.width, p5.height, PApplet.JAVA2D);
		redraw = true;
		cacheAsBitmap = false;
	}
	
	public static PGraphics getStageContext(){
		return stageContext;
	}
	
	public void draw(){
		p5.background(255);
			stageContext.beginDraw();
			stageContext.clear();
			draw(stageContext);
			stageContext.endDraw();
		p5.image(stageContext,0,0);
	}
	
	public boolean isOver(PVector i_position){
		return true;
	}
	
	public PVector getLocalPosition(){
		return new PVector();
	}
	
	public PVector getGlobalPosition(){
		return new PVector();
	}
	
	public void click(PVector i_mousePos) {
		InteractiveDisplayObject clicked = getChildAtPoint(i_mousePos);
		
		if(clicked != null && clicked!= this)
			clicked.click(i_mousePos);
	}

	public void mousePressed(PVector i_mousePos) {
		InteractiveDisplayObject tempSel = getChildAtPoint(i_mousePos);
		
		if(tempSel != null && tempSel != this){
			System.out.println("selectedChild");
			selectedChild = tempSel;
			selectedChild.select();
			selectedChild.mousePressed(i_mousePos);
			dragging = true;
		}
	}

	public void mouseReleased(PVector i_mousePos) {
		
		if(selectedChild != null){
			selectedChild.mouseReleased(i_mousePos);
			selectedChild.deselect();
			dragging = false;
			selectedChild = null;
		}
		
	}

	public void updateMouse(PVector i_mousePos) {
		
		if(selectedChild != null && dragging){
			selectedChild.mouseDragged(i_mousePos);
		}
		
		for(InteractiveDisplayObject child : children){
			child.updateMouse(i_mousePos);
		}
		
	}
	
	@Override
	public void mouseDragged(PVector i_mousePos) {
		// TODO Auto-generated method stub
		
	}
	
	public void keyPressed(char i_key){
		
	}

	@Override
	public void mouseEnter(PVector i_mousePos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExit(PVector i_mousePos) {
		// TODO Auto-generated method stub
		
	}



}
