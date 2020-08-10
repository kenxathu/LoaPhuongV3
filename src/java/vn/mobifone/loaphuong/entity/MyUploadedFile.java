/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import be.tarsos.transcoder.Attributes;
import be.tarsos.transcoder.Transcoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.primefaces.model.UploadedFile;
import vn.mobifone.loaphuong.lib.SystemConfig;

/**
 *
 * @author cuong.trinh
 */
public class MyUploadedFile {
    private UploadedFile uploadedFile;

    public MyUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getDuration() {
        long durationInSeconds = 0;
        try {
            String ext = uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf(".") + 1);
            ext = ext.toUpperCase();
            AudioInputStream audioInputStream = null;
//        if (ext.equals("WAV")) {
//            audioInputStream = AudioSystem.getAudioInputStream(uploadedFile.getInputstream());
//        } else {
//            audioInputStream = AudioSystem.getAudioInputStream(convertedFile);
//        }
            //File t = new File(uploadedFile.getInputstream());
            File convertedFile = new File(convertAudio(uploadedFile));

            audioInputStream = AudioSystem.getAudioInputStream(convertedFile);
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = convertedFile.length();
            int frameSize = format.getFrameSize();
            long frameRate = (long) format.getFrameRate();
            durationInSeconds = (audioFileLength / (frameSize * frameRate ));
            convertedFile.delete();
            
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(MyUploadedFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyUploadedFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (durationInSeconds/60 < 10 ? "0" + durationInSeconds/60 : durationInSeconds/60) + ":" + (durationInSeconds%60 < 10 ? "0" + durationInSeconds%60 : durationInSeconds%60);
    }
    
    public String convertAudio(UploadedFile audioFile){
        String outputFile = null;
        try {
           String timestamp = System.currentTimeMillis()+"";
            String assetsDir = SystemConfig.getConfig("AssetsDir");
            String outputRaw = assetsDir+ SystemConfig.getConfig("audioOutputRaw");
            String outputConverted =assetsDir+ SystemConfig.getConfig("audioOutputConverted");


            File folderConverted = new File(outputConverted);
            File folderRow = new File(outputRaw);
            if(!folderRow.exists()|| !folderRow.isDirectory()){
                folderRow.mkdirs();
            }
            if(!folderConverted.exists()|| !folderConverted.isDirectory()){
                folderConverted.mkdirs();
            }


            String audioFormat = SystemConfig.getConfig("audioAudioFormat");
            String audioCodec = SystemConfig.getConfig("audioCodec");
            String samplingRate = SystemConfig.getConfig("audioSamplingRate");
            
            String outputFileName = timestamp+"_"+audioFile.getFileName().replaceAll("[^A-Za-z0-9\\.]","");
            String outputFilePath = outputRaw+outputFileName;
            saveInputStreamToFile(audioFile.getInputstream(), outputFilePath);
            File f = new File(outputFilePath);
            if(!f.exists()){
                throw new FileNotFoundException("upload failed");
            }

            Attributes alaw = new Attributes(audioFormat, audioCodec, Integer.parseInt(samplingRate), 1);
            outputFile = outputConverted+outputFileName+ "."+ alaw.getFormat();
            Transcoder.transcode(f.getAbsolutePath(), outputFile, alaw);
            f.delete();
       } catch (Exception e) {
           
           e.printStackTrace();
       }
        
        if(outputFile!=null && new File(outputFile).exists()){
            return outputFile;            
        }else{
            return null;
        } 
    }
    
    private void saveInputStreamToFile(InputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];

		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
    }
}
