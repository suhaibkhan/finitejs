package com.finitejs.modules.plot.scales;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import com.finitejs.modules.plot.Scale;

/**
 * Represents a linear scale, which maps a continuous input domain to 
 * continuous output range.
 */
public class LinearScale implements Scale<Double, Double>{

	/** input domain */
	private Double[] domain;
	
	/** output range */
	private Double[] range;
	
	/** linear interpolation function */
	private PolynomialSplineFunction interpFunction;
	
	/** linear interpolation function for invert mapping */
	private PolynomialSplineFunction invertInterpFunction;
	
	public LinearScale(){
		this(null, null);
	}
	
	public LinearScale(Double[] domain, Double[] range){
		this.domain = domain;
		this.range = range;
		generateInterpFunctions();
	}
	
	@Override
	public Double convert(Double value) {
		if (domain == null){
			throw new IllegalStateException("domain cannot be empty");
		}
		
		if (range == null){
			throw new IllegalStateException("range cannot be empty");
		}
		
		return interpFunction.value(value);
	}

	@Override
	public Double invert(Double value) {
		
		if (domain == null){
			throw new IllegalStateException("domain cannot be empty");
		}
		
		if (range == null){
			throw new IllegalStateException("range cannot be empty");
		}
		
		return invertInterpFunction.value(value);
	}

	@Override
	public void setDomain(Double[] domain) {
		this.domain = domain;
		generateInterpFunctions();
	}

	@Override
	public Double[] getDomain() {
		return domain;
	}

	@Override
	public void setRange(Double[] range) {
		this.range = range;
		generateInterpFunctions();
	}

	@Override
	public Double[] getRange() {
		return range;
	}
	
	private void generateInterpFunctions(){
		if (domain != null && range != null){
			// re initaite interp function
			interpFunction = new LinearInterpolator().interpolate(unbox(domain), unbox(range));
			invertInterpFunction = new LinearInterpolator().interpolate(unbox(range), unbox(domain));
		}
	}
	
	/**
	 * Converts {@code Double} array to {@code double} array.
	 * 
	 * @param objectArray  {@code Double} array
	 * @return {@code double} array
	 */
	protected static double[] unbox(Double[] objectArray){
		double[] array = new double[objectArray.length];
		int i = 0;
		for (double v : objectArray){
			array[i++] = v;
		}
		return array;
	}

}
