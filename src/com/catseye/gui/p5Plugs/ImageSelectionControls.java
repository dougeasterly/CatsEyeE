package com.catseye.gui.p5Plugs;

import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.gui.Cp5Plug;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.Textfield;

public class ImageSelectionControls extends Cp5Plug {

	Textfield printWidthField, printHeightField;
	Button marqueeToggle;
	
	public ImageSelectionControls(InteractiveDisplayObject i_parent, ControlP5 i_cp5) {
		super(i_parent, i_cp5);
	}

	@Override
	protected void setupControls(InteractiveDisplayObject i_object) {

		Button loadImageBtn = cp5.addButton("load image")
		.setPosition(20, 20)
		.setSize(80,20);
		
		i_object.addCP5Control(loadImageBtn, "loadImage");
		
		
	    printWidthField = cp5.addTextfield("printWidth")
	      .setPosition(120, 20)
	        .setSize(80, 20)
	          .setValue("1000")
	            .setAutoClear(false)
	                .setInputFilter(ControlP5.INTEGER);

	    i_object.addCP5Control(printWidthField, "printWidth");

	    
	    printHeightField = cp5.addTextfield("printHeight")
	      .setPosition(220, 20)
	        .setSize(80, 20)
	          .setValue("1000")
	            .setAutoClear(false)
	                .setInputFilter(ControlP5.INTEGER);
	    
	    i_object.addCP5Control(printHeightField, "printHeight");	
		
	    
	    marqueeToggle = cp5.addButton("Triangle Selector")
			.setPosition(320, 20)
				.setSize(100,20);
	    
		i_object.addCP5Control(marqueeToggle, "toggleMarquee");
		
		Button generateBtn = cp5.addButton("generate")
			.setPosition(440, 20)
				.setSize(80,20);
				
		i_object.addCP5Control(generateBtn, "generate");
		
	}
	
	public void setSettingsFromGrid(TileGrid i_grid){
		printWidthField.setValue((int)i_grid.getRenderSize().x + "");
		printHeightField.setValue((int)i_grid.getRenderSize().y + "");
	}
	
	public void setSelectionMarqueeLabel(String i_label){
		marqueeToggle.setLabel(i_label);
	}

}
