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
public class HttpOutput {
    private String headerHttp;
    private String messageHttp;  
    private String dataSent;
    private WebserviceOutput wsOutput;
    private String responseJson;
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }
    
    
    
    
    public String getDataSent() {
        return dataSent;
    }

    public void setDataSent(String dataSent) {
        this.dataSent = dataSent;
    }
    


    public WebserviceOutput getWsOutput() {
        return wsOutput;
    }

    public void setWsOutput(WebserviceOutput wsOutput) {
        this.wsOutput = wsOutput;
    }
    
    public String getHeaderHttp() {
        return headerHttp;
    }

    public void setHeaderHttp(String headerHttp) {
        this.headerHttp = headerHttp;
    }

    public String getMessageHttp() {
        return messageHttp;
    }

    public void setMessageHttp(String messageHttp) {
        this.messageHttp = messageHttp;
    }
    
}
