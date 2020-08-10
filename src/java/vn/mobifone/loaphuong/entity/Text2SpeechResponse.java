/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.util.List;
import vn.mobifone.loaphuong.entity.UserLogin;

/**
 *
 * @author cuong.trinh
 */
public class Text2SpeechResponse {
    private String async;
    private String request_id;
    private String message;
    private int error;

    
    
    
    public Text2SpeechResponse() {
    }

    public String getAsync() {
        return async;
    }

    public void setAsync(String async) {
        this.async = async;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }



}

