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
public class ListDataResponse {
    private String codeDescVn;

    private String codeDescEn;
    private int code;

    public List<Object> javaResponse;
    
    
    
    public ListDataResponse() {
    }

    
    
    public String getCodeDescVn() {
        return codeDescVn;
    }

    public void setCodeDescVn(String codeDescVn) {
        this.codeDescVn = codeDescVn;
    }



    public String getCodeDescEn() {
        return codeDescEn;
    }

    public void setCodeDescEn(String codeDescEn) {
        this.codeDescEn = codeDescEn;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Object> getJavaResponse() {
        return javaResponse;
    }

    public void setJavaResponse(List<Object> javaResponse) {
        this.javaResponse = javaResponse;
    }
    

}

