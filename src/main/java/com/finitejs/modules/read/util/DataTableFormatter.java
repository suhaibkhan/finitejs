package com.finitejs.modules.read.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.finitejs.modules.read.DataTable;

/**
 * Formatter utility for {@code DataTable}.
 */
public class DataTableFormatter {

	/** 
	 * Constant for format of skipped rows message to be printed 
	 * in end of tabular format. 
	 */
	private static final String ROWS_REMAINING_MSG_FORMAT = "Skipped %s rows.";
	
	/** Constant for a column separator character used in tabular format. */
	private static final String TABLE_VIEW_SEPARATOR = "|";
	
	/** Constant for a line character used in tabular format. */
	private static final String LINE_CHAR = "-";

	/**
	 * Formats a {@code DataTable} in tabular format.
	 * 
	 * @param table  {@code DataTable} instance
	 * @param startIndex  starting position of the table
	 * @param limit  number of rows to be included
	 * @return string representing {@code DataTable} in tabular format
	 */
	public static String formatToTable(DataTable table, int startIndex, int limit){
		
		StringBuffer tableStrBuffer = new StringBuffer();
		
		List<List<String>> tableData = new ArrayList<>();
		
		List<String> headerList = table.getHeaderList();
		
		// find max char count for each column
		int maxCharCount = 0;
		
		for (String header : headerList){
			maxCharCount = maxCharCount < header.length() ? header.length() : maxCharCount;
		}
		
		Iterator<List<String>> tableItr = table.formattedIterator(startIndex, limit);
		while(tableItr.hasNext()){
			List<String> rowData = tableItr.next();
			for (String row : rowData){
				maxCharCount = maxCharCount < row.length() ? row.length() : maxCharCount;
			}
			// add to data list
			tableData.add(rowData);
		}
		
		// print headers
		// total char count = ((maxCharCount + 2 space) * column size) + (column size + 1 seperators)
		int totalChartCountInRow = ((maxCharCount + 2) * headerList.size()) + (headerList.size() + 1);
		
		// top line
		tableStrBuffer.append(drawLine(totalChartCountInRow));

		tableStrBuffer.append(TABLE_VIEW_SEPARATOR);
		for (String header : headerList){
			tableStrBuffer.append(String.format(" %" + -maxCharCount + "s %s", header, TABLE_VIEW_SEPARATOR));
		}
		tableStrBuffer.append(System.lineSeparator());	
		
		// header end line
		tableStrBuffer.append(drawLine(totalChartCountInRow));

		
		// table contents
		for (List<String> rowData : tableData){
			tableStrBuffer.append(TABLE_VIEW_SEPARATOR);
			for (String row : rowData){
				tableStrBuffer.append(String.format(" %" + -maxCharCount + "s %s", row, TABLE_VIEW_SEPARATOR));
			}
			tableStrBuffer.append(System.lineSeparator());
		}
		
		// show remaining row info
		if (startIndex > 0 || limit < table.getRowCount()){
			
			int noOfSkippedStartRows = startIndex;
			int noOfSkippedEndRows = table.getRowCount() - (startIndex + limit);
			
			// empty row
			tableStrBuffer.append(String.format("%s %" + (totalChartCountInRow - 2) + "s%n", 
					TABLE_VIEW_SEPARATOR, TABLE_VIEW_SEPARATOR));
			
			StringBuffer msgPart = new StringBuffer();
			if (noOfSkippedStartRows > 0){
				msgPart.append(String.format("first %d", noOfSkippedStartRows));
			}
			
			if (noOfSkippedEndRows > 0){
				if (noOfSkippedStartRows > 0){
					msgPart.append(" and ");
				}
				msgPart.append(String.format("last %d", noOfSkippedEndRows));
			}
			
			
			String msg = String.format(ROWS_REMAINING_MSG_FORMAT, msgPart);
			tableStrBuffer.append(String.format("%s%" + (totalChartCountInRow - 3) + "s %s%n", 
					TABLE_VIEW_SEPARATOR, msg, TABLE_VIEW_SEPARATOR));
		}
		
		// end line
		tableStrBuffer.append(drawLine(totalChartCountInRow));
		
		return tableStrBuffer.toString();
	
	}
	
	/**
	 * Used to draw a line of specific length with alphanumeric characters.
	 * 
	 * @param length  length of the line drawn
	 * @return line string
	 */
	private static String drawLine(int length){
		StringBuffer lineStrBuffer = new StringBuffer();
		
		for (int i = 0; i < length; i++){
			lineStrBuffer.append(LINE_CHAR);
		}
		
		lineStrBuffer.append(System.lineSeparator());
		
		return lineStrBuffer.toString();
	}
	
}
