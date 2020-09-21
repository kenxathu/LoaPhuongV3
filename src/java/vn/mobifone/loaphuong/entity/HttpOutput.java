package vn.mobifone.loaphuong.entity;

import vn.mobifone.loaphuong.entity.OtpResponse;
import vn.mobifone.loaphuong.entity.WebserviceOutput;

public class HttpOutput {
  private String headerHttp;
  
  private String messageHttp;
  
  private String dataSent;
  
  private WebserviceOutput wsOutput;
  
  private OtpResponse otpRes;
  
  private String responseJson;
  
  private boolean success;
  
  public boolean isSuccess() {
    return this.success;
  }
  
  public void setSuccess(boolean success) {
    this.success = success;
  }
  
  public String getResponseJson() {
    return this.responseJson;
  }
  
  public void setResponseJson(String responseJson) {
    this.responseJson = responseJson;
  }
  
  public String getDataSent() {
    return this.dataSent;
  }
  
  public void setDataSent(String dataSent) {
    this.dataSent = dataSent;
  }
  
  public WebserviceOutput getWsOutput() {
    return this.wsOutput;
  }
  
  public void setWsOutput(WebserviceOutput wsOutput) {
    this.wsOutput = wsOutput;
  }
  
  public String getHeaderHttp() {
    return this.headerHttp;
  }
  
  public void setHeaderHttp(String headerHttp) {
    this.headerHttp = headerHttp;
  }
  
  public String getMessageHttp() {
    return this.messageHttp;
  }
  
  public void setMessageHttp(String messageHttp) {
    this.messageHttp = messageHttp;
  }
  
  public OtpResponse getOtpRes() {
    return this.otpRes;
  }
  
  public void setOtpRes(OtpResponse otpRes) {
    this.otpRes = otpRes;
  }
}
