package com.finitejs.system;

import com.finitejs.modules.core.ConsoleUtils;

/**
 * Entry point for finite.js.
 */
public class FiniteJS {
	
	/**
	 * Constant for application name.
	 */
	public static final String APP_NAME = "finite.js";
	
	/**
	 * Constant for minimum java version required to run finite.js.
	 */
	public static final double MIN_JAVA_VERSION = 1.8;
	
	/**
	 * Debug mode flag.
	 * <p>
	 * Debug mode can also be turned on via command line argument {@code -Debug}.
	 * This flag is also available in JavaScript as a global variable 
	 * in the name defined by the constant {@link JSEngine.DEBUG_VAR_NAME}.
	 * </p>
	 */
	public static boolean DEBUG = false;
	
	public static void main(String[] args){

		// check java version
		try{
			Double javaVersion = Double.parseDouble(System.getProperty("java.specification.version"));
			if (javaVersion < MIN_JAVA_VERSION){
				// Old version detected
				ConsoleUtils.errorf("%s requires Java version %s or later.%n", 
						APP_NAME, MIN_JAVA_VERSION);
				System.exit(1);
			}
		}catch(NumberFormatException e){
			// not able to check version
			// exit application
			ConsoleUtils.errorln("Failed in checking Java version");
			System.exit(1);
		}
		
		// process cmd line args
		CmdLineArgProcessor.process(args);
		
		// start finite js
		JSEngine.getInstance().startFiniteJS();
	}
	
}
