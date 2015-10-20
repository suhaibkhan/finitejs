package com.finitejs.modules.math.complex;

import org.apache.commons.math3.complex.Complex;

public class ComplexWrapper {

    public static double abs(double real, double imaginary) {
        return new Complex(real, imaginary).abs();
    }

}
