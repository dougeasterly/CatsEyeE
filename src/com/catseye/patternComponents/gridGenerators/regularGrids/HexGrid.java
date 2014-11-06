package com.catseye.patternComponents.gridGenerators.regularGrids;

import com.catseye.patternComponents.gridGenerators.TileGrid;

import processing.core.PGraphics;

/*---------------------------------------------------------------------------------------------
*
*    HexGrid
*    
*    Implementation of hexagonal tiling grid
*
*    Ben Jack 6/4/2014 
*
*---------------------------------------------------------------------------------------------*/

public class HexGrid extends TileGrid{
 
  
 public HexGrid(){
   super();
 }
 
 public HexGrid(TileGrid i_oldGrid){
   super(i_oldGrid);
 }
 
 
 public void generate(boolean i_outlines){
   
      setupRegularNgonGenerator(6);
      PGraphics currentContext = initGeneration(i_outlines);
      
      int cellsY = (int)(currentContext.height/cellSize.x+2);
      int cellsX = (int)(currentContext.width /cellSize.y+2);
    
      for (int i = 0; i < cellsX; ++i) {
        for (int j = 0; j < cellsY; ++j) {
        
          float centerX = (i-1)*(cellSize.x*2);
          if (j%2 == 1) centerX += cellSize.x;
    
          float centerY = (j-1)*(cellRadius+cellSize.y);
    
          drawNgonAt(centerX, centerY, currentContext);
          
        }
      }
      
      completeGeneration(i_outlines);
  }
  
}
	
