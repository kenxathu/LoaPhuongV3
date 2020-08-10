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
public class DayObject {
    private String name;
    private int value;

    public DayObject(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public DayObject() {
    }
    
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    
    
    
}
