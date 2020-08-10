/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

import be.tarsos.transcoder.Attributes;
import be.tarsos.transcoder.Transcoder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.model.timeline.TimelineModel;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.Category;
import vn.mobifone.loaphuong.entity.DayObject;
import vn.mobifone.loaphuong.entity.EventRecord;
import vn.mobifone.loaphuong.entity.HistoryRecord;
import vn.mobifone.loaphuong.entity.HouseHold;
import vn.mobifone.loaphuong.entity.ListDataResponse;
import vn.mobifone.loaphuong.entity.MCUHistory;
import vn.mobifone.loaphuong.entity.MyUploadedFile;
import vn.mobifone.loaphuong.entity.RadioChannel;
import vn.mobifone.loaphuong.entity.Record;
import vn.mobifone.loaphuong.entity.Text2SpeechResponse;
import vn.mobifone.loaphuong.entity.TimeTable;
import vn.mobifone.loaphuong.entity.Voice;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.TSPermission;
import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
import vn.mobifone.loaphuong.model.AreaModel;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.util.Common;
import vn.mobifone.loaphuong.util.DateUtil;
import vn.mobifone.loaphuong.webservice.AreaWebservice;
import vn.mobifone.loaphuong.webservice.CallWebservice;



/**
 *
 * @author cuong.trinh
 */
@ManagedBean(name = "RecordController")
@ViewScoped
public class RecordController extends TSPermission implements Serializable  {
    private TreeNode selectedNode;
    private AreaTree selectedArea;
    private AreaTree selectedAreaForTimeline;
    private AreaTree areaRoot;
    private AreaTree addedArea;
    private CallWebservice coreWS;
    
    private AreaModel areaModel;

    private PersistAction recordActtionFlag;
    private boolean allowHouseManagement;
    private boolean isCityMode;

    
    //Record
    private UploadedFile audioFile;
    private List<Category> listRecordcategory;
    private List<Record> listRecord;
    private Record selectedRecord;
    
    private List<DayObject> listDaysWeek;
    private List<DayObject> listDaysMounth;
    private List<Integer> listDaysSelected;
    private int[] listDaysSelected2;
    
    private List<AreaTree> listSelectedArea;
    private TreeNode[] listSelectedArea2;
    private File tmpAudioFile;
    private final Date today = new Date();
    private Date dayNextYear;
    
    private String hourAdded;
    private Date hourAddeded2;
    private List<HouseHold> listMCUHouse;
    private HouseHold[] listSelectedMCUHouse;
    private String recPlayTimeString;
    private long[] listReceiptMCU;

    private String text;
    
    //time filter
    private Date fromDate;
    private Date toDate;
    

    
    // timeline
    private TimelineModel model;  
    private Date start;  
    private Date end; 
    private List<EventRecord> events;
    private EventRecord[] events2;
    private String eventsAsJson;
    private boolean calendarMode = true;
    
    private Date fromDateForTimeline;
    private Date toDateForTimeline;
    private boolean startDateEditable = true;
    
    
    // Radio
    private List<RadioChannel> listChannel;
    private String endTimeForRadio;
    private long recChannelId;
    
    //Overlap
    private int overlap = 0;
    private int notice = 0;
    private int noticeTS = 0;
    private List<Record> listOverlapRecord;
    private Record overlapRecord;
    private int[] listDaysSelectedOverlap;
    private String endTimeForRadioOverlap;
    
    //Mix multiple file
    private List<MyUploadedFile> listAudioFile;
    private List<MyUploadedFile> listSaveAudioFile;
    
    // Text to Speech
    private String textForConvert;
    private String savedtextForConvert;
    private Text2SpeechResponse t2tResponse;
    private int voice;
    private int speed;
    private File tmpT2TFile;
    
    
    // Record History
    private List<HistoryRecord> recordHistory;
    private List<MCUHistory> mcuHistory;
    
    SimpleDateFormat DateFormatter3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
    SimpleDateFormat DateFormatter = new SimpleDateFormat("dd/MM/yyyy"); 
    SimpleDateFormat DateFormatterForRadio = new SimpleDateFormat("HH:mm:ss"); 
    SimpleDateFormat DateFormatterForTime = new SimpleDateFormat("HH:mm:ss");
    //private final String APP_PATH = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
    
    // multi time for radio
    private String startTimeAdded;
    private String endTimeAdded;
    private String recPlayTimeStringForRadio;
    
    // cảnh báo phe duyêt khi đang tiếp sóng
    private List<Record> listTSPlaying;

        // Operation Flag -- Enum
    public static enum PersistAction {

        SELECT,
        CREATE,
        DELETE,
        VIEW,
        APPROVE,
        UPDATE,
        UPDATE_AFTER_APPROVE
    }
    
    //@ManagedProperty("#{documentService}")
    private DocumentService service;
     
    private Map<String,String> voices = null;
    private String[] voiceNames = null;

    public RecordController() throws ParseException {
        service = new DocumentService();
        areaRoot = service.createAreaTree();
        coreWS = new CallWebservice();
        
        
        //areaRoot.getChildren().get(0).setSelected(true);
        //areaRoot.getChildren().get(0).setExpanded(true);
        selectedArea = (AreaTree) areaRoot.getChildren().get(0);
        //allowHouseManagement = Integer.parseInt(SystemConfig.getConfig("viewHouseManagement"))==1?true:false;
        allowHouseManagement = (boolean)Session.getSessionValue(SessionKeyDefine.ALLOW_HOUSE_MANAGEMENT);
        isCityMode = Integer.parseInt(SystemConfig.getConfig("areaMode"))==2?true:false;
        
        toDate = today;
        Calendar cal = Calendar.getInstance();  
        cal.add(Calendar.DATE, -7);  
        fromDate = cal.getTime();
        
        // date for timeline
        fromDateForTimeline = DateFormatter.parse(DateFormatter.format(today));
        //fromDateForTimeline.setHours(0);
        //fromDateForTimeline.setMinutes(0);
        //fromDateForTimeline.setSeconds(0);
        cal.add(Calendar.DATE, 14);  
        toDateForTimeline = cal.getTime();
        
        Calendar cal2 = Calendar.getInstance();  
        cal2.add(Calendar.YEAR, 1);  
        dayNextYear = cal2.getTime();
        
       
        
        
        loadRecord();
        
        areaModel = new AreaModel();
        recordActtionFlag = PersistAction.SELECT;
        
        selectedRecord = new Record();
        
        listRecordcategory = coreWS.getListRecordCateogy(1);
        
        
        // for repeat day
        listDaysWeek = new ArrayList<>();
        listDaysMounth = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            listDaysMounth.add(new DayObject(""+i, i));
        }
        for (int i = 2; i <= 7; i++) {
            listDaysWeek.add(new DayObject("Thứ " + i , i));
        }
        listDaysWeek.add(new DayObject("Chủ nhật", 8));
        
        
        
        listDaysSelected = new ArrayList<Integer>();
        listSelectedArea = new ArrayList<>();

        listMCUHouse = coreWS.getListHouseHasMCU(selectedArea.getAreaId());
        selectedAreaForTimeline = selectedArea;
        List<Voice> voiceLst = coreWS.getListVoices();
        voices = new LinkedHashMap<String, String>();
        voiceNames = new String[voiceLst.size()];
        int i =0;
        for (Voice v : voiceLst) {
            voices.put(v.getName(),v.getCode());
            voiceNames[i] = v.getSys_name();
            i++;
        }
        /*voices.put("Mạnh Dũng (nam miền Bắc)","0");
        voices.put("Thùy Linh (nữ miền Bắc)","1");
        voices.put("Minh Hoàng (nam miền Nam)","2");
        voices.put("Thảo Trinh (nữ miền Nam)","3");
        
        voiceNames[0] = "hn_male_xuantin_vdts_48k-hsmm";
        voiceNames[1] = "hn_female_thutrang_phrase_48k-hsmm";
        voiceNames[2] = "sg_male_minhhoang_dial_48k-hsmm";
        voiceNames[3] = "sg_female_thaotrinh_dialog_48k-hsmm";*/
    }
    public Map<String, String> getVoices() {
        return voices;
    }
    
    public TreeNode getSelectedNode() {
        return selectedNode;
    }
 
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
        
    
        System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()" + "select note " + selectedArea.getAreaName());
        
 
    }                
 
    public void setService(DocumentService service) {
        this.service = service;
    }
     
    public void displaySelectedSingle() {
        //if(selectedNode != null) {
            System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()"    +  " Action work");
            //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", selectedNode.getData().toString());
            //FacesContext.getCurrentInstance().addMessage(null, message);
        //}
    }
    
    public void resetTree(TreeNode node, TreeNode[] listSelected) {
        
        for (TreeNode treeNode : node.getChildren()) {
            for (int i = 0; i < listSelected.length; i++) {
                if (listSelected[i] != null) {
                    
                    if (((AreaTree)treeNode).getAreaId() == ((AreaTree)listSelected[i]).getAreaId()) {
                        //treeNode.setSelected(true);
                        setAllChildSelected(treeNode);
                        setAllParentExpanded(treeNode);
//                        if (treeNode.getParent() != null) {
//                            treeNode.getParent().setExpanded(true);
//                            if (treeNode.getParent().getParent() != null) {
//                                treeNode.getParent().getParent().setExpanded(true);
//
//                            }
//                        }

                    } 
                }   
            }
            
            resetTree(treeNode, listSelected);
        }
        
    }
    
    public void resetTreeState(TreeNode node) {
        node.setSelected(false);
        node.setExpanded(false);
        if (recordActtionFlag == PersistAction.APPROVE || recordActtionFlag == PersistAction.VIEW) {
                node.setSelectable(false);
            } else {
                node.setSelectable(true);
            }
        for (TreeNode child : node.getChildren()) {
            resetTreeState(child);
        }
    }
    
    public void setAllChildSelected(TreeNode node) {
        node.setSelected(true);
        for (TreeNode treeNode : node.getChildren()) {
            setAllChildSelected(treeNode);
        }
    }
    public void setAllParentExpanded(TreeNode node) {
        node.setExpanded(true);
        if (node.getParent() != null) {
            setAllParentExpanded(node.getParent());
        }
        
        
    }
       
    public void viewMCUHistoryEvent(HistoryRecord historyRec) { 
        mcuHistory = coreWS.getListMCUByListArea(selectedRecord.getRec_receipt());
        
        List<Long> listMCUPlay = coreWS.getListMCUPlay(selectedRecord.getRec_id(), historyRec.getPlayTime(), historyRec.getEndTime() );
        for (Long mcuId : listMCUPlay) {
            for (int i = 0; i < mcuHistory.size(); i++) {
                if (mcuHistory.get(i).getMcu_id() == mcuId) {
                    mcuHistory.get(i).setPlayStatus(1);
                }
            }
            
         
        }
    }
    
    public void viewHistoryEvent(ActionEvent evt) { 
        recordHistory = new ArrayList<>();
        mcuHistory = coreWS.getListMCUByListArea(selectedRecord.getRec_receipt());
                
        if (selectedRecord.getRec_play_mode() == 1) {   // phát theo lịch
                        if (selectedRecord.getRec_play_repeat_type() == 0) {    // lap lai theo tuan
                           
                            
                            Calendar gcal = Calendar.getInstance();
                            gcal.setTime(selectedRecord.getRec_playStartDate());

                            while (!gcal.getTime().after(selectedRecord.getRec_playExpireDate())) {
//                                if (gcal.getTime().before(fromDateForTimeline)) {
//                                    gcal.a dd(Calendar.DATE, 1);
//                                    continue;
//                                }
                                if (gcal.getTime().after(today)) {
                                    break;
                                    
                                }

                                Date date = gcal.getTime();
                                Calendar c = Calendar.getInstance();
                                c.setTime(date);
                                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                                
                                String num = "1";
                                for (int j = 2; j < dayOfWeek; j++) {
                                    num += "0";
                                }
                                if ((Integer.valueOf(num, 2) & selectedRecord.getRec_play_repeat_days()) > 0) {
                                     
                                    for (int i = 0; i < selectedRecord.getRec_play_time().size(); i++){
                                    //for (Integer time : selectedRecord.getRec_play_time()) {
                                        Integer time = selectedRecord.getRec_play_time().get(i);
                                        Date startTime = new Date(date.getTime() + time  * 1000 );
                                        Date endTime = null;
                                        if (selectedRecord.getRec_type()==0){
                                            endTime = new Date((date.getTime() + time  * 1000 + selectedRecord.getDuration() * 1000)); 
                                        }else if (selectedRecord.getRec_type()==2){
                                            time = selectedRecord.getRec_end_time().get(i);
                                            endTime = new Date(date.getTime() + time  * 1000 );
                                        }else{
                                            endTime = new Date((date.getTime() + time  * 1000 + selectedRecord.getDuration() * 1000)); 
                                        }
                                        //int numPlay = coreWS.getNumberMCUPlay(selectedRecord.getRec_id(), startTime, new Date(startTime.getTime() + 1 * 60 * 1000 ));
                                        
                                        int numPlay = coreWS.getNumberMCUPlay(selectedRecord.getRec_id(), startTime, endTime);
                                        
                                        recordHistory.add(new HistoryRecord(startTime,endTime, numPlay));
                                        
//                                        TimelineEvent event = new TimelineEvent(selectedRecord, startTime, endTime, false, ""+ (5 - selectedRecord.getRec_priority()));  
//                                        event.setStyleClass("style" + selectedRecord.getRec_type() + selectedRecord.getRec_play_mode());
//                                        
//                                        model.add(event);
//
//                                        EventRecord eventRecord = new EventRecord(DateFormatter.format(date), selectedRecord.getRec_summary(), 
//                                                startTime.getHours(), startTime.getMinutes(), startTime.getSeconds(),
//                                                endTime.getHours(), endTime.getMinutes(), endTime.getSeconds(),
//                                                selectedRecord.getRec_play_mode(), selectedRecord.getRec_play_repeat_type(), repeatDayString, selectedRecord.getArea_name(), selectedRecord.getRec_play_timeString(), selectedRecord.getRec_type());
//                                       
//                                        
//                                        events.add(eventRecord);
                                    }
                                    
                                     
                                }

                                gcal.add(Calendar.DATE, 1);
                            }
                            
                            
                        } else { // lap lai theo thang

                        }

                        
                    } else if (selectedRecord.getRec_play_mode() == 2) {   // Phát ngay lập tức
                        try {
                                Date startTime =null;
                                Date endTime = null;
                                if (selectedRecord.getRec_type()==0){
                                     startTime = DateFormatter3.parse(selectedRecord.getApprove_date());
                                     endTime = new Date((startTime.getTime() + selectedRecord.getDuration() * 1000)); 
                                }else if (selectedRecord.getRec_type()==2){
                                     //startTime = DateFormatter3.parse(selectedRecord.getApprove_date());
                                     startTime = new Date(selectedRecord.getRec_playStartDate().getTime() + selectedRecord.getRec_play_time().get(0) * 1000); 
                                     endTime = new Date(selectedRecord.getRec_playStartDate().getTime() + selectedRecord.getRec_end_time().get(0) * 1000); 
                                }else{
                                     startTime = DateFormatter3.parse(selectedRecord.getApprove_date());
                                     endTime = new Date((startTime.getTime() + selectedRecord.getDuration() * 1000)); 
                                }
                                int numPlay = coreWS.getNumberMCUPlay(selectedRecord.getRec_id(), startTime, endTime);
                                recordHistory.add(new HistoryRecord(startTime,endTime, numPlay));

                        } catch (ParseException ex) {
                            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                            
                    }
    }
    
    public void viewTimelineEvent() { 
        
       //timeline
        // set initial start / end dates for the axis of the timeline  
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
       Calendar calendar = Calendar.getInstance(timeZone);
        Date now = new Date();  
   
        calendar.setTimeInMillis(now.getTime() - 4 * 60 * 60 * 1000);  
        start = calendar.getTime();  
   
        calendar.setTimeInMillis(now.getTime() + 8 * 60 * 60 * 1000);  
        end = calendar.getTime();  
   
   
        // create timeline model  
        model = new TimelineModel();  
        Calendar cal3 = Calendar.getInstance();  
        cal3.add(Calendar.YEAR, -2);  
   
        events = new ArrayList<>();
        List<Record> listDestRec = coreWS.getRecordByDestArea(selectedAreaForTimeline.getAreaId(), cal3.getTime(), toDate);


        Calendar gcal2 = Calendar.getInstance();
        gcal2.setTime(fromDateForTimeline);

        while (!gcal2.getTime().after(toDateForTimeline)) {
            EventRecord eventRecord = new EventRecord(DateFormatter.format(gcal2.getTime()), "", 
                                            0, 0, 0,
                                            0, 0, 0,
                                            0, 0,"", "", "", -1, 0);


            events.add(eventRecord);
            gcal2.add(Calendar.DATE, 1);
        }

            
            for (Record record : listDestRec) {
                long r = Math.round(Math.random() * 2);  
                //String availability = (r == 0 ? "Unavailable" : (r == 1 ? "Available" : "Maybe")); 
                if (record.getStatus() == 1) {  // đã duyệt
                    if (record.getRec_play_mode() == 1) {   // phát theo lịch
                        if (record.getRec_play_repeat_type() == 0) {    // lap lai theo tuan
                            int[] listRepeatDay;
                                        listRepeatDay = getRepeatDaysFromBit(record.getRec_play_repeat_days(), 0);
                                        String repeatDayString = "";
                                        for (int i = 0; i < listRepeatDay.length; i++) {
                                            if (listRepeatDay[i] == 8) {
                                                repeatDayString += "Chủ nhật";
                                            } else {
                                                repeatDayString += "Thứ " + listRepeatDay[i] + " , ";
                                            }
                                        }
                            
                            Calendar gcal = Calendar.getInstance();
                            gcal.setTime(record.getRec_playStartDate());

                            while (!gcal.getTime().after(record.getRec_playExpireDate())) {
                                if (gcal.getTime().before(fromDateForTimeline)) {
                                    gcal.add(Calendar.DATE, 1);
                                    continue;
                                }
                                if (gcal.getTime().after(toDateForTimeline)) {
                                    break;
                                    
                                }

                                Date date = gcal.getTime();
                                Calendar c = Calendar.getInstance();
                                c.setTime(date);
                                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                                
                                String num = "1";
                                for (int j = 2; j < dayOfWeek; j++) {
                                    num += "0";
                                }
                                if ((Integer.valueOf(num, 2) & record.getRec_play_repeat_days()) > 0) {
                                     
                                    
                                    for (Integer time : record.getRec_play_time()) {
                                        Date startTime = new Date(date.getTime() + time  * 1000 );  
                                        Date endTime = null;
                                        
                                        if (record.getRec_type() == 2 && record.getRec_play_mode() == 1) {
                                            endTime = new Date((date.getTime() +   record.getRec_end_time().get(record.getRec_play_time().indexOf(time))  * 1000 )); 
                                        } else {
                                            endTime = new Date((date.getTime() + time  * 1000 + record.getDuration() * 1000)); 
                                        }

                                        
                                        TimelineEvent event = new TimelineEvent(record, startTime, endTime, false, ""+ (5 - record.getRec_priority()));  
                                        event.setStyleClass("style" + record.getRec_type() + record.getRec_play_mode());
                                        
                                        model.add(event);

                                        EventRecord eventRecord = new EventRecord(DateFormatter.format(date), record.getRec_summary()==null?"":record.getRec_summary(), 
                                                startTime.getHours(), startTime.getMinutes(), startTime.getSeconds(),
                                                endTime.getHours(), endTime.getMinutes(), endTime.getSeconds(),
                                                record.getRec_play_mode(), record.getRec_play_repeat_type(), repeatDayString, record.getArea_name(), record.getRec_play_timeString(), record.getRec_type(), record.getRec_id());
                                       
                                        
                                        events.add(eventRecord);
                                    }
                                    
                                     
                                }

                                gcal.add(Calendar.DATE, 1);
                            }
                            
                            
                        } else { // lap lai theo thang

                        }

                        
                    } else if (record.getRec_play_mode() == 2) {   // Phát ngay lập tức
                        if(record.getRec_receipt_type()==2){
                            try {
                                    Date startTime = DateFormatter3.parse(record.getApprove_date());

                                    if (!startTime.before(fromDateForTimeline) && !startTime.after(toDateForTimeline)) {
                                        Date endTime = new Date((startTime.getTime() + record.getDuration() * 1000));
                                        TimelineEvent event = new TimelineEvent(record, startTime, endTime, false, ""+(5-record.getRec_priority()));  
                                        event.setStyleClass("style" + record.getRec_type() + record.getRec_play_mode());  

                                        model.add(event);

                                        EventRecord eventRecord = new EventRecord(DateFormatter.format(startTime), record.getRec_summary(), 
                                                        startTime.getHours(), startTime.getMinutes(), startTime.getSeconds(),
                                                        endTime.getHours(), endTime.getMinutes(), endTime.getSeconds(),
                                                        record.getRec_play_mode(), record.getRec_play_repeat_type(),"", record.getArea_name(), record.getRec_play_timeString(), record.getRec_type(), record.getRec_id());


                                        events.add(eventRecord);
                                    }
                            } catch (ParseException ex) {
                                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                             try {
                                    Date startTime = DateFormatter3.parse(record.getApprove_date());

                                    if (!startTime.before(fromDateForTimeline) && !startTime.after(toDateForTimeline)) {
                                        Date endTime =  null;
                                        if (record.getDuration()>0){
                                            endTime = new Date((startTime.getTime() + record.getDuration() * 1000));
                                        }else{
                                            endTime = new Date(startTime.getTime() + (record.getRec_end_time().get(0)- record.getRec_play_time().get(0))*1000);
                                        }
                                        TimelineEvent event = new TimelineEvent(record, startTime, endTime, false, ""+(5-record.getRec_priority()));  
                                        event.setStyleClass("style" + record.getRec_type() + record.getRec_play_mode());  

                                        model.add(event);

                                        EventRecord eventRecord = new EventRecord(DateFormatter.format(startTime), record.getRec_summary(), 
                                                        startTime.getHours(), startTime.getMinutes(), startTime.getSeconds(),
                                                        endTime.getHours(), endTime.getMinutes(), endTime.getSeconds(),
                                                        record.getRec_play_mode(), record.getRec_play_repeat_type(),"", record.getArea_name(), record.getRec_play_timeString(), record.getRec_type(), record.getRec_id());


                                        events.add(eventRecord);
                                    }
                            } catch (ParseException ex) {
                                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    
                }
            }

 
    } 

    public void addRecordEvent(ActionEvent evt) { 
        try {
            RequestContext requestContext = RequestContext.getCurrentInstance();  
            requestContext.execute("showDownloadAudioButton(false);");   
            recordActtionFlag = PersistAction.CREATE;
            selectedRecord = new Record();
            selectedRecord.setAreaId(selectedArea.getAreaId());
            selectedRecord.setRec_receipt_type(1);
            selectedRecord.setRec_receipt_name("");
            selectedRecord.setRec_play_mode(1);
            listDaysSelected = new ArrayList<>();
            //listDaysSelected2 = getRepeatDaysFromBit(selectedRecord.getRec_play_repeat_days(), selectedRecord.getRec_play_repeat_type());
            //listSelectedArea2 = getListAreaSelectedFromID(selectedRecord.getRec_receipt());
            
            listDaysSelected2 = new int[0];
            listSelectedArea2 = new TreeNode[0];
            listReceiptMCU = null;
            audioFile = null;
            tmpAudioFile = null;
            
            //resetTree(areaRoot, listSelectedArea2);
            resetTreeState(areaRoot);
            if (selectedRecord.getRec_receipt() != null) {
                resetTree(areaRoot, getListAreaSelectedFromID(selectedRecord.getRec_receipt()));
            }
            areaRoot.getChildren().get(0).setExpanded(true);
            listSelectedAreaName = null;
            
            hourAdded = null;
            startTimeAdded = null;
            endTimeAdded = null;
            //hourAddeded2 = new Date();
            recPlayTimeString = "";
            recPlayTimeStringForRadio = "";
            endTimeForRadio = "";
            overlap = 0;
            notice = 0;
            noticeTS = 0;
            listAudioFile = new ArrayList<>();
            listSaveAudioFile = new ArrayList<>();
            savedtextForConvert = "";
            t2tResponse = null;
            speed = 0;
            voice = 0;
            startDateEditable = true;
            
        } catch (Exception e) {
            
        }
    }  
    
    public void editRecordFromTimeline() { 
        savedtextForConvert = "";
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        long recId =  Long.parseLong(params.get("rec_id"));
        
        selectedRecord = coreWS.getRecordById(recId);
        recordActtionFlag = PersistAction.UPDATE_AFTER_APPROVE;
        loadRecordForEdit();
        
        if (selectedRecord.getRec_playStartDate().before(today)) {
            startDateEditable = false;
        }
    }
    
    public void addContinueRecordFromTimeline() { 
        RequestContext requestContext = RequestContext.getCurrentInstance();  
        requestContext.execute("showDownloadAudioButton(false);");   
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        long recId =  Long.parseLong(params.get("rec_id"));
        
        
        addContinueRecordEvent(coreWS.getRecordById(recId));
    }
    
    public void editRecordAfterApproval(Record record) { 
        savedtextForConvert = "";
        selectedRecord = new Record(record);
        recordActtionFlag = PersistAction.UPDATE_AFTER_APPROVE;
        loadRecordForEdit();
        if (selectedRecord.getRec_playStartDate().before(today)) {
            startDateEditable = false;
        }
    }
       
    public void editRecordEvent(Record record) { 
        savedtextForConvert = "";
        recordActtionFlag = PersistAction.UPDATE;
        selectedRecord = new Record(record);
        loadRecordForEdit();
        startDateEditable = true;
        
    } 
    
    public void addContinueRecordEvent(Record record) { 
        savedtextForConvert = "";
        recordActtionFlag = PersistAction.CREATE;
        selectedRecord = new Record(record);
        selectedRecord.setIsNext(true);
        //08_Apr_2020
        selectedRecord.setIs_link(true);
        if (selectedRecord.getRec_type()==0){
            RequestContext requestContext = RequestContext.getCurrentInstance();  
            requestContext.execute("showDownloadAudioButton(false);"); 
            List<Integer> playTime = new ArrayList<>();
            List<Integer> endTime = new ArrayList<>();
            for (Integer time : selectedRecord.getRec_play_time()) {
                int newPlayTime = time + Integer.parseInt(""+selectedRecord.getDuration()) + 1;
                playTime.add(newPlayTime);
                endTime.add(newPlayTime + Integer.parseInt(""+selectedRecord.getDuration()));
            }
            selectedRecord.setRec_play_time(playTime);
            selectedRecord.setRec_end_time(endTime);
        }else{
            List<Integer> playTime = new ArrayList<>();
            List<Integer> endTime = new ArrayList<>();
            if (selectedRecord.getRec_play_mode() == 1) { // theo lịch
                for (int i=0; i< selectedRecord.getRec_end_time().size(); i++){
                    endTime.add(selectedRecord.getRec_end_time().get(i) + (selectedRecord.getRec_end_time().get(i) - selectedRecord.getRec_play_time().get(i))+1);
                    playTime.add(selectedRecord.getRec_end_time().get(i) + 1);
                }
            } else {
                endTime.add(selectedRecord.getRec_end_time().get(0) + (selectedRecord.getRec_end_time().get(0) - selectedRecord.getRec_play_time().get(0))+1);
                playTime.add(selectedRecord.getRec_end_time().get(0) + 1);
            }
            selectedRecord.setRec_play_time(playTime);
            selectedRecord.setRec_end_time(endTime);
        }
        
        //selectedRecord.setRecFileName("");
        //selectedRecord.setRec_url("");
        selectedRecord.setRec_id(0);
        selectedRecord.setRec_summary("");
        selectedRecord.setAreaId(selectedArea.getAreaId());
       
        if (selectedRecord.getRec_playStartDate().before(today)) {
            selectedRecord.setRec_playStartDate(today);
        }
    
        //08_Apr_2020
        selectedRecord.setPlayTimeRecLink(recPlayTimeString);
        
        loadRecordForEdit();
        startDateEditable = true;
        
    } 
    
    public void cloneRecordEvent(Record record) { 
        savedtextForConvert = "";
        recordActtionFlag = PersistAction.CREATE;
        selectedRecord = new Record(record);
        
       
        
  
        selectedRecord.setRec_play_time(null);
       
        selectedRecord.setRecFileName("");
        selectedRecord.setRec_id(0);
        selectedRecord.setRec_summary("");
        selectedRecord.setAreaId(selectedArea.getAreaId());
        selectedRecord.setRec_playStartDate(null);
        selectedRecord.setRec_playExpireDate(null);
        selectedRecord.setRec_play_expire_date("");
        selectedRecord.setRec_play_start("");
        selectedRecord.setRec_play_repeat_days(0);
        
    
        
        loadRecordForEdit();
        startDateEditable = true;
        
    } 
    
    public void loadRecordForEdit(){
        overlap = 0;
        notice = 0;
        noticeTS = 0;
        if (selectedRecord.getRec_type()!=0) {  // tin tiep song
            listChannel = (new AreaWebservice()).getListChannelByArea(selectedArea.getAreaId(), selectedRecord.getRec_type());
            if (selectedRecord.getRec_play_mode() == 1) { // theo lịch
                //endTimeForRadio = convertNumberToTime(selectedRecord.getRec_play_time().get(0) + selectedRecord.getDuration());
                recPlayTimeStringForRadio = selectedRecord.getRec_play_timeStringRadio();
            } else {
                //endTimeForRadio = convertNumberToTime(convertTimeToNumber(DateFormatterForRadio.format(selectedRecord.getCreateDate())) + selectedRecord.getDuration());
                //endTimeForRadio = convertNumberToTime(selectedRecord.getRec_play_time().get(0) + selectedRecord.getDuration());
                endTimeForRadio = convertNumberToTime(selectedRecord.getRec_end_time().get(0));
            }
        } else {
            recPlayTimeStringForRadio = "";
            RequestContext requestContext = RequestContext.getCurrentInstance();  
            requestContext.execute("showDownloadAudioButton(true);"); 
        }
          
        
        listDaysSelected2 = getRepeatDaysFromBit(selectedRecord.getRec_play_repeat_days(), selectedRecord.getRec_play_repeat_type());
        listSelectedArea2 = getListAreaSelectedFromID(selectedRecord.getRec_receipt());
        
        if (selectedRecord.getRec_receipt_type() == 2) {
            getListMCUSelectedForView();
        } else {
            //resetTree(areaRoot, listSelectedArea2);
            //areaRoot.getChildren().get(0).setExpanded(true);
        }
        
        hourAdded = null;
        startTimeAdded = null;
        endTimeAdded = null;
        hourAddeded2 = new Date();
        recPlayTimeString = selectedRecord.getRec_play_timeString();
        listAudioFile = new ArrayList<>();
        listSaveAudioFile = new ArrayList<>();
        audioFile = null;
        tmpAudioFile = null;
        tmpT2TFile = null;
        speed = 4;
        voice = 0;
    }

    public void viewRecordEvent(Record record) { 
        recordActtionFlag = PersistAction.VIEW;
        selectedRecord = record;
        overlap = 0;
        
        if (selectedRecord.getRec_type()!=0) {  // tin tiep song
            listChannel = (new AreaWebservice()).getListChannelByArea(selectedArea.getAreaId(), selectedRecord.getRec_type());
            if (selectedRecord.getRec_play_mode() == 1) { // theo lịch
                //endTimeForRadio = convertNumberToTime(selectedRecord.getRec_play_time().get(0) + selectedRecord.getDuration());
                recPlayTimeStringForRadio = selectedRecord.getRec_play_timeStringRadio();
            } else {
                //endTimeForRadio = convertNumberToTime(selectedRecord.getRec_play_time().get(0) + selectedRecord.getDuration());
                endTimeForRadio = convertNumberToTime(selectedRecord.getRec_end_time().get(0));
            }
        } else {
            RequestContext requestContext = RequestContext.getCurrentInstance();  
            requestContext.execute("showDownloadAudioButton(true);"); 
        }
        hourAdded = null;
        hourAddeded2 = new Date();
        recPlayTimeString = selectedRecord.getRec_play_timeString();
        listDaysSelected2 = getRepeatDaysFromBit(selectedRecord.getRec_play_repeat_days(), selectedRecord.getRec_play_repeat_type());
        listSelectedArea2 = getListAreaSelectedFromID(selectedRecord.getRec_receipt());
        if (selectedRecord.getRec_receipt_type() == 2) {
            getListMCUSelectedForView();
        } else {
            //resetTree(areaRoot, listSelectedArea2);
            //areaRoot.getChildren().get(0).setExpanded(true);
        }
    }  
    
    public void viewRecordEventForOverlap(Record record) { 
        overlapRecord = record;
        
        if (overlapRecord.getRec_type() == 3) {  // tin tiep song
            listChannel = (new AreaWebservice()).getListChannelByArea(selectedArea.getAreaId(), selectedRecord.getRec_type());
            if (overlapRecord.getRec_play_mode() == 1) { // theo lịch
                endTimeForRadioOverlap = convertNumberToTime(overlapRecord.getRec_play_time().get(0) + overlapRecord.getDuration());
            } else {
                endTimeForRadioOverlap = convertNumberToTime(convertTimeToNumber(DateFormatterForRadio.format(overlapRecord.getCreateDate())) + overlapRecord.getDuration());
                
            }
        } 
        
        if (overlapRecord.getRec_type() == 2) {  // tin tiep song
            listChannel = (new AreaWebservice()).getListChannelByArea(selectedArea.getAreaId(), selectedRecord.getRec_type());
            if (overlapRecord.getRec_play_mode() == 1) { // theo lịch
                endTimeForRadioOverlap = convertNumberToTime(overlapRecord.getRec_play_time().get(0) + overlapRecord.getDuration());
                //recPlayTimeStringForRadio = selectedRecord.getRec_play_timeStringRadio();
            } else {
                //endTimeForRadioOverlap = convertNumberToTime(convertTimeToNumber(DateFormatterForRadio.format(overlapRecord.getCreateDate())) + overlapRecord.getDuration());
                //endTimeForRadio = convertNumberToTime(selectedRecord.getRec_play_time().get(0) + selectedRecord.getDuration());
                endTimeForRadio = convertNumberToTime(selectedRecord.getRec_end_time().get(0));
            }
        } 
        //recPlayTimeString = overlapRecord.getRec_play_timeString();
        listDaysSelectedOverlap = getRepeatDaysFromBit(overlapRecord.getRec_play_repeat_days(), overlapRecord.getRec_play_repeat_type());
        //listSelectedArea2 = getListAreaSelectedFromID(overlapRecord.getRec_receipt());
        if (overlapRecord.getRec_receipt_type() == 2) {
         //   getListMCUSelectedForView();
        } else {
            //resetTree(areaRoot, listSelectedArea2);
            //areaRoot.getChildren().get(0).setExpanded(true);
        }
    }  

    
    public void approveRecordEvent(Record record) { 
        recordActtionFlag = PersistAction.APPROVE;
        selectedRecord = record;
        overlap = 0;
        notice = 0;
        noticeTS = 0;
        if (selectedRecord.getRec_type()!= 0) {  // tin tiep song
            listChannel = (new AreaWebservice()).getListChannelByArea(selectedArea.getAreaId(), selectedRecord.getRec_type());
            if (selectedRecord.getRec_play_mode() == 1) { // theo lịch
                //endTimeForRadio = convertNumberToTime(selectedRecord.getRec_play_time().get(0) + selectedRecord.getDuration());
                recPlayTimeStringForRadio = selectedRecord.getRec_play_timeStringRadio();
            } else {
                //endTimeForRadio = convertNumberToTime(convertTimeToNumber(DateFormatterForRadio.format(selectedRecord.getCreateDate())) + selectedRecord.getDuration());
                //endTimeForRadio = convertNumberToTime(selectedRecord.getRec_play_time().get(0) + selectedRecord.getDuration());
                endTimeForRadio = convertNumberToTime(selectedRecord.getRec_end_time().get(0));
            }
        } else {
            RequestContext requestContext = RequestContext.getCurrentInstance();  
            requestContext.execute("showDownloadAudioButton(true);"); 
        }
        hourAdded = null;
        recPlayTimeString = selectedRecord.getRec_play_timeString();
        listDaysSelected2 = getRepeatDaysFromBit(selectedRecord.getRec_play_repeat_days(), selectedRecord.getRec_play_repeat_type());
        listSelectedArea2 = getListAreaSelectedFromID(selectedRecord.getRec_receipt());
        if (selectedRecord.getRec_receipt_type() == 2) {
            getListMCUSelectedForView();
        } else {
            //resetTree(areaRoot, listSelectedArea2);
            //areaRoot.getChildren().get(0).setExpanded(true);
        }
    } 
    
    public void deleteRecordEvent(Record record) { 
        recordActtionFlag = PersistAction.DELETE;
        selectedRecord = record;
    }  
    
    
    public void onShowAddRecordPopup(){
        System.out.println("onShowAddRecordPopup");

        if(this.selectedRecord != null && this.selectedRecord.getRec_url()!=null){
            RequestContext requestContext = RequestContext.getCurrentInstance();  
            requestContext.execute("reloadPlayer()");            
        }

    }
    
    public void onHideAddRecordPopup(){
        System.out.println("onHideAddRecordPopup");
        this.selectedRecord = null;
        if(tmpAudioFile!=null && tmpAudioFile.exists()){
            tmpAudioFile.delete();
            tmpAudioFile = null;
        }
        if (this.audioFile!=null){
            try{
                this.audioFile.getInputstream().close();
            }catch (Exception ex){
                //ex.printStackTrace();
            }
            this.audioFile = null;
        }
        if (listAudioFile!=null && listAudioFile.size()>0){
            for (int i = 0; i < listAudioFile.size(); i++) {
                MyUploadedFile myUploadedFile = listAudioFile.get(i);
                try{
                    myUploadedFile.getUploadedFile().getInputstream().close();
                }catch (Exception ex1){
                }
            }
            listAudioFile.clear();
        }
        if (listSaveAudioFile!=null && listSaveAudioFile.size()>0){
            for (int i = 0; i < listSaveAudioFile.size(); i++) {
                MyUploadedFile myUploadedFile = listSaveAudioFile.get(i);
                try{
                    myUploadedFile.getUploadedFile().getInputstream().close();
                }catch (Exception ex1){
                }
            }
            listSaveAudioFile.clear();
        }
        
        RequestContext requestContext = RequestContext.getCurrentInstance();  
        requestContext.execute("stopPlayer();");  
    }
    
    public void saveRecord(ActionEvent evt){
         //System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()"    +  " save record");
         int dayInMs = 1000 * 60 * 60 * 24;
         Date previousDay = new Date(today.getTime() - dayInMs);
         if (overlap != 3) {
            overlap = 0;
         }
         if (recPlayTimeStringForRadio!=null && recPlayTimeStringForRadio.length()>0){
             List<Integer> startList = new ArrayList<>();
                List<Integer> endList = new ArrayList<>();
                String timeString = recPlayTimeStringForRadio.replaceAll("\\s","");
                List<String> timeList = Arrays.asList(timeString.split(","));
                Collections.sort(timeList);
                for (String time : timeList) {
                    String start = Arrays.asList(time.split("-")).get(0);
                    String end = Arrays.asList(time.split("-")).get(1);
                    startList.add(convertStringToTime(start));
                    endList.add(convertStringToTime(end));
                }
                for (int i = 0; i < startList.size(); i++) {
                    if (startList.get(i)>=endList.get(i)){
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian nhập vào không hợp lệ, Thời gian kết thúc phải sau thời gian bắt đầu! Vui lòng kiểm tra lại");
                        return;   
                    }
                }
                if (startList.size()>1){
                    for (int i = 0; i < startList.size()-1; i++) {

                        if (!((startList.get(i+1) <= startList.get(i) && endList.get(i+1) <= startList.get(i)) || (startList.get(i+1) >= endList.get(i) && endList.get(i+1) >= endList.get(i)))) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Các khoảng thời gian đã chọn bị trùng chờm! Vui lòng kiểm tra lại");
                            return;
                        }
                    }
                }
         }
        
         if (selectedRecord.getDuration() > 0 && recordActtionFlag != recordActtionFlag.DELETE) {
             if (recPlayTimeString!=null && !recPlayTimeString.isEmpty()){ 
                recPlayTimeString = recPlayTimeString.replaceAll("\\s","");
                List<Integer> timeLst = DateUtil.convertTimeToNumber(recPlayTimeString);
                if(timeLst.size()>1){
                    Collections.sort(timeLst);
                    for (int i = 0; i < timeLst.size()-1; i++) {
                        if (timeLst.get(i+1) - timeLst.get(i) < selectedRecord.getDuration()){
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Các giờ phát trùng, chờm với thời gian đã chọn! Vui lòng chọn lại");
                            return;
                        }
                    }
                }
             }
        }
        
        try {
            
            //List<TimeTable> listTimeTable = coreWS.getTimeTableByArea(Long.parseLong(SystemConfig.getConfig("domainArea")), 2);
            ListDataResponse response = new ListDataResponse();
            
            
            //-----------------------------------------UPDATE  
            if (recordActtionFlag == PersistAction.UPDATE || recordActtionFlag == PersistAction.UPDATE_AFTER_APPROVE) {
                
                
                if (selectedRecord.getRec_play_mode() == 1) {
                    if (startDateEditable && !(selectedRecord.getRec_playStartDate().compareTo(previousDay) > 0)) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không được chọn ngày trong quá khứ! Vui lòng chọn lại ngày");
                        return;
                    }
                    if (!(selectedRecord.getRec_playExpireDate().compareTo(previousDay) > 0)) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không được chọn ngày trong quá khứ! Vui lòng chọn lại ngày");
                        return;
                    }
                    if (selectedRecord.getRec_playStartDate().after(selectedRecord.getRec_playExpireDate())) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Ngày kết thúc phải sau ngày bắt đầu! Vui lòng chọn lại ngày");
                        return;
                    }
                    selectedRecord.setRec_play_repeat_days(getRepeatDaysBit(listDaysSelected2, selectedRecord.getRec_play_repeat_type()));

                } else if (selectedRecord.getRec_play_mode() == 2){
                    selectedRecord.setRec_playStartDate(today);
                    selectedRecord.setRec_playExpireDate(today);
                } else {
                    //selectedRecord.setRec_play_mode(1);
                    if (!(selectedRecord.getRec_playStartDate().compareTo(previousDay) > 0)) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không được chọn ngày trong quá khứ! Vui lòng chọn lại ngày");
                        return;
                    }
                    selectedRecord.setRec_playExpireDate(selectedRecord.getRec_playStartDate());
                    Calendar c = Calendar.getInstance();
                    c.setTime(selectedRecord.getRec_playStartDate());
                    //selectedRecord.setRec_play_repeat_days((long) Math.pow(2, c.get(Calendar.DAY_OF_WEEK) - 2));
                    switch (c.get(Calendar.DAY_OF_WEEK)){
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            selectedRecord.setRec_play_repeat_days((long) Math.pow(2, c.get(Calendar.DAY_OF_WEEK) - 2));
                            break;
                        default:
                            selectedRecord.setRec_play_repeat_days(64);
                            break;
                        
                    }
                }
                    
                //Tin thường hay tin tiếp sóng
                if (selectedRecord.getRec_type() == 0) {   // tin thường
                    if (selectedRecord.getRecFileName().equals("")) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa tải lên file thông báo!");
                            return;
                    }
                    if(audioFile != null){
                        if (!uploadRecordToFileServer(audioFile)) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không thành công: Đã xảy ra lỗi trong quá trình upload file!");
                            return;
                        }
                    } else if (tmpAudioFile != null) {
                        if (!uploadRecordToFileServer2(tmpAudioFile)) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không thành công: Đã xảy ra lỗi trong quá trình upload file!");
                            return;
                        }
                    }
                    selectedRecord.setRec_play_timeString(recPlayTimeString);
                    if (selectedRecord.getRec_play_mode() != 2 && selectedRecord.getRec_play_time().size() == 0) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa nhập giờ phát hoặc giờ phát không hợp lệ!  Mời nhập vào giờ phát dạng HH:mm:ss cách nhau bởi dấu phẩy");
                        return;
                    } 

                     // Phát ngay lập tức
                    if (selectedRecord.getRec_play_mode() == 2) {
                        selectedRecord.setRec_play_repeat_type(-1); 
                        selectedRecord.setRec_priority(4);
                    } else {
                        // Check khung h phát
                        boolean isOutOfTime = true;
                        if (((List<TimeTable>)Session.getSessionValue(SessionKeyDefine.TIME_TABLE_LIST)).size() == 0) {
                            isOutOfTime = false;
                        } else {
                            for (Integer playTime : selectedRecord.getRec_play_time()) {
                                for (TimeTable timeTable : (List<TimeTable>)Session.getSessionValue(SessionKeyDefine.TIME_TABLE_LIST)) {
                                    if (playTime >= timeTable.getStart_time() && (playTime + selectedRecord.getDuration()/60) < timeTable.getEnd_time()) {
                                        isOutOfTime = false;
                                        break;
                                    }
                                }
                            }
                        }

                        if (isOutOfTime) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Ngoài khung giờ phát cho phép! Khung giờ phát cho phép:  " + Session.getSessionValue(SessionKeyDefine.TIME_TABLE_STRING));
                            return;
                        }
                        
                        //Cảnh báo sát giờ phát
                        if (checkNearPlayTime()) {
                            return;
                        }
                        
                        switch (selectedArea.getAreaType()){
                            case 1:
                                selectedRecord.setRec_priority(3);
                                break;
                            case 2:
                                selectedRecord.setRec_priority(2);
                                break;
                            case 3:
                            case 4:
                                selectedRecord.setRec_priority(1);
                                break;    
                        }
                    }
       
                } else if (selectedRecord.getRec_type() == 2) {   // Tin tiếp sóng
                    selectedRecord.setRec_priority(0);
                    selectedRecord.setRec_play_timeStringRadio(recPlayTimeStringForRadio);
                    
                    if (selectedRecord.getRec_play_mode() != 2 && selectedRecord.getRec_end_time().size() == 0) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa nhập khung giờ phát hoặc khung giờ phát không hợp lệ!  Mời nhập vào khung giờ phát dạng HH:mm:ss-HH:mm:ss cách nhau bởi dấu phẩy");
                        return;
                    } 
                    
                    // Phát ngay lập tức
                    if (selectedRecord.getRec_play_mode() == 2) {
                        selectedRecord.setRec_play_repeat_type(-1);
                        String startTime = Arrays.asList(recPlayTimeStringForRadio.split("-")).get(0);
                        //selectedRecord.setDuration(convertTimeToNumber(endTimeForRadio) - convertTimeToNumber(startTime)); 
                        //selectedRecord.setDuration((convertTimeToNumber(endTimeForRadio) - convertTimeToNumber(DateFormatterForRadio.format(selectedRecord.getCreateDate())))); 
                        recPlayTimeStringForRadio = startTime + "-" + endTimeForRadio;
                        selectedRecord.setRec_play_timeStringRadio(recPlayTimeStringForRadio);
                    } else {    // Phát theo lịch
                        //selectedRecord.setDuration((convertTimeToNumber(endTimeForRadio)-selectedRecord.getRec_play_time().get(0))); 
                    }
                    
                    if (selectedRecord.getDuration() <= 0) {
                        //ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian phát không hợp lệ! Vui lòng chọn lại");
                        //return;
                    }
                    
                } else {   // Tin thu phát FM
                    selectedRecord.setRec_priority(0);
                    selectedRecord.setRec_play_timeString(recPlayTimeString);
                    
                    // Phát ngay lập tức
                    if (selectedRecord.getRec_play_mode() == 2) {
                        selectedRecord.setRec_play_repeat_type(-1); 
                        selectedRecord.setDuration((convertTimeToNumber(endTimeForRadio) - convertTimeToNumber(DateFormatterForRadio.format(selectedRecord.getCreateDate())))); 
                           
                    } else {    // Phát theo lịch
                        selectedRecord.setDuration((convertTimeToNumber(endTimeForRadio)-selectedRecord.getRec_play_time().get(0))); 
                    }
                    
                    if (selectedRecord.getDuration() <= 0) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian phát không hợp lệ! Vui lòng chọn lại");
                            return;
                    }
                    
                }

                
                // Nếu chế độ phát đến MCU, lưu danh sách MCU ID
                if (selectedRecord.getRec_receipt_type() == 2) {
                    getListMCUSelected();
                }
                response = coreWS.executeRecord(selectedRecord, recordActtionFlag == PersistAction.UPDATE?2:3, overlap==3?1:0);
                
                
                
            //-----------------------------------------CREATE    
                
            } else if (recordActtionFlag == PersistAction.CREATE) {
                //getListAreaSelected();
                if (selectedRecord.getRec_play_mode() == 1) {
                    if (!(selectedRecord.getRec_playStartDate().compareTo(previousDay) > 0 && selectedRecord.getRec_playExpireDate().compareTo(previousDay) > 0)) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không được chọn ngày trong quá khứ! Vui lòng chọn lại ngày");
                        return;
                    }
                    if (selectedRecord.getRec_playStartDate().after(selectedRecord.getRec_playExpireDate())) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Ngày kết thúc phải sau ngày bắt đầu! Vui lòng chọn lại ngày");
                        return;
                    }
                    selectedRecord.setRec_play_repeat_days(getRepeatDaysBit(listDaysSelected2, selectedRecord.getRec_play_repeat_type()));
                } else if (selectedRecord.getRec_play_mode() == 2){
                    selectedRecord.setRec_playStartDate(today);
                    selectedRecord.setRec_playExpireDate(today);
                } else {
                    //selectedRecord.setRec_play_mode(1);
                    if (!(selectedRecord.getRec_playStartDate().compareTo(previousDay) > 0 )) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không được chọn ngày trong quá khứ! Vui lòng chọn lại ngày");
                        return;
                    }
                    selectedRecord.setRec_playExpireDate(selectedRecord.getRec_playStartDate());
                    Calendar c = Calendar.getInstance();
                    c.setTime(selectedRecord.getRec_playStartDate());
                    switch (c.get(Calendar.DAY_OF_WEEK)){
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            selectedRecord.setRec_play_repeat_days((long) Math.pow(2, c.get(Calendar.DAY_OF_WEEK) - 2));
                            break;
                        default:
                            selectedRecord.setRec_play_repeat_days(64);
                            break;
                        
                    }
                    
                }
                

                //Tin thường hay tin tiếp sóng
                if (selectedRecord.getRec_type() == 0) {   // tin thường
                    if (audioFile != null) {
                        if (!uploadRecordToFileServer(audioFile)) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không thành công: Đã xảy ra lỗi trong quá trình upload file!");
                            return;
                        }
                    } else if (tmpAudioFile != null) {
                        if (!uploadRecordToFileServer2(tmpAudioFile)) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không thành công: Đã xảy ra lỗi trong quá trình upload file!");
                            return;
                        }
                    } else if (selectedRecord.getRecFileName()==null || selectedRecord.getRecFileName().equals("")) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa tải lên file thông báo!");
                            return;
                    }
                    
                    
                    selectedRecord.setRec_play_timeString(recPlayTimeString);
                    if (selectedRecord.getRec_play_mode() != 2 && selectedRecord.getRec_play_time().size() == 0) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa nhập giờ phát hoặc giờ phát không hợp lệ!  Mời nhập vào giờ phát dạng HH:mm:ss cách nhau bởi dấu phẩy");
                        return;
                    } 

                    // Phát ngay lập tức
                    if (selectedRecord.getRec_play_mode() == 2) {
                        selectedRecord.setRec_play_repeat_type(-1); 
                        selectedRecord.setRec_priority(4);
                    } else {

                        // Check khung h phát
                        boolean isOutOfTime = true;
                        if (((List<TimeTable>)Session.getSessionValue(SessionKeyDefine.TIME_TABLE_LIST)).size() == 0) {
                            isOutOfTime = false;
                        } else {
                            for (Integer playTime : selectedRecord.getRec_play_time()) {
                                for (TimeTable timeTable : (List<TimeTable>)Session.getSessionValue(SessionKeyDefine.TIME_TABLE_LIST)) {
                                    if (playTime >= timeTable.getStart_time() && (playTime + selectedRecord.getDuration()/60) < timeTable.getEnd_time()) {
                                        isOutOfTime = false;
                                        break;
                                    }
                                }
                            }
                        }

                        if (isOutOfTime) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Ngoài khung giờ phát cho phép! Khung giờ phát cho phép:  " + Session.getSessionValue(SessionKeyDefine.TIME_TABLE_STRING));
                            return;
                        }

                        switch (selectedArea.getAreaType()){
                            case 1:
                                selectedRecord.setRec_priority(3);
                                break;
                            case 2:
                                selectedRecord.setRec_priority(2);
                                break;
                            case 3:
                            case 4:
                                selectedRecord.setRec_priority(1);
                                break;    
                        }
                        
                        //Cảnh báo sát giờ phát
                        if (checkNearPlayTime()) {
                            return;
                        }
                        
                    }
                       
                    
                    
                } else if (selectedRecord.getRec_type() == 2){   // Tin tiếp sóng
                    selectedRecord.setRec_priority(0);
                    //selectedRecord.setRec_play_repeat_days(getRepeatDaysBit(listDaysSelected2, selectedRecord.getRec_play_repeat_type()));
                    selectedRecord.setRec_play_timeStringRadio(recPlayTimeStringForRadio);
                    
                    if (selectedRecord.getRec_play_mode() != 2 && selectedRecord.getRec_end_time().size() == 0) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa nhập khung giờ phát hoặc khung giờ phát không hợp lệ!  Mời nhập vào khung giờ phát dạng HH:mm:ss-HH:mm:ss cách nhau bởi dấu phẩy");
                        return;
                    } 
                    
                    // Phát ngay lập tức
                    if (selectedRecord.getRec_play_mode() == 2) {
                        selectedRecord.setRec_play_repeat_type(-1); 
                        String startTime = DateFormatterForRadio.format(new Date());
                        //selectedRecord.setDuration((convertTimeToNumber(endTimeForRadio) - convertTimeToNumber(startTime))); 
                        if (recPlayTimeStringForRadio == null || recPlayTimeStringForRadio.length() < 13) {
                            recPlayTimeStringForRadio = startTime + "-" + endTimeForRadio;
                            selectedRecord.setRec_play_timeStringRadio(recPlayTimeStringForRadio);
                        }
                        
                    } else {
                        //selectedRecord.setDuration((convertTimeToNumber(endTimeForRadio)-selectedRecord.getRec_play_time().get(0))); 
                    }
                    
                    if (selectedRecord.getDuration() <= 0) {
                        //ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian phát không hợp lệ! Vui lòng chọn lại");
                        //    return;
                    }
                    
                } else {   // Tin thu phát Fm
                    selectedRecord.setRec_priority(0);
                    //selectedRecord.setRec_play_repeat_days(getRepeatDaysBit(listDaysSelected2, selectedRecord.getRec_play_repeat_type()));
                    selectedRecord.setRec_play_timeString(recPlayTimeString);
                    
                    // Phát ngay lập tức
                    if (selectedRecord.getRec_play_mode() == 2) {
                        selectedRecord.setRec_play_repeat_type(-1); 
                        selectedRecord.setDuration((convertTimeToNumber(endTimeForRadio) - convertTimeToNumber(DateFormatterForRadio.format(new Date())))); 
                           
                    } else {
                        selectedRecord.setDuration((convertTimeToNumber(endTimeForRadio)-selectedRecord.getRec_play_time().get(0))); 
                    }
                    
                    if (selectedRecord.getDuration() <= 0) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian phát không hợp lệ! Vui lòng chọn lại");
                            return;
                    }
                    
                }
                
                // Nếu chế độ phát đến MCU, lưu danh sách MCU ID
                if (selectedRecord.getRec_receipt_type() == 2) {
                    getListMCUSelected();
                }
                response = coreWS.executeRecord(selectedRecord, 1 , overlap==3?1:0);
                
                
            } else if (recordActtionFlag == PersistAction.APPROVE) {
                if (selectedRecord.getRec_type() == 0) {   // tin thường
                    
                    // Cảnh báo đang phát bản tin tiếp sóng
                    if (checkTSPlaying()) {
                            return;
                    }
                        
                    //Cảnh báo sát giờ phát
                    if (checkNearPlayTime()) {
                        return;
                    }
                    
                }
                response = coreWS.approveRecordNews(1, selectedRecord.getRec_id());
            } else if (recordActtionFlag == PersistAction.DELETE) {
                if (selectedRecord.getStatus() == 0) {   //Xoa tin tuc
                    response = coreWS.deactiveRecord(2, selectedRecord.getRec_id());
                } else {             // Huy tin tuc
                    response = coreWS.deactiveRecord( 1, selectedRecord.getRec_id());
                }
            }
            
            //Trùng lịch phát
            if (response.getCode() == 606 || response.getCode() == 607) {
                overlap = response.getCode() == 606? 1:2;
                Gson gson = new Gson();
                listOverlapRecord = new ArrayList<>();

                for (Object object : response.getJavaResponse()) {
                    Record record = (Record) gson.fromJson(gson.toJson(object),Record.class);
                    listOverlapRecord.add(record);
                }
            }
            
            if (response.getCode() == 200) {
                overlap = 0;
                listRecord = coreWS.getListRecordByArea(selectedArea.getAreaId(), true, fromDate, toDate);
                //tmpAudioFile.delete();
                
                if (recordActtionFlag == PersistAction.UPDATE_AFTER_APPROVE){
                    viewTimelineEvent();
                }

            }
            
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            //ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không thành công: Đã xảy ra lỗi không xác định!");
        }
        
    }
    
    //Canh bao sat gio phat
    public boolean checkNearPlayTime() {
        if (notice != -1) {
            notice = 0;
            if (selectedRecord.getRec_play_repeat_type() == 0 || selectedRecord.getRec_play_repeat_type() == 3) {   // lap lại theo tuan
                
                int mcuSpeed;
                mcuSpeed = SystemConfig.getConfigInt("mcu_speed");  
                mcuSpeed = mcuSpeed==0?8:mcuSpeed;
                
                Date today = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(today);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                String num = "1";
                for (int j = 2; j < dayOfWeek; j++) {
                    num += "0";
                }
                if ((Integer.valueOf(num, 2) & selectedRecord.getRec_play_repeat_days()) > 0) {
                    for (Integer playTime : selectedRecord.getRec_play_time()) {
                        int nowTime = today.getHours() * 3600 + today.getMinutes()*60 + today.getSeconds();
                        long filesize = selectedRecord.getDuration()*8;
                        if (playTime - nowTime < filesize/8 && playTime >= nowTime) {
                            notice = 1;
                            //String time = num/60 + ":" + (num%60 < 10 ? "0" + num%60 : num%60);
                        }


                    }
                }

            } else{  // Lap lai theo thang

            }

            if (notice == 1) {
                //ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Cảnh báo!");
                return true;
            }
        }
        return false;
    }
    
    
    //Canh bao sat gio phat
    public boolean checkTSPlaying() {
        if (noticeTS != -1) {
            noticeTS = 0;
            
            listTSPlaying = coreWS.getTSRecordPlayingByArea(selectedRecord.getRec_receipt());
            
            if (listTSPlaying.size() > 0) {
                noticeTS = 1;
            }
            
            if (noticeTS == 1) {
                //ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Cảnh báo!");
                return true;
            }
        }
        return false;
    }
    
    public void forceOverlapEvent(ActionEvent evt) { 
        overlap = 3;
        saveRecord(evt);
    }
    public void forceNoticeEvent(ActionEvent evt) { 
        notice = -1;
        saveRecord(evt);
    }
    
    public void forceNoticeTSEvent(ActionEvent evt) { 
        noticeTS = -1;
        saveRecord(evt);
    }
    public void closeOverlapEvent(ActionEvent evt) { 
        overlap = 0;
    }
    
    public void showCalendarEvent(ActionEvent evt) { 
        calendarMode = true;
    }
    
    public void showTimelineEvent(ActionEvent evt) { 
        calendarMode = false;
    }
    
    public void mixMultiFileEvent(ActionEvent evt) { 
        listAudioFile = new ArrayList<>();
        for (MyUploadedFile uploadedFile : listSaveAudioFile) {
            listAudioFile.add(uploadedFile);
        }
    }
    
    public void T2TEvent(ActionEvent evt) { 
        tmpT2TFile = tmpAudioFile;
        textForConvert = savedtextForConvert;
    }
    public void removeFileEvent(MyUploadedFile file) { 
        listAudioFile.remove(file);
        try{
        file.getUploadedFile().getInputstream().close();
        }catch (Exception ex1){
            //ex1.printStackTrace();
        }
    }
    
    public void changeFilterTimeEvent(ActionEvent evt) { 
        if (fromDate.after(toDate)) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Khoảng thời gian đã chọn không hợp lệ! Vui lòng kiểm tra lại");
            return;
        }
        loadRecord();
    }
    
    public void setDuration(ActionEvent evt) { 
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        double dur =  Double.parseDouble(map.get("duration").toString());
        
        long duration =  Math.round(dur);
        selectedRecord.setDuration(duration);
    }
    
    public void saveTimeEvent(ActionEvent evt) { 
//        if (hourAddeded2 != null) {
//            hourAdded = DateFormatterForTime.format(hourAddeded2);
//        }
        try{
            if (hourAdded.equals("") || hourAdded == null) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa chọn giờ!");
                return;
            }
            if(recPlayTimeString.equals(hourAdded)){
                return;
            }
            if(recPlayTimeString.length()>8 && recPlayTimeString.contains(hourAdded)){
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Giờ đã tồn tại! Vui lòng chọn lại");
                return;
            }
            

            if (recPlayTimeString == null || recPlayTimeString.length() < 8) {
                recPlayTimeString = hourAdded;
            } else {
                recPlayTimeString = recPlayTimeString.replaceAll("\\s","");
                List<String> timeList = Arrays.asList(recPlayTimeString.split(","));
                recPlayTimeString = timeList.toString().substring(1, timeList.toString().length()-1);
                
                if (selectedRecord.getDuration() > 0) {
                    List<Integer> selectedTime = DateUtil.convertTimeToNumber(recPlayTimeString);
                    for (Integer time : selectedTime) {
                        if (Math.abs(time - DateUtil.convertTimeToNumber(hourAdded).get(0)) < selectedRecord.getDuration()) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian trùng, chờm với thời gian đã chọn! Vui lòng chọn lại");
                            return;
                        }
                    }
                }
                recPlayTimeString = recPlayTimeString + ", " + hourAdded;
            }
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
    } 
    
    
    public void saveRadioTimeEvent(ActionEvent evt) { 
//        if (hourAddeded2 != null) {
//            hourAdded = DateFormatterForTime.format(hourAddeded2);
//        }
        try{
            if (startTimeAdded == null || startTimeAdded.equals("")) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa chọn giờ bắt đầu!");
                return;
            }
            if (endTimeAdded == null || endTimeAdded.equals("")) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa chọn giờ kết thúc!");
                return;
            }
            
            if (startTimeAdded.compareTo(endTimeAdded) >= 0) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian kết thúc phải sau thời gian bắt đầu! Vui lòng chọn lại");
                return;
            }
            
