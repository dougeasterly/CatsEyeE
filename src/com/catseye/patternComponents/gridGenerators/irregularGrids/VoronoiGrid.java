package com.catseye.patternComponents.gridGenerators.irregularGrids;

import com.catseye.patternComponents.gridGenerators.TileGrid;

public class VoronoiGrid extends VoronoiDelaunayGrid {

	
	  public VoronoiGrid() {
		 super();
		 init();
	  }

	  public VoronoiGrid(TileGrid i_oldGrid) {
		 super(i_oldGrid);
		 init();
	  }
	
	@Override
	protected void init(){
		type = VORONOI;
	}
	
	
}
