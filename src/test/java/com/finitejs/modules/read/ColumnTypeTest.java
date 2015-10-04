package com.finitejs.modules.read;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ColumnTypeTest {

	@Test
	public void testGetType(){
		
		ColumnType<?> type = ColumnType.getType("number");
		assertEquals("number", type.toString());
		
		type = ColumnType.getType("boolean ");
		assertEquals("boolean(TRUE_FALSE)", type.toString());
		
		type = ColumnType.getType(" Boolean (true_false) ");
		assertEquals("boolean(TRUE_FALSE)", type.toString());
		
		type = ColumnType.getType("boolean( yes_no )");
		assertEquals("boolean(YES_NO)", type.toString());
		
		type = ColumnType.getType("boolean( Yes_No)");
		assertEquals("boolean(YES_NO)", type.toString());
		
		// without format
		type = ColumnType.getType("date");
		assertNull(type);
		
		type = ColumnType.getType("date (M/d/y)");
		assertEquals("date(M/d/y)", type.toString());
		
		type = ColumnType.getType("time( H:m )");
		assertEquals("time(H:m)", type.toString());
		
		type = ColumnType.getType("datetime(d/M/y H:m)");
		assertEquals("datetime(d/M/y H:m)", type.toString());
		
		type = ColumnType.getType("zoneddatetime(d/M/y H:m zzz)");
		assertEquals("zoneddatetime(d/M/y H:m zzz)", type.toString());
		
		type = ColumnType.getType("string");
		assertEquals("string", type.toString());
		
		type = ColumnType.getType("unknown");
		assertNull(type);
		
		type = ColumnType.getType("");
		assertNull(type);
		
		type = ColumnType.getType(null);
		assertNull(type);
	}
	
	@Test
	public void testFindType(){
		ColumnType<?> type = ColumnType.findType("10");
		assertEquals("number", type.toString());
		
		type = ColumnType.findType("10.555");
		assertEquals("number", type.toString());
		
		type = ColumnType.findType("yes");
		assertEquals("boolean(YES_NO)", type.toString());
		
		type = ColumnType.findType("false");
		assertEquals("boolean(TRUE_FALSE)", type.toString());
		
		type = ColumnType.findType("falseeee");
		assertEquals("string", type.toString());
		
		type = ColumnType.findType("2014-04-20T12:13:14+05:30");
		assertEquals("zoneddatetime(y-M-d'T'HH:mm:ssXXX)", type.toString());
		
		type = ColumnType.findType("2014-04-20 12:13:14");
		assertEquals("datetime(y-M-d HH:mm:ss)", type.toString());
		
		type = ColumnType.findType("2014-04-20");
		assertEquals("date(y-M-d)", type.toString());
		
		type = ColumnType.findType("12:13:14");
		assertEquals("time(HH:mm:ss)", type.toString());
		
		type = ColumnType.findType("");
		assertNull(type);
		
		type = ColumnType.findType(null);
		assertNull(type);
	}
	
}
