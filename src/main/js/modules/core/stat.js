'use strict';

// java imports
var StatUtils = Java.type('org.apache.commons.math3.stat.StatUtils');

/**
 * Module for statistical functions.
 *
 * @exports stat
 */
var stat = {};

/**
 * Returns mean of the values.
 *
 * @param {...Number|Number[]} values
 * @returns {Number} mean
 */
stat.mean = function(values) {
    return Array.isArray(values) ?
    	StatUtils.mean(Java.to(values, "double[]")) : StatUtils.mean(Java.to(arguments, "double[]"));
};

/**
 * Returns mode of the values. The mode is the most frequently occurring value in the sample.
 *
 * @param {...Number|Number[]} values
 * @returns {Number[]} modes, array of array of the most frequently 
 * occurring element(s) sorted in ascending order
 */
stat.mode = function(values) {
    return Array.isArray(values) ?
    	Java.from(StatUtils.mode(Java.to(values, "double[]"))) : 
    		Java.from(StatUtils.mode(Java.to(arguments, "double[]")));
};

/**
 * Returns maximum of the values.
 *
 * @param {...Number|Number[]} values
 * @returns {Number} maximum
 */
stat.max = function(values) {
    return Array.isArray(values) ?
    	StatUtils.max(Java.to(values, "double[]")) : StatUtils.max(Java.to(arguments, "double[]"));
};

/**
 * Returns minimum of the values.
 *
 * @param {...Number|Number[]} values
 * @returns {Number} minimum
 */
stat.min = function(values) {
    return Array.isArray(values) ?
    	StatUtils.min(Java.to(values, "double[]")) : StatUtils.min(Java.to(arguments, "double[]"));
};

/**
 * Returns sum of the values.
 *
 * @param {...Number|Number[]} values
 * @returns {Number} sum
 */
stat.sum = function(values) {
    return Array.isArray(values) ?
    	StatUtils.sum(Java.to(values, "double[]")) : StatUtils.sum(Java.to(arguments, "double[]"));
};

/**
 * Returns sum of the natural logs of values.
 *
 * @param {...Number|Number[]} values
 * @returns {Number} sum of the natural logs
 */
stat.sumLog = function(values) {
    return Array.isArray(values) ?
    	StatUtils.sumLog(Java.to(values, "double[]")) : StatUtils.sumLog(Java.to(arguments, "double[]"));
};

/**
 * Returns sum of the squares of values.
 *
 * @param {...Number|Number[]} values
 * @returns {Number} sum of the squares
 */
stat.sumSq = function(values) {
    return Array.isArray(values) ?
    	StatUtils.sumSq(Java.to(values, "double[]")) : StatUtils.sumSq(Java.to(arguments, "double[]"));
};

/**
 * Returns product of the values.
 *
 * @param {...Number|Number[]} values
 * @returns {Number} product
 */
stat.product = function(values) {
    return Array.isArray(values) ?
    	StatUtils.product(Java.to(values, "double[]")) : StatUtils.product(Java.to(arguments, "double[]"));
};

/**
 * Returns variance of the values.
 * 
 * @param {...Number|Number[]} values
 * @returns {Number} variance
 */
stat.variance = function(values) {
	return Array.isArray(values) ?
	    StatUtils.variance(Java.to(values, "double[]")) : StatUtils.variance(Java.to(arguments, "double[]"));
};

/**
 * Returns standard deviation of the values.
 *
 * @param {...Number|Number[]} values
 * @returns {Number} standard deviation
 */
stat.sd = function(values) {
    return Array.isArray(values) ?
    	Math.sqrt(stat.variance(values)) : 
    		Math.sqrt(stat.variance(Array.prototype.slice.call(arguments).sort()));
};

/**
 * Normalizes (standardize) the values, so it is has a mean of 0 and a standard deviation of 1.
 * 
 * @param {...Number|Number[]} values
 * @returns {Number[]} normalized values
 */
stat.normalize = function(values) {
	return Array.isArray(values) ?
		Java.from(StatUtils.normalize(Java.to(values, "double[]"))) : 
			Java.from(StatUtils.normalize(Java.to(arguments, "double[]")));
};

/**
 * Returns an estimate of the p<sup>th</sup> percentile of the values.
 * 
 * @param {...Number|Number[]} values
 * @param {Number} p - the percentile value to compute
 * @returns {Number} the percentile value
 */
stat.percentile = function(values, p) {
	
	if (!Array.isArray(values)){
		throw 'Illegal argument';
	}
	
	return StatUtils.percentile(Java.to(values, "double[]"), p);
};

module.exports = stat;

