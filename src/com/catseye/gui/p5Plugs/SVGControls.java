package com.catseye.gui.p5Plugs;

import java.util.ArrayList;

import com.catseye.util.svgUtils.SvgLayerSettings;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.gui.Cp5Plug;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.DropdownList;
import controlP5.ListBox;
import controlP5.Numberbox;
import controlP5.Toggle;

public class SVGControls extends Cp5Plug{

	
	private DropdownList layers;
	private Button fillColorButton, strokeColorButton;
	private Toggle editSVGToggle, fillToggle, strokeToggle;
	private InteractiveDisplayObject listener;
	private Numberbox strokeWeightNumberBox;
	
	public SVGControls(InteractiveDisplayObject i_parent, ControlP5 i_cp5) {
		super(i_parent, i_cp5);
	}

	@Override
	protected void setupControls(InteractiveDisplayObject i_object) {
		
		listener = i_object;
		
		float yPos = i_object.getSize().y - 40;
		
		editSVGToggle = cp5.addToggle("edit SVG")
			.setPosition(20, yPos)
				.setSize(20,20);
		
		i_object.addCP5Control(editSVGToggle, "editSvg");
		
		
		layers = cp5.addDropdownList("layers")
			.setPosition(60, yPos+20)
				.setSize(60, 60)					
					.setItemHeight(20)
						.setBarHeight(20);
		
		layers.captionLabel().style().marginTop = 5;
		layers.captionLabel().style().marginLeft = 3;
		layers.valueLabel().style().marginTop = 3;
		layers.scroll(0);
				
		i_object.addCP5Control(layers);
		
		fillToggle = cp5.addToggle("fill")
			.setPosition(140, yPos)
				.setSize(20, 20);
		
		i_object.addCP5Control(fillToggle, "useFill");

		fillColorButton = cp5.addButton("fill color")
			.setPosition(180, yPos)
				.setSize(60, 20);
			
		i_object.addCP5Control(fillColorButton, "setFill");
		
		strokeToggle = cp5.addToggle("stroke")
			.setPosition(260, yPos)
				.setSize(20, 20)
					.setValue(1);
		
		
		i_object.addCP5Control(strokeToggle, "useStroke");
		
		strokeColorButton = cp5.addButton("stroke color")
			.setPosition(300, yPos)
				.setSize(60, 20)
					.setValue(1);
			
		i_object.addCP5Control(strokeColorButton, "setStroke");
		
		strokeWeightNumberBox = cp5.addNumberbox("stroke weight")
			.setPosition(380, yPos)
				.setSize(60,20)
					.setRange(0, 20);
		
		i_object.addCP5Control(strokeWeightNumberBox, "setStrokeWeight");

		
		
		showSVGControls(false);
		
	}
	
	public DropdownList getLayerDropdown(){
		return layers;
	}

	public void showSVGControls(boolean i_editSvg) {
		
		if(i_editSvg){
			
			layers.show();
			fillToggle.show();
			fillColorButton.show();
			strokeToggle.show();
			strokeColorButton.show();
			strokeWeightNumberBox.show();
			
		}else{
			
			layers.hide();
			fillToggle.hide();
			fillColorButton.hide();
			strokeToggle.hide();
			strokeColorButton.hide();
			strokeWeightNumberBox.hide();
		}
		
	}
	
	public void setButtonValuesAndColors(SvgLayerSettings i_layer){
		fillColorButton.setColorBackground(i_layer.getFill());
		strokeColorButton.setColorBackground(i_layer.getStroke());
		fillToggle.setValue(i_layer.getUseFill());
		strokeToggle.setValue(i_layer.getUseStroke());
		strokeWeightNumberBox.setValue(i_layer.getStrokeWeight());
	}
	
	public void setLayers(ArrayList<SvgLayerSettings> i_layers){
				
		for(int i = 0; i < i_layers.size(); ++i){
			layers.addItem("layer "+i, i);
		}
	}	

	public void destroy(){
		
		layers.remove();
		fillToggle.remove();
		fillColorButton.remove();
		strokeToggle.remove();
		strokeColorButton.remove();
		strokeWeightNumberBox.remove();
		editSVGToggle.remove();
		
	}
	
}
