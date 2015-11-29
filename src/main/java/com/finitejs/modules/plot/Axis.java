package com.finitejs.modules.plot;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Class represents axis in a chart.
 *
 */
public class Axis {

	public enum Alignment {HORIZONTAL, VERTICAL};
	
	private Scale<?, ?> scale;
	
	private String title;
	
	private final PlotStyle style;
	
	private Dimension dimension;
	
	public Axis(final PlotStyle style){
		this.style = style;
		dimension = new Dimension();
	}
	
	public Image drawAxis(int width, int height){
		Image image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.dispose();
				
		return image;
	}
}
