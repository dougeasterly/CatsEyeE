package com.quickdrawProcessing.display;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Stage extends InteractiveDisplayObject{
	
	
	
	public static PApplet p5;
	private boolean dragging;
	private InteractiveDisplayObject selected;

	private static PGraphics stageContext;
	
	
	public Stage(PApplet i_parentApp){
		super(new PVector(0,0), new PVector(i_parentApp.width, i_parentApp.height));
		
		p5 = i_parentApp;
		
		setPositionFromGlobal(new PVector(0,0));
		
		selected = null;
		stage = this;
		parent = null;
		
		stageContext = p5.createGraphics(p5.width, p5.height);
		redraw = true;
	}
	
	public static PGraphics getStageContext(){
		return stageContext;
	}
	
	public void draw(){
		
		stageContext.background(255);
		stageContext.beginDraw();
		draw(stageContext);
		stageContext.endDraw();
		p5.image(stageContext,0,0);
	}
	
	public boolean isOver(PVector i_position){
		return true;
	}
	
	public void click(PVector i_mousePos) {
		InteractiveDisplayObject clicked = getChildAtPoint(i_mousePos);
		
		if(clicked != null && clicked!= this)
			clicked.click(i_mousePos);
	}

	public void mousePressed(PVector i_mousePos) {
		InteractiveDisplayObject tempSel = getChildAtPoint(i_mousePos);
		
		if(tempSel != null && tempSel != this){
			System.out.println("selected");
			selected = tempSel;
			selected.mousePressed(i_mousePos);
			dragging = true;
		}
	}

	public void mouseReleased(PVector i_mousePos) {
		
		if(selected != null){
			selected.mouseReleased(i_mousePos);
			dragging = false;
			selected = null;
		}
		
	}

	public void updateMouse(PVector i_mousePos) {
		
		if(selected != null && dragging){
			selected.mouseDragged(i_mousePos);
			System.out.println("dragged");
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
