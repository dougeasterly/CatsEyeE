package com.catseye.patternComponents.polygonGenerators;

import processing.core.*;

import com.catseye.CatsEye;
/*---------------------------------------------------------------------------------------------
*
*    NGonGenerator
*    
*    Base class for various NGon generators (JAVA2D, P2D, IRREGULAR etc..)
*    Do not instantiate this class directly, rather use one of the subclasses that have the DrawAt() 
*    And drawOutlinesAt() functions implemented.
*
*    Ben Jack 12/4/2014 
*
*---------------------------------------------------------------------------------------------*/

public class NGonGenerator {

  protected int sides;
  protected float radius, unitWidth, unitHeight;


  //----------------------CONSTRUCTORS------------------------

  public NGonGenerator(int i_segments, float i_radius) {
    sides = i_segments;
    radius = i_radius;
  }


  //----------------------------------SETTERS/GETTERS-----------------------------------------


  /*
      unitWidth and unitHeight must be set in subclass 
  */
  
  public float cellWidth() {
    return unitWidth;
  }

  public float cellHeight() {
    return unitHeight;
  }  

  public float cellRadius() {
    return radius;
  }

  public PImage getUnitImage() {
    /*Override this function in a subclass, 
    this only returns a stub blank image*/
    return null;
  }
  
  
  
  
  //---------------------------------METHODS--------------------------------------

  public void drawAt(float i_x, float i_y, PGraphics renderContext) {
    /*Implement this function in a subclass*/
  }

  public void drawOutlinesAt(float i_x, float i_y, PGraphics renderContext) {
    /*Implement this function in a subclass*/
  }
  
}

