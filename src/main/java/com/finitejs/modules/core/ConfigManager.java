package com.finitejs.modules.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class for handling configurations.
 *
 */
public class ConfigManager {
	
	/**
	 * Constant for root path configuration.
	 */
	public static final String ROOT_PATH = "finitejs.rootpath";
	
	/**
	 * Constant for JavaScript modules path configuration.
	 */
	public static final String MODULES_PATH = "finitejs.modulespath";
	
	/**
	 * Constant for configuration file name.
	 */
	// private static final String CONFIG_FILENAME = "finitejs_config.json";
	
	
	/**
	 * {@code ConfigManager} singleton instance.
	 */
	private static ConfigManager instance;
	
	/**
	 * Map for storing configurations.
	 */
	private Map<String, String> configurationMap;
	
	private ConfigManager(){
		configurationMap = new HashMap<String, String>();
		// add root path to configuration
		getRootPath();
		// add modules path to configuration
		getModulesPath();
	}
	
	/**
	 * Creates and returns singleton instance of configuration manager.
	 * 
	 * @return instance of {@code ConfigManager}
	 */
	public static ConfigManager getInstance(){
		if (instance == null){
			instance = new ConfigManager();
		}
		return instance;
	}
	
	/**
	 * Add a group of configurations to configuration manager.
	 * 
	 * @param config  Map containing configuration key-value pair
	 */
	public void save(Map<String, String> config){
		configurationMap.putAll(config);
	}
	
	/**
	 * Add a configuration to configuration manager.
	 * 
	 * @param key  configuration key
	 * @param value  configuration value
	 */
	public void add(String key, String value){
		if (key.equals(ROOT_PATH)){
			// won't allow to change root path
			return;
		}else if(key.equals(MODULES_PATH)){
			// print a warning
			ConsoleUtils.errorf("Modifying modules directory to %s.%n", value);
		}
		configurationMap.put(key, value);
	}
	
	/**
	 * Get a configuration from configuration manager.
	 * 
	 * @param key  configuration key
	 * @return configuration value
	 */
	public String get(String key){
		String value = null;
		if (configurationMap.containsKey(key)){
			value = configurationMap.get(key);
		}
		return value;
	}
	
	/**
	 * Reads and returns the contents of configuration file.
	 * 
	 * @return configuration file contents
	 * @throws IOException 
	 */
	/*
	// TODO config file reading
	private String readConfigFile() throws IOException{
		String configFileContents = "";
		// Config file must be put in the root dir.
		File configFile = new File(getRootPath(), CONFIG_FILENAME);
		if (configFile.exists()){
			configFileContents = FileUtils.readTextFile(configFile);
		}
		return configFileContents;
	}
	*/
	
	/**
	 * Get root path saved in the configuration manager.
	 * If not present, retrieves it using {@code FileUtils.getRootPath()} 
	 * and adds to configuration manager.
	 * 
	 * @return root path
	 */
	public String getRootPath(){
		String rootPath = get(ROOT_PATH);
		if (rootPath == null){
			rootPath = FileUtils.getRootPath();
			configurationMap.put(ROOT_PATH, rootPath);
		}
		return rootPath;
	}
	
	/**
	 * Get modules path saved in the configuration manager.
	 * If not present, finds it and adds to configuration manager.
	 * 
	 * <p>All JavaScript modules are loaded relative to this path.</p>
	 * 
	 * @return modules path
	 */
	public String getModulesPath(){
		String modulesPath = get(MODULES_PATH);
		if (modulesPath == null){
			File modulesDir = new File(getRootPath(), ModuleLoaderUtils.getDefaultModulesDir());
			modulesPath = modulesDir.getAbsolutePath();
			configurationMap.put(MODULES_PATH, modulesPath);
		}
		return modulesPath;
	}
}
