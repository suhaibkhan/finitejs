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
	
	/**
	 * Creates a new {@code DataTable} instance with given header list, 
	 * type list and data as list of list.
	 * 
	 * @param data  tabular data as list of rows and each 
	 * row as list of string values
	 * @param typeList  column types
	 * @param headerList  column header names
	 * @return {@code DataTable} instance
	 */
	public static DataTable getTable(List<List<String>> data, 
			List<ColumnType<?>> typeList, List<String> headerList){
		
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
		if(data != null){
			Iterator<List<String>> dataItr = data.iterator();
			while(dataItr.hasNext()){
				table.addRow(dataItr.next());
			}
		}
		
		return table;
	}
	
	/**
	 * Creates a new {@code DataTable} instance with given header list, 
	 * type specifier list and data as list of list.
	 * 
	 * @param data  tabular data as list of rows and each 
	 * row as list of string values.
	 * @param typeStringList  list of string representation of types in column order
	 * @param headerList  list of column headers
	 * @return {@code DataTable} instance
	 */
	public static DataTable getTableWithTypeStrings(List<List<String>> data, 
			List<String> typeStringList, List<String> headerList){
		
		List<ColumnType<?>> predefinedTypeList = new ArrayList<>();
		
		// convert type strings to ColumnType list
		if (typeStringList != null && !typeStringList.isEmpty()){
			for (String typeString : typeStringList){
				predefinedTypeList.add(ColumnType.getType(typeString));
			}
		}
		
		List<ColumnType<?>> typeList = findDataTypes(data, predefinedTypeList);
		if (typeList.size() == 0){
			throw new IllegalArgumentException("Not able to determine type of data");
		}
		
		if (headerList == null){
			headerList = new ArrayList<>();
		}
		
		// add column index as default headers
		if (headerList.size() < typeList.size()){
			for (int i = headerList.size(); i < typeList.size(); i++){
				headerList.add(String.valueOf(i));
			}
		}
		
		return getTable(data, typeList, headerList);
	}
	
	/**
	 * Creates a new {@code DataTable} instance with given tabular data.
	 * 
	 * @param data  tabular data as list of rows and each 
	 * row as list of string values
	 * @return {@code DataTable} instance
	 */
	public static DataTable getTable(List<List<String>> data){
		
		// dynamically find column type
		List<ColumnType<?>> typeList = findDataTypes(data, null);
		if (typeList.size() == 0){
			throw new IllegalArgumentException("Not able to determine type of data");
		}
		
		List<String> headerList = new ArrayList<>();
		
		// add column index default header
		for (int i = 0; i < typeList.size(); i++){
			headerList.add(String.valueOf(i));
		}
		
		return getTable(data, typeList, headerList);
	}
	
	/**
	 * Dynamically find type of tabular data. If a predefined type exists for a column,
	 * then type checking will be skipped for that column.
	 * 
	 * @param data  tabular data as list of rows and each 
	 * row as list of string values.
	 * @param predefinedTypeList  list of predefined types in column order
	 * @return list of types
	 */
	public static List<ColumnType<?>> findDataTypes(
			List<List<String>> data, List<ColumnType<?>> predefinedTypeList){
		
		List<ColumnType<?>> typeList = new ArrayList<>();
		if (predefinedTypeList != null){
			typeList.addAll(predefinedTypeList);
		}
		
		// null check
		if (data == null){
			return typeList;
		}
		
		Iterator<List<String>> rowItr = data.iterator();
		
		while (rowItr.hasNext()){
			List<String> rowData = rowItr.next();
			
			ColumnType<?> prevType = null;
			ColumnType<?> curType = null;
			Iterator<String> colItr = rowData.iterator();
			
			for (int i = 0; i < rowData.size(); i++){
				
				// check if type already defined
				if (predefinedTypeList != null && predefinedTypeList.size() > i && 
						predefinedTypeList.get(i) != null){
					continue;
				}
				
				// get previous type of the column
				if (typeList.size() <= i){
					// initialize type list with null
					typeList.add(null);
				}else{
					prevType = typeList.get(i);
				}
				
				// determine current type
				curType = ColumnType.findType(colItr.next());
				
				// resolve conflict
				if (prevType != null && curType != null && (!prevType.equals(curType))){
					curType = ColumnType.getPreferredType(prevType, curType);
					typeList.set(i, curType);
				}
				
				// update new type
				if (prevType == null && curType != null){
					typeList.set(i, curType);
				}
			}
		}
		
		return typeList;
	}
}
