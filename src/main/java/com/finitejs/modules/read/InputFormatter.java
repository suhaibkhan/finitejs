package com.finitejs.modules.read;

/**
 * InputFormatter can be used to format reader input before adding to DataTable. 
 * 
 * @author Suhaib Khan
 */
public interface InputFormatter {

	String format(String inputValue);
	
}
