/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vu.vuongtat
 */
public class AiCamera implements Serializable {

    private long id;
    private String cam_id;
    private String cam_name;
    private String cam_position;
    private double lat;
    private double lon;
    private String create_date;
    private int status;
    private long areaId;
    private int announce_type;
    private long voice;

    private Date createDate;

    SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy-MM-dd");    //2017-07-31 06:38:45.867
    SimpleDateFormat DateFormatter2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");    //2017-07-31 06:38:45.867
    SimpleDateFormat DateFormatterFake = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat DateFormatterFake1 = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat DateFormatterFake2 = new SimpleDateFormat("HH:mm:ss");

    public AiCamera() {
    }

    public AiCamera(long id, String cam_id, String cam_name, String cam_position, double lat, double lon, String create_date, int status, long areaId, int announce_type, long voice) {
        this.id = id;
        this.cam_id = cam_id;
        this.cam_name = cam_name;
        this.cam_position = cam_position;
        this.lat = lat;
        this.lon = lon;
        this.create_date = create_date;
        this.status = status;
        this.areaId = areaId;
        this.announce_type = announce_type;
        this.voice = voice;
    }

    public AiCamera(AiCamera aiCam) {
        this.id = aiCam.id;
        this.cam_id = aiCam.cam_id;
        this.cam_name = aiCam.cam_name;
        this.cam_position = aiCam.cam_position;
        this.lat = aiCam.lat;
        this.lon = aiCam.lon;
        this.create_date = aiCam.create_date;
        this.status = aiCam.status;
        this.areaId = aiCam.areaId;
        this.announce_type = aiCam.announce_type;
        this.voice = aiCam.voice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCam_id() {
        return cam_id;
    }

    public void setCam_id(String cam_id) {
        this.cam_id = cam_id;
    }

    public String getCam_name() {
        return cam_name;
    }

    public void setCam_name(String cam_name) {
        this.cam_name = cam_name;
    }

    public String getCam_position() {
        return cam_position;
    }

    public void setCam_position(String cam_position) {
        this.cam_position = cam_position;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public Date getCreateDate() {
        try {
            createDate = DateFormatter2.parse(create_date);
        } catch (ParseException ex) {
            Logger.getLogger(AiCamera.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getAnnounce_type() {
        return announce_type;
    }

    public void setAnnounce_type(int announce_type) {
        this.announce_type = announce_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getVoice() {
        return voice;
    }

    public void setVoice(long voice) {
        this.voice = voice;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

}
