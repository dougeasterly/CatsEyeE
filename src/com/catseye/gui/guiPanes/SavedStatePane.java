package com.catseye.gui.guiPanes;

import java.io.File;
import java.util.ArrayList;

import com.catseye.CatsEye;
import com.catseye.gui.components.SavedStateLoader;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.quickdrawProcessing.display.DisplayPane;
import com.quickdrawProcessing.display.Stage;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.JSONObject;

public class SavedStatePane extends DisplayPane implements Runnable{

	public static final String SAVEPATH = "data/saveData/"; 
	
	protected Thread loaderThread;
	protected PVector saveSize, saveSpace;
	protected int saveCount = 0;
	
	public SavedStatePane(PVector i_position, PVector i_size) {
		super(i_position, i_size);
		
		saveSize = new PVector(i_size.y-40, i_size.y-40);
		saveSpace = new PVector(20, 20);
	}
	
	public void addSave(JSONObject jsonString){
		SavedStateLoader ldr = new SavedStateLoader(new PVector(saveCount*(saveSize.x+saveSpace.x)+saveSpace.x, saveSpace.y), saveSize, jsonString);
		addChild(ldr);
	}
	
	@Override
	public void addedToStage(){
		loaderThread = new Thread(this);
		loaderThread.start();
	}
	
	public void draw(PGraphics i_context){
		PGraphics context = preDraw(i_context);
		context.background(180);
		postDraw(context);
	}

	@Override
	public void run() {

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
	
}
