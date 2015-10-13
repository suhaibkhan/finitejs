package com.finitejs.modules.read.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringTypeTest {

	@Test
	public void testGetType(){
		StringType type = StringType.getType();
		assertEquals("string", type.toString());
	}
	
	@Test
	public void testEquals(){
		
		StringType type1 = StringType.getType();
		StringType type2 = StringType.getType();
		
		assertTrue(type1.equals(type2));
		
	}
	
	@Test
	public void testFormat(){
		
		StringType type = StringType.getType();
		
		assertEquals(type.format(""), "");
		assertEquals(type.format("some string"), "some string");
		assertEquals(type.format(null), "");
	}
	
	@Test
	public void testParse(){
		
		StringType type = StringType.getType();
		
		assertEquals(type.parse(""), "");
		assertEquals(type.parse("some string"), "some string");
		assertNull(type.parse(null));
	}
	
	@Test
	public void testCompare(){
		StringType type = StringType.getType();
		
		assertEquals(-1, type.compareTo(null, "s"));
		assertEquals(1, type.compareTo("s", null));
		assertEquals(0, type.compareTo("ss", "ss"));
		assertEquals(1, type.compareTo("ab", "aa"));
		assertEquals(-1, type.compareTo("aa", "ab"));
	}
	
}
