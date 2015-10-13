package com.finitejs.modules.read;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	
	/** Index map of {@code DataTable} */
	private Map<String, List<Integer>> tableIndexMap;
	
	/** Index iterator */
	private Iterator<String> indexIterator;
	
	/** Iterator for list of row indices inside index map */
	private Iterator<Integer> rowIndexIterator;
	
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
		
		// get index
		tableIndexMap = table.getIndexMap();
		indexIterator = tableIndexMap.keySet().iterator();
		
		// get column list
		columnList = table.getColumnList();
		
		// skip indexIterator until startIndex 
		// O(startIndex) operation
		for (int i = 0; i < startIndex; i++){
			if (rowIndexIterator == null || !rowIndexIterator.hasNext()){
				rowIndexIterator = tableIndexMap.get(indexIterator.next()).iterator();
			}
			rowIndexIterator.next();
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
		
		if (rowIndexIterator == null || !rowIndexIterator.hasNext()){
			rowIndexIterator = tableIndexMap.get(indexIterator.next()).iterator();
		}
		
		int nextRowIndex = rowIndexIterator.next();
				
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
