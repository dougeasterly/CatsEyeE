package com.catseye.gui.guiPanes;

import java.io.File;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import com.catseye.CatsEye;
import com.catseye.gui.components.GridSelectionButton;
import com.catseye.gui.components.selectionPanel.GridSelectionPanel;
import com.catseye.gui.components.selectionPanel.StandardSelectionPanel;
import com.catseye.gui.components.selectionPanel.VoronoiDelaunaySelectionPanel;
import com.catseye.gui.p5Plugs.GridSelectionControls;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.catseye.patternComponents.gridGenerators.regularGrids.HexGrid;
import com.quickdrawProcessing.display.DisplayPane;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.display.Stage;
import com.quickdrawProcessing.gui.Cp5Plug;


public class GridSelectPane extends DisplayPane {


	 protected GridSelectionControls gridCtls; 
	 protected GridSelectionPanel[] gridSelections;

	 protected float cellSize;
	 protected boolean useMask;
	 protected float missingOdds;
	 
	 protected TileGrid currentGrid;
	 protected GridSelectionButton currentButton;
	
	public GridSelectPane(PVector i_position, PVector i_size) {
		super(i_position, i_size);
		onlyRedrawWhileMouseOver(true);
		gridSelections = new GridSelectionPanel[2];
		gridSelections[0] = new StandardSelectionPanel(new PVector(0,200), new PVector(size.x, size.y-100));
		gridSelections[1] = new VoronoiDelaunaySelectionPanel(new PVector(0,200), new PVector(size.x, size.y-100));
		drawBorder = true;
	}
	
	public void setGrid(TileGrid i_grid){
		currentGrid = i_grid;
		gridCtls.setSettingsFromGrid(i_grid);
	}
	
	@Override
	public void draw(PGraphics i_context){
		i_context.background(clearColor);
	}
	
	@Override
	public void addedToStage(){
		addChild(gridSelections[1]);
		gridCtls = new GridSelectionControls(this, Stage.cp5);
		currentGrid = gridSelections[1].getGrid();
		redrawNow();
	}
	

	public TileGrid getTileGrid(){
		return currentGrid;
	}
	
	@Override
	public void actionHook(InteractiveDisplayObject child, int i_action){
		
	
		
	}
	
	public void missingOdds(float i_odds){
		currentGrid.setMissingOdds(i_odds);
	}
	
	public void cellSize(int i_value){
		currentGrid.setCellRadius(i_value);
	}
	
	public void useMask(boolean i_useMask){
		currentGrid.useMask(i_useMask);
	}
	
	public void loadMask(int GUI_JUNK) {
	 	File file = new File("");
	    Stage.p5.selectInput("Select an image", "maskSelected", file, this);
	 }
	   
	   
	   /*
	   *   callback function for mask selection filepicker
	   */
	    
    public void maskSelected(File selection) {
	 PImage maskImage;
	   
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
