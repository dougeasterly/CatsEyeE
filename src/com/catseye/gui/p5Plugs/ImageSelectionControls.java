package com.catseye.gui.p5Plugs;

import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.gui.Cp5Plug;

import controlP5.Button;
import controlP5.ControlP5;

public class ImageSelectionControls extends Cp5Plug {

	public ImageSelectionControls(InteractiveDisplayObject i_parent, ControlP5 i_cp5) {
		super(i_parent, i_cp5);
	}

	@Override
	protected void setupControls(InteractiveDisplayObject i_object) {

		Button loadImageBtn = cp5.addButton("load image")
		.setPosition(20, 20)
		.setSize(80,20);
		
		i_object.addCP5Control(loadImageBtn, "loadImage");
	}

}
