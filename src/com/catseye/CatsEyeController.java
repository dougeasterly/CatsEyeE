package com.catseye;

import java.io.File;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

import com.catseye.gui.components.SavedStateLoader;
import com.catseye.gui.guiPanes.GridSelectPane;
import com.catseye.gui.guiPanes.ImageDisplayPane;
import com.catseye.gui.guiPanes.ImageSelectionPane;
import com.catseye.gui.guiPanes.SavedStatePane;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.display.Stage;

public class CatsEyeController extends InteractiveDisplayObject{
	
	private String savePath;
	
	private ImageSelectionPane selector;
	private ImageDisplayPane patternDisplay;
	private GridSelectPane gridPane;
	private SavedStatePane savePane;
	
	protected CatsEyeController(PVector i_position, PVector i_size) {
		super(i_position, i_size);
	}
	
	@Override
	public void addedToStage(){
	     selector = new ImageSelectionPane(new PVector(0,0), new PVector(size.x/3.0f, size.y/2.0f));
	     selector.setInteractionHandler(this);
	     addChild(selector);
	     
	     gridPane = new GridSelectPane(new PVector(0, size.y/2.0f), new PVector(size.x/3.0f, size.y/2.0f));
	     gridPane.setInteractionHandler(this);
	     addChild(gridPane);
	     
	     patternDisplay = new ImageDisplayPane(new PVector(size.x/3.0f, 0), new PVector(2.0f*(size.x/3.0f), size.y-140));
	     patternDisplay.setInteractionHandler(this);
	     addChild(patternDisplay);
	     
	     savePane = new SavedStatePane(new PVector(size.x/3.0f, size.y-140), new PVector(2.0f*(size.x/3.0f), 140));
	     savePane.setInteractionHandler(this);
	     addChild(savePane);
	}
	
	private void setupSavePath(){
		 
		 String[] savePathFile = Stage.p5.loadStrings("data/imageSavePath.txt");
	     File f = new File(""); 
	     
	     if(savePathFile == null){
	    	  Stage.p5.selectFolder("Select a folder to save images into:", "savePathSelected", f, this);
	     }else
	    	 savePath = savePathFile[0];
	     
	}
	
	public void savePathSelected(File selection) {
		  if (selection == null) {
		    System.out.println("Window was closed or the user hit cancel.");
		  } else {
		    savePath = selection.getAbsolutePath();
		    String[] outString = new String[1];
		    outString[0] = savePath;
		    Stage.p5.saveStrings("data/imageSavePath.txt", outString);
		  }
	}

	@Override 
	public void actionHook(InteractiveDisplayObject i_child, int i_action){
		
		switch(i_action){
			case HandlerActions.GENERATE:
				
				System.gc();
				
				if(selector.isLoaded()){
					TileGrid grid = gridPane.getTileGrid();
					grid.setRenderSize(selector.getRenderSize());
					grid.setTexture(selector.getTexture());
					grid.setTextureCoords(selector.getTexCoords());
					grid.generate();
 				
					patternDisplay.setImages(grid.getRender(), grid.getGridImage());
				}
				
				break;
			
			case HandlerActions.SAVESETTINGS:
				
					TileGrid grid = gridPane.getTileGrid();
					JSONObject save = grid.saveAsJSON();
					savePane.addSave(save);
					
				break;
				

			case HandlerActions.LOADSETTINGS:
					
					SavedStateLoader loader = (SavedStateLoader)i_child;
					TileGrid loadedGrid = loader.getGrid();
					gridPane.setGrid(loadedGrid);
					selector.setSettingsFromGrid(loadedGrid);
					patternDisplay.setSettingsFromGrid(loadedGrid);
					selector.draw();
					patternDisplay.draw();
					gridPane.draw();
					
				
				break;
				
			case HandlerActions.SAVEIMAGE:

					if(savePath == null)
					   setupSavePath();

					System.out.println("saving...");
					gridPane.getTileGrid().saveImage(getSavePath("images"));
				break;
			

			case HandlerActions.SAVETILE:
					
					if(savePath == null)
						setupSavePath();
				
					gridPane.getTileGrid().saveTile(getSavePath("tiles"));
				break;
				
			
			default:
				break;
		}
		
	}

	@Override
	public boolean isOver(PVector i_position) {
		return inBounds(i_position);
	}
	
	String[] months = {
	      "january", "febuary", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"
	  };
	  
	public String getSavePath(String i_saveType) { 
		return savePath+"/images/"+PApplet.year()+"/"+months[PApplet.month()-1]+"/"+PApplet.day()+"/"+i_saveType+"/"+PApplet.hour()+"_"+PApplet.minute()+"_"+PApplet.second();
  	}
	
}
