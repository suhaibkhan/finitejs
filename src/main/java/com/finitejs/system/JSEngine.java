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
 * Singleton class for initiating and using Nashorn ScriptEngine.
 * 
 * @author Suhaib Khan
 */
public class JSEngine {
	
	// global variable names
	public static final String DEBUG_VAR_NAME = "__debug";
	public static final String MAIN_FILE_VAR_NAME = "__mainfile";
	public static final String SHELL_MODE_VAR_NAME = "__shell";
	
	// Name of script engine used
	private static final String ENGINE_NAME = "nashorn";
	
	// variable used by module loader to export library
	private static final String MODULE_LOADER_EXPORT_VAR_NAME = "exports";
	
	// Syntax template for executing finite js core with the
	// help of module loader js.
	private static final String CORE_EXECUTE_TEMPLATE = "%s._require('%s');";
	
	// JSEngine singleton instance
	private static JSEngine instance;
	
	// Script Engine used to execute js
	private ScriptEngine engine;
	
	// map for storing variables shared across all contexts
	private Map<String, Object> globalMap;
	
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
	
	protected JSEngine(){
		engine = new ScriptEngineManager().getEngineByName(ENGINE_NAME);
		globalMap = new HashMap<String, Object>();
	}
	
	public void addGlobalVariable(String varName, Object object){
		globalMap.put(varName, object);
	}
	
	public void addLocalVariable(String varName, Object object, ScriptContext context){
		context.getBindings(ScriptContext.ENGINE_SCOPE).put(varName, object);
	}
	
	public void addAllLocalVariables(Map<String, Object> varMap, ScriptContext context){
		context.getBindings(ScriptContext.ENGINE_SCOPE).putAll(varMap);
	}
	
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
	
	public void startFiniteJS(){
		
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
			engine.eval(String.format(CORE_EXECUTE_TEMPLATE, 
					MODULE_LOADER_EXPORT_VAR_NAME, coreModulePath));
		} catch (ScriptException e) {
			ConsoleUtils.errorln(e);
			ConsoleUtils.errorf("Core module failed to load from %s%n.", coreModulePath);
			System.exit(1);
		}
	}
	
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
	
	public Object evalInContext(String script, ScriptContext context) throws ScriptException{
		// run script
		return engine.eval(script, context);
	}
}
