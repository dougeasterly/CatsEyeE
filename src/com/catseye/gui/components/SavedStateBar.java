package com.catseye.gui.components;

import java.io.File;
import java.util.ArrayList;

import com.catseye.CatsEye;
import com.catseye.patternComponents.gridGenerators.TileGrid;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class SavedStateBar {

	public static final String SAVEPATH = "data/saveData/"; 
	private PApplet parent;
	ArrayList<SavedStateLoader> saves;
	
	public SavedStateBar(PApplet i_parent) {
		
		parent = i_parent;
		
		saves = new ArrayList<SavedStateLoader>();
		File[] files = listFiles(SAVEPATH);
		
		if(files != null){
			for(int i = 0; i < files.length; ++i){
				
				if(files[i].isDirectory()){
						String saveFolder = files[i].getAbsolutePath();
						System.out.println(saveFolder);
						JSONObject jsonString = CatsEye.p5.loadJSONObject(saveFolder+"/saveData.json");
						addSave(jsonString);
					
				}
			}
		}
		
	}
	
	public void addSave(JSONObject jsonString){
		SavedStateLoader ldr = new SavedStateLoader(jsonString);
		saves.add(ldr);	
	}
	
	public void draw(){
		
		for(int i = 0; i < saves.size(); ++i){
			parent.image(saves.get(i).getImage(), i*120+20, parent.height - 100);
		}
		
	}
	
	public TileGrid getClicked(PVector i_mousePos){
		
		for(int i = 0; i < saves.size(); ++i){
			if(i_mousePos.x > i*120+20 && i_mousePos.x < i*120+20+100 && i_mousePos.y > parent.height - 100)
				return saves.get(i).getGrid();
		}
		
		return null;
		
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
	
}
