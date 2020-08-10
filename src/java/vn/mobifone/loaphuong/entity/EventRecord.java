/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;

/**
 *
 * @author cuong.trinh
 */
public class EventRecord implements Serializable{
    private String date;
    private String summary;
    private int fromH;
    private int fromM;
    private int fromS;
    private int toH;
    private int toM;
    private int toS;
    private int playMode;
    private String playTime;
    private String repeatDays;
    private int repeatType;
    private String createdArea;
    private int type;
    private long rec_id;

    public EventRecord() {
    }

    public EventRecord(String date, String summary, int fromH, int fromM, int fromS, int toH, int toM, int toS) {
        this.date = date;
        this.summary = summary;
        this.fromH = fromH;
        this.fromM = fromM;
        this.fromS = fromS;
        this.toH = toH;
        this.toM = toM;
        this.toS = toS;
    }

    public EventRecord(String date, String summary, int fromH, int fromM, int fromS, int toH, int toM, int toS, int playMode, int repeatType, String repeatDay, String createdArea, String playTime, int type, long rec_id) {
        this.date = date;
        this.summary = summary;
        this.fromH = fromH;
        this.fromM = fromM;
        this.fromS = fromS;
        this.toH = toH;
        this.toM = toM;
        this.toS = toS;
        this.playMode = playMode;
        this.repeatType = repeatType;
        this.repeatDays = repeatDay;
        this.createdArea = createdArea;
        this.playTime = playTime;
        this.type = type;
        this.rec_id = rec_id;
    }

    public long getRec_id() {
        return rec_id;
    }

    public void setRec_id(long rec_id) {
        this.rec_id = rec_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getFromH() {
        return fromH;
    }

    public void setFromH(int fromH) {
        this.fromH = fromH;
    }

    public int getFromM() {
        return fromM;
    }

    public void setFromM(int fromM) {
        this.fromM = fromM;
    }

    public int getFromS() {
        return fromS;
    }

    public void setFromS(int fromS) {
        this.fromS = fromS;
    }

    public int getToH() {
        return toH;
    }

    public void setToH(int toH) {
        this.toH = toH;
    }

    public int getToM() {
        return toM;
    }

    public void setToM(int toM) {
        this.toM = toM;
    }

    public int getToS() {
        return toS;
    }

    public void setToS(int toS) {
        this.toS = toS;
    }

    public int getPlayMode() {
        return playMode;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    public int getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

    public String getCreatedArea() {
        return createdArea;
    }

    public void setCreatedArea(String createdArea) {
        this.createdArea = createdArea;
    }
    
    
}
