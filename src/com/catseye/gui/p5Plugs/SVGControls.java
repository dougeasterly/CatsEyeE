package com.catseye.gui.p5Plugs;

import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.gui.Cp5Plug;

import controlP5.ControlP5;
import controlP5.Toggle;

public class SVGControls extends Cp5Plug {

	public SVGControls(InteractiveDisplayObject i_parent, ControlP5 i_cp5) {
		super(i_parent, i_cp5);
		
	}

	@Override
	protected void setupControls(InteractiveDisplayObject i_object) {
		
		float yPos = i_object.getSize().y-40;
		
		Toggle editSVGToggle = cp5.addToggle("edit SVG")
			.setPosition(20, yPos)
				.setSize(20,20);
		
		i_object.addCP5Control(editSVGToggle, "editSVG");
		
		
		
	}

	public void showSVGControls(boolean i_editSvg) {
		
		
		
	}

}
