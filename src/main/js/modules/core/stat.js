'use strict';

var Stat = Java.type('com.finitejs.modules.math.stats.Stat');

var stat = {};

stat.mean = function(values) {
    return Array.isArray(values) ?
    	Stat.mean(Java.to(values, "double[]")) : Stat.mean(Java.to(arguments, "double[]"));
};

stat.std = function(values) {
    return Array.isArray(values) ?
    	Stat.std(Java.to(values, "double[]")) : Stat.std(Java.to(arguments, "double[]"));
};

module.exports = stat;

