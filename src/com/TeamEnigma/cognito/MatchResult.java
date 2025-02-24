
package com.TeamEnigma.cognito;


public class MatchResult<K> {
    
    private final K key;
    private final int likelihoodRatio;
    private final double distance;
    

    MatchResult(K key, int likelihoodRatio, double distance) {
        super();
        this.key = key;
        this.likelihoodRatio = likelihoodRatio;
        this.distance = distance;
    }


    public K getKey() {
        return key;
    }


    public int getLikelihoodRatio() {
        return likelihoodRatio;
    }

    double getDistance() {
        return distance;
    }
}
