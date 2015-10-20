'use strict';

// java imports
var Stat = Java.type('com.finitejs.modules.math.stats.Stat');

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
    	Stat.mean(Java.to(values, "double[]")) : Stat.mean(Java.to(arguments, "double[]"));
};

/**
 * Returns standard deviation of the values.
 *
 * @param {...Number|Number[]} values
 * @returns {Number} standard deviation
 */
stat.std = function(values) {
    return Array.isArray(values) ?
    	Stat.std(Java.to(values, "double[]")) : Stat.std(Java.to(arguments, "double[]"));
};

module.exports = stat;

