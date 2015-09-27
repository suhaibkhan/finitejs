'use strict';

(function(exports){
		
	// modules will be cached under file name
	var _modulesCache = {};
	
	//Java class with utility methods for loading modules.
	var NativeModulesUtil = Java.type("com.finitejs.modules.core.ModuleLoaderUtils");
	// Console I/O utility
	var console = Java.type("com.finitejs.modules.core.ConsoleUtils");
	// JSEngine for executing JavaScript.
	var JSEngine = Java.type('com.finitejs.system.JSEngine');
	
	// Module class
	function Module(id, parent){
		this.id = id;
		this.parent = parent;
		this.exports = {};
		this.filename = null;
		this.context = null;
	}
	
	// loads a module
	Module._load = function(id, parent, isMain){
		
		isMain = isMain || false; // default false
		
		var parentFilePath = parent ? parent.filename : null;
		var module, compileSuccess, modulePackageContents, modulePackageInfo, modulePackageMain;
		var moduleFilePath = NativeModulesUtil.resolveModuleFilePath(id, parentFilePath, isMain);
		
		// resolve directory modules by reading package files
		if (moduleFilePath != null && !moduleFilePath.endsWith(NativeModulesUtil.JS_EXTENSION)){
			try{
				// read package.json inside directory
				modulePackageContents = NativeModulesUtil.readModulePackageFile(moduleFilePath);
			}catch(ioex){}
			
			if (modulePackageContents){
				modulePackageInfo = eval(modulePackageContents);
				modulePackageMain = modulePackageInfo.main || null;
			}
			
			// will return null if main not found
			moduleFilePath = NativeModulesUtil.checkModulePackageMain(
					moduleFilePath, modulePackageMain);
		}
		
		if (moduleFilePath == null){
			console.errorf('Module %s not found', id);
			if (!__shell){
				// Terminate program if a module not found.
				quit();
			}
			return null;
		}
		
		// check for module in cache
		module = _modulesCache[moduleFilePath];
		if (!module){
			module = new Module(id, parent);
			module.filename = moduleFilePath;
			
			// compile module 
			compileSuccess = module._compile();
						
			if (compileSuccess){
				// save module to cache
				_modulesCache[moduleFilePath] = module;
			}else{
				// shell mode
				return null;
			}
		}
		
		return module.exports;
	}
	
	Module.prototype._compile = function(){
		
		var compileSuccess = true;
		var moduleFilePath = this.filename;
		var sharedObjects = {};
		var moduleScript;
		
		try{
			// read module script
			moduleScript = NativeModulesUtil.readModule(moduleFilePath); 
		
			// module is bind to the new context
			sharedObjects.module = this; 
			this.context = JSEngine.getInstance().createNewContext(sharedObjects);
			
			// compile and execute in newly created context
			JSEngine.getInstance().evalInContext(moduleScript, this.context);
			
		}catch(moduleException){
			compileSuccess = false;
			console.error(moduleException);
			if (!__shell){
				// Terminate program if a module not found.
				quit();
			}
		}
		
		return compileSuccess;
	};
	
	Module.prototype.require = function(id, isMain){
		isMain = isMain || false; // default false
		// load new module with current module as parent
		return	Module._load(id, this, isMain);
	};
	
	// make load method available globally
	exports._require = function(id){
		Module._load(id, null, false)
	};
	
})(exports);