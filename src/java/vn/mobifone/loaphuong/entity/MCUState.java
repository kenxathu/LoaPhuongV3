/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cuong.trinh
 * 
 * 

 */


public class MCUState implements Serializable{
    private List<MCUScheduleList> scheduleList;
    private List<MCUPlayStateList> playStateList;
    private List<MCUStatusLog> statusLog;


    public MCUState() {
    }

    public List<MCUScheduleList> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<MCUScheduleList> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public List<MCUPlayStateList> getPlayStateList() {
        return playStateList;
    }

    public void setPlayStateList(List<MCUPlayStateList> playStateList) {
        this.playStateList = playStateList;
    }

    public List<MCUStatusLog> getStatusLog() {
        return statusLog;
    }

    public void setStatusLog(List<MCUStatusLog> statusLog) {
        this.statusLog = statusLog;
    }

   

}
