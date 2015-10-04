package com.finitejs.modules.read;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DataTableIterator implements Iterator<List<?>>{
	
	private int limit;
	
	private int position;
	
	private List<ListIterator<?>> columnIterators;
	
	public DataTableIterator(DataTable table){
		this(table, 0);
	}
	
	public DataTableIterator(DataTable table, int startIndex){
		this(table, 0, table.getRowCount());
	}
	
	public DataTableIterator(DataTable table, int startIndex, int limit){
		position = startIndex;
		this.limit = limit < table.getRowCount() ? limit : table.getRowCount();
		columnIterators = new ArrayList<>();
		
		// get iterators of each column
		List<Column<?>> columnList = table.getColumnList();
		for (Column<?> column : columnList){
			columnIterators.add(column.listIterator(startIndex));
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
	public List<?> next() {
		List<Object> rowData = new ArrayList<>();
		for (Iterator<?> columnItr : columnIterators){
			rowData.add(columnItr.next());
		}
		position++;
		return rowData;
	}

}
