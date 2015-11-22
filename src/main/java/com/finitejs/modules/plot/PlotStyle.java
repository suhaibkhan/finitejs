package com.finitejs.modules.plot;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

public class PlotStyle {

	/* ***************** Plot Style Attribute Names ************** */
	
	/** Constant for panel background color key. */
	public static final String PANEL_BACKGROUND = "panel.background";
	
	/** Constant for plot background color key. */
	public static final String PLOT_BACKGROUND = "plot.background";
	
	/** Constant for plot left margin key. */
	public static final String PLOT_LEFT_MARGIN = "plot.margin.left";
	
	/** Constant for plot top margin key. */
	public static final String PLOT_TOP_MARGIN = "plot.margin.top";
	
	/** Constant for plot right margin key. */
	public static final String PLOT_RIGHT_MARGIN = "plot.margin.right";
	
	/** Constant for plot bottom margin key. */
	public static final String PLOT_BOTTOM_MARGIN = "plot.margin.bottom";
	
	/** Constant for plot left padding key. */
	public static final String PLOT_LEFT_PADDING = "plot.padding.left";
	
	/** Constant for plot top padding key. */
	public static final String PLOT_TOP_PADDING = "plot.padding.top";
	
	/** Constant for plot right padding key. */
	public static final String PLOT_RIGHT_PADDING = "plot.padding.right";
	
	/** Constant for plot bottom padding key. */
	public static final String PLOT_BOTTOM_PADDING = "plot.padding.bottom";
	
	/** Constant for plot border stroke width key. */
	public static final String PLOT_BORDER_WIDTH = "plot.border.width";
	
	/** Constant for plot border color key. */
	public static final String PLOT_BORDER_COLOR = "plot.border.color";
	
	/** Constant for plot title font key. */
	public static final String PLOT_TITLE_FONT = "plot.title.font";
	
	/** Constant for plot title font size key. */
	public static final String PLOT_TITLE_FONT_SIZE = "plot.title.font.size";
	
	/** Constant for plot title font style key. */
	public static final String PLOT_TITLE_FONT_STYLE = "plot.title.font.style";
	
	/** Constant for plot title font color key. */
	public static final String PLOT_TITLE_FONT_COLOR = "plot.title.font.color";
	
	/** Constant for plot title - region spacing key. */
	public static final String PLOT_TITLE_REGION_SPACING = "plot.title_region.spacing";
	
	/* *********************************************************** */
	
	/* ***** Font style constants **** */
	public static final String FONT_STYLE_PLAIN = "plain";
	public static final String FONT_STYLE_ITALIC = "italic";
	public static final String FONT_STYLE_BOLD = "bold";
	public static final String FONT_STYLE_BOLD_ITALIC = "bold italic";
	/* ******************************* */
	
	/** Map that contains style attributes for each plot. */
	private Map<String, String> styleMap;
	
	/**
	 * Constructor
	 */
	public PlotStyle(){
		styleMap = new HashMap<>();
	}
	
	/**
	 * Constructor
	 * 
	 * @param styleMap  map containing style name-value pairs.
	 */
	public PlotStyle(Map<String, String> styleMap){
		this.styleMap = new HashMap<>();
		if (styleMap != null && !styleMap.isEmpty()){
			this.styleMap.putAll(styleMap);
		}
	}
	
	/**
	 * Returns value of plot style attribute/property with specified name.
	 * 
	 * @param name  name of style attribute/property
	 * @return attribute/property value
	 * @throws NullPointerException if style attribute not found
	 */
	public String get(String name){
		if (!styleMap.containsKey(name)){
			throw new NullPointerException(
					String.format("Style attribute %s not found in plot theme", name));
		}
		return styleMap.get(name);
	}
	
	/**
	 * Set style attribute/property.
	 * 
	 * @param name  name of style attribute/property
	 * @param value  value of style attribute/property
	 */
	public void set(String name, String value){
		styleMap.put(name, value);
	}
	
	/**
	 * Get font style integer value from style name.
	 * 
	 * @param styleName  font style name
	 * @return font style integer value
	 */
	public static int getFontStyle(String styleName){
		int fontStyle;
		switch (styleName.toLowerCase()){
			case FONT_STYLE_PLAIN :  
				fontStyle = Font.PLAIN;
				break;
			case FONT_STYLE_ITALIC : 
				fontStyle = Font.ITALIC;
				break;
			case FONT_STYLE_BOLD :
				fontStyle = Font.BOLD;
				break;
			case FONT_STYLE_BOLD_ITALIC :
				fontStyle = Font.BOLD | Font.ITALIC;
				break;
			default :
				fontStyle = Font.PLAIN;
		}
		return fontStyle;
	}
}
