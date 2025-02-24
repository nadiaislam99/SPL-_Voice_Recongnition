
package com.TeamEnigma.cognito.enhancements;


public class Normalizer {

    public double normalize(double[] audioSample, float sampleRate) {

        double max = Double.MIN_VALUE;

        for (int i = 0; i < audioSample.length; i++) {
            double abs = Math.abs(audioSample[i]);
            if (abs > max) {
                max = abs;
            }
        }
        if(max > 1.0d) {
            throw new IllegalArgumentException("Expected value for audio are in the range -1.0 <= v <= 1.0 ");
        }
        if (max < 5 * Math.ulp(0.0d)) { // ulp of 0.0 is extremely small ! i.e. as small as it can get
            return 1.0d;
        }
        for (int i = 0; i < audioSample.length; i++) {
            audioSample[i] /= max;
        }
        return 1.0d / max;
    }

}
