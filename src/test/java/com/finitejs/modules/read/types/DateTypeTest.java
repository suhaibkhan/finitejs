package com.finitejs.modules.read.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DateTypeTest {

	private static final String[] dateFormats = {
		"y-M-d",
		"y/M/d",
		"M/d/y",
		"d-M-y"
	};
	
	private static final String[] sampleDateValues = {
		"2014-4-21",
		"2014/04/21",
		"5/1/2015",
		"1-05-2014"
	};
	
	@Test
	public void testGetType(){
		DateType type = DateType.getType("illegal");
		assertNull(type);
		
		type = DateType.getType("y-M-d");
		assertEquals("date(y-M-d)", type.toString());
		
		// not default format
		type = DateType.getType("d/M/y");
		assertEquals("date(d/M/y)", type.toString());
		
		type = DateType.getType("d/M/y'H'");
		assertEquals("date(d/M/y'H')", type.toString());
		
		type = DateType.getType("d/M/y'H'EEE");
		assertEquals("date(d/M/y'H'EEE)", type.toString());
		
		type = DateType.getType("d/M/y HH:mm:ss");
		assertNull(type);
		
		type = DateType.getType("d/M/y H:m:s");
		assertNull(type);
		
		type = DateType.getType("d/M/j");
		assertNull(type);
	}
	
	@Test
	public void testCheckAndGetType(){
		
		DateType type = DateType.checkAndGetType("illegal");
		assertNull(type);
		
		for (int i = 0; i < sampleDateValues.length; i++){
			type = DateType.checkAndGetType(sampleDateValues[i]);
			assertEquals(String.format("%s(%s)", "date", dateFormats[i]), type.toString());
		}
	}
	
	@Test
	public void testEquals(){
		DateType type1 = DateType.checkAndGetType(sampleDateValues[0]);
		DateType type2 = DateType.getType(dateFormats[0]);
		DateType type3 = DateType.checkAndGetType(sampleDateValues[1]);
		
		assertTrue(type1.equals(type2));
		assertFalse(type1.equals(type3));
	}
	
	@Test
	public void testFormatAndParse(){
		DateType type1 = DateType.checkAndGetType(sampleDateValues[0]);
		// custom format
		DateType type2 = DateType.getType("dd/MM/yyyy");
		
		assertEquals("2014-4-2", type1.format(type1.parse("2014-04-2")));
		assertEquals("2014-4-2", type1.format(type1.parse("2014-4-2")));
		assertEquals("21/04/2015", type2.format(type2.parse("21/04/2015")));
		assertEquals("", type2.format(type2.parse("21/4/2015")));
		assertEquals("", type2.format(type2.parse("21/04/15")));
		assertEquals("", type1.format(type1.parse("")));
		assertEquals("", type1.format(type1.parse(null)));
		assertEquals("", type1.format(null));
		assertNull(type1.parse(null));
		assertNull(type1.parse(""));
	}
}
