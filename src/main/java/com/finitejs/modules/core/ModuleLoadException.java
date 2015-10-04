package com.finitejs.modules.core;

/**
 * Exception class for errors related to module loading.
 */
public class ModuleLoadException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public ModuleLoadException(){
		super();
	}
	
	public ModuleLoadException(String msg){
		super(msg);
	}

	public ModuleLoadException(String msg, Throwable cause){
		super(msg, cause);
	}
}
