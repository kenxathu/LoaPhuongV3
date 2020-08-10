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


public class SysLog implements Serializable{
    private long id;
    private String log_name;
    private String action_code;
    private String username;
    private String request_content;
    private String response_content;
    private String created_date;
    
    
    
    public SysLog() {
    }

    public SysLog(long id, String log_name, String log_code, String username, String input_data, String output_data, String date) {
        this.id = id;
        this.log_name = log_name;
        this.action_code = log_code;
        this.username = username;
        this.request_content = input_data;
        this.response_content = output_data;
        this.created_date = date;
    }
    
    

    public long getId() {
        return id;
    }

    public String getLog_name() {
        String name = "";
//        switch (action_code){
//            case "DELETE_USER":
//                name = "Xóa người dùng";
//                break;
//            case "CREATE_USER":
//                name = "Tạo người dùng";
//                break;
//            case "CREATE_AREA":
//                name = "Thêm địa bàn";
//                break;
//            case "DELETE_AREA":
//                name = "Xóa địa bàn";
//                break;
//            case "UPDATE_AREA":
//                name = "Sửa địa bàn";
//                break;
//            case "CREATE_RECORD":
//                name = "Tạo thông báo";
//                break;
//            case "DELETE_RECORD":
//                name = "Xóa thông báo";
//                break;
//            case "APPROVE_RECORD":
//                name = "Phê duyệt thông báo";
//                break;
//            case "CANCEL_RECORD":
//                name = "Hủy thông báo";
//                break;
//            default: name = action_code;
//              
//        }
        
        return name;
    }

    public String getLog_code() {
        return action_code;
    }

    public String getUsername() {
        return username;
    }

    public String getInput_data() {
        return request_content;
    }

    public String getOutput_data() {
        return response_content;
    }

    public String getDate() {
        return created_date;
    }

}
