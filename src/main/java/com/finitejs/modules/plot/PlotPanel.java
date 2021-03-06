package com.finitejs.modules.plot;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Custom panel for drawing a plot.
 */
public class PlotPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/** {@code Plot} instance used to draw the component. */
	private Plot plot;
	
	/** {@code PlotLayoutHelper} used position the plot inside the parent. */
	private PlotLayoutHelper layoutHelper;
	
	/**
	 * Creates a plot panel.
	 * 
	 * @param plot  plot to be drawn on the panel
	 * @param layoutHelper  layout helper with positioning attributes
	 */
	public PlotPanel(Plot plot, PlotLayoutHelper layoutHelper){
		this.plot = plot;
		this.layoutHelper = layoutHelper;
	}
	
	/**
	 * Returns the plot drawn on this panel.
	 * 
	 * @return plot 
	 */
	public Plot getPlot(){
		return plot;
	}
	
	/**
	 * Returns the layout helper used to position this plot.
	 * 
	 * @return {@code PlotLayoutHelper}
	 */
	public PlotLayoutHelper getLayoutHelper(){
		return layoutHelper;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
		Graphics2D g2 = (Graphics2D)g;
		if (plot != null){
			// update plot size
			plot.setSize(getWidth(), getHeight());
			plot.render(g2);
		}
		g2.dispose();
	}
}
