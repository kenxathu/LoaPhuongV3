package vn.mobifone.loaphuong.lib;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.AgeFileFilter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Xử lý Dọn rác cho Birt Report
 * @author Tuan Dung
 *
 */
public class TelSoftBIRTCleanup implements Runnable {

   // static final Logger LOG = (Logger) LoggerFactory.getLogger(TelSoftBIRTCleanup.class);
    boolean stopped = false;
    String imagePath;
    String outputPath;
    String rptDocumentPath;
    int wakeupInMinutes;
    int imagesOlderThanInMinutes;
    int outputOlderThanInMinutes;
    int rptDocumentOlderThanInMinutes;

    public TelSoftBIRTCleanup(String imagePath, String outputPath,
            String rptDocumentPath, int periodInMinutes,
            int imagesOlderThanInMinutes, int outputOlderThanInMinutes,
            int rptDocumentOlderThanInMinutes) {
        // TODO Auto-generated constructor stub
        this.imagePath = imagePath;
        this.outputPath = outputPath;
        this.rptDocumentPath = rptDocumentPath;
        this.wakeupInMinutes = periodInMinutes;
        this.imagesOlderThanInMinutes = imagesOlderThanInMinutes;
        this.outputOlderThanInMinutes = outputOlderThanInMinutes;
        this.rptDocumentOlderThanInMinutes = rptDocumentOlderThanInMinutes;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!stopped) {
            try {
                TimeUnit.MINUTES.sleep(wakeupInMinutes);
                cleanup();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
               // LOG.debug("BIRT-Engine-Cleanup thread interrupted");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //LOG.debug("BIRT-Engine-Cleanup thread stopped");
    }

    public void cleanup() throws Exception {
        File imageDir = new File(imagePath);
        File outputDir = new File(outputPath);
        File rptDocumentDir = new File(rptDocumentPath);

        // Clean up temporary images
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, imagesOlderThanInMinutes);
        Date cutoffDate = now.getTime();

        FileFilter ageFileFilter = new AgeFileFilter(cutoffDate);
        File[] files = imageDir.listFiles(ageFileFilter);

        if (files != null) {
            for (File file : files) {
                file.delete();
               // LOG.debug(file.getName() + " deleted");
            }
        }

        // Cleanup temporary ouput
        now = Calendar.getInstance();
        now.add(Calendar.MINUTE, outputOlderThanInMinutes);
        cutoffDate = now.getTime();

        ageFileFilter = new AgeFileFilter(cutoffDate);
        files = outputDir.listFiles(ageFileFilter);

        if (files != null) {
            for (File file : files) {
                file.delete();
              //  LOG.debug(file.getName() + " deleted");
            }
        }

        // Cleanup temporary rptdocument
        now = Calendar.getInstance();
        now.add(Calendar.MINUTE, rptDocumentOlderThanInMinutes);
        cutoffDate = now.getTime();

        ageFileFilter = new AgeFileFilter(cutoffDate);
        files = rptDocumentDir.listFiles(ageFileFilter);

        if (files != null) {
            for (File file : files) {
                file.delete();
               // LOG.debug(file.getName() + " deleted");
            }
        }

    }
}
