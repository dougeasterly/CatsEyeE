package com.catseye.gui.guiPanes;

import java.io.File;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

import com.catseye.CatsEyeController;
import com.catseye.HandlerActions;
import com.catseye.gui.components.ImageSelectionTool;
import com.catseye.gui.components.ImageSelectionWidget;
import com.catseye.gui.components.SvgSelectionTool;
import com.catseye.gui.p5Plugs.ImageSelectionControls;
import com.catseye.patternComponents.gridGenerators.TileGrid;
import com.catseye.util.svgUtils.SVGLoader;
import com.quickdrawProcessing.display.DisplayPane;
import com.quickdrawProcessing.display.Stage;

public class ImageSelectionPane extends DisplayPane {

	ImageSelectionControls ctls;
	ImageSelectionTool selector;
	
	protected String printWidth, printHeight;
	protected boolean isLoaded = false;
	
	public ImageSelectionPane(PVector i_position, PVector i_size) {
		super(i_position, i_size);
		
		printWidth="1000";
		printHeight="1000";
	}
	
	@Override
	protected void addedToStage(){
		ctls = new ImageSelectionControls(this, Stage.cp5);
		draw();
	}
	
	public void draw(PGraphics i_context){
		PGraphics currentContext = preDraw(i_context);
			currentContext.fill(clearColor);
			currentContext.noStroke();
			currentContext.rect(0, 0, size.x, size.y);
		postDraw(currentContext);
	}

	public void loadImage(){
		File imageFile = new File("");
	    Stage.p5.selectInput("select image", "loadTextureImage", imageFile, this);
	}

	public void loadTextureImage(File selection) {
	    
	    if (selection == null) {
	      System.out.println("Window was closed or the user hit cancel.");
	    } 
	    else {
	  
	      String path = selection.getAbsolutePath();
	      String suffix = path.substring(path.length()-3);
	      
	      removeChild(selector);
	      
	      int selectionType = selector == null || selector.getSelectionType() == ImageSelectionWidget.MARQUEE ? ImageSelectionWidget.MARQUEE : ImageSelectionWidget.TRIANGULAR;
	      
	      if (suffix.equals("svg")){
	    	  PShape chosenShape = Stage.p5.loadShape(path);
		      setImageTool(chosenShape, selectionType);	    	  
	      }else{
		      PImage chosenImage = Stage.p5.loadImage(path);
		      setImageTool(chosenImage, selectionType);
	      }	      
	      
	      updateMarqueeToggle();
	    }
	  }

	public void setImageTool(PShape i_shape, int i_marqueeType){
		
		if(selector != null)
			selector.destroy();

		removeChild(selector);
		
		selector = new SvgSelectionTool(new PVector(0,70), new PVector(getSize().x, getSize().y-70), i_shape, i_marqueeType);
	      
	    addChild(selector);
	    
	    isLoaded = true;
	}
	
	public void setImageTool(PImage i_image, int i_marqueeType){
		
		if(selector != null)
			selector.destroy();
		
		removeChild(selector);
		
		selector = new ImageSelectionTool(new PVector(0,70), new PVector(getSize().x, getSize().y-70), i_image, i_marqueeType);
	      
	    addChild(selector);
	    
	    isLoaded = true;
	}
	
	public void toggleMarquee(){
		
		if(selector != null){
			selector.toggleMarqueeType();
			updateMarqueeToggle();
			draw();
		}
	}
	
	public void setSettingsFromGrid(TileGrid i_grid){
		PVector renderSize = i_grid.getRenderSize();
		printWidth = ((int)renderSize.x)+"";
		printHeight= ((int)renderSize.y)+"";
		
		
		int marqueeType =  (int)i_grid.getTextureCoords()[3].x;
		setImageTool(i_grid.getTextureImage(), marqueeType);
		updateMarqueeToggle();
		
		selector.setSettingsFromGrid(i_grid);
		selector.draw();
		
		ctls.setSettingsFromGrid(i_grid);
	}
	
	public void updateMarqueeToggle(){
		ctls.setSelectionMarqueeLabel(selector.getSelectionType() == ImageSelectionWidget.MARQUEE ? "Triangular Selector" : "Marquee Selector");
	}
	
	public void generate(){
		interactionHandler.actionHook(this,  HandlerActions.GENERATE);
	}
	
	public PVector getRenderSize(){
		int w = Integer.parseInt(printWidth);
		int h = Integer.parseInt(printHeight);
		return new PVector(w,h);
	}
	
	public PImage getTexture(){
		return selector.getImage();
	}
	
	public PVector[] getTexCoords(){
		return selector.getTextureCoordinates();
	}
	
	public boolean isLoaded(){
		return isLoaded;
	}
	
}
