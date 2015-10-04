package com.finitejs.modules.read.types;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ZonedDateTimeType extends AbstractDateType<ZonedDateTime>{
	
	private ZonedDateTimeType(){}
	
	@Override
	public ZonedDateTime parse(String stringValue) {
		ZonedDateTime value = null;
		
		if (stringValue != null && !stringValue.isEmpty() && formatter != null){
			
			// correct am/pm format
			stringValue = stringValue.replace("am", "AM");
			stringValue = stringValue.replace("pm", "PM");
			
			try{
				value = ZonedDateTime.parse(stringValue, formatter);
			}catch(DateTimeParseException e){
				// do nothing
				// returns null
			}
		}
		
		return value;
	}
	
	@Override
	public String toString(){
		return String.format("%s(%s)", ZONEDDATETIME_TYPE, format);
	}
	
	public static ZonedDateTimeType checkAndGetType(String stringValue){
		
		ZonedDateTimeType type = null;
		
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
		
		// zoned date time formats
		for (String format : DEFAULT_ZONEDDATETIME_FORMATS){
			dtf = DateTimeFormatter.ofPattern(format);
			try{
				ZonedDateTime.parse(stringValue, dtf);
				type = new ZonedDateTimeType();
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
	
	public static ZonedDateTimeType getType(String format){
		ZonedDateTimeType type = null;
		
		// empty check
		if (format == null || format.isEmpty()){
			return type;
		}
		
		try{
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
			type = new ZonedDateTimeType();
			type.formatter = dtf;
			type.format = format;
		}catch(IllegalArgumentException e){
			// do nothing
			// return null
		}
		
		return type;
	}

}
