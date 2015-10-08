package com.finitejs.modules.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code PlainReader} can be used to read file formats like CSV, TSV, 
 * and all other formats where rows are newline separated and 
 * columns separated by some delimiters.
 */
public class PlainReader {
	
	/**
	 * Constant for default comment string/character. 
	 * Lines starting with comment characters will be ignored while reading.
	 */
	public static final String DEFAULT_COMMENT_STRING = "#";
	
	/**
	 * Constant for default delimiter.
	 * If no other delimiters are used while reading, file format is 
	 * considered to be CSV.
	 */
	public static final String DEFAULT_DELIMITER = ",";
	
	/** 
	 * Predefined column type list. If a column type is predefined, 
	 * then dynamic type checking is skipped for that column. Table column index
	 * is matched with column type index in this list.
	 */
	private List<ColumnType<?>> predefinedTypeList;
	
	/**
	 * Predefined column header list. If a column header is predefined, 
	 * then header from file is discarded. Table column index
	 * is matched with column header index in this list.
	 */
	private List<String> preDefinedHeaderList;
	
	/**
	 * {@link InputFormatter} to be used for each column. Table column index
	 * is matched with column formatter index in this list.
	 */
	private List<InputFormatter> inputFormatterList;
	
	/** Comment string used */
	private String commentString;
	
	private PlainReader(){
		predefinedTypeList = new ArrayList<>();
		preDefinedHeaderList = new ArrayList<>();
		inputFormatterList = new ArrayList<>();
		commentString = DEFAULT_COMMENT_STRING;
	}
	
	/**
	 * Set a predefined type for a column with specified index. If a column 
	 * type is predefined, then dynamic type checking is skipped for that column.
	 * 
	 * @param columnIndex  index of the column to which the type is set
	 * @param typeString  string representation of the type
	 */
	public void setType(int columnIndex, String typeString){
		// initialize with null
		if (predefinedTypeList.size() <= columnIndex){
			for (int i = 0; i <= columnIndex; i++){
				predefinedTypeList.add(null);
			}
		}
		
		predefinedTypeList.set(columnIndex, ColumnType.getType(typeString));
	}
	
	/**
	 * Set a predefined type for columns. If a column type is predefined, then dynamic 
	 * type checking is skipped for that column. Table column index is matched with 
	 * column type index in this list.
	 * 
	 * @param typeStringArray  list of string representation of types in column order
	 */
	public void setType(String[] typeStringArray){
		
		// clear type list
		predefinedTypeList.clear();
		// add new types
		for (int i = 0; i < typeStringArray.length; i++){
			predefinedTypeList.add(ColumnType.getType(typeStringArray[i]));
		}
		
	}
	
	/**
	 * Set a predefined header for a column with specified index. If a column header 
	 * is predefined, then header from file is discarded.
	 * 
	 * @param columnIndex  index of the column to which the header is set
	 * @param header  column header
	 */
	public void setHeader(int columnIndex, String header){
		// initialize with null
		if (preDefinedHeaderList.size() <= columnIndex){
			for (int i = 0; i <= columnIndex; i++){
				preDefinedHeaderList.add(null);
			}
		}
		
		preDefinedHeaderList.set(columnIndex, header);
	}
	
	/**
	 * Set a predefined header for columns. If a column header is predefined, then header 
	 * from file is discarded.  Table column index is matched with column header 
	 * index in this list.
	 * 
	 * @param headerArray  list of column headers
	 */
	public void setHeader(String[] headerArray){
		
		// clear header list
		preDefinedHeaderList.clear();
		// copy values
		for (int i = 0; i < headerArray.length; i++){
			preDefinedHeaderList.add(headerArray[i]);
		}
		
	}
	
	/**
	 * Set an {@link InputFormatter} for a column with specified index. 
	 * 
	 * @param columnIndex  index of the column to which the formatter is set
	 * @param inputFormatter  {@link InputFormatter} to use
	 */
	public void setFormatter(int columnIndex, InputFormatter inputFormatter){
		// initialize with null
		if (inputFormatterList.size() <= columnIndex){
			for (int i = 0; i <= columnIndex; i++){
				inputFormatterList.add(null);
			}
		}
		
		inputFormatterList.set(columnIndex, inputFormatter);
	}
	
	/**
	 * Set {@link InputFormatter} for columns. Table column index is matched with 
	 * column formatter index in this list.
	 * 
	 * @param inputFormatterArray  list of {@link InputFormatter} to be used
	 */
	public void setFormatter(InputFormatter[] inputFormatterArray){
		
		// clear formatter list
		inputFormatterList.clear();
		// copy values
		for (int i = 0; i < inputFormatterArray.length; i++){
			inputFormatterList.add(inputFormatterArray[i]);
		}
		
	}
	
	/**
	 * Set a comment string to use while reading. Lines starting with comment 
	 * string will be ignored.
	 * 
	 * @param commentString  comment string
	 */
	public void setCommentString(String commentString){
		if (commentString != null && !commentString.trim().isEmpty()){
			this.commentString = commentString;
		}else{
			// disable comment
			this.commentString = null;
		}
	}
	
