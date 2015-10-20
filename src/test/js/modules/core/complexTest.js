'use strict';

var Complex = require('complex');

exports.test = {
    testAbs: function() {
        assert.equals(2.23606797749979, new Complex(1,2).abs());
    },

    testConjugate: function() {
        var complexNumber = new Complex(1,2).conjugate();

        assert.equals(1, complexNumber.real);
        assert.equals(-2, complexNumber.imaginary);
    }
};