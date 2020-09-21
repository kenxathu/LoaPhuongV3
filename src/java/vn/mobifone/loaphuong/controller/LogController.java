/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

import vn.mobifone.loaphuong.user.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.entity.HouseHold;
import vn.mobifone.loaphuong.entity.SysLog;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.TSPermission;
import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.security.Serializables;
import vn.mobifone.loaphuong.util.MD5Encoder;
import vn.mobifone.loaphuong.util.ResourceBundleUtil;
import vn.mobifone.loaphuong.util.ValidatorUtil;
import vn.mobifone.loaphuong.webservice.LoginWebservice;
import vn.mobifone.loaphuong.webservice.ServeyWebservice;

/**
 * Description: UserController Class Date: 28/08/2015 09:26:16 Version: 1.0
 *
 * @author: ChienDX
 */
@ManagedBean(name = "LogController")
@ViewScoped
//Quản lý NSD
public class LogController extends TSPermission implements Serializables {

    /**
     * Fields
     */
    private SysLog selectedLog;
    private List<SysLog> listLog;
    private UserModel muserModel;

      //time filter
    private Date fromDate;
    private Date toDate;

    public LogController() {
        
        toDate = new Date();
        Calendar cal = Calendar.getInstance();  
        cal.add(Calendar.DATE, -7);  
        fromDate = cal.getTime();
        loadLog();
        
//        listLog = new ArrayList<>();
//        listLog.add(new SysLog(11, "Tạo thông báo", "CREATE_RECORD", "hanoi.admin3", "{\"rec_id\":0,\"rec_summary\":\"thông báo 1\",\"rec_audio\":\"20180419\\/20180419150524_1524125169451_tlbb1p.wav.wav\",\"rec_audio_codec\":1,\"rec_play_mode\":2,\"rec_priority\":4,\"rec_play_time\":[],\"rec_play_repeat_type\":-1,\"rec_play_repeat_days\":0,\"rec_play_start\":\"19-04-2018\",\"rec_play_expire\":\"19-04-2018\",\"rec_receipt_type\":1,\"rec_receipt_ids\":[1080],\"category_id\":1,\"create_user\":146,\"rec_type\":0,\"notify_flag\":1,\"area_id\":1,\"duration\":59,\"force_override\":0}", 
//                "{\"code\":200,\"codeDescVn\":\"Thành công\",\"codeDescEn\":\"Success\",\"javaResponse\":[]}", "20-04-2018 09:20:14"));
//        listLog.add(new SysLog(11, "Tạo thông báo", "CREATE_RECORD", "hanoi.admin3", "{\"rec_id\":0,\"rec_summary\":\"thông báo 1\",\"rec_audio\":\"20180419\\/20180419150524_1524125169451_tlbb1p.wav.wav\",\"rec_audio_codec\":1,\"rec_play_mode\":2,\"rec_priority\":4,\"rec_play_time\":[],\"rec_play_repeat_type\":-1,\"rec_play_repeat_days\":0,\"rec_play_start\":\"19-04-2018\",\"rec_play_expire\":\"19-04-2018\",\"rec_receipt_type\":1,\"rec_receipt_ids\":[1080],\"category_id\":1,\"create_user\":146,\"rec_type\":0,\"notify_flag\":1,\"area_id\":1,\"duration\":59,\"force_override\":0}", 
//                "{\"code\":200,\"codeDescVn\":\"Thành công\",\"codeDescEn\":\"Success\",\"javaResponse\":[]}", "20-04-2018 09:20:14"));
//        listLog.add(new SysLog(11, "Tạo thông báo", "CREATE_RECORD", "hanoi.admin3", "{\"rec_id\":0,\"rec_summary\":\"thông báo 1\",\"rec_audio\":\"20180419\\/20180419150524_1524125169451_tlbb1p.wav.wav\",\"rec_audio_codec\":1,\"rec_play_mode\":2,\"rec_priority\":4,\"rec_play_time\":[],\"rec_play_repeat_type\":-1,\"rec_play_repeat_days\":0,\"rec_play_start\":\"19-04-2018\",\"rec_play_expire\":\"19-04-2018\",\"rec_receipt_type\":1,\"rec_receipt_ids\":[1080],\"category_id\":1,\"create_user\":146,\"rec_type\":0,\"notify_flag\":1,\"area_id\":1,\"duration\":59,\"force_override\":0}", 
//                "{\"code\":200,\"codeDescVn\":\"Thành công\",\"codeDescEn\":\"Success\",\"javaResponse\":[]}", "20-04-2018 09:20:14"));
//        listLog.add(new SysLog(11, "Tạo thông báo", "CREATE_RECORD", "hanoi.admin3", "{\"rec_id\":0,\"rec_summary\":\"thông báo 1\",\"rec_audio\":\"20180419\\/20180419150524_1524125169451_tlbb1p.wav.wav\",\"rec_audio_codec\":1,\"rec_play_mode\":2,\"rec_priority\":4,\"rec_play_time\":[],\"rec_play_repeat_type\":-1,\"rec_play_repeat_days\":0,\"rec_play_start\":\"19-04-2018\",\"rec_play_expire\":\"19-04-2018\",\"rec_receipt_type\":1,\"rec_receipt_ids\":[1080],\"category_id\":1,\"create_user\":146,\"rec_type\":0,\"notify_flag\":1,\"area_id\":1,\"duration\":59,\"force_override\":0}", 
//                "{\"code\":200,\"codeDescVn\":\"Thành công\",\"codeDescEn\":\"Success\",\"javaResponse\":[]}", "20-04-2018 09:20:14"));
    }

    ////////////////////////////////////////////////////////
    public void viewLog(SysLog log) {
        selectedLog = log;
    }
    ////////////////////////////////////////////////////////////////////////
     public void changeFilterTimeEvent(java.awt.event.ActionEvent evt) { 
        if (fromDate.after(toDate)) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Khoảng thời gian đã chọn không hợp lệ! Vui lòng kiểm tra lại");
            return;
        }
        loadLog();
    }
     
    private void loadLog(){
        listLog = new ServeyWebservice().getListSysLog(SecUser.getUserLogged().getAreaId(), fromDate, toDate);
    }

    public SysLog getSelectedLog() {
        return selectedLog;
    }

    public List<SysLog> getListLog() {
        return listLog;
    }

   

}
