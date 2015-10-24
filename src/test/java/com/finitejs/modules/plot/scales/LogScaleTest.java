package com.finitejs.modules.plot.scales;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogScaleTest {

	private LogScale logScale = null;
	
	@Before
	public void setup(){
		logScale = new LogScale(new Double[]{1d, 10d}, new Double[]{0d, 100d});
	}
	
	@Test
	public void testConvert(){
		
		assertEquals(0, logScale.convert(1d), 0);
		assertEquals(69.89700043360187, logScale.convert(5d), 0);
		assertEquals(100, logScale.convert(10d), 0);
	}
	
	@Test
	public void testInvert(){
		assertEquals(1, logScale.invert(0d), 0);
		assertEquals(3.1622776601683795, logScale.invert(50d), 0);
		assertEquals(10, logScale.invert(100d), 0);
	}
	
}
