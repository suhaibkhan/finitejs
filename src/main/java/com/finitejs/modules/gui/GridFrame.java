package com.finitejs.modules.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class GridFrame extends CachableFrame{

	private static final long serialVersionUID = 1L;
		
	public GridFrame(int rows, int cols, int width, int height, String title){
		super(title);
		setSize(width, height);
		
		// set grid layout
		setLayout(new GridLayout(rows, cols));
	}
	
	public GridFrame(int width, int height, String title){
		this(1, 1, width, height, title);
	}
	
	public GridFrame(int width, int height){
		this(1, 1, width, height, null);
	}
	
	public void addToGrid(JPanel panel){
		add(panel);
	}
}
