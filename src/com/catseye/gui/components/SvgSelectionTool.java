package com.catseye.gui.components;

import com.catseye.gui.p5Plugs.SVGControls;
import com.catseye.util.svgUtils.SVGLoader;
import com.quickdrawProcessing.display.Stage;

import controlP5.ControlEvent;
import processing.core.PShape;
import processing.core.PVector;

public class SvgSelectionTool extends ImageSelectionTool {

	SVGLoader loader;
	SVGControls svgControls;
	int currentChild;
	boolean doRefresh = false;
	
	public SvgSelectionTool(PVector i_position, PVector i_size, PShape i_shape, int i_selectionMethod) {
		super(i_position, i_size);
		bounds = i_size.get();
		bounds.y -= 80;
		
		loader = new SVGLoader(i_shape, 4);
		
		selectionMethod = i_selectionMethod;
	}
	
	@Override
	public void addedToStage(){
		setTexture(loader.draw());
		super.addedToStage();
		svgControls = new SVGControls(this, Stage.cp5);
	}
	
	public void editSvg(boolean i_editSvg){
		loader.editMode(i_editSvg);
		setTexture(loader.draw());
		svgControls.showSVGControls(i_editSvg);	
		svgControls.setLayers(loader.getLayers());
	}
	
	@Override
	public void update(){
		if(loader.isDirty() || doRefresh){
			refreshImage();
			loader.clean();
			doRefresh = false;
		}
	}
	
	public void refreshImage(){
		setTexture(loader.draw());
		svgControls.setButtonValuesAndColors(loader.getLayerSettings(currentChild));
	}
	
	public void selectChild(int i_child){
		currentChild = i_child;
		svgControls.setButtonValuesAndColors(loader.getLayerSettings(i_child));
	}
	
	public void useFill(boolean i_useFill){
		loader.setUseFill(currentChild, i_useFill);
		doRefresh = true;
	}
	
	public void useStroke(boolean i_useStroke){
		loader.setUseStroke(currentChild, i_useStroke);
		doRefresh = true;
	}
	
	public void setStrokeWeight(int i_strokeWeight){
		loader.setStrokeWeight(currentChild, i_strokeWeight);
		doRefresh = true;
	}
	
	public void setFill(){
		loader.colorSelect(SVGLoader.FILL_BUTTON, currentChild);
	}
	
	public void setStroke(){
		loader.colorSelect(SVGLoader.STROKE_BUTTON, currentChild);
	}
	
	
	@Override
	public void controlEvent(ControlEvent i_controller) {
		if (i_controller.isFrom(svgControls.getLayerDropdown())){
			  selectChild((int)i_controller.getGroup().getValue());
		}
	}

	public void destroy(){
		svgControls.destroy();
	}
	
}
