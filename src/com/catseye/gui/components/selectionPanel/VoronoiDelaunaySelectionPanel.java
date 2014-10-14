package com.catseye.gui.components.selectionPanel;

import java.util.ArrayList;
import java.util.List;

import com.catseye.gui.guiPanes.ImageSelectionPane;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.catseye.patternComponents.gridGenerators.irregularGrids.VoronoiDelaunayGrid;
import com.quickdrawProcessing.display.Stage;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import toxi.geom.Polygon2D;
import toxi.geom.PolygonClipper2D;
import toxi.geom.Rect;
import toxi.geom.SutherlandHodgemanClipper;
import toxi.geom.Triangle2D;
import toxi.geom.Vec2D;
import toxi.geom.mesh2d.Voronoi;
import controlP5.ControlP5;

public class VoronoiDelaunaySelectionPanel extends GridSelectionPanel {
	
	  private int gridType;

	  ArrayList<PVector> points;
	  Voronoi voronoi;
	  PolygonClipper2D clip;
	
	public VoronoiDelaunaySelectionPanel(PVector i_position, PVector i_size) {
		super(i_position, i_size);
		
		points = new ArrayList<PVector>();
		gridType = VoronoiDelaunayGrid.VORONOI;
		voronoi = new Voronoi();
		clip=new SutherlandHodgemanClipper(new Rect(0, 0, i_size.x, i_size.y));
	}
	
	public void addedToStage(){
		clearPoints();
	}
	
	
	  public void clearPoints() {
		  points.clear();
		  calculateVoronoi();
	  }
		  
	  public void addPoint(PVector i_point){
		  points.add(i_point);
	  }
	  
	  public void addRandomPoints() {
		  addRandomPoints(20);
	  }
	  
	  public void addRandomPoints(int i_randomCount) {
		  
	    for (int i = 0; i < i_randomCount; ++i) {
	      points.add(new PVector(Stage.p5.random(size.x), Stage.p5.random(size.y)));
	    }
	    
	    calculateVoronoi();
	  }
	  
	  public ArrayList<PVector> getNormalizedPoints(){
	   
	   ArrayList<PVector> normalized = new ArrayList<PVector>();
	   
	   for(PVector pt : points){
	     normalized.add(new PVector(pt.x/(size.x+0.0f), pt.y/(size.y+0.0f))); 
	   } 
	    
	   return normalized;
	   
	  }
	  
	  public void setGridType(int i_type){
	    gridType = i_type;
	  }
	  
	  
	 private void calculateVoronoi(){
	        
        voronoi = new Voronoi();
        
        for(PVector i : points){
          voronoi.addPoint(new Vec2D(i.x, i.y));
        }
        
	 }
	 
	 @Override
	 public TileGrid getGrid(){
		 VoronoiDelaunayGrid grid = new VoronoiDelaunayGrid();
		 grid.setType(gridType);
		 PVector renderSize = new PVector(ImageSelectionPane.getRenderSize().x, ImageSelectionPane.getRenderSize().y);
		 grid.setRenderSize(renderSize);
		 grid.addNormalizedPoints(renderSize, getNormalizedPoints());
		 return grid;
	 }
	 
	 public void draw(PGraphics i_context) {

		  i_context.background(255);
		    
		  i_context.noFill();
		  i_context.stroke(0);
		     
		     List<Polygon2D> polys;
		     
		     if(gridType == VoronoiDelaunayGrid.DELAUNAY){
		       polys = new ArrayList<Polygon2D>();
		       for(Triangle2D t : voronoi.getTriangles()){
		        polys.add(t.toPolygon2D()); 
		       }
		     }
		     else
		        polys = voronoi.getRegions();
		   
		     
		     for (Polygon2D poly : polys) {
		        
		        Polygon2D clipped = clip.clipPolygon(poly);

		        i_context.beginShape();
		        for(Vec2D pt : clipped.vertices){
		        	i_context.vertex(pt.x, pt.y);
		        }
		        
		        Vec2D pt = clipped.vertices.get(0);
		        i_context.vertex(pt.x, pt.y);
		        i_context.endShape();
		        
		                
		     }

		    for (PVector pos : points) {
		    	i_context.fill(255, 0, 0);
		    	i_context.stroke(255, 0, 0);
		    	i_context.ellipse(pos.x, pos.y, 2, 2);
		    }
		    
		  } 
	  
	  
	  @Override
	  public void click(PVector i_position){
		  PVector localPos = globalToLocal(i_position);
		  addPoint(localPos);
		  calculateVoronoi();
		  System.out.println("voronoi click");
	  }
	  
	    



}
