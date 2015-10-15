package com.finitejs.modules.read;

/**
 * InputFormatter can be used to format reader input before adding to {@link DataTable}. 
 */
public interface InputFormatter {

	/**
	 * Formats the input string before adding to a {@link DataTable}.
	 * 
	 * @param inputValue  input string value
	 * @return output string value after formatting
	 */
	public String format(String inputValue);
	
}
