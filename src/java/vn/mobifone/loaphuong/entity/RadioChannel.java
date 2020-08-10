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
public class RadioChannel implements Serializable{
    private long id;
    private String name;
    private String description;
    private String url;
    private double frequency;

    public RadioChannel(long id, String name, String description, String url, double frequency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.frequency = frequency;
    }

    public RadioChannel() {
    }
    
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    
}
