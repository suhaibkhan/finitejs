package com.finitejs.modules.read;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.finitejs.modules.read.util.DataTableFormatter;

public class DataTable implements Iterable<List<?>>{
	
	private static final int DEFAULT_PRINT_LIMIT = 30;
	
	private List<Column<?>> table;
	
	private int rowCount;
		
	private DataTable(){
		rowCount = 0;
		table = new ArrayList<>();
	}
	
	public int getRowCount(){
		return rowCount;
	}
	
	public int getColumnCount(){
		return table.size();
	}
	
	public List<Column<?>> getColumnList(){
		return table;
	}
	
	public List<String> getHeaderList(){
		List<String> headerList = new ArrayList<>();
		for (Column<?> column : table){
			headerList.add(column.getName());
		}
		return headerList;
	}
	
	public List<ColumnType<?>> getTypeList(){
		List<ColumnType<?>> typeList = new ArrayList<>();
		for (Column<?> column : table){
			typeList.add(column.getType());
		}
		return typeList;
	}
	
	/**
	 * Returns a string that represents DataTable type information.
	 * 
	 * @return Type String
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
	
	public void addColumn(String header, ColumnType<?> type, List<String> columnData){
		
		if (header == null){
			// index as header
			header = String.valueOf(getColumnCount());
		}
		
		Column<?> column = Column.create(header, type);
		
		if (columnData != null){
			Iterator<String> columnItr = columnData.iterator();
			for (int i = 0; i < rowCount; i++){
				if (columnItr.hasNext()){
					column.parseAndAdd(columnItr.next());
				}else{
					// fill with null
					column.parseAndAdd(null);
				}
			}
		}
		
		// add new column to table
		table.add(column);
	}
	
	public void addColumn(String header, String typeString, List<String> columnData){
		ColumnType<?> type = ColumnType.getType(typeString);
		addColumn(header, type, columnData);
	}
	
	/**
	 * Factory method for DataTable
	 * 
	 * @param headerList
	 * @param typeList
	 * @param data
	 * @return
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
	
	@Override
	public Iterator<List<?>> iterator() {
		return new DataTableIterator(this);
	}
	
	public Iterator<List<?>> iterator(int startIndex) {
		return new DataTableIterator(this, startIndex);
	}
	
	public Iterator<List<?>> iterator(int startIndex, int limit) {
		return new DataTableIterator(this, startIndex, limit);
	}
	
	public Iterator<List<String>> formattedIterator() {
		return new FormattedDataTableIterator(this);
	}
	
	public Iterator<List<String>> formattedIterator(int startIndex) {
		return new FormattedDataTableIterator(this, startIndex);
	}
	
	public Iterator<List<String>> formattedIterator(int startIndex, int limit) {
		return new FormattedDataTableIterator(this, startIndex, limit);
	}
	
	@Override
	public String toString(){
		return this.toString(0);
	}
	
	public String toString(int startIndex){
		return this.toString(startIndex, DEFAULT_PRINT_LIMIT);
	}
	
	public String toString(int startIndex, int limit){
		return DataTableFormatter.formatToTable(this, startIndex, limit);
	}
}
