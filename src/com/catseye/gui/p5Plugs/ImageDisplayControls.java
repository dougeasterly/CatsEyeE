package com.catseye.gui.p5Plugs;

import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.gui.Cp5Plug;
import controlP5.ControlP5;
import controlP5.Toggle;

public class ImageDisplayControls extends Cp5Plug {

	public ImageDisplayControls(InteractiveDisplayObject i_parent, ControlP5 i_cp5) {
		super(i_parent, i_cp5);
	}

	@Override
	protected void setupControls(InteractiveDisplayObject i_object) {
		
		float xPos = parent.getSize().x-40; 
		Toggle gridToggle = cp5.addToggle("show grid")
		.setPosition(xPos, 20)
		.setSize(20,20);
				
		i_object.addCP5Control(gridToggle, "drawGrid");
				
	}

}
