package com.finitejs.modules.read.formatter;

import java.util.regex.Pattern;

import com.finitejs.modules.read.InputFormatter;

/**
 * 
 * Input formatter that formats and returns values based on regular expressions.
 * <p>
 * Example :
 * <pre>
 * InputFormatter formatter = 
 * 		new RegExFormatter("^(\\d{4})-(\\d{1,2})-(\\d{1,2})$", "$3-$2-$1 00:00:00");
 * </pre>
 * Above {@code formatter} can be used to convert value {@code 2014-04-14} to {@code 14-04-2014 00:00:00}.
 * </p>
 * 
 */
public class RegExFormatter implements InputFormatter{

	/** Compiled RegEx pattern used to format. */
	private Pattern pattern;
	
	/** Replacement string */
	private String replacement;
	
	/**
	 * Creates a new {@code RegExFormatter} based on 
	 * a RegEx pattern and replacement.
	 * 
	 * @param regex  regular expression to use
	 * @param replacement  replacement string
	 */
	public RegExFormatter(String regex, String replacement){
		this.pattern = Pattern.compile(regex);
		this.replacement = replacement;
	}
	
	@Override
	public String format(String inputValue) {
		String formattedValue = null;
		if (inputValue != null && pattern != null){
			formattedValue = pattern.matcher(inputValue).replaceAll(replacement);
		}
		return formattedValue;
	}

}
