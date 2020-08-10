/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.admin.entity;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author ChienDX
 */
public class MenuAuthorizator implements Serializable{
    @SerializedName("action_url")
    private String modulePath;
    @SerializedName("permit_code")
    private String permissionCode;

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
    
    
}
