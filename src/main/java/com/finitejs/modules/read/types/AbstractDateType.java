package com.finitejs.modules.read.types;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.finitejs.modules.read.ColumnType;

/**
 * Abstract class that represents all date-time column types.
 *
 * @param <T>  any of the date-time type
 */
public abstract class AbstractDateType<T extends TemporalAccessor> implements ColumnType<T> {
	
	/**
	 * Default date formats supported by {@link DateType}.
	 */
	protected static final String[] DEFAULT_DATE_FORMATS = {
		"y-M-d",
		"y/M/d",
		"M/d/y",
		"d-M-y"
	};
	
	/**
	 * Default date-time formats supported by {@link ZonedDateTimeType}.
	 */
	protected static final String[] DEFAULT_ZONEDDATETIME_FORMATS = {
		"y-M-d'T'HH:mm:ssXXX",
		"y-M-d'T'HH:mm:ssZZZ",
		"EEE MMM d HH:mm:ss zzz y"
	};
	
	/**
	 * Default date-time formats supported by {@link DateTimeType}.
	 */
	protected static final String[] DEFAULT_DATETIME_FORMATS = {
		"y-M-d hh:mm:ss a",
		"y-M-d HH:mm:ss",
		"y-M-d hh:mm a",
		"y-M-d HH:mm",
		
		"y/M/d hh:mm:ss a",
		"y/M/d HH:mm:ss",
		"y/M/d hh:mm a",
		"y/M/d HH:mm",
		
		"M/d/y hh:mm:ss a",
		"M/d/y HH:mm:ss",
		"M/d/y hh:mm a",
		"M/d/y HH:mm",
		
		"d-M-y hh:mm:ss a",
		"d-M-y HH:mm:ss",
		"d-M-y hh:mm a",
		"d-M-y HH:mm",
		
		"d-MMM-y hh.mm.ss.SSS a",
		"d-MMM-y hh.mm.ss.SSSSSSSSS a"
	};
	
	/**
	 * Default time formats supported by {@link TimeType}.
	 */
	protected static final String[] DEFAULT_TIME_FORMATS = {
		"hh:mm:ss a",
		"HH:mm:ss",
		"hh:mm a",
		"HH:mm"
	};
	
	/** 
	 * {@code DateTimeFormatter} corresponding to the type.
	 * Each date-time type corresponds to a single format.
	 */
	protected DateTimeFormatter formatter;
	
	/** 
	 * String representation of the format used by date-time type.
	 */
	protected String format;
	
	@Override
	public String format(T value) {
		String stringValue = "";
		
		if (value != null && formatter != null){
			stringValue = formatter.format(value);
		}
		
		return stringValue;
	}
	
	/**
	 * Checks for equality of two date-time types and types are
	 * equal only if there formats are equal.
	 */
	@Override
	public boolean equals(Object object){
		if (object instanceof AbstractDateType){
			if (object.toString().equals(this.toString())){
				return true;
			}
		}
		return false;
	}
	
}
