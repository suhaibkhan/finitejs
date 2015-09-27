package com.finitejs.modules.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Utility class for console I/O.
 * @author Suhaib Khan
 *
 */
public class ConsoleUtils {

	public static BufferedReader getInputReader(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader;
	}
	
	public static void print(Object object){
		System.out.print(object);
	}
	
	public static void println(Object object){
		System.out.println(object);
	}
	
	public static void printf(String format, Object... args){
		String str = String.format(format, args);
		System.out.print(str);
	}
	
	public static void error(Object error){
		System.err.print(error);
	}
	
	public static void errorln(Object error){
		System.err.println(error);
	}
	
	public static void errorf(String format, Object... args){
		String error = String.format(format, args);
		System.err.print(error);
	}
	
}
