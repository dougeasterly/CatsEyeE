package com.catseye.gui.components.selectionPanes;

import java.util.ArrayList;

import processing.core.*;

import com.catseye.gui.components.GridSelectionButton;
import com.catseye.patternComponents.gridGenerators.TileGrid;

import controlP5.ControlP5;

public class GridSelectionPane {
	
	public static final PVector BUTTONSIZE = new PVector(100,100);
	public static final PVector BUTTONSPACE = new PVector(20,20);

	
	protected PApplet parent;
	protected PVector size, scrollOffset;
	protected int columns;
	protected ArrayList<GridSelectionButton> buttons;
	
	protected ControlP5 cp5;
	protected GridSelectionButton selected_btn;
	
	
	public GridSelectionPane(PApplet i_parent, PVector i_size, ControlP5 i_cp5){	
		cp5 = i_cp5;
		parent = i_parent;
		size = i_size;
		columns = PApplet.floor((size.x-BUTTONSPACE.x) / (BUTTONSIZE.x+BUTTONSPACE.x));
		scrollOffset = new PVector(0,0);
		buttons = new ArrayList<GridSelectionButton>();
	}
	
	public void addButton(String i_gridClassName){
		
		float buttonX = BUTTONSPACE.x + (buttons.size()%columns)*(BUTTONSIZE.x+BUTTONSPACE.x);
		float buttonY = BUTTONSPACE.y + PApplet.floor(buttons.size()/columns)*(BUTTONSIZE.y+BUTTONSPACE.y);
		
		PVector nextPos = new PVector(buttonX ,buttonY);
		
		
		buttons.add(new GridSelectionButton(parent,
				i_gridClassName,
				BUTTONSIZE,
				nextPos,
				TileGrid.getGridMiniPreview(i_gridClassName, BUTTONSIZE)
				));

	}
	
	public void scroll(PVector i_amount){
		
		
		
		int rows = PApplet.ceil(buttons.size()/(columns+0.0f));
		
		if((rows*(BUTTONSIZE.y+BUTTONSPACE.y)+BUTTONSPACE.y*2) > size.y){
			scrollOffset.y += i_amount.y;
			
			scrollOffset.y = PApplet.constrain(scrollOffset.y, -((rows*(BUTTONSIZE.y+BUTTONSPACE.y)+BUTTONSPACE.y*2) - size.y), 0);
		}
	}
	
	public void drawButtons(){	
			
		for(int i = 0; i < buttons.size(); ++i){
			buttons.get(i).draw(scrollOffset);
		}
		
	}
	
	public String click(PVector i_point){
	
		for(GridSelectionButton i : buttons){
			if(i.isPtOver(i_point, scrollOffset)){
				
				if(selected_btn != null)
					selected_btn.setSelected(false);
				
				i.setSelected(true);
				selected_btn = i;
				return i.getType();
			}
		}
		
		return null;
	}
	
}
