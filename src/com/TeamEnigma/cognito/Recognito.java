
package com.TeamEnigma.cognito;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.TeamEnigma.cognito.distances.DistanceCalculator;
import com.TeamEnigma.cognito.distances.EuclideanDistanceCalculator;
import com.TeamEnigma.cognito.enhancements.Normalizer;
import com.TeamEnigma.cognito.features.FeaturesExtractor;
import com.TeamEnigma.cognito.features.LpcFeaturesExtractor;
import com.TeamEnigma.cognito.utils.FileHelper;
import com.TeamEnigma.cognito.vad.AutocorrellatedVoiceActivityDetector;


public class Recognito<K> {

    private static final float MIN_SAMPLE_RATE = 8000.0f;
    
    private final ConcurrentHashMap<K, VoicePrint> store = new ConcurrentHashMap<K, VoicePrint>();
    private final float sampleRate;

    private final AtomicBoolean universalModelWasSetByUser = new AtomicBoolean();
    private VoicePrint universalModel;

    

    public Recognito(float sampleRate) {
        if(sampleRate < MIN_SAMPLE_RATE) {
            throw new IllegalArgumentException("Sample rate should be at least 8000 Hz");
        }
        this.sampleRate = sampleRate;
    }
    

    public Recognito(float sampleRate, Map<K, VoicePrint> voicePrintsByUserKey) {
        this(sampleRate);
        Iterator<VoicePrint> it = voicePrintsByUserKey.values().iterator();
        if (it.hasNext()) {
            VoicePrint print = it.next();
            universalModel = new VoicePrint(print);            
            while (it.hasNext()) {
                universalModel.merge(it.next());
            }
        }
        store.putAll(voicePrintsByUserKey);
    }
    

    public VoicePrint getUniversalModel() {
        return new VoicePrint(universalModel);
    }
    

    public synchronized void setUniversalModel(VoicePrint universalModel) {
        if(universalModel == null) {
            throw new IllegalArgumentException("The universal model may not be null");
        }
        this.universalModelWasSetByUser.set(false);
        this.universalModel = universalModel;
    }
    

    public synchronized VoicePrint createVoicePrint(K userKey, double[] voiceSample) {
        if(userKey == null) {
            throw new NullPointerException("The userKey is null");
        }
        if(store.containsKey(userKey)) {
            throw new IllegalArgumentException("The userKey already exists: [" + userKey + "]");
        }
        
        double[] features = extractFeatures(voiceSample, sampleRate);
        VoicePrint voicePrint = new VoicePrint(features);
         
        synchronized (this) {
            if (!universalModelWasSetByUser.get()) {
                if (universalModel == null) {
                    universalModel = new VoicePrint(voicePrint);
                } else {
                    universalModel.merge(features);
                }
            }
        }
        store.put(userKey, voicePrint);
        
        return voicePrint;
    }
    

    public VoicePrint createVoicePrint(K userKey, File voiceSampleFile) 
            throws UnsupportedAudioFileException, IOException {
        
        double[] audioSample = convertFileToDoubleArray(voiceSampleFile);

        return createVoicePrint(userKey, audioSample);
    }


    private double[] convertFileToDoubleArray(File voiceSampleFile) 
            throws UnsupportedAudioFileException, IOException {
        
        AudioInputStream sample = AudioSystem.getAudioInputStream(voiceSampleFile);
        AudioFormat format = sample.getFormat();
        float diff = Math.abs(format.getSampleRate() - sampleRate);
        if(diff > 5 * Math.ulp(0.0f)) {
            throw new IllegalArgumentException("The sample rate for this file is different than Recognito's " +
            		"defined sample rate : [" + format.getSampleRate() + "]");
        }
        return FileHelper.readAudioInputStream(sample);
    }
    

    public VoicePrint mergeVoiceSample(K userKey, double[] voiceSample) {
        
        if(userKey == null) {
            throw new NullPointerException("The userKey is null");
        }
        
        VoicePrint original = store.get(userKey);
        if(original == null) {
            throw new IllegalArgumentException("No voice print linked to this user key [" + userKey + "]");
        }

        double[] features = extractFeatures(voiceSample, sampleRate);
        synchronized (this) {
            if(!universalModelWasSetByUser.get()) {
                universalModel.merge(features);
            }
        }
        original.merge(features);
        
        return original;
    }
    

    public VoicePrint mergeVoiceSample(K userKey, File voiceSampleFile) 
            throws UnsupportedAudioFileException, IOException {
        
        double[] audioSample = convertFileToDoubleArray(voiceSampleFile);

        return mergeVoiceSample(userKey, audioSample);
    }

    public List<MatchResult<K>> identify(double[] voiceSample) {
        
        if(store.isEmpty()) {
            throw new IllegalStateException("There is no voice print enrolled in the system yet");
        }

        VoicePrint voicePrint = new VoicePrint(extractFeatures(voiceSample, sampleRate));
        
        DistanceCalculator calculator = new EuclideanDistanceCalculator();
        List<MatchResult<K>> matches = new ArrayList<MatchResult<K>>(store.size());

        double distanceFromUniversalModel = voicePrint.getDistance(calculator, universalModel);
        for (Entry<K, VoicePrint> entry : store.entrySet()) {
            double distance = entry.getValue().getDistance(calculator, voicePrint);
            // likelihood : how close is the given voice sample to the current VoicePrint 
            // compared to the total distance between the current VoicePrint and the universal model 
            int likelihood = 100 - (int) (distance / (distance + distanceFromUniversalModel) * 100);
            matches.add(new MatchResult<K>(entry.getKey(), likelihood, distance));
        }

        Collections.sort(matches, new Comparator<MatchResult<K>>() {
            @Override
            public int compare(MatchResult<K> m1, MatchResult<K> m2) {
                return Double.compare(m1.getDistance(), m2.getDistance());
            }
        });
        
        return matches;
    }
  

    public  List<MatchResult<K>> identify(File voiceSampleFile) 
            throws UnsupportedAudioFileException, IOException {
        
        double[] audioSample = convertFileToDoubleArray(voiceSampleFile);

        return identify(audioSample);
    }
  

    private double[] extractFeatures(double[] voiceSample, float sampleRate) {

        AutocorrellatedVoiceActivityDetector voiceDetector = new AutocorrellatedVoiceActivityDetector();
        Normalizer normalizer = new Normalizer();
        FeaturesExtractor<double[]> lpcExtractor = new LpcFeaturesExtractor(sampleRate, 20);

        voiceDetector.removeSilence(voiceSample, sampleRate);
        normalizer.normalize(voiceSample, sampleRate);
        double[] lpcFeatures = lpcExtractor.extractFeatures(voiceSample);

        return lpcFeatures;
    }
}
