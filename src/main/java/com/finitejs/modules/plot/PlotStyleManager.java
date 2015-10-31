package com.finitejs.modules.plot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.finitejs.modules.core.FileUtils;

/**
 * Singleton class for handling plot styles.
 * This class will be initialized from finite.js plot module.
 */
public class PlotStyleManager {

	/**
	 * Constant for default plot module theme.
	 */
	public static final String DEFAULT_THEME = "default";
	
	/**
	 * Constant for directory name inside plot module which 
	 * contains the theme files.
	 */
	private static final String THEME_DIRECTORY = "themes";
	
	/**
	 * Constant for extension of theme files.
	 */
	private static final String THEME_EXTENSION = ".json";

	/**
	 * {@code PlotStyleManager} singleton instance.
	 */
	private static PlotStyleManager instance;
	
	/** Path to the themes directory. */
	private String themesPath;
	
	/** Map that contains style attributes read from theme files. */
	private Map<String, String> styleMap;
	
	private PlotStyleManager(String plotModulePath) throws FileNotFoundException{
		
		if (plotModulePath == null){
			throw new NullPointerException("Plot module path cannot be null.");
		}
		
		// get path to themes directory
		File themesDir = new File(plotModulePath, THEME_DIRECTORY);
		if (!themesDir.exists()){
			throw new FileNotFoundException("Plot themes directory not found.");
		}else{
			themesPath = themesDir.getAbsolutePath();
		}
		
		styleMap = new HashMap<>();
	}
	
	/**
	 * Creates and returns singleton instance of plot style manager.
	 * 
	 * @param plotModulePath  path to the plot module directory
	 * @return instance of {@code PlotStyleManager}
	 * @throws FileNotFoundException if themes directory not found
	 */
	public static PlotStyleManager getInstance(String plotModulePath) throws FileNotFoundException{
		if (instance == null){
			instance = new PlotStyleManager(plotModulePath);
		}
		return instance;
	}
	
	/**
	 * Read contents of the specified theme.
	 * 
	 * @param theme  theme name
	 * @return theme file contents
	 * @throws IOException if error while reading theme file
	 */
	public String readTheme(String theme) throws IOException{
		
		String themeContents;
		
		if (theme == null){
			throw new NullPointerException("Theme cannot be null.");
		}
		
		// check whether theme file exists
		
		if (!theme.endsWith(THEME_EXTENSION)){
			theme = theme.concat(THEME_EXTENSION);
		}
		
		File themeFile = new File(themesPath, theme);
		if (!themeFile.exists()){
			throw new FileNotFoundException("Plot theme file not found.");
		}
		
		// read theme file 
		themeContents = FileUtils.readJSONFile(themeFile);
		return themeContents;
	}
	
	/**
	 * Set/replace style attributes loaded from theme file.
	 * 
	 * @param themeMap  style attributes as key-value pair
	 */
	public void setTheme(Map<String, String> themeMap){
		styleMap.putAll(themeMap);
	}
	
	/**
	 * Returns value of plot style attribute/property with specified key.
	 * 
	 * @param key  key of style attribute/property
	 * @return attribute/property value
	 */
	public String get(String key){
		return styleMap.get(key);
	}
}
