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
	 * Predefined column name list. If a column name is predefined, 
	 * then header/name from file is discarded. Table column index
	 * is matched with column name index in this list.
	 */
	private List<String> preDefinedNameList;
	
	/**
	 * {@link InputFormatter} to be used for each column. Table column index
	 * is matched with column formatter index in this list.
	 */
	private List<InputFormatter> inputFormatterList;
	
	/** Comment string used */
	private String commentString;
	
	private PlainReader(){
		predefinedTypeList = new ArrayList<>();
		preDefinedNameList = new ArrayList<>();
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
	 * Set a predefined name for a column with specified index. If a column name 
	 * is predefined, then name/header from file is discarded.
	 * 
	 * @param columnIndex  index of the column to which the name is set
	 * @param name  column name
	 */
	public void setName(int columnIndex, String name){
		// initialize with null
		if (preDefinedNameList.size() <= columnIndex){
			for (int i = 0; i <= columnIndex; i++){
				preDefinedNameList.add(null);
			}
		}
		
		preDefinedNameList.set(columnIndex, name);
	}
	
	/**
	 * Set predefined name for columns. If a column name is predefined, then header/name 
	 * from file is discarded.  Table column index is matched with column name 
	 * index in this list.
	 * 
	 * @param nameArray  list of column names
	 */
	public void setName(String[] nameArray){
		
		// clear name list
		preDefinedNameList.clear();
		// copy values
		for (int i = 0; i < nameArray.length; i++){
			preDefinedNameList.add(nameArray[i]);
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
	 * will be considered as header row with column names.
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
	 * non-comment row will be considered as header row with column names.
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
	 * @param isHeaderPresent  true if first non-comment row is header 
	 * row with column names, else false
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
	 * @param isHeaderPresent  true if first non-comment row is header 
	 * row with column names, else false
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
		List<String> nameList = new ArrayList<>(preDefinedNameList);
		
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
				// consider row as header with column names
				for (int i = 0; i < rowData.size(); i++){
					if (nameList.size() <= i){
						// initialize names list with null
						nameList.add(null);
					}
					
					// check for predefined headers
					if (nameList.get(i) == null){
						nameList.set(i, rowData.get(i));
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
		
		// add column index as default names
		if (nameList.size() < columnSize){
			for (int i = nameList.size(); i < columnSize; i++){
				nameList.add(String.valueOf(i));
			}
		}
		
		// index null, default will be taken
		DataTable dTable = DataTable.getTable(csvData, typeList, nameList, null);
		
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
	 * Returns a new {@code PlainReader} instance with specified type list and column names.
	 * 
	 * @param typeStringArray  list of string representation of types in column order
	 * @param nameArray  list of column names
	 * @return {@code PlainReader} instance
	 */
	public static PlainReader get(String[] typeStringArray, String[] nameArray){
		PlainReader reader = new PlainReader();
		reader.setType(typeStringArray);
		reader.setName(nameArray);
		return reader;
	}
	
}
