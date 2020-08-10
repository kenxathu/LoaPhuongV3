/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

/**
 *
 * @author cuong.trinh
 */
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.primefaces.model.TreeNode;
 
public class AreaEntity implements Serializable {
    private Long areaId;
    private String areaCode;
    private String areaName;
    private int status;
    private String createdUser;
    private Date createdDate;
    private Long parentAreaId;
    private int areaType;
     
    
     
    private String type;
     
    public AreaEntity(String name, int type) {
        this.areaName = name;
        this.areaType = type;
    }

    public AreaEntity() {
       
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getParentAreaId() {
        return parentAreaId;
    }

    public void setParentAreaId(Long parentAreaId) {
        this.parentAreaId = parentAreaId;
    }

    public int getAreaType() {
        return areaType;
    }

    public void setAreaType(int areaType) {
        this.areaType = areaType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}  