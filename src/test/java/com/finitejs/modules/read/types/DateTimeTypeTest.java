package com.finitejs.modules.read.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DateTimeTypeTest {
	
	private String[] dateTimeFormats = {
			
		"y-M-d hh:mm:ss a",
		"y-M-d HH:mm:ss",
		"y-M-d hh:mm a",
		"y-M-d HH:mm",
		
		"y/M/d hh:mm:ss a",
		"y/M/d HH:mm:ss",
		"y/M/d hh:mm a",
		"y/M/d HH:mm",
		
		"M/d/y hh:mm:ss a",
		"M/d/y HH:mm:ss",
		"M/d/y hh:mm a",
		"M/d/y HH:mm",
		
		"d-M-y hh:mm:ss a",
		"d-M-y HH:mm:ss",
		"d-M-y hh:mm a",
		"d-M-y HH:mm",
		
		"d-MMM-y hh.mm.ss.SSS a",
		"d-MMM-y hh.mm.ss.SSSSSSSSS a",
		"d-MMM-y hh.mm.ss.SSSSSSSSS a"		
	
	};
	
	private String[] sampleDateTimeValues = {
			
		"2014-04-21 01:12:23 PM",
		"2014-4-21 13:12:23",
		"2014-4-21 03:12 PM",
		"2014-04-21 15:12",
		
		"2014/04/21 01:12:23 PM",
		"2014/4/21 13:12:23",
		"2014/4/21 03:12 PM",
		"2014/04/21 15:12",
		
		"04/21/2014 01:12:23 pm",
		"4/21/2014 13:12:23",
		"4/21/2014 03:12 PM",
		"04/21/2014 15:12",
		
		"20-04-2015 01:12:23 PM",
		"20-4-2015 13:12:23",
		"20-4-2015 03:12 PM",
		"20-04-2015 15:12",
		
		"21-Jan-15 10.10.10.123 PM",
		"21-Jan-15 10.10.10.123456789 PM",
		"21-Jan-2015 10.10.10.123456789 PM",
	};
	
	@Test
	public void testGetType(){
		DateTimeType type = DateTimeType.getType("illegal");
		assertNull(type);
		
		type = DateTimeType.getType("y-M-d HH:mm:ss");
		assertEquals("datetime(y-M-d HH:mm:ss)", type.toString());
		
		// not default format
		type = DateTimeType.getType("d/M/y HH:mm:ss");
		assertEquals("datetime(d/M/y HH:mm:ss)", type.toString());
		
		type = DateTimeType.getType("d/M/y'z'HH:mm:ss");
		assertEquals("datetime(d/M/y'z'HH:mm:ss)", type.toString());
		
		type = DateTimeType.getType("d/M/y HH:mm:ss zzz");
		assertNull(type);
		
		type = DateTimeType.getType("d/M/j h:m:s");
		assertNull(type);
	}
	
	@Test
	public void testCheckAndGetType(){
		
		DateTimeType type = DateTimeType.checkAndGetType("illegal");
		assertNull(type);
		
		for (int i = 0; i < sampleDateTimeValues.length; i++){
			type = DateTimeType.checkAndGetType(sampleDateTimeValues[i]);
			assertEquals(String.format("%s(%s)", "datetime", dateTimeFormats[i]), type.toString());
		}
	}
	
	@Test
	public void testEquals(){
		DateTimeType type1 = DateTimeType.checkAndGetType(sampleDateTimeValues[0]);
		DateTimeType type2 = DateTimeType.getType(dateTimeFormats[0]);
		DateTimeType type3 = DateTimeType.checkAndGetType(sampleDateTimeValues[1]);
		
		assertTrue(type1.equals(type2));
		assertFalse(type1.equals(type3));
	}
	
	@Test
	public void testFormatAndParse(){
		DateTimeType type1 = DateTimeType.checkAndGetType(sampleDateTimeValues[0]);
		// custom format
		DateTimeType type2 = DateTimeType.getType("dd/MM/yyyy HH:mm:ss");
		
		assertEquals("2014-4-21 01:12:23 PM", type1.format(type1.parse("2014-04-21 01:12:23 PM")));
		assertEquals("21/04/2015 23:23:23", type2.format(type2.parse("21/04/2015 23:23:23")));
		assertEquals("", type2.format(type2.parse("21/4/2015 23:23:23")));
		assertEquals("", type1.format(type1.parse("")));
		assertEquals("", type1.format(type1.parse(null)));
		assertEquals("", type1.format(null));
		assertNull(type1.parse(null));
		assertNull(type1.parse(""));
	}
	
}
