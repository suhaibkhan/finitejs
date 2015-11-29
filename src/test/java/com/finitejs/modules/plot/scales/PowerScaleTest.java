package com.finitejs.modules.plot.scales;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PowerScaleTest {

	private PowerScale powScale = null;
	
	@Before
	public void setup(){
		powScale = new PowerScale(2d, new Double[]{0d, 10d}, new Double[]{0d, 100d});
	}
	
	@Test
	public void testConvert(){
		
		assertEquals(0, powScale.convert(0d), 0);
		assertEquals(25, powScale.convert(5d), 0);
		assertEquals(100, powScale.convert(10d), 0);
	}
	
	@Test
	public void testInvert(){
		assertEquals(0, powScale.invert(0d), 0);
		assertEquals(8, powScale.invert(64d), 0);
		assertEquals(10, powScale.invert(100d), 0);
	}
	
	@Test
	public void testGetTicks(){
		assertEquals(5, powScale.getTicks(10)[5], 0);
	}
}
