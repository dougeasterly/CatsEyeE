package com.catseye.gui.components;

import com.catseye.gui.p5Plugs.SVGControls;
import com.catseye.util.svgUtils.SVGLoader;
import com.quickdrawProcessing.display.Stage;

import processing.core.PShape;
import processing.core.PVector;

public class SvgSelectionTool extends ImageSelectionTool {

	SVGLoader loader;
	SVGControls svgControls;
	
	public SvgSelectionTool(PVector i_position, PVector i_size, PShape i_shape, int i_selectionMethod) {
		super(i_position, i_size);
		bounds = i_size.get();
		bounds.y -= 60;
		
		svgControls = new SVGControls(this, Stage.cp5);
		
		loader = new SVGLoader(i_shape, 4);
		setTexture(loader.draw());
		
		selectionMethod = i_selectionMethod;
	}
	
	public void editSvg(boolean i_editSvg){
		loader.editMode(i_editSvg);
		setTexture(loader.draw());
		svgControls.showSVGControls(i_editSvg);
	}

}
