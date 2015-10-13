package com.finitejs.modules.read.types;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeType extends AbstractDateType<LocalTime>{
	
	private TimeType(){}
	
	@Override
	public LocalTime parse(String stringValue) {
		LocalTime value = null;
		
		if (stringValue != null && !stringValue.isEmpty() && formatter != null){
			
			// correct am/pm format
			stringValue = stringValue.replace("am", "AM");
			stringValue = stringValue.replace("pm", "PM");
			
			try{
				value = LocalTime.parse(stringValue, formatter);
			}catch(DateTimeParseException e){
				// do nothing
				// returns null
			}
		}
		
		return value;
	}
	
	@Override
	public String toString(){
		return String.format("%s(%s)", TIME_TYPE, format);
	}
	
	@Override
	public int compareTo(LocalTime a, LocalTime b) {
		
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
	
	public static TimeType checkAndGetType(String stringValue){
		
		TimeType type = null;
		
		// check for empty
		if (stringValue == null || stringValue.isEmpty()){
			return type;
		}
		
		// check for any of the time separators in the string
		if ((stringValue.indexOf(':') == -1) &&
			(stringValue.indexOf('.') == -1)){
			return type;
		}
		
		// correct am/pm format
		stringValue = stringValue.replace("am", "AM");
		stringValue = stringValue.replace("pm", "PM");
		
		// check for supported formats
		DateTimeFormatter dtf;
		
		// zoned date time formats
		for (String format : DEFAULT_TIME_FORMATS){
			dtf = DateTimeFormatter.ofPattern(format);
			try{
				LocalTime.parse(stringValue, dtf);
				type = new TimeType();
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
	
	public static TimeType getType(String format){
		TimeType type = null;
		
		// empty check
		if (format == null || format.isEmpty()){
			return type;
		}
		
		String tempFormat = format.replaceAll("'[^']*'", "");
		// check for date elements
		if (!tempFormat.matches("[^GuyYDdMLQqWwEecF]*")){
			return type;
		}
		// check for time zone elements
		if (!tempFormat.matches("[^VzZXxO]*")){
			return type;
		}
		
		try{
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
			type = new TimeType();
			type.formatter = dtf;
			type.format = format;
		}catch(IllegalArgumentException e){
			// do nothing
			// return null
		}
		
		return type;
	}

}
