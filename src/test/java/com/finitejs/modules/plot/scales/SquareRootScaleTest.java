package com.finitejs.modules.plot.scales;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SquareRootScaleTest {

	private SquareRootScale sqrtScale = null;
	
	@Before
	public void setup(){
		sqrtScale = new SquareRootScale(new Double[]{0d, 100d}, new Double[]{0d, 10d});
	}
	
	@Test
	public void testConvert(){
		
		assertEquals(0, sqrtScale.convert(0d), 0);
		assertEquals(5, sqrtScale.convert(25d), 0);
		assertEquals(10, sqrtScale.convert(100d), 0);
	}
	
	@Test
	public void testInvert(){
		assertEquals(0, sqrtScale.invert(0d), 0);
		assertEquals(64, sqrtScale.invert(8d), 0);
		assertEquals(100, sqrtScale.invert(10d), 0);
	}
	
	@Test
	public void testGetTicks(){
		assertEquals(50, sqrtScale.getTicks(10)[5], 0);
	}
}
