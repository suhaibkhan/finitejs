package com.finitejs.system;

import com.finitejs.modules.core.ConfigManager;

/**
 * Class for processing command line arguments.
 */
public class CmdLineArgProcessor {
	
	/**
	 * Constant for command line argument flag to 
	 * change modules directory.
	 */
	public static final String MODULES_DIR_SETTING = "-ModulesDir";
	
	/**
	 * Constant for command line argument flag to 
	 * enable debug mode.
	 */
	public static final String DEBUG_ENABLE_SETTING = "-Debug";
	
	/**
	 * Processes command line arguments passed to finite.js executable.
	 * 
	 * @param args command line arguments
	 */
	public static void process(final String[] args){
				
		String mainFile = null;
		
		if (args.length > 0){
			
			int newModulesDirPos = -1;
			
			for (int  i = 0 ; i < args.length; i++){
				if (args[i].equalsIgnoreCase(MODULES_DIR_SETTING)){
					// next argument is expected to be modules dir path
					newModulesDirPos = ++i;
				}else if (args[i].equalsIgnoreCase(DEBUG_ENABLE_SETTING)){
					// enable debug mode 
					FiniteJS.DEBUG = true;
				}else{
					// first non-settings argument will be considered 
					// as main file to be executed.
					if (mainFile == null){
						mainFile = args[i];
					}
				}
			}
			
			// set new modules dir
			if (newModulesDirPos >= 0 && args.length > newModulesDirPos){
				ConfigManager.getInstance().add(ConfigManager.MODULES_PATH, args[newModulesDirPos]);
			}
		}
		
		// set debug mode
		JSEngine.getInstance().addGlobalVariable(JSEngine.DEBUG_VAR_NAME, FiniteJS.DEBUG);
		
		boolean shellMode = false;
		
		if (mainFile != null){
			// main file will be executed by finitejs core js
			// main file path will be available as global variable __mainfile
			JSEngine.getInstance().addGlobalVariable(JSEngine.MAIN_FILE_VAR_NAME, mainFile);
		}else{
			// shell mode
			shellMode = true;
		}
		
		// global variable to indicate REPL mode
		JSEngine.getInstance().addGlobalVariable(JSEngine.SHELL_MODE_VAR_NAME, shellMode);		
	}
	
}
