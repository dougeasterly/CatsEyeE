package com.catseye.gui.guiPanes;

import java.io.File;
import java.util.ArrayList;

import com.catseye.CatsEye;
import com.catseye.HandlerActions;
import com.catseye.gui.components.SavedStateLoader;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.DisplayPane;
import com.quickdrawProcessing.display.InteractiveDisplayObject;
import com.quickdrawProcessing.display.Stage;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.JSONObject;

public class SavedStatePane extends DisplayPane{

	public static final String SAVEPATH = "data/saveData/"; 
	
	protected PVector saveSize, saveSpace;
	
	protected ArrayList<SavedStateLoader> saves;
	
	public SavedStatePane(PVector i_position, PVector i_size) {
		super(i_position, i_size);
		
		saves = new ArrayList<SavedStateLoader>();
		saveSize = new PVector(i_size.y-40, i_size.y-40);
		saveSpace = new PVector(20, 20);
		
		onlyRedrawWhileMouseOver(true);
		drawBorder = true;
	}
	
	public void addSave(JSONObject jsonString){
		int saveCount = saves.size();
		SavedStateLoader ldr = new SavedStateLoader(new PVector(saveCount*(saveSize.x+saveSpace.x)+saveSpace.x, saveSpace.y), saveSize, jsonString);
		ldr.setInteractionHandler(interactionHandler);
		addChild(ldr);
		saves.add(ldr);
		redrawNow();
	}
	
	@Override
	public void addedToStage(){
		loadFiles();
		redrawNow();
	}
	
	public void draw(PGraphics i_context){
		i_context.background(180);
		i_context.fill(0);
		i_context.text("Saved files ", 20, 15);
	}

	public void loadFiles() {

		File[] files = listFiles(SAVEPATH);
		
		if(files != null){
			for(int i = 0; i < files.length; ++i){
				
				if(files[i].isDirectory()){
					String saveFolder = files[i].getAbsolutePath();
					JSONObject jsonString = Stage.p5.loadJSONObject(saveFolder+"/saveData.json");
					addSave(jsonString);
				}
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	
	private File[] listFiles(String dir) {
		 File file = new File(dir);
		 if (file.isDirectory()) {
		   File[] files = file.listFiles();
		   return files;
		 } else {
		   // If it's not a directory
		   return null;
		 }
	}

	@Override
	public void actionHook(InteractiveDisplayObject i_obj, int i_action){
		
		if(i_action == HandlerActions.CLOSE){
			removeChild(i_obj);
			int ind = saves.indexOf(i_obj);
			for(int i = ind; i < saves.size(); ++i){
				saves.get(i).setPositionFromLocal(new PVector((i-1)*(saveSize.x+saveSpace.x)+saveSpace.x, saveSpace.y));
			}
			saves.remove(i_obj);
			
		}
		
	}
	
	
}
