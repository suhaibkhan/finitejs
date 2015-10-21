'use strict';

var CommonsComplex = Java.type('org.apache.commons.math3.complex.Complex');

/**
 * Module for complex number operations.
 *
 * @class
 */
function Complex(real, imaginary){
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

    negate: function() {
        var negated = this._complex.negate();
        return new Complex(negated.getReal(), negated.getImaginary());
    },

    add: function(addend) {
        var sum = this._complex.add(
            new CommonsComplex(
                addend.real(),
                addend.imaginary()
            )
        );

        return new Complex(sum.getReal(), sum.getImaginary());
    },

    subtract: function(subtrahend) {
        var difference = this._complex.subtract(
            new CommonsComplex(
                subtrahend.real(),
                subtrahend.imaginary()
            )
        );

        return new Complex(difference.getReal(), difference.getImaginary());
    },

    multiply: function(factor) {
        var product = this._complex.multiply(
            new CommonsComplex(
                factor.real(),
                factor.imaginary()
            )
        );

        return new Complex(product.getReal(), product.getImaginary());
    },

    toString: function() {
        var real = this._complex.getReal();
        var imaginary = this._complex.getImaginary();

        var SIGN = (imaginary>0) ? "+" : "";

        return "(" + real + SIGN + imaginary + "i" + ")";
    }

};

module.exports = Complex;