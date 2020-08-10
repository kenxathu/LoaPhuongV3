/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.role;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author tho.nt
 */
//{"name":"Quản trị hệ thống","id":"1"}
//
public class Role {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String roleName;
    @SerializedName("user_id")
    private long userId;

    public Role() {
    }

    public Role(long roleId, String roleName) {
        this.id = roleId;
        this.roleName = roleName;
    }

    public long getRoleId() {
        return id;
    }

    public void setRoleId(long roleId) {
        this.id = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
}
