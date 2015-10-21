'use strict';

var CommonsComplex = Java.type('org.apache.commons.math3.complex.Complex');

/**
 * Represents a complex number.
 *
 * @class
 */
function Complex(real, imaginary){
    this._complex = new CommonsComplex(real, imaginary);
}

/**
 * Access the real part of the complex number.
 *
 * @returns {number} Real part of the complex number.
 */
Complex.prototype.real = function() {
    return this._complex.getReal();
};

/**
 * Access the imaginary part of the complex number.
 *
 * @returns {number} Imaginary part of the complex number.
 */
Complex.prototype.imaginary = function() {
    return this._complex.getImaginary();
};

/**
 * Returns the absolute value of this complex number.
 *
 * @returns {number} Absolute value of this complex number.
 */
Complex.prototype.abs = function() {
    return this._complex.abs();
};

/**
 * Returns the conjugate of this complex number.
 *
 * @returns {Complex} Instance of Complex which represents the conjugate.
 */
Complex.prototype.conjugate = function() {
    var conjugate =  this._complex.conjugate();
    return new Complex(conjugate.getReal(), conjugate.getImaginary());
};

/**
 * Returns the negated value of this complex number.
 *
 * @returns {Complex} Instance of Complex which represents negated value of this complex number.
 */
Complex.prototype.negate = function() {
    var negated = this._complex.negate();
    return new Complex(negated.getReal(), negated.getImaginary());
};

Complex.prototype = {

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

    divide: function(divisor) {
        var result = this._complex.divide(
            new CommonsComplex(
                divisor.real(),
                divisor.imaginary()
            )
        );

        return new Complex(result.getReal(), result.getImaginary());
    },

    toString: function() {
        var real = this._complex.getReal();
        var imaginary = this._complex.getImaginary();

        var SIGN = (imaginary>0) ? "+" : "";

        return "(" + real + SIGN + imaginary + "i" + ")";
    }

};

module.exports = Complex;