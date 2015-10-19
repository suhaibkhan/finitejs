'use strict';

/**
 * Module for statistical functions.
 *
 * @exports stat
 */
var Stat = Java.type('com.finitejs.modules.math.stats.Stat');

var stat = {};

/**
 * Returns mean of the values.
 *
 * @param {...number|number[]} values
 */
stat.mean = function(values) {
    return Array.isArray(values) ?
    	Stat.mean(Java.to(values, "double[]")) : Stat.mean(Java.to(arguments, "double[]"));
};

/**
 * Return standard deviation of the values.
 *
 * @param {...number|number[]} values
 */
stat.std = function(values) {
    return Array.isArray(values) ?
    	Stat.std(Java.to(values, "double[]")) : Stat.std(Java.to(arguments, "double[]"));
};

module.exports = stat;

