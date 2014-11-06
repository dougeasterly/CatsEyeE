package com.catseye.gui.components.selectionPanes;

import processing.core.*;
import controlP5.*;

public class StandardSelectionPane extends GridSelectionPane {

	
	
	public StandardSelectionPane(PApplet i_parent, PVector i_size, ControlP5 i_cp5){
		super(i_parent, i_size, i_cp5);
		
		addButton("com.catseye.patternComponents.gridGenerators.regularGrids.HexGrid");
		addButton("com.catseye.patternComponents.gridGenerators.regularGrids.TriGrid");
		addButton("com.catseye.patternComponents.gridGenerators.regularGrids.SquareGrid");

		createControls();
	}
	
	private void createControls(){
				
	}
	
}
