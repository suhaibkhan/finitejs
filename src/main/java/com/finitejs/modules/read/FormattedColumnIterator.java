package com.finitejs.modules.read;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * Iterator for formatted string representation of column values.
 * 
 * @param <T>  Type of column
 */
public class FormattedColumnIterator<T> implements Iterator<String>{

	private Column<T> column;
	private ListIterator<T> columnIterator;
	
	public FormattedColumnIterator(Column<T> column){
		this(column, 0);
	}
	
	public FormattedColumnIterator(Column<T> column, int startIndex){
		this.column = column;
		this.columnIterator = column.listIterator(startIndex);
	}
	
	@Override
	public boolean hasNext() {
		return columnIterator.hasNext();
	}

	@Override
	public String next() {
		return column.getType().format(columnIterator.next());
	}

}
