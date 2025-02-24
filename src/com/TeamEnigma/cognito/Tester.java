package com.TeamEnigma.cognito;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
public class Tester {
    Map<String,Double> feq;
    StringBuilder result;
    public Tester(){
        try{
            System.out.println("recording started...");
            Record record = new Record();
            File tmp;
            tmp = new File(record.getVoice());
            File folder = new File("sample");
            System.out.println("I/O is ok : "+folder.canRead());
            feq=new HashMap<>();
            int setCount=0;
            for (File a : folder.listFiles()) {
                setCount++;
                Recognito<String> reco = new Recognito<>(16000.0f);
                double max = 0;
                String mPerson = null;
                System.out.println("\n"+a);
                for(File b:a.listFiles()){
                    VoicePrint print = reco.createVoicePrint(b.getName(), b);
                }
                List<MatchResult<String>> matches = reco.identify(tmp);
                for (MatchResult<String> m : matches) {
                    double ratio = m.getLikelihoodRatio();
                    if (feq.containsKey(m.getKey())) {
                        feq.replace(m.getKey(), feq.get(m.getKey()) + ratio);
                    } else {
                        feq.put(m.getKey(), ratio);
                    }
                    System.out.println(m.getKey() + "->" + ratio);
                    if (ratio > max) {
                        max = ratio;
                        mPerson = m.getKey();
                    }
                }
                System.out.println("predicted person for set "+setCount+": "+ mPerson + " with precision " + max);
            }
            System.out.println("\n------------------final result------------------");
            result = new StringBuilder();
            double finalMax=0;
            String finalPerson=null;
            for (String s : feq.keySet()) {
                double res=feq.get(s)/setCount;
                System.out.println(s + "->" + res);
                if(res>finalMax){
                    finalMax=res;
                    finalPerson=s;
                }
            }
            if(finalMax>30){
                System.out.println("predicted final person : "+ finalPerson + " with accuracy " + finalMax);
                result.append("predicted final person : ").append(finalPerson).append(" with accuracy ").append(finalMax);
            }
            else{
                System.out.println("No match");
                result.append("No match");
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }

    public Map<String, Double> getFeq() {
        return feq;
    }
    public String getResult(){
        return result.toString();
    }
}
