package com.catseye.patternComponents.polygonGenerators;

import processing.core.*;
import com.catseye.CatsEye;

/*---------------------------------------------------------------------------------------------
 *
 *    P2DNgonGenerator
 *    
 *    subclass of NGonGenerator that uses JAVA2D to render images.
 *    this class uses a textured PShape using texture coordinates.
 *
 *    The benefit of using P2D is that it enables a lot of features by using openGl.
 *    The drawback is that the size of images that can be produced are limited by the 
 *    graphics card of the user.
 *
 *    Ben Jack 11/4/2014 
 *
 *---------------------------------------------------------------------------------------------*/



public class P2DNgonGenerator extends NGonGenerator {
	
  private PShape polygon;
  private PShape outlines;
  private PImage tesselationUnit;

  public P2DNgonGenerator(int i_segments, float i_radius, PImage i_gfx) {
    super(i_segments, i_radius);

    PVector[] defaultTexCoords = {
      new PVector(0, 1), new PVector(1, 1), new PVector(1, 0)
      };

      init(i_gfx, defaultTexCoords);
  }

  public P2DNgonGenerator(int i_segments, float i_radius, PImage i_gfx, PVector[] i_texCoords) {
    super(i_segments, i_radius);

    init(i_gfx, i_texCoords);
  }

  private void init(PImage i_gfx, PVector[] i_texCoords) 
  {
    polygon = CatsEye.p5.createShape();
    polygon.beginShape(PApplet.TRIANGLE_FAN);
    polygon.noStroke();
    polygon.textureMode(PApplet.NORMAL);
    polygon.texture(i_gfx);
    polygon.vertex(0, 0, i_texCoords[0].x, i_texCoords[0].y);
    createPolygon(polygon, i_texCoords);
    polygon.endShape();


    PShape outer = CatsEye.p5.createShape();
    outer.beginShape();
    outer.stroke(0, 0, 255);
    outer.strokeWeight(1);
    outer.noFill();
    createPolygon(outer, i_texCoords);
    outer.endShape();
    
    PShape inner = CatsEye.p5.createShape();
    inner.beginShape(PApplet.TRIANGLE_FAN);
    inner.stroke(255, 0, 0);
    inner.strokeWeight(1);
    inner.noFill();
    inner.vertex(0, 0, i_texCoords[0].x, i_texCoords[0].y);
    createPolygon(inner, i_texCoords);
    inner.endShape();
    
    outlines = CatsEye.p5.createShape(PApplet.GROUP);
    outlines.addChild(inner);
    outlines.addChild(outer);
    

    tesselationUnit = createTesselationUnit(i_gfx, i_texCoords);
  }

  private void createPolygon(PShape i_polygon, PVector[] i_texCoords) {
    
    float theta = PApplet.TWO_PI/sides;

    for (int i = 0; i <= sides; ++i) {
      float t = (theta*i)+(theta/2);
      float nt = (theta*(i+1))+(theta/2);

      PVector pt = new PVector(PApplet.cos(t)*radius, PApplet.sin(t)*radius);
      PVector npt = new PVector(PApplet.cos(nt)*radius, PApplet.sin(nt)*radius);
      PVector mid = PVector.lerp(pt, npt, 0.5f);

      unitWidth  = PVector.dist(new PVector(0, 0), mid);
      unitHeight = PVector.dist(pt, mid); 

      i_polygon.vertex(pt.x, pt.y, i_texCoords[1].x, i_texCoords[1].y);
      i_polygon.vertex(mid.x, mid.y, i_texCoords[2].x, i_texCoords[2].y);
    }
  }

  private PImage createTesselationUnit(PImage i_gfx, PVector[] i_texCoords) {

    PGraphics out = CatsEye.p5.createGraphics((int)UnitPreview.SIZE+10, (int)UnitPreview.SIZE+10, PApplet.P2D);
    out.beginDraw();
    out.background(0, 0);

    PShape tri = CatsEye.p5.createShape();

    tri.beginShape(PApplet.TRIANGLES);
    tri.noStroke();
    tri.textureMode(PApplet.NORMAL);
    tri.texture(i_gfx);

    float theta = PApplet.TWO_PI/sides;
    float t = 0;
    float nt = theta;

    PVector pt = new PVector(PApplet.cos(t)* UnitPreview.SIZE, PApplet.sin(t)*UnitPreview.SIZE);
    PVector npt = new PVector(PApplet.cos(nt)* UnitPreview.SIZE, PApplet.sin(nt)*UnitPreview.SIZE);
    PVector mid = PVector.lerp(pt, npt, 0.5f);

    tri.vertex(0, 0, i_texCoords[0].x, i_texCoords[0].y);
    tri.vertex(pt.x, pt.y, i_texCoords[1].x, i_texCoords[1].y);
    tri.vertex(mid.x, mid.y, i_texCoords[2].x, i_texCoords[2].y);

    tri.endShape();

    out.shape(tri);
    out.endDraw();

    return out.get();
  }

  public void drawAt(float i_x, float i_y, PGraphics renderContext) {

    renderContext.pushMatrix();
    renderContext.translate(i_x, i_y);
    renderContext.shape(polygon);
    renderContext.popMatrix();
  }

  public void drawOutlinesAt(float i_x, float i_y, PGraphics renderContext) {

    renderContext.pushMatrix();
    renderContext.translate(i_x, i_y);
    renderContext.shape(outlines);
    renderContext.popMatrix();
  }

  public PImage getUnitImage() {
    return tesselationUnit;
  }
  
}


