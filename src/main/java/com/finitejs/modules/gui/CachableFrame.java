package com.finitejs.modules.gui;

import java.util.UUID;

import javax.swing.JFrame;

/**
 * 
 * @author Suhaib Khan
 *
 */
public class CachableFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final String FRAME_ID_FORMAT = "frame-%s";
	
	private String frameId;
	
	public CachableFrame(){
		super();
		generateFrameId();
	}
	
	public CachableFrame(String title){
		super(title);
		generateFrameId();
	}

	public String getFrameId() {
		return frameId;
	}

	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}
	
	private void generateFrameId(){
		String frameId = String.format(FRAME_ID_FORMAT, UUID.randomUUID().toString());
		this.setFrameId(frameId);
	}

}
