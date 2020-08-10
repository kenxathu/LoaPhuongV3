/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;

/**
 *
 * @author cuong.trinh
 */
public class HouseHold implements Serializable {
    private long id;
    private String holder_name;
    private String address;
    private long area_id;
    private String area_path;
    private String areaName;
    private long mcu_id;
    private String area_name;

    public HouseHold() {
    }

    
    
    public HouseHold(Long id, String holder_name) {
        this.id = id;
        this.holder_name = holder_name;
    }

    public long getId() {
        return id;
    }

    public long getMcu_id() {
        return mcu_id;
    }

    public void setMcu_id(long mcu_id) {
        this.mcu_id = mcu_id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHolder_name() {
        return holder_name;
    }

    public void setHolder_name(String holder_name) {
        this.holder_name = holder_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getArea_id() {
        return area_id;
    }

    public void setArea_id(long area_id) {
        this.area_id = area_id;
    }

    public String getArea_path() {
        return area_path;
    }

    public void setArea_path(String area_path) {
        this.area_path = area_path;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }
    
   
}
