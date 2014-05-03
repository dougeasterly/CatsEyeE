package com.catseye.patternComponents.gridGenerators;

import processing.core.PGraphics;

/*---------------------------------------------------------------------------------------------
*
*    triGrid
*    
*    Implementation of triagonal tiling grid
*
*    Ben Jack 6/4/2014 
*
*---------------------------------------------------------------------------------------------*/

public class TriGrid extends TileGrid{
 
  
 public TriGrid(){
   super();
 }
 
 public TriGrid(TileGrid i_oldGrid){
   super(i_oldGrid);
 }
 
 public void generate(boolean i_outlines){
   
      setupRegularNgonGenerator(3);
      PGraphics currentContext = initGeneration(i_outlines);
    
      float triHeight  = cellSize.x + cellRadius;
      float cellWidth  = cellSize.x;
      float cellHeight = cellSize.y;
    
      int cellsY = (int)(currentContext.height/triHeight+2);
      int cellsX = (int)(currentContext.width /triHeight+2);
    
      for (int flip = 0; flip < 2; ++flip) {
        for (int i = -1; i < cellsX; ++i) {
          for (int j = -1; j < cellsY; ++j) {
    
            currentContext.pushMatrix();
    
            float centerY = j*(cellHeight*2) + (i%2 == 0? cellHeight : 0) + (flip%2==1?cellHeight:0);
            float centerX = (i*triHeight + (flip%2==1 ? cellWidth : 0))*(flip%2==1?1:-1);
    
            if (flip % 2 == 0) {
              currentContext.scale(-1, 1);
            }
    
            drawNgonAt(centerX, centerY, currentContext);
    
            currentContext.popMatrix();
          }
        }
      }
    
      completeGeneration(i_outlines);
  }
  
}
