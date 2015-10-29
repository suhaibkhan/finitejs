'use strict';

(function(exports){
	
	// Module class
	function Module(id, parent){
		this.id = id;
		this.parent = parent;
		this.exports = {};
		this.filename = null;
		this.info = null;
	}
	
	// modules will be cached under file name
	Module._modulesCache = {};
	
	//Java class with utility methods for loading modules.
	Module._nativeUtil = Java.type("com.finitejs.modules.core.ModuleLoaderUtils");
	
	// Console I/O utility
	Module._console = Java.type("com.finitejs.modules.core.ConsoleUtils");
	
	// loads a module
	Module._load = function(id, parent, isMain){
		
		isMain = isMain || false; // default false
		
		var parentFilePath = parent ? parent.filename : null;
		var module, compileSuccess, modulePackageContents, modulePackageInfo, modulePackageMain;
		var moduleFilePath = Module._nativeUtil.resolveModuleFilePath(id, parentFilePath, isMain);
		
		// resolve directory modules by reading package files
		if (moduleFilePath != null && !moduleFilePath.endsWith(Module._nativeUtil.JS_EXTENSION)){
			try{
				// read package.json inside directory
				modulePackageContents = Module._nativeUtil.readModulePackageFile(moduleFilePath);
			}catch(ioex){}
			
			if (modulePackageContents){
				modulePackageInfo = JSON.parse(modulePackageContents);
				modulePackageMain = modulePackageInfo.main || null;
			}
			
			// will return null if main not found
			moduleFilePath = Module._nativeUtil.checkModulePackageMain(
					moduleFilePath, modulePackageMain);
		}
		
		if (moduleFilePath == null){
			Module._console.errorf('Module %s not found. %n', id);
			if (!__shell){
				// Terminate program if a module not found.
				quit();
			}
			return null;
		}
		
		// check for module in cache
		module = Module._modulesCache[moduleFilePath];
		if (!module){
			
			// for relative modules starting with 
			// ../ and ./ module id will be there full file name.
			if (id.slice(0, 3) === '../' || 
					id.slice(0, 2) === './'){
				id = moduleFilePath;
			}
			
			module = new Module(id, parent);
			module.filename = moduleFilePath;
			module.info = modulePackageInfo || null;
			
			// compile module 
			compileSuccess = module._compile();
						
			if (compileSuccess){
				// save module to cache
				Module._modulesCache[moduleFilePath] = module;
			}else{
				// shell mode
				return null;
			}
		}
		
		return module.exports;
	};
	
	
	// execute module script and update module reference
	// with exports
	Module._execute = function(module, moduleScript){
		
		// hide module loader local/protected variables
		// from execution
		var Module;
		
		// global function required by modules
		var require = function(id, isMain){
			return module.require(id, isMain);
		};
		
		eval(moduleScript);
		
	};
	
	Module.prototype._compile = function(){
		
		var compileSuccess = true;
		var moduleFilePath = this.filename;
		var moduleScript;
		
		try{
			// read module script
			moduleScript = Module._nativeUtil.readModule(moduleFilePath); 
			
			// execute and update module reference with exports
			Module._execute(this, moduleScript);
			
		}catch(moduleException){
			compileSuccess = false;
			if (!__shell){
				Module._console.error(moduleException);
				// Terminate program if a module not found.
				quit();
			}else{
				throw moduleException;
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