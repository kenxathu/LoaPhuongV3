/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cuong.trinh
 */
public class Record implements Serializable{
    private int rec_play_repeat_type;
    private int rec_receipt_type;
    private int notify_flag = 1;
    private int status;
    private List<Integer> rec_play_time;
    private List<Integer> rec_end_time;
    private long category_id;
    private String rec_play_start="";
    private String rec_play_expire_date="";
    private List<Long> rec_receipt;
    private int rec_play_mode = 0;
    private int notify_status;
    private String create_date;
    private int rec_type;
    private int rec_audio_codec;
    private String update_date;
    private String rec_summary;
    private String create_user;
    private long rec_play_repeat_days;
    private long rec_id;
    private int rec_priority;
    private String rec_url;
    private String rec_receipt_name;
    private String area_name;
    private long area_type_id; 
    private long duration;
    private String approve_date;
    private String create_username;
    private String approve_username;
    
    private String categoryName;
    private String rec_receiptString;
    private String rec_play_timeString;
    private String rec_play_timeStringRadio;
    private Date rec_playExpireDate;
    private Date rec_playStartDate;
    private String rec_repeatDayString;
    private Boolean notify_flagBoolean;
    private String recFileName;
    private long areaId;
    private String listPlayName;
    private Date createDate;
    private boolean isNext = false;
    private boolean is_link = false;
    private long rec_link;
    private String playTimeRecLink;
    

    SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy-MM-dd");    //2017-07-31 06:38:45.867
    SimpleDateFormat DateFormatter2 = new SimpleDateFormat("dd-MM-yyyy");    //2017-07-31 06:38:45.867
    SimpleDateFormat DateFormatter3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 

    public Record(Record rec) {
        this.rec_play_repeat_type = rec.rec_play_repeat_type;
        this.rec_receipt_type = rec.rec_receipt_type;
        this.status = rec.status;
        this.rec_play_time = rec.rec_play_time;
        this.category_id = rec.category_id;
        this.rec_receipt = rec.rec_receipt;
        this.notify_status = rec.notify_status;
        this.create_date = rec.create_date;
        this.rec_type = rec.rec_type;
        this.rec_audio_codec = rec.rec_audio_codec;
        this.update_date = rec.update_date;
        this.rec_summary = rec.rec_summary;
        this.create_user = rec.create_user;
        this.rec_play_repeat_days = rec.rec_play_repeat_days;
        this.rec_id = rec.rec_id;
        this.rec_priority = rec.rec_priority;
        this.rec_url = rec.rec_url;
        this.rec_receipt_name = rec.rec_receipt_name;
        this.area_name = rec.area_name;
        this.area_type_id = rec.area_type_id;
        this.categoryName = rec.categoryName;
        this.rec_receiptString = rec.rec_receiptString;
        this.rec_repeatDayString = rec.rec_repeatDayString;
        this.recFileName = rec.recFileName;
        this.areaId = rec.areaId;
        this.listPlayName = rec.listPlayName;
        this.notify_flag = rec.notify_flag;
        this.rec_play_start = rec.rec_play_start;
        this.rec_play_expire_date = rec.rec_play_expire_date;
        this.rec_play_mode = rec.rec_play_mode;
        this.duration = rec.duration;
        this.createDate = rec.createDate;
        this.rec_end_time = rec.rec_end_time;
        //08_Apr_2020
        this.is_link = rec.is_link;
        this.rec_link = rec.rec_link;
    }

    public Record() {
    }

 
    

    public Date getRec_playExpireDate() {
        //rec_playExpireDate = new Date();
        try {
            
            rec_playExpireDate = rec_play_expire_date.equals("")?null:DateFormatter2.parse(rec_play_expire_date);
        } catch (ParseException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rec_playExpireDate;

    }

    public void setRec_playExpireDate(Date rec_playExpireDate) {
        this.rec_playExpireDate = rec_playExpireDate;
        try {
           //rec_playExpireDate = rec_play_expire_date.equals("")?new Date():DateFormatter2.parse(rec_play_expire_date);
           rec_play_expire_date = DateFormatter2.format(rec_playExpireDate);

        } catch (Exception e) {
            
        }
    }

    public String getRec_receiptString() {
        String rec_receiptString = "";
        try {
            rec_receiptString = rec_receipt.toString().substring(1, rec_receipt.toString().length()-1);
            return rec_receiptString.replaceAll("\\s","");
        } catch (Exception e) {
            
        }
        return rec_receiptString;
    }

    public void setRec_receiptString(String rec_receiptString) {
        this.rec_receiptString = rec_receiptString;
        rec_receipt = new ArrayList<>();
            try {
                List<String> valueArr = Arrays.asList(rec_receiptString.split(","));
                for (String string : valueArr) {
                    rec_receipt.add(Long.parseLong(string));
                }
            } catch (Exception e) {
        }

    }

    public String getRec_play_timeString() {
        String rec_play_timeString = "";
        try {
            List<String> timeList = convertNumberToTime(rec_play_time);
            
            rec_play_timeString = timeList.toString().substring(1, timeList.toString().length()-1);
            return rec_play_timeString;
        } catch (Exception e) {
            
        }
        return rec_play_timeString;
    }

    public void setRec_play_timeString(String rec_play_timeString) {
        this.rec_play_timeString = rec_play_timeString;
        rec_play_time = new ArrayList<>();
            try {
                rec_play_time = convertTimeToNumber(rec_play_timeString);
                
//                List<String> valueArr = Arrays.asList(rec_play_timeString.split(","));
//                for (String string : valueArr) {
//                    rec_play_time.add(Integer.parseInt(string));
//                }
            } catch (Exception e) {
        }
    }

    public String getRec_play_timeStringRadio() {
        String rec_play_timeStringRadio = "";
        try {
            List<String> timeList = convertNumberToTimeForRadio(rec_play_time, rec_end_time);
            
            rec_play_timeStringRadio = timeList.toString().substring(1, timeList.toString().length()-1);
            return rec_play_timeStringRadio;
        } catch (Exception e) {
            
        }
        return rec_play_timeStringRadio;
    }

    public void setRec_play_timeStringRadio(String rec_play_timeStringRadio) {
        this.rec_play_timeStringRadio = rec_play_timeStringRadio;
        rec_play_time = new ArrayList<>();
        rec_end_time = new ArrayList<>();
            try {
                convertTimeToNumberForRadio(rec_play_timeStringRadio);

            } catch (Exception e) {
        }
    }

   
    
    

    public String getRec_play_start() {
        return rec_play_start;
    }

    public void setRec_play_start(String rec_play_start) {
        this.rec_play_start = rec_play_start;
    }

    public Date getRec_playStartDate() {
        //rec_playStartDate = new Date();
        try {
            rec_playStartDate = rec_play_start.equals("")?null:DateFormatter2.parse(rec_play_start);
//            rec_playStartDate = DateFormatter2.parse(rec_play_start);
        } catch (ParseException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rec_playStartDate;
    }

    public void setRec_playStartDate(Date rec_playStartDate) {
        this.rec_playStartDate = rec_playStartDate;
        try {
           rec_play_start = DateFormatter2.format(rec_playStartDate);

        } catch (Exception e) {
            
        }
    }
    

    public int getRec_play_repeat_type() {
        return rec_play_repeat_type;
    }

    public void setRec_play_repeat_type(int rec_play_repeat_type) {
        this.rec_play_repeat_type = rec_play_repeat_type;
    }

    public int getRec_receipt_type() {
        return rec_receipt_type;
    }

    public void setRec_receipt_type(int rec_receipt_type) {
        this.rec_receipt_type = rec_receipt_type;
    } 

    public int getNotify_flag() {
        return notify_flag;
    }

    public void setNotify_flag(int notify_flag) {
        this.notify_flag = notify_flag;
    }
    
//    public int getPlayStatus() {
////        try {
////            rec_playExpireDate = rec_play_expire_date.equals("")?null:DateFormatter2.parse(rec_play_expire_date);
////        } catch (ParseException ex) {
////            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
////        }
////        if (!rec_playExpireDate.after(new Date())) {
////            return 1; 
////        } else if (status == -1) {
////            return 1;
////        } else return 2;
//
//            
//            /*try {
//                rec_playExpireDate = rec_play_expire_date.equals("")?null:DateFormatter2.parse(rec_play_expire_date);
//                
//                String expireDate = DateFormatter.format(rec_playExpireDate);
//                String today = DateFormatter.format(new Date());
//                if (expireDate.compareTo(today) < 0) {
//                    return 1; 
//                } else if (status == -1) {
//                    return 1;
//                } else return 2;
//            } catch (ParseException ex) {
//                Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            return 2;*/
//        try {
//            if (status == -1) {
//                //Da huy
//                return -1;
//            }else if (status == 1){
//                rec_playExpireDate = rec_play_expire_date.equals("")?null:DateFormatter2.parse(rec_play_expire_date);
//                rec_playStartDate = rec_play_start.equals("")?null:DateFormatter2.parse(rec_play_start);
//                String expireDate = DateFormatter.format(rec_playExpireDate);
//                String startDate = DateFormatter.format(rec_playStartDate);
//                String today = DateFormatter.format(new Date());
//                
//                if (today.compareTo(startDate)<0){
//                    //Chua den lich phat
//                    return 3;
//                }else if (today.compareTo(expireDate)> 0){
//                    //Da hoan thanh
//                    return 2;
//                }else{
//                    Calendar now = Calendar.getInstance();
//                    Calendar midnight = Calendar.getInstance();
//                    midnight.set(Calendar.HOUR_OF_DAY, 0);
//                    midnight.set(Calendar.MINUTE, 0);
//                    midnight.set(Calendar.SECOND, 0);
//                    midnight.set(Calendar.MILLISECOND, 0);
//                    long timeNow = (now.getTime().getTime() - midnight.getTime().getTime())/1000;
//                    
//                    
//                    switch (rec_play_mode){
//                        case 1:// phat theo lich
//                            if (rec_type == 2){
//                                if(rec_play_time.size()>1){
//                                      Collections.sort(rec_play_time);
//                                }
//                                if(rec_end_time.size()>1){
//                                      Collections.sort(rec_end_time);
//                                }
//                                if (timeNow < rec_play_time.get(0)){
//                                    if (rec_play_time.get(0) - timeNow <=30*60){
//                                        return 0;
//                                    }else{
//                                        //Chua den lich phat
//                                        return 3;
//                                    }
//                                }else if (timeNow> rec_end_time.get(rec_end_time.size()-1)){
//                                    //Da hoan thanh
//                                    return 2;
//                                }else{
//                                    boolean isPlaying = false;
//                                    for (int i=0; i< rec_play_time.size(); i++) {
//                                        if (timeNow >= rec_play_time.get(i) && timeNow <= rec_end_time.get(i)){
//                                            isPlaying = true;
//                                            break;
//                                        }else if (i < rec_play_time.size() - 1 && timeNow > rec_end_time.get(i) && timeNow <rec_play_time.get(i+1)){
//                                            if (rec_play_time.get(i+1) - timeNow <= 30*60){
//                                                return 0;
//                                            }else{
//                                                return 3;
//                                            }
//                                        }
//                                    }
//                                    if (isPlaying){
//                                        return 1;
//                                    }else{
//                                        return 0;
//                                    }
//                                }
//                            }else{
//                                if(rec_play_time.size()>1){
//                                      Collections.sort(rec_play_time);
//                                }
//                                if (timeNow < rec_play_time.get(0)){
//                                    if (rec_play_time.get(0) - timeNow <=30*60){
//                                        return 0;
//                                    }else{
//                                        //Chua den lich phat
//                                        return 3;
//                                    }
//                                }else if (timeNow> rec_play_time.get(rec_play_time.size()-1) + duration){
//                                    //Da hoan thanh
//                                    return 2;
//                                }else{
//                                    boolean isPlaying = false;
//                                    for (int i=0; i< rec_play_time.size(); i++) {
//                                        if (timeNow >= rec_play_time.get(i) && timeNow <= rec_play_time.get(i) + duration){
//                                            isPlaying = true;
//                                            break;
//                                        }else if (i < rec_play_time.size() - 1 && timeNow > rec_play_time.get(i) + duration && timeNow <rec_play_time.get(i+1)){
//                                            if (rec_play_time.get(i+1) - timeNow <= 30*60){
//                                                return 0;
//                                            }else{
//                                                return 3;
//                                            }
//                                        }
//                                    }
//                                    if (isPlaying){
//                                        return 1;
//                                    }else{
//                                        return 0;
//                                    }
//                                }
//                            }
//                        case 2: //phat ngay lap tuc
//                            
//                            if (rec_type == 0){// tin thuong
//                                long playTime = (DateFormatter3.parse(approve_date).getTime() - midnight.getTime().getTime())/1000;
//                                if (timeNow< playTime){
//                                    if (playTime - timeNow <=30*60){
//                                        return 0;
//                                    }else{
//                                        //Chua den lich phat
//                                        return 3;
//                                    }
//                                }else if (timeNow> playTime+ duration){
//
//                                    //Da hoan thanh
//                                    return 2;
//                                }
//                                else{
//                                    //Dang phat
//                                    return 1;
//                                }
//                            }else if (rec_type==2){//tin tiep song
//                                int playTime = rec_play_time.get(0);
//                                int endTime = rec_end_time.get(0);
//                                if (timeNow< playTime){
//                                    if (playTime - timeNow <=30*60){
//                                        return 0;
//                                    }else{
//                                        //Chua den lich phat
//                                        return 3;
//                                    }
//                                }else if (timeNow> endTime){
//                                    //Da hoan thanh
//                                    return 2;
//                                }
//                                else{
//                                    //Dang phat
//                                    return 1;
//                                }
//                            }else{
//                                return -2;
//                            }
//                    }
//                }
//            }
//        }catch (ParseException ex) {
//            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return -2;
//    } 
    
    public int getPlayStatus() {
//        try {
//            rec_playExpireDate = rec_play_expire_date.equals("")?null:DateFormatter2.parse(rec_play_expire_date);
//        } catch (ParseException ex) {
//            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (!rec_playExpireDate.after(new Date())) {
//            return 1; 
//        } else if (status == -1) {
//            return 1;
//        } else return 2;

            
            /*try {
                rec_playExpireDate = rec_play_expire_date.equals("")?null:DateFormatter2.parse(rec_play_expire_date);
                
                String expireDate = DateFormatter.format(rec_playExpireDate);
                String today = DateFormatter.format(new Date());
                if (expireDate.compareTo(today) < 0) {
                    return 1; 
                } else if (status == -1) {
                    return 1;
                } else return 2;
            } catch (ParseException ex) {
                Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 2;*/
        try {
            if (status == -1) {
                //Da huy
                return -1;
            }else if (status == 1){
                rec_playExpireDate = rec_play_expire_date.equals("")?null:DateFormatter2.parse(rec_play_expire_date);
                rec_playStartDate = rec_play_start.equals("")?null:DateFormatter2.parse(rec_play_start);
                String expireDate = DateFormatter.format(rec_playExpireDate);
                String startDate = DateFormatter.format(rec_playStartDate);
                String today = DateFormatter.format(new Date());
                
                if (today.compareTo(startDate)<0){
                    //Chua den lich phat
                    return 3;
                }else if (today.compareTo(expireDate)> 0){
                    //Da hoan thanh
                    return 2;
                }else{
                    Calendar now = Calendar.getInstance();
                    Calendar midnight = Calendar.getInstance();
                    midnight.set(Calendar.HOUR_OF_DAY, 0);
                    midnight.set(Calendar.MINUTE, 0);
                    midnight.set(Calendar.SECOND, 0);
                    midnight.set(Calendar.MILLISECOND, 0);
                    long timeNow = (now.getTime().getTime() - midnight.getTime().getTime())/1000;
                    
                    
                    switch (rec_play_mode){
                        case 1:// phat theo lich
                            if (rec_type == 2){
                                if(rec_play_time.size()>1){
                                      Collections.sort(rec_play_time);
                                }
                                if(rec_end_time.size()>1){
                                      Collections.sort(rec_end_time);
                                }
                                if (timeNow < rec_play_time.get(0)){
                                    if (rec_play_time.get(0) - timeNow <=30*60){
                                        return 0;
                                    }else{
                                        //Chua den lich phat
                                        return 3;
                                    }
                                }else if (timeNow> rec_end_time.get(rec_end_time.size()-1)){
                                    //Da hoan thanh
                                    if (today.compareTo(expireDate)<0){
                                        return 4;
                                    }else{
                                        return 2;
                                    }
                                }else{
                                    boolean isPlaying = false;
                                    for (int i=0; i< rec_play_time.size(); i++) {
                                        if (timeNow >= rec_play_time.get(i) && timeNow <= rec_end_time.get(i)){
                                            isPlaying = true;
                                            break;
                                        }else if (i < rec_play_time.size() - 1 && timeNow > rec_end_time.get(i) && timeNow <rec_play_time.get(i+1)){
                                            if (rec_play_time.get(i+1) - timeNow <= 30*60){
                                                return 0;
                                            }else{
                                                return 3;
                                            }
                                        }
                                    }
                                    if (isPlaying){
                                        return 1;
                                    }else{
                                        return 0;
                                    }
                                }
                            }else{
                                if(rec_play_time.size()>1){
                                      Collections.sort(rec_play_time);
                                }
                                if (timeNow < rec_play_time.get(0)){
                                    if (rec_play_time.get(0) - timeNow <=30*60){
                                        return 0;
                                    }else{
                                        //Chua den lich phat
                                        return 3;
                                    }
                                }else if (timeNow> rec_play_time.get(rec_play_time.size()-1) + duration){
                                    //Da hoan thanh
                                    //return 4;
                                    if (today.compareTo(expireDate)<0){
                                        return 4;
                                    }else{
                                        return 2;
                                    }
                                }else{
                                    boolean isPlaying = false;
                                    for (int i=0; i< rec_play_time.size(); i++) {
                                        if (timeNow >= rec_play_time.get(i) && timeNow <= rec_play_time.get(i) + duration){
                                            isPlaying = true;
                                            break;
                                        }else if (i < rec_play_time.size() - 1 && timeNow > rec_play_time.get(i) + duration && timeNow <rec_play_time.get(i+1)){
                                            if (rec_play_time.get(i+1) - timeNow <= 30*60){
                                                return 0;
                                            }else{
                                                return 3;
                                            }
                                        }
                                    }
                                    if (isPlaying){
                                        return 1;
                                    }else{
                                        return 0;
                                    }
                                }
                            }
                        case 2: //phat ngay lap tuc
                            
                            if (rec_type == 0){// tin thuong
                                long playTime = (DateFormatter3.parse(approve_date).getTime() - midnight.getTime().getTime())/1000;
                                if (timeNow< playTime){
                                    if (playTime - timeNow <=30*60){
                                        return 0;
                                    }else{
                                        //Chua den lich phat
                                        return 3;
                                    }
                                }else if (timeNow> playTime+ duration){

                                    //Da hoan thanh
                                    return 2;
                                }
                                else{
                                    //Dang phat
                                    return 1;
                                }
                            }else if (rec_type==2){//tin tiep song
                                int playTime = rec_play_time.get(0);
                                int endTime = rec_end_time.get(0);
                                if (timeNow< playTime){
                                    if (playTime - timeNow <=30*60){
                                        return 0;
                                    }else{
                                        //Chua den lich phat
                                        return 3;
                                    }
                                }else if (timeNow> endTime){
                                    //Da hoan thanh
                                    return 2;
                                }
                                else{
                                    //Dang phat
                                    return 1;
                                }
                            }else{
                                return -2;
                            }
                    }
                }
            }
        }catch (ParseException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -2;
    } 

    public int getStatus() {
        return status;
    } 
 
    public void setStatus(int status) {
        this.status = status;
    }

    public List<Integer> getRec_play_time() {
        return rec_play_time;
    }

    public void setRec_play_time(List<Integer> rec_play_time) {
        this.rec_play_time = rec_play_time;
    }

    public List<Integer> getRec_end_time() {
        return rec_end_time;
    }

    public void setRec_end_time(List<Integer> rec_end_time) {
        this.rec_end_time = rec_end_time;
    }
    
    

    public List<Long> getRec_receipt() {
        return rec_receipt;
    }

    public void setRec_receipt(List<Long> rec_receipt) {
        this.rec_receipt = rec_receipt;
    }

    public Boolean getNotify_flagBoolean() {
        return notify_flag==1;
    }

    public void setNotify_flagBoolean(Boolean notify_flagBoolean) {
        this.notify_flagBoolean = notify_flagBoolean;
        this.notify_flag = notify_flagBoolean?1:0;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getRec_play_expire_date() {
        return rec_play_expire_date;
    }

    public void setRec_play_expire_date(String rec_play_expire_date) {
        this.rec_play_expire_date = rec_play_expire_date;
    }

    public int getRec_play_mode() {
        return rec_play_mode;
    }

    public void setRec_play_mode(int rec_play_mode) {
        this.rec_play_mode = rec_play_mode;
    }

    public int getNotify_status() {
        return notify_status;
    }

    public void setNotify_status(int notify_status) {
        this.notify_status = notify_status;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int getRec_type() {
        return rec_type;
    }

    public void setRec_type(int rec_type) {
        this.rec_type = rec_type;
    }

    public int getRec_audio_codec() {
        return rec_audio_codec;
    }

    public void setRec_audio_codec(int rec_audio_codec) {
        this.rec_audio_codec = rec_audio_codec;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getRec_summary() {
        return rec_summary;
    }

    public void setRec_summary(String rec_summary) {
        this.rec_summary = rec_summary;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public long getRec_play_repeat_days() {
        return rec_play_repeat_days;
    }

    public void setRec_play_repeat_days(long rec_play_repeat_days) {
        this.rec_play_repeat_days = rec_play_repeat_days;
    }

    public long getRec_id() {
        return rec_id;
    }

    public void setRec_id(long rec_id) {
        this.rec_id = rec_id;
    }

    public int getRec_priority() {
        
        return rec_priority;
    }

    public void setRec_priority(int rec_priority) {
        this.rec_priority = rec_priority;
    }

    public String getRec_url() {
        return rec_url;
    }

    public void setRec_url(String rec_url) {
        this.rec_url = rec_url;
    }

    public String getCategoryName() {
        
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getRec_repeatDayString() {
        return rec_repeatDayString;
    }

    public void setRec_repeatDayString(String rec_repeatDayString) {
        this.rec_repeatDayString = rec_repeatDayString;
    }

    public String getRecFileName() {
        if ((this.recFileName == null || this.recFileName.equals("")) && (rec_url != null)) {
            List<String> valueArr = Arrays.asList(rec_url.split("/"));
            return valueArr.get(valueArr.size()-1);
        }

        return recFileName;
    }

    public void setRecFileName(String recFileName) {
        this.recFileName = recFileName;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public String getListPlayName() {
        return listPlayName;
        
    }

    public void setListPlayName(String listPlayName) {
        this.listPlayName = listPlayName;
    }

    public String getRec_receipt_name() {
        return rec_receipt_name;
    }

    public void setRec_receipt_name(String rec_receipt_name) {
        this.rec_receipt_name = rec_receipt_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public long getArea_type_id() {
        return area_type_id;
    }

    public void setArea_type_id(long area_type_id) {
        this.area_type_id = area_type_id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getApprove_date() {
        return approve_date;
    }
    
    public String getDurationString(){
        if (duration == 0) {
            return "";
        }
        return (duration/60 < 10 ? "0" + duration/60 : duration/60) + ":" + (duration%60 < 10 ? "0" + duration%60 : duration%60);
    }

    public String getCreate_username() {
        return create_username;
    }

    public String getApprove_username() {
        return approve_username;
    }

//    public boolean isPlayNow() {
//        return rec_play_mode == 2 ? true:false;
//    }
//
//    public void setPlayNow(boolean playNow) {
//        this.playNow = playNow;
//        this.rec_play_mode = playNow ? 2 : 1;
//        
//    }

    public Date getCreateDate() {
        try {
            createDate = DateFormatter3.parse(create_date);
        } catch (ParseException ex) {
            Logger.getLogger(AreaNews.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createDate;
    }

    // HH:MM-----------------------------------------------------------------
    private static List<String> convertNumberToTime2(List<Integer> numList){
        List<String> timeList = new ArrayList<>();
        
        for (Integer num : numList) {
            String time = num/60 + ":" + (num%60 < 10 ? "0" + num%60 : num%60);
            timeList.add(time);
          
        }
        return timeList;
    }
    
    private static List<Integer> convertTimeToNumber2(String timeString){
        List<Integer> numList = new ArrayList<>();
        timeString = timeString.replaceAll("\\s","");
        List<String> timeList = Arrays.asList(timeString.split(","));
  
        for (String string : timeList) {
            int hh = Integer.parseInt(Arrays.asList(string.split(":")).get(0));
            int mm = Integer.parseInt(Arrays.asList(string.split(":")).get(1));
            
            if (hh > 23 || mm > 59) {
                return new ArrayList<>();
            }
            int num =  hh * 60 + mm ;
            numList.add(num);
        }
        return numList;
    }
    
    //HH:MM:SS-------------------------------------------------------------------------
    private static List<String> convertNumberToTime(List<Integer> numList){
        List<String> timeList = new ArrayList<>();
        
        for (Integer num : numList) {
            //String time = num/60 + ":" + (num%60 < 10 ? "0" + num%60 : num%60);
            
            String time = (num/3600 < 10 ? "0" + num/3600 : num/3600)
                    + ":" + ((num%3600)/60 < 10 ? "0" + (num%3600)/60 : (num%3600)/60) 
                    + ":" + (num%60 < 10 ? "0" + num%60 : num%60);
            timeList.add(time);
        }
        return timeList;
    }
    
    private static List<Integer> convertTimeToNumber(String timeString){
        List<Integer> numList = new ArrayList<>();
        timeString = timeString.replaceAll("\\s","");
        List<String> timeList = Arrays.asList(timeString.split(","));
  
        for (String string : timeList) {
            int hh = Integer.parseInt(Arrays.asList(string.split(":")).get(0));
            int mm = Integer.parseInt(Arrays.asList(string.split(":")).get(1));
            int ss = Integer.parseInt(Arrays.asList(string.split(":")).get(2));
            
            if (hh > 23 || mm > 59) {
                return new ArrayList<>();
            }
            int num =  hh * 3600 + mm * 60 + ss ;
            numList.add(num);
        }
        return numList;
    }
    
    
    private static List<String> convertNumberToTimeForRadio(List<Integer> startList, List<Integer> endList){
        List<String> timeList = new ArrayList<>();
        
        if (startList.size() != endList.size()) {
            return timeList;
        }
        
        for (int i = 0; i < startList.size(); i++) {
            int start = startList.get(i);
            int end = endList.get(i);
      
            String startTime = (start/3600 < 10 ? "0" + start/3600 : start/3600)
                    + ":" + ((start%3600)/60 < 10 ? "0" + (start%3600)/60 : (start%3600)/60) 
                    + ":" + (start%60 < 10 ? "0" + start%60 : start%60);
            
            String endTime = (end/3600 < 10 ? "0" + end/3600 : end/3600)
                    + ":" + ((end%3600)/60 < 10 ? "0" + (end%3600)/60 : (end%3600)/60) 
                    + ":" + (end%60 < 10 ? "0" + end%60 : end%60);
            
            timeList.add(startTime + "-" + endTime);
        }
        return timeList;
    }
    
    private void convertTimeToNumberForRadio(String timeString) throws Exception{
        List<Integer> startList = new ArrayList<>();
        List<Integer> endList = new ArrayList<>();
        timeString = timeString.replaceAll("\\s","");
        List<String> timeList = Arrays.asList(timeString.split(","));
  
        for (String time : timeList) {
            
            String start = Arrays.asList(time.split("-")).get(0);
            String end = Arrays.asList(time.split("-")).get(1);
            
           
            startList.add(convertStringToTime(start));
            endList.add(convertStringToTime(end));
        }
        
        rec_play_time = startList;
        rec_end_time = endList;
    }
    
    private static int convertStringToTime(String time) throws Exception {
        int hh = Integer.parseInt(Arrays.asList(time.split(":")).get(0));
        int mm = Integer.parseInt(Arrays.asList(time.split(":")).get(1));
        int ss = Integer.parseInt(Arrays.asList(time.split(":")).get(2));
            
        if (hh > 23 || mm > 59 || ss > 59) {
            throw new Exception();
        }
        return (hh * 3600 + mm * 60 + ss);
    }
//    
//    public static void main(String[] args) {
//        String  str = "12:20,14:50,17:30";
//        List<Integer> time = convertTimeToNumber(str);
//        
//        List<String> tr2 = convertNumberToTime(time);
//        
//        
//        
//    }
    public boolean getIsNext(){
        return isNext;
    }
    public void setIsNext(boolean isNext){
        this.isNext = isNext;
    }

    public boolean isIs_link() {
        return is_link;
    }

    public void setIs_link(boolean is_link) {
        this.is_link = is_link;
    }

    public long getRec_link() {
        return rec_link;
    }

    public void setRec_link(long rec_link) {
        this.rec_link = rec_link;
    }

    public String getPlayTimeRecLink() {
        return playTimeRecLink;
    }

    public void setPlayTimeRecLink(String playTimeRecLink) {
        this.playTimeRecLink = playTimeRecLink;
    }
    
}
