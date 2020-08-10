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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
 
public class AreaTree extends DefaultTreeNode implements Serializable {
    
    // code":"HNO","created_user":null,"parent_id":"0","name":"Hà Nội","id":"1","area_type_id":"1","status":"1"
    private long id;
    private String code;
    private String name;
    private int status;
    private String created_user;
    private Date createdDate;
    private long parent_id;
    private int area_type_id;
    
    private Boolean isCheck;
    private String audio_path;
     
  
     
     
    public AreaTree() {
       setType("document");
    }

    public long getAreaId() {
        return id;
    }

    public void setAreaId(long areaId) {
        this.id = areaId;
    }

    public String getAreaCode() {
        return code;
    }

    public void setAreaCode(String areaCode) {
        this.code = areaCode;
    }

    public String getAreaName() {
        return name;
    }

    public void setAreaName(String areaName) {
        this.name = areaName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedUser() {
        return created_user;
    }

    public void setCreatedUser(String createdUser) {
        this.created_user = createdUser;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public long getParentAreaId() {
        return parent_id;
    }

    public void setParentAreaId(long parentAreaId) {
        this.parent_id = parentAreaId;
    }

    public int getAreaType() {
        return area_type_id;
    }

    public void setAreaType(int areaType) {
        this.area_type_id = areaType;
    }

    @Override
    public String getType() {
         switch (area_type_id){
            case 0: setType("vn");
                    break;
            case 1: setType("tp");
                    break;
            case 2: setType("q");
                    break;
            case 3: setType("p");
                    break;
            case 4: setType("t");
                    break;
                    
            default: setType("document");
        }
        return super.getType(); //To change body of generated methods, choose Tools | Templates.
    }

    public String getAudio_path() {
        return audio_path;
    }

    public void setAudio_path(String audio_path) {
        this.audio_path = audio_path;
    }
    
    



}  