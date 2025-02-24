package com.TeamEnigma.cognito;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Record {
    private String name="tmp.wav";
    public Record(){
        try {
            AudioFormat format = new AudioFormat(16000, 8, 2, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open();
            System.out.println("Starting Recording");
            System.out.println("Give a  5sec voice input : ");
            targetDataLine.start();
            Thread stopper = new Thread(new Runnable() {
                @Override
                public void run() {
                    AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                    File wavFile = new File(name);//This is the file location where
                    // the wave file will be saved.

                    try {
                        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            stopper.start();
            Thread.sleep(5000);
            targetDataLine.stop();
            targetDataLine.close();
            System.out.println("Sound test ended");
            System.out.println("Your file is saved ");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public String getVoice() {
        return name;
    }
}
