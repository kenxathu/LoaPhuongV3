/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import vn.mobifone.loaphuong.action.Action;
import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.entity.ListDataResponse;
import vn.mobifone.loaphuong.entity.DashBoard;
import vn.mobifone.loaphuong.entity.TimeTable;
import vn.mobifone.loaphuong.role.Role;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.user.User;
import vn.mobifone.loaphuong.util.MD5Encoder;
import static vn.mobifone.loaphuong.webservice.CallWebservice.executeGET;

/**
 *
 * @author cuong.trinh
 */
public class LoginWebservice extends DataPreprocessor{

    private static final String WS_LINK = SystemConfig.getConfig("CoreWebservice"); 

    SimpleDateFormat DateFormatter2 = new SimpleDateFormat("dd-MM-yyyy"); 
    SimpleDateFormat DateFormatter3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
    
    //private static final DateFormat sdf3 = new SimpleDateFormat("yyyy\/MM\/dd HH:mm:ss");

    private static final String TOKEN = decode(SystemConfig.getConfig("WSToken"));
    

    public LoginWebservice() {

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
    
    public DataResponse executeLogin(String userName, String password) {

             MD5Encoder MD5 = new MD5Encoder();
            
            
            DataResponse loginEntity = new DataResponse();
            
            try {
                String WSlink = WS_LINK + "login?username=" + userName + "&password=" + MD5.encrypt(password);
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                loginEntity = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return loginEntity;

    }
    
     public DashBoard getListDashBoardByArea(long areaId) {

            DashBoard dashBoard = new DashBoard();
            
            try {
                String WSlink = WS_LINK + "getDashboardData?area_id=" + areaId;
                HttpOutput output = executeGET(WSlink);
                DataResponse data = new DataResponse();
                
                ObjectMapper objectMapper = new ObjectMapper();
                
                data = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
                
                Gson gson = new Gson();
                          
                dashBoard = (DashBoard) gson.fromJson(gson.toJson(data.getJavaResponse()),DashBoard.class);
               
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return dashBoard;

    }
     
    public List<Action> getListAction() {

            List<Action> listAction = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getListAction" ;
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                Gson gson = new Gson();
            

                for (Object object : response.getJavaResponse()) {
                    listAction.add((Action) gson.fromJson(gson.toJson(object),Action.class));
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            return listAction;
    }
    
    public List<Action> getListActionByRoleId(long roleId) {

            List<Action> listAction = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getListActionByRoleId?role_id=" + roleId ;
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                Gson gson = new Gson();
            

                for (Object object : response.getJavaResponse()) {
                    listAction.add((Action) gson.fromJson(gson.toJson(object),Action.class));
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            return listAction;
    }
    
    public List<Role> getListRole() {

            List<Role> listRole = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getListRole" ;
                HttpOutput output = executeGET(WSlink);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                
                Gson gson = new Gson();
            

                for (Object object : response.getJavaResponse()) {
                    listRole.add((Role) gson.fromJson(gson.toJson(object),Role.class));
                }
            
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            return listRole;
    }
    
    
     public DataResponse updatePassword(long userId, String oldPass, String newPass, String cardId) {

            DataResponse response = new DataResponse();
            MD5Encoder MD5 = new MD5Encoder();
            try {
                String queryParam = "changePassword?user_id=" + userId
                                  + "&old_password=" + MD5.encrypt(oldPass)
                                  + "&new_password=" + MD5.encrypt(newPass)
                                  + "&card_id=" + cardId;
                
                HttpOutput output = executeGET(WS_LINK + queryParam);
                
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
//                if (response.getCode() == 200) {
//                    ClientMessage.logSuccess( "Thêm MCU thành công!");
//                } else {
//                    ClientMessage.logSuccess( "Không thành công: "+ response.getCodeDescVn());
//                }
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
            return response;
    }
     
    private Gson gson;
    final GsonBuilder builder = new GsonBuilder(); 
    public DataResponse editUser(User user) {
        DataResponse response = new DataResponse();
        try {
            
            gson = builder.create();
            String data = gson.toJson(user);
            HttpOutput output = CallWebservice.executePOST("updateUser", data);
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
            
        } catch (Exception ex) {
            ClientMessage.logErr("Không thành công: " + ex);
        }
        return response;
    }
    
        public List<TimeTable> getTimeTableByArea(long areaId, int areaType) {

            List<TimeTable> listTimeTable = new ArrayList<>();
            ListDataResponse response = new ListDataResponse();
            
            try {
                String WSlink = WS_LINK + "getRecordTimeRangeByArea?area_id=" + areaId 
                                        + "&area_type=" + areaType;
                
                HttpOutput output = executeGET(WSlink);
                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
                Gson gson = new Gson();
                for (Object object : response.getJavaResponse()) {
                    TimeTable tb = (TimeTable) gson.fromJson(gson.toJson(object),TimeTable.class);
                    listTimeTable.add(tb);
                }
                //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
            } catch (IOException ex) {
                Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
            }
            return listTimeTable;

    }
}
