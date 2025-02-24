
package com.TeamEnigma.cognito.utils;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;


public class FileHelper {

    public static double[] readAudioInputStream(AudioInputStream is) 
            throws IOException, UnsupportedAudioFileException {
        
        AudioFormat originalFormat = is.getFormat(); 
        AudioFormat format = new AudioFormat(originalFormat.getSampleRate(), 16, 1, true, true);

        AudioInputStream localIs = null;

        if(!originalFormat.matches(format)) {
            if(AudioSystem.isConversionSupported(format, originalFormat)) {
                localIs = AudioSystem.getAudioInputStream(format, is);
            } else {
                throw new UnsupportedAudioFileException("Alas, the system could not decode your file type." +
                		"Try converting your file to some PCM 16bit 16000 Hz mono file format using dedicated " +
                		"software. (Hint : http://sox.sourceforge.net/");
            }
        } else {
            localIs = is;
        }
        
        
        double[] audioSample = new double[(int)localIs.getFrameLength()];
        byte[] buffer = new byte[8192];
        int bytesRead = 0;
        int offset = 0;
        
        while((bytesRead = localIs.read(buffer)) > -1) {
            int wordCount = (bytesRead / 2) + (bytesRead % 2);
            for (int i = 0; i < wordCount; i++) {
                double d = (double) byteArrayToShort(buffer, 2 * i, format.isBigEndian()) / 32768;
                audioSample[offset + i] = d;
            }
            offset += wordCount;
        }
        return audioSample;
    }
    
    private static short byteArrayToShort(byte[] bytes, int offset, boolean bigEndian) {
        int low, high;
        if (bigEndian) {
            low = bytes[offset + 1];
            high = bytes[offset + 0];
        } else {
            low = bytes[offset + 0];
            high = bytes[offset + 1];
        }
        return (short) ((high << 8) | (0xFF & low));
    }
    
}
