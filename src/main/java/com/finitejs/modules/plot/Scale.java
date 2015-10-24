package com.finitejs.modules.plot;

/**
 * Represents a scale. Scales provide mapping between a set of input domain values
 * and output range values.
 *
 * @param <T1>  scale domain type
 * @param <T2>  scale range type
 */
public interface Scale<T1, T2> {

	/**
	 * Returns value in output range corresponding to the specified value in input domain.
	 * 
	 * @param value  value in the input domain
	 * @return value in the output range
	 */
	public T2 convert(T1 value);
	
	/**
	 * Returns value in input domain corresponding to the specified value in output range.
	 * 
	 * @param value  value in the output range
	 * @return value in the input domain
	 */
	public T1 invert(T2 value);
	
	/**
	 * Set the input domain.
	 * 
	 * @param domain  input domain
	 */
	public void setDomain(T1[] domain);
	
	/**
	 * Returns the input domain.
	 * 
	 * @return input domain
	 */
	public T1[] getDomain();
	
	/**
	 * Set the output range.
	 * 
	 * @param range  output range
	 */
	public void setRange(T2[] range);
	
	/**
	 * Returns the output range.
	 *  
	 * @return output range
	 */
	public T2[] getRange();
}
