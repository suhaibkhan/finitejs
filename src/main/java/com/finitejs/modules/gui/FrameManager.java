package com.finitejs.modules.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import com.finitejs.system.FiniteJS;

public class FrameManager {

	private Map<String, CachableFrame> frameCache;
	
	private static FrameManager instance;
	
	protected FrameManager(){
		frameCache = new HashMap<String, CachableFrame>();
	}
	
	public static FrameManager getInstance(){
		if (instance == null){
			instance = new FrameManager();
		}
		return instance;
	}
	
	public String create(int width, int height, String title){
		
		// set default title if title is empty
		if (title == null || title.equals("")){
			title = FiniteJS.APP_NAME;
		}
		
		// Create frame.
		CachableFrame frame = new CachableFrame(title);
		String frameId = frame.getFrameId();
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Add close event
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				CachableFrame sourceFrame = (CachableFrame) e.getSource();
				frameCache.remove(sourceFrame.getFrameId());
				sourceFrame.dispose();
		    }
		});
		
		// Show frame
		frame.setVisible(true);
		
		// Save frame in cache for future reuse.
		frameCache.put(frameId, frame);
		
		// Return frame id.
		return frameId;
	}
	
	public void resize(String frameId, int width, int height){
		JFrame frame = frameCache.get(frameId);
		frame.setSize(width, height);
	}
	
	public void dispose(String frameId){
		JFrame frame = frameCache.get(frameId);
		frameCache.remove(frameId);
		frame.dispose();
	}
	
}
