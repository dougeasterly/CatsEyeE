package com.catseye.gui.guiPanes;

import processing.core.PVector;

import com.catseye.gui.components.selectionPanel.GridSelectionPanel;
import com.catseye.gui.components.selectionPanel.StandardSelectionPanel;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.DisplayPane;


public class GridSelectPane extends DisplayPane {

	
	 protected GridSelectionPanel[] gridSelections;

	 protected float cellSize;
	 protected boolean useMask;
	 protected float missingOdds;
	 
	 protected TileGrid currentGrid;
	 
	 protected boolean scrolling;
	 
	 
	
	public GridSelectPane(PVector i_position, PVector i_size) {
		super(i_position, i_size);
		gridSelections = new GridSelectionPanel[1];
		gridSelections[0] = new StandardSelectionPanel(new PVector(0,100), new PVector(size.x, size.y-100 ));
	}
	
	@Override
	public void addedToStage(){
		addChild(gridSelections[0]);		
	}
	

	public TileGrid getTileGrid(){
		return currentGrid;
	}
	  
	

}
