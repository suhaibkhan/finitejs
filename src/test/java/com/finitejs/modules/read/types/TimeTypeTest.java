package com.finitejs.modules.read.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeTypeTest {

	private static final String[] timeFormats = {
		"hh:mm:ss a",
		"HH:mm:ss",
		"hh:mm a",
		"HH:mm"
	};
	
	private static final String[] sampleTimeValues = {
		"10:20:00 am",
		"22:20:00",
		"01:20 PM",
		"13:20"
	};
	
	@Test
	public void testGetType(){
		TimeType type = TimeType.getType("illegal");
		assertNull(type);
		
		type = TimeType.getType("hh:mm:ss a");
		assertEquals("time(hh:mm:ss a)", type.toString());
		
		// not default format
		type = TimeType.getType("H:m");
		assertEquals("time(H:m)", type.toString());
		
		type = TimeType.getType("H:m'd':s");
		assertEquals("time(H:m'd':s)", type.toString());
		
		type = TimeType.getType("d/M/y H:m");
		assertNull(type);
		
		type = TimeType.getType("H:j");
		assertNull(type);
	}
	
	@Test
	public void testCheckAndGetType(){
		
		TimeType type = TimeType.checkAndGetType("illegal");
		assertNull(type);
		
		for (int i = 0; i < sampleTimeValues.length; i++){
			type = TimeType.checkAndGetType(sampleTimeValues[i]);
			assertEquals(String.format("%s(%s)", "time", timeFormats[i]), type.toString());
		}
	}
	
	@Test
	public void testEquals(){
		TimeType type1 = TimeType.checkAndGetType(sampleTimeValues[0]);
		TimeType type2 = TimeType.getType(timeFormats[0]);
		TimeType type3 = TimeType.checkAndGetType(sampleTimeValues[1]);
		
		assertTrue(type1.equals(type2));
		assertFalse(type1.equals(type3));
	}
	
	@Test
	public void testFormatAndParse(){
		TimeType type1 = TimeType.checkAndGetType(sampleTimeValues[0]);
		// custom format
		TimeType type2 = TimeType.getType("H:m");
		
		assertEquals("01:12:01 PM", type1.format(type1.parse("01:12:01 PM")));
		assertEquals("01:12:01 PM", type1.format(type1.parse("01:12:01 pm")));
		assertEquals("", type1.format(type1.parse("01:12:1 pm")));
		
		assertEquals("1:1", type2.format(type2.parse("01:01")));
		assertEquals("1:1", type2.format(type2.parse("1:1")));
		
		assertEquals("", type1.format(type1.parse("")));
		assertEquals("", type1.format(type1.parse(null)));
		assertEquals("", type1.format(null));
		assertNull(type1.parse(null));
		assertNull(type1.parse(""));
	}
}
