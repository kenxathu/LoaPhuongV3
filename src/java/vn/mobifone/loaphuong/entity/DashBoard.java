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


//{"count_record":"0","count_user":"1","count_mcu":"0","count_area":"4","count_news":"0","count_household":"2"}
public class DashBoard implements Serializable{
    private long count_record;
    private long count_user;
    private long count_mcu;
    private long count_area;
    private long count_news;
    private long count_household;

    public DashBoard() {
    }

    public long getCount_record() {
        return count_record;
    }

    public void setCount_record(long count_record) {
        this.count_record = count_record;
    }

    public long getCount_user() {
        return count_user;
    }

    public void setCount_user(long count_user) {
        this.count_user = count_user;
    }

    public long getCount_mcu() {
        return count_mcu;
    }

    public void setCount_mcu(long count_mcu) {
        this.count_mcu = count_mcu;
    }

    public long getCount_area() {
        return count_area;
    }

    public void setCount_area(long count_area) {
        this.count_area = count_area;
    }

    public long getCount_news() {
        return count_news;
    }

    public void setCount_news(long count_news) {
        this.count_news = count_news;
    }

    public long getCount_household() {
        return count_household;
    }

    public void setCount_household(long count_household) {
        this.count_household = count_household;
    }
    
    

    

}
