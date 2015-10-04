package com.finitejs.modules.read.types;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateType extends AbstractDateType<LocalDate>{
	
	private DateType(){}
	
	@Override
	public LocalDate parse(String stringValue) {
		LocalDate value = null;
		
		if (stringValue != null && !stringValue.isEmpty() && formatter != null){
			
			try{
				value = LocalDate.parse(stringValue, formatter);
			}catch(DateTimeParseException e){
				// do nothing
				// returns null
			}
		}
		
		return value;
	}
	
	@Override
	public String toString(){
		return String.format("%s(%s)", DATE_TYPE, format);
	}
	
	public static DateType checkAndGetType(String stringValue){
		
		DateType type = null;
		
		// check for empty
		if (stringValue == null || stringValue.isEmpty()){
			return type;
		}
		
		// check for any of the date separators in the string
		if ((stringValue.indexOf('/') == -1) && 
			(stringValue.indexOf('-') == -1)){
			return type;
		}
		
		// check for supported formats
		DateTimeFormatter dtf;
		
		// zoned date time formats
		for (String format : DEFAULT_DATE_FORMATS){
			dtf = DateTimeFormatter.ofPattern(format);
			try{
				LocalDate.parse(stringValue, dtf);
				type = new DateType();
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
	
	public static DateType getType(String format){
		DateType type = null;
		
		// empty check
		if (format == null || format.isEmpty()){
			return type;
		}
		
		String tempFormat = format.replaceAll("'[^']*'", "");
		
		// check for time elements
		if (!tempFormat.matches("[^aAhHKkmsSnN]*")){
			return type;
		}
		// check for time zone elements
		if (!tempFormat.matches("[^VzZXxO]*")){
			return type;
		}
		
		try{
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
			type = new DateType();
			type.formatter = dtf;
			type.format = format;
		}catch(IllegalArgumentException e){
			// do nothing
			// return null
		}
		
		return type;
	}

}
