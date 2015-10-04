package com.finitejs.modules.read.types;

import com.finitejs.modules.read.ColumnType;

/**
 * Class that represents boolean column types.
 */
public class BooleanType implements ColumnType<Boolean>{

	/**
	 * Enumeration representing {@code BooleanType} categories.
	 */
	private enum Category {
		TRUE_FALSE, YES_NO
	}
	
	/** Constant for string true value. */
	private static final String TRUE = "true";
	
	/** Constant for string false value. */
	private static final String FALSE = "false";
	
	/** Constant for yes value equivalent to boolean true. */
	private static final String YES = "yes";
	
	/** Constant for no value equivalent to boolean false. */
	private static final String NO = "no";
	
	/** Category of boolean type */
	private Category category;
	
	private BooleanType(){
		// default category
		category = Category.TRUE_FALSE;
	}
	
	/**
	 * Parses a compatible string value and returns a {@code Boolean} value.
	 * 
	 * @return {@code Boolean} value, or null if error in parsing
	 */
	@Override
	public Boolean parse(String stringValue) {
		
		Boolean value = null;
		
		if (stringValue != null && !stringValue.isEmpty()){
			if (category == Category.YES_NO){
				value = stringValue.equalsIgnoreCase(YES) ? true : false;
			}else if (category == Category.TRUE_FALSE){
				value = stringValue.equalsIgnoreCase(TRUE) ? true : false;
			}
		}
		return value;
	}
	
	/**
	 * Reverse process of {@code parse}. Returns string representation 
	 * of the given {@code Boolean} value.
	 * 
	 * @param value  {@code Boolean} value
	 */
	@Override
	public String format(Boolean value) {
		
		String stringValue = "";
		if (value != null){
			if (category == Category.TRUE_FALSE){
				stringValue = value ? TRUE : FALSE;
			}else if (category == Category.YES_NO){
				stringValue = value ? YES : NO;
			}
		}
		return stringValue;
	}
	
	/**
	 * Checks for equality of two boolean types and types are
	 * equal only if there categories are equal.
	 */
	@Override
	public boolean equals(Object object){
		if (object instanceof BooleanType){
			BooleanType type = (BooleanType) object;
			if (category == type.category){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		return String.format("%s(%s)", BOOLEAN_TYPE, category.toString());
	}
	
	/**
	 * Checks whether given string is a compatible {@code BooleanType}.
	 * 
	 * @param stringValue  string value to check
	 * @return {@code BooleanType} instance if compatible, or null if not.
	 */
	public static BooleanType checkAndGetType(String stringValue){
		
		BooleanType type = null;
		
		if (stringValue != null && !stringValue.isEmpty()){
			if (stringValue.equalsIgnoreCase(TRUE) || 
				stringValue.equalsIgnoreCase(FALSE)){
				type = new BooleanType();
				type.category = Category.TRUE_FALSE;
			}else if(stringValue.equalsIgnoreCase(YES) || 
				stringValue.equalsIgnoreCase(NO)){
				type = new BooleanType();
				type.category = Category.YES_NO;
			}
		}
		
		return type;
	}
	
	/**
	 * Factory method to create an instance of {@code BooleanType} 
	 * based on given category.
	 * 
	 * @param category  string representation of {@code BooleanType} 
	 * category, or null for default category
	 * @return {@code BooleanType} instance, or null if invalid category 
	 */
	public static BooleanType getType(String category){
		BooleanType type = null;
		// default category
		if (category == null || category.isEmpty()){
			category = Category.TRUE_FALSE.toString();
		}
		try{
			Category c = Category.valueOf(category.toUpperCase());
			type = new BooleanType();
			type.category = c;
		}catch(IllegalArgumentException e){
			// do nothing
			// return null
		}
		return type;
	}

}
