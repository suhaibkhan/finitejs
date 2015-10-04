package com.finitejs.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import com.finitejs.modules.core.ConsoleUtils;
import com.finitejs.modules.core.ModuleLoaderUtils;

/**
 * Singleton class for initiating and using JavaScript ScriptEngine.
 * This class handles all script related tasks, also this
 * class is responsible for starting finite.js by loading core module.
 */
public class JSEngine {
	
	/**
	 * Constant for JavaScript global variable name that indicates debug flag.
	 */
	public static final String DEBUG_VAR_NAME = "__debug";
	
	/**
	 * Constant for JavaScript global variable name that contains 
	 * main file name passed as command line argument. finite.js core will
	 * use this variable to load main file.
	 */
	public static final String MAIN_FILE_VAR_NAME = "__mainfile";
	
	/**
	 * Constant for JavaScript global variable name that 
	 * indicates shell mode flag. This flag is checked by finite.js core to
	 * start the program in REPL mode.
	 */
	public static final String SHELL_MODE_VAR_NAME = "__shell";
	
	/** Constant for name of script engine used. */
	private static final String ENGINE_NAME = "nashorn";
	
	/** 
	 * Constant for variable name used by module loader to export that 
	 * library. This variable contains JavaScript module loader, 
	 * which is used to load finite.js core as another module.
	 */
	private static final String MODULE_LOADER_EXPORT_VAR_NAME = "exports";
	
	/**
	 * Syntax template for executing finite.js core with the help 
	 * of JavaScript module loader.
	 */
	private static final String CORE_EXECUTE_TEMPLATE = "%s._require('%s');";
	
	/** JSEngine singleton instance. */
	private static JSEngine instance;
	
	/** Script Engine instance used to execute JavaScript. */
	private ScriptEngine engine;
	
	/** Map for storing global variables, which are shared across all contexts. */
	private Map<String, Object> globalMap;
	
	/** Flag to indicate whether finite.js started or not. */
	private boolean finiteJsStarted;
	
	/**
	 * Returns singleton instance of JSEngine.
	 * 
	 * @return JSEngine instance
	 */
	public static JSEngine getInstance(){
		if (instance == null){
			instance = new JSEngine();
		}
		return instance;
	}
	
	private JSEngine(){
		engine = new ScriptEngineManager().getEngineByName(ENGINE_NAME);
		globalMap = new HashMap<String, Object>();
		finiteJsStarted = false;
	}
	
	/**
	 * Adds a variable to global map. 
	 * These global variables will be shared across all contexts.
	 * 
	 * @param varName  name of the global variable
	 * @param object  value of the variable
	 */
	public void addGlobalVariable(String varName, Object object){
		globalMap.put(varName, object);
	}
	
	/**
	 * Adds a variable to specified context. Unlike global variables, this variable
	 * will be only available in this context.
	 * 
	 * @param varName  name of the variable
	 * @param object  value of the variable
	 * @param context  context to which variable needs to be added
	 */
	public void addLocalVariable(String varName, Object object, ScriptContext context){
		context.getBindings(ScriptContext.ENGINE_SCOPE).put(varName, object);
	}
	
	/**
	 * Adds a group of local variables to specified context.
	 * 
	 * @param varMap  map containing variables as key-value pairs
	 * @param context  context to which variables need to be added
	 */
	public void addAllLocalVariables(Map<String, Object> varMap, ScriptContext context){
		context.getBindings(ScriptContext.ENGINE_SCOPE).putAll(varMap);
	}
	
	/**
	 * Loads module loader. 
	 * Used by {@code startFiniteJS} method to load finite.js core module.
	 * Will terminate if loader module not found.
	 */
	private void loadModuleLoader(){
		String moduleLoaderPath = ModuleLoaderUtils.getModuleLoaderPath();
		if (moduleLoaderPath == null){
			ConsoleUtils.errorln("Module loader missing. Try reinstalling the application.");
			System.exit(1);
		}
		
		Exception loadException = null;
		
		try {
			String moduleLoaderScript = ModuleLoaderUtils.readScript(moduleLoaderPath);
			engine.eval(moduleLoaderScript);
		} catch (IOException e) {
			loadException = e;
		} catch (ScriptException e) {
			loadException = e;
		}
		
		if (loadException != null){
			ConsoleUtils.errorln(loadException);
			ConsoleUtils.errorf("Module loader failed to load from %s%n.", moduleLoaderPath);
			System.exit(1);
		}
	}
	
	/**
	 * Starts finite.js by loading core module.
	 * Will terminate if core module not found.
	 */
	public void startFiniteJS(){
		
		if (finiteJsStarted){
			// already running
			return;
		}
		
		// just started
		finiteJsStarted = true;
		
		// make globals variable available in this context
		addAllLocalVariables(globalMap, engine.getContext());
		
		// initialize an exports variable in this context
		// so that module loader can expose library
		addLocalVariable(MODULE_LOADER_EXPORT_VAR_NAME, 
				new HashMap<String, Object>(), engine.getContext());
		
		// load module loader
		// it will create MODULE_LOADER_EXPORT_VAR_NAME._require 
		// function for loading core module
		loadModuleLoader();
		
		// get core js path
		String coreModulePath = ModuleLoaderUtils.getCoreModulePath();
		if (coreModulePath == null){
			ConsoleUtils.errorln("Core module missing. Try reinstalling the application.");
			System.exit(1);
		}
				
		try {
			String coreExecuteScript = String.format(CORE_EXECUTE_TEMPLATE, 
					MODULE_LOADER_EXPORT_VAR_NAME, coreModulePath);
			engine.eval(coreExecuteScript);
		} catch (ScriptException e) {
			ConsoleUtils.errorln(e);
			ConsoleUtils.errorf("Core module failed to load from %s%n.", coreModulePath);
			System.exit(1);
		}
	}
	
	/**
	 * Creates a new JavaScript context for executing script.
	 * Used by module loader as each module is loaded in its own context.
	 * A set of variables are shared with this newly created context 
	 * along with global variables.
	 * 
	 * @param sharedObjectMap  map containing variables to be shared
	 * @return new context
	 */
	public ScriptContext createNewContext(Map<String, Object> sharedObjectMap){
		
		// create new context
		ScriptContext context = new SimpleScriptContext();
		context.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
		
		// make globals variable available in this context
		addAllLocalVariables(globalMap, context);
		
		// set shared objects in this context
		addAllLocalVariables(sharedObjectMap, context);
				
		return context;
		
	}
	
	/**
	 * Executes script in the specified context.
	 * 
	 * @param script  script to be executed
	 * @param context  context in which script needs to be executed
	 * @return result of script execution
	 * @throws ScriptException if error occurs while executing script
	 */
	public Object evalInContext(String script, ScriptContext context) throws ScriptException{
		// run script
		return engine.eval(script, context);
	}
}
