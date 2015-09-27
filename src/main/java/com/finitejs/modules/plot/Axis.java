package com.finitejs.modules.plot;

/**
 * Class represents axis in a chart.
 * 
 * @author Suhaib Khan
 *
 */
public class Axis {

	public enum Alignment {HORIZONTAL, VERTICAL};
	
	private String[] ticks;
	
	private Alignment alignment;
	
	private String lineColor;
	
	private String tickColor;
	
	private int lineWidth;
	
	private int tickLineWidth;
	
	private String title;
	
	private String fontName;
	
	private int titleFontSize;
	
	private int tickFontSize;
}
