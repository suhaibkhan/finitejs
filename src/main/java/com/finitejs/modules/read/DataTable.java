package com.finitejs.modules.read;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.finitejs.modules.read.util.DataTableFormatter;

/**
 * Class used to represent tabular data.
 * Each column in tabular data is an instance of {@link Column} class.
 * Values are stored in {@link Column} instances and are of different type. 
 */
public class DataTable implements Iterable<List<?>>{
	/**
	 * Constant for default row count while printing in tabular format.
	 */
	public static final int DEFAULT_PRINT_LIMIT = 30;
	
	/** List to store columns */
	private List<Column<?>> table;
	
	/** Number of rows */
	private int rowCount;
		
	private DataTable(){
		rowCount = 0;
		table = new ArrayList<>();
	}
	
	/**
	 * Returns number of rows in the table.
	 * 
	 * @return number of rows
	 */
	public int getRowCount(){
		return rowCount;
	}
	
	/**
	 * Returns number of columns in the table.
	 * 
	 * @return number of columns
	 */
	public int getColumnCount(){
		return table.size();
	}
	
	/**
	 * Get table as list of {@link Column} objects. 
	 * Each column can be of different type and contains all values in that 
	 * column as a list. {@link Column} contains a {@link ColumnType}, which can 
	 * be used to used to parse and format values supported by that column.
	 * 
	 * @return list of columns
	 */
	public List<Column<?>> getColumnList(){
		return table;
	}
	
	/**
	 * Get list of column headers in table.
	 * 
	 * @return list of headers
	 */
	public List<String> getHeaderList(){
		List<String> headerList = new ArrayList<>();
		for (Column<?> column : table){
			headerList.add(column.getName());
		}
		return headerList;
	}
	
	/**
	 * Get type information of each column as {@link ColumnType} list.
	 * 
	 * @return list of types
	 */
	public List<ColumnType<?>> getTypeList(){
		List<ColumnType<?>> typeList = new ArrayList<>();
		for (Column<?> column : table){
			typeList.add(column.getType());
		}
		return typeList;
	}
	
	/**
	 * Returns a string that represents {@code DataTable} type information.
	 * 
	 * @return string representing types of table values
	 */
	public String getType(){
		StringBuffer typeStrBuffer = new StringBuffer();
		
		Iterator<Column<?>> tableItr = table.iterator();
		while(tableItr.hasNext()){
			Column<?> column = tableItr.next();
			typeStrBuffer.append(column.getName().toString());
			typeStrBuffer.append(":");
			typeStrBuffer.append(column.getType().toString());
			if (tableItr.hasNext()){
				typeStrBuffer.append(", ");
			}
		}
		return typeStrBuffer.toString();
	}
	
	/**
	 * Adds list of string values as a new row of table.
	 * 
	 * @param rowData  string representations of values in new row
	 */
	public void addRow(List<String> rowData){
		
		if (rowData == null){
			return;
		}
		
		Iterator<Column<?>> tableItr = table.iterator();
		Iterator<String> rowItr = rowData.iterator();
		
		while(tableItr.hasNext()){
			Column<?> column = tableItr.next();
			if (rowItr.hasNext()){
				column.parseAndAdd(rowItr.next());
			}else{
				// fill with null
				column.parseAndAdd(null);
			}
		}
		
		// increase row count
		rowCount++;
	}
	
	/**
	 * Adds a new column to the table.
	 * 
	 * @param header  column header name, if null column index is used as header
	 * @param type  type information, if null column will not be created
	 * @param columnData   string representations of data to be added to 
	 * the column, if null columns with empty values will be created
	 */
	public void addColumn(String header, ColumnType<?> type, List<String> columnData){
		
		if (header == null){
			// index as header
			header = String.valueOf(getColumnCount());
		}
		
		Column<?> column = Column.create(header, type);
		
		if (column == null){
			return;
		}
		
		Iterator<String> columnItr = null;
		if (columnData != null){
			columnItr = columnData.iterator();
		}
		
		for (int i = 0; i < rowCount; i++){
			if (columnItr != null && columnItr.hasNext()){
				column.parseAndAdd(columnItr.next());
			}else{
				// fill with null
				column.parseAndAdd(null);
			}
		}
		
		// add new column to table
		table.add(column);
	}
	
