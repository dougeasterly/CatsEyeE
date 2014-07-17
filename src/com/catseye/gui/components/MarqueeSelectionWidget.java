package com.catseye.gui.components;

import processing.core.PGraphics;
import processing.core.PVector;

public class MarqueeSelectionWidget extends ImageSelectionWidget {

	private static final int HANDLE_SIZE = 10;
	private static final int EDGE_SIZE = 2;
	
	PVector TL, BR;

	public MarqueeSelectionWidget( PVector i_position, PVector i_size) {
		super(i_position, i_size);
		TL = new PVector(0,0);
		BR = i_size.get();
	}
	
	public boolean isOver(PVector i_mousePosition){
		
		PVector mouseLocal = globalToLocal(i_mousePosition);		
		return true;
	}
	
	public void draw(PGraphics i_context){		
		
	}


}
