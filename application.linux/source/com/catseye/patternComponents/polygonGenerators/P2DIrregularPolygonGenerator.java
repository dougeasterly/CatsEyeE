package com.catseye.patternComponents.polygonGenerators;


import processing.core.*;
import java.util.ArrayList;
import com.catseye.CatsEye;

/*---------------------------------------------------------------------------------------------
 *
 *    P2DIrregularPolygonGenerator
 *    
 *    subclass of NGonGenerator that uses P2D to create irregular polygons.
 *    this class uses a textured PShape using texture coordinates.
 *
 *    Ben Jack 11/4/2014 
 *
 *---------------------------------------------------------------------------------------------*/


public class P2DIrregularPolygonGenerator extends NGonGenerator {

  private PVector[] texCoords;
  private PImage texture;
  
  private PShape polygon;
  private PShape outlines;
  private PImage tesselationUnit;

  public P2DIrregularPolygonGenerator(){
   super(-1, -1); 
  }

  public P2DIrregularPolygonGenerator(PImage i_gfx) {
    super(-1, -1);

    PVector[] defaultTexCoords = {
      new PVector(0, 1), new PVector(1, 1), new PVector(1, 0)
      };

      texture = i_gfx;
      texCoords = defaultTexCoords;
      
      tesselationUnit = createTesselationUnit(i_gfx, defaultTexCoords);
  }

  public P2DIrregularPolygonGenerator(PImage i_gfx, PVector[] i_texCoords) {
    super(-1, -1);
     
    texture = i_gfx;
    texCoords = i_texCoords;
    
    tesselationUnit = createTesselationUnit(i_gfx, i_texCoords);
  }

  public void constructIrregularPolygon(ArrayList<PVector> i_corners, PVector i_centroid, boolean i_outlines) {

    
    if(!i_outlines){
      
      polygon = CatsEye.p5.createShape();
      polygon.beginShape(PApplet.TRIANGLE_FAN);
      polygon.noStroke();
      polygon.textureMode(PApplet.NORMAL);
      polygon.texture(texture);
      polygon.vertex(0, 0, texCoords[0].x, texCoords[0].y);
      constructPolygon(polygon, i_corners, i_centroid);
      polygon.endShape();
      
    }else{
      outlines = CatsEye.p5.createShape(PApplet.GROUP);
      
      PShape outer = CatsEye.p5.createShape();
      outer.beginShape();
      outer.stroke(0,0,255);
      outer.strokeWeight(1);
      outer.noFill();
      constructPolygon(outer, i_corners, i_centroid);
      outer.endShape();

      PShape inner = CatsEye.p5.createShape();
      inner.beginShape(PApplet.TRIANGLE_FAN);
      inner.stroke(255, 0, 0);
      inner.strokeWeight(1);
      inner.noFill();
      inner.vertex(0, 0, texCoords[0].x, texCoords[0].y);
      constructPolygon(inner, i_corners, i_centroid);
      inner.endShape();
      
      outlines.addChild(inner);
      outlines.addChild(outer);

      
    }

  }
  
  private void constructPolygon(PShape i_shape, ArrayList<PVector> i_corners,  PVector i_centroid){
    
    for (int i = 0; i <= i_corners.size(); ++i) {
      
      PVector pt = PVector.sub(i_corners.get(i%i_corners.size()), i_centroid);
      PVector npt = PVector.sub(i_corners.get((i+1)%i_corners.size()), i_centroid);
      PVector mid = PVector.lerp(pt, npt, 0.5f);

      i_shape.vertex(pt.x, pt.y, texCoords[1].x, texCoords[1].y);
      i_shape.vertex(mid.x, mid.y, texCoords[2].x, texCoords[2].y);
      
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

    float theta = PApplet.TWO_PI/6;
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

