package com.catseye.gui.p5Plugs;

import processing.core.PImage;

import com.catseye.HandlerActions;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.catseye.patternComponents.gridGenerators.irregularGrids.VoronoiDelaunayGrid;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.gui.Cp5Plug;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Numberbox;
import controlP5.RadioButton;
import controlP5.Slider;
import controlP5.Tab;
import controlP5.Toggle;

public class GridSelectionControls extends Cp5Plug{

	public static final int REGULAR = 0;
	public static final int VORONOI_DELAUNAY = 1;
	
	
	//Global controls
	private RadioButton tabs;
	
	private Button maskImageButton;
	private Slider oddsControl;
	private Toggle useMaskControl;
	
	//Regular gird controls
	private Numberbox cellSizeControl;
	
	//voronoi delaunay controls
	private RadioButton vDselection;
	private Button addRandomVDPoints, clearVD;
	
	public GridSelectionControls(InteractiveDisplayObject i_parent,
			ControlP5 i_cp5) {
		super(i_parent, i_cp5);
	}


	@Override
	protected void setupControls(InteractiveDisplayObject i_object) {
		  
		  tabs = cp5.addRadioButton("grid styles")
			.setPosition(0,2)
			.setItemsPerRow(2)
			.setLabelPadding(-(int)i_object.getSize().x/4-25, 0)
			.setSize((int)i_object.getSize().x/2, 14)
			.setSpacingColumn(0)
			.addItem("regular grids", REGULAR)
			.addItem("voronoi/delaunay", VORONOI_DELAUNAY);
		  
		 
		 i_object.addCP5Control(tabs, "changeTab");
		 tabs.activate(REGULAR);
		
		 setupGlobalControls(i_object);
		 setupRegularControls(i_object);
		 setupVoronoiDelaunayControls(i_object);
		 
		 setControls(REGULAR);
	}
	
	private void setupGlobalControls(InteractiveDisplayObject i_object){
	 
	  int buttonSize = (int)(i_object.getSize().x/2-80)/3;
		
	  useMaskControl = cp5.addToggle("useMask")
	  	.setPosition(20, 90)
	  	.setSize(20, 20);
	  
	  i_object.addCP5Control(useMaskControl, "useMask");

	  maskImageButton = cp5.addButton("loadMask")
		.setPosition(buttonSize+40, 100-buttonSize/2)
		.setSize(buttonSize, buttonSize);
	  
	  i_object.addCP5Control(maskImageButton, "loadMask");
	  
	  oddsControl = cp5.addSlider("missing odds")
	     .setPosition(buttonSize*2+60, 90)
	     .setSize(buttonSize, 20)
	     .setRange(0, 1);
	  
	  i_object.addCP5Control(oddsControl, "missingOdds");
	}
	
	private void setupRegularControls(InteractiveDisplayObject i_object){
		
		cellSizeControl = cp5.addNumberbox("cellsize")
	  	.setPosition(i_object.getSize().x/2 + i_object.getSize().x/4 -40, 90)
	  	.setSize(80, 20)
	  	.setLabel("Cell Size")
	  	.setMin(10)
	  	.setMax(1000)
	  	.setValue(150);
	  	
	  	i_object.addCP5Control(cellSizeControl, "cellSize");
		
	}
	
	private void setupVoronoiDelaunayControls(InteractiveDisplayObject i_object){
		
		float buttonSize = (i_object.getSize().x/2.0f-60)/2;
		
		vDselection= cp5.addRadioButton("VoronoiDelaunaySelection")
         .setPosition(i_object.getSize().x/2 + i_object.getSize().x/4 - 50, 70)
         .setSize(20,20)
         .setItemsPerRow(2)
         .setSpacingColumn(60)
         .addItem("Voronoi",VoronoiDelaunayGrid.VORONOI)
         .addItem("Delaunay",VoronoiDelaunayGrid.DELAUNAY)
         .activate(0);
		
		i_object.addCP5Control(vDselection, "voronoiDelaunaySwitch");
		
		
		
		
		addRandomVDPoints = cp5.addButton("random")
			.setPosition(i_object.getSize().x/2 + 20, 110)
			.setSize((int)buttonSize,20);
		
		i_object.addCP5Control(addRandomVDPoints, "addRandomVDPoints");
		
		clearVD = cp5.addButton("clear")
			.setPosition(i_object.getSize().x/2 + buttonSize+40, 110)
			.setSize((int)buttonSize,20);
			
		i_object.addCP5Control(clearVD, "clearVD");
		
	}
	
	public void setControls(int i_type){
		
		tabs.activate(i_type);
		
		switch(i_type){
			case REGULAR:
				
				vDselection.hide();
				addRandomVDPoints.hide();
				clearVD.hide();
				
				cellSizeControl.show();
				break;
				
			case VORONOI_DELAUNAY:
				
				cellSizeControl.hide();
				
				vDselection.show();
				addRandomVDPoints.show();
				clearVD.show();
				break;
				
			default:
				break;
				
		}
		
	}
	
	public void setMaskImage(PImage i_maskImage){
			       
	    PImage tempImg = i_maskImage.get();
	    
	    if (tempImg.width > tempImg.height)
	       tempImg.resize(80, 0);
	    else
	       tempImg.resize(0, 80);
	   
	    maskImageButton.setImage(tempImg);
	}
	
	public void setSettingsFromGrid(TileGrid i_grid){
		
		PImage mskImg = i_grid.getMaskImage();
		
		if(mskImg != null)
			maskImageButton.setImage(mskImg);
		
		cellSizeControl.setValue(i_grid.getCellRadius());
		oddsControl.setValue(i_grid.getMissingOdds());
		useMaskControl.setValue(i_grid.getUseMask());
		
	}


}
