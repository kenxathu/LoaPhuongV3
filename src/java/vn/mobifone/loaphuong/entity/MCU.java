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


public class MCU implements Serializable{
    private long mcu_id;
    private String vcode;
    private String mcu_ip;
    private String mcu_tel;
    private long mcu_area_id;
    private String mcu_area_path;
    private String household_area_path;
    private long household_area_id;
    private long household_id;
    private double mcu_lat;
    private double mcu_lng;
    private String address;
    private int mcu_type;
    private int mcu_tx_type;
    private int mcu_mobile_csq;
    private int mcu_wifi_csq;
    private int volume;
    private String phone_number;
    private String mcu_sim_serial;
    private String mira_name;
    private String mcu_area_name;
    private int mcu_fm_volume;
    private int mcu_fm_auto;
    private String mcu_version;
            
    
    private String household_address;
    private int mcu_connect_status;
    private String mcu_connect_time;
    private String holder_name;

    public MCU() {
    }

    public MCU(MCU mcu) {
        this.mcu_id = mcu.mcu_id;
        this.mcu_lat = mcu.mcu_lat;
        this.mcu_lng = mcu.mcu_lng;
        this.mcu_type = mcu.mcu_type;
        this.volume = mcu.volume;
        this.mcu_tel = mcu.mcu_tel;
        this.phone_number = mcu.mcu_tel;
        this.mcu_sim_serial = mcu.mcu_sim_serial;
        this.mira_name = mcu.mira_name;
        this.mcu_fm_volume = mcu.mcu_fm_volume;
        this.mcu_fm_auto = mcu.mcu_fm_auto;
        this.address = mcu.address;
        this.mira_name = mcu.mira_name;
        this.mcu_version = mcu.mcu_version;
    }

    public String getMcu_version() {
        return mcu_version;
    }

    public void setMcu_version(String mcu_version) {
        this.mcu_version = mcu_version;
    }

    
    
    public int getMcu_tx_type() {
        return mcu_tx_type;
    }

    public void setMcu_tx_type(int mcu_tx_type) {
        this.mcu_tx_type = mcu_tx_type;
    }


    public int getMcu_mobile_csq() {
        return mcu_mobile_csq;
    }

    public void setMcu_mobile_csq(int mcu_mobile_csq) {
        this.mcu_mobile_csq = mcu_mobile_csq;
    }

    public int getMcu_wifi_csq() {
        return mcu_wifi_csq;
    }

    public void setMcu_wifi_csq(int mcu_wifi_csq) {
        this.mcu_wifi_csq = mcu_wifi_csq;
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

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getMcu_ip() {
        return mcu_ip;
    }

    public void setMcu_ip(String mcu_ip) {
        this.mcu_ip = mcu_ip;
    }

    public String getMcu_tel() {
        return mcu_tel;
    }

    public void setMcu_tel(String mcu_tel) {
        this.mcu_tel = mcu_tel;
    }

    public long getMcu_area_id() {
        return mcu_area_id;
    }

    public void setMcu_area_id(long mcu_area_id) {
        this.mcu_area_id = mcu_area_id;
    }

    public String getMcu_area_path() {
        return mcu_area_path;
    }

    public void setMcu_area_path(String mcu_area_path) {
        this.mcu_area_path = mcu_area_path;
    }

    public String getHousehold_area_path() {
        return household_area_path;
    }

    public void setHousehold_area_path(String household_area_path) {
        this.household_area_path = household_area_path;
    }

    public long getHousehold_area_id() {
        return household_area_id;
    }

    public void setHousehold_area_id(long household_area_id) {
        this.household_area_id = household_area_id;
    }

    public String getHousehold_address() {
        return household_address;
    }

    public void setHousehold_address(String household_address) {
        this.household_address = household_address;
    }

    public int getMcu_connect_status() {
        return mcu_connect_status;
    }

    public void setMcu_connect_status(int mcu_connect_status) {
        this.mcu_connect_status = mcu_connect_status;
    }

    public String getMcu_connect_time() {
        return mcu_connect_time;
    }

    public void setMcu_connect_time(String mcu_connect_time) {
        this.mcu_connect_time = mcu_connect_time;
    }

    public String getHolder_name() {
        return holder_name;
    }

    public void setHolder_name(String holder_name) {
        this.holder_name = holder_name;
    }

    public long getHousehold_id() {
        return household_id;
    }

    public void setHousehold_id(long household_id) {
        this.household_id = household_id;
    }

    public double getMcu_lat() {
        return mcu_lat;
    }

    public void setMcu_lat(double mcu_lat) {
        this.mcu_lat = mcu_lat;
    }

    public double getMcu_lng() {
        return mcu_lng;
    }

    public void setMcu_lng(double mcu_lng) {
        this.mcu_lng = mcu_lng;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


    public String getMcu_sim_serial() {
        return mcu_sim_serial;
    }

    public void setMcu_sim_serial(String mcu_sim_serial) {
        this.mcu_sim_serial = mcu_sim_serial;
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

    public int getMcu_fm_volume() {
        return mcu_fm_volume;
    }

    public void setMcu_fm_volume(int mcu_fm_volume) {
        this.mcu_fm_volume = mcu_fm_volume;
    }


    public boolean getFM_AutoBoolean() {
        return mcu_fm_auto==1;
    }

    public void setFM_AutoBoolean(boolean fmAuto) {
        this.mcu_fm_auto = fmAuto?1:0;
    }


    
  

}
