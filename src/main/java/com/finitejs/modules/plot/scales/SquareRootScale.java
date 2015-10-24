package com.finitejs.modules.plot.scales;

/**
 * Represents a square root scale similar to exponential scale, but 
 * square root transform is applied to input domain before 
 * calculating output range.
 */
public class SquareRootScale extends PowerScale {

	private static final double EXPONENT = 0.5;
	
	public SquareRootScale(){
		super(EXPONENT);
	}
	
	public SquareRootScale(Double[] domain, Double[] range){
		super(EXPONENT, domain, range);
	}
	
	@Override
	public void setExponent(double exponent){
	    throw new UnsupportedOperationException("Cannot change exponent of SquareRootScale.");
	}
}
