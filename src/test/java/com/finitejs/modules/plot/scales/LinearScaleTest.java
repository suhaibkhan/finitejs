package com.finitejs.modules.plot.scales;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinearScaleTest {

	private LinearScale linearScale = null;
	
	@Before
	public void setup(){
		linearScale = new LinearScale(new Double[]{0d, 5d, 10d}, new Double[]{0d, 50d, 100d});
	}
	
	@Test
	public void testConvert(){
		
		assertEquals(0, linearScale.convert(0d), 0);
		assertEquals(50, linearScale.convert(5d), 0);
		assertEquals(100, linearScale.convert(10d), 0);
	}
	
	@Test
	public void testInvert(){
		assertEquals(0, linearScale.invert(0d), 0);
		assertEquals(5, linearScale.invert(50d), 0);
		assertEquals(10, linearScale.invert(100d), 0);
	}
	
}
