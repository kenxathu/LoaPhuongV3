/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

/**
 *
 * @author Administrator
 */
public class Otp {
    private String sessionId;
    private String otpCode;

    public Otp(String sessionId, String otpCode) {
        this.sessionId = sessionId;
        this.otpCode = otpCode;
    }

    public Otp() {
        
    }
    
    

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

  
    
    
}
