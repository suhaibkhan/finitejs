package com.finitejs.system;

/**
 * Entry point for finitejs.
 * 
 * @author Suhaib Khan
 */
public class FiniteJS {
	
	// debug mode disabled by default and 
	// can be enabled using cmdline args
	public static boolean DEBUG = false;
	public static String APP_NAME = "finite.js";
	
	public static void main(String[] args){

		// process cmd line args
		CmdLineArgProcessor.process(args);
		
		// start finite js
		JSEngine.getInstance().startFiniteJS();
	}
	
}