	/**
	 * Read the specified file and returns the whole data as a {@link DataTable}.
	 * Default delimiter will be used to separate columns and first non-comment row
	 * will be considered as header row.
	 * 
	 * @param filePath  path of the file
	 * @return {@link DataTable}
	 * @throws IOException if error occurs when reading file
	 */
	public DataTable read(String filePath) throws IOException{
		return read(new File(filePath), DEFAULT_DELIMITER, true);
	}
	
	/**
	 * Read the specified file and returns the whole data as a {@link DataTable}.
	 * Specified custom delimiter will be used to separate columns and first 
	 * non-comment row will be considered as header row.
	 * 
	 * @param filePath  path of the file
	 * @param delimiter  custom delimiter to separate columns in a row
	 * @return {@link DataTable}
	 * @throws IOException if error occurs when reading file
	 */
	public DataTable read(String filePath, String delimiter) throws IOException{
		return read(new File(filePath), delimiter, true);
	}
	
	/**
	 * Read the specified file and returns the whole data as a {@link DataTable}.
	 * Specified custom delimiter will be used to separate columns.
	 * 
	 * @param filePath  path of the file
	 * @param delimiter  custom delimiter to separate columns in a row
	 * @param isHeaderPresent  true if first non-comment row is header row, else false
	 * @return {@link DataTable}
	 * @throws IOException if error occurs when reading file
	 */
	public DataTable read(String filePath, 
			String delimiter, boolean isHeaderPresent) throws IOException{
		return read(new File(filePath), delimiter, isHeaderPresent);
	}
	
	/**
	 * Read the specified file and returns the whole data as a {@link DataTable}.
	 * Specified custom delimiter will be used to separate columns.
	 * 
	 * @param file  file to read
	 * @param delimiter  custom delimiter to separate columns in a row
	 * @param isHeaderPresent  true if first non-comment row is header row, else false
	 * @return {@link DataTable}
	 * @throws IOException if error occurs when reading file
	 */
	public DataTable read(File file, 
			String delimiter, boolean isHeaderPresent) throws IOException {
		
		if (!file.exists()){
			throw new FileNotFoundException();
		}
		
		List<List<String>> csvData = new ArrayList<>();
		List<ColumnType<?>> typeList = new ArrayList<>(predefinedTypeList);
		List<String> headerList = new ArrayList<>(preDefinedHeaderList);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		int columnSize = 0;
		List<String> rowData;
		ColumnType<?> prevType;
		ColumnType<?> curType;
		InputFormatter inputFormatter;
		
		// store CSV data in a dynamic array of string array
		// also determine data type and number of columns
		while((line = reader.readLine()) != null){
			
			// check for comment
			if (commentString != null && line.startsWith(commentString)){
				// discard line
				continue;
			}
			
			rowData = Arrays.asList(line.split(delimiter));
			columnSize = columnSize < rowData.size() ? rowData.size() : columnSize;
			
			if (isHeaderPresent){
				// consider row as header
				for (int i = 0; i < rowData.size(); i++){
					if (headerList.size() <= i){
						// initialize header list with null
						headerList.add(null);
					}
					
					// check for predefined headers
					if (headerList.get(i) == null){
						headerList.set(i, rowData.get(i));
					}
				}
				
				// header already read
				isHeaderPresent = false;
				continue;
			}
			
			prevType = null;
			curType = null;
			inputFormatter = null;
			
			// find type
			for (int i = 0; i < rowData.size(); i++){
				
				// check for formatters
				if (inputFormatterList.size() > i && inputFormatterList.get(i) != null){
					inputFormatter = inputFormatterList.get(i);
					
					// format row value with formatter
					rowData.set(i, inputFormatter.format(rowData.get(i)));
				}
				
				// check if type already defined
				if (predefinedTypeList.size() > i && predefinedTypeList.get(i) != null){
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
				curType = ColumnType.findType(rowData.get(i));
				
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
			
			csvData.add(rowData);
		}
		
		reader.close();
		
		// add column index as default headers
		if (headerList.size() < columnSize){
			for (int i = headerList.size(); i < columnSize; i++){
				headerList.add(String.valueOf(i));
			}
		}
		
		DataTable dTable = DataTable.getTable(csvData, typeList, headerList);
		
		return dTable;
	}
	
	/**
	 * Returns a new {@code PlainReader} instance.
	 * 
	 * @return {@code PlainReader} instance
	 */
	public static PlainReader get(){
		return new PlainReader();
	}
	
	/**
	 * Returns a new {@code PlainReader} instance with specified type list.
	 * 
	 * @param typeStringArray  list of string representation of types in column order
	 * @return {@code PlainReader} instance
	 */
	public static PlainReader get(String[] typeStringArray){
		PlainReader reader = new PlainReader();
		reader.setType(typeStringArray);
		return reader;
	}
	
	/**
	 * Returns a new {@code PlainReader} instance with specified type list and column headers.
	 * 
	 * @param typeStringArray  list of string representation of types in column order
	 * @param headerArray  list of column headers
	 * @return {@code PlainReader} instance
	 */
	public static PlainReader get(String[] typeStringArray, String[] headerArray){
		PlainReader reader = new PlainReader();
		reader.setType(typeStringArray);
		reader.setHeader(headerArray);
		return reader;
	}
	
}
