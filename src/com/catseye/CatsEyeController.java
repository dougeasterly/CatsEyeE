package com.catseye;

import processing.core.PVector;

import com.catseye.gui.guiPanes.GridSelectPane;
import com.catseye.gui.guiPanes.ImageDisplayPane;
import com.catseye.gui.guiPanes.ImageSelectionPane;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.InteractiveDisplayObject;

public class CatsEyeController extends InteractiveDisplayObject{

	public static final int GENERATE = 0;
	
	ImageSelectionPane selector;
	ImageDisplayPane patternDisplay;
	GridSelectPane gridPane;
	
	protected CatsEyeController(PVector i_position, PVector i_size) {
		super(i_position, i_size);
	}
	
	@Override
	public void addedToStage(){
	     selector = new ImageSelectionPane(new PVector(0,0), new PVector(size.x/3.0f, size.y/2.0f));
	     addChild(selector);
	     
	     gridPane = new GridSelectPane(new PVector(0, size.y/2.0f), new PVector(size.x/3.0f, size.y/2.0f));
	     addChild(gridPane);
	     
	     patternDisplay = new ImageDisplayPane(new PVector(size.x/3.0f, 0), new PVector(2.0f*(size.x/3.0f), size.y));
	     addChild(patternDisplay);
	     
	     selector.setInteractionHandler(this);
	     gridPane.setInteractionHandler(this);
	     patternDisplay.setInteractionHandler(this);
	}

	@Override 
	public void actionHook(InteractiveDisplayObject i_child, int i_action){
		
		switch(i_action){
			case GENERATE:
				
				TileGrid grid = gridPane.getTileGrid();
				grid.setTexture(selector.getTexture());
				grid.setTextureCoords(selector.getTexCoords());
				grid.generate();
 				
				patternDisplay.setImages(grid.getRender(), grid.getGridImage());
				
				break;
			default:
				break;
		}
		
	}

	@Override
	public boolean isOver(PVector i_position) {
		return inBounds(i_position);
	}
	
}
