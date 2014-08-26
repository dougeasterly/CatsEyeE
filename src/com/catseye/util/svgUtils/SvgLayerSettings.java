package com.catseye.util.svgUtils;

import java.awt.Color;

import com.quickdrawProcessing.display.Stage;

public class SvgLayerSettings {

	boolean useFill;
	int fillColor;
	boolean useStroke;
	int strokeColor; 
	float strokeWeight;
	
	
	public SvgLayerSettings(){
		useFill = true;
		useStroke = true;
		strokeWeight = 1;
		setFill(0);
		setStroke(0);
	}
	
	public SvgLayerSettings(int i_layer, int i_totalLayers){
		useFill = true;
		useStroke = true;
		strokeWeight = 1;
		
		float fill = (i_layer/(i_totalLayers-1.0f))*255;
		setFill(fill, fill, fill);
		setStroke(fill, fill, fill);
	}
	
	
	public boolean getUseStroke(){
		return useStroke;
	}
	
	public boolean getUseFill(){
		return useFill;
	}
	
	public float getStrokeWeight(){
		return strokeWeight;
	}
	
	public int getFill(){
		return fillColor;
	}
	
	public int getStroke(){
		return strokeColor;
	}
	
	
	public void setStrokeWeight(float i_strokeWeight){
		strokeWeight = i_strokeWeight;
	}
	
	public void setUseStroke(boolean i_useStroke){
		useStroke = i_useStroke;
	}
	
	public void setUseFill(boolean i_useFill){
		useFill = i_useFill;
	}
	
	public void setStroke(int i_color){
		strokeColor = i_color;
	}
	
	public void setStroke(float i_r, float i_g, float i_b){
		strokeColor = Stage.p5.color(i_r, i_g, i_b);
	}

	public void setStroke(float i_r, float i_g, float i_b, float i_a){
		strokeColor = Stage.p5.color(i_r, i_g, i_b, i_a);
	}
	
	public void setStroke(Color i_color){
		strokeColor = Stage.p5.color(i_color.getRed(), i_color.getGreen(), i_color.getBlue());
	}

	public void setFill(int i_color){
		fillColor = i_color;
	}
	
	public void setFill(float i_r, float i_g, float i_b){
		fillColor = Stage.p5.color(i_r, i_g, i_b);
	}

	public void setFill(float i_r, float i_g, float i_b, float i_a){
		fillColor = Stage.p5.color(i_r, i_g, i_b, i_a);
	}
	
	public void setFill(Color i_color){
		fillColor = Stage.p5.color(i_color.getRed(), i_color.getGreen(), i_color.getBlue());
	}
	
	
}
