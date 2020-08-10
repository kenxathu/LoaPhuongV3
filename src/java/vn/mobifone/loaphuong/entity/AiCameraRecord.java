/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author vu.vuongtat
 */
public class AiCameraRecord implements Serializable {

    private long id;
    private String ai_camera_id;
    private String content;
    private Date create_date;
    private int status;
    private String rec_receipt;
    private Date pushed_date;
    private int direction;

    public AiCameraRecord() {
    }

    public AiCameraRecord(long id, String ai_camera_id, String content, Date create_date, int status, String rec_receipt, Date pushed_date, int direction) {
        this.id = id;
        this.ai_camera_id = ai_camera_id;
        this.content = content;
        this.create_date = create_date;
        this.status = status;
        this.rec_receipt = rec_receipt;
        this.pushed_date = pushed_date;
        this.direction = direction;
    }

    public AiCameraRecord(AiCameraRecord aiCameraRecord) {
        this.id = aiCameraRecord.id;
        this.ai_camera_id = aiCameraRecord.ai_camera_id;
        this.content = aiCameraRecord.content;
        this.create_date = aiCameraRecord.create_date;
        this.status = aiCameraRecord.status;
        this.rec_receipt = aiCameraRecord.rec_receipt;
        this.pushed_date = aiCameraRecord.pushed_date;
        this.direction = aiCameraRecord.direction;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getRec_receipt() {
        return rec_receipt;
    }

    public void setRec_receipt(String rec_receipt) {
        this.rec_receipt = rec_receipt;
    }

    public Date getPushed_date() {
        return pushed_date;
    }

    public void setPushed_date(Date pushed_date) {
        this.pushed_date = pushed_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

}
