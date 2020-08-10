/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;

/**
 *
 * @author vu179
 */
public class AiCameraSlot implements Serializable {

    private long start_time;
    private long end_time;
    private String ai_camera_id;
    private int type; // 1- Live, 2- Off

    public AiCameraSlot() {
    }

    public AiCameraSlot(int type) {
        this.type = type;
    }

    public AiCameraSlot(String ai_camera_id, int type) {
        this.ai_camera_id = ai_camera_id;
        this.type = type;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getAi_camera_id() {
        return ai_camera_id;
    }

    public void setAi_camera_id(String ai_camera_id) {
        this.ai_camera_id = ai_camera_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
