/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;

/**
 *
 * @author vu.vuongtat
 */
public class AiCameraMCU implements Serializable {

    private long id;
    private String ai_camera_id;
    private long mcu_id;
    private int direction;

    public AiCameraMCU() {
    }

    public AiCameraMCU(long id, String ai_camera_id, long mcu_id, int direction) {
        this.id = id;
        this.ai_camera_id = ai_camera_id;
        this.mcu_id = mcu_id;
        this.direction = direction;
    }

    public AiCameraMCU(AiCameraMCU aiCameraMCU) {
        this.id = aiCameraMCU.id;
        this.ai_camera_id = aiCameraMCU.ai_camera_id;
        this.mcu_id = aiCameraMCU.mcu_id;
        this.direction = aiCameraMCU.direction;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAi_camera_id() {
        return ai_camera_id;
    }

    public void setAi_camera_id(String ai_camera_id) {
        this.ai_camera_id = ai_camera_id;
    }

    public long getMcu_id() {
        return mcu_id;
    }

    public void setMcu_id(long mcu_id) {
        this.mcu_id = mcu_id;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

}
