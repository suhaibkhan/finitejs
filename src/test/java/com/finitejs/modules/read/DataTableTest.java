package com.finitejs.modules.read;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DataTableTest {

	private DataTable table = null;
	
	@Before
    public void setup() throws IOException{
		String sampleFilePath = "samples/sample.csv";
		PlainReader reader = PlainReader.get();
		table = reader.read(sampleFilePath);
	}
	
	@Test
	public void testRowCount(){
		assertEquals(36, table.getRowCount());
	}
	
	@Test
	public void testColumnCount(){
		assertEquals(3, table.getColumnCount());
	}
	
	@Test
	public void testGetColumnList(){
		assertEquals(3, table.getColumnList().size());
	}
	
	@Test
	public void testGetIndexMap(){
		assertArrayEquals(new Integer[]{3}, table.getIndexMap().get("3").toArray());
	}
	
	@Test
	public void testGetHeaderList(){
		assertArrayEquals(new String[]{"Date", "Item", "Sales Count"}, table.getHeaderList().toArray());
	}
	
	@Test
	public void testGetTypeList(){
		assertEquals(ColumnType.getType("date(y-M-d)"), table.getTypeList().get(0));
	}
	
	@Test
	public void testGetTypeStringList(){
		assertArrayEquals(new String[]{"date(y-M-d)", "string", "number"}, table.getTypeStringList().toArray());
	}
	
	@Test
	public void testAddRow(){
		List<String> newRow = new ArrayList<>();
		newRow.add("2014-5-5");
		newRow.add("Graphics Card");
		newRow.add("1");
		table.addRow(newRow);
		assertEquals(37, table.getRowCount());
	}
	
	@Test
	public void testIncompleteAddRow(){
		List<String> newRow = new ArrayList<>();
		newRow.add("2014-5-5");
		newRow.add("Graphics Card");
		table.addRow(newRow);
		assertEquals(37, table.getRowCount());
	}
	
	@Test
	public void testAddRowWithNull(){
		List<String> newRow = new ArrayList<>();
		newRow.add("2014-1-10");
		newRow.add(null);
		newRow.add("1");

		table.addRow(newRow);
		assertEquals(37, table.getRowCount());

	}
	
	@Test
	public void testAddColumn(){
		// incomplete column data
		table.addColumn("Profit %", ColumnType.getType("number"), Arrays.asList("1","3","10"));
		assertEquals(4, table.getColumnCount());
		// System.out.println(table);
	}
	
	@Test
	public void testAddColumnWithTypeString(){
		// empty data
		table.addColumn("Profit %", "number", null);
		assertEquals(4, table.getColumnCount());
	}
	
	@Test
	public void testAscSort(){
		// default direction ascending
		table.sort("Sales Count", null);
		assertArrayEquals(new String[]{"2014-1-9", "Printer", "2"}, table.getFormattedRow(0).toArray());
	}
	
	@Test
	public void testDescSort(){
		table.sort("Date", "desc");
		assertArrayEquals(new String[]{"2014-1-12", "CPU", "20"}, table.getFormattedRow(0).toArray());
	}
	
	@Test
	public void testGetRow(){
		assertEquals(10d, table.getRow(0).get(2));
		assertEquals(3, table.getRow(0).size());
	}
	
	@Test
	public void testGetFormattedRow(){
		assertEquals("2014-1-1", table.getFormattedRow(0).get(0));
		assertEquals(3, table.getFormattedRow(0).size());
	}
	
	@Test
	public void testGetColumn(){
		assertEquals(9d, table.getColumn(2).get(1));
		assertEquals(36, table.getColumn(2).size());
	}
	
	@Test
	public void testGetFormattedColumn(){
		assertEquals("9", table.getFormattedColumn(2).get(1));
		assertEquals(36, table.getFormattedColumn(2).size());
	}
}
