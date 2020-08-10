/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.util.List;

/**
 *
 * @author cuong.trinh
 */
public class UserLogin {
    private long user_id;
    private String username;
    private long role_id;
    private List<String> action_list;
    private String area_path;
    private long household_id;
    private String name;
    private String role_name;
    private int type;
    private String email;
    private long area_id;
    private String phone;
    private String id_card;
    
    
    
    

    public UserLogin() {
    }

    public long getArea_id() {
        return area_id;
    }

    public void setArea_id(long area_id) {
        this.area_id = area_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }




    public long getId() {
        return user_id;
    }

    public void setId(long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    public List<String> getAction_list() {
        return action_list;
    }

    public void setAction_list(List<String> action_list) {
        this.action_list = action_list;
    }

    public String getArea_path() {
        return area_path;
    }

    public void setArea_path(String area_path) {
        this.area_path = area_path;
    }

    public long getHousehold_id() {
        return household_id;
    }

    public void setHousehold_id(long household_id) {
        this.household_id = household_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
