package com.finitejs.modules.plot.scales;

import com.finitejs.modules.plot.Scale;

/**
 * Abstract class representing a scale with numeric domain and range.
 */
public abstract class NumericScale implements Scale<Double, Double> {

	/**
	 * Returns uniformly spaced human-readable tick values within 
	 * the extent of the input domain. The number of ticks depends on the
	 * input domain and given maxTicksCount. 
	 * 
	 * @param maxTicksCount  hint on number of ticks required
	 * @return uniformly spaced ticks within input domain
	 */
	public abstract double[] getTicks(int maxTicksCount);
	
	/**
	 * Converts {@code Double} array to {@code double} array.
	 * 
	 * @param objectArray  {@code Double} array
	 * @return {@code double} array
	 */
	public static double[] unbox(Double[] objectArray){
		double[] array = new double[objectArray.length];
		int i = 0;
		for (double v : objectArray){
			array[i++] = v;
		}
		return array;
	}
	
	/**
	 * Calculate uniformly spaced human readable tick values with 
	 * in specified range.
	 * 
	 * @param min  minimum
	 * @param max  maximum
	 * @param maxTicksCount  hint on number of ticks required
	 * @return uniformly spaced ticks within range
	 */
	public static double[] calculateTicks(double min, double max, int maxTicksCount){
		
		double domainRange = nice(max - min, false);
		double tickInterval = nice(domainRange / (maxTicksCount - 1), true);
		
		double minTick = Math.floor(min / tickInterval) * tickInterval;
		double maxTick = Math.ceil(max / tickInterval) * tickInterval + tickInterval/2; // inclusive of maxTick
		
		int noOfTicks = new Double(Math.ceil((maxTick - minTick)/tickInterval)).intValue();
		double[] tickArray = new double[noOfTicks];
		for (int i = 0; i < noOfTicks; i++){
			tickArray[i] = minTick + (i * tickInterval);
		}
		return tickArray;
	}
	
	/**
	 * Returns a nice number approximately equal to given number.
	 * Rounds the number if round = true or takes the ceiling if round = false.
	 * 
	 * @param number  number
	 * @param round  round the result, if true
	 * @return a "nice" number to be used for the data range
	 */
	private static double nice(double number, boolean round){
		
		double exponent;
		double fraction;
		double niceFraction;

	    exponent = Math.floor(Math.log10(number));
	    fraction = number / Math.pow(10, exponent);

		if (round) {
			if (fraction < 1.5){
				niceFraction = 1;
			}else if (fraction < 3){
				niceFraction = 2;
			}else if (fraction < 7){
				niceFraction = 5;
			}else{
				niceFraction = 10;
			}
		} else {
			if (fraction <= 1){
				niceFraction = 1;
			}else if (fraction <= 2){
				niceFraction = 2;
			}else if (fraction <= 5){
				niceFraction = 5;
			}else{
				niceFraction = 10;
			}
		}
	    return niceFraction * Math.pow(10, exponent);
	}
}
