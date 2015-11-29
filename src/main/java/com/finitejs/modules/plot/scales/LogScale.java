package com.finitejs.modules.plot.scales;

/**
 * Represents a logarithmic scale similar to linear scale, but 
 * logarithmic transform is applied to input domain before 
 * calculating output range.
 */
public class LogScale extends LinearScale{
	
	/** Constant for default base of the logarithmic scale. */
	public static final double DEFAULT_BASE = 10d;
	
	/** Input domain */
	private Double[] domain;
	
	/** Base of logarithmic scale. */
	private double base;
	
	public LogScale(){
		this(DEFAULT_BASE, null, null);
	}
	
	public LogScale(Double base){
		this(base, null, null);
	}
	
	public LogScale(Double[] domain, Double[] range){
		this(DEFAULT_BASE, domain, range);
	}
	
	public LogScale(Double base, Double[] domain, Double[] range){
		// log of domain is taken
		super(log(domain, base, domain[0] < 0), range);
		this.base = base;
		this.domain = domain;
	}
	
	@Override
	public Double convert(Double value) {
		return super.convert(log(value, base, domain[0] < 0));
	}
	
	@Override
	public Double invert(Double value){
		// inverse of log is calculated
		return pow(super.invert(value), base, domain[0] < 0);
	}
	
	@Override
	public void setDomain(Double[] domain) {
		super.setDomain(log(domain, base, domain[0] < 0));
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
	 * Returns base of the logarithmic scale.
	 * 
	 * @return base of the logarithmic scale
	 */
	public double getBase(){
		return base;
	}
	
	/**
	 * Set base of the logarithmic scale. If not set {@link DEFAULT_BASE} will be used.
	 * 
	 * @param base  base of the logarithmic scale
	 */
	public void setBase(double base){
		this.base = base;
	}
	
	/**
	 * Inverse of log function.
	 */
	private static double pow(double value, double base, boolean negativeDomain) {
		return negativeDomain ? -Math.pow(base, -value) : Math.pow(base, value);
	}
	
	/**
	 * Calculates log.
	 */
	private static double log(double value, double base, boolean negativeDomain){
		return (negativeDomain ? -Math.log(value > 0 ? 0 : -value) :
			Math.log(value < 0 ? 0 : value)) / Math.log(base);
	}
	
	/**
	 * Calculates log of an array.
	 */
	private static Double[] log(Double[] valueArray, double base, boolean negativeDomain){
		Double[] logValArray = new Double[valueArray.length];
		int i = 0;
		for (double value : valueArray){
			logValArray[i++] = log(value, base, negativeDomain);
		}
				
		return logValArray;
	}
}
