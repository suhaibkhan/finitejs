package com.finitejs.modules.read.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BooleanTypeTest {

	@Test
	public void testGetType(){
		BooleanType type = BooleanType.getType(null);
		assertEquals("boolean(TRUE_FALSE)", type.toString());
		
		type = BooleanType.getType("true_false");
		assertEquals("boolean(TRUE_FALSE)", type.toString());
		
		type = BooleanType.getType("yes_no");
		assertEquals("boolean(YES_NO)", type.toString());
		
		type = BooleanType.getType("illegal");
		assertNull(type);
	}
	
	@Test
	public void testCheckAndGetType(){
		
		BooleanType type = BooleanType.checkAndGetType("yes");
		assertEquals("boolean(YES_NO)", type.toString());
		
		type = BooleanType.checkAndGetType("no");
		assertEquals("boolean(YES_NO)", type.toString());
		
		type = BooleanType.checkAndGetType("true");
		assertEquals("boolean(TRUE_FALSE)", type.toString());
		
		type = BooleanType.checkAndGetType("false");
		assertEquals("boolean(TRUE_FALSE)", type.toString());
		
		type = BooleanType.checkAndGetType("other");
		assertNull(type);
	}
	
	@Test
	public void testEquals(){
		
		BooleanType type1 = BooleanType.checkAndGetType("yes");		
		BooleanType type2 = BooleanType.checkAndGetType("no");
		BooleanType type3 = BooleanType.checkAndGetType("true");
		
		assertTrue(type1.equals(type2));
		assertFalse(type1.equals(type3));
		
	}
	
	@Test
	public void testFormat(){
		
		BooleanType type1 = BooleanType.getType("true_false");
		BooleanType type2 = BooleanType.getType("yes_no");
		
		assertEquals("false", type1.format(false));
		assertEquals("true", type1.format(true));
		
		assertEquals("no", type2.format(false));
		assertEquals("yes", type2.format(true));
		
		assertEquals("", type2.format(null));
	}
	
	@Test
	public void testParse(){
		
		BooleanType type1 = BooleanType.getType("true_false");
		BooleanType type2 = BooleanType.getType("yes_no");
		
		assertTrue(type1.parse("true"));
		assertFalse(type1.parse("false"));
		
		assertTrue(type2.parse("yes"));
		assertFalse(type2.parse("no"));
		
		assertNull(type1.parse(""));
		assertNull(type2.parse(null));
		
		assertFalse(type1.parse("other"));
		assertFalse(type2.parse("other"));
	}
	
	@Test
	public void testCompare(){
		BooleanType type = BooleanType.getType("true_false");
		
		assertEquals(-1, type.compareTo(null, false));
		assertEquals(1, type.compareTo(false, null));
		assertEquals(0, type.compareTo(false, false));
		assertEquals(1, type.compareTo(true, false));
		assertEquals(-1, type.compareTo(false, true));
	}
	
}
