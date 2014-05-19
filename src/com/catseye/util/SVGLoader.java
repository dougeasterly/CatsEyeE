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
*    
*    for loading and colouring SVG images. needs re-implementing, probably doesn't work currently
*
*    Ben Jack 12/4/2014 
*
*---------------------------------------------------------------------------------------------*/


//


public class SVGLoader {
	private int select;
	public PImage display;
	protected PShape mySVG; // main SVG
	protected PShape [] children; // SVG layers
	protected int bgColor = CatsEye.p5.color(255,255,255);
    //private int[] fillColors = new int[100];
	ArrayList<Color> fillColors = new ArrayList<Color>();
    ArrayList<Color> strokeColors = new ArrayList<Color>();
	ArrayList<Float> strokeWeights = new ArrayList<Float>();
	private int SVGw, SVGh;
	private  PGraphics SVGCanvas; // redrawing the SVG children 
    public int childrenNum;
	private float scale = 1.0f;
	
	//colors
	private int p5Color;

	private JColorChooser colorPopUpWindow = null;
	
	 public SVGLoader( PShape tSVG, float i_scale)
	  {
	   mySVG = tSVG;
	   childrenNum = mySVG.getChildCount();
	   children =  new PShape[childrenNum];
	   for (int i = 0; i<childrenNum; i++) {
		      children[i] = mySVG.getChild(i);
		    }
	  }
	 
	 public int getChildrenNum(){
	 return childrenNum;
	 }
	 

	  public PImage drawRaw () {
	  SVGw = (int)(mySVG.width*scale);
	  SVGh = (int)(mySVG.height*scale);
		    SVGCanvas = CatsEye.p5.createGraphics( SVGw, SVGh, PApplet.JAVA2D);
		    SVGCanvas.beginDraw();
		    SVGCanvas.smooth();
		 SVGCanvas.shape(mySVG);
		    SVGCanvas.endDraw();
		    display = SVGCanvas.get();
		    return display;
    //return CatsEye.p5.createImage(1, 1, PApplet.ARGB);
		  }
	  
	  public PImage init () {
		  if(fillColors.isEmpty()!=true)
			  fillColors.clear();
		  if(strokeColors.isEmpty()!=true)
			  strokeColors.clear();
		  if(strokeWeights.isEmpty()!=true)
			  strokeWeights.clear();
 
	    mySVG.disableStyle();
	    SVGCanvas.clear();
	    SVGCanvas.beginDraw();
	    SVGCanvas.smooth();
	    SVGCanvas.backgroundColor = bgColor;
	    SVGCanvas.strokeWeight = 2.0f;
		
	    for (int i = 0; i<=childrenNum-1; i++) {
	    	final int cfScaling = (int)(255/childrenNum);
	    	final int csScaling = (int)((255/childrenNum)*.90);
	    	fillColors.add(new Color(i*cfScaling, i*cfScaling, i*cfScaling));
	    	strokeColors.add(new Color(i*csScaling, i*csScaling, i*csScaling));
	    	strokeWeights.add(2.0f);
	    	final Color ff = fillColors.get(i);
	    	final Color sf = strokeColors.get(i);
	    	SVGCanvas.fill(ff.getRed(), ff.getGreen(), ff.getBlue());
	    	SVGCanvas.stroke(sf.getRed(), sf.getGreen(), sf.getBlue());
	        children[i].scale(scale);
	       SVGCanvas.shape(children[i], 0, 0);
	    }
	    SVGCanvas.endDraw();
	    display = SVGCanvas.get();
	    return display;
	  }
	  

	  public int getBGcolor (){
		  return bgColor;
	  }
	  public Color getFillColor (int t_child){
	    	final Color ff = fillColors.get(t_child);
          return ff;	
	  }
	  public Color getStrokeColor (int t_child){
	    	final Color sf = strokeColors.get(t_child);
	          return sf;
	  }
	  public void setStrokeWeight (float tNum, int t_child){
		  strokeWeights.set(t_child, tNum);

	  }
	  

	  
	  
	  
	  public PImage updateSvgView() {
	    SVGCanvas.clear();
	    SVGCanvas.beginDraw();
	    SVGCanvas.smooth();
	    SVGCanvas.backgroundColor = bgColor;
	    SVGCanvas.strokeWeight = 1.0f;
	    
	    for (int i = 0; i<=childrenNum-1; i++) {
	    	final Color ff = fillColors.get(i);
	    	final Color sf = strokeColors.get(i);
	    	SVGCanvas.fill(ff.getRed(), ff.getGreen(), ff.getBlue());
	    	SVGCanvas.stroke(sf.getRed(), sf.getGreen(), sf.getBlue());
	    	SVGCanvas.strokeWeight(strokeWeights.get(i));
	        children[i].scale(scale);
	       SVGCanvas.shape(children[i], 0, 0);
	       CatsEye.p5.println(ff.getRed());
	    }
	    
	    SVGCanvas.endDraw();
	    display = SVGCanvas.get();
	    return display;
	  }
	    
	  public void colorSelect (String buttonType, int svgLayer) {
			final Color tempColor;
			JFrame frame = new JFrame();	
			tempColor = colorPopUpWindow.showDialog(frame, "Color Chooser", Color.white);
				 if(buttonType == "fc"){
					 fillColors.set(svgLayer, tempColor);	
					  CatsEye.p5.println("booya"+fillColors.get(svgLayer));
			  }else if(buttonType =="sc"){
					 strokeColors.set(svgLayer, tempColor);				 
				 }
				 }
			
	    //return CatsEye.p5.createImage(1, 1, PApplet.ARGB);
	  
	
  
}

