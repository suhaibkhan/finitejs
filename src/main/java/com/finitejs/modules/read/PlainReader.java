package com.finitejs.modules.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlainReader {
	
	private static final String DEFAULT_COMMENT_STRING = "#";
	private static final String DEFAULT_DELIMITER = ",";
	
	private List<ColumnType<?>> predefinedTypeList;
	
	private List<String> preDefinedHeaderList;
	
	private List<InputFormatter> inputFormatterList;
	
	private String commentString;
	
	private PlainReader(){
		predefinedTypeList = new ArrayList<>();
		preDefinedHeaderList = new ArrayList<>();
		inputFormatterList = new ArrayList<>();
		commentString = DEFAULT_COMMENT_STRING;
	}
	
	public void setType(int columnIndex, String typeString){
		// initialize with null
		if (predefinedTypeList.size() <= columnIndex){
			for (int i = 0; i <= columnIndex; i++){
				predefinedTypeList.add(null);
			}
		}
		
		predefinedTypeList.set(columnIndex, ColumnType.getType(typeString));
	}
	
	public void setType(String[] typeStringArray){
		
		// clear type list
		predefinedTypeList.clear();
		// add new types
		for (int i = 0; i < typeStringArray.length; i++){
			predefinedTypeList.add(ColumnType.getType(typeStringArray[i]));
		}
		
	}
	
	public void setHeader(int columnIndex, String header){
		// initialize with null
		if (preDefinedHeaderList.size() <= columnIndex){
			for (int i = 0; i <= columnIndex; i++){
				preDefinedHeaderList.add(null);
			}
		}
		
		preDefinedHeaderList.set(columnIndex, header);
	}
	
	public void setHeader(String[] headerArray){
		
		// clear header list
		preDefinedHeaderList.clear();
		// copy values
		for (int i = 0; i < headerArray.length; i++){
			preDefinedHeaderList.add(headerArray[i]);
		}
		
	}
	
	public void setFormatter(int columnIndex, InputFormatter inputFormatter){
		// initialize with null
		if (inputFormatterList.size() <= columnIndex){
			for (int i = 0; i <= columnIndex; i++){
				inputFormatterList.add(null);
			}
		}
		
		inputFormatterList.set(columnIndex, inputFormatter);
	}
	
	public void setFormatter(InputFormatter[] inputFormatterArray){
		
		// clear formatter list
		inputFormatterList.clear();
		// copy values
		for (int i = 0; i < inputFormatterArray.length; i++){
			inputFormatterList.add(inputFormatterArray[i]);
		}
		
	}
	
	public void setCommentString(String commentString){
		if (commentString != null && !commentString.trim().isEmpty()){
			this.commentString = commentString;
		}else{
			// disable comment
			this.commentString = null;
		}
	}
	
	public DataTable read(String filePath) throws IOException{
		return read(new File(filePath), DEFAULT_DELIMITER, true);
	}
	
	public DataTable read(String filePath, String delimiter) throws IOException{
		return read(new File(filePath), delimiter, true);
	}
	
	public DataTable read(String filePath, 
			String delimiter, boolean isHeaderPresent) throws IOException{
		return read(new File(filePath), delimiter, isHeaderPresent);
	}
	
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
		
		DataTable dTable = DataTable.getTable(headerList, typeList, csvData);
		
		return dTable;
	}
	
	public static PlainReader get(){
		return new PlainReader();
	}
	
	public static PlainReader get(String[] typeStringArray){
		PlainReader reader = new PlainReader();
		reader.setType(typeStringArray);
		return reader;
	}
	
	public static PlainReader get(String[] typeStringArray, String[] headerArray){
		PlainReader reader = new PlainReader();
		reader.setType(typeStringArray);
		reader.setHeader(headerArray);
		return reader;
	}
	
}
