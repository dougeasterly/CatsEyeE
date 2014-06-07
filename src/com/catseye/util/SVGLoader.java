package com.catseye.util;

import processing.core.*;

import com.catseye.CatsEye;

import java.util.ArrayList; 
import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JFrame;

/*---------------------------------------------------------------------------------------------
*
*    SVGTile
*    for loading and colouring SVG images. 
*
*    Ben Jack 12/4/2014 
*
*---------------------------------------------------------------------------------------------*/



public class SVGLoader {
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
	private JColorChooser colorPopUpWindow = null;	
	private Boolean fillOn = true;
	ArrayList<Boolean> fillBooleans = new ArrayList<Boolean>();
	ArrayList<Color> fillColors = new ArrayList<Color>();
	private Boolean strokeOn = true;
	ArrayList<Boolean> strokeBooleans = new ArrayList<Boolean>();
    ArrayList<Color> strokeColors = new ArrayList<Color>(); 
	ArrayList<Float> strokeWeights = new ArrayList<Float>();


	
	
	 public SVGLoader( PShape tSVG, float i_scale)
	  {
	   mySvg = tSVG; // the selected svg image
	   //storing the svg layers/children
	   childrenNum = mySvg.getChildCount();
	   children =  new PShape[childrenNum];
	   for (int i = 0; i<childrenNum; i++) {
		      children[i] = mySvg.getChild(i);
		    }
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
	  svgWidth = (int)(mySvg.width*scale);
	  svgHeight = (int)(mySvg.height*scale);
		    svgCanvas = CatsEye.p5.createGraphics( svgWidth, svgHeight, PApplet.JAVA2D);
		    svgCanvas.beginDraw();
		    svgCanvas.smooth();
		    svgCanvas.shape(mySvg);
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
	        children[i].scale(scale);
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
	        children[i].scale(scale);
	       svgCanvas.shape(children[i], 0, 0);
	    }
	    
	    svgCanvas.endDraw();
	    display = svgCanvas.get();
	    return display;
	  }
	    
	  public void colorSelect (String buttonType, int svgLayer) {
			final Color tempColor;
			JFrame frame = new JFrame();	
			tempColor = colorPopUpWindow.showDialog(frame, "Color Chooser", Color.white);
				 if(buttonType == "fc"){
					 fillColors.set(svgLayer, tempColor);	
			  }else if(buttonType =="sc"){
					 strokeColors.set(svgLayer, tempColor);				 
				 }
				 }
		// This code stub was here - is this a better way to draw the SVG?	
	    //return CatsEye.p5.createImage(1, 1, PApplet.ARGB);
	  
	
  
}

