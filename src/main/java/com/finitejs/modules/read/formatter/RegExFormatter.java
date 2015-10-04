package com.finitejs.modules.read.formatter;

import java.util.regex.Pattern;

import com.finitejs.modules.read.InputFormatter;

public class RegExFormatter implements InputFormatter{

	private Pattern pattern;
	private String replacement;
	
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
