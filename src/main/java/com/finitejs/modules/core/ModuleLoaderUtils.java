package com.finitejs.modules.core;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for finitejs modules loading operations.
 * 
 * @author Suhaib Khan
 *
 */
public class ModuleLoaderUtils {
	
	public static final String JS_EXTENSION = ".js";
	public static final String MODULE_PACKAGE_FILE_NAME = "package.json";
	public static final String[] DEFAULT_PACKAGE_MAIN = {"index.js", "main.js"};
	
	private static final String DEFAULT_MODULE_DIRNAME = "modules";
	private static final String CORE_MODULE_DIRNAME = "core";
	private static final String CORE_MODULE_NAME = "finite";
	private static final String LOADER_MODULE_NAME = "module";
	
	// Module template - All modules will be wrapped around this template.
	private static final String MODULE_TEMPLATE = String.format("%s%s", 
			"(function (exports, require, module, __filename, __dirname){",
			"%n%s%n}).call(this, module.exports, module.require, module, '%s', '%s');");
	
	/**
	 * 
	 * @param modulePath
	 * @return
	 * @throws ModuleLoadException
	 */
	public static String readModule(String modulePath) throws ModuleLoadException{
		String moduleContents = "";
		try {
			moduleContents = FileUtils.readTextFile(modulePath);
		} catch (IOException e) {
			String msg = String.format("Module not found in %s.", modulePath);
			throw new ModuleLoadException(msg, e);
		}
		
		// return module contents by wrapping anonymous function
		return String.format(MODULE_TEMPLATE, 
				moduleContents, modulePath, new File(modulePath).getParent());
	}
	
	public static String readScript(String path) throws IOException{
		String script = FileUtils.readTextFile(path);
		return script;
	}
	
	/**
	 * 
	 * @param moduleId
	 * @param parentFilePath
	 * @return
	 */
	public static String resolveModuleFilePath(
			String moduleId, String parentFilePath, boolean isMain){
		
		String modulePath = null;
		
		if (moduleId == null || moduleId.isEmpty()){
			return modulePath;
		}
		
		// check for absolute files
		File moduleFile = new File(moduleId);
		if (moduleFile.isAbsolute()){
			
			if (moduleFile.exists()){
				modulePath = moduleId;
			}
			
			// null if module not found
			return modulePath;
		}
		
		// if parent path is not available use current working dir as parent path
		if (parentFilePath == null || isMain){
			parentFilePath = FileUtils.getWorkingDir();
		}
				
		// correct slashes
		moduleId = moduleId.replace("\\\\", "/");
		moduleId = moduleId.replace("\\", "/");
		
		// correct main file path to make it relative to working dir
		if (isMain && !moduleId.startsWith("./") && !moduleId.startsWith("../")){
			// prepend ./
			moduleId = String.format("%s%s", "./", moduleId);
		}
		
		// check whether relative to parent file path
		if (moduleId.startsWith("./")){
			// strip ./
			moduleId = moduleId.substring(2);
			
			// correct environment based file seperator
			moduleId = moduleId.replaceAll("/", File.separator);
			
			// get parent directory
			File parentFile = new File(parentFilePath);
			String parentDirPath = parentFile.getParent();
			if (parentFile.isDirectory()){
				// if parentFilePath is working directory
				parentDirPath = parentFile.getAbsolutePath();
			}
			
			modulePath = checkModuleExists(parentDirPath, moduleId);
			
			// null if module not found
			return modulePath;
		}
		
		// check whether relative to parent of parent file path
		if (moduleId.startsWith("../")){
			// strip ./
			moduleId = moduleId.substring(3);
			
			// correct environment based file seperator
			moduleId = moduleId.replaceAll("/", File.separator);
			
			// get parent directory
			File parentFile = new File(parentFilePath);
			String parentDirPath = parentFile.getParentFile().getParent();
			if (parentFile.isDirectory()){
				// if parentFilePath is working directory
				parentDirPath = parentFile.getParent();
			}
			
			modulePath = checkModuleExists(parentDirPath, moduleId);
			
			// null if module not found
			return modulePath;
		}
		
		// check whether core module in modules/core dir
		File coreModuleDir = new File(ConfigManager.getInstance().getModulesPath(), 
				CORE_MODULE_DIRNAME);
		String coreModuleDirPath = coreModuleDir.getAbsolutePath();
		
		modulePath = checkModuleExists(coreModuleDirPath, moduleId);
		
		if (modulePath != null){
			return modulePath;
		}
		
		// modules other than core modules
		String modulesDirPath = ConfigManager.getInstance().getModulesPath();
		
		modulePath = checkModuleExists(modulesDirPath, moduleId);
		
		return modulePath;
	}
	
	private static String checkModuleExists(String parentDir, String moduleId){
		
		String modulePath = null;
		
		File moduleFile = null;
		File moduleDir = null;
		
		if (moduleId.endsWith(JS_EXTENSION)){
			// check for file only
			moduleFile = new File(parentDir, moduleId);
		}else{
			// check for file and dir
			moduleFile = new File(parentDir, moduleId.concat(JS_EXTENSION));
			moduleDir = new File(parentDir, moduleId);
		}
		
		// first preference for file
		if (moduleFile.exists()){
			modulePath = moduleFile.getAbsolutePath();
		}
		
		if (modulePath == null && moduleDir != null && moduleDir.exists()){
			modulePath = moduleDir.getAbsolutePath();
		}
		
		// escape windows file string
		modulePath = modulePath.replace("\\", "\\\\");
		
		// null if module not found
		return modulePath;
		
	}
	
	public String readModulePackageFile(String modulePath) throws IOException{
		String packageFileContents = null;
		File packageFile = new File(modulePath, MODULE_PACKAGE_FILE_NAME);
		if (packageFile.exists()){
			packageFileContents = FileUtils.readTextFile(packageFile);
		}
		return packageFileContents;
	}
	
	public String checkModulePackageMain(String packageDir, String packageMainFile){
		String modulePath = null;
		File packageMain;
		
		if (packageMainFile != null){
			
			// correct slashes
			packageMainFile = packageMainFile.replace("\\\\", "/");
			packageMainFile = packageMainFile.replace("\\", "/");
			
			// correct environment based file seperator
			packageMainFile = packageMainFile.replaceAll("/", File.separator);
			
			// check whether package main file exists
			packageMain = new File(packageDir, packageMainFile);
			if (packageMain.exists() && packageMain.isFile()){
				modulePath = packageMain.getAbsolutePath();
			}
		}else{
			
			// check for default main files
			for (int i = 0; i < DEFAULT_PACKAGE_MAIN.length; i++){
				packageMain = new File(packageDir, DEFAULT_PACKAGE_MAIN[i]);
				if (packageMain.exists() && packageMain.isFile()){
					modulePath = packageMain.getAbsolutePath();
					break;
				}
			}
		}
		
		// escape windows file string
		modulePath = modulePath.replace("\\", "\\\\");
		
		return modulePath;
	}
	
	public static String getDefaultModulesDir(){
		return DEFAULT_MODULE_DIRNAME;
	}
	
	/**
	 * @return
	 */
	public static String getModuleLoaderPath(){
		String moduleLoaderPath = 
				resolveModuleFilePath(LOADER_MODULE_NAME, null, false);
		return moduleLoaderPath;
	}
	
	public static String getCoreModulePath(){
		String coreModulePath = 
				resolveModuleFilePath(CORE_MODULE_NAME, null, false);
		return coreModulePath;
	}
	
}
