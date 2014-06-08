package com.catseye.util;

import processing.core.*;

import com.catseye.CatsEye;

import java.util.ArrayList; 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

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
	
	private boolean dirty = false;
	
	//graphics
	public PImage display;
	protected PShape mySvg; // main SVG
	protected PShape [] children; // SVG layers
	private int svgWidth, svgHeight;
	private  PGraphics svgCanvas; // redrawing the SVG children 
    public int childrenNum;
	private float scale = 1.0f;
	
	//color
	private int p5Color;
	private Boolean fillOn = true;
	ArrayList<Boolean> fillBooleans = new ArrayList<Boolean>();
	ArrayList<Color> fillColors = new ArrayList<Color>();
	private Boolean strokeOn = true;
	ArrayList<Boolean> strokeBooleans = new ArrayList<Boolean>();
    ArrayList<Color> strokeColors = new ArrayList<Color>(); 
	ArrayList<Float> strokeWeights = new ArrayList<Float>();

	private boolean foreground;
	private int svgLayer;

	private ColourChooserWindow colorChooserWindow;
	
	
	 public SVGLoader( PShape tSVG, float i_scale)
	  {
		 
	  colorChooserWindow = new ColourChooserWindow();
		
	   scale = i_scale;
	
	   mySvg = tSVG; // the selected svg image
	   //storing the svg layers/children
	   childrenNum = mySvg.getChildCount();
	   children =  new PShape[childrenNum];
	   for (int i = 0; i<childrenNum; i++) {
		  children[i] = mySvg.getChild(i);
		  children[i].scale(scale);
	   }
	   
	   svgWidth = (int)(mySvg.width*scale);
	   svgHeight = (int)(mySvg.height*scale);	  
	   svgCanvas = CatsEye.p5.createGraphics( svgWidth, svgHeight, PApplet.JAVA2D);
		  
	  }
	 
//	 public void setFillOn(){
//		 fillOn = !fillOn;
//	 }
//	 
//	 public void setStrokeOn(){
//		 strokeOn = !strokeOn;
//	 }
//	 public boolean getFillOn(){
//		 return fillOn ;
//	 }
//	 
//	 public boolean getStrokeOn(){
//		 return strokeOn;
//	 }
	 
	 public int getChildrenNum(){
	 return childrenNum;
	 }
	 
      //draw the svg image with default color settings
	  public PImage drawRaw () {
		  
		  svgCanvas.clear();
		  
		  svgCanvas.beginDraw();
		  svgCanvas.smooth();
		  svgCanvas.shape(mySvg, 0, 0);
		  svgCanvas.endDraw();
		  display = svgCanvas.get();
		  return display;
	  
	  }
	  
	  public PImage init () {
		  if(fillColors.isEmpty()!=true)
			  fillColors.clear();
		  if(strokeColors.isEmpty()!=true)
			  strokeColors.clear();
		  if(strokeWeights.isEmpty()!=true)
			  strokeWeights.clear();
 
	    mySvg.disableStyle();
	    svgCanvas.clear();
	    svgCanvas.beginDraw();
	    svgCanvas.smooth();
	    svgCanvas.strokeWeight = 2.0f;
		
	    for (int i = 0; i<=childrenNum-1; i++) {
	    	final int cfScaling = (int)(255/childrenNum);
	    	final int csScaling = (int)((255/childrenNum)*.90);
	    	fillColors.add(new Color(i*cfScaling, i*cfScaling, i*cfScaling));
	    	strokeColors.add(new Color(i*csScaling, i*csScaling, i*csScaling));
	    	fillBooleans.add(true);
	    	strokeBooleans.add(true);
	    	strokeWeights.add(2.0f);
	    	final Color ff = fillColors.get(i);
	    	final Color sf = strokeColors.get(i);
	    	svgCanvas.fill(ff.getRed(), ff.getGreen(), ff.getBlue());
	    	svgCanvas.stroke(sf.getRed(), sf.getGreen(), sf.getBlue());
	    	svgCanvas.shape(children[i], 0, 0);
	    }
	    
	    svgCanvas.endDraw();
	    display = svgCanvas.get();
	    return display;
	  }
	  
	  
	  // returning fill state
	  public Boolean getFillBoolean (int t_child){
	    	final Boolean tfb = fillBooleans.get(t_child);
	    	return tfb;	
	  }
	  
	  
	  public Color getFillColor (int t_child){
	    	final Color ff = fillColors.get(t_child);
	    	return ff;	
	  }

	  
	  //returning stroke state
	  public Boolean getStrokeBoolean (int t_child){
	    	final Boolean sfb = strokeBooleans.get(t_child);
	    	return sfb;	
	  }
	  
	  public Color getStrokeColor (int t_child){
	    	final Color sf = strokeColors.get(t_child);
	          return sf;
	  }
	  
	  public float getStrokeWeight (int t_child){
	    	final float sw = strokeWeights.get(t_child);
	          return sw;
	  }


	  
	  public Boolean setFillBoolean (int t_child){
	    	final Boolean tfb = fillBooleans.get(t_child);
	    	fillBooleans.set(t_child, !tfb);
	    	return !tfb;	
	  }
	  
	  public Boolean setStrokeBoolean (int t_child){
	    	final Boolean tsb = strokeBooleans.get(t_child);
	    	strokeBooleans.set(t_child, !tsb);
	          return !tsb;
	  }
	  	
	  public void setStrokeWeight (float tNum, int t_child){
	       System.out.println(tNum);

		  strokeWeights.set(t_child, tNum);

	  }
	  

	  public boolean isDirty(){
		  return dirty;
	  }
	  
	  public void clean(){
		  dirty = false;
	  }
	  
	  
	  public PImage updateSvgView() {
	    
		svgCanvas.clear();
	    svgCanvas.beginDraw();
	    svgCanvas.smooth();
	    
	    for (int i = 0; i<=childrenNum-1; i++) {
	    	final Color ff = fillColors.get(i);
	    	final Color sf = strokeColors.get(i);
	    	final Boolean tF = fillBooleans.get(i);
	    	final Boolean tS = strokeBooleans.get(i);
	    	final float tSW = strokeWeights.get(i);
	    	
	    	if(tF == true){
	    	svgCanvas.fill(ff.getRed(), ff.getGreen(), ff.getBlue());
	    	}else{
	    	svgCanvas.noFill();	
	    	}
	    	if(tS == true){
	    	svgCanvas.stroke(sf.getRed(), sf.getGreen(), sf.getBlue());
	    	svgCanvas.strokeWeight(tSW);
	        System.out.println(tSW);

	    	}else{
	    	svgCanvas.noStroke();
	    	}
	    	
	    	svgCanvas.shape(children[i], 0, 0);
	    	}
	    
	    svgCanvas.endDraw();
	    display = svgCanvas.get();
	    
	  	dirty = true;
	    
	    return display;
	  }
	  
	    
	  public void colorSelect (String i_buttonType, int i_svgLayer) {
		  foreground = i_buttonType.equals("fc");
		  svgLayer = i_svgLayer;
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

		  	if(foreground){
				 fillColors.set(svgLayer, color);	
		    }else{
				 strokeColors.set(svgLayer, color);				 
			}
		  	
		  	updateSvgView();
    
    	}
    	
    	public void stateChanged(ChangeEvent e) {
    		System.out.println("BOINK");
    		update();
    	}

		public void actionPerformed(ActionEvent arg0) {
			update();
		}

    	

	
    	
    	
    	
	  }


}


