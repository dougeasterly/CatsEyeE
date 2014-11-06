package com.catseye.gui.p5Plugs;

import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.display.Stage;
import com.quickdrawProcessing.gui.Cp5Plug;

import controlP5.Button;
import controlP5.CColor;
import controlP5.ControlP5;
import controlP5.Textfield;
import controlP5.Toggle;

public class ImageDisplayControls extends Cp5Plug {

	public ImageDisplayControls(InteractiveDisplayObject i_parent, ControlP5 i_cp5) {
		super(i_parent, i_cp5);
	}

	@Override
	protected void setupControls(InteractiveDisplayObject i_object) {
				
		float xPos = 20; 
		Toggle gridToggle = cp5.addToggle("grid")
			.setPosition(xPos, 20)
				.setSize(20,20)
					.setColorBackground(Stage.p5.color(200,200,200));
				
		i_object.addCP5Control(gridToggle, "drawGrid");
	    

		Button saveImageBtn = cp5.addButton("save Image")
	    	.setPosition(60, 20)
				.setSize(80,20);
		    
		i_object.addCP5Control(saveImageBtn, "saveImage");
		
		
		Button saveSettingsBtn = cp5.addButton("save settings")
			.setPosition(160, 20)
				.setSize(80,20);
	    
		i_object.addCP5Control(saveSettingsBtn, "saveSettings");
		
			
	}

}
