package com.finitejs.modules.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.finitejs.system.FiniteJS;

/**
 * Utility class for console I/O.
 */
public class ConsoleUtils {

	/**
	 * Obtains console input reader.
	 * Used in REPL mode.
	 * 
	 * @return input reader
	 */
	public static BufferedReader getInputReader(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader;
	}
	
	/**
	 * Prints string representation of an object to console.
	 * 
	 * @param object  object to print
	 */
	public static void print(Object object){
		System.out.print(object);
	}
	
	/**
	 * Prints string representation of an object to 
	 * console and terminates with a line break.
	 * 
	 * @param object  object to print
	 */
	public static void println(Object object){
		System.out.println(object);
	}
	
	/**
	 * Prints a formatted string to console using 
	 * the given format and arguments.
	 * 
	 * @param format  format specifier
	 * @param args  arguments in format
	 * 
	 * @see String.format
	 */
	public static void printf(String format, Object... args){
		String str = String.format(format, args);
		System.out.print(str);
	}
	
	/**
	 * Prints string representation of an object to error console/stream.
	 * 
	 * @param object  object to print
	 */
	public static void error(Object error){
		System.err.print(error);
	}
	
	/**
	 * Prints string representation of an object to 
	 * error console/stream and terminates with a line break.
	 * 
	 * @param object  object to print
	 */
	public static void errorln(Object error){
		System.err.println(error);
	}
	
	/**
	 * Prints a formatted string to error console/stream using 
	 * the given format and arguments.
	 * 
	 * @param format  format specifier
	 * @param args  arguments in format
	 * 
	 * @see String.format
	 */
	public static void errorf(String format, Object... args){
		String error = String.format(format, args);
		System.err.print(error);
	}
	
	/**
	 * Prints application information on console.
	 */
	public static void printAppInfo(){
		String appInfoStr = String.format("%s %s%n", FiniteJS.APP_NAME, FiniteJS.APP_VERSION);
		System.out.println(appInfoStr);
	}
	
}
