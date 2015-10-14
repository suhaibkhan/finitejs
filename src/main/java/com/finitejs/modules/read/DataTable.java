package com.finitejs.modules.read;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.finitejs.modules.read.types.NumberType;
import com.finitejs.modules.read.util.DataTableFormatter;

/**
 * Class used to represent tabular data.
 * Each column in tabular data is an instance of {@link Column} class.
 * Values are stored in {@link Column} instances and are of different type. 
 */
public class DataTable implements Iterable<List<Object>>{
	/**
	 * Constant for default row count while printing in tabular format.
	 */
	public static final int DEFAULT_PRINT_LIMIT = 30;
	
	/**
	 * Constant for default index header name
	 */
	private static final String DEFAULT_INDEX_HEADER_NAME = "###INDEX###";
	
	/** List to store columns */
	private List<Column<?>> table;
	
	/** Number of rows */
	private int rowCount;
	
	/** Index map */
	private Map<String, List<Integer>> indexMap; 
	
	/** List that maps index map order to row index */
	private List<Integer> indexOrderList;
	
	/** Column name - index map */
	private Map<String, Integer> columnIndexMap;
	
	/** Name of the index column */
	private String indexColumnName;
	
	/** Index column */
	private Column<?> indexColumn;
	
	private DataTable(){
		rowCount = 0;
		table = new ArrayList<>();
		indexMap = new LinkedHashMap<>();
		indexOrderList = new ArrayList<>();
		columnIndexMap = new LinkedHashMap<>();
		
		// row index will be index column by default and 
		// default index type is number
		indexColumnName = DEFAULT_INDEX_HEADER_NAME;
		indexColumn = Column.create(DEFAULT_INDEX_HEADER_NAME, NumberType.getType());
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
	 * Returns index map used.
	 * 
	 * @return index map
	 */
	public Map<String, List<Integer>> getIndexMap(){
		return indexMap;
	}
	
	/**
	 * Returns index order list. 
	 * Index order list maps current table order to original data row order.
	 * n<sup>th</sup> element in index order list returns the 
	 * internal data row index corresponding to n<sup>th</sup> row of table.
	 * 
	 * @return index order list
	 */
	public List<Integer> getIndexOrderList(){
		return indexOrderList;
	}
	
	/**
	 * Get list of column headers in table.
	 * 
	 * @return list of headers
	 */
	public List<String> getHeaderList(){
		List<String> headerList = new ArrayList<>();
		headerList.addAll(columnIndexMap.keySet());
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
	 * Returns list of string that represents {@code DataTable} type information.
	 * 
	 * @return list of string representing types of table values
	 */
	public List<String> getTypeStringList(){
		List<String> typeStringList = new ArrayList<>();
		for (Column<?> column : table){
			typeStringList.add(column.getType().toString());
		}
		return typeStringList;
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
		
		// default index value is row index
		String indexValue = NumberType.getType().format((double) rowCount);
		
		while(tableItr.hasNext()){
			Column<?> column = tableItr.next();
			
			// fill horizontally with null if no value present
			String rowVal = null;
			if (rowItr.hasNext()){
				rowVal = rowItr.next();
			}
			
			column.parseAndAdd(rowVal);
			
			if (indexColumnName.equals(column.getName())){
				// last added value
				indexValue = column.getFormattedValue(rowCount);
			}
		}
		
		// add to default index column
		if (indexColumnName.equals(DEFAULT_INDEX_HEADER_NAME)){
			indexColumn.parseAndAdd(indexValue);
		}
		
		// add to index map
		if (!indexMap.containsKey(indexValue)){
			indexMap.put(indexValue, new ArrayList<>());
		}
		indexMap.get(indexValue).add(rowCount);

		// update index order
		indexOrderList.add(rowCount);
		
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
	 * @throws IllegalArgumentException if duplicate column name
	 */
	public void addColumn(String header, ColumnType<?> type, List<String> columnData){
		
		if (header == null){
			// index as header
			header = String.valueOf(getColumnCount());
		}
		
		if (header.equals(DEFAULT_INDEX_HEADER_NAME)){
			throw new IllegalArgumentException(
					String.format("Cannot use %s as column name.", DEFAULT_INDEX_HEADER_NAME));
		}
		
		if (columnIndexMap.containsKey(header)){
			throw new IllegalArgumentException("Column names must be unique.");
		}
		
		Column<?> column = Column.create(header, type);
		
		if (column == null){
			return;
		}
		
		Iterator<String> columnItr = null;
		if (columnData != null){
			columnItr = columnData.iterator();
		}
		
		boolean isIndexColumn = false;
		if (indexColumnName.equals(header)){
			// update index column reference
			indexColumn = column;
			isIndexColumn = true;
		}
		
		for (int i = 0; i < rowCount; i++){
			
			// fill vertically with null if no value present
			String colVal = null;
			if (columnItr != null && columnItr.hasNext()){
				colVal = columnItr.next();
			}
			
			column.parseAndAdd(colVal);
			
			// if this column is an index column
			// then each row must be added to index map also
			if (isIndexColumn){
				String indexValue = column.getFormattedValue(i);
				
				if (!indexMap.containsKey(indexValue)){
					indexMap.put(indexValue, new ArrayList<>());
				}
				indexMap.get(indexValue).add(i);
			}
		}
		
		// add new column to table
		table.add(column);
		
		// add column to column-index map
		columnIndexMap.put(header, table.size() - 1);
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
	 * Get specified row elements.
	 * 
	 * @param rowIndex  index of the specified row
	 * @return row values
	 * @throws IndexOutOfBoundsException if row index out of range
	 */
	public List<Object> getRow(int rowIndex){
		List<Object> rowValues = new ArrayList<>();
		int dataIndex = indexOrderList.get(rowIndex);
		for (Column<?> column : table){
			rowValues.add(column.get(dataIndex));
		}
		return rowValues;
	}
	
	/**
	 * Get string representations of specified row elements.
	 * 
	 * @param rowIndex  index of the specified row
	 * @return string representations of row values
	 * @throws IndexOutOfBoundsException if row index out of range
	 */
	public List<String> getFormattedRow(int rowIndex){
		List<String> formattedRowValues = new ArrayList<>();
		int dataIndex = indexOrderList.get(rowIndex);
		for (Column<?> column : table){
			formattedRowValues.add(column.getFormattedValue(dataIndex));
		}
		return formattedRowValues;
	}
	
	/**
	 * Get specified column elements.
	 * 
	 * @param colIndex  index of the specified column
	 * @return column values
	 * @throws IndexOutOfBoundsException if column index out of range
	 */
	public List<Object> getColumn(int colIndex){
		List<Object> colValues = new ArrayList<>();
		Column<?> column = table.get(colIndex);
		
		// get values in correct order
		indexOrderList.forEach(i -> colValues.add(column.get(i)));
		
		return colValues;
	}
	
	/**
	 * Get string representations of specified column values.
	 * 
	 * @param colIndex  index of the specified column
	 * @return string representation of column values
	 * @throws IndexOutOfBoundsException if column index out of range
	 */
	public List<String> getFormattedColumn(int colIndex){
		
		List<String> columnStrValues = new ArrayList<>();
		Column<?> column = table.get(colIndex);
		
		// get values in correct order
		indexOrderList.forEach(i -> columnStrValues.add(column.getFormattedValue(i)));
		
		return columnStrValues;
	}
	
	/**
	 * Sorts {@code DataTable} with respect to the specified column name.
	 * 
	 * @param columnName  name of the column which specifies the sorting order
	 * @param sortOrder  sorting direction {@link Column.SORT_ORDER_ASC} or {@link Column.SORT_ORDER_DESC}
	 * @throws IllegalArgumentException if invalid column name
	 */
	public void sort(String columnName, String sortOrder){
		
		// check for column
		if (columnName == null || !columnIndexMap.containsKey(columnName)){
			throw new IllegalArgumentException("Invalid column name");
		}
		
		String prevIndexColumn = null;
		if (!columnName.equals(indexColumnName)){
			// change index
			prevIndexColumn = indexColumnName;
			index(columnName);
		}
		
		// sort based on current index
		List<String> sortedIndexValues = indexColumn.sort(sortOrder);
		
		// clear existing order
		indexOrderList.clear();
		
		Map<String, List<Integer>> newIndexMap = new LinkedHashMap<>();
		sortedIndexValues.stream().distinct().forEach(v -> {
			newIndexMap.put(v, indexMap.get(v));
			// new order
			indexOrderList.addAll(indexMap.get(v));
		});
		
		indexMap = newIndexMap;
		
		if (prevIndexColumn != null){
			// change index back to previous index
			index(prevIndexColumn);
		}
		
	}
	
	/**
	 * Sorts {@code DataTable} with respect to the specified column index.
	 * 
	 * @param colIndex  index of the specified column
	 * @param sortOrder  sorting direction {@link Column.SORT_ORDER_ASC} or {@link Column.SORT_ORDER_DESC}
	 * @throws IndexOutOfBoundsException if column index out of range
	 */
	public void sort(int colIndex, String sortOrder){
		String columnName = table.get(colIndex).getName();
		sort(columnName, sortOrder);
	}
	
	/**
	 * Change index column of the {@code DataTable}.
	 * 
	 * @param columnName  name of the new index column
	 * @throws IllegalArgumentException if invalid column name
	 */
	public void index(String columnName){
		// check for column
		if (columnName == null || (!columnIndexMap.containsKey(columnName) && 
				!columnName.equals(DEFAULT_INDEX_HEADER_NAME))){
			throw new IllegalArgumentException("Invalid column name");
		}
		
		Column<?> column = null;
		if (columnName.equals(DEFAULT_INDEX_HEADER_NAME)){
			// create default row index based index column
			column = Column.create(DEFAULT_INDEX_HEADER_NAME, NumberType.getType());
			for (int i = 0; i < rowCount; i++){
				column.parseAndAdd(String.valueOf(i));
			}
		}else{
			column = table.get(columnIndexMap.get(columnName));
		}
		
		// create a new index map
		Map<String, List<Integer>> newIndexMap = new LinkedHashMap<>();
		
		// iterate through existing index order to find values 
		// of new column in current order
		for (int nextRowIndex : indexOrderList){
			
			String columnStrVal = column.getFormattedValue(nextRowIndex);
			if (!newIndexMap.containsKey(columnStrVal)){
				newIndexMap.put(columnStrVal, new ArrayList<>());
			}
			
			newIndexMap.get(columnStrVal).add(nextRowIndex);
		}
		
		// update index
		indexMap = newIndexMap;
		indexColumn = column;
		indexColumnName = columnName;
	}
	
	/**
	 * Change index column of the {@code DataTable} to specified column.
	 * 
	 * @param colIndex  index of the specified column
	 * @throws IndexOutOfBoundsException if column index out of range
	 */
	public void index(int colIndex){
		String columnName = table.get(colIndex).getName();
		index(columnName);
	}
	
	/**
	 * Returns an iterator over rows of table.
	 */
	@Override
	public Iterator<List<Object>> iterator() {
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
	public Iterator<List<Object>> iterator(int rowIndex) {
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
	public Iterator<List<Object>> iterator(int rowIndex, int limit) {
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
	 * @param typeList  column types, cannot be empty or null
	 * @param headerList  column header names, cannot be empty or null
	 * @param indexColumn  name/header of the column to be used as index
	 * @return {@code DataTable} instance
	 * @throws IllegalArgumentException if empty type list or header list
	 */
	public static DataTable getTable(List<List<String>> data, 
			List<ColumnType<?>> typeList, List<String> headerList, String indexColumnName){
		
		DataTable table = new DataTable();
		
		// set index column
		if (indexColumnName != null){
			table.indexColumnName = indexColumnName;
		}
		
		if (typeList == null || headerList == null || 
				typeList.isEmpty() || headerList.isEmpty()){
			throw new IllegalArgumentException("Type list or header list cannot be empty");
		}
		
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
	 * @param typeStringList  list of string representation of types in 
	 * column order, or null if no predefined types and types will be determined dynamically
	 * @param headerList  list of column headers, or null if no predefined 
	 * headers and column indexes will be used as headers
	 * @param indexColumn  name/header of the column to be used as index
	 * @return {@code DataTable} instance
	 * @throws IllegalArgumentException if not able to determine type
	 */
	public static DataTable getTableWithTypeStrings(List<List<String>> data, 
			List<String> typeStringList, List<String> headerList, String indexColumn){
		
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
		
		return getTable(data, typeList, headerList, indexColumn);
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
