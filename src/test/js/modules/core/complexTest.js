'use strict';

var Complex = require('complex');

exports.test = {
    testAbs: function() {
        assert.equals(2.23606797749979, new Complex(1,2).abs());
    }
};