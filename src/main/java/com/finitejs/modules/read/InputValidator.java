package com.finitejs.modules.read;

/**
 * {@code InputValidator} can be used to validate reader input before adding to {@link DataTable}.
 * If a column is invalid in a row, that whole row will be discarded.
 */
public interface InputValidator {
	
	/**
	 * Validates the input string before adding to a {@link DataTable}.
	 * If a column is invalid in a row, that whole row will be discarded.
	 * 
	 * @param inputValue  input string value
	 * @return true if valid input value, else false
	 */
	public boolean validate(String inputValue);
	
}
