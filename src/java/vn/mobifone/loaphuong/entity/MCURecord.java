/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author cuong.trinh
 * 
 * 

 */

public class MCURecord implements Serializable{
    private long mcu_id;
    private long log_index;
    private String created_date;
    private int fm_play_duration;
    private int rec_status;
    private String rec_summary;
    private int rec_play_mode;
    private int rec_audio_codec;
    private int rec_size;
    private long rec_checksum;
    private long record_id;
    //private int rec_play_time;
    private long rec_play_start;
    private long rec_play_expire;
    private long rec_created;
    private int fm_requency;
    private int fm_auto_switch_time;
    private int rec_priority;
    private int rec_play_repeat_type;
    private int rec_play_repeat_day;
    private int rec_audio_format;

    SimpleDateFormat DateFormatter2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
    SimpleDateFormat DateFormatter3 = new SimpleDateFormat("dd-MM-yyyy");

    public MCURecord() {
    }

    public long getMcu_id() {
        return mcu_id;
    }

    public void setMcu_id(long mcu_id) {
        this.mcu_id = mcu_id;
    }

    public long getLog_index() {
        return log_index;
    }

    public void setLog_index(long log_index) {
        this.log_index = log_index;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public int getFm_play_duration() {
        return fm_play_duration;
    }

    public void setFm_play_duration(int fm_play_duration) {
        this.fm_play_duration = fm_play_duration;
    }

    public int getRec_status() {
        return rec_status;
    }

    public void setRec_status(int rec_status) {
        this.rec_status = rec_status;
    }

    public String getRec_summary() {
        return rec_summary;
    }

    public void setRec_summary(String rec_summary) {
        this.rec_summary = rec_summary;
    }

    public int getRec_play_mode() {
        return rec_play_mode;
    }

    public void setRec_play_mode(int rec_play_mode) {
        this.rec_play_mode = rec_play_mode;
    }

    public int getRec_audio_codec() {
        return rec_audio_codec;
    }

    public void setRec_audio_codec(int rec_audio_codec) {
        this.rec_audio_codec = rec_audio_codec;
    }

    public int getRec_size() {
        return rec_size/1024;
    }

    public void setRec_size(int rec_size) {
        this.rec_size = rec_size;
    }

    public long getRec_checksum() {
        return rec_checksum;
    }

    public void setRec_checksum(long rec_checksum) {
        this.rec_checksum = rec_checksum;
    }

    public long getRecord_id() {
        return record_id;
    }

    public void setRecord_id(long record_id) {
        this.record_id = record_id;
    }
//
//    public int getRec_play_time() {
//        return rec_play_time;
//    }
//
//    public void setRec_play_time(int rec_play_time) {
//        this.rec_play_time = rec_play_time;
//    }

    public long getRec_play_start() {
        return rec_play_start;
    }

    public void setRec_play_start(long rec_play_start) {
        this.rec_play_start = rec_play_start;
    }

    public long getRec_play_expire() {
        return rec_play_expire;
    }

    public void setRec_play_expire(long rec_play_expire) {
        this.rec_play_expire = rec_play_expire;
    }

    public long getRec_created() {
        return rec_created;
    }
    
    public String getRecCreate() {
        return DateFormatter2.format(new Date(rec_created * 1000));
    }
    
    public String getRecStartDate() {
        return DateFormatter3.format(new Date(rec_play_start * 1000));
    }
    
    public String getDurationString(){
        return (fm_play_duration/60 < 10 ? "0" + fm_play_duration/60 : fm_play_duration/60) + ":" + (fm_play_duration%60 < 10 ? "0" + fm_play_duration%60 : fm_play_duration%60);
    }
    
    public String getRecEndDate() {
        return DateFormatter3.format(new Date(rec_play_expire * 1000));
    }

    public void setRec_created(long rec_created) {
        this.rec_created = rec_created;
    }

    public int getFm_requency() {
        return fm_requency;
    }

    public void setFm_requency(int fm_requency) {
        this.fm_requency = fm_requency;
    }

    public int getFm_auto_switch_time() {
        return fm_auto_switch_time;
    }

    public void setFm_auto_switch_time(int fm_auto_switch_time) {
        this.fm_auto_switch_time = fm_auto_switch_time;
    }

    public int getRec_priority() {
        return rec_priority;
    }

    public void setRec_priority(int rec_priority) {
        this.rec_priority = rec_priority;
    }

    public int getRec_play_repeat_type() {
        return rec_play_repeat_type;
    }

    public void setRec_play_repeat_type(int rec_play_repeat_type) {
        this.rec_play_repeat_type = rec_play_repeat_type;
    }

    public int getRec_play_repeat_day() {
        return rec_play_repeat_day;
    }

    public void setRec_play_repeat_day(int rec_play_repeat_day) {
        this.rec_play_repeat_day = rec_play_repeat_day;
    }

    public int getRec_audio_format() {
        return rec_audio_format;
    }

    public void setRec_audio_format(int rec_audio_format) {
        this.rec_audio_format = rec_audio_format;
    }

  

}
