package com.catseye.patternComponents.polygonGenerators;

import processing.core.*;

public class Java2DNgonGenerator extends NGonGenerator {
	
  private PatternTriangle unit;
  private PatternTriangle unitPreview;
  private PMatrix2D[] transforms;

  public Java2DNgonGenerator(int i_segments, float i_radius, PImage i_gfx) 
  {
    super(i_segments, i_radius);
    float theta = PApplet.TWO_PI/i_segments;

    unit = createUnit(i_radius, i_gfx, theta, true);
    unitPreview = createUnit(UnitPreview.SIZE, i_gfx, theta, false);

    transforms = new PMatrix2D[i_segments*2];
    PMatrix2D flip = new PMatrix2D();
    flip.scale(-1, 0);

    for (int i = 0; i < i_segments*2; ++i) {
      PMatrix2D newMat = new PMatrix2D();

      if (i%2 == 1) {
        newMat.scale(-1, 1);
        newMat.rotate(-PApplet.PI-theta);
      }
      newMat.rotate(theta/2);
      newMat.rotate((i/2)*theta);
      transforms[i] = newMat;
    }
    
  }
  
  private PatternTriangle createUnit(float i_radius, PImage i_gfx, float i_theta, boolean i_isMainUnit){
   
    PVector temp1 = new PVector(i_radius, 0, 0);
    PVector temp2 = new PVector(PApplet.cos(i_theta)*i_radius, PApplet.sin(i_theta)*i_radius, 0);

    temp1.lerp(temp2, 0.5f);

    PVector f1 = new PVector(0, 0, 0);
    PVector f2 = new PVector(i_radius, 0, 0);
    PVector f3 = temp1.get();

    PatternTriangle out = new PatternTriangle(f1, f2, f3, i_gfx);  

    if(i_isMainUnit){
      unitWidth = PVector.dist(f1, f3);
      unitHeight = PVector.dist(f2, f3);  
    }
    
    return out;
    
  }

  public void drawAt(float i_x, float i_y, PGraphics renderContext) {

    for (int i = 0; i < sides*2; ++i) {
      renderContext.pushMatrix();
      renderContext.translate(i_x, i_y);

      //hack to fix bizarre single pixel tearing on the screen diagonal when creating a square grid
      if (sides == 4)
        renderContext.rotate(0.0001f);
      //-------------------------------------------------------------------------------

      renderContext.applyMatrix(transforms[i]);
      unit.draw(renderContext);
      renderContext.popMatrix();
    }
  }

  public void drawOutlinesAt(float i_x, float i_y, PGraphics renderContext) {

    for (int i = 0; i < sides*2; ++i) {
      renderContext.pushMatrix();
      renderContext.translate(i_x, i_y);
      renderContext.applyMatrix(transforms[i]);
      unit.drawOutline(renderContext);
      renderContext.popMatrix();
    }
  }

  public PImage getUnitImage() {
    return unitPreview.getImage();
  }
}





