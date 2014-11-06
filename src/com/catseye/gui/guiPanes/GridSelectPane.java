package com.catseye.gui.guiPanes;

import java.io.File;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

import com.catseye.CatsEye;
import com.catseye.HandlerActions;
import com.catseye.gui.components.GridSelectionButton;
import com.catseye.gui.components.selectionPanel.GridSelectionPanel;
import com.catseye.gui.components.selectionPanel.StandardSelectionPanel;
import com.catseye.gui.components.selectionPanel.VoronoiDelaunaySelectionPanel;
import com.catseye.gui.p5Plugs.GridSelectionControls;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.catseye.patternComponents.gridGenerators.irregularGrids.VoronoiDelaunayGrid;
import com.catseye.patternComponents.gridGenerators.regularGrids.HexGrid;
import com.quickdrawProcessing.display.DisplayPane;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.display.Stage;
import com.quickdrawProcessing.gui.Cp5Plug;


public class GridSelectPane extends DisplayPane {


	 protected GridSelectionPanel[] panels; 
	
	 protected GridSelectionControls gridCtls; 
	 protected GridSelectionPanel currentPanel;
	 protected static int currentTab;
	 
	 
	 
	 protected float cellSize;
	 protected boolean useMask;
	 protected float missingOdds;

	
	public GridSelectPane(PVector i_position, PVector i_size) {
		super(i_position, i_size);
		onlyRedrawWhileMouseOver(true);
	
		panels = new GridSelectionPanel[2];
		panels[0] = new StandardSelectionPanel(new PVector(0,200), new PVector(size.x, size.y-100));
		float voronoiRegion = size.y-200;
		panels[1] = new VoronoiDelaunaySelectionPanel(new PVector((size.x-voronoiRegion)/2,200), new PVector(voronoiRegion, voronoiRegion));
		
		drawBorder = true;
	}
	
	public void setGrid(TileGrid i_grid){
		currentPanel.setGrid(i_grid);
		gridCtls.setSettingsFromGrid(i_grid);
	}
	
	@Override
	public void draw(PGraphics i_context){
		i_context.background(clearColor);
	}
	
	@Override
	public void addedToStage(){
		gridCtls = new GridSelectionControls(this, Stage.cp5);
		currentPanel = panels[0];
		updatePanel();
	}
	
	private void updatePanel(){
		addChild(currentPanel);
		redrawNow();
	}

	public TileGrid getTileGrid(){
		return currentPanel.getGrid();
	}
	
	@Override
	public void actionHook(InteractiveDisplayObject child, int i_action){
	
		
		/*if(i_action == HandlerActions.UPDATE_GRID){
			currentGrid = ((GridSelectionPanel)child).getGrid();
		}*/
		
		
	}
	
	public void voronoiDelaunaySwitch(int i_vd){
		
		((VoronoiDelaunaySelectionPanel)currentPanel).setGridType(i_vd);
		
	}
	
	public void changeTab(int i_tabID){
		

		removeChild(currentPanel);
		
		switch(i_tabID){
			
			case GridSelectionControls.REGULAR:
				currentPanel = panels[0];
				currentTab = GridSelectionControls.REGULAR;
				break;
			case GridSelectionControls.VORONOI_DELAUNAY:
				currentPanel = panels[1];
				currentTab = GridSelectionControls.VORONOI_DELAUNAY;
				break;
			default:
				break;
		}
		
		gridCtls.setControls(i_tabID);
		updatePanel();
			 

	}
	
	public void setVDPoints(JSONObject i_json){
		JSONArray i_pts = i_json.getJSONArray("VDPoints");
		
		VoronoiDelaunayGrid currentGrid = (VoronoiDelaunayGrid)currentPanel.getGrid();
		
		for(int i = 0; i < i_pts.size(); ++i){
			JSONObject json = i_pts.getJSONObject(i);
			
			float ptX = (json.getFloat("x")/currentGrid.getRenderSize().x) * currentPanel.getSize().x;
			float ptY = (json.getFloat("y")/currentGrid.getRenderSize().y) * currentPanel.getSize().y;
			
			((VoronoiDelaunaySelectionPanel)currentPanel).addPoint(new PVector(ptX, ptY), false);
			((VoronoiDelaunayGrid)currentGrid).addPoint(new PVector(json.getFloat("x"), json.getFloat("y")));
		}
		
		((VoronoiDelaunaySelectionPanel)currentPanel).calculateVoronoi();
		
	}
	
	public void addRandomVDPoints(){
		((VoronoiDelaunaySelectionPanel)currentPanel).addRandomPoints();
	}
	
	public void clearVD(){
		((VoronoiDelaunaySelectionPanel)currentPanel).clearPoints();
	}
	
	public void missingOdds(float i_odds){
		TileGrid currentGrid = currentPanel.getGrid();
		currentGrid.setMissingOdds(i_odds);
	}
	
	public void cellSize(int i_value){
		TileGrid currentGrid = currentPanel.getGrid();
		currentGrid.setCellRadius(i_value);
	}
	
	public void useMask(boolean i_useMask){
		TileGrid currentGrid = currentPanel.getGrid();
		currentGrid.useMask(i_useMask);
	}
	
	public void loadMask(int GUI_JUNK) {
	 	File file = new File("");
	    Stage.p5.selectInput("Select an image", "maskSelected", file, this);
	 }
	   
	 public static int getCurrentTab(){
		 return currentTab;
	 }
	   
	   /*
	   *   callback function for mask selection filepicker
	   */
	    
    public void maskSelected(File selection) {
	 PImage maskImage;
	 
	 TileGrid currentGrid = currentPanel.getGrid();
	   
	 if (selection == null) {
       System.out.println("Window was closed or the user hit cancel.");
	 } 
     else {
       maskImage = Stage.p5.loadImage(selection.getAbsolutePath());
       gridCtls.setMaskImage(maskImage);
       currentGrid.setMask(maskImage);
     }
     
   }  
	

	  
	

}
