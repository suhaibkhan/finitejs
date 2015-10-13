package com.finitejs.modules.read;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class PlainReaderTest {

	private DataTable table = null;
	
    @Before
    public void setup() throws IOException{
    	String sampleFilePath = "samples/sample.csv";
    	// String[] typeArray = new String[] {"date(yyyy-MM-dd)", "string", "number"};
    	// String[] headerArray = new String[]{"DATE", "ITEM NAME", "SALES COUNT"};
    	
    	// PlainReader reader = PlainReader.get(typeArray, headerArray);
    	PlainReader reader = PlainReader.get();

    	reader.setHeader(2, "SALES_COUNT");
    	// reader.setType(0, "datetime(dd-MM-yyyy HH:mm:ss)");
    	reader.setCommentString("..");
    	// reader.setFormatter(0, new RegExFormatter("^(\\d{4})-(\\d{1,2})-(\\d{1,2})$", "$3-$2-$1 00:00:00"));
    	// reader.setFormatter(0, value -> value.replaceAll("^(\\d{4})-(\\d{1,2})-(\\d{1,2})$", "$3-$2-$1 00:00:00"));
    	/*
    	reader.setFormatter(0, value -> { 
    		value = "s";
    		return value;
    	});*/
    	
    	table = reader.read(sampleFilePath);
    }
	
    @Test
    public void testRead(){
    	assertEquals(36, table.getRowCount());
    	assertEquals(3, table.getColumnCount());
    }
    
    @Test
    public void printTable(){
    	System.out.println(table.getIndexMap());
    	System.out.print(table.toString());
    	table.sort("Date", null);
    	System.out.println(table.getIndexMap());
    	System.out.print(table.toString());

    }
    
}
