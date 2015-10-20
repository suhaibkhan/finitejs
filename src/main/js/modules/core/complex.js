'use strict';

/**
 * Module for complex number operations.
 *
 * @class
 */
function Complex(real, imaginary){
    this.ComplexWrapper = Java.type('com.finitejs.modules.math.complex.ComplexWrapper');

    this.real = real;
    this.imaginary = imaginary;
}

Complex.prototype = {

    abs: function() {
        return this.ComplexWrapper.abs(this.real, this.imaginary);
    }

};



module.exports = Complex;