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


public class MCUHistory implements Serializable{
    private long mcu_id;
    private String mcu_area_name;
    private String address;
    private int mcu_type;
    private String mira_name;
    private int playStatus;
    
    private String holder_name;

    public MCUHistory() {
    }

   
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMcu_type() {
        return mcu_type;
    }

    public void setMcu_type(int mcu_type) {
        this.mcu_type = mcu_type;
    }


    public long getMcu_id() {
        return mcu_id;
    }

    public void setMcu_id(long mcu_id) {
        this.mcu_id = mcu_id;
    }

    public String getHolder_name() {
        return holder_name;
    }

    public void setHolder_name(String holder_name) {
        this.holder_name = holder_name;
    }

    public String getMira_name() {
        if (holder_name != null && !holder_name.equals("")) {
            return holder_name;
        }
        return mira_name;
    }

    public void setMira_name(String mira_name) {
        this.mira_name = mira_name;
    }

    public String getMcu_area_name() {
        return mcu_area_name;
    }

    public void setMcu_area_name(String mcu_area_name) {
        this.mcu_area_name = mcu_area_name;
    }

    public int getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(int playStatus) {
        this.playStatus = playStatus;
    }



}