//            if(recPlayTimeStringForRadio.contains(startTimeAdded) || recPlayTimeStringForRadio.contains(endTimeAdded)){
//                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Giờ đã tồn tại! Vui lòng chọn lại");
//                return;
//            }
            

            if (recPlayTimeStringForRadio == null || recPlayTimeStringForRadio.length() < 13) {

                recPlayTimeStringForRadio = startTimeAdded + "-" + endTimeAdded;
            } else {
                
                List<Integer> startList = new ArrayList<>();
                List<Integer> endList = new ArrayList<>();
                String timeString = recPlayTimeStringForRadio.replaceAll("\\s","");
                List<String> timeList = Arrays.asList(timeString.split(","));
                
                recPlayTimeStringForRadio = timeList.toString().substring(1, timeList.toString().length()-1);
                

                for (String time : timeList) {

                    String start = Arrays.asList(time.split("-")).get(0);
                    String end = Arrays.asList(time.split("-")).get(1);


                    startList.add(convertStringToTime(start));
                    endList.add(convertStringToTime(end));
                }
                
                int startAdded = convertStringToTime(startTimeAdded);
                int endAdded = convertStringToTime(endTimeAdded);
                
                for (int i = 0; i < startList.size(); i++) {
                    if (!((startAdded <= startList.get(i) && endAdded <= startList.get(i)) || (startAdded >= endList.get(i) && endAdded >= endList.get(i)))) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian trùng, chờm với thời gian đã chọn! Vui lòng chọn lại");
                        return;
                    }
                }
                
                recPlayTimeStringForRadio = recPlayTimeStringForRadio + ", " + startTimeAdded + "-" + endTimeAdded;
            }
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian nhập vào không hợp lệ! Vui lòng chọn lại");
            
        }
       
       
    } 
    
    private static int convertStringToTime(String time) {
        int hh = Integer.parseInt(Arrays.asList(time.split(":")).get(0));
        int mm = Integer.parseInt(Arrays.asList(time.split(":")).get(1));
        int ss = Integer.parseInt(Arrays.asList(time.split(":")).get(2));
            
        if (hh > 23 || mm > 59 || ss > 59) {
             return -1;
        }
        return (hh * 3600 + mm * 60 + ss);
    }
    
    public void addPlayTimeEvent(ActionEvent evt) {
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("width", 640);
        options.put("height", 340);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");
         
        RequestContext.getCurrentInstance().openDialog("add_time", options, null);
    }
    
    public void eventChangePlayMode(ActionEvent evt){
        try {
            listDaysSelected2 = new int[0];
            listSelectedArea2 = new TreeNode[0];
            selectedRecord.setRec_play_repeat_type(0);
            
            if (selectedRecord.getRec_play_mode() == 3) {
                selectedRecord.setRec_playStartDate(today);
            }
            
            hourAdded = "";
            recPlayTimeString = "";
             
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void eventChangeRecType(ActionEvent evt){
        try {
            selectedRecord.setRec_url("");
            selectedRecord.setRecFileName("");
            audioFile = null;
            if (selectedRecord.getRec_type() != 0) {
                listChannel = (new AreaWebservice()).getListChannelByArea(selectedArea.getAreaId(), selectedRecord.getRec_type());
                selectedRecord.setRec_receipt_type(1);
                if (selectedRecord.getIsNext()){
                    if (selectedRecord.getRec_play_mode() == 1) { // theo lịch
                        //endTimeForRadio = convertNumberToTime(selectedRecord.getRec_play_time().get(0) + selectedRecord.getDuration());
                        recPlayTimeStringForRadio = selectedRecord.getRec_play_timeStringRadio();
                    } else {
                        //endTimeForRadio = convertNumberToTime(convertTimeToNumber(DateFormatterForRadio.format(selectedRecord.getCreateDate())) + selectedRecord.getDuration());
                        //endTimeForRadio = convertNumberToTime(selectedRecord.getRec_play_time().get(0) + selectedRecord.getDuration());
                        endTimeForRadio = convertNumberToTime(selectedRecord.getRec_end_time().get(0));
                    }
                }
            } 
            
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eventChangeRepeatType(ActionEvent evt){
        try {
             listDaysSelected2 = new int[0];
             
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eventChangeReceiptType(ActionEvent evt){
        try {
             //selectedRecord.setListPlayName("");
             selectedRecord.setRec_receipt_name("");
             selectedRecord.setRec_receipt(new ArrayList<Long>());
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public void eventSaveHouseMCU(ActionEvent evt){
//        try {
//            String listHouseName = "";
//            List<Long> listMCUReceipt = new ArrayList<>();
//            for (HouseHold houseHold : listSelectedMCUHouse) {
//                listHouseName += houseHold.getHolder_name() + ", ";
//                listMCUReceipt.add(houseHold.getMcu_id());
//            }
//            
//            selectedRecord.setListPlayName(listHouseName);
//            selectedRecord.setRec_receipt(listMCUReceipt);
//             
//        } catch (Exception ex) {
//            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    public void cancelRecord(ActionEvent evt){
        try {
            ListDataResponse response = new ListDataResponse();
             // Huy tin tuc
            response = coreWS.deactiveRecord( 1, selectedRecord.getRec_id());
            if (response.getCode() == 200) {
                listRecord = coreWS.getListRecordByArea(selectedArea.getAreaId(), true, fromDate, toDate);
            }
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void deleteNode() { 
        System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()"    +  " delete note");
        selectedNode.getChildren().clear();
        selectedNode.getParent().getChildren().remove(selectedNode);
        selectedNode.setParent(null);
         
        selectedNode = null;
    }  

    public AreaTree getSelectedArea() {
        return selectedArea;
    }

    public void setSelectedArea(AreaTree selectedArea) {
        this.selectedArea = selectedArea;
    }
    
    

    public void loadRecord() {
        
        try {
            if (selectedArea != null) {
                listRecord = coreWS.getListRecordByArea(selectedArea.getAreaId(), true, fromDate, toDate);

            }
        } catch (Exception ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void saveListArea(ActionEvent evt){
        try {
           getListAreaSelected();
            
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void eventChangeRecCategory(){
        
        
    }
    
    
    public void eventSelectArea1(){
        System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()" + "select area " + selectedArea.getAreaName());
        
    }
    
    public void eventUploadRecord(FileUploadEvent event) {
            String ext = event.getFile().getFileName();
            try {
                ext = ext.substring(ext.lastIndexOf(".") + 1);
                ext = ext.toUpperCase();
            } catch (Exception e) {
                ext = "";
            }
            
            if (ext == null || ext.length()==0 || !(ext.equals("WAV"))){
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "File không được hỗ trợ. Mời tải lên định dạng file WAV");
                return;
            } else {
                audioFile = event.getFile();
                selectedRecord.setRecFileName(audioFile.getFileName());
            }
    }
    
    
//    
//    public boolean uploadRecord(UploadedFile audioFile) {
//              String ext = audioFile.getFileName();
//            try {
//                ext = ext.substring(ext.lastIndexOf(".") + 1);
//                ext = ext.toUpperCase();
//            } catch (Exception e) {
//                ext = "";
//            }
//            
//            if (ext == null || ext.length()==0) {
//                
//            } else if (ext.equals("WAV") || ext.equals("MP3")){
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//                java.util.Date date = java.util.Calendar.getInstance().getTime();
//                String time = sdf.format(date);
//                //String appPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
//                String audioPath =SystemConfig.getConfig("AssetsDir") + "resource/audio/";
//                //boolean isCheckFolder=new File(APP_PATH + "/resource/images/"
//                //        + "").mkdirs();
//                boolean isCheckFolder=new File(audioPath).mkdirs();
//                try {
//                   // FileUtils.copyFile(imageFile, targetFile);
//                   saveToFile(audioFile.getInputstream(), audioPath + time +"_"+ audioFile.getFileName());
//                   System.out.print(audioPath + time +"_"+ audioFile.getFileName());
//                   selectedRecord.setRec_url("/resource/audio/"+ time +"_"+ audioFile.getFileName());
//                   return true;
//                } catch (IOException ex) {
//                    Logger.getLogger(MediaController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//            } else {
//                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "File không được hỗ trợ. Mời tải lên định dạng file JPG/JPEG/PNG/GIF!");
//               
//                
//            }
//        return  false;
//   
//    }
    
    //OLD function
    public void handleT2TEvent(ActionEvent evt) throws Exception { 
        t2tResponse = new Text2SpeechResponse();
        t2tResponse = coreWS.convertText2Speech(textForConvert, voice, speed);
        if (t2tResponse.getError()==0 && t2tResponse != null) {
            try {
                
                File tmp = null;

                do {                
                    String assetsDir = SystemConfig.getConfig("AssetsDir");
                    String outputTmpDir = assetsDir+ SystemConfig.getConfig("audioOutputTmp");
                    if(!new File(outputTmpDir).exists()){
                        new File(outputTmpDir).mkdirs();
                    }
                    String timestamp = System.currentTimeMillis()+"";
                    String outputPath = outputTmpDir + timestamp + "_"+"text-to-speech.mp3";
                    
                    URLConnection conn = new URL(t2tResponse.getAsync()).openConnection();
                    InputStream is = conn.getInputStream();

                    saveInputStreamToFile(is, outputPath);
                    //AudioSystem.write(AudioSystem.getAudioInputStream(new File(convertAudio(audioFile))), AudioFileFormat.Type.WAVE, new File(outputPath));
                    tmp = new File(outputPath);
                    TimeUnit.SECONDS.sleep(1);
                } while (!tmp.exists());
                
            } catch (InterruptedException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ClientMessage.logErr("Không thành công: " + "Đã xảy ra lỗi trong quá trình xử lý! Vui lòng thử lại!");
            return;
        }
    }
    
    //OLD function
    public void handleT2TEvent1(ActionEvent evt) { 
        String step = "";
        try {
            
            if (textForConvert.length() < 3) {
                 ClientMessage.logErr("Không thành công: " + "Vui lòng nhập đoạn văn bản dài hơn!");
                 return;
            }

            String text = textForConvert;
            String textAfter = "";

            List<String> listSub = new ArrayList<>();
            while (text.length()> 495) {
                String tmp = text.substring(0, 495);

                int pos = tmp.lastIndexOf(".");
                if (pos > 0) {
                    tmp = new StringBuilder(tmp).insert(tmp.lastIndexOf("."), "!.!").toString(); 
                } else {
                    tmp = new StringBuilder(tmp).insert(tmp.lastIndexOf(","), "!.!").toString(); 
                }

                textAfter += tmp;
                text = text.substring(495, text.length());
            }
            textAfter += text;


            List<String> valueArr = Arrays.asList(textAfter.split(Pattern.quote("!.!")));
            List<Text2SpeechResponse> listT2TResponse = new ArrayList<>();

            for (String subText : valueArr) {
                Text2SpeechResponse t2tResponse = coreWS.convertText2Speech(subText, voice, speed);

                if (t2tResponse.getError()!=0 || t2tResponse == null) {
                    ClientMessage.logErr("Không thành công: " + "Đã xảy ra lỗi trong quá trình xử lý! Vui lòng thử lại!");
                    return;
                }

                listT2TResponse.add(t2tResponse);
            }

            //for debug 
            step += "step1-";


            if (listT2TResponse.size() > 0) {
                AudioInputStream mixedFile = null;
                InputStream mixedFileIS = null;

                    for (Text2SpeechResponse t2tResponse : listT2TResponse) {

                            HttpURLConnection conn = (HttpURLConnection) new URL(t2tResponse.getAsync()).openConnection();
                            int status = conn.getResponseCode();
                            int errorCount = 0;
                            while (status != 200) {
                                TimeUnit.SECONDS.sleep(1);
                                conn = (HttpURLConnection) new URL(t2tResponse.getAsync()).openConnection();
                                status = conn.getResponseCode();
                                if (errorCount == 5) {
                                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Server không phản hồi! Vui lòng thử lại");
                                    return;
                                }
                                errorCount ++;
                            }

                            InputStream is = conn.getInputStream();
                            if (mixedFileIS != null) {
                                mixedFileIS = new SequenceInputStream(mixedFileIS, is);
                            } else {
                                mixedFileIS = is;
                            } 
                            
                            step += "step2-";



                    }
                    mixedFile = AudioSystem.getAudioInputStream(new File(convertAudio2(mixedFileIS)));
                    //AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File("C:\\wavAppended.wav")); 
                    tmpT2TFile = null;
                    String timestamp = "";
                    
                    step += "step3-";

                    if(mixedFile != null) {
                        
                            String assetsDir = SystemConfig.getConfig("AssetsDir");
                            String outputTmpDir = assetsDir+ SystemConfig.getConfig("audioOutputTmp");
                            if(!new File(outputTmpDir).exists()){
                                new File(outputTmpDir).mkdirs();
                            }
                            timestamp = System.currentTimeMillis()+"";
                            String outputPath = outputTmpDir + timestamp + "_"+"T2T_file.wav";


                            AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File(outputPath));
                            tmpT2TFile = new File(outputPath);
                            //tmpAudioFile = new File(outputPath);
                            //audioFile = (UploadedFile) tmpAudioFile;

                        RequestContext.getCurrentInstance().execute("reloadPlayerT2T()");  
                    }

            } else {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Chưa chọn file âm thanh!");
                return;
            }
        
        } catch (IOException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "1-" + step + ex.getMessage());
                return;
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "2-" + step + ex.getMessage());
                return;
        } catch (InterruptedException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE .ERR, "3-" + step + ex.getMessage());
                return;
        }   catch (Exception ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "4-" + step + ex.getMessage());
                return;
        }
    }
    
//    public void handleT2TEvent2(ActionEvent evt) { 
//         String step = "";
//        try {
//            
//            if (textForConvert.length() < 3) {
//                 ClientMessage.logErr("Không thành công: " + "Vui lòng nhập đoạn văn bản dài hơn!");
//                 return;
//            }
//
//            String text = textForConvert;
//            String textAfter = "";
//
//            List<String> listSub = new ArrayList<>();
//            while (text.length()> 495) {
//                String tmp = text.substring(0, 495);
//
//                int pos = tmp.lastIndexOf(".");
//                if (pos > 0) {
//                    tmp = new StringBuilder(tmp).insert(tmp.lastIndexOf("."), "!.!").toString(); 
//                } else {
//                    tmp = new StringBuilder(tmp).insert(tmp.lastIndexOf(","), "!.!").toString(); 
//                }
//
//                textAfter += tmp;
//                text = text.substring(495, text.length());
//            }
//            textAfter += text;
//
//
//            List<String> valueArr = Arrays.asList(textAfter.split(Pattern.quote("!.!")));
//            List<Text2SpeechResponse> listT2TResponse = new ArrayList<>();
//
//            for (String subText : valueArr) {
//                Text2SpeechResponse t2tResponse = coreWS.convertText2Speech(subText, voice, speed);
//
//                if (t2tResponse.getError()!=0 || t2tResponse == null) {
//                    ClientMessage.logErr("Không thành công: " + "Đã xảy ra lỗi trong quá trình xử lý! Vui lòng thử lại!");
//                    return;
//                }
//
//                listT2TResponse.add(t2tResponse);
//            }
//            
//            step += "step1-";
//            
//            if (listT2TResponse.size() > 0) {
//                AudioInputStream mixedFile = null;
//                InputStream mixedFileIS = null;
//
//                    for (Text2SpeechResponse t2tResponse : listT2TResponse) {
//
//                            //URL url = new URL(t2tResponse.getAsync());
//                            URL url = new URL(null, t2tResponse.getAsync(),new sun.net.www.protocol.https.Handler());
//                            // open HTTPS connection
//                            HttpsURLConnection conn = null;
//                            conn = (HttpsURLConnection)url.openConnection();
//                           ((HttpsURLConnection) conn).setHostnameVerifier(new MyHostnameVerifier());
//
//    
//                            //HttpsURLConnection conn = (HttpsURLConnection) new URL(t2tResponse.getAsync()).openConnection();
//                            int status = conn.getResponseCode();
//                            int errorCount = 0;
//                            while (status != 200) {
//                                TimeUnit.SECONDS.sleep(1);
//                                //conn = (HttpURLConnection) new URL(t2tResponse.getAsync()).openConnection();
//                                 url = new URL(null, t2tResponse.getAsync(),new sun.net.www.protocol.https.Handler());
//                                // open HTTPS connection
//                                conn = (HttpsURLConnection)url.openConnection();
//                               ((HttpsURLConnection) conn).setHostnameVerifier(new MyHostnameVerifier());
//                                
//                                
//                                status = conn.getResponseCode();
//                                if (errorCount == 5) {
//                                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Server không phản hồi! Vui lòng thử lại");
//                                    return;
//                                }
//                                errorCount ++;
//                            }
//
//                            InputStream is = conn.getInputStream();
//                            if (mixedFileIS != null) {
//                                mixedFileIS = new SequenceInputStream(mixedFileIS, is);
//                            } else {
//                                mixedFileIS = is;
//                            } 
//                            
//                            step += "step2-";
//
//                    }
//                    mixedFile = AudioSystem.getAudioInputStream(new File(convertAudio2(mixedFileIS)));
//                    //AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File("C:\\wavAppended.wav")); 
//                    tmpT2TFile = null;
//                    String timestamp = "";
//                    
//                    step += "step3-";
//
//
//                    if(mixedFile != null) {
//                        
//                            String assetsDir = SystemConfig.getConfig("AssetsDir");
//                            String outputTmpDir = assetsDir+ SystemConfig.getConfig("audioOutputTmp");
//                            if(!new File(outputTmpDir).exists()){
//                                new File(outputTmpDir).mkdirs();
//                            }
//                            timestamp = System.currentTimeMillis()+"";
//                            String outputPath = outputTmpDir + timestamp + "_"+"T2T_file.wav";
//
//
//                            AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File(outputPath));
//                            tmpT2TFile = new File(outputPath);
//                            //tmpAudioFile = new File(outputPath);
//                            //audioFile = (UploadedFile) tmpAudioFile;
//
//                        RequestContext.getCurrentInstance().execute("reloadPlayerT2T()");  
//                    }
//
//            } else {
//                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Chưa chọn file âm thanh!");
//                return;
//            }
//        
//        } catch (IOException ex) {
//            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "1-" + step + ex.getMessage());
//                return;
//        } catch (UnsupportedAudioFileException ex) {
//            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "2-" + step + ex.getMessage());
//                return;
//        } catch (InterruptedException ex) {
//            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE .ERR, "3-" + step + ex.getMessage());
//                return;
//        }   catch (Exception ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "4-" + step + ex.getMessage());
//                return;
//        }
//       
//    }
    
    public void handleT2TEvent2(ActionEvent evt) {
        String step = "";
        try {

            if (textForConvert.length() < 3) {
                ClientMessage.logErr("Không thành công: " + "Vui lòng nhập đoạn văn bản dài hơn!");
                return;
            }

            String text = textForConvert;
            String textAfter = "";

            List<String> listSub = new ArrayList<>();
            while (text.length() > 495) {
                String tmp = text.substring(0, 495);

                int pos = tmp.lastIndexOf(".");
                if (pos > 0) {
                    tmp = new StringBuilder(tmp).insert(tmp.lastIndexOf("."), "!.!").toString();
                } else {
                    tmp = new StringBuilder(tmp).insert(tmp.lastIndexOf(","), "!.!").toString();
                }

                textAfter += tmp;
                text = text.substring(495, text.length());
            }
            textAfter += text;

            List<String> valueArr = Arrays.asList(textAfter.split(Pattern.quote("!.!")));
            List<Text2SpeechResponse> listT2TResponse = new ArrayList<>();

            for (String subText : valueArr) {
                Text2SpeechResponse t2tResponse = coreWS.convertText2Speech(subText, voice, speed);

                if (t2tResponse.getError() != 0 || t2tResponse == null) {
                    ClientMessage.logErr("Không thành công: " + "Đã xảy ra lỗi trong quá trình xử lý! Vui lòng thử lại!");
                    return;
                }

                listT2TResponse.add(t2tResponse);
            }

            step += "step1-";

            if (listT2TResponse.size() > 0) {
                AudioInputStream mixedFile = null;
                InputStream mixedFileIS = null;
                URL url = null;
                HttpsURLConnection conn = null;
                for (Text2SpeechResponse t2tResponse : listT2TResponse) {

                    url = new URL(t2tResponse.getAsync());
                    // URL url = new URL(null, t2tResponse.getAsync(), new sun.net.www.protocol.https.Handler());
                    // open HTTPS connection

                    conn = (HttpsURLConnection) url.openConnection();
                    ((HttpsURLConnection) conn).setHostnameVerifier(new MyHostnameVerifier());

                    //HttpsURLConnection conn = (HttpsURLConnection) new URL(t2tResponse.getAsync()).openConnection();
                    int status = conn.getResponseCode();
                    int errorCount = 0;
                    while (status != 200) {
                        TimeUnit.SECONDS.sleep(1);
                        //conn = (HttpURLConnection) new URL(t2tResponse.getAsync()).openConnection();
                        //url = new URL(null, t2tResponse.getAsync(), new sun.net.www.protocol.https.Handler());
                        // open HTTPS connection
                        //conn = (HttpsURLConnection) url.openConnection();
                        ((HttpsURLConnection) conn).setHostnameVerifier(new MyHostnameVerifier());

                        status = conn.getResponseCode();
                        if (errorCount == 5) {
                            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Server không phản hồi! Vui lòng thử lại");
                            return;
                        }
                        errorCount++;
                    }

                    InputStream is = conn.getInputStream();
                    if (mixedFileIS != null) {
                        mixedFileIS = new SequenceInputStream(mixedFileIS, is);
                    } else {
                        mixedFileIS = is;
                    }

                    step += "step2-";

                }
                mixedFile = AudioSystem.getAudioInputStream(new File(convertAudio2(mixedFileIS)));
                //AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File("C:\\wavAppended.wav")); 
                tmpT2TFile = null;
                String timestamp = "";

                step += "step3-";

                if (mixedFile != null) {

                    String assetsDir = SystemConfig.getConfig("AssetsDir");
                    String outputTmpDir = assetsDir + SystemConfig.getConfig("audioOutputTmp");
                    if (!new File(outputTmpDir).exists()) {
                        new File(outputTmpDir).mkdirs();
                    }
                    timestamp = System.currentTimeMillis() + "";
                    String outputPath = outputTmpDir + timestamp + "_" + "T2T_file.wav";

                    AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File(outputPath));
                    tmpT2TFile = new File(outputPath);
                    //tmpAudioFile = new File(outputPath);
                    //audioFile = (UploadedFile) tmpAudioFile;

                    RequestContext.getCurrentInstance().execute("reloadPlayerT2T()");
                }

            } else {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Chưa chọn file âm thanh!");
                return;
            }

        } catch (IOException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "1-" + step + ex.getMessage());
            return;
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "2-" + step + ex.getMessage());
            return;
        } catch (InterruptedException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "3-" + step + ex.getMessage());
            return;
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "4-" + step + ex.getMessage());
            return;
        }

    }
     
    
    public void handleT2TMobiFoneEvent(ActionEvent evt) { 
         String step = "1-";
        try {
            
            if (textForConvert.length() < 3) {
                 ClientMessage.logErr("Không thành công: " + "Vui lòng nhập đoạn văn bản dài hơn!");
                 return;
            }

            String text = textForConvert;
            String textAfter = "";

            List<String> listSub = new ArrayList<>();
            while (text.length()> 495) {
                String tmp = text.substring(0, 495);

                int pos = tmp.lastIndexOf(".");
                if (pos > 0) {
                    tmp = new StringBuilder(tmp).insert(tmp.lastIndexOf("."), "!.!").toString(); 
                } else {
                    tmp = new StringBuilder(tmp).insert(tmp.lastIndexOf(","), "!.!").toString(); 
                }

                textAfter += tmp;
                text = text.substring(495, text.length());
            }
            textAfter += text;


            List<String> valueArr = Arrays.asList(textAfter.split(Pattern.quote("!.!")));
            List<Text2SpeechResponse> listT2TResponse = new ArrayList<>();
            
            AudioInputStream mixedFile = null;
            InputStream mixedFileIS = null;
            int i = 0;
            List<String> listFile = new ArrayList<>();
            
            for (String subText : valueArr) {
                i++;
                InputStream is = coreWS.convertText2SpeechMobi(subText, voice, speed);
                
                if (is == null) {
                    ClientMessage.logErr("Không thành công: " + "Text to speech server không phản hồi! Vui lòng thử lại!");
                    return;
                }
//                if (mixedFileIS != null) {
//                    mixedFileIS = new SequenceInputStream(mixedFileIS, is);
//                } else {
//                    mixedFileIS = is;
//                } 
                
                
                String timestamp = System.currentTimeMillis()+"";
                String assetsDir = SystemConfig.getConfig("AssetsDir");
                String outputRaw = assetsDir+ SystemConfig.getConfig("audioOutputRaw");
                saveInputStreamToFile(is,outputRaw + "/"+i + "_"+ timestamp + ".mp3");
                
                listFile.add(outputRaw + "/"+i + "_"+ timestamp + ".mp3");
            }
            
            //mixedFile = AudioSystem.getAudioInputStream(new File(convertAudio2(mixedFileIS)));
            
            if (listFile.size() > 0) {
                for (String string : listFile) {
                    if (mixedFileIS != null) {
                        mixedFileIS = new SequenceInputStream(mixedFileIS, new FileInputStream(new File(string)));
                    } else {
                        mixedFileIS = new FileInputStream(new File(string));
                    } 
                }
            }
            
            
            mixedFile = AudioSystem.getAudioInputStream(new File(convertAudio2(mixedFileIS)));
            
            tmpT2TFile = null;
            String timestamp = "";
            if(mixedFile != null) {
                        
                    String assetsDir = SystemConfig.getConfig("AssetsDir");
                    String outputTmpDir = assetsDir+ SystemConfig.getConfig("audioOutputTmp");
                    if(!new File(outputTmpDir).exists()){
                        new File(outputTmpDir).mkdirs();
                    }
                    timestamp = System.currentTimeMillis()+"";
                    String outputPath = outputTmpDir + timestamp + "_"+"T2T_file.wav";


                    AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File(outputPath));
                    tmpT2TFile = new File(outputPath);
                    //tmpAudioFile = new File(outputPath);
                    //audioFile = (UploadedFile) tmpAudioFile;
                    try{
                        mixedFileIS.close();
                        mixedFile.close();
                    } catch (Exception ex1){
                        //ex1.printStackTrace();
                    }
                RequestContext.getCurrentInstance().execute("reloadPlayerT2T()");  
            }

        } catch (IOException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "1-" + step + ex.getMessage());
                return;
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "2-" + step + ex.getMessage());
                return;
        } catch (InterruptedException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE .ERR, "3-" + step + ex.getMessage());
                return;
        }   catch (Exception ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "4-" + step + ex.getMessage());
                return;
        }
       
    }
    /*public void handlePostT2TMobiFoneEvent(ActionEvent evt) { 
        String step = "1-";
        try {
            
            if (textForConvert.length() < 3) {
                 ClientMessage.logErr("Không thành công: " + "Vui lòng nhập đoạn văn bản dài hơn!");
                 return;
            }

            CallWebservice ws = new CallWebservice();
            InputStream is = ws.convertT2TMobiPost(textForConvert, voice, speed, voiceNames);
                
                if (is == null) {
                    ClientMessage.logErr("Không thành công: " + "Text to speech server không phản hồi! Vui lòng thử lại!");
                    return;
                } 
               
            step+="2-";

            AudioInputStream mixedFile = null;
            String outFileName = convertAudio2(is);
            if (outFileName == null){
                ClientMessage.logErr("Text to speech server không phản hồi! Lỗi không thể chuyển đoạn văn bản thành giọng nói!");
                return;
            }
            mixedFile = AudioSystem.getAudioInputStream(new File(outFileName));
            tmpT2TFile = null;
            String timestamp = "";
            if(mixedFile != null) {
                    step+="3-";    
                    String assetsDir = SystemConfig.getConfig("AssetsDir");
                    String outputTmpDir = assetsDir+ SystemConfig.getConfig("audioOutputTmp");
                    if(!new File(outputTmpDir).exists()){
                        new File(outputTmpDir).mkdirs();
                    }
                    timestamp = System.currentTimeMillis()+"";
                    String outputPath = outputTmpDir + timestamp + "_"+"T2T_file.wav";

                    step+="4-";    
                    AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File(outputPath));
                    tmpT2TFile = new File(outputPath);
                    //tmpAudioFile = new File(outputPath);
                    //audioFile = (UploadedFile) tmpAudioFile;

                RequestContext.getCurrentInstance().execute("reloadPlayerT2T()"); 
               
            }

        } catch (IOException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "1-" + step + ex.getMessage());
                return;
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "2-" + step + ex.getMessage());
                return;
        } catch (InterruptedException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE .ERR, "3-" + step + ex.getMessage());
                return;
        }   catch (Exception ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không thành công: " + step + ex.getMessage());
                return;
        }
        
       
    }*/
    public void handlePostT2TMobiFoneEvent(ActionEvent evt) { 
        String step = "1-";
        try {
            
            if (textForConvert.length() < 3) {
                 ClientMessage.logErr("Không thành công: " + "Vui lòng nhập đoạn văn bản dài hơn!");
                 return;
            }
            String text = textForConvert;
            String textAfter = "";

            List<String> listSub = new ArrayList<>();
            while (text.length()> 495) {
                String tmp = text.substring(0, 495);

                int pos = tmp.lastIndexOf(".");
                if (pos > 0) {
                    tmp = new StringBuilder(tmp).insert(tmp.lastIndexOf("."), "!.!").toString(); 
                } else {
                    tmp = new StringBuilder(tmp).insert(tmp.lastIndexOf(","), "!.!").toString(); 
                }

                textAfter += tmp;
                text = text.substring(495, text.length());
            }
            textAfter += text;


            List<String> valueArr = Arrays.asList(textAfter.split(Pattern.quote("!.!")));
            List<Text2SpeechResponse> listT2TResponse = new ArrayList<>();
            CallWebservice ws = new CallWebservice();
            InputStream mixedFileIS = null;
             int i = 0;
            List<String> listFile = new ArrayList<>();
            for (String subText : valueArr) {
                InputStream is = ws.convertT2TMobiPost(subText, voice, speed, voiceNames);
                if (is == null) {
                    ClientMessage.logErr("Không thành công: " + "Đã xảy ra lỗi trong quá trình xử lý! Vui lòng thử lại!");
                    return;
                }
                
                String timestamp = System.currentTimeMillis()+"";
                String assetsDir = SystemConfig.getConfig("AssetsDir");
                String outputRaw = assetsDir+ SystemConfig.getConfig("audioOutputRaw");
                saveInputStreamToFile(is,outputRaw + "/"+i + "_"+ timestamp + ".mp3");
                
                listFile.add(outputRaw + "/"+i + "_"+ timestamp + ".mp3");
                
                /*if (mixedFileIS != null) {
                    mixedFileIS = new SequenceInputStream(mixedFileIS, is);
                } else {
                    mixedFileIS = is;
                }*/
            }
            if (listFile.size() > 0) {
                for (String string : listFile) {
                    if (mixedFileIS != null) {
                        mixedFileIS = new SequenceInputStream(mixedFileIS, new FileInputStream(new File(string)));
                    } else {
                        mixedFileIS = new FileInputStream(new File(string));
                    } 
                }
            }
            
            if (mixedFileIS == null) {
                ClientMessage.logErr("Không thành công: " + "Text to speech server không phản hồi! Vui lòng thử lại!");
                return;
            } 
               
            step+="2-";

            //AudioInputStream mixedFile = null;
            String outFileName = convertAudio2(mixedFileIS);
            if (outFileName == null){
                ClientMessage.logErr("Text to speech server không phản hồi! Lỗi không thể chuyển đoạn văn bản thành giọng nói!");
                return;
            }
            //mixedFile = AudioSystem.getAudioInputStream(new File(outFileName));
            tmpT2TFile = new File(outFileName);
            try{
                        mixedFileIS.close();
                        //mixedFile.close();
                    }catch(Exception ex1){
                        //ex1.printStackTrace();
                    }
                RequestContext.getCurrentInstance().execute("reloadPlayerT2T()"); 
            /*String timestamp = "";
            if(mixedFile != null) {
                    step+="3-";    
                    String assetsDir = SystemConfig.getConfig("AssetsDir");
                    String outputTmpDir = assetsDir+ SystemConfig.getConfig("audioOutputTmp");
                    if(!new File(outputTmpDir).exists()){
                        new File(outputTmpDir).mkdirs();
                    }
                    timestamp = System.currentTimeMillis()+"";
                    String outputPath = outputTmpDir + timestamp + "_"+"T2T_file.wav";

                    step+="4-";    
                    AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File(outputPath));
                    tmpT2TFile = new File(outputPath);
                    //tmpAudioFile = new File(outputPath);
                    //audioFile = (UploadedFile) tmpAudioFile;
                    try{
                        mixedFileIS.close();
                        mixedFile.close();
                    }catch(Exception ex1){
                        //ex1.printStackTrace();
                    }
                RequestContext.getCurrentInstance().execute("reloadPlayerT2T()"); 
               
            }*/

        } catch (IOException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "1-" + step + ex.getMessage());
                return;
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "2-" + step + ex.getMessage());
                return;
        } catch (InterruptedException ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE .ERR, "3-" + step + ex.getMessage());
                return;
        }   catch (Exception ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không thành công: " + step + ex.getMessage());
                return;
        }
        
       
    }
   
    public static class MyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
                return true;
        }
    }

     
    public void saveT2TFileEvent(ActionEvent evt) { 
        if (tmpT2TFile!=null && tmpT2TFile.exists()) {
            tmpAudioFile = tmpT2TFile;
            savedtextForConvert = textForConvert;
            selectedRecord.setRecFileName(tmpAudioFile.getName());
            RequestContext.getCurrentInstance().execute("reloadPlayer()"); 
        } else {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Chưa có file âm thanh!");
            return;
        }
    }
