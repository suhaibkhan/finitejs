package com.finitejs.modules.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class for handling configurations.
 * 
 * @author Suhaib Khan
 *
 */
public class ConfigManager {
	
	// Configuration Keys
	public static final String ROOT_PATH = "finitejs.rootpath";
	public static final String MODULES_PATH = "finitejs.modulespath";
	
	private static final String CONFIG_FILENAME = "finitejs_config.json";
	
	private static ConfigManager instance;
	private Map<String, String> configurationMap;
	
	protected ConfigManager(){
		configurationMap = new HashMap<String, String>();
		// add root path to configuration
		getRootPath();
		// add modules path to configuration
		getModulesPath();
	}
	
	public static ConfigManager getInstance(){
		if (instance == null){
			instance = new ConfigManager();
		}
		return instance;
	}
	
	public void save(Map<String, String> config){
		configurationMap.putAll(config);
	}
	
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
	
	public String get(String key){
		String value = null;
		if (configurationMap.containsKey(key)){
			value = configurationMap.get(key);
		}
		return value;
	}
	
	/**
	 * Read and returns the contents of configuration file.
	 * 
	 * @return configuration file contents
	 * @throws IOException 
	 */
	public String readConfigFile() throws IOException{
		String configFileContents = "";
		// Config file must be put in the root dir.
		File configFile = new File(getRootPath(), CONFIG_FILENAME);
		if (configFile.exists()){
			configFileContents = FileUtils.readTextFile(configFile);
		}
		return configFileContents;
	}
	
	public String getRootPath(){
		String rootPath = get(ROOT_PATH);
		if (rootPath == null){
			rootPath = FileUtils.getRootPath();
			configurationMap.put(ROOT_PATH, rootPath);
		}
		return rootPath;
	}
	
	public String getModulesPath(){
		String modulesPath = get(MODULES_PATH);
		if (modulesPath == null){
			modulesPath = String.format("%s%s%s", getRootPath(), 
					File.separator, ModuleLoaderUtils.getDefaultModulesDir());
			configurationMap.put(MODULES_PATH, modulesPath);
		}
		return modulesPath;
	}
}
