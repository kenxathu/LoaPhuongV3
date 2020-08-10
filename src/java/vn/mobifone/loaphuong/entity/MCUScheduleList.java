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


public class MCUScheduleList implements Serializable{
    private long rec_id;
    private String start_time;
    private String end_time;
    private  String rec_summary;
    private int rec_type;
    public String getRec_summary() {
        return rec_summary;
    }

    public void setRec_summary(String rec_summary) {
        this.rec_summary = rec_summary;
    }

    public int getRec_type() {
        return rec_type;
    }

    public void setRec_type(int rec_type) {
        this.rec_type = rec_type;
    }


    public MCUScheduleList() {
    }

    public long getRec_id() {
        return rec_id;
    }

    public void setRec_id(long rec_id) {
        this.rec_id = rec_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

   
    

}
