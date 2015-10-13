var _math = Java.type('java.lang.Math');

var math = {};

math.abs = function(a) {
    return _math.abs(a);
};

math.max = function(a, b) {
    return _math.max(a, b);
};

module.exports = math;



