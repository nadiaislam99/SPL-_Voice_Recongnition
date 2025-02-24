
package com.TeamEnigma.cognito.algorithms.windowing;

public abstract class WindowFunction {

    protected static final double TWO_PI = 2 * Math.PI;
    
    private final int windowSize;
    private final double[] factors;

    public WindowFunction(int windowSize) {
        this.windowSize = windowSize;
        this.factors = getPrecomputedFactors(windowSize);
    }

    public void applyFunction(double[] window) {
        if (window.length == this.windowSize) {
            for (int i = 0; i < window.length; i++) {
                window[i] *= factors[i];
            }
        } else {
            throw new IllegalArgumentException("Incompatible window size for this WindowFunction instance : " +
                    "expected " + windowSize + ", received " + window.length);
        }
    }
    protected abstract double[] getPrecomputedFactors(int windowSize);
}
