package com.finitejs.modules.read;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Iterator for formatted string representation of table rows.
 * 
 * @param <T>  Type of column
 */
public class FormattedDataTableIterator implements Iterator<List<String>>{
	
	/** Current iterator position */
	private int position;
	
	/** Iterator row count limit */
	private int limit;
	
	/** List of column iterators */
	private List<Iterator<String>> formattedColumnIterators;
	
	public FormattedDataTableIterator(DataTable table){
		this(table, 0);
	}
	
	public FormattedDataTableIterator(DataTable table, int startIndex){
		this(table, startIndex, table.getRowCount());
	}
	
	public FormattedDataTableIterator(DataTable table, int startIndex, int limit){
		position = startIndex;
		this.limit = limit < table.getRowCount() ? limit : table.getRowCount();
		formattedColumnIterators = new ArrayList<>();
		
		List<Column<?>> columnList = table.getColumnList();
		for (Column<?> column : columnList){
			formattedColumnIterators.add(column.formattedValueIterator(startIndex));
		}
	}
	
	@Override
	public boolean hasNext() {
		if (position < limit){
			return true;
		}
		return false;
	}

	@Override
	public List<String> next() {
		List<String> rowData = new ArrayList<>();
		for (Iterator<String> columnItr : formattedColumnIterators){
			rowData.add(columnItr.next());
		}
		position++;
		return rowData;
	}

}
