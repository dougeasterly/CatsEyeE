package com.catseye.util.colourUtils;

/*---------------------------------------------------------------------------------------------
*
*    SVGColour
*    
*    Implementation of SVG colour settings
*
*    Douglas Easterly 13/5/2014 
*
*---------------------------------------------------------------------------------------------
*/
import processing.core.*;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import com.catseye.CatsEye;
import java.awt.Color;


public class SVGColour {
	public int p5Color = CatsEye.p5.color(255,255,255);
	private Color tempColor;
	private JColorChooser colorPopUpWindow = null;
	JFrame frame = new JFrame();
	
	 public int SVGColour( )
	  {
		 colorPopUpWindow.showDialog(frame, "Color Chooser", Color.white);
		 if(colorPopUpWindow!=null) 
			 p5Color       = CatsEye.p5.color(tempColor.getRed(),tempColor.getGreen(),tempColor.getBlue());
	return p5Color;
	  }




}

