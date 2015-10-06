package com.finitejs.modules.read;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Class represents a column in {@link DataTable}.
 * 
 * @param <T>  type of the column
 */
public class Column<T> implements Iterable<T>{
	
	/** Column header name */
	private String name;
	
	/** Column type for type T*/
	private ColumnType<T> type;
	
	/** List with column values */
	private List<T> column;
	
	/**
	 * Creates a new {@code Column} instance. 
	 * Used by the factory method {@code create}.
	 * 
	 * @param name  column header name
	 * @param type  column type
	 */
	private Column(String name, ColumnType<T> type){
		column = new ArrayList<>();
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Adds a new value to the column of type T.
	 * 
	 * @param value  value to be added
	 */
	public void add(T value){
		column.add(value);
	}
	
	/**
	 * Parse a string value and adds to the column.
	 * 
	 * @param stringValue  string representation of the value to be added
	 */
	public void parseAndAdd(String stringValue){
		if (type != null){
			T value = type.parse(stringValue);
			column.add(value);
		}
	}
	
	/**
	 * Get formatted string representation of given column value.
	 * 
	 * @param value  value to be formatted
	 * @return formatted string representation
	 */
	public String formatValue(T value){
		if (type == null){
			return "";
		}
		return type.format(value);
	}
	
	/**
	 * Get header name of the column.
	 * 
	 * @return header name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Get type of the column.
	 * 
	 * @return type of column
	 */
	public ColumnType<T> getType(){
		return type;
	}
	
	/**
	 * Factory method for creating a new column.
	 * 
	 * @param name  column header name
	 * @param type  type of the column
	 * @return column instance, or null if type is empty
	 */
	public static <T> Column<T> create(String name, ColumnType<T> type){
		// type cannot be null
		if (type == null){
			return null;
		}
		return new Column<T>(name, type);
	}

	@Override
	public Iterator<T> iterator() {
		return column.iterator();
	}
	
	/**
	 * Returns a list iterator over column elements of type T.
	 * 
	 * @return a list iterator
	 */
	public ListIterator<T> listIterator(){
		return column.listIterator();
	}
	
	/**
	 * Returns a list iterator over column elements of type T, 
	 * starting at the specified position in the column list.
	 * 
	 * @param index  index of first element to be 
	 * returned by the iterator {@code next()} method.
	 * @return a list iterator starting at the specified index
	 */
	public ListIterator<T> listIterator(int index){
		return column.listIterator(index);
	}
	
	/**
	 * Returns a list iterator over formatted string representation 
	 * of column elements.
	 * 
	 * @return a list iterator for formatted elements
	 */
	public Iterator<String> formattedValueIterator(){
		return new FormattedColumnIterator<T>(this);
	}
	
	/**
	 * Returns a list iterator over formatted string representation 
	 * of column elements, starting at the specified position in the column list.
	 * 
	 * @param index  index of first element to be 
	 * returned by the iterator {@code next()} method.
	 * @return a list iterator for formatted elements starting at the specified index
	 */
	public Iterator<String> formattedValueIterator(int index){
		return new FormattedColumnIterator<T>(this, index);
	}
	
}
