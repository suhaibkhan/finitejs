var _stat = Java.type('com.finitejs.modules.math.stats.Stat');

var stat = {};

stat.mean = function(values) {
    return _stat.mean(Java.to(values, "double[]"));
};

stat.std = function(values) {
    return _stat.std(Java.to(values, "double[]"));
};

module.exports = stat;

