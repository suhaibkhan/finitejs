'use strict';

/**
 * Module for complex number operations.
 *
 * @class
 */
function Complex(real, imaginary){
    var CommonsComplex = Java.type('org.apache.commons.math3.complex.Complex');
    this._complex = new CommonsComplex(real, imaginary);
}

Complex.prototype = {

    real: function() {
        return this._complex.getReal();
    },

    imaginary: function() {
        return this._complex.getImaginary();
    },

    abs: function() {
        return this._complex.abs();
    },

    conjugate: function() {
        var conjugate =  this._complex.conjugate();
        return new Complex(conjugate.getReal(), conjugate.getImaginary());
    },

    toString: function() {
        var real = this._complex.getReal();
        var imaginary = this._complex.getImaginary();

        var SIGN = (imaginary>0) ? "+" : "";

        return "(" + real + SIGN + imaginary + "i" + ")";
    }

};

module.exports = Complex;