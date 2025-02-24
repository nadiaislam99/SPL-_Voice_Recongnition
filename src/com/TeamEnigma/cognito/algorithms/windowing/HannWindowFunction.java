
package com.TeamEnigma.cognito.algorithms.windowing;

import java.util.HashMap;
import java.util.Map;

public final class HannWindowFunction 
        extends WindowFunction {
    
    private static final Map<Integer, double[]> factorsByWindowSize = new HashMap<Integer, double[]>();


    public HannWindowFunction(int windowSize) {
        super(windowSize);
    }

    @Override
    protected double[] getPrecomputedFactors(int windowSize) {
        // precompute factors for given window, avoid re-calculating for several instances
        synchronized (HannWindowFunction.class) {
            double[] factors;
            if(factorsByWindowSize.containsKey(windowSize)) {
                factors = factorsByWindowSize.get(windowSize);
            } else {
                factors = new double[windowSize];
                int sizeMinusOne = windowSize - 1;
                for(int i = 0; i < windowSize; i++) {
                    factors[i] = 0.5d * (1 - Math.cos((TWO_PI * i) / sizeMinusOne));
                }
                factorsByWindowSize.put(windowSize, factors);
            }
            return factors;
        }
    }

}
