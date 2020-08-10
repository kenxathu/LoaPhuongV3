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
 * 
 * 

 */


public class MCULog extends Object implements Serializable{
    private long log_index;
    private String created_date;
    private long mcu_id;
    private int amount_record;

    public MCULog() {
    }

    public long getLog_index() {
        return log_index;
    }

    public void setLog_index(long log_index) {
        this.log_index = log_index;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public long getMcu_id() {
        return mcu_id;
    }

    public void setMcu_id(long mcu_id) {
        this.mcu_id = mcu_id;
    }

    public int getAmount_record() {
        return amount_record;
    }

    public void setAmount_record(int amount_record) {
        this.amount_record = amount_record;
    }

    @Override
    public String toString() {
        return created_date.substring(created_date.indexOf(" "));
    }

    
   

   
    

}
