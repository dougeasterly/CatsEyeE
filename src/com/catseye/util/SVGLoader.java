package com.catseye.util;

import processing.core.*;
import com.catseye.CatsEye;
/*---------------------------------------------------------------------------------------------
*
*    SVGTile
*    
*    for loading and colouring SVG images. needs re-implementing, probably doesn't work currently
*
*    Ben Jack 12/4/2014 
*
*---------------------------------------------------------------------------------------------*/


// Tile colours have been removed for now.

public class SVGLoader {

	 public SVGLoader( PShape tSVG, float i_scale)
	  {
	   
	  }

	  float getTileWidth() {
	    return 0;
	  }
	  float getTileHeight() {
	    return 0;
	  }
	  int getTileChildrenCount() {
	    return 0;
	  }
	  float getMaskWidth() {
	    return 0;
	  }
	  float getMaskHeight() {
	    return 0;
	  }


	  public PImage drawTile () {
	    return CatsEye.p5.createImage(1, 1, PApplet.ARGB);
	  }
	
  
}

