package com.catseye.gui.components.selectionPanel;

import com.catseye.patternComponents.gridGenerators.regularGrids.HexGrid;

import processing.core.*;

public class StandardSelectionPanel extends GridSelectionPanel {

	
	
	public StandardSelectionPanel(PVector i_position, PVector i_size){
		super(i_position, i_size);
		drawBorder = false;
		currentGrid = new HexGrid();
	}
	
	@Override
	public void addedToStage(){

		super.addedToStage();
		addButton("com.catseye.patternComponents.gridGenerators.regularGrids.HexGrid");
		addButton("com.catseye.patternComponents.gridGenerators.regularGrids.TriGrid");
		addButton("com.catseye.patternComponents.gridGenerators.regularGrids.SquareGrid");

		createControls();
	}
	
	private void createControls(){
				
	}
	
}
