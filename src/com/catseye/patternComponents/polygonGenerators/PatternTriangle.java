package com.catseye.patternComponents.polygonGenerators;

import processing.core.*;
import com.catseye.CatsEye;

/*---------------------------------------------------------------------------------------------
*
*    PatternTriangle
*    
*    a masked texture triangle used in the Java2DNgonGenerator
*
*    Ben Jack 12/4/2014 
*
*---------------------------------------------------------------------------------------------*/

public class PatternTriangle {

  private PVector pt1, pt2, pt3;

  private PGraphics mask;
  private PImage canvas;

  public PatternTriangle(PVector i_pt1, PVector i_pt2, PVector i_pt3, PImage texture) {

    pt1 = i_pt1;
    pt2 = i_pt2;
    pt3 = i_pt3;

    float maxX = PApplet.max(i_pt1.x, i_pt2.x, i_pt3.x);
    float minX = PApplet.min(i_pt1.x, i_pt2.x, i_pt3.x);
    float maxY = PApplet.max(i_pt1.y, i_pt2.y, i_pt3.y);
    float minY = PApplet.min(i_pt1.y, i_pt2.y, i_pt3.y);

    int canvasWidth  = (int)(maxX-minX);
    int canvasHeight = (int)(maxY-minY);

    mask = CatsEye.p5.createGraphics(canvasWidth+2, canvasHeight+2);

    canvas = texture.get();
    canvas.resize(canvasWidth+2, canvasHeight+2);

    mask.beginDraw();
    mask.noSmooth();
    mask.background(0);
    mask.fill(255);
    mask.stroke(255);
    mask.strokeWeight(2);
    mask.triangle(pt1.x, pt1.y, pt2.x, pt2.y, pt3.x, pt3.y);
    mask.endDraw();
    
    canvas.mask(mask);
  }



  void draw(PGraphics renderContext){
    renderContext.image(canvas, 0, 0);
  }



  void drawOutline(PGraphics renderContext) {
    renderContext.stroke(255, 0, 0);
    renderContext.strokeWeight(1);
    renderContext.line(pt1.x, pt1.y, pt2.x, pt2.y);
    renderContext.line(pt2.x, pt2.y, pt3.x, pt3.y);
    renderContext.line(pt3.x, pt3.y, pt1.x, pt1.y);
    
    renderContext.stroke(0, 0, 255);
    renderContext.strokeWeight(1);
    renderContext.line(pt2.x, pt2.y, pt3.x, pt3.y);
  }
  
  

  PImage getImage() {
    return canvas.get();
  }
}

