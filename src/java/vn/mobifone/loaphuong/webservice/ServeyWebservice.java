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
import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.mobifone.loaphuong.security.EncryptionService;
import vn.mobifone.loaphuong.entity.HttpOutput;
import vn.mobifone.loaphuong.entity.WebserviceOutput;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.data.DataPreprocessor;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import vn.mobifone.loaphuong.entity.AreaNews;
import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.entity.ListDataResponse;
import vn.mobifone.loaphuong.entity.Category;
import vn.mobifone.loaphuong.entity.Servey;
import vn.mobifone.loaphuong.entity.SysLog;
import vn.mobifone.loaphuong.security.SecUser;
import static vn.mobifone.loaphuong.webservice.LoginWebservice.executeGET;

/**
 *
 * @author cuong.trinh
 */
public class ServeyWebservice extends DataPreprocessor{

    private static final String WS_LINK = SystemConfig.getConfig("CoreWebservice");    //"http://10.3.11.148:7003/LoaPhuongService/rest/cmsws/";

    SimpleDateFormat DateFormatter2 = new SimpleDateFormat("dd-MM-yyyy"); 
    SimpleDateFormat DateFormatter3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
    
    //private static final DateFormat sdf3 = new SimpleDateFormat("yyyy\/MM\/dd HH:mm:ss");

    private static final String TOKEN = decode(SystemConfig.getConfig("WSToken"));
    
    private List<Category> listNewsCategory;


    public ServeyWebservice() {

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
                    .header("username", "cmsuser")
                    .header("password", "cmspass!@#")
                    .get(ClientResponse.class);
            output.setDataSent("");

            if (response.getStatus() != 201 && response.getStatus() != 200) {
                output.setHeaderHttp(response.getStatus() + "");
                output.setMessageHttp("Lỗi khi kết nối, mã lỗi " + response.getStatus());
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Đã xảy ra lỗi");
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
    

    
    public List<Servey> getListServeyByArea(long areaId) {

            List<Servey> listServey = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            try {
                
                
                String WSlink = WS_LINK + "getListSurvey?area_id=" + areaId ;

                HttpOutput output = executeGET(WSlink);

                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);

                Gson gson = new Gson();
                for (Object object : response.getJavaResponse()) {
                    Servey news = (Servey) gson.fromJson(gson.toJson(object),Servey.class);
                    listServey.add(news);
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(ServeyWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return listServey;

    }
    
    
    
     public DataResponse executeNewsJson(AreaNews news, int act) {   //action = 1: insert . action = 2: update
//	@Path("/addNews")
//	public Response addNews(
//			@FormParam("title") String title,
//			@FormParam("short_description") String shortDescription,
//			@FormParam("full_description") String fullDescription,
//			@FormParam("url") String url,
//			@FormParam("image") String image,
//			@FormParam("from_date") String fromDate,
//			@FormParam("to_date") String toDate,
//			@FormParam("is_sticky") int isSticky,
//			@FormParam("publish_date") String publishDate,
//			@FormParam("category_id") int categoryId,
//			@FormParam("area_id") int areaId,
//			@FormParam("create_user") String createUser,
//			@FormParam("notify_flag") int notifyFlag) {

            DataResponse response = new DataResponse();
            try {
                JSONObject jObj = new JSONObject();
                jObj.put("title", news.getTitle());
                jObj.put("short_description", news.getShort_description());
                jObj.put("full_description", news.getFull_description());
                jObj.put("url", "" + news.getUrl());
                jObj.put("image", "" +news.getImage());
                jObj.put("from_date", "" + DateFormatter3.format(new Date()));
                jObj.put("to_date", "" + DateFormatter3.format(new Date()));
                jObj.put("is_sticky", news.getIs_sticky());
                jObj.put("publish_date", "" + news.getPublish_date());
                jObj.put("category_id", news.getCategory_id());
                jObj.put("area_id", news.getArea_id());
                jObj.put("create_user", SecUser.getUserLogged().getUserId());
                jObj.put("notify_flag", news.getNotify_flag());
                
                if (act == 2) {
                    jObj.put("news_id", news.getId());
                }
        

                String data = jObj.toString();
               // data = data.replaceAll("--", "/");
                //System.out.println("mobifone.marketplace.admin.controller.ProductController  data: " + data);
                SystemLogger.getLogger().debug(data);
                
                String successMess = "";

                String action = "";
                if (act == 1) {
                    action = "addNews";
                    successMess = "Thêm tin tức thành công!";
                } else if (act == 2) {
                    action = "editNews";
                    successMess = "Cập nhật tin tức thành công!";
                }
                
                
                HttpOutput output = executePOST(action, data);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
                if (response.getCode() == 200) {
                    ClientMessage.logSuccess(successMess);
                } else {
                    ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
                }
            } catch (IOException ex) {
                Logger.getLogger(ServeyWebservice.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
            Logger.getLogger(ServeyWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            return response;

    }
     
     
   
    // Phê duyệt tin tức + thông báo
    public DataResponse approveRecordNews(int type, long id) {   // type = 1:record, 2:news

            DataResponse response = new DataResponse();
            String queryParam;
            try {
                if (type == 1) {
                    queryParam = "approveRecord?rec_id=" + id;
                } else {
                    queryParam = "approveNews?news_id=" + id;
                }
                

                HttpOutput output = executeGET(WS_LINK + queryParam);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);

                if (response.getCode() == 200) {
                    ClientMessage.logSuccess( "Phê duyệt thành công!");
                } else {
                    ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
                }
            } catch (IOException ex) {
                Logger.getLogger(ServeyWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return response;
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
    
     public List<SysLog> getListSysLog(long area_id, Date from, Date to) {

            List<SysLog> listLog = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getActionLog?area_id=" + area_id
                                        + "&from_date=" + DateFormatter2.format(from)
                                        + "&to_date=" + DateFormatter2.format(to);
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                Gson gson = new Gson();
            

                for (Object object : response.getJavaResponse()) {
                    listLog.add((SysLog) gson.fromJson(gson.toJson(object),SysLog.class));
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return listLog;

    }

}
