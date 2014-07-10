package com.catseye.gui.windows;

import java.awt.Frame;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PVector;

public class GUIWindowManager{
  
  private HashMap<String, Frame> windows;
  private HashMap<String, PApplet> childApps;

  
  public GUIWindowManager(){
	  windows = new HashMap<String, Frame>();
	  childApps = new HashMap<String, PApplet>();
  }  
  
  public PApplet addWindow(String i_name, GUIApp i_child) {
	  return addWindow(i_name, i_child, false);
  }
  
  public PApplet addWindow(String i_name, GUIApp i_child, boolean i_visible) {
	    Frame f = new Frame(i_name);
	    f.add(i_child);
	    i_child.init();
	    f.setTitle(i_name);
	    f.setSize(i_child.width(), i_child.height());
	    f.setLocation(0, windows.size()*150);
	    f.setResizable(false);
	    f.setVisible(i_visible);
	    f.setAlwaysOnTop(true);
	    windows.put(i_name, f);
	    childApps.put(i_name, i_child);
	    return i_child;
  }
  
  public PApplet getApp(String i_name){
	 return childApps.get(i_name);
  }
  
  public Frame getWindow(String i_name){
	 return windows.get(i_name);
  }
  
  public void setVisible(String i_name, boolean i_visible){
	  windows.get(i_name).setVisible(i_visible);
  }
  
}
