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
 * 
 * 

 */


public class MCUPlayStateList implements Serializable{
    private long rec_id;
    private long mcu_id;
    private int log_type;
    private String log_time;
    private  String rec_summary;
    private int rec_type;


    public MCUPlayStateList() {
    }

    public int getRec_type() {
        return rec_type;
    }

    public void setRec_type(int rec_type) {
        this.rec_type = rec_type;
    }

    public long getRec_id() {
        return rec_id;
    }

    public void setRec_id(long rec_id) {
        this.rec_id = rec_id;
    }

    public long getMcu_id() {
        return mcu_id;
    }

    public void setMcu_id(long mcu_id) {
        this.mcu_id = mcu_id;
    }

    public int getLog_type() {
        return log_type;
    }

    public void setLog_type(int log_type) {
        this.log_type = log_type;
    }

    public String getLog_time() {
        return log_time;
    }

    public void setLog_time(String log_time) {
        this.log_time = log_time;
    }

    public String getRec_summary() {
        return rec_summary;
    }

    public void setRec_summary(String rec_summary) {
        this.rec_summary = rec_summary;
    }

    

}
