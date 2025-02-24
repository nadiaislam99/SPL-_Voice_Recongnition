
package com.TeamEnigma.cognito.distances;


public abstract class DistanceCalculator {

    public abstract double getDistance(double[] features1, double[] features2);

    protected double positiveInfinityIfEitherOrBothAreNull(double[] features1, double[] features2) {
        if(features1 == null || features2 == null) {
            return Double.POSITIVE_INFINITY;
        } else {
            return -1.0d;
        }
    }
}
