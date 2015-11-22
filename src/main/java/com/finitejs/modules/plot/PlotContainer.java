package com.finitejs.modules.plot;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.finitejs.modules.core.ConsoleUtils;
import com.finitejs.system.FiniteJS;

/**
 * Class represents a container for plot frame/window.
 * Plot frame/window can be manipulated only using an instance 
 * of this class.
 */
public class PlotContainer {
	
	/** Constant for default window title. */
	public static final String DEFAULT_WINDOW_TITLE = FiniteJS.APP_NAME;

	/** Frame/Window in which the plot is rendered. */
	private JFrame frame;
	
	/** List of plot panels in frame. */
	private List<PlotPanel> plotPanelList;
	
	/** Window/Frame title */
	private String title;
	
	/** Window/Frame width */
	private int width;
	
	/** Window/Frame height */
	private int height;
	
	/**
	 * Creates a plot container.
	 * 
	 * @param width  width of the window in pixels
	 * @param height  height of the window in pixels
	 * @param title  title of the window
	 */
	public PlotContainer(int width, int height, String title){
		plotPanelList = new ArrayList<>();
		
		this.width = width;
		this.height = height;
		
		// use default title if null
		if (title != null){
			this.title = title;
		}else{
			this.title = DEFAULT_WINDOW_TITLE;
		}
	}
	
	/**
	 * Initializes the plot window.
	 * 
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public void init() throws InvocationTargetException, InterruptedException{
		
		Runnable thread = new Runnable(){
			@Override
			public void run() {
				if (frame == null){
					frame = new JFrame();
					
					// use absolute positioning in frame
					frame.getContentPane().setLayout(null);
					
					// set title
					frame.setTitle(title);
					
					// set size
			        Insets insets = frame.getContentPane().getInsets();
					frame.getContentPane().setPreferredSize(new Dimension(
							width + insets.left + insets.right, 
							height + insets.top + insets.bottom));
					
					// resize frame to content pane
					frame.pack();
					
					// center frame
					frame.setLocationRelativeTo(null);
					
					// add resize event
					frame.addComponentListener(new ComponentAdapter(){
						@Override
						public void componentResized(ComponentEvent e){
							redraw();
						}
					});
					
					// add close event
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					frame.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							if (frame != null){
								frame.dispose();
								frame = null;
							}
					    }
					});
				}
			}
		};
		
		execInEventDispatchThread(thread, true);
	}
	
	/**
	 * Redraws the plot window by redrawing all plot panels present.
	 */
	public void redraw(){
		
		Runnable thread = new Runnable(){

			@Override
			public void run() {	
				if (!plotPanelList.isEmpty()){
					for (PlotPanel plotPanel : plotPanelList){
						
						// update layout helper
						plotPanel.getLayoutHelper().setParentDimensions(
								frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
						
						// update bounds
						Insets insets = frame.getContentPane().getInsets();
						plotPanel.setBounds(insets.left + plotPanel.getLayoutHelper().getLeft(), 
								insets.top + plotPanel.getLayoutHelper().getTop(), 
								plotPanel.getLayoutHelper().getWidth(), 
								plotPanel.getLayoutHelper().getHeight());
					}
				}	
			}
			
		};
		
		try {
			// initiate redraw and do not wait to complete
			execInEventDispatchThread(thread, false);
		} catch (InvocationTargetException e) {
			// log error
			ConsoleUtils.errorln("Error while redrawing plot : " + e.getMessage());
			if (FiniteJS.DEBUG){
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			// log error
			ConsoleUtils.errorln("Error while redrawing plot : " + e.getMessage());
			if (FiniteJS.DEBUG){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Disposes the plot window.
	 * 
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public void dispose() throws InvocationTargetException, InterruptedException{
		
		Runnable thread = new Runnable(){

			@Override
			public void run() {	
				if (frame != null){
					plotPanelList.clear();
					frame.dispose();
					frame = null;
				}	
			}
			
		};
		
		execInEventDispatchThread(thread, true);
	}
	
	/**
	 * Adds a plot to the created window.
	 * 
	 * @param plot  plot to be added
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public void addPlot(final Plot plot) throws InvocationTargetException, InterruptedException{
		Runnable thread = new Runnable(){

			@Override
			public void run() {	
				
				if (plot != null && frame != null){
					
					// create a new panel with default layout specifiers
					PlotLayoutHelper layoutHelper  = new PlotLayoutHelper("0%", "0%", "100%", "100%");
					layoutHelper.setParentDimensions(
							frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
					PlotPanel plotPanel = new PlotPanel(plot, layoutHelper);
					plotPanelList.add(plotPanel);
					
					// specify size
					Insets insets = frame.getContentPane().getInsets();
					plotPanel.setBounds(insets.left + plotPanel.getLayoutHelper().getLeft(), 
							insets.top + plotPanel.getLayoutHelper().getTop(), 
							plotPanel.getLayoutHelper().getWidth(), 
							plotPanel.getLayoutHelper().getHeight());
					
					// add panel to frame
					frame.getContentPane().add(plotPanel);
				}
				
			}
			
		};
		
		execInEventDispatchThread(thread, true);
	}
	
	/**
	 * Makes the plot window visible.
	 * By default the plot window is hidden.
	 * 
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public void show() throws InvocationTargetException, InterruptedException{
		Runnable thread = new Runnable(){

			@Override
			public void run() {	
				if (frame != null){
					frame.setVisible(true);
				}
			}
			
		};
		
		execInEventDispatchThread(thread, true);
	}
	
	/**
	 * Executes a thread in the event dispatch thread.
	 * 
	 * @param thread  thread to be executed
	 * @param wait  if true, the current thread is blocked until 
	 * pending events have been completed
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	private static void execInEventDispatchThread(Runnable thread, 
			boolean wait) throws InvocationTargetException, InterruptedException{
		
		// check current thread
		// to avoid deadlock
		if (SwingUtilities.isEventDispatchThread()){
			thread.run();
		}else{
			if (wait){
				SwingUtilities.invokeAndWait(thread);
			}else{
				SwingUtilities.invokeLater(thread);
			}
		}
	}
}
