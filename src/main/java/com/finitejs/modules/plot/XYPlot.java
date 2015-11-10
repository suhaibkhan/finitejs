package com.finitejs.modules.plot;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Abstract parent class of all x-y plots.
 */
public abstract class XYPlot extends Plot{
	
	public XYPlot(String title){
		super(title);
	}
	
	public void render(Graphics2D g){
		
		super.render(g);
		
		Rectangle plotRegion = getPlotRegion();
		
		g.draw(plotRegion);
		
		// TODO
		// draw legend
		// draw axes
	}
	
}
