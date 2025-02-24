
package com.TeamEnigma.cognito.algorithms.windowing;

import java.util.HashMap;
import java.util.Map;

public class HammingWindowFunction 
        extends WindowFunction {

    private static final Map<Integer, double[]> factorsByWindowSize = new HashMap<Integer, double[]>();

    public HammingWindowFunction(int windowSize) {
        super(windowSize);
    }

    @Override
    protected double[] getPrecomputedFactors(int windowSize) {
        // precompute factors for given window, avoid re-calculating for several instances
        synchronized (HammingWindowFunction.class) {
            double[] factors;
            if(factorsByWindowSize.containsKey(windowSize)) {
                factors = factorsByWindowSize.get(windowSize);
            } else {
                factors = new double[windowSize];
                int sizeMinusOne = windowSize - 1;
                for(int i = 0; i < windowSize; i++) {
                    factors[i] = 0.54d - (0.46d * Math.cos((TWO_PI * i) / sizeMinusOne));
                }
                factorsByWindowSize.put(windowSize, factors);
            }
            return factors;
        }
    }

}