	/**
	 * Adds a new column to the table.
	 * 
	 * @param header  column header name, if null column index is used as header
	 * @param typeString  string representation of type, if null column will not be created
	 * @param columnData   string representations of data to be added to 
	 * the column, if null columns with empty values will be created
	 */
	public void addColumn(String header, String typeString, List<String> columnData){
		ColumnType<?> type = ColumnType.getType(typeString);
		addColumn(header, type, columnData);
	}
	
	/**
	 * Creates a new {@code DataTable} instance with given header list, 
	 * type list and data as list of list.
	 * 
	 * @param headerList  column header names
	 * @param typeList  column types
	 * @param data  tabular data as list of columns and each 
	 * column as list of string values.
	 * @return {@code DataTable} instance
	 */
	public static DataTable getTable(
			List<String> headerList, List<ColumnType<?>> typeList, 
			List<List<String>> data){
		
		DataTable table = new DataTable();
		
		// create columns
		Iterator<String> headerItr = headerList.iterator();
		Iterator<ColumnType<?>> typeItr = typeList.iterator();
		while(headerItr.hasNext()){
			
			String header = headerItr.next();
			ColumnType<?> type = null;
			if (typeItr.hasNext()){
				type = typeItr.next();
			}
			
			table.addColumn(header, type, null);
		}
		
		// add rows
		Iterator<List<String>> dataItr = data.iterator();
		while(dataItr.hasNext()){
			table.addRow(dataItr.next());
		}
		
		return table;
	}
	
	/**
	 * Returns an iterator over rows of table.
	 */
	@Override
	public Iterator<List<?>> iterator() {
		return new DataTableIterator(this);
	}
	
	/**
	 * Returns an iterator over rows of table, starting at
	 * a specified row position.
	 * 
	 * @param rowIndex  index of the first row to be returned 
	 * by the iterator {@code next()} method
	 * @return a row iterator starting at the specified index
	 */
	public Iterator<List<?>> iterator(int rowIndex) {
		return new DataTableIterator(this, rowIndex);
	}
	
	/**
	 * Returns an iterator over rows of table, starting at
	 * a specified row position limited to specified number of rows.
	 * 
	 * @param rowIndex  index of the first row to be returned 
	 * by the iterator {@code next()} method
	 * @param limit  number of rows to be returned by the iterator
	 * @return a row iterator starting at the specified index with limit
	 */
	public Iterator<List<?>> iterator(int rowIndex, int limit) {
		return new DataTableIterator(this, rowIndex, limit);
	}
	
	/**
	 * Returns an iterator over formatted rows of table.
	 * 
	 * @return an iterator that returns formatted rows
	 */
	public Iterator<List<String>> formattedIterator() {
		return new FormattedDataTableIterator(this);
	}
	
	/**
	 * Returns an iterator over formatted rows of table, starting at
	 * a specified row position.
	 * 
	 * @param startIndex  index of the first row to be returned 
	 * by the iterator {@code next()} method
	 * @return a formatted row iterator starting at the specified index
	 */
	public Iterator<List<String>> formattedIterator(int startIndex) {
		return new FormattedDataTableIterator(this, startIndex);
	}
	
	/**
	 * Returns an iterator over formatted rows of table, starting at
	 * a specified row position limited to specified number of rows.
	 * 
	 * @param startIndex  index of the first row to be returned 
	 * by the iterator {@code next()} method
	 * @param limit  number of rows to be returned by the iterator
	 * @return a formatted row iterator starting at the specified index with limit
	 */
	public Iterator<List<String>> formattedIterator(int startIndex, int limit) {
		return new FormattedDataTableIterator(this, startIndex, limit);
	}
	
	/**
	 * Returns string representation of the tabular data, number of 
	 * rows limited to {@code DEFAULT_PRINT_LIMIT}.
	 */
	@Override
	public String toString(){
		return this.toString(0);
	}
	
	/**
	 * Returns string representation of the tabular data, starting at specified
	 * row index and number of rows limited to {@code DEFAULT_PRINT_LIMIT}.
	 * 
	 * @param startIndex  index of the first row in the string representation
	 * @return string representation
	 */
	public String toString(int startIndex){
		return this.toString(startIndex, DEFAULT_PRINT_LIMIT);
	}
	
	/**
	 * Returns string representation of the tabular data, starting at specified
	 * row index and number of rows limited to specified limit.
	 * 
	 * @param startIndex  index of the first row in the string representation
	 * @param limit  number of rows in the string representation
	 * @return string representation
	 */
	public String toString(int startIndex, int limit){
		return DataTableFormatter.formatToTable(this, startIndex, limit);
	}
}
