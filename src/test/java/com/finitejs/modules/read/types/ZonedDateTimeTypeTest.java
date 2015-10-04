package com.finitejs.modules.read.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ZonedDateTimeTypeTest {

	private static final String[] zonedDateTimeFormats = {
		"y-M-d'T'HH:mm:ssXXX",
		"y-M-d'T'HH:mm:ssZZZ",
		"EEE MMM d HH:mm:ss zzz y",
		"EEE MMM d HH:mm:ss zzz y"
	};
	
	private String[] sampleZonedDateTimeValues = {
		"2014-04-20T12:13:14+05:30",
		"2014-04-20T12:13:14+0530",
		"Tue Oct 06 20:10:00 IST 2015",
		"Tue Oct 06 20:10:00 Asia/Calcutta 2015"
	};
	
	@Test
	public void testGetType(){
		ZonedDateTimeType type = ZonedDateTimeType.getType("illegal");
		assertNull(type);
		
		type = ZonedDateTimeType.getType("y-M-d'T'HH:mm:ssXXX");
		assertEquals("zoneddatetime(y-M-d'T'HH:mm:ssXXX)", type.toString());
		
		// not default format
		type = ZonedDateTimeType.getType("d/M/y HH:mm:ss zzz");
		assertEquals("zoneddatetime(d/M/y HH:mm:ss zzz)", type.toString());
		
		type = ZonedDateTimeType.getType("d/M/j h:m:s zzz");
		assertNull(type);
	}
	
	@Test
	public void testCheckAndGetType(){
		
		ZonedDateTimeType type = ZonedDateTimeType.checkAndGetType("illegal");
		assertNull(type);
		
		for (int i = 0; i < sampleZonedDateTimeValues.length; i++){
			type = ZonedDateTimeType.checkAndGetType(sampleZonedDateTimeValues[i]);
			assertEquals(String.format("%s(%s)", "zoneddatetime", zonedDateTimeFormats[i]), type.toString());
		}
	}
	
	@Test
	public void testEquals(){
		ZonedDateTimeType type1 = ZonedDateTimeType.checkAndGetType(sampleZonedDateTimeValues[0]);
		ZonedDateTimeType type2 = ZonedDateTimeType.getType(zonedDateTimeFormats[0]);
		ZonedDateTimeType type3 = ZonedDateTimeType.checkAndGetType(sampleZonedDateTimeValues[1]);
		
		assertTrue(type1.equals(type2));
		assertFalse(type1.equals(type3));
	}
	
	@Test
	public void testFormatAndParse(){
		ZonedDateTimeType type1 = ZonedDateTimeType.checkAndGetType(sampleZonedDateTimeValues[0]);
		// custom format
		ZonedDateTimeType type2 = ZonedDateTimeType.getType("dd/MM/yyyy HH:mm:ss zzz");
		
		assertEquals("2014-4-2T12:13:14+05:00", type1.format(type1.parse("2014-04-2T12:13:14+05:00")));
		assertEquals("21/04/2015 23:23:23 IST", type2.format(type2.parse("21/04/2015 23:23:23 Asia/Calcutta")));
		assertEquals("", type2.format(type2.parse("21/4/2015 23:23:23 IST")));
		assertEquals("", type2.format(type2.parse("21/04/2015 23:23:23+05:00")));
		assertEquals("", type1.format(type1.parse("")));
		assertEquals("", type1.format(type1.parse(null)));
		assertEquals("", type1.format(null));
		assertNull(type1.parse(null));
		assertNull(type1.parse(""));
	}
}
