/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cuong.trinh
 * 
 * 

 */


public class HistoryRecord implements Serializable{
    private Date playTime;
    private Date endTime;
    private long numberPlay;
    
    SimpleDateFormat DateFormatter3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 

    public HistoryRecord(Date playTime, Date endTime, long numberPlay) {
        this.playTime = playTime;
        this.endTime = endTime;
        this.numberPlay = numberPlay;
    }
    
    public HistoryRecord() {
    }

    public Date getPlayTime() {
        return playTime;
    }
    
    public String getPlayTimeString() {
        return DateFormatter3.format(playTime);
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    
    public String getEndTimeString() {
        return DateFormatter3.format(endTime);
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getNumberPlay() {
        return numberPlay;
    }

    public void setNumberPlay(long numberPlay) {
        this.numberPlay = numberPlay;
    }

}
