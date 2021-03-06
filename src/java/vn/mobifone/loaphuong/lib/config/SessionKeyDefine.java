package vn.mobifone.loaphuong.lib.config;

import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Khai báo các session key
 *
 * @author CuongTN
 */
public class SessionKeyDefine implements Serializable {

    public static String USER = "mbftuser";
    public static String LIST_MODULE = "mbflistmodule";
    public static String LIST_MODULE_AUTH = "mbflistmoduleauth";
    public static String MAP_MODULE_AUTH = "mbfmapmoduleauth";
    public static String MODULE_AUTH = "mbfma";
    public static String LOGIN_FAIL = "mbfloginfail";
    public static String LOGIN_FAIL_USER = "mbfloginfailuser";
       
    public static String OTP_FAIL = "mbfotpfail";
  
    public static String OTP_FAIL_USER = "mbfotpfailuser";
  
    public static String RESEND_OTP_FAIL = "mbfresendotpfail";
  
    public static String RESEND_OTP_FAIL_USER = "mbfresendfailuser";
        
    public static String START_TIME_LOGIN_FAIL = "mbfloginfailstartime";
    public static String PERMIT_CODE = "mbfloginpermit";
    public static String ALLOW_HOUSE_MANAGEMENT = "allowhousemanagement";
    public static String TIME_TABLE_LIST = "timetablelist";
    public static String TIME_TABLE_STRING = "timetablestring";
    public static String MAIN_AREA = "mainarea";
    public static String SESSION_ID = "sessionid";
    public static String LOGIN_DATA = "logindata";
    public static String STATUS_OTP = "statusotp";
    public static String USER_NAME = "mbfusername";
    public static String EXPIRE_TIME = "SESSION_EXPIRE_TIME";
}
