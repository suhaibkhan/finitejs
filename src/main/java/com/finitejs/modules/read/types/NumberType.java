package com.finitejs.modules.read.types;

import com.finitejs.modules.read.ColumnType;

/**
 * Class that represents numerical column types.
 */
public class NumberType implements ColumnType<Double>{
	
	private NumberType(){}
	
	@Override
	public Double parse(String stringValue) {
		Double value = null;
		
		if (stringValue != null && !stringValue.isEmpty()){
			try{
				value = Double.parseDouble(stringValue);
			}catch(NumberFormatException e){
				// do nothing
				// returns DEFAULT_VALUE
			}
		}
		
		return value;
	}

	@Override
	public String format(Double value) {
		String stringValue = "";
		if (value != null){
			if (value == value.longValue()){
				stringValue = String.valueOf(value.longValue());
			}else{
				stringValue = String.valueOf(value);
			}
		}
		return stringValue;
	}
	
	@Override
	public String toString(){
		return NUMBER_TYPE;
	}
	
	@Override
	public boolean equals(Object object){
		if (object instanceof NumberType){
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(Double a, Double b) {
		
		if (a == null && b != null){
			return -1;
		}
		
		if (a != null && b == null){
			return 1;
		}
		
		if (a == null && b == null){
			return 0;
		}
		
		return Double.compare(a, b);
	}
	
	public static NumberType checkAndGetType(String stringValue){
		NumberType type = null;
		if (stringValue != null && !stringValue.isEmpty()){
			try{
				Double.parseDouble(stringValue);
				type = new NumberType();
			}catch(NumberFormatException e){
				// do nothing
				// returns null
			}
		}
		return type;
	}
	
	public static NumberType getType(){
		return new NumberType();
	}

}
