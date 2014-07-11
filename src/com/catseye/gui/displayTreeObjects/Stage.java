package com.catseye.gui.displayTreeObjects;

import com.catseye.CatsEye;

import processing.core.PApplet;
import processing.core.PVector;

public class Stage extends InteractiveDisplayObject{
	
	public static PApplet p5;
	public boolean dragging;
	public InteractiveDisplayObject selected;
	
	
	public Stage(){
		super();
		
		p5 = CatsEye.p5;
		globalPosition = new PVector(0,0);
		
		selected = null;
		stage = this;
	}
	
	public void click(PVector i_mousePos) {
		InteractiveDisplayObject clicked = getChildAtPoint(i_mousePos);
		
		if(clicked != null)
			clicked.click(i_mousePos);
	}

	public void mousePressed(PVector i_mousePos) {
		InteractiveDisplayObject selected = getChildAtPoint(i_mousePos);
		
		if(selected != null){
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
		
		if(selected != null && dragging)
			selected.mouseDragged(i_mousePos);
		
		for(InteractiveDisplayObject child : children){
			child.updateMouse(i_mousePos);
		}
		
	}

}
