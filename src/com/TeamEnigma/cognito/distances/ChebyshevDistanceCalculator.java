
package com.TeamEnigma.cognito.distances;


public class ChebyshevDistanceCalculator 
        extends DistanceCalculator {


    public double getDistance(double[] features1, double[] features2) {
        double distance = positiveInfinityIfEitherOrBothAreNull(features1, features2);
        if (distance < 0) {
            if(features1.length != features2.length) {
                throw new IllegalArgumentException("Both features should have the same length. Received lengths of [" +
                        + features1.length + "] and [" + features2.length + "]");
            }
            distance = 0.0;
            for (int i = 0; i < features1.length; i++) {
                double currentDistance = Math.abs(features1[i] - features2[i]);
                distance = (currentDistance > distance) ? currentDistance : distance; 
            }
        }
        return distance;
    }

}
