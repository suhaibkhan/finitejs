package com.finitejs.modules.plot.scales;

/**
 * Represents a exponential scale similar to linear scale, but 
 * exponential transform is applied to input domain before 
 * calculating output range.
 */
public class PowerScale extends LinearScale{
	
	/** Constant for default exponent of exponential scale. */
	public static final double DEFAULT_EXPONENT = 1d;
	
	/** Input domain. */
	private Double[] domain;
	
	/** Exponent of exponential scale. */
	private double exponent;
	
	public PowerScale(){
		this(DEFAULT_EXPONENT, null, null);
	}
	
	public PowerScale(Double exponent){
		this(exponent, null, null);
	}
	
	public PowerScale(Double[] domain, Double[] range){
		this(DEFAULT_EXPONENT, domain, range);
	}
	
	public PowerScale(Double exponent, Double[] domain, Double[] range){
		// exponential transform is applied to input domain
		super(pow(domain, exponent), range);
		this.exponent = exponent;
		this.domain = domain;
	}
	
	@Override
	public Double convert(Double value) {
		return super.convert(pow(value, exponent));
	}
	
	@Override
	public Double invert(Double value){
		return invPow(super.invert(value), exponent);
	}
	
	@Override
	public void setDomain(Double[] domain) {
		// exponential transform is applied to input domain
		super.setDomain(pow(domain, exponent));
		this.domain = domain;
	}

	@Override
	public Double[] getDomain() {
		return domain;
	}

	@Override
	public double[] getTicks(int maxTicksCount) {
		double min = domain[0]; // first
		double max = domain[domain.length - 1]; // last
		if (min > max){
			// swap
			max = domain[0];
			min = domain[domain.length - 1];
		}
		return calculateTicks(min, max, maxTicksCount);
	}
	
	/**
	 * Returns exponent of the exponential scale.
	 * 
	 * @return exponent
	 */
	public double getExponent() {
		return exponent;
	}

	/**
	 * Set exponent of the exponential scale.
	 * 
	 * @param exponent  exponent to set
	 */
	public void setExponent(double exponent) {
		this.exponent = exponent;
	}

	/** Calculates power */
	private static double pow(double value, double exponent) {
	    return value < 0 ? -Math.pow(-value, exponent) : Math.pow(value, exponent);
	}
	
	/** Inverse of power */
	private static double invPow(double value, double exponent) {
		return value < 0 ? -Math.pow(-value, 1 / exponent) : Math.pow(value, 1 / exponent);
	}
	
	/** Calculates power of array */
	private static Double[] pow(Double[] valueArray, double exponent) {
		Double[] powValArray = new Double[valueArray.length];
		int i = 0;
		for (double value : valueArray){
			powValArray[i++] = pow(value, exponent);
		}
				
		return powValArray;
	}
}
