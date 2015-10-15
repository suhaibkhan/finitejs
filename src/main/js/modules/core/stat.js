var _stat = Java.type('com.finitejs.modules.math.stats.Stat');

var stat = {};

stat.mean = function(values) {
    return Array.isArray(values) ?
        _stat.mean(Java.to(values, "double[]")) : _stat.mean(Java.to(arguments, "double[]"));
};

stat.std = function(values) {
    return Array.isArray(values) ?
        _stat.std(Java.to(values, "double[]")) : _stat.std(Java.to(arguments, "double[]"));
};

module.exports = stat;

