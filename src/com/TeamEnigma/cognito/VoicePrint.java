
package com.TeamEnigma.cognito;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.TeamEnigma.cognito.distances.DistanceCalculator;


public final class VoicePrint
        implements Serializable {

    private static final long serialVersionUID = 5656438598778733593L;
    
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
    
    private double[] features;
    private int meanCount;

    

    VoicePrint(double[] features) {
        super();
        this.features = features;
        this.meanCount = 1;
    }


    VoicePrint(VoicePrint print) {
        this(Arrays.copyOf(print.features, print.features.length));
    }


    double getDistance(DistanceCalculator calculator, VoicePrint voicePrint) {
        r.lock();
        try { 
            return calculator.getDistance(this.features, voicePrint.features);
        } 
        finally { r.unlock(); }
    }


    void merge(double[] features) {
        if(this.features.length != features.length) {
            throw new IllegalArgumentException("Features of new VoicePrint is of different size : [" + 
                    features.length + "] expected [" + this.features.length + "]");
        }
        w.lock();
        try { 
            merge(this.features, features);
            meanCount++;
        } 
        finally { w.unlock(); }
    }


    void merge(VoicePrint print) {
        this.merge(print.features); 
    }


    private void merge(double[] inner, double[] outer) {
        for (int i = 0; i < inner.length; i++) {
            inner[i] = (inner[i] * meanCount + outer[i]) / (meanCount + 1);
        }
    }


    @Override
    public String toString() {
        return Arrays.toString(features);
    }
    
}
