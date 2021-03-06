package com.catseye.patternComponents.gridGenerators.irregularGrids;


/*---------------------------------------------------------------------------------------------
 *
 *    VoronoiDelauneyGrid
 *    
 *    Implementation of voronoi and delauney triangulation tiling grids
 *
 *    Ben Jack 6/4/2014 
 *
 *---------------------------------------------------------------------------------------------*/

import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;
import toxi.geom.*;
import toxi.geom.mesh2d.*;
import toxi.util.datatypes.*;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.catseye.patternComponents.polygonGenerators.P2DIrregularPolygonGenerator;
import com.quickdrawProcessing.display.Stage;


public class VoronoiDelaunayGrid extends TileGrid {

  public static final int VORONOI = 0;
  public static final int DELAUNAY = 1; 
  
  protected int type;
  
  Voronoi voronoi;
  PolygonClipper2D clip;

  private FloatRange xpos, ypos;


  public VoronoiDelaunayGrid() {
    super();
    init();
  }

  public VoronoiDelaunayGrid(TileGrid i_oldGrid) {
    super(i_oldGrid);
    init();
  }

  protected void init() {
    voronoi = new Voronoi();
    type = VORONOI;
    renderMode = PApplet.P2D;
    cellRadius = -1;
    cellSize = new PVector(-1, -1);
    
    xpos= new BiasedFloatRange(-renderSize.x*0.5f, renderSize.x*1.5f, renderSize.x/2, 0.222f);
    ypos= new BiasedFloatRange(-renderSize.x*0.5f, renderSize.y*1.5f, renderSize.y/2, 0.222f);
  }
  
  public PImage getMiniGridImage(PVector i_size) {
	  init();
	  addRandomPoints(40);
	  return getGridImage(); 
  }
  
  public void setRenderMode(String i_mode) {
      System.out.println("only P2D mode for voronoi and delaunay grids");
  }

  public void setType(int i_type){
    type = i_type;
  } 

  public void addPoint(PVector i_pt) {
    voronoi.addPoint(new Vec2D(i_pt.x, i_pt.y));
  }

  public void addPoints(ArrayList<PVector> i_points){
	  for(PVector vec : i_points){
		  addPoint(vec);
	  }
  }

  public void addRandomPoints(int i_count) {
    for (int i = 0; i < i_count; ++i) {
      addPoint(new PVector(xpos.pickRandom(), ypos.pickRandom()));
    }
  }
  
  public void clearPoints(){
    voronoi = new Voronoi();
  }
  
  public void addNormalizedPoints(PVector renderSize, ArrayList<PVector> i_normalized){
    
    for(PVector pt : i_normalized){
     addPoint(new PVector(renderSize.x * pt.x, renderSize.y * pt.y)); 
    }
    
  }
  
  public void generate(boolean i_outlines) {
 
    P2DIrregularPolygonGenerator generator = setupIrregularNgonGenerator();
    PGraphics currentContext = initGeneration(i_outlines);

    clip=new SutherlandHodgemanClipper(new Rect(0, 0, renderSize.x, renderSize.y));

    if(type==VORONOI){
      for (Polygon2D poly : voronoi.getRegions()) {
        
        Polygon2D clipped = clip.clipPolygon(poly);
        Vec2D centroid = clipped.getCentroid();

        if(clipped.vertices.size() > 0){
          generator.constructIrregularPolygon(reformatList(clipped.vertices), new PVector(centroid.x, centroid.y), i_outlines);
          drawNgonAt(centroid.x, centroid.y, currentContext);
        }
      }
    }else if(type==DELAUNAY){
      for (Triangle2D tri : voronoi.getTriangles()) {
        
        Polygon2D clipped = clip.clipPolygon(tri.toPolygon2D());
        Vec2D centroid = clipped.getCentroid();

        if(clipped.vertices.size() > 0){
          generator.constructIrregularPolygon(reformatList(clipped.vertices), new PVector(centroid.x, centroid.y), i_outlines);
          drawNgonAt(centroid.x, centroid.y, currentContext);
        }
      }  
    }

    completeGeneration(i_outlines);
  }



  private ArrayList<PVector> reformatList(List<Vec2D> i_list) {

    ArrayList<PVector> output = new ArrayList<PVector>();

    for (Vec2D i : i_list) {
      output.add(new PVector(i.x, i.y));
    } 

    return output;
    
  }

  @Override
  public JSONObject saveAsJSON(){
	  
	  JSONObject json = super.saveAsJSON(false);
	  
	  String path = json.getString("savePath");
	  
	  
	  JSONArray pointsArr = new JSONArray();
	   
	   for(Vec2D i : voronoi.getSites()){
		   JSONObject vec = new JSONObject();
		   vec.setFloat("x", i.x);
		   vec.setFloat("y", i.y);
		   
		   pointsArr.append(vec);
	   }
	   
	   json.setJSONArray("VDPoints", pointsArr);
	  
	   
	  File savePathFile = new File(path);
	  
	  
	  if(!savePathFile.exists())
		   savePathFile.mkdirs();
	  
	  Stage.p5.saveJSONObject(json, savePathFile.getAbsolutePath()+"/saveData.json");
	   
	   
	  
	  return json;
  
  }
  
  
}

