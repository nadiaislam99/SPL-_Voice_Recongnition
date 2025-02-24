
package com.TeamEnigma.cognito.features;

public interface FeaturesExtractor<T> {

    public T extractFeatures(double[] voiceSample);

}