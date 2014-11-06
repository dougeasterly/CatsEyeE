package com.catseye.gui.components.selectionPanel;


import processing.core.*;

import com.catseye.gui.components.GridSelectionButton;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.DisplayPane;


public class GridSelectionPanel extends DisplayPane{
	
	public static final PVector BUTTONSIZE = new PVector(100,100);
	public static final PVector BUTTONSPACE = new PVector(20,20);

	
	protected PVector scrollOffset;
	protected int columns;
	//protected ArrayList<GridSelectionButton> buttons;
	protected int buttonCount;
	
	protected GridSelectionButton selected_btn;
	protected DisplayPane buttonPane; 
	
	
	public GridSelectionPanel(PVector i_position, PVector i_size){
		
		super(i_position, i_size);
		
		buttonPane  = new DisplayPane(new PVector(0,0), i_size);
		buttonCount = 0;
		columns = PApplet.floor((size.x-BUTTONSPACE.x) / (BUTTONSIZE.x+BUTTONSPACE.x));
		scrollOffset = new PVector(0,0);
	}

	@Override
	public void addedToStage(){
		addChild(buttonPane);
	}
	
	public void addButton(String i_gridClassName){
		
		float buttonX = BUTTONSPACE.x + (buttonCount%columns)*(BUTTONSIZE.x+BUTTONSPACE.x);
		float buttonY = BUTTONSPACE.y + PApplet.floor(buttonCount/columns)*(BUTTONSIZE.y+BUTTONSPACE.y);
		
		PVector nextPos = new PVector(buttonX ,buttonY);
		
		
		GridSelectionButton button = new GridSelectionButton(
				nextPos,
				BUTTONSIZE,
				i_gridClassName,
				TileGrid.getGridMiniPreview(i_gridClassName, BUTTONSIZE)
				);
		
		button.setInteractionHandler(parent);
		
		++buttonCount;
		
		buttonPane.addChild(button);

	}

//	public void scroll(PVector i_amount){
//		
//		int rows = PApplet.ceil(buttonCount/(columns+0.0f));
//		
//		if((rows*(BUTTONSIZE.y+BUTTONSPACE.y)+BUTTONSPACE.y*2) > size.y){
//			scrollOffset.y += i_amount.y;
//			
//			scrollOffset.y = PApplet.constrain(scrollOffset.y, -((rows*(BUTTONSIZE.y+BUTTONSPACE.y)+BUTTONSPACE.y*2) - size.y), 0);
//		}
//	}

	@Override
	public boolean isOver(PVector i_position) {
		return inBounds(i_position);
	}
	
	@Override
	public void click(PVector i_position){
		

			System.out.println("addingChild");
		
	}
}
