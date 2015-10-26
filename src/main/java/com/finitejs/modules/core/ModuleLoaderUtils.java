package com.finitejs.modules.core;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for finite.js module loading operations.
 * This class is used by finite.js JavaScript module loader for loading
 * other modules.
 */
public class ModuleLoaderUtils {
	
	/**
	 * Constant for JavaScript extension.
	 * <p>
	 * Module files can be loaded without extension and this 
	 * will be the default extension.
	 * </p>
	 */
	public static final String JS_EXTENSION = ".js";
	
	/**
	 * Constant for module package file name.
	 * <p>
	 * Module main file information is present in this
	 * package file in the case of directory modules.
	 * </p>
	 */
	public static final String MODULE_PACKAGE_FILE_NAME = "package.json";
	
	/**
	 * Constant containing default module main file names.
	 * <p>
	 * If package file of a directory module does not contain 
	 * information regarding main file, then module loader will search for
	 * these default main files.
	 * </p>
	 */
	public static final String[] DEFAULT_PACKAGE_MAIN = {"index.js", "main.js"};
	
	/**
	 * Constant for default finite.js modules directory name.
	 */
	private static final String DEFAULT_MODULE_DIRNAME = "modules";
	
	/**
	 * Constant for finite.js core module name.
	 * <p>
	 * Core module is the JavaScript entry point of finite.js and
	 * this core module is also loaded by JavaScript module loader.
	 * </p>
	 */
	private static final String CORE_MODULE_NAME = "finite";
	
	/**
	 * Constant for finite.js JavaScript module loader name.
	 */
	private static final String LOADER_MODULE_NAME = "module";
	
	/**
	 * Constant for module execution template.
	 * <p>
	 * All module execution is made by wrapping around this template.
	 * </p>
	 */
	private static final String MODULE_TEMPLATE = String.format("%s%s", 
			"(function (exports, require, module, __filename, __dirname){",
			"%n%s%n}).call(this, module.exports, module.require, module, '%s', '%s');");
	
	/**
	 * Reads contents of a module script based on the 
	 * path returned by {@code resolveModuleFilePath} and
	 * wraps it around execution template.
	 * 
	 * @param modulePath  path of module
	 * @return module script for execution
	 * @throws ModuleLoadException if error while reading module
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
	
	/**
	 * Reads a JavaScript file. Used to read script file other 
	 * than modules. {@code JSEngine} uses this method to load module loader.
	 * 
	 * @param path  path of JavaScript file.
	 * @return script file contents
	 * @throws IOException
	 */
	public static String readScript(String path) throws IOException{
		String script = FileUtils.readTextFile(path);
		return script;
	}
	
	/**
	 * Resolves module id to a file path based on given rules.
	 * <p>
	 * <li>Check whether module id is an absolute file path, then return it as is.</li>
	 * <li>Check whether module id starts with {@code ./} or {@code ../} or a main file, 
	 * then these modules are loaded relative to parent file and main 
	 * file relative to working directory. Main file is the file supplied to
	 * finite.js as command line argument.</li>
	 * <li>Check whether if module exists in modules directory.</li>
	 * </p>
	 * 
	 * @param moduleId  id of module
	 * @param parentFilePath  module parent path
	 * @param isMain  indicates whether module is a main file
	 * @return absolute file/directory path of module or null if module not exists
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
		
		// if parent path is not available use current working directory as parent path
		// in shell mode parent path is not available, so all relative 
		// modules starting with ./ or ../ will be loaded with 
		// relative to working directory
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
		
		// check whether relative to parent of parent file path
		// or relative to parent file path
		if (moduleId.startsWith("../") || moduleId.startsWith("./")){
			
			File parentFile;
			
			// resolve all ../ and ./
			while(moduleId.startsWith("../") || moduleId.startsWith("./")){
				
				// get parent file
				parentFile = new File(parentFilePath);
				
				if (moduleId.startsWith("../")){
					// strip ../
					moduleId = moduleId.substring(3);
					
					if (parentFile.isDirectory()){
						// if parentFilePath points to a directory
						parentFilePath = parentFile.getParent();
					}else{
						// if parentFilePath points to a file
						parentFilePath = parentFile.getParentFile().getParent();
					}
					
				}else if (moduleId.startsWith("./")){
					// strip ./
					moduleId = moduleId.substring(2);
					
					if (parentFile.isDirectory()){
						// if parentFilePath points to a directory
						parentFilePath = parentFile.getAbsolutePath();
					}else{
						// if parentFilePath points to a file
						parentFilePath = parentFile.getParent();
					}
				}
			}
			
			// correct environment based file seperator
			moduleId = moduleId.replaceAll("/", File.separator);
			
			modulePath = checkModuleExists(parentFilePath, moduleId);
			
			// null if module not found
			return modulePath;
		}
		
		// check in modules dir
		String modulesDirPath = ConfigManager.getInstance().getModulesPath();
		
		modulePath = checkModuleExists(modulesDirPath, moduleId);
		
		return modulePath;
	}
	
	/**
	 * Used by {@code resolveModuleFilePath} for checking whether modules exists.
	 */
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
		if (modulePath != null){
			modulePath = modulePath.replace("\\", "\\\\");
		}
		
		// null if module not found
		return modulePath;
		
	}
	
	/**
	 * Read contents of module package file while loading directory modules.
	 * 
	 * @param modulePath  module directory path
	 * @return package file contents
	 * @throws IOException
	 */
	public static String readModulePackageFile(String modulePath) throws IOException{
		String packageFileContents = null;
		File packageFile = new File(modulePath, MODULE_PACKAGE_FILE_NAME);
		if (packageFile.exists()){
			packageFileContents = FileUtils.readTextFile(packageFile);
			// strip comments from package file
			packageFileContents = packageFileContents.replaceAll(
					"(?:\\/\\*(?:[^\\*]|(?:\\*+[^\\*\\/]))*\\*+\\/)|(?:\\/\\/.*)", "");
		}
		return packageFileContents;
	}
	
	/**
	 * Check for directory module main file. 
	 * <p>
	 * Main file is read from package file, if main file doesn't exists
	 * then checks for default main files.
	 * </p>
	 * 
	 * @param packageDir  module directory path 
	 * @param packageMainFile  module main file, can be null
	 * @return absolute path to directory module main file or null if not found
	 */
	public static String checkModulePackageMain(String packageDir, String packageMainFile){
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
		if (modulePath != null){
			modulePath = modulePath.replace("\\", "\\\\");
		}
		
		return modulePath;
	}
	
	/**
	 * Returns default finite.js modules directory name.
	 * 
	 * @return modules directory name
	 */
	public static String getDefaultModulesDir(){
		return DEFAULT_MODULE_DIRNAME;
	}
	
	/**
	 * Get finite.js JavaScript module loader path.
	 * 
	 * @return module loader path
	 */
	public static String getModuleLoaderPath(){
		String moduleLoaderPath = 
				resolveModuleFilePath(LOADER_MODULE_NAME, null, false);
		return moduleLoaderPath;
	}
	
	/**
	 * Get finite.js core module path.
	 * 
	 * @return core module path.
	 */
	public static String getCoreModulePath(){
		String coreModulePath = 
				resolveModuleFilePath(CORE_MODULE_NAME, null, false);
		return coreModulePath;
	}
	
}
