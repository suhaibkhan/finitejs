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
	
	public PlotPanel(Plot plot, PlotLayoutHelper layoutHelper){
		this.plot = plot;
		this.layoutHelper = layoutHelper;
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
			plot.render(g2, getWidth(), getHeight());
		}
		g2.dispose();
	}
}
