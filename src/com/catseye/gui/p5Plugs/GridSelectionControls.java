package com.catseye.gui.p5Plugs;

import processing.core.PImage;

import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.gui.Cp5Plug;

import controlP5.Button;
import controlP5.ControlP5;
import controlP5.Numberbox;
import controlP5.Slider;
import controlP5.Toggle;

public class GridSelectionControls extends Cp5Plug {

	private Button maskImageButton;
	Numberbox cellSizeControl;
	Slider oddsControl;
	Toggle useMaskControl;
	
	public GridSelectionControls(InteractiveDisplayObject i_parent,
			ControlP5 i_cp5) {
		super(i_parent, i_cp5);
	}

	@Override
	protected void setupControls(InteractiveDisplayObject i_object) {
		  
		  cellSizeControl = cp5.addNumberbox("cellsize")
		  	.setPosition(20, 20)
		  	.setSize(80, 20)
		  	.setLabel("Cell Size")
		  	.setMin(10)
		  	.setMax(1000)
		  	.setValue(150);
		  	
		  	i_object.addCP5Control(cellSizeControl, "cellSize");
		  
		   oddsControl = cp5.addSlider("missing odds")
		     .setPosition(120, 20)
		     .setSize(80, 20)
		     .setRange(0, 1);
		     
		  i_object.addCP5Control(oddsControl, "missingOdds");

		  
		  useMaskControl = cp5.addToggle("useMask")
		  	.setPosition(20, 60)
		  	.setSize(20, 20);
		  
		  i_object.addCP5Control(useMaskControl, "useMask");

		  maskImageButton = cp5.addButton("loadMask")
			.setPosition(120, 60)
			.setSize(80, 80);
		  
		  i_object.addCP5Control(maskImageButton, "loadMask");
	}
	
	public void setMaskImage(PImage i_maskImage){
			       
	    PImage tempImg = i_maskImage.get();
	    
	    if (tempImg.width > tempImg.height)
	       tempImg.resize(80, 0);
	    else
	       tempImg.resize(0, 80);
	   
	    maskImageButton.setImage(tempImg);
	}
	
	public void setSettingsFromGrid(TileGrid i_grid){
		
		PImage mskImg = i_grid.getMaskImage();
		
		if(mskImg != null)
			maskImageButton.setImage(mskImg);
		
		cellSizeControl.setValue(i_grid.getCellRadius());
		oddsControl.setValue(i_grid.getMissingOdds());
		useMaskControl.setValue(i_grid.getUseMask());
		
	}

}
