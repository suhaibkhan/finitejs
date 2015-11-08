package com.finitejs.modules.plot;

/**
 * Helper class to parse plot layout specifiers.
 * A plot is positioned inside a window with the help of this class.
 */
public class PlotLayoutHelper {
	
	/** Constant for pixel value prefix. */
	private static final String PIXEL_PREFIX = "px";
	
	/** Constant for percentage value prefix. */
	private static final String PERCENTAGE_PREFIX = "%";

	/** Left position specifier */
	private String left;
	
	/** Top position specifier */
	private String top;
	
	/** Width measurement specifier */
	private String width;
	
	/** Height measurement specifier */
	private String height;
	
	/** Width of parent component */
	private int parentWidth;
	
	/** Height of parent component */
	private int parentHeight;
	
	public PlotLayoutHelper(String left, String top, String width, String height){
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Set parent component dimensions. Used for relative measurement specifiers 
	 * like percentage.
	 * 
	 * @param width  width of the parent component
	 * @param height  height of the parent component
	 */
	public void setParentDimensions(int width, int height){
		this.parentWidth = width;
		this.parentHeight = height;
	}
	
	/**
	 * Returns left position of the plot with current layout specifiers.
	 * 
	 * @return left position in pixels
	 */
	public int getLeft(){
		return Math.round(getMeasurement(left, parentWidth));
	}
	
	/**
	 * Returns top position of the plot with current layout specifiers.
	 * 
	 * @return top position in pixels
	 */
	public int getTop(){
		return Math.round(getMeasurement(top, parentHeight));
	}
	
	/**
	 * Returns width of the plot with current layout specifiers.
	 * 
	 * @return width in pixels
	 */
	public int getWidth(){
		return Math.round(getMeasurement(width, parentWidth));
	}
	
	/**
	 * Returns height of the plot with current layout specifiers.
	 * 
	 * @return height in pixels
	 */
	public int getHeight(){
		return Math.round(getMeasurement(height, parentHeight));
	}
	
	/**
	 * Converts measurement specifier into values.
	 * 
	 * @param measurementSpecifier  measurement specifier
	 * @param relativeMeasurement  relative measurement, needed in 
	 * case of specifiers like percentage
	 * @return measurement value
	 * @throws NullPointerException if specifier is null
	 * @throws NumberFormatException if invalid measurement specifier
	 */
	private static float getMeasurement(String measurementSpecifier, int relativeMeasurement){
		float relativeFactor = 1;
		
		// remove whitespaces from specifier
		measurementSpecifier = measurementSpecifier.toLowerCase().replaceAll("\\s", "");
		
		if (measurementSpecifier.endsWith(PIXEL_PREFIX)){
			// check whether a pixel value
			measurementSpecifier = measurementSpecifier.substring(0, measurementSpecifier.length() - 2);
		}else if (measurementSpecifier.endsWith(PERCENTAGE_PREFIX)){
			// check whether a percentage value
			measurementSpecifier = measurementSpecifier.substring(0, measurementSpecifier.length() - 1);
			relativeFactor = relativeMeasurement/100f;
		}
		
		// parse 
		float measurement = Float.parseFloat(measurementSpecifier) * relativeFactor;
		
		// handle negative values
		if (measurement < 0){
			measurement = relativeMeasurement + measurement;
		}
		return measurement;
	}
}
