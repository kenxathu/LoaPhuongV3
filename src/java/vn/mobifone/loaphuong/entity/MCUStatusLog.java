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


public class MCUStatusLog implements Serializable{
    private long id;
    private long mcu_id;
    private int mcu_status;
    private String mcu_connect_time;
    private String mcu_stat_value;
    private String mcu_log_time;
    private String mcu_version;
    private String created_date;
    private String mcu_ip;
    

    public MCUStatusLog() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMcu_id() {
        return mcu_id;
    }

    public void setMcu_id(long mcu_id) {
        this.mcu_id = mcu_id;
    }

    public int getMcu_status() {
        return mcu_status;
    }

    public void setMcu_status(int mcu_status) {
        this.mcu_status = mcu_status;
    }

    public String getMcu_connect_time() {
        return mcu_connect_time;
    }

    public void setMcu_connect_time(String mcu_connect_time) {
        this.mcu_connect_time = mcu_connect_time;
    }

    public String getMcu_stat_value() {
        return mcu_stat_value;
    }

    public void setMcu_stat_value(String mcu_stat_value) {
        this.mcu_stat_value = mcu_stat_value;
    }

    public String getMcu_log_time() {
        return mcu_log_time;
    }

    public void setMcu_log_time(String mcu_log_time) {
        this.mcu_log_time = mcu_log_time;
    }

    public String getMcu_version() {
        return mcu_version;
    }

    public void setMcu_version(String mcu_version) {
        this.mcu_version = mcu_version;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getMcu_ip() {
        return mcu_ip;
    }

    public void setMcu_ip(String mcu_ip) {
        this.mcu_ip = mcu_ip;
    }

 
    
    
  

}
