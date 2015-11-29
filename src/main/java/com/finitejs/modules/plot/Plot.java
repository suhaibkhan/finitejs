package com.finitejs.modules.plot;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a plot.
 */
public class Plot {
	
	/** List of series in plot */
	private List<Series> seriesList;
	
	/** List of axes */
	private List<Axis> axisList;
	
	/** Plot style */
	private PlotStyle style;
	
	/** Plot frame */
	private PlotFrame frame;
	
	/** Plot width */
	private int width;
	
	/** Plot height */
	private int height;
	
	/** 
	 * Constructor
	 */
	public Plot(){
		seriesList = new ArrayList<>();
		axisList = new ArrayList<>();
		
		// initialize plot style with attributes from theme
		// plot should be created only after loading theme
		style = new PlotStyle(PlotThemeManager.getInstance().getTheme());
		
		// init frame
		frame = new PlotFrame(style);
	}
	
	/**
	 * Set plot dimensions.
	 * 
	 * @param width  width of the plot
	 * @param height  height of the plot
	 */
	public void setSize(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Get width of the plot.
	 * 
	 * @return width
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * Get height of the plot.
	 * 
	 * @return height
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * Returns plot style.
	 * 
	 * @return {@link PlotStyle} instance
	 */
	public PlotStyle getStyle(){
		return style;
	}
	
	/**
	 * Returns plot frame.
	 * 
	 * @return {@link PlotFrame} instance
	 */
	public PlotFrame getFrame(){
		return frame;
	}
	
	public void render(Graphics2D g){
		// draw frame
		g.drawImage(frame.paint(width, height), 0, 0, null);
	}
	
}
