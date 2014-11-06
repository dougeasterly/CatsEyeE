package com.quickdrawProcessing.gui;

import java.util.ArrayList;

import com.quickdrawProcessing.display.InteractiveDisplayObject;

import controlP5.ControlP5;
import controlP5.Controller;

@SuppressWarnings("rawtypes")
public abstract class Cp5Plug {

	protected ControlP5 cp5;
	protected InteractiveDisplayObject parent;
	protected ArrayList<Controller> controls;
	
	public Cp5Plug(InteractiveDisplayObject i_parent, ControlP5 i_cp5){
		controls = new ArrayList<Controller>();
		cp5 = i_cp5;
		parent = i_parent;
		setupControls(i_parent);
	}
	
	public ArrayList<Controller> getControls(){
		return controls;
	}
	
	protected abstract void setupControls(InteractiveDisplayObject i_object);
	
	
}
