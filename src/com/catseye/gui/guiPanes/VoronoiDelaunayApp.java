package com.catseye.gui.guiPanes;

import java.util.List;
import java.util.ArrayList;

import com.catseye.CatsEye;
import com.catseye.gui.GUI;
import com.catseye.patternComponents.gridGenerators.irregularGrids.VoronoiDelaunayGrid;
import com.catseye.patternComponents.gridGenerators.regularGrids.GridType;

import processing.core.*;
import controlP5.*;
import toxi.geom.*;
import toxi.geom.mesh2d.Voronoi;


public class VoronoiDelaunayApp extends GUIApp {

  private static final long serialVersionUID = 3904042928204019888L;
	
	
	
	
  ControlP5 cp5;
  GUI parent;

  private int randomCount;
 // private VoronoiDelaunayGrid vdGrid;

  private GridType gridType;

  ArrayList<PVector> points;
  Voronoi voronoi;
  PolygonClipper2D clip;

  
  @SuppressWarnings("unused")
  private VoronoiDelaunayApp() {
  }


  public VoronoiDelaunayApp(GUI i_parent, int i_width, int i_height) {
    parent = i_parent;
    appWidth = i_width;
    appHeight = i_height;

    voronoi = new Voronoi();
    clip=new SutherlandHodgemanClipper(new Rect(0, 0, i_width, i_height));
  }


  public void setup() {
    size(appWidth, appHeight); 

    createGuiControls();  
    points = new  ArrayList<PVector>();
  }


  public void clearPoints() {
    
    points.clear();
    calculateVoronoi();
  }
  
  

  public void addRandomPoints() {
    for (int i = 0; i < randomCount; ++i) {
      points.add(new PVector(random(this.width), random(this.height)));
    }
    
    calculateVoronoi();
  }
  
  
  
  public ArrayList<PVector> getNormalizedPoints(){
   
   ArrayList<PVector> normalized = new ArrayList<PVector>();
   
   for(PVector pt : points){
     normalized.add(new PVector(pt.x/(appWidth+0.0f), pt.y/(appHeight+0.0f))); 
   } 
    
   return normalized;
   
  }
  
  public void setGridType(GridType i_type){
    gridType = i_type;
  }
  
  
  
  //--------------------------------------PRIVATE METHODS----------------------------------------
  //although some of these are public due to controlP5 needs or otherwise, they shouldn't be used     
    
    
  public void mousePressed() {
    points.add(new PVector(mouseX, mouseY));
    calculateVoronoi();
  }


  public void draw() {

    background(255);

    
     noFill();
     stroke(0);
     
     List<Polygon2D> polys;
     
     if(gridType == GridType.DELAUNAY){
       polys = new ArrayList<Polygon2D>();
       for(Triangle2D t : voronoi.getTriangles()){
        polys.add(t.toPolygon2D()); 
       }
     }
     else
        polys = voronoi.getRegions();
   
     
     for (Polygon2D poly : polys) {
        
        Polygon2D clipped = clip.clipPolygon(poly);

        beginShape();
        for(Vec2D pt : clipped.vertices){
          vertex(pt.x, pt.y);
        }
        
        Vec2D pt = clipped.vertices.get(0);
        vertex(pt.x, pt.y);
        endShape();
        
                
     }

    for (PVector pos : points) {
      fill(255, 0, 0);
      stroke(255, 0, 0);
      ellipse(pos.x, pos.y, 2, 2);
    }
    
  }
  
    
    private void calculateVoronoi(){
      
      voronoi = new Voronoi();
      
      for(PVector i : points){
        voronoi.addPoint(new Vec2D(i.x, i.y));
      }
      
    }
  


  //--------------------------------------------ACTUAL GUI CREATION--------------------------------------------------------

  private void createGuiControls() {
    this.cp5 = new ControlP5(this);

    this.cp5.addButton("clearPoints")
      .setPosition(20, 20);

    this.cp5.addButton("add random")
      .setPosition(100, 20)
        .plugTo(this, "addRandomPoints");
        
    cp5.addNumberbox("randomCount")
    .setPosition(180, 20)
      .setSize(45, 14)
        .setScrollSensitivity(1.1f)
          .setValue(10)
            .setRange(10,1000);
        
     }
}

