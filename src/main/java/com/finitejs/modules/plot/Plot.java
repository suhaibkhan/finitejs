package com.finitejs.modules.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * Abstract parent class of all plots.
 * Responsible for rendering common plot elements like 
 * margin, border, padding, title.
 * <pre>
 * Plot Anatomy
 * ============
 * 
 *          |----------- PLOT WIDTH ------------|
 *        
 *    ---    ----------------------------------- 
 *     |    |              MARGIN               |
 *     |    |    ======== BORDER ==========     |
 *     |    |   |          PADDING         |    |
 *     |    |   |    -----------------     |    |--->> PLOT PANEL
 *     |    |   |   |      TITLE      |    |    |
 *     |    |   |   |-----------------|    |    |
 *    PLOT  |   |   |                 |    |    |
 *   HEIGHT |   |   |   PLOT REGION   |    |    |
 *     |    |   |   |                 |    |    |
 *     |    |   |   |                 |    |    |
 *     |    |   |    -----------------     |    |
 *     |    |   |                          |    |
 *     |    |    ==========================     |
 *     |    |                                   |
 *    ---    ----------------------------------- 
 *    
 * </pre>
 */
public abstract class Plot {
	
	/** Font style constants */
	private static final String PLAIN = "plain";
	private static final String ITALIC = "italic";
	private static final String BOLD = "bold";
	private static final String BOLD_ITALIC = "bold italic";
	
	/** Plot width */
	private int width;
	
	/** Plot height */
	private int height;
	
	/** Plot title */
	private String title;
	
	/** Plot title font */
	private Font titleFont;
	
	/**
	 * Variables cached for drawing multiline title.
	 */
	private LineBreakMeasurer titleLineBreakMeasurer;
	private int titleCharBeginIndex;
	private int titleCharEndIndex;
	
	/** Rectangle that defines plot region */
	private Rectangle plotRegionRect;
	
