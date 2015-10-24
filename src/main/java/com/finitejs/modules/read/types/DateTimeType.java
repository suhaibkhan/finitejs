package com.finitejs.modules.read.types;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class that represents date with time column types.
 */
public class DateTimeType extends AbstractDateType<LocalDateTime> {
	
	private DateTimeType(){}
	
	@Override
	public LocalDateTime parse(String stringValue) {
		LocalDateTime value = null;
		
		if (stringValue != null && !stringValue.isEmpty() && formatter != null){
			
			// correct am/pm format
			stringValue = stringValue.replace("am", "AM");
			stringValue = stringValue.replace("pm", "PM");
			
			try{
				value = LocalDateTime.parse(stringValue, formatter);
			}catch(DateTimeParseException e){
				// do nothing
				// returns null
			}
		}
		
		return value;
	}
	
	@Override
	public String toString(){
		return String.format("%s(%s)", DATETIME_TYPE, format);
	}
	
	@Override
	public int compareTo(LocalDateTime a, LocalDateTime b) {
		
		if (a == null && b != null){
			return -1;
		}
		
		if (a != null && b == null){
			return 1;
		}
		
		if (a == null && b == null){
			return 0;
		}
		
		return a.compareTo(b);
	}
	
	public static DateTimeType checkAndGetType(String stringValue){
		
		DateTimeType type = null;
		
		// check for empty
		if (stringValue == null || stringValue.isEmpty()){
			return type;
		}
		
		// check for any of the date separators in the string
		if ((stringValue.indexOf('/') == -1) && 
			(stringValue.indexOf('-') == -1) &&
			(stringValue.indexOf(':') == -1) &&
			(stringValue.indexOf('.') == -1)){
			return type;
		}
		
		// correct am/pm format
		stringValue = stringValue.replace("am", "AM");
		stringValue = stringValue.replace("pm", "PM");
		
		
		// check for supported formats
		DateTimeFormatter dtf;
		
		// date time formats
		for (String format : DEFAULT_DATETIME_FORMATS){
			dtf = DateTimeFormatter.ofPattern(format);
			try{
				LocalDateTime.parse(stringValue, dtf);
				type = new DateTimeType();
				type.formatter = dtf;
				type.format = format;
				
				// first matching format is selected
				break;
			}catch(DateTimeParseException e){
				// do nothing 
				// try another format
			}
		}
		
		return type;
	}

	public static DateTimeType getType(String format){
		DateTimeType type = null;
		
		// empty check
		if (format == null || format.isEmpty()){
			return type;
		}
		
		String tempFormat = format.replaceAll("'[^']*'", "");

		// check for time zone elements
		if (!tempFormat.matches("[^VzZXxO]*")){
			return type;
		}
		
		try{
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
			type = new DateTimeType();
			type.formatter = dtf;
			type.format = format;
		}catch(IllegalArgumentException e){
			// do nothing
			// return null
		}
		return type;
	}
	
}
