/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.webservice;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.ws.rs.core.UriBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.Category;
import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.entity.HouseHold;
import vn.mobifone.loaphuong.entity.HttpOutput;
import vn.mobifone.loaphuong.entity.ListDataResponse;
import vn.mobifone.loaphuong.entity.MCU;
import vn.mobifone.loaphuong.entity.MCUHistory;
import vn.mobifone.loaphuong.entity.Record;
import vn.mobifone.loaphuong.entity.Text2SpeechResponse;
import vn.mobifone.loaphuong.entity.TimeTable;
import vn.mobifone.loaphuong.entity.Voice;
import vn.mobifone.loaphuong.entity.WebserviceOutput;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.data.DataPreprocessor;
import vn.mobifone.loaphuong.model.AreaModel;
import vn.mobifone.loaphuong.security.EncryptionService;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.util.MD5Encoder;
import static vn.mobifone.loaphuong.webservice.AreaWebservice.executeGET;

/**
 *
 * @author cuong.trinh
 */
public class CallWebservice extends DataPreprocessor{

    private static final String WS_LINK = SystemConfig.getConfig("CoreWebservice");    //"http://10.3.11.148:7003/LoaPhuongService/rest/cmsws/";

    SimpleDateFormat DateFormatter2 = new SimpleDateFormat("dd-MM-yyyy"); 
    SimpleDateFormat DateFormatter3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
    
    //private static final DateFormat sdf3 = new SimpleDateFormat("yyyy\/MM\/dd HH:mm:ss");

    private static final String TOKEN = decode(SystemConfig.getConfig("WSToken"));
    
    private List<Category> listRecordcategory;
    //private List<AreaTree> listArea;
    private static HttpURLConnection connection;


