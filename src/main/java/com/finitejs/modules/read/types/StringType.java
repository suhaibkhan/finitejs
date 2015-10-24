package com.finitejs.modules.read.types;

import com.finitejs.modules.read.ColumnType;

/**
 * Class that represents string column types.
 */
public class StringType implements ColumnType<String>{
	
	private StringType(){}
	
	@Override
	public String parse(String stringValue) {
		String value = null;
		if (stringValue != null){
			value = stringValue;
		}
		return value;
	}

	@Override
	public String format(String value) {
		return value == null ? "" : value;
	}
	
	@Override
	public String toString(){
		return STRING_TYPE;
	}
	
	@Override
	public boolean equals(Object object){
		if (object instanceof StringType){
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(String a, String b) {
		
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
	
	public static StringType getType(){
		return new StringType();
	}

}
