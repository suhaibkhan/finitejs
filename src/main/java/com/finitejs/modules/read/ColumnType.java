package com.finitejs.modules.read;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.finitejs.modules.read.types.BooleanType;
import com.finitejs.modules.read.types.DateTimeType;
import com.finitejs.modules.read.types.DateType;
import com.finitejs.modules.read.types.NumberType;
import com.finitejs.modules.read.types.StringType;
import com.finitejs.modules.read.types.TimeType;
import com.finitejs.modules.read.types.ZonedDateTimeType;

public interface ColumnType<T> {
	
	/**
	 * Constant for {@link NumberType} string representation.
	 */
	public static final String NUMBER_TYPE = "number";
	
	/**
	 * Constant for {@link BooleanType} string representation.
	 */
	public static final String BOOLEAN_TYPE = "boolean";
	
	/**
	 * Constant for {@link DateType} string representation.
	 */
	public static final String DATE_TYPE = "date";
	
	/**
	 * Constant for {@link ZonedDateTimeType} string representation.
	 */
	public static final String ZONEDDATETIME_TYPE = "zoneddatetime";
	
	/**
	 * Constant for {@link DateTimeType} string representation.
	 */
	public static final String DATETIME_TYPE = "datetime";
	
	/**
	 * Constant for {@link TimeType} string representation.
	 */
	public static final String TIME_TYPE = "time";
	
	/**
	 * Constant for {@link StringType} string representation.
	 */
	public static final String STRING_TYPE = "string";
	
	/**
	 * Parses a string value compatible of the column type 
	 * and returns a value of type T.
	 * 
	 * @param stringValue  string value to parse
	 * @return value of type T, or null if error in parsing with the column type
	 */
	public T parse(String stringValue);
	
	/**
	 * Reverse process of {@code parse}. Returns string representation 
	 * of the given value of type T.
	 * 
	 * @param value  value of type T
	 * @return string representation of given value
	 */
	public String format(T value);
	
	/**
	 * Get string representation of the column type.
	 * 
	 * @return column type string representation
	 */
	public String toString();
	
	
	//=================================================//
	//  Static methods inside interface are supported  //
	//  only from Java 8 onwards.                      //
	//=================================================//
	
	/**
	 * Get column type corresponding to a string value.
	 * <p>
	 * Checks each type of compatibility and return the first 
	 * matching type in the following order.
	 * <li>{@link NumberType}</li>
	 * <li>{@link BooleanType}</li>
	 * <li>{@link DateType}</li>
	 * <li>{@link TimeType}</li>
	 * <li>{@link DateTimeType}</li>
	 * <li>{@link ZonedDateTimeType}</li>
	 * <li>{@link DateType}</li>
	 * <li>{@link StringType}</li>
	 * </p>
	 * 
	 * @param stringValue  string value
	 * @return column type, or null type cannot be determined
	 */
	public static ColumnType<?> findType(String stringValue){
		ColumnType<?> type = null;
		
		// check for empty value
		if (stringValue == null || stringValue.isEmpty()){
			// not possible to determine type
			return type;
		}
		
		// first check for number
		type = NumberType.checkAndGetType(stringValue);
		if (type != null){
			return type;
		}
		
		// check for boolean
		type = BooleanType.checkAndGetType(stringValue);
		if (type != null){
			return type;
		}
		
		// check for date
		type = DateType.checkAndGetType(stringValue);
		if (type != null){
			return type;
		}
		
		// check for time
		type = TimeType.checkAndGetType(stringValue);
		if (type != null){
			return type;
		}
		
		// check for date time
		type = DateTimeType.checkAndGetType(stringValue);
		if (type != null){
			return type;
		}
		
		// check for zoned date time
		type = ZonedDateTimeType.checkAndGetType(stringValue);
		if (type != null){
			return type;
		}
		
		// default is string type
		type = StringType.getType();
		
		return type;
	}
	
	/**
	 * Resolves conflict between two types. If conflict arises between 
	 * two non-compatible types, then value will be stored as StringType.
	 * 
	 * @param typeA  first type
	 * @param typeB  second type
	 * @return new type that resolves conflict
	 */
	public static ColumnType<?> getPreferredType(ColumnType<?> typeA, ColumnType<?> typeB){
		// if conflict arises between two types
		// value will be stored as StringType
		return StringType.getType();
	}
	
	/**
	 * Return a column type from string representation of the type.
	 * Some types needs type specific arguments, like boolean type
	 * category can be specified in the argument and a format argument is 
	 * mandatory for all date-time types.
	 * <p>
	 * Examples for type specifiers :
	 * <pre>
	 * number
	 * boolean(true_false)
	 * boolean(yes_no)
	 * date(y/M/d)
	 * zoneddatetime(y-M-d'T'HH:mm:ssXXX)
	 * datetime(M/d/y HH:mm)
	 * time(hh:mm:ss a)
	 * string
	 * </pre>
	 * For supported date-time formats see {@link java.time.format.DateTimeFormatter}.
	 * </p>
	 * 
	 * @param typeString  type specifier string
	 * @return column type
	 */
	public static ColumnType<?> getType(String typeString){
		ColumnType<?> type = null;
		
		// check empty argument
		if (typeString == null || typeString.isEmpty()){
			return type;
		}
		
		String typeName = null;
		String[] typeArgs = null;
		// find type name and arguments if any
		Matcher typeStringMatcher = 
				Pattern.compile("^\\s*(\\w+)\\s*(?:\\(\\s*([^()]*)\\s*\\)\\s*)?$").matcher(typeString);
		if (typeStringMatcher.matches()){
			typeName = typeStringMatcher.group(1);
			if (typeStringMatcher.group(2) != null){
				typeArgs = typeStringMatcher.group(2).trim().split("\\s*,\\s*");
			}
		}
		
		// check for empty type name
		if (typeName == null || typeName.isEmpty()){
			return type;
		}
		
		if (BOOLEAN_TYPE.equalsIgnoreCase(typeName)){
			
			// check for category
			if (typeArgs != null && typeArgs.length != 0){
				type = BooleanType.getType(typeArgs[0]);
			}else{
				type = BooleanType.getType(null);
			}
			
		}else if (NUMBER_TYPE.equalsIgnoreCase(typeName)){
			
			type = NumberType.getType();
			
		}else if (DATE_TYPE.equalsIgnoreCase(typeName)){
			
			// check for format
			if (typeArgs != null && typeArgs.length != 0){
				type = DateType.getType(typeArgs[0]);
			}
			
		}else if (TIME_TYPE.equalsIgnoreCase(typeName)){
			
			// check for format
			if (typeArgs != null && typeArgs.length != 0){
				type = TimeType.getType(typeArgs[0]);
			}
			
		}else if (DATETIME_TYPE.equalsIgnoreCase(typeName)){
			
			// check for format
			if (typeArgs != null && typeArgs.length != 0){
				type = DateTimeType.getType(typeArgs[0]);
			}
			
		}else if (ZONEDDATETIME_TYPE.equalsIgnoreCase(typeName)){
			
			// check for format
			if (typeArgs != null && typeArgs.length != 0){
				type = ZonedDateTimeType.getType(typeArgs[0]);
			}
			
		}else if (STRING_TYPE.equalsIgnoreCase(typeName)){
			
			type = StringType.getType();
		}
		
		return type;
	}
	

}