	protected Plot(String title){
		// set title
		this.title = title;
		
		// plot should be created only after
		// loading all style attributes
		
		// load title font and cache it
		String titleFontName = getStyle(PlotStyleAttributes.PLOT_TITLE_FONT);
		int titleFontSize = Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_TITLE_FONT_SIZE));
		String titleFontStyleName = getStyle(PlotStyleAttributes.PLOT_TITLE_FONT_STYLE);
		
		titleFont = new Font(titleFontName, getFontStyle(titleFontStyleName), titleFontSize);
		// empty rectangle
		plotRegionRect = new Rectangle();
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
	 * Set plot title.
	 * 
	 * @param title  title of the plot
	 */
	public void setTitle(String title){
		this.title = title;
		// clear existing LineBreakMeasurer
		titleLineBreakMeasurer = null;
	}
	
	/**
	 * Get plot title.
	 * 
	 * @return title
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * Returns the rectangle that defines the plot region.
	 * 
	 * @return plot region bounds as a {@link Rectangle}
	 */
	public Rectangle getPlotRegion(){
		return plotRegionRect;
	}
	
	/**
	 * Returns value of the specified style attribute loaded from theme file.
	 * 
	 * @param attributeName  style attribute name
	 * @return value of the style attribute
	 */
	protected String getStyle(String attributeName){
		return PlotStyleManager.getInstance().get(attributeName);
	}
	
	/**
	 * Get font style integer value from style name.
	 * 
	 * @param styleName  font style name
	 * @return font style integer value
	 */
	protected int getFontStyle(String styleName){
		int fontStyle;
		switch (styleName.toLowerCase()){
			case PLAIN :  
				fontStyle = Font.PLAIN;
				break;
			case ITALIC : 
				fontStyle = Font.ITALIC;
				break;
			case BOLD :
				fontStyle = Font.BOLD;
				break;
			case BOLD_ITALIC :
				fontStyle = Font.BOLD | Font.ITALIC;
				break;
			default :
				fontStyle = Font.PLAIN;
		}
		return fontStyle;
	}
	
	public void render(Graphics2D g){
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int topMargin = Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_TOP_MARGIN));
		int rightMargin = Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_RIGHT_MARGIN));
		int bottomMargin = Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_BOTTOM_MARGIN));
		int leftMargin = Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_LEFT_MARGIN));
		
		int topPadding = Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_TOP_PADDING));
		int rightPadding = Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_RIGHT_PADDING));
		int bottomPadding = Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_BOTTOM_PADDING));
		int leftPadding = Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_LEFT_PADDING));
				
		// fill panel background if any
		String panelBackgroundColor = getStyle(PlotStyleAttributes.PANEL_BACKGROUND);
		if (panelBackgroundColor != null){
			g.setPaint(Color.decode(panelBackgroundColor));
			g.fill(new Rectangle2D.Double(0, 0, width, height));
		}
		
		// find plot dimensions excluding margin and border
		int borderStrokeWidth = Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_BORDER_WIDTH));
		int plotBoxLeft = leftMargin + borderStrokeWidth;
		int plotBoxTop = topMargin + borderStrokeWidth;
		int plotBoxWidth = width - (leftMargin + rightMargin) - (borderStrokeWidth * 2);
		int plotBoxHeight = height - (topMargin + bottomMargin) - (borderStrokeWidth * 2);
		
		//  fill plot region including padding
		String plotBackgroundColor = getStyle(PlotStyleAttributes.PLOT_BACKGROUND);
		if (plotBackgroundColor != null){
			g.setPaint(Color.decode(plotBackgroundColor));
			g.fill(new Rectangle2D.Double(plotBoxLeft, plotBoxTop, plotBoxWidth, plotBoxHeight));
		}
		
		// draw border
		String borderColor = getStyle(PlotStyleAttributes.PLOT_BORDER_COLOR);		
		if (borderStrokeWidth > 0){
			
			int borderLeft = leftMargin + borderStrokeWidth/2;
			int borderTop = topMargin + borderStrokeWidth/2;
			int borderBoxWidth = width - (leftMargin + rightMargin) - borderStrokeWidth;
			int borderBoxHeight = height - (topMargin + bottomMargin) - borderStrokeWidth;
			
			BasicStroke borderStroke = new BasicStroke(borderStrokeWidth);

			g.setStroke(borderStroke);
			g.setPaint(Color.decode(borderColor));
			g.draw(new Rectangle2D.Double(borderLeft, borderTop, borderBoxWidth, borderBoxHeight));
		}
		
		int titleHeight = 0; // required to find plot region
		// draw title
		if (title != null && !"".equals(title)){
			
			// if title doesn't fit in single line
			// will be spanned across multiple lines
			
			// create a new LineBreakMeasurer from the title
	        // it will be cached and re-used
			if (titleLineBreakMeasurer == null){
				AttributedString styledString = new AttributedString(title);
				styledString.addAttribute(TextAttribute.FONT, titleFont);
				// set color
				String titleFontColor = getStyle(PlotStyleAttributes.PLOT_TITLE_FONT_COLOR);
				styledString.addAttribute(TextAttribute.FOREGROUND, Color.decode(titleFontColor));
				
				AttributedCharacterIterator titleCharItr = styledString.getIterator();
				titleCharBeginIndex = titleCharItr.getBeginIndex();
				titleCharEndIndex = titleCharItr.getEndIndex();
	            FontRenderContext frc = g.getFontRenderContext();
	            titleLineBreakMeasurer = new LineBreakMeasurer(titleCharItr, frc);
			}
			
			// find title dimensions
			int titleLeft = plotBoxLeft + leftPadding;
			int titleTop = plotBoxTop + topPadding;
			int titleWidth = plotBoxWidth - (leftPadding + rightPadding);
			
			// draw multiline title
			float drawPosY = titleTop;
			titleLineBreakMeasurer.setPosition(titleCharBeginIndex);
			while(titleLineBreakMeasurer.getPosition() < titleCharEndIndex){
				// retrieve next layout
				TextLayout layout = titleLineBreakMeasurer.nextLayout(titleWidth);
				
				// compute pen x position
				// if the paragraph is right-to-left we
	            // will align the TextLayouts to the right edge of the panel
				float drawPosX = layout.isLeftToRight()
		                ? titleLeft : titleWidth - layout.getAdvance();
				
				// move y-coordinate by the ascent of the layout
	            drawPosY += layout.getAscent();
	            	            
	            // draw the TextLayout at center
	            Rectangle2D bounds = layout.getBounds();
	            layout.draw(g, drawPosX + (titleWidth - (float)bounds.getWidth()) / 2, drawPosY);

	            // move y-coordinate in preparation for next layout
	            drawPosY += layout.getDescent() + layout.getLeading();
			}
			
			// update title height
			titleHeight = Math.round(drawPosY) - titleTop;
			// add spacing with region to title height
			titleHeight += Integer.parseInt(getStyle(PlotStyleAttributes.PLOT_TITLE_REGION_SPACING));

		}
		
		// find plot region dimensions
		int plotRegionTop = plotBoxTop + topPadding + titleHeight;
		int plotRegionLeft = plotBoxLeft + leftPadding;
		int plotRegionHeight = plotBoxHeight - (topPadding + bottomPadding + titleHeight);
		int plotRegionWidth = plotBoxWidth - (leftPadding + rightPadding);
		
		// update plot region
		plotRegionRect.setBounds(plotRegionLeft, plotRegionTop, plotRegionWidth, plotRegionHeight);
		
	}
	
}
