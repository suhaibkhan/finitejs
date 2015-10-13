package com.finitejs.modules.read.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NumberTypeTest {
	
	@Test
	public void testGetType(){
		NumberType type = NumberType.getType();
		assertEquals("number", type.toString());
	}
	
	@Test
	public void testCheckAndGetType(){
		
		NumberType type = NumberType.checkAndGetType("10");
		assertEquals("number", type.toString());
		
		type = NumberType.checkAndGetType("10.555");
		assertEquals("number", type.toString());
		
		type = NumberType.checkAndGetType("11.sss");
		assertNull(type);
		
		type = NumberType.checkAndGetType("other");
		assertNull(type);
		
		type = NumberType.checkAndGetType("");
		assertNull(type);
		
		type = NumberType.checkAndGetType(null);
		assertNull(type);
	}
	
	@Test
	public void testEquals(){
		
		NumberType type1 = NumberType.checkAndGetType("10");
		NumberType type2 = NumberType.checkAndGetType("10.567");
		
		assertTrue(type1.equals(type2));
		
	}
	
	@Test
	public void testFormat(){
		
		NumberType type = NumberType.getType();
		
		assertEquals("10.0", type.format(10d));
		assertEquals("10.22", type.format(10.22));
		assertEquals("", type.format(null));
	}
	
	@Test
	public void testParse(){
		
		NumberType type = NumberType.getType();
		
		assertNull(type.parse(null));
		assertNull(type.parse(""));
		assertNull(type.parse("10.ssss"));
		assertNull(type.parse("other"));
		
		assertEquals(10d, type.parse("10"), 0.0);
		assertEquals(10.2567, type.parse("10.2567"), 0.0);
	}
	
	@Test
	public void testCompare(){
		NumberType type = NumberType.getType();
		
		assertEquals(-1, type.compareTo(null, 1d));
		assertEquals(1, type.compareTo(2d, null));
		assertEquals(0, type.compareTo(2d, 2d));
		assertEquals(1, type.compareTo(3d, 2d));
		assertEquals(-1, type.compareTo(2d, 3d));
	}
}
