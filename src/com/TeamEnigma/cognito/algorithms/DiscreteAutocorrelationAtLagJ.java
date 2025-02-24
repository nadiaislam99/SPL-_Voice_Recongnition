
package com.TeamEnigma.cognito.algorithms;


public class DiscreteAutocorrelationAtLagJ {


    public double autocorrelate(double[] buffer, int lag) {
        if(lag > -1 && lag < buffer.length) {
            double result = 0.0;
            for (int i = lag; i < buffer.length; i++) {
                result += buffer[i] * buffer[i - lag];
            }
            return result;
        } else {
            throw new IndexOutOfBoundsException("Lag parameter range is : -1 < lag < buffer size. Received [" 
                    + lag + "] for buffer size of [" + buffer.length + "]");
        }
    }

}