//    
//    public void saveT2TFileEvent(ActionEvent evt) { 
//        try {
//            String assetsDir = SystemConfig.getConfig("AssetsDir");
//            String outputTmpDir = assetsDir+ SystemConfig.getConfig("audioOutputTmp");
//            if(!new File(outputTmpDir).exists()){
//                new File(outputTmpDir).mkdirs();
//            }
//            String timestamp = System.currentTimeMillis()+"";
//            String outputPath = outputTmpDir + timestamp + "_"+"text-to-speech.mp3";
//            
//            URLConnection conn = new URL(t2tResponse.getAsync()).openConnection();
//            InputStream is = conn.getInputStream();
//            
//            saveInputStreamToFile(is, outputPath);
//            //AudioSystem.write(AudioSystem.getAudioInputStream(new File(convertAudio(audioFile))), AudioFileFormat.Type.WAVE, new File(outputPath));
//            //tmpAudioFile = new File(outputPath);
//            
//            InputStream  ais =  (InputStream) new BufferedInputStream(new FileInputStream(new File(outputPath)));
//            //String content = convertAudio2(ais);
//            tmpAudioFile = new File(convertAudio2(ais));
//            
//            
//            selectedRecord.setRecFileName(timestamp + "_text-to-speech.mp3");
//            RequestContext.getCurrentInstance().execute("reloadPlayer()");  
//            System.out.print("Succesful"+ "text-to-speech.mp3" + " is uploaded.");
//        } catch (IOException ex) {
//            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//            ClientMessage.logErr("Không thành công: " + "Đã xảy ra lỗi trong quá trình xử lý! Vui lòng thử lại!");
//            return;
//        }
//    }
    
    public void handleMixFileEvent(ActionEvent evt) { 
        if (listAudioFile.size() > 0) {
            listSaveAudioFile = new ArrayList<>();
            AudioInputStream mixedFile = null;
            audioFile = null;
            try {
                for (MyUploadedFile uploadedFile : listAudioFile) {
                        listSaveAudioFile.add(uploadedFile);

                        AudioInputStream clip1 = AudioSystem.getAudioInputStream(new File(convertAudio(uploadedFile.getUploadedFile())));
                        if (mixedFile != null) {
                            mixedFile = new AudioInputStream(new SequenceInputStream(mixedFile, clip1),     
                                            mixedFile.getFormat(), mixedFile.getFrameLength() + clip1.getFrameLength());
                        } else { 
                            mixedFile = clip1;
                        }              
                }
                //AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File("C:\\wavAppended.wav")); 
                tmpAudioFile = null;
                String timestamp = "";


                if(mixedFile != null) {
                    try {


                        String assetsDir = SystemConfig.getConfig("AssetsDir");
                        String outputTmpDir = assetsDir+ SystemConfig.getConfig("audioOutputTmp");
                        if(!new File(outputTmpDir).exists()){
                            new File(outputTmpDir).mkdirs();
                        }
                        timestamp = System.currentTimeMillis()+"";
                        String outputPath = outputTmpDir + timestamp + "_"+"mixed_file.wav";


                        AudioSystem.write(mixedFile, AudioFileFormat.Type.WAVE, new File(outputPath));
                        tmpAudioFile = new File(outputPath);
                        //audioFile = (UploadedFile) tmpAudioFile;
                        try{
                        mixedFile.close();
                    }catch(Exception ex1){
                        //ex1.printStackTrace();
                    }
                    } catch (IOException ex) {
                        Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    selectedRecord.setRecFileName(timestamp + "_mixed_file.wav");
                    RequestContext.getCurrentInstance().execute("reloadPlayer()");  
                    //System.out.print("Succesful"+ audioFile.getFileName() + " is uploaded.");
                }



            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Chưa chọn file âm thanh!");
            return;
        }
        
            
    }
    
    public boolean uploadRecordToFileServer(UploadedFile uploadedFile) {
       // System.err.println("event.getFile().getFileName() = " + file2.getFileName());
        /*try {
            //copyFile(event.getFile().getFileName(), event.getFile().getInputstream());


        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
        
        String ext = uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf(".") + 1);
        ext = ext.toUpperCase();
        File convertedFile = null;
        
        
        convertedFile = new File(convertAudio(uploadedFile));
       
        
        

        if(!convertedFile.exists()){
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Quá trình xử lý file gặp lỗi");
            return false;
        }
        
        HttpClient client = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(SystemConfig.getConfig("WSAudioUrl"));
        try {
            //Set various attributes
            MultipartEntity multiPartEntity = new MultipartEntity();
            multiPartEntity.addPart("fileDescription", new StringBody(""));
            multiPartEntity.addPart("fileName", new StringBody(convertedFile.getName()));
            InputStreamBody fileBody = new InputStreamBody(new FileInputStream(convertedFile),"application/octect-stream",convertedFile.getName());
            //Prepare payload
            multiPartEntity.addPart("file", fileBody);
            //Set to request body
            
            postRequest.setEntity(multiPartEntity);
            postRequest.addHeader("username", "cmsuser");
            postRequest.addHeader("password", "cmspass!@#");
            //Send request
            HttpResponse response = client.execute(postRequest);
            //Verify response if any
            if (response != null) {
                if (response.getStatusLine().getStatusCode()==200){
                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    JsonParser jsonParser = new JsonParser();
                    JsonObject entityJs = (JsonObject)jsonParser.parse(responseString);
                    JsonObject javaResponse= entityJs.get("javaResponse").getAsJsonObject();
                    if (javaResponse!=null)
                    selectedRecord.setRec_url(javaResponse.get("audioUri").getAsString());
                    //selectedRecord.setRec_url("20170830/20170830115501_1504068945337_tuyenngondoclap.mp3.wav");
                    
                    
                    
//                    AudioInputStream audioInputStream = null; 
//                    if (ext.equals("WAV")) {
//                        audioInputStream = AudioSystem.getAudioInputStream(uploadedFile.getInputstream());
//                    } else {
//                        
//                    }
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(convertedFile);
                    AudioFormat format = audioInputStream.getFormat();
                    long audioFileLength = convertedFile.length();
                    int frameSize = format.getFrameSize();
                    long frameRate = (long) format.getFrameRate();
                    long durationInSeconds = (audioFileLength / (frameSize * frameRate));
                    try{
                        audioInputStream.close();
                        fileBody.getInputStream().close();
                    }catch (Exception ex1){
                        //ex1.printStackTrace();
                    }
//                    if (frameSize * frameRate * 8 / 1000 > Long.parseLong(SystemConfig.getConfig("maxBitRate"))) {
//                        selectedRecord.setRec_url(null);
//                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "File không hỗ trợ! Hệ thống chỉ hỗ trợ file có bitrate tối đa 192kbps");
//                        return false;
//                    }
                    
//                    AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(convertedFile);
//                    Map properties = baseFileFormat.properties();
//                    String key_author = "author";
//                    String author = (String) properties.get(key_author);
//                    String key_duration = "duration";
//                    Long duration = (Long) properties.get(key_duration);
                    
                    selectedRecord.setDuration(durationInSeconds);
                    return true;
                }
            }
            
             
            
        } catch (Exception ex) {
            selectedRecord.setRec_url(null);
            ex.printStackTrace();
            return false;
        }
        return false;
    }
    
    public boolean uploadRecordToFileServer2(File convertedFile) {
       // System.err.println("event.getFile().getFileName() = " + file2.getFileName());
        /*try {
            //copyFile(event.getFile().getFileName(), event.getFile().getInputstream());


        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
        //File convertedFile = new File(convertAudio(uploadedFile));
        

        if(!convertedFile.exists()){
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Quá trình xử lý file gặp lỗi");
            return false;
        }
        
        HttpClient client = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(SystemConfig.getConfig("WSAudioUrl"));
        try {
            //Set various attributes
            MultipartEntity multiPartEntity = new MultipartEntity();
            multiPartEntity.addPart("fileDescription", new StringBody(""));
            multiPartEntity.addPart("fileName", new StringBody(convertedFile.getName()));
            InputStreamBody fileBody = new InputStreamBody(new FileInputStream(convertedFile),"application/octect-stream",convertedFile.getName());
            //Prepare payload
            multiPartEntity.addPart("file", fileBody);
            //Set to request body
            
            postRequest.setEntity(multiPartEntity);
            postRequest.addHeader("username", "cmsuser");
            postRequest.addHeader("password", "cmspass!@#");
            //Send request
            HttpResponse response = client.execute(postRequest);
            //Verify response if any
            if (response != null) {
                if (response.getStatusLine().getStatusCode()==200){
                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    JsonParser jsonParser = new JsonParser();
                    JsonObject entityJs = (JsonObject)jsonParser.parse(responseString);
                    JsonObject javaResponse= entityJs.get("javaResponse").getAsJsonObject();
                    if (javaResponse!=null)
                    selectedRecord.setRec_url(javaResponse.get("audioUri").getAsString());
                    //selectedRecord.setRec_url("20170830/20170830115501_1504068945337_tuyenngondoclap.mp3.wav");
                    
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(convertedFile);
                    AudioFormat format = audioInputStream.getFormat();
                    long audioFileLength = convertedFile.length();
                    int frameSize = format.getFrameSize();
                    long frameRate = (long) format.getFrameRate();
                    long durationInSeconds = (audioFileLength / (frameSize * frameRate));
             
                    selectedRecord.setDuration(durationInSeconds);
                    try{
                        audioInputStream.close();
                        fileBody.getInputStream().close();
                    }catch (Exception ex1){
                        //ex1.printStackTrace();
                    }
                    return true;
                }
            }
        } catch (Exception ex) {
            selectedRecord.setRec_url(null);
            ex.printStackTrace();
        }
        return false;
    }
    
   public String convertAudio(UploadedFile audioFile){
        String outputFile = null;
        try {
           String timestamp = System.currentTimeMillis()+"";
            String assetsDir = SystemConfig.getConfig("AssetsDir");
            String outputRaw = assetsDir+ SystemConfig.getConfig("audioOutputRaw");
            String outputConverted =assetsDir+ SystemConfig.getConfig("audioOutputConverted");


            File folderConverted = new File(outputConverted);
            File folderRow = new File(outputRaw);
            if(!folderRow.exists()|| !folderRow.isDirectory()){
                folderRow.mkdirs();
            }
            if(!folderConverted.exists()|| !folderConverted.isDirectory()){
                folderConverted.mkdirs();
            }


            String audioFormat = SystemConfig.getConfig("audioAudioFormat");
            String audioCodec = SystemConfig.getConfig("audioCodec");
            String samplingRate = SystemConfig.getConfig("audioSamplingRate");
            
            String outputFileName = timestamp+"_"+audioFile.getFileName().replaceAll("[^A-Za-z0-9\\.]","");
            String outputFilePath = outputRaw+outputFileName;
            saveInputStreamToFile(audioFile.getInputstream(), outputFilePath);
            File f = new File(outputFilePath);
            if(!f.exists()){
                throw new FileNotFoundException("upload failed");
            }

            Attributes alaw = new Attributes(audioFormat, audioCodec, Integer.parseInt(samplingRate), 1);
            outputFile = outputConverted+outputFileName+ "."+ alaw.getFormat();
            Transcoder.transcode(f.getAbsolutePath(), outputFile, alaw);
            f.delete();
       } catch (Exception e) {
           e.printStackTrace();
           ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Xảy ra lỗi trong quá trình convert file: " + e);
       }
        
        if(outputFile!=null && new File(outputFile).exists()){
            return outputFile;            
        }else{
            return null;
        }

        
    }
    
     public String convertAudio2(InputStream stream){
        String outputFile = null;
        try {
           String timestamp = System.currentTimeMillis()+"";
            String assetsDir = SystemConfig.getConfig("AssetsDir");
            String outputRaw = assetsDir+ SystemConfig.getConfig("audioOutputRaw");
            String outputConverted =assetsDir+ SystemConfig.getConfig("audioOutputConverted");


            File folderConverted = new File(outputConverted);
            File folderRow = new File(outputRaw);
            if(!folderRow.exists()|| !folderRow.isDirectory()){
                folderRow.mkdirs();
            }
            if(!folderConverted.exists()|| !folderConverted.isDirectory()){
                folderConverted.mkdirs();
            }


            String audioFormat = SystemConfig.getConfig("audioAudioFormat");
            String audioCodec = SystemConfig.getConfig("audioCodec");
            String samplingRate = SystemConfig.getConfig("audioSamplingRate");
            String outputFileName = timestamp+"_text-to-speech.mp3";
            String wavFileName = timestamp+"_T2T_file";
            String outputFilePath = outputRaw+outputFileName;
            saveInputStreamToFile(stream, outputFilePath);
            File f = new File(outputFilePath);
            if(!f.exists()){
                throw new FileNotFoundException("upload failed");
            }

            Attributes alaw = new Attributes(audioFormat, audioCodec, Integer.parseInt(samplingRate), 1);
            outputFile = outputConverted+ wavFileName+ "."+ alaw.getFormat();
            Transcoder.transcode(f.getAbsolutePath(), outputFile, alaw);
            f.delete();
       } catch (Exception e) {
           
           e.printStackTrace();
       }
        
        if(outputFile!=null && new File(outputFile).exists()){
            return outputFile;            
        }else{
            return null;
        }

        
    }
    

    
    public String getCategoryName(long catId) {
        for (Category recCat : listRecordcategory) {
            if (recCat.getId() == catId) {
                return recCat.getName();
                
                
            }
        }
        
        return "";
    }
    
    public static long getRepeatDaysBit(int[] listDays, int mode){  // mode = 0:week, 1:mounth
        
        long result = 0;
        int[] dayArray = null;
        String dayString ="";
        
        try {
            if (mode == 0) {
                dayArray = new int[7];
                for (int day : listDays) {
                    if (day > 0) {
                        dayArray[8 - day] = 1;
                    }
                }
            } else if(mode == 1) {
                dayArray = new int[31];
                for (int day : listDays) {
                    if (day > 0) {
                        dayArray[31-day] = 1;
                    }
                }
            }

            dayString = Arrays.toString(dayArray);
            dayString = dayString.replaceAll("\\s","");
            dayString = dayString.replaceAll(",", "");
            dayString = dayString.substring(1, dayString.toString().length()-1);
            
            
            if (mode == 0) {
                result = (Integer.valueOf(dayString, 2) & Common.WEEK);
            } else if(mode == 1) {
                result = (Integer.valueOf(dayString, 2) & Common.MOUNTH);
            }
            
        } catch (Exception e) {
            
        }
        return result;
    }
    
    public static int[] getRepeatDaysFromBit(long listDaysBit, int mode){  // mode = 0:week, 1:mounth
        
        List<Integer> listDays = new ArrayList<>();
        int[] result = null;
        String dayString ="";
        
        try {
            if (mode == 0) {
                for (int i = 0; i <= 6; i++) {
                    String num = "1";
                    for (int j = 0; j < i; j++) {
                        num += "0";
                    }
                    if ((Integer.valueOf(num, 2) & listDaysBit) > 0) {
                        listDays.add(i+2);
                    }
                    
                }
            } else if(mode == 1) {
                for (int i = 0; i <= 30; i++) {
                      String num = "1";
                      for (int j = 0; j < i; j++) {
                          num += "0";
                      }
                      if ((Integer.valueOf(num, 2) & listDaysBit) > 0) {
                          listDays.add(i+1);
                      }

                  }
            }
            
            result = new int[listDays.size()];
            for (int i = 0; i < listDays.size(); i++) {
                result[i] = listDays.get(i);
            }


        } catch (Exception e) {
            
        }
        return result;
    }
    
    private void saveToFile(InputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];
                File f = new File(target);
                f.setExecutable(true, false);
                f.setReadable(true, false);
		out = new FileOutputStream(f);
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
    }
    
  
    public void uploadAudioHandler(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        
        String ext;
        try {
            ext = file.getFileName().substring(file.getFileName().lastIndexOf(".") + 1);
            ext = ext.toUpperCase();
        } catch (Exception e) {
            ext = "";
        }

        if (ext == null || ext.length()==0) {

        } else if (!(ext.equals("WAV") || ext.equals("MP3") || ext.equals("AMR") || ext.equals("GIF") || ext.equals("M4A") ||ext.equals("AAC") || ext.equals("FLAC") || ext.equals("OGG"))){
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "File không được hỗ trợ. Mời tải lên định dạng file WAV/MP3/M4A/AMR/AAC/OGG..."); 
            return;
        }
        
//        // Check file size
//        if (file.getSize()/(1024 *1024) > Long.parseLong(SystemConfig.getConfig("maxFileSize"))) { 
//            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Hệ thống chỉ hỗ trợ file có dng lượng tối đa " + SystemConfig.getConfig("maxFileSize") + "MB"); 
//            return;
//        }
        //tmpAudioFile = null;
        
        if(file != null) {
            try {
                
                
                String assetsDir = SystemConfig.getConfig("AssetsDir");
                String outputTmpDir = assetsDir+ SystemConfig.getConfig("audioOutputTmp");
                if(!new File(outputTmpDir).exists()){
                    new File(outputTmpDir).mkdirs();
                }
                String timestamp = System.currentTimeMillis()+"";
                String outputPath = outputTmpDir + timestamp + "_"+file.getFileName();
                
                saveInputStreamToFile(file.getInputstream(), outputPath);
                //AudioSystem.write(AudioSystem.getAudioInputStream(new File(convertAudio(audioFile))), AudioFileFormat.Type.WAVE, new File(outputPath));
                
                
                float bitrate = 0;
                if (ext.equals("MP3")) {
                    Mp3File mp3 = new Mp3File(outputPath);
                    bitrate = mp3.getBitrate();
                } else if (ext.equals("WAV") && file.getContentType().compareTo("application/octet-stream") != 0) {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getInputstream());
                    AudioFormat format = audioInputStream.getFormat();
                    int frameSize = format.getFrameSize();
                    float frameRate = format.getFrameRate();
                    bitrate = frameSize * frameRate * 8 /1000;
                }
               
                if (bitrate > Long.parseLong(SystemConfig.getConfig("maxBitRate"))) { 
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Hệ thống chỉ hỗ trợ file có bitrate tối đa " + SystemConfig.getConfig("maxBitRate") + "kbps"); 
                    return;
                }
                
                audioFile = file;
                tmpAudioFile = new File(outputPath);
                selectedRecord.setRecFileName(audioFile.getFileName());
                //RequestContext.getCurrentInstance().update("fileName");
                RequestContext.getCurrentInstance().execute("reloadPlayer()");  
                System.out.print("Succesful"+ audioFile.getFileName() + " is uploaded.");
                
                
            } catch (IOException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedTagException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidDataException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
            
           
            


            
            
//            try {
//                
//                
//                
//                Media hit = new Media(file.toURI().toString());
//                Duration d = hit.getDuration();
//                //MediaPlayer mediaPlayer = new MediaPlayer(hit);
//               
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
//                AudioFormat format = audioInputStream.getFormat();
//                long audioFileLength = tmpAudioFile.length();
//                int frameSize = format.getFrameSize();
//                float frameRate = format.getFrameRate();
//                
//            } catch (UnsupportedAudioFileException ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               }
            
            
            
            
           
            


            
            
//            try {
//                
//                
//                
//                Media hit = new Media(file.toURI().toString());
//                Duration d = hit.getDuration();
//                //MediaPlayer mediaPlayer = new MediaPlayer(hit);
//               
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
//                AudioFormat format = audioInputStream.getFormat();
//                long audioFileLength = tmpAudioFile.length();
//                int frameSize = format.getFrameSize();
//                float frameRate = format.getFrameRate();
//                
//            } catch (UnsupportedAudioFileException ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               }
            
            
            
            
           
            


            
            
//            try {
//                
//                
//                
//                Media hit = new Media(file.toURI().toString());
//                Duration d = hit.getDuration();
//                //MediaPlayer mediaPlayer = new MediaPlayer(hit);
//               
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
//                AudioFormat format = audioInputStream.getFormat();
//                long audioFileLength = tmpAudioFile.length();
//                int frameSize = format.getFrameSize();
//                float frameRate = format.getFrameRate();
//                
//            } catch (UnsupportedAudioFileException ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               }
            
            
            
            
           
            


            
            
//            try {
//                
//                
//                
//                Media hit = new Media(file.toURI().toString());
//                Duration d = hit.getDuration();
//                //MediaPlayer mediaPlayer = new MediaPlayer(hit);
//               
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
//                AudioFormat format = audioInputStream.getFormat();
//                long audioFileLength = tmpAudioFile.length();
//                int frameSize = format.getFrameSize();
//                float frameRate = format.getFrameRate();
//                
//            } catch (UnsupportedAudioFileException ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               }
            
        }
    }
    
    public void uploadAudioHandlerForMix(FileUploadEvent event) {
        
        UploadedFile audioFile = event.getFile();
        String ext;
        try {
            ext = audioFile.getFileName().substring(audioFile.getFileName().lastIndexOf(".") + 1);
            ext = ext.toUpperCase();
        } catch (Exception e) {
            ext = "";
        }

        if (ext == null || ext.length()==0) {

        } else if (!(ext.equals("WAV") || ext.equals("MP3") || ext.equals("AMR") || ext.equals("GIF") || ext.equals("M4A") ||ext.equals("AAC") || ext.equals("FLAC") || ext.equals("OGG"))){
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "File không được hỗ trợ. Mời tải lên định dạng file WAV/MP3/M4A/AMR/AAC/OGG..."); 
            return;
        }
        
//        //Check file size
//        if (audioFile.getSize()/(1024 *1024) > Long.parseLong(SystemConfig.getConfig("maxFileSize"))) { 
//            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Hệ thống chỉ hỗ trợ file có dng lượng tối đa " + SystemConfig.getConfig("maxFileSize") + "MB"); 
//            return;
//        }
       
        listAudioFile.add(new MyUploadedFile(audioFile));
        
//        if(audioFile != null) {
//            try {
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(tmpAudioFile);
//                AudioFormat format = audioInputStream.getFormat();
//                long audioFileLength = tmpAudioFile.length();
//                int frameSize = format.getFrameSize();
//                float frameRate = format.getFrameRate();
//                float durationInSeconds = (audioFileLength / (frameSize * frameRate));
//            } catch (UnsupportedAudioFileException ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               }
//
//        }
    }
    
    public AreaTree getAreaRoot() {
        return areaRoot;
    }

    public void setAreaRoot(AreaTree areaRoot) {
        this.areaRoot = areaRoot;
    }

    public AreaTree getAddedArea() {
        return addedArea;
    }

    public void setAddedArea(AreaTree addedArea) {
        this.addedArea = addedArea;
    }

    public PersistAction getRecordActtionFlag() {
        return recordActtionFlag;
    }

    public void setRecordActtionFlag(PersistAction houseActtionFlag) {
        this.recordActtionFlag = houseActtionFlag;
    }

  
    public UploadedFile getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(UploadedFile audioFile) {
        this.audioFile = audioFile;
    }

    
    public List<Category> getListRecordcategory() {
        return listRecordcategory;
    }

    public void setListRecordcategory(List<Category> listRecordcategory) {
        this.listRecordcategory = listRecordcategory;
    }
    
    

    public List<Record> getListRecord() {
        return listRecord;
    }

    public void setListRecord(List<Record> listRecord) {
        this.listRecord = listRecord;
    }

    public Record getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(Record selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public StreamedContent getPlaybackStream() throws FileNotFoundException, IOException {
                
        if(tmpAudioFile!= null && tmpAudioFile.exists()){
            InputStream is = new FileInputStream(tmpAudioFile);
            String mimeType = URLConnection.guessContentTypeFromStream(is);
            return new DefaultStreamedContent(is, mimeType, tmpAudioFile.getName()," identity;q=1, *;q=0",(int)tmpAudioFile.length());
        }else if(this.selectedRecord != null && this.selectedRecord.getRec_url()!=null){
          
            URLConnection connection = new URL(SystemConfig.getConfig("WSAudioDownload") + selectedRecord.getRec_url().replace("/", "%2f")).openConnection();
            connection.setRequestProperty("username", SystemConfig.getConfig("AudioWSUser"));
            connection.setRequestProperty("password", SystemConfig.getConfig("AudioWSPass"));
            InputStream response = connection.getInputStream();
            
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            byte[] bytes = new byte[1024];
//            int count;
//            while ((count = response.read(bytes)) > 0) {
//                out.write(bytes, 0, count);
//            }
//            Integer size = out.size();
//          
            return new DefaultStreamedContent(response, "audio/wav", selectedRecord.getRecFileName());
        }else{
            return null;
        }
    }
    
    public StreamedContent getPlaybackStreamForT2T() throws FileNotFoundException, IOException {
                
        if(tmpT2TFile!= null && tmpT2TFile.exists()){
            InputStream is = new FileInputStream(tmpT2TFile);
            String mimeType = URLConnection.guessContentTypeFromStream(is);
            return new DefaultStreamedContent(is, mimeType, tmpT2TFile.getName()," identity;q=1, *;q=0",(int)tmpT2TFile.length());
        }else if(this.selectedRecord != null && this.selectedRecord.getRec_url()!=null){
          
            URLConnection connection = new URL(SystemConfig.getConfig("WSAudioDownload") + selectedRecord.getRec_url().replace("/", "%2f")).openConnection();
            connection.setRequestProperty("username", SystemConfig.getConfig("AudioWSUser"));
            connection.setRequestProperty("password", SystemConfig.getConfig("AudioWSPass"));
            InputStream response = connection.getInputStream();
                 
            return new DefaultStreamedContent(response, "audio/wav", selectedRecord.getRecFileName());
        }else{
            return null;
        }
    }

    public List<DayObject> getListDaysWeek() {
        return listDaysWeek;
    }

    public void setListDaysWeek(List<DayObject> listDaysWeek) {
        this.listDaysWeek = listDaysWeek;
    }

    public List<DayObject> getListDaysMounth() {
        return listDaysMounth;
    }

    public void setListDaysMounth(List<DayObject> listDaysMounth) {
        this.listDaysMounth = listDaysMounth;
    }

    public List<Integer> getListDaysSelected() {
        return listDaysSelected;
    }

    public void setListDaysSelected(List<Integer> listDaysSelected) {
        this.listDaysSelected = listDaysSelected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public void upload1(String subject){
        System.out.println(""+ subject);
    }

    public List<AreaTree> getListSelectedArea() {
        //AreaTree area =  listSelectedArea.get(1);
        return listSelectedArea;
        
    }

    public void setListSelectedArea(List<AreaTree> listSelectedArea) {
        this.listSelectedArea = listSelectedArea;
    }

    public TreeNode[] getListSelectedArea2() {
        return listSelectedArea2;
    }

    public void setListSelectedArea2(TreeNode[] listSelectedArea2) {
        this.listSelectedArea2 = listSelectedArea2;

    }

    public int[] getListDaysSelected2() {
        return listDaysSelected2;
    }

    public void setListDaysSelected2(int[] listDaysSelected2) {
        this.listDaysSelected2 = listDaysSelected2;
    }

    
    public void showAreaSelectEvent(ActionEvent evt) { 
        resetTreeState(areaRoot);
        if (selectedRecord.getRec_receipt() != null) {
            resetTree(areaRoot, getListAreaSelectedFromID(selectedRecord.getRec_receipt()));
        }
        areaRoot.getChildren().get(0).setExpanded(true);
        listSelectedAreaName = selectedRecord.getRec_receipt_name();
    }
    
    public void saveAreaEvent(ActionEvent evt) { 
        //selectedRecord.setListPlayName(listSelectedAreaName);
        selectedRecord.setRec_receipt_name(listSelectedAreaName);
        selectedRecord.setRec_receipt(playListId);
    }

    public String getListSelectedAreaName() {
        return listSelectedAreaName;
    }
    
    
    
    // Save list area receipt 
    private List<Long> playListId;
    private String listSelectedAreaName;
    public void getListAreaSelected(){
        listSelectedArea = new ArrayList<>();
        playListId = new ArrayList<>();
        listSelectedAreaName = "";
        
        if (listSelectedArea2.length > 0) {
            try {
                for (int i = 0; i < listSelectedArea2.length; i++) {
                    if (!containsArea(listSelectedArea2[i].getParent())) {
                        listSelectedArea.add((AreaTree) listSelectedArea2[i]);
                        if (listSelectedAreaName.length() < 1) {
                            listSelectedAreaName += ((AreaTree) listSelectedArea2[i]).getAreaName();
                        } else {
                            listSelectedAreaName += " , " + ((AreaTree) listSelectedArea2[i]).getAreaName();
                        }
                        
                        playListId.add(((AreaTree) listSelectedArea2[i]).getAreaId());
                    }
                    
                }

            } catch (Exception e) {
                
            }
        }
        
    }
    
    // Save list MCU receipt 
    public void getListMCUSelected(){
        List<Long> playListId = new ArrayList<>();
        for (int i = 0; i < listReceiptMCU.length; i++) {
            playListId.add(listReceiptMCU[i]);
        }
        selectedRecord.setRec_receipt(playListId);
    }
    
    // get list MCU ID selected for view
    public void getListMCUSelectedForView(){
        listReceiptMCU = new long[selectedRecord.getRec_receipt().size()];
        for (int i = 0; i < selectedRecord.getRec_receipt().size(); i++) {
            listReceiptMCU[i] = selectedRecord.getRec_receipt().get(i);
        }
    }
    
    
    public TreeNode[] getListAreaSelectedFromID(List<Long> listAreaId){
        TreeNode[] result = new TreeNode[listAreaId.size()];
        for (int i = 0; i < listAreaId.size(); i++) {
            for (AreaTree area : service.getListArea()) {
                if (area.getAreaId() == listAreaId.get(i)) {
                    result[i] = (TreeNode)area;
                }
            }
        }
        
        return result;
    }
    
    
    
    
    private Boolean containsArea(TreeNode node){
        Boolean result = false;
        
        for (int i = 0; i < listSelectedArea2.length; i++) {
            if (((AreaTree)listSelectedArea2[i]).getAreaId() - ((AreaTree)node).getAreaId() == 0) {
                result = true;
            }

        }
        
        return result;
        
        
    }

    public void uploadAssets() {
        try{
            String fileObj= FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()  
           .get("file");  
            String filename= FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()  
           .get("filename"); 
            byte dearr[] = Base64.getDecoder().decode(fileObj);
            //String imgPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resource/images";
            String imgPath =SystemConfig.getConfig("AssetsDir") + "resource/images";
            boolean isCheckFolder=new File(imgPath).mkdirs();
            FileOutputStream fos = new FileOutputStream(new File(imgPath+"/" + filename)); 
            fos.write(dearr); 
            fos.close();
            
            Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
            //add owners permission
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            //add group permissions
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.GROUP_EXECUTE);
            //add others permissions
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_EXECUTE);
            Files.setPosixFilePermissions(Paths.get(imgPath+"/" + filename), perms);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
   }

    private static int convertTimeToNumber(String timeString){
        
        int hh = Integer.parseInt(Arrays.asList(timeString.split(":")).get(0));
        int mm = Integer.parseInt(Arrays.asList(timeString.split(":")).get(1));
        int ss = Integer.parseInt(Arrays.asList(timeString.split(":")).get(2));

        if (hh > 23 || mm > 59|| ss > 59) {
            return 0;
        }
        int num =  hh * 3600 + mm * 60 + ss ;
        
        return num;
    }
   
    private static String convertNumberToTime(long num){
        //String time = num/60 + ":" + (num%60 < 10 ? "0" + num%60 : num%60);
        
        String time = (num/3600 < 10 ? "0" + num/3600 : num/3600)
                    + ":" + ((num%3600)/60 < 10 ? "0" + (num%3600)/60 : (num%3600)/60) 
                    + ":" + (num%60 < 10 ? "0" + num%60 : num%60);
        return time;
    }
    
    
    
   
//   public boolean convertAudio(UploadedFile audioFile) throws IOException, EncoderException{
//        String timestamp = System.currentTimeMillis()+"";
//        String assetsDir = SystemConfig.getConfig("AssetsDir");
//        String outputRaw = assetsDir+ SystemConfig.getConfig("audioOutputRaw");
//        String outputConverted =assetsDir+ SystemConfig.getConfig("audioOutputConverted");
//
//        
//        File folderConverted = new File(outputConverted);
//        File folderRow = new File(outputRaw);
//        if(!folderRow.exists()|| !folderRow.isDirectory()){
//            folderRow.mkdirs();
//        }
//        if(!folderConverted.exists()|| !folderConverted.isDirectory()){
//            folderConverted.mkdirs();
//        }
//        
//
//        String audioFormat = SystemConfig.getConfig("audioAudioFormat");
//        String audioCodec = SystemConfig.getConfig("audioCodec");
//        String samplingRate = SystemConfig.getConfig("audioSamplingRate");
//        
//        saveInputStreamToFile(audioFile.getInputstream(), outputRaw+timestamp+"_"+audioFile.getFileName());
//        File f = new File(outputRaw+timestamp+"_"+audioFile.getFileName());
//        if(!f.exists()){
//            throw new FileNotFoundException("upload failed");
//        }
//        
//        Attributes alaw = new Attributes(audioFormat, audioCodec, Integer.parseInt(samplingRate), new Integer(1));
//        String outputFile = outputConverted+timestamp+"_"+audioFile.getFileName()+ "."+ alaw.getFormat();
//        Transcoder.transcode(f.getAbsolutePath(), outputFile, alaw);
//
//        selectedRecord.setRec_url(outputFile);
//        return true;
//        
//    }
//    
    private void saveInputStreamToFile(InputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];

		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
    }
    
    private void saveAudioInputStreamToFile(AudioInputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];

		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
    }
   
    //- ----  quyen doi voi ho ban tin
    public boolean isAllowUpdateNEW(){
        return super.isIsAllowUpdate("EDIT_NEWS") ;
    }
    public boolean isAllowInsertNEW(){
        return super.isIsAllowInsert("ADD_NEWS") ;
    }
    public boolean isAllowDeleteNEW(){
        return super.isIsAllowDelete("DELETE_NEWS") ;
    }
     public boolean isAllowApprovalNEW(){
        return super.isIsAllowApproval("APPROVAL_NEWS") ;
    }
    
     //- ----  quyen doi voi thong bao
    public boolean isAllowUpdateRECORD(){
        return super.isIsAllowUpdate("EDIT_RECORD") ;
    }
    public boolean isAllowInsertRECORD(){
        return super.isIsAllowInsert("ADD_RECORD") ;
    }
    public boolean isAllowDeleteRECORD(){
        return super.isIsAllowDelete("DELETE_RECORD") ;
    }
    
    public boolean isAllowApprovalRECORD(){
        return super.isIsAllowApproval("APPROVAL_RECORD") ;
    }
    
    public boolean isAllowSTREAMING(){
        return super.isIsAllowApproval("ALLOW_STREAMING") ;
    }

    public Date getToday() {
        return today;
    }

    public String getHourAdded() {
         return hourAdded;
    }

    public void setHourAdded(String hourAdded) {
        this.hourAdded = hourAdded;
    }

    public Date getHourAddeded2() {
        return hourAddeded2;
    }

    public void setHourAddeded2(Date hourAddeded2) {
        this.hourAddeded2 = hourAddeded2;
    }
    
    

    public List<HouseHold> getListMCUHouse() {
        return listMCUHouse;
    }

    public void setListMCUHouse(List<HouseHold> listMCUHouse) {
        this.listMCUHouse = listMCUHouse;
    }

    public String getRecPlayTimeString() {
        return recPlayTimeString;
    }

    public void setRecPlayTimeString(String recPlayTimeString) {
        this.recPlayTimeString = recPlayTimeString;
    }

    public String getRecPlayTimeStringForRadio() {
        return recPlayTimeStringForRadio;
    }

    public void setRecPlayTimeStringForRadio(String recPlayTimeStringForRadio) {
        this.recPlayTimeStringForRadio = recPlayTimeStringForRadio;
    }

    public HouseHold[] getListSelectedMCUHouse() {
        return listSelectedMCUHouse;
    }

    public void setListSelectedMCUHouse(HouseHold[] listSelectedMCUHouse) {
        this.listSelectedMCUHouse = listSelectedMCUHouse;
    }

    public long[] getListReceiptMCU() {
        return listReceiptMCU;
    }

    public void setListReceiptMCU(long[] listReceiptMCU) {
        this.listReceiptMCU = listReceiptMCU;
    }

    public boolean isAllowHouseManagement() {
        return allowHouseManagement;
    }

    public boolean isIsCityMode() {
        return isCityMode;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public TimelineModel getModel() {  
        return model;  
    }  
   
    public Date getStart() {  
        return start;  
    }  
   
    public Date getEnd() {  
        return end;  
    }  

    public AreaTree getSelectedAreaForTimeline() {
        return selectedAreaForTimeline;
    }

    public void setSelectedAreaForTimeline(AreaTree selectedAreaForTimeline) {
        if (selectedAreaForTimeline != null) {
            this.selectedAreaForTimeline = selectedAreaForTimeline;
        } 
    }

    public List<EventRecord> getEvents() {
        return events;
    }

    public EventRecord[] getEvents2() {
        return events2;
    }

    public String getEventsAsJson() {
        if (events != null) {
            return new Gson().toJson(events);
        }
        return null;    
    }

    public boolean isCalendarMode() {
        return calendarMode;
    }

    public Date getDayNextYear() {
        return dayNextYear;
    }

    public List<RadioChannel> getListChannel() {
        return listChannel;
    }

    public String getEndTimeForRadio() {
        return endTimeForRadio;
    }

    public void setEndTimeForRadio(String endTimeForRadio) {
        this.endTimeForRadio = endTimeForRadio;
    }

    public Date getFromDateForTimeline() {
        return fromDateForTimeline;
    }

    public void setFromDateForTimeline(Date fromDateForTimeline) {
        this.fromDateForTimeline = fromDateForTimeline;
    }

    public Date getToDateForTimeline() {
        return toDateForTimeline;
    }

    public void setToDateForTimeline(Date toDateForTimeline) {
        this.toDateForTimeline = toDateForTimeline;
    }
    
    public boolean isAdminUser (){
        return SecUser.getUserLogged().getRoles().contains(new Long(1))?true:false;
    }

    public int getOverlap() {
        return overlap;
    }

    public List<Record> getListOverlapRecord() {
        return listOverlapRecord;
    }

    public List<Record> getListTSPlaying() {
        return listTSPlaying;
    }
    
    public Record getOverlapRecord() {
        return overlapRecord;
    }

    public int[] getListDaysSelectedOverlap() {
        return listDaysSelectedOverlap;
    }

    public String getEndTimeForRadioOverlap() {
        return endTimeForRadioOverlap;
    }

    public List<MyUploadedFile> getListAudioFile() {
        return listAudioFile;
    }

    public String getTextForConvert() {
        return textForConvert;
    }

    public void setTextForConvert(String textForConvert) {
        this.textForConvert = textForConvert;
    }

    public Text2SpeechResponse getT2tResponse() {
        return t2tResponse;
    }

    public void setT2tResponse(Text2SpeechResponse t2tResponse) {
        this.t2tResponse = t2tResponse;
    }

    public int getVoice() {
        return voice;
    }

    public void setVoice(int voice) {
        this.voice = voice;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public List<HistoryRecord> getRecordHistory() {
        return recordHistory;
    }

    public List<MCUHistory> getMcuHistory() {
        return mcuHistory;
    }

    public int getNotice() {
        return notice;
    }

    public int getNoticeTS() {
        return noticeTS;
    }

    public boolean isStartDateEditable() {
        return startDateEditable;
    }

    public String getStartTimeAdded() {
        return startTimeAdded;
    }

    public void setStartTimeAdded(String startTimeAdded) {
        this.startTimeAdded = startTimeAdded;
    }

    public String getEndTimeAdded() {
        return endTimeAdded;
    }

    public void setEndTimeAdded(String endTimeAdded) {
        this.endTimeAdded = endTimeAdded;
    }
    public void valueChangeListener(ValueChangeEvent ex){
        if (recPlayTimeString == null || recPlayTimeString.isEmpty()){
            recPlayTimeString = "";
        }
    }
     public void txtPlayTimeChangeValue(AjaxBehaviorEvent vce) {
        System.out.println(recPlayTimeString);
        String name= (String) ((UIOutput) vce.getSource()).getValue();
        System.out.println(name);
     }
     public void txtPlayTimeChangeValue(ValueChangeEvent event) {
        System.out.println(recPlayTimeString);
        System.out.println("New: "+event.getNewValue()+", Old: "+event.getOldValue());
     }
     public void txtPlayTimeChangeValue1() {
        System.out.println(recPlayTimeString);
     }
}