    public CallWebservice() {
        //this.WS_LINK = linkWS;
        connection = null;

        try {
            listRecordcategory = getListRecordCateogy(1);
            //listArea = (new AreaModel()).getListAreaByAreaId(SecUser.getUserLogged().getAreaId());
        } catch (Exception ex) {
            Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    }
    
    public void closeConnection(){
        connection.disconnect();
    }

    /**
     * Ex
     *
     * @param data
     */
    public HttpOutput execute(String data) {

        HttpOutput output = new HttpOutput();

        try {

            Client client = Client.create();
            WebResource webResource = client
                    .resource(WS_LINK);

            ClientResponse response = webResource.accept("application/json; charset=utf-8").type("application/json; charset=utf-8")
                    .post(ClientResponse.class, data);
            output.setDataSent(data);

            if (response.getStatus() != 201 && response.getStatus() != 200) {
                output.setHeaderHttp(response.getStatus() + "");
                output.setMessageHttp("Lỗi khi kết nối, mã lỗi " + response.getStatus());

            } else {
                output.setHeaderHttp("200");
                output.setMessageHttp("Kết nối thành công");
                //Goi webservice thanh cong -> check content service tra ve.
                Gson gson = new Gson();
                String outputStr = response.getEntity(String.class);
                SystemLogger.getLogger().debug(outputStr);

               // //System.out.println("mobifone.marketplace.admin.controller.CallWS  WS result: " + outputStr);

                output.setResponseJson(outputStr);
                WebserviceOutput wsOutput = gson.fromJson(outputStr, WebserviceOutput.class);
                output.setWsOutput(wsOutput);

            }

        } catch (Exception e) {
            SystemLogger.getLogger().error(e, e);
            output.setHeaderHttp("ERROR_TIMEOUT");
            output.setMessageHttp(e.getMessage());

        }

        return output;
    }
    
    public static HttpOutput executePOST(String actionLink, String data) {

        HttpOutput output = new HttpOutput();

        try {

            Client client = Client.create();
            WebResource webResource = client
                    .resource(WS_LINK + actionLink);
            ////System.out.println("mobifone.marketplace.admin.controller.CallWS  WS link: " + WS_LINK + actionLink);

            ClientResponse response = webResource.accept("application/json; charset=utf-8").type("application/json; charset=utf-8")
                    .header("requester", SecUser.getUserLogged().getUserName()).header("area_id", SecUser.getUserLogged().getAreaId())
                    .header("username", "cmsuser")
                    .header("password", "cmspass!@#")
                    .post(ClientResponse.class, data);
            output.setDataSent(data);

            if (response.getStatus() != 201 && response.getStatus() != 200) {
                output.setHeaderHttp(response.getStatus() + "");
                output.setMessageHttp("Lỗi khi kết nối, mã lỗi " + response.getStatus());
                
                Gson gson = new Gson();
                String outputStr = response.getEntity(String.class);
                SystemLogger.getLogger().debug(outputStr);

                ////System.out.println("mobifone.marketplace.admin.controller.CallWS  WS result: " + outputStr);

                output.setResponseJson(outputStr);

            } else {
                output.setHeaderHttp("200");
                output.setMessageHttp("Kết nối thành công");
                //Goi webservice thanh cong -> check content service tra ve.
                Gson gson = new Gson();
                String outputStr = response.getEntity(String.class);
                SystemLogger.getLogger().debug(outputStr);

                ////System.out.println("mobifone.marketplace.admin.controller.CallWS  WS result: " + outputStr);

                output.setResponseJson(outputStr);
                WebserviceOutput wsOutput = gson.fromJson(outputStr, WebserviceOutput.class);
                output.setWsOutput(wsOutput);

            }

        } catch (Exception e) {
            SystemLogger.getLogger().error(e, e);
            output.setHeaderHttp("ERROR_TIMEOUT");
            output.setMessageHttp(e.getMessage());

        }

        return output;
    }
     
    public static HttpOutput executePOSTForm(String actionLink) {

        HttpOutput output = new HttpOutput();

        try {

            Client client = Client.create();
            WebResource webResource = client
                    .resource(WS_LINK + actionLink);
            //System.out.println("mobifone.marketplace.admin.controller.CallWS  WS link: " + WS_LINK + actionLink);

            ClientResponse response = webResource.post(ClientResponse.class);

            if (response.getStatus() != 201 && response.getStatus() != 200) {
                output.setHeaderHttp(response.getStatus() + "");
                output.setMessageHttp("Lỗi khi kết nối, mã lỗi " + response.getStatus());
                
                Gson gson = new Gson();
                String outputStr = response.getEntity(String.class);
                SystemLogger.getLogger().debug(outputStr);

                //System.out.println("mobifone.marketplace.admin.controller.CallWS  WS result: " + outputStr);

                output.setResponseJson(outputStr);

            } else {
                output.setHeaderHttp("200");
                output.setMessageHttp("Kết nối thành công");
                //Goi webservice thanh cong -> check content service tra ve.
                Gson gson = new Gson();
                String outputStr = response.getEntity(String.class);
                SystemLogger.getLogger().debug(outputStr);

                //System.out.println("mobifone.marketplace.admin.controller.CallWS  WS result: " + outputStr);

                output.setResponseJson(outputStr);
                WebserviceOutput wsOutput = gson.fromJson(outputStr, WebserviceOutput.class);
                output.setWsOutput(wsOutput);

            }

        } catch (Exception e) {
            SystemLogger.getLogger().error(e, e);
            output.setHeaderHttp("ERROR_TIMEOUT");
            output.setMessageHttp(e.getMessage());

        }

        return output;
    }
    
    public static HttpOutput executeGET(String WSlink) {

        HttpOutput output = new HttpOutput();

        try {

            Client client = Client.create();
            WebResource webResource = client
                    .resource(WSlink);
            //System.out.println("mobifone.marketplace.admin.controller.CallWS  WS link: " + WSlink);

            ClientResponse response = webResource.accept("application/json; charset=utf-8").type("application/json; charset=utf-8")
                    .header("requester", SecUser.getUserLogged().getUserName()).header("area_id", SecUser.getUserLogged().getAreaId())
                    .header("username", "cmsuser")
                    .header("password", "cmspass!@#")
                    .get(ClientResponse.class);
            output.setDataSent("");

            if (response.getStatus() != 201 && response.getStatus() != 200) {
                output.setHeaderHttp(response.getStatus() + "");
                output.setMessageHttp("Lỗi khi kết nối, mã lỗi " + response.getStatus());
                //ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Đã xảy ra lỗi");
                String outputStr = response.getEntity(String.class);
                SystemLogger.getLogger().debug(outputStr);

                //System.out.println("mobifone.marketplace.admin.controller.CallWS  WS result: " + outputStr);

                output.setResponseJson(outputStr);

            } else {
                output.setHeaderHttp("200");
                output.setMessageHttp("Kết nối thành công");
                //Goi webservice thanh cong -> check content service tra ve.
                Gson gson = new Gson();
                String outputStr = response.getEntity(String.class);
                SystemLogger.getLogger().debug(outputStr);

                //System.out.println("mobifone.marketplace.admin.controller.CallWS  WS result: " + outputStr);

                output.setResponseJson(outputStr);
                WebserviceOutput wsOutput = gson.fromJson(outputStr, WebserviceOutput.class);
                output.setWsOutput(wsOutput);

            }

        } catch (Exception e) {
            SystemLogger.getLogger().error(e, e);
            output.setHeaderHttp("ERROR_TIMEOUT");
            output.setMessageHttp(e.getMessage());

        }

        return output;
    }

   
    
    

    public static String getSha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            return bytesToHex(md.digest());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
    
    public static String getSha256UTF8(String base) {
    try{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(base.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    } catch(Exception ex){
       throw new RuntimeException(ex);
    }
}
    
        //Gi?i m� m?t kh?u
    private static String decode(String string) {
        EncryptionService es;
        String strPassword = null;

        try {
            es = new EncryptionService();
            strPassword = es.decrypt(string);
            
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }

        return strPassword;
    }
    

    public List<HouseHold> getListHouseByArea(long areaId) {

            List<HouseHold> listHouse = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getHouseholdByArea?area_id=" + areaId;
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                Gson gson = new Gson();
            

                for (Object object : response.getJavaResponse()) {
                    listHouse.add((HouseHold) gson.fromJson(gson.toJson(object),HouseHold.class));
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return listHouse;

    }
    
    
    public List<HouseHold> getListHouseHasMCU(long areaId) {

            List<HouseHold> listHouse = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getHouseholdByArea?area_id=" + areaId
                                        + "&is_have_mcu_only=1";
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                Gson gson = new Gson();
                for (Object object : response.getJavaResponse()) {
                    listHouse.add((HouseHold) gson.fromJson(gson.toJson(object),HouseHold.class));
                }
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            return listHouse;
    }
    



    
    public List<Category> getListRecordCateogy(long areaId) {

            List<Category> listRecord = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getRecordCategory?area_id=" + areaId;
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                Gson gson = new Gson();
            

                for (Object object : response.getJavaResponse()) {
                    listRecord.add((Category) gson.fromJson(gson.toJson(object),Category.class));
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return listRecord;

    }
    
    public List<Voice> getListVoices() {

            List<Voice> listVoices = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getListVoices";
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                Gson gson = new Gson();
            

                for (Object object : response.getJavaResponse()) {
                    listVoices.add((Voice) gson.fromJson(gson.toJson(object),Voice.class));
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return listVoices;

    }
    
    public List<Record> getListRecordByArea(long areaId, boolean includeSub, Date from, Date to) {

            List<Record> listRecord = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getRecordByArea?area_id=" + areaId + "&is_include_sub=" + includeSub
                                        + "&from_date=" + DateFormatter2.format(from)
                                        + "&to_date=" + DateFormatter2.format(to);
                HttpOutput output = executeGET(WSlink);
                
                if (output.getHeaderHttp().compareTo("500")==0) {
                    ClientMessage.log("Tải dữ liệu không thành công: Hệ thống đang bận!");
                    return listRecord;
                }
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                Gson gson = new Gson();
                
                if (response.getCode() == 200) {
                    //ClientMessage.logSuccess("" + response.getJavaResponse());
                } else {
                    ClientMessage.logErr("Không tải được dữ liệu");
                }

                for (Object object : response.getJavaResponse()) {
                    Record record = (Record) gson.fromJson(gson.toJson(object),Record.class);
                    record.setCategoryName(getRecCategoryName(record.getCategory_id()));
                    
//                    String receptList = "";
//                    
//                    for (Long receptId : record.getRec_receipt()) {
//                        for (AreaTree area : listArea) {
//                            if (area.getAreaId() == receptId) {
//                                receptList += area.getAreaName() + ", " ;
//                            }
//                        }
//                    }
//                    
//                    record.setListPlayName(receptList);

                    listRecord.add(record);
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
                ClientMessage.logErr(""+ex);
            }
            
            return listRecord;

    }
    
    
    public Record getRecordById(long recId) {

            Record record = new Record();
            DataResponse response = new DataResponse();
            
            try {
                String WSlink = WS_LINK + "getRecordById?rec_id=" + recId ;
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
                
                
                
                Gson gson = new Gson();

                record = (Record) gson.fromJson(gson.toJson(response.getJavaResponse()),Record.class);
                    
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return record;

    }
    
    
    public List<Record> getRecordByDestArea(long areaId, Date from, Date to) {

            List<Record> listRecord = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getRecordByDestArea?area_id=" + areaId 
                                        + "&from_date=" + DateFormatter2.format(from)
                                        + "&to_date=" + DateFormatter2.format(to)
                                        + "&status=1";
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                Gson gson = new Gson();
            

                for (Object object : response.getJavaResponse()) {
                    Record record = (Record) gson.fromJson(gson.toJson(object),Record.class);
                    record.setCategoryName(getRecCategoryName(record.getCategory_id()));
                    


                    listRecord.add(record);
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return listRecord;

    }
    
    
    public List<Record> getTSRecordPlayingByArea(List<Long> areaList) {

            List<Record> listRecord = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            String api  = "";
            if (areaList.size() > 1) {
                api = "getPlayingLiveRecordByListArea";
            } else {
                api = "getPlayingLiveRecordByArea";
            }
            
            String area = areaList.toString().substring(1, areaList.toString().length()-1);
            area = area.replaceAll("\\s","");
                    
            try {
                String WSlink = WS_LINK + api + "?area_id=" + area ;
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                Gson gson = new Gson();
            

                for (Object object : response.getJavaResponse()) {
                    Record record = (Record) gson.fromJson(gson.toJson(object),Record.class);
                    record.setCategoryName(getRecCategoryName(record.getCategory_id()));

                    listRecord.add(record);
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return listRecord;

    }
    
     public ListDataResponse executeRecord(Record rec, int act, int force_override) {   //action = 1: insert . action = 2: update , 3: update affter approve


         ListDataResponse response = new ListDataResponse();
            
//           { "rec_id": 0, 
//           "rec_summary": "1", 
//           "rec_audio": "1", 
//           "rec_audio_codec": 0,
//           "rec_play_mode": 0, 
//           "rec_priority": 0, 
//           "rec_play_time": [ 0 ], 
//           "rec_play_repeat_type": 0, 
//           "rec_play_repeat_days": 0, 
//           "rec_play_expire": "2017-07-31T06:38:45.867Z", 
//           "rec_receipt_type": 0, 
//           "rec_receipt_ids": [ 0 ], 
//           "category_id":1, 
//           "create_user":"1", 
//           "rec_type":1, 
//           "notify_flag":1 }

            
            try {
                JSONObject jObj = new JSONObject();
                jObj.put("rec_id", rec.getRec_id());

                jObj.put("rec_summary", rec.getRec_summary());
                jObj.put("rec_audio", ""+rec.getRec_url());
                jObj.put("rec_audio_codec", 1);  // defalt = 1 
                jObj.put("rec_play_mode", rec.getRec_play_mode()==3?1:rec.getRec_play_mode());
                jObj.put("rec_priority", rec.getRec_priority());
                jObj.put("rec_play_time", rec.getRec_play_time());
                jObj.put("rec_play_repeat_type", rec.getRec_play_repeat_type());
                jObj.put("rec_play_repeat_days", rec.getRec_play_repeat_days());
                jObj.put("rec_play_start", rec.getRec_play_start());
                jObj.put("rec_play_expire", rec.getRec_play_expire_date());
                jObj.put("rec_receipt_type", rec.getRec_receipt_type());
                jObj.put("rec_receipt_ids", rec.getRec_receipt());
                jObj.put("category_id", rec.getCategory_id());
                jObj.put("create_user", SecUser.getUserLogged().getUserId());
                jObj.put("rec_type", rec.getRec_type());
                jObj.put("notify_flag", rec.getNotify_flag());
                jObj.put("area_id", rec.getAreaId());
                jObj.put("duration", rec.getDuration());
                jObj.put("force_override", force_override);
                //08_Apr_2020
                if (rec.getIsNext()){
                        jObj.put("is_link", 1);
                        jObj.put("rec_link", rec.getRec_link());
                }
                if (rec.getRec_type() == 2) {
                    jObj.put("rec_end_time", rec.getRec_end_time());
                }
                
                
        

                String data = jObj.toString();
               // data = data.replaceAll("--", "/");
                //System.out.println("mobifone.marketplace.admin.controller.ProductController  data: " + data);
                SystemLogger.getLogger().debug(data);

                String action = "";
                if (act == 1) {
                    action = "addRecord";
                } else if (act == 2) {
                    action = "updateRecord";
                } else if (act == 3) {
                    action = "updateRecordAfterApproved";
                }
                
                
                HttpOutput output = executePOST(action, data);
                
                if (output.getHeaderHttp().compareTo("500")==0) {
                    ClientMessage.logErr( "Không thành công: Hệ thống đang xảy ra lỗi!");
                }
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                if (response.getCode() == 200) {
                    ClientMessage.logSuccess( act==1?"Thêm thông báo thành công!":"Cập nhật thông báo thành công!" );
                } else if (response.getCode() != 606 && response.getCode() != 607) {
                    ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
                }
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main() success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
            Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            return response;

    }
     
    // Phê duyệt tin tức + thông báo
    public ListDataResponse approveRecordNews(int type, long id) {   // type = 1:record, 2:news

            ListDataResponse response = new ListDataResponse();
            String queryParam;
            try {
                if (type == 1) {
                    queryParam = "approveRecord?rec_id=" + id
                                    +"&username=" +  URLEncoder.encode(SecUser.getUserLogged().getUserName(), "UTF-8");
                } else {
                    queryParam = "approveNews?news_id=" + id;
                }
                

                HttpOutput output = executeGET(WS_LINK + queryParam);
                
                if (output.getHeaderHttp().compareTo("500")==0) {
                    ClientMessage.logErr( "Không thành công: Hệ thống đang xảy ra lỗi!");
                }
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);

                if (response.getCode() == 200) {
                    ClientMessage.logSuccess( "Phê duyệt thành công!");
                } else {
                    ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
                }
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return response;
    }
    

     // Xóa, Hủy Thông báo
    public ListDataResponse deactiveRecord(int status, long id) {   // status = 1: after approve , 2: before approve.

            ListDataResponse response = new ListDataResponse();
            String queryParam;
            
            try {
                if (status == 1) {
                    queryParam ="deactiveRecordAfterApprove?rec_id=" + id ;
                } else {
                    queryParam ="deactiveRecordBeforeApprove?rec_id=" + id ;
                }
                HttpOutput output = executeGET(WS_LINK + queryParam);
                
                if (output.getHeaderHttp().compareTo("500")==0) {
                    ClientMessage.logErr( "Không thành công: Hệ thống đang xảy ra lỗi!");
                }
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);

                if (response.getCode() == 200) {
                    ClientMessage.logSuccess( status == 2 ? "Xóa thông báo thành công!" : "Hủy thông báo thành công!");
                } else {
                    ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
                }
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return response;
    }
    
    
    // Get category name for Record
    public String getRecCategoryName(long category_id){
        for (Category recordCategory : listRecordcategory) {
            if (recordCategory.getId() == category_id) {
                return recordCategory.getName();
            }
        }
        return "";
    }
    
    public List<MCUHistory> getListMCUByListArea(List<Long> listAreaId) {

            List<MCUHistory> listMCU = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            for (Long areaId : listAreaId) {
                try {
                    String WSlink = WS_LINK + "getMCUByAreaId?area_id=" + areaId;
                    HttpOutput output = executeGET(WSlink);

                    ObjectMapper objectMapper = new ObjectMapper();
                    response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);

                    Gson gson = new Gson();


                    for (Object object : response.getJavaResponse()) {
                        listMCU.add((MCUHistory) gson.fromJson(gson.toJson(object),MCUHistory.class));
                    }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
                } catch (IOException ex) {
                    Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            return listMCU;

    }
//    public static void main(String[] args){
//        CallWebservice ws = new CallWebservice(("11"));
//        HttpOutput output = ws.executeGET("http://10.4.200.52:7001/LoaPhuongService/rest/cmsws/login?username=cuong&password=827ccb0eea8a706c4c34a16891f84e7b");
//        
//        
//        ObjectMapper objectMapper = new ObjectMapper();
//        ListDataResponse loginEntity = new ListDataResponse();
//        try {
//            loginEntity = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
//            
//            
//            
//            Gson gson = new Gson();
//            
//            List<UserLogin> data = new ArrayList<>();
//            
//            for (Object object : loginEntity.getJavaResponse()) {
//                data.add((UserLogin) gson.fromJson(gson.toJson(loginEntity),UserLogin.class));
//            }
//            
//            
//            //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
//        } catch (IOException ex) {
//            Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//
//       // executeLogin("cuong", "cuongxiu123");
//        
//        
//        
//    }
//    public static void main(String[] args){
//        try {
//            CallWebservice ws = new CallWebservice();
//            
//            int test = 0; 
//            
//            test = ws.getNumberMCUPlay(1935, DateFormatter3.parse("22-01-2018 10:18:00"), DateFormatter3.parse("22-01-2018 10:19:00"));
//        } catch (ParseException ex) {
//            Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    public int getNumberMCUPlay(long recId, Date from, Date to) {
            int result = 0;
            DataResponse response = new DataResponse();
            
            try {
                String WSlink = WS_LINK + "getRecordPlayAmount?rec_id=" + recId 
                                        + "&start_time=" + URLEncoder.encode(DateFormatter3.format(from), "UTF-8")
                                        + "&end_time=" + URLEncoder.encode(DateFormatter3.format(to), "UTF-8");
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);

                LinkedHashMap lk = (LinkedHashMap) response.getJavaResponse();
                result = (int) lk.get("amount");

                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return result;

    }
    
    public List<Long> getListMCUPlay(long recId, Date from, Date to) {
            List<Long> result = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getRecordPlayHistory?rec_id=" + recId 
                                        + "&start_time=" + URLEncoder.encode(DateFormatter3.format(from), "UTF-8")
                                        + "&end_time=" + URLEncoder.encode(DateFormatter3.format(to), "UTF-8");
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                for (Object object : response.getJavaResponse()) {
                    LinkedHashMap lk = (LinkedHashMap) object;
                    result.add(Long.parseLong(lk.get("mcu_id").toString()));
                }
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return result;

    }
    
       public static Text2SpeechResponse convertText2Speech1(String data, int voice, int speed) throws Exception{

        Text2SpeechResponse t2tResponse = new Text2SpeechResponse();

            Client client = Client.create();
            //WebResource webResource = client.resource("https://api.openfpt.vn/text2speech/v4");
            
            WebResource webResource = client.resource(UriBuilder.fromUri("http://api.openfpt.vn/text2speech/v4").build()); 
            ////System.out.println("mobifone.marketplace.admin.controller.CallWS  WS link: " + WS_LINK + actionLink);
            
            String btv;
            switch (voice){
                case 0:
                    btv = "female";
                    break;
                case 1:
                    btv = "male";
                    break;
                case 2:
                    btv = "ngoclam ";
                    break;
                case 3:
                    btv = "hatieumai";
                    break;
                default:
                    btv = "female";
            } 
                        

            ClientResponse response = webResource.accept("application/json; charset=utf-8")
                    .header("voice", btv)
                    .header("speed", speed - 4)
                    .header("api_key", "da66915a28934527917986c19f62d6bc")
                    .post(ClientResponse.class, data);

            if (response.getStatus() != 201 && response.getStatus() != 200) {
                ////System.out.println("mobifone.marketplace.admin.controller.CallWS  WS result: " + outputStr);
                ClientMessage.logErr("Không thành công: " + "Đã xảy ra lỗi trong quá trình xử lý! Vui lòng thử lại");
            } else {

                //Goi webservice thanh cong -> check content service tra ve.
                Gson gson = new Gson();
                String outputStr = response.getEntity(String.class);
                SystemLogger.getLogger().debug(outputStr);

                t2tResponse = gson.fromJson(outputStr, Text2SpeechResponse.class);

            }



        return t2tResponse;
    }

    
    public static Text2SpeechResponse convertText2Speech(String data, int voice, int speed) throws Exception{
        Text2SpeechResponse t2tResponse = new Text2SpeechResponse();
        String btv;
            switch (voice){
                case 0:
                    btv = "female";
                    break;
                case 1:
                    btv = "male";
                    break;
                case 2:
                    btv = "ngoclam ";
                    break;
                case 3:
                    btv = "hatieumai";
                    break;
                case 4:
                    btv = "banmai";
                    break;
                default:
                    btv = "female";
            } 
        Map<String,Object> paramNameToValue = new LinkedHashMap<>();
		String URL_BASE = "http://api.openfpt.vn";
                //String URL_BASE =  "https://dev.openfpt.vn";
		String method = "POST";
		URL url = new URL(URL_BASE + "/text2speech/v4");
                //URL url = new URL(URL_BASE + "/speechService");
                //URL url = new URL(null, URL_BASE  + "/text2speech/v4",new sun.net.www.protocol.https.Handler());
		// open HTTPS connection
		HttpURLConnection connection = null;
		connection = (HttpURLConnection)url.openConnection();
		//((HttpsURLConnection) connection).setHostnameVerifier(new MyHostnameVerifier());
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestMethod(method);
		connection.setRequestProperty("voice", btv);
		connection.setRequestProperty("speed", "" + (speed - 4));
		connection.setRequestProperty("api_key", "da66915a28934527917986c19f62d6bc");
		
		StringBuilder postData = new StringBuilder();
                postData.append( data);
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
	    connection.setDoOutput(true);
	    connection.getOutputStream().write(postDataBytes);
        int status = connection.getResponseCode();

        switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
                
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                System.out.println(sb.toString());
                Gson gson = new Gson();
                t2tResponse = gson.fromJson(sb.toString(), Text2SpeechResponse.class);
        }
	connection.disconnect();
        return t2tResponse;
    }
    
    
    
    public static InputStream convertText2SpeechMobi(String data, int voice, int speed) throws Exception{
        Text2SpeechResponse t2tResponse = new Text2SpeechResponse();
        String btv;
            switch (voice){
                case 0:
                    btv = "hn_male_xuantin_vdts_48k-hsmm";
                    break;
                case 1:
                    btv = "hn_female_xuanthu_news_48k-hsmm";
                    break;
                case 2:
                    btv = "hn_female_thutrang_phrase_48k-hsmm";
                    break;
                case 3:
                    btv = "sg_male_xuankien_vdts_48k-hsmm";
                    break;
                case 4:
                    btv = "sg_female_xuanhong_vdts_48k-hsmm";
                    break;
                default:
                    btv = "hn_male_xuantin_vdts_48k-hsmm";
            } 
            
        float sp  = speed;
        sp = 1 + sp/10;
        Map<String,Object> paramNameToValue = new LinkedHashMap<>();
        
                InputStream is = null;
		String URL_BASE = "https://tts.mobifone.ai/api/tts";
		
                
                
                String WSlink = URL_BASE + "?app_id=" + "5b50477fa7c7ec0988a5945e"
                                        + "&key=" + "4b30ad9284fc9876a8885b223c947df3"
                                        + "&user_id=" + "46070"
                                        + "&voice=" + btv
                                        + "&rate=" + sp
                                       // + "&service_type=" + "mp3" 
                                        + "&service_type=" + "1"
                                        + "&input_text=" + URLEncoder.encode(data, "UTF-8");
		
                
                Client client = Client.create();
                WebResource webResource = client
                        .resource(WSlink);

                ClientResponse response = webResource.accept("application/json; charset=utf-8").type("application/json; charset=utf-8")
                    .post(ClientResponse.class, data);
                
                if (response.getStatus() != 201 && response.getStatus() != 200) {
                  

                } else {
                  
                    //is = response.getEntityInputStream();
                    is = response.getEntity(InputStream.class);
                    String erer = "errr";
                    
                }


        return is;
    }
    public static InputStream convertT2TMobiPost(String data, int voice, int speed,String[] voiceNames) throws Exception{
        Text2SpeechResponse t2tResponse = new Text2SpeechResponse();
        String btv = voiceNames[voice];
        /*
            switch (voice){
                case 0:
                    //btv = "hn_male_xuantin_vdts_48k-hsmm";
                    btv = btvs[0];
                    break;
                case 1:
                    //btv = "hn_female_xuanthu_news_48k-hsmm";
                    //btv = "hn_female_thutrang_phrase_48k-hsmm";
                    btv = btvs[1];
                    break;
                //case 2:
                //    btv = "hn_female_thutrang_phrase_48k-hsmm";
                //    break;
                //case 3:
                case 2:
                    //btv = "sg_male_xuankien_vdts_48k-hsmm";
                    //btv = "sg_male_minhhoang_dial_48k-hsmm";
                    btv = btvs[2];
                    break;
                //case 4:
                case 3:
                    //btv = "sg_female_xuanhong_vdts_48k-hsmm";
                    //btv = "sg_female_thaotrinh_dialog_48k-hsmm";
                    btv = btvs[3];
                    break;
                default:
                    //btv = "hn_male_xuantin_vdts_48k-hsmm";
                    btv = btvs[0];
            } 
         */
        float sp  = speed;
        sp = 1 + sp/10;
        Map<String,Object> paramNameToValue = new LinkedHashMap<>();
        
                InputStream is = null;
		String URL_BASE = SystemConfig.getConfig("TTS_MOBIFONE");
		String APP_ID = SystemConfig.getConfig("app_id");
                String USER_ID = SystemConfig.getConfig("user_id");
		String PRIVATE_KEY = SystemConfig.getConfig("private_key");
                String timestamp = System.currentTimeMillis()+"";

                JSONObject jObj = new JSONObject();
                jObj.put("user_id", USER_ID);
                jObj.put("app_id", APP_ID);
                jObj.put("key",(new MD5Encoder()).encrypt(PRIVATE_KEY +":"+ APP_ID +":"+ timestamp));
                jObj.put("voice", btv);
                jObj.put("rate", sp);  // defalt = 1 
                jObj.put("time", timestamp);
                jObj.put("audio_type", "mp3");
                jObj.put("input_text", data);
     
                
        

                String jsonData = jObj.toString();
                
                String WSlink = URL_BASE ;
		
                
                Client client = Client.create();
                WebResource webResource = client
                        .resource(UriBuilder.fromUri(WSlink).build());

                ClientResponse response = webResource.accept("application/json; charset=utf-8").type("application/json; charset=utf-8")
                    .post(ClientResponse.class, jsonData);
                
                
                
                if (response.getStatus() != 201 && response.getStatus() != 200) {
                  

                } else {
                  
                    //is = response.getEntityInputStream();
                    is = response.getEntity(InputStream.class);
                    String erer = "errr";
                }


        return is;
    }
    
    public static InputStream convertT2TMobiPost2(String data, int voice, int speed) throws Exception{
        String btv;
            switch (voice){
                case 0:
                    btv = "hn_male_xuantin_vdts_48k-hsmm";
                    break;
                case 1:
                    btv = "hn_female_xuanthu_news_48k-hsmm";
                    break;
                case 2:
                    btv = "hn_female_thutrang_phrase_48k-hsmm";
                    break;
                case 3:
                    btv = "sg_male_xuankien_vdts_48k-hsmm";
                    break;
                case 4:
                    btv = "sg_female_xuanhong_vdts_48k-hsmm";
                    break;
                default:
                    btv = "hn_male_xuantin_vdts_48k-hsmm";
            } 
            
        float sp  = speed;
        sp = 1 + sp/10;
        Map<String,Object> paramNameToValue = new LinkedHashMap<>();
        
                InputStream is = null;
		String URL_BASE = SystemConfig.getConfig("TTS_MOBIFONE");
		String APP_ID = SystemConfig.getConfig("app_id");
                String USER_ID = SystemConfig.getConfig("user_id");
		String PRIVATE_KEY = SystemConfig.getConfig("private_key");
                String timestamp = System.currentTimeMillis()+"";

                JSONObject jObj = new JSONObject();
                jObj.put("user_id", USER_ID);
                jObj.put("app_id", APP_ID);
                jObj.put("key",(new MD5Encoder()).encrypt(PRIVATE_KEY +":"+ APP_ID +":"+ timestamp));
                jObj.put("voice", btv);
                jObj.put("rate", sp);  // defalt = 1 
                jObj.put("time", timestamp);
                jObj.put("audio_type", "mp3");
                jObj.put("input_text", data);

                String jsonData = jObj.toString();

		String method = "POST";
		URL url = new URL(URL_BASE);
      
		connection = (HttpURLConnection)url.openConnection();
		//((HttpsURLConnection) connection).setHostnameVerifier(new MyHostnameVerifier());
		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestMethod(method);
		
		StringBuilder postData = new StringBuilder();
                postData.append(jsonData);
                
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
	    connection.setDoOutput(true);
	    connection.getOutputStream().write(postDataBytes);
        int status = connection.getResponseCode();
        switch (status) {
            case 200:
            case 201:
                is =  connection.getInputStream();     
        }
	//connection.disconnect();

        return is;
    }
    
    public static class MyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}


