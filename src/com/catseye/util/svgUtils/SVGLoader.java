package com.catseye.util.svgUtils;

import processing.core.*;

import com.quickdrawProcessing.display.Stage;

import java.util.ArrayList; 
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*---------------------------------------------------------------------------------------------
*
*    SVGTile
*    for loading and colouring SVG images. 
*
*    Ben Jack 12/4/2014 
*
*---------------------------------------------------------------------------------------------*/



public class SVGLoader{
	
	public static final int FILL_BUTTON = 0; 
	public static final int STROKE_BUTTON = 1;
	
	private boolean dirty, disabledStyle = false;
	private boolean editing, editModeSetup = false;
	
	private int svgWidth, svgHeight;
	private float scale = 1.0f;
	
	//graphics
	public PImage display;
	private  PGraphics svgCanvas; 
	protected PShape mySvg;
	
	private PShape[] children;
	private int childCount;
	private ArrayList<SvgLayerSettings> layerSettings;
	
	//color
	private int pressedButtonType;
	private int selectedLayer;

	private ColourChooserWindow colorChooserWindow;
	
	
	 public SVGLoader( PShape i_SVG, float i_scale)
	 {
		 
	   colorChooserWindow = new ColourChooserWindow();
	   layerSettings = new ArrayList<SvgLayerSettings>();
	   
	   scale = i_scale;
	
	   mySvg = i_SVG; 
	   childCount = mySvg.getChildCount();

	   children =  new PShape[childCount];
	   for (int i = 0; i<childCount; i++) {
		  children[i] = mySvg.getChild(i);
		  children[i].scale(scale);
	   }
	   
	   svgWidth = (int)(mySvg.width*scale);
	   svgHeight = (int)(mySvg.height*scale);	  
	   svgCanvas = Stage.p5.createGraphics( svgWidth, svgHeight, PApplet.JAVA2D);
		  
	 }
	 
	 public void editMode(boolean i_useEditMode){
		 editing = i_useEditMode;
		 disabledStyle = editing;
		
		 if(editing) mySvg.disableStyle();
		 else mySvg.enableStyle();
		 
		 if(editing && !editModeSetup)
			 setupEditMode();
	 }
	
	 
	 public PImage draw(){
		 return editing ? drawEdited() : drawRaw();
	 }
	 
	  public PImage drawRaw() {
		  
		  svgCanvas.clear();
		  svgCanvas.beginDraw();
		  svgCanvas.smooth();
		  svgCanvas.shape(mySvg, 0, 0);
		  svgCanvas.endDraw();
		  
		  display = svgCanvas.get();
		  return display;
	  
	  }
	  
	  
	  public PImage drawEdited(){
			
		  	svgCanvas.beginDraw();
			svgCanvas.clear();
			svgCanvas.smooth();
		    
			for( int i = 0; i < childCount; ++i){
				
				SvgLayerSettings layer = layerSettings.get(i);
				
				svgCanvas.fill(layer.getFill());
		    	svgCanvas.stroke(layer.getStroke());
		    	svgCanvas.strokeWeight(layer.getStrokeWeight());
		    	
		    	if(!layer.getUseFill()){
		    		svgCanvas.noFill();
		    		svgCanvas.fill(0,0);
		    	}
		    	
		    	if(!layer.getUseStroke()){
		    		svgCanvas.noStroke();
		    		svgCanvas.stroke(0,0);
		    	}
		    	
		    	svgCanvas.shape(children[i], 0, 0);
			}
			
			svgCanvas.endDraw();
			display = svgCanvas.get();
			
			dirty = true;
			
			return display;
			
	  }
	  
	  public void setupEditMode(){
		
	    for (int i = 0; i<childCount; ++i) {
	    	SvgLayerSettings layer = new SvgLayerSettings(i, childCount);
	    	layerSettings.add(layer);
	    }
	    
	    editModeSetup = true;
	    
	  }
	  
	  
	  public ArrayList<SvgLayerSettings> getLayers(){
		  return layerSettings;
	  }
	  
	  public SvgLayerSettings getLayerSettings(int i_layer){
		  return layerSettings.get(i_layer);
	  }
	  
	 public int getChildCount(){
		 return childCount;
	 }
	  
	  public Boolean getUseFill(int i_child){
	    	return layerSettings.get(i_child).getUseFill();	
	  }
	  
	  public int getFillColor (int i_child){
	    	return layerSettings.get(i_child).getFill();	
	  }
	  
	  public Boolean getUseStroke(int i_child){
		  return layerSettings.get(i_child).getUseStroke();	
	  }
	  
	  public int getStrokeColor (int i_child){
		  return layerSettings.get(i_child).getStroke();	
	  }
	  
	  public float getStrokeWeight (int i_child){
		  return layerSettings.get(i_child).getStrokeWeight();	
	  }

	  public void setUseFill(int i_child, boolean i_useFill){
	    	layerSettings.get(i_child).setUseFill(i_useFill);
	  }
	  
	  public void setUseStroke(int i_child, boolean i_useStroke){
	    	layerSettings.get(i_child).setUseStroke(i_useStroke);
	  }
	  	
	  public void setStrokeWeight (int i_child, int i_strokeWeight){
	    	layerSettings.get(i_child).setStrokeWeight(i_strokeWeight);
	  }
	  
	  public void setFill(int i_child, int i_fillColor){
	    	layerSettings.get(i_child).setStrokeWeight(i_fillColor);
	  }

	  public void setStroke(int i_child, int i_strokeColor){
	    	layerSettings.get(i_child).setStrokeWeight(i_strokeColor);
	  }
	  
	  public boolean isDirty(){
		  return dirty;
	  }
	  
	  public void clean(){
		  dirty = false;
	  }
	    
	  public void colorSelect(int i_buttonType, int i_svgLayer) {
		  pressedButtonType = i_buttonType;
		  selectedLayer = i_svgLayer;
		  colorChooserWindow.show();
	  }
	  
	  
	  


      private class ColourChooserWindow extends JFrame implements ChangeListener, ActionListener{
	
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JColorChooser colorPopUpWindow = null;	
    	private JDialog dialog;
    	
    	public ColourChooserWindow(){
    		super();
    		colorPopUpWindow = new JColorChooser();
    		dialog = JColorChooser.createDialog(this,
                    "select colour",
                    false,
                    colorPopUpWindow,
                    this,
                    null);
    		
    		dialog.setVisible(false);
    		colorPopUpWindow.getSelectionModel().addChangeListener(this);
    	}
    	
    	public void show(){
    		dialog.setVisible(true);
    	}
    	
    	private void update(){
    		
    		Color color = colorPopUpWindow.getColor();

		  	if(pressedButtonType == FILL_BUTTON){
		  		 layerSettings.get(selectedLayer).setFill(color);
		    }else if(pressedButtonType == STROKE_BUTTON){
		  		 layerSettings.get(selectedLayer).setStroke(color);
			}
		  	
		  	draw();
		  	
    	}
    	
    	public void stateChanged(ChangeEvent e) {
    		update();
    	}

		public void actionPerformed(ActionEvent arg0) {
			update();
		}
    	
    	
    	
	  } 


}



