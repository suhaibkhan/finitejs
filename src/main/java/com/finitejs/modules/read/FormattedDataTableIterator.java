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
	
	/** Iterator for index order list of {@code DataTable}*/
	private Iterator<Integer> tableIndexOrderIterator;
	
	/** List of columns */
	private List<Column<?>> columnList;
	
	public FormattedDataTableIterator(DataTable table){
		this(table, 0);
	}
	
	public FormattedDataTableIterator(DataTable table, int startIndex){
		this(table, startIndex, table.getRowCount() - startIndex);
	}
	
	public FormattedDataTableIterator(DataTable table, int startIndex, int limit){
		
		// check whether startIndex is available
		if (startIndex >= table.getRowCount()){
			throw new ArrayIndexOutOfBoundsException(startIndex);
		}
		
		position = startIndex;
		limit += startIndex;
		this.limit = limit < table.getRowCount() ? limit : table.getRowCount();
		
		// get index order iterator
		tableIndexOrderIterator = table.getIndexOrderList().listIterator(startIndex);
		
		// get column list
		columnList = table.getColumnList();
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
		
		int nextRowIndex = tableIndexOrderIterator.next();
		
		List<String> rowData = new ArrayList<>();
		for (Column<?> column : columnList){
			// get from column is an operation with complexity O(1)
			// as ArrayList is used inside column.
			rowData.add(column.getFormattedValue(nextRowIndex));
		}
				
		position++;
		return rowData;
	}

}
