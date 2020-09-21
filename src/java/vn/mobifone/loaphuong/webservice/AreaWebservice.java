package vn.mobifone.loaphuong.webservice;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import vn.mobifone.loaphuong.controller.AreaController;
import vn.mobifone.loaphuong.entity.AiCamera;
import vn.mobifone.loaphuong.entity.AiCameraMCU;
import vn.mobifone.loaphuong.entity.AiCameraSlot;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.Category;
import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.entity.HouseHold;
import vn.mobifone.loaphuong.entity.HttpOutput;
import vn.mobifone.loaphuong.entity.ListDataResponse;
import vn.mobifone.loaphuong.entity.MCU;
import vn.mobifone.loaphuong.entity.MCULog;
import vn.mobifone.loaphuong.entity.MCURecord;
import vn.mobifone.loaphuong.entity.MCUState;
import vn.mobifone.loaphuong.entity.RadioChannel;
import vn.mobifone.loaphuong.entity.WebserviceOutput;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.data.DataPreprocessor;
import vn.mobifone.loaphuong.security.EncryptionService;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.webservice.AreaWebservice;
import vn.mobifone.loaphuong.webservice.CallWebservice;
import vn.mobifone.loaphuong.webservice.RadioWebservice;

public class AreaWebservice extends DataPreprocessor {
  private static final String WS_LINK = SystemConfig.getConfig("CoreWebservice");
  
  SimpleDateFormat DateFormatter2 = new SimpleDateFormat("dd-MM-yyyy");
  
  SimpleDateFormat DateFormatter3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
  
  private static final String TOKEN = decode(SystemConfig.getConfig("WSToken"));
  
  private List<Category> listRecordcategory;
  
  public HttpOutput execute(String data) {
    HttpOutput output = new HttpOutput();
    try {
      Client client = Client.create();
      WebResource webResource = client.resource(WS_LINK);
      ClientResponse response = (ClientResponse)((WebResource.Builder)webResource.accept(new String[] { "application/json; charset=utf-8" }).type("application/json; charset=utf-8")).post(ClientResponse.class, data);
      output.setDataSent(data);
      if (response.getStatus() != 201 && response.getStatus() != 200) {
        output.setHeaderHttp(response.getStatus() + "");
        output.setMessageHttp("Lỗi khi kết nối, mã lỗi " + response.getStatus());
      } else {
        output.setHeaderHttp("200");
        output.setMessageHttp("Kết nối thành công");
        Gson gson = new Gson();
        String outputStr = (String)response.getEntity(String.class);
        SystemLogger.getLogger().debug(outputStr);
        output.setResponseJson(outputStr);
        WebserviceOutput wsOutput = (WebserviceOutput)gson.fromJson(outputStr, WebserviceOutput.class);
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
      WebResource webResource = client.resource(WS_LINK + actionLink);
      ClientResponse response = (ClientResponse)((WebResource.Builder)((WebResource.Builder)((WebResource.Builder)((WebResource.Builder)((WebResource.Builder)webResource.accept(new String[] { "application/json; charset=utf-8" }).type("application/json; charset=utf-8")).header("requester", SecUser.getUserLogged().getUserName())).header("area_id", Long.valueOf(SecUser.getUserLogged().getAreaId()))).header("username", "cmsuser")).header("password", "cmspass!@#")).post(ClientResponse.class, data);
      output.setDataSent(data);
      if (response.getStatus() != 201 && response.getStatus() != 200) {
        output.setHeaderHttp(response.getStatus() + "");
        output.setMessageHttp("Lỗi khi kết nối, mã lỗi " + response.getStatus());
        Gson gson = new Gson();
        String outputStr = (String)response.getEntity(String.class);
        SystemLogger.getLogger().debug(outputStr);
        output.setResponseJson(outputStr);
      } else {
        output.setHeaderHttp("200");
        output.setMessageHttp("Kết nối thành công");
        Gson gson = new Gson();
        String outputStr = (String)response.getEntity(String.class);
        SystemLogger.getLogger().debug(outputStr);
        output.setResponseJson(outputStr);
        WebserviceOutput wsOutput = (WebserviceOutput)gson.fromJson(outputStr, WebserviceOutput.class);
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
      WebResource webResource = client.resource(WS_LINK + actionLink);
      ClientResponse response = (ClientResponse)webResource.post(ClientResponse.class);
      if (response.getStatus() != 201 && response.getStatus() != 200) {
        output.setHeaderHttp(response.getStatus() + "");
        output.setMessageHttp("Lỗi khi kết nối, mã lỗi " + response.getStatus());
        Gson gson = new Gson();
        String outputStr = (String)response.getEntity(String.class);
        SystemLogger.getLogger().debug(outputStr);
        output.setResponseJson(outputStr);
      } else {
        output.setHeaderHttp("200");
        output.setMessageHttp("Kết nối thành công");
        Gson gson = new Gson();
        String outputStr = (String)response.getEntity(String.class);
        SystemLogger.getLogger().debug(outputStr);
        output.setResponseJson(outputStr);
        WebserviceOutput wsOutput = (WebserviceOutput)gson.fromJson(outputStr, WebserviceOutput.class);
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
      WebResource webResource = client.resource(WSlink);
      ClientResponse response = (ClientResponse)((WebResource.Builder)((WebResource.Builder)((WebResource.Builder)((WebResource.Builder)((WebResource.Builder)webResource.accept(new String[] { "application/json; charset=utf-8" }).type("application/json; charset=utf-8")).header("requester", SecUser.getUserLogged().getUserName())).header("area_id", Long.valueOf(SecUser.getUserLogged().getAreaId()))).header("username", "cmsuser")).header("password", "cmspass!@#")).get(ClientResponse.class);
      output.setDataSent("");
      if (response.getStatus() != 201 && response.getStatus() != 200) {
        output.setHeaderHttp(response.getStatus() + "");
        output.setMessageHttp("Lỗi khi kết nối, mã lỗi " + response.getStatus());
        String outputStr = (String)response.getEntity(String.class);
        SystemLogger.getLogger().debug(outputStr);
        output.setResponseJson(outputStr);
      } else {
        output.setHeaderHttp("200");
        output.setMessageHttp("Kết nối thành công");
        Gson gson = new Gson();
        String outputStr = (String)response.getEntity(String.class);
        SystemLogger.getLogger().debug(outputStr);
        output.setResponseJson(outputStr);
        WebserviceOutput wsOutput = (WebserviceOutput)gson.fromJson(outputStr, WebserviceOutput.class);
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
    for (byte b : bytes)
      result.append(Integer.toString((b & 0xFF) + 256, 16).substring(1)); 
    return result.toString();
  }
  
  public static String getSha256UTF8(String base) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(base.getBytes("UTF-8"));
      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xFF & hash[i]);
        if (hex.length() == 1)
          hexString.append('0'); 
        hexString.append(hex);
      } 
      return hexString.toString();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    } 
  }
  
  private static String decode(String string) {
    String strPassword = null;
    try {
      EncryptionService es = new EncryptionService();
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
      response = (ListDataResponse)objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
      Gson gson = new Gson();
      for (Object object : response.getJavaResponse())
        listHouse.add((HouseHold)gson.fromJson(gson.toJson(object), HouseHold.class)); 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return listHouse;
  }
  
  public List<AreaTree> getListArea(long areaId) {
    List<AreaTree> listArea = new ArrayList<>();
    ListDataResponse response = new ListDataResponse();
    try {
      String WSlink = WS_LINK + "getSubArea?area_id=" + areaId;
      HttpOutput output = executeGET(WSlink);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (ListDataResponse)objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
      Gson gson = new Gson();
      for (Object object : response.getJavaResponse())
        listArea.add((AreaTree)gson.fromJson(gson.toJson(object), AreaTree.class)); 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return listArea;
  }
  
  public DataResponse addArea(AreaTree area) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "addArea?code=" + URLEncoder.encode(area.getAreaCode(), "UTF-8") + "&status=" + area.getStatus() + "&name=" + URLEncoder.encode(area.getAreaName(), "UTF-8") + "&created_user=" + SecUser.getUserLogged().getUserId() + "&parent_id=" + area.getParentAreaId() + "&area_type_id=" + area.getAreaType();
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse addHeader(int type, long id, String fileUrl) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "updateHeader?header_id=9&receipt_type=" + type + "&receipt_id=" + id + "&audio_path=" + URLEncoder.encode(fileUrl, "UTF-8");
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
      if (response.getCode() == 200) {
        ClientMessage.logSuccess("Cập nhật file nhạc đầu bản tin thành công! ");
      } else {
        ClientMessage.logSuccess("Không thành công" + response.getCodeDescVn());
      } 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse updateArea(AreaTree area) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "editArea?area_id=" + area.getAreaId() + "&code=" + URLEncoder.encode(area.getAreaCode(), "UTF-8") + "&name=" + URLEncoder.encode(area.getAreaName(), "UTF-8");
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse deleteArea(long areaId) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "deleteArea?area_id=" + areaId;
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse updateHeader(long headerId, long receiptId, int receiptType, String audioPath) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "updateHeader?header_id=" + headerId + "&receipt_id=" + receiptId + "&receipt_type=" + receiptType + "&audio_path=" + URLEncoder.encode(audioPath, "UTF-8");
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse addHouseHold(HouseHold house) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "addHouseHold?holder_name=" + URLEncoder.encode(house.getHolder_name(), "UTF-8") + "&address=" + URLEncoder.encode(house.getAddress(), "UTF-8") + "&area_id=" + house.getArea_id() + "&area_path=" + house.getArea_path();
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
      if (response.getCode() == 200) {
        ClientMessage.logSuccess("Thêm hộ gia đình thành công! ");
      } else {
        ClientMessage.logErr("Không thành công" + response.getCodeDescVn());
      } 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse updateHouseHold(HouseHold house) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "updateHouseHold?holder_name=" + URLEncoder.encode(house.getHolder_name(), "UTF-8") + "&address=" + URLEncoder.encode(house.getAddress(), "UTF-8") + "&area_id=" + house.getArea_id() + "&area_path=" + house.getArea_path() + "&household_id=" + house.getId();
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
      if (response.getCode() == 200) {
        ClientMessage.logSuccess("Cập nhật hộ gia đình thành công! ");
      } else {
        ClientMessage.logErr("Không thành công" + response.getCodeDescVn());
      } 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse deleteHouseHold(long houseId) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "deleteHouseHold?household_id=" + houseId;
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
      if (response.getCode() == 200) {
        ClientMessage.logSuccess("Xóa hộ gia đình thành công! ");
      } else {
        ClientMessage.logErr("Không thành công" + response.getCodeDescVn());
      } 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public List<MCU> getListMCUByHouse(long houseId) {
    List<MCU> listMCU = new ArrayList<>();
    ListDataResponse response = new ListDataResponse();
    try {
      String WSlink = WS_LINK + "getMCUByHousehold?household_id=" + houseId;
      HttpOutput output = executeGET(WSlink);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (ListDataResponse)objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
      Gson gson = new Gson();
      for (Object object : response.getJavaResponse())
        listMCU.add((MCU)gson.fromJson(gson.toJson(object), MCU.class)); 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return listMCU;
  }
  
  public List<MCU> getListMCUByArea(long areaId) {
    List<MCU> listMCU = new ArrayList<>();
    ListDataResponse response = new ListDataResponse();
    try {
      String WSlink = WS_LINK + "getMCUByAreaId?area_id=" + areaId;
      HttpOutput output = executeGET(WSlink);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (ListDataResponse)objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
      Gson gson = new Gson();
      for (Object object : response.getJavaResponse()) {
        MCU mcu = (MCU)gson.fromJson(gson.toJson(object), MCU.class);
        String operator = SystemConfig.getConfig("operator");
        if (!SecUser.isSuperAdmin() && (
          operator == null || operator.isEmpty()))
          mcu.setMcu_connect_status(1); 
        listMCU.add(mcu);
      } 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return listMCU;
  }
  
  public DataResponse addMCU(MCU mcu) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "addMCUHousehold?vcode=" + URLEncoder.encode(mcu.getVcode(), "UTF-8") + "&mcu_id=" + mcu.getMcu_id() + "&household_id=" + mcu.getHousehold_id() + "&lat=" + mcu.getMcu_lat() + "&lng=" + mcu.getMcu_lng() + "&phone_number=" + mcu.getPhone_number() + "&serial_sim=" + mcu.getMcu_sim_serial();
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse addPublicMCU(MCU mcu) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "addMCUPublic?vcode=" + URLEncoder.encode(mcu.getVcode(), "UTF-8") + "&mcu_id=" + mcu.getMcu_id() + "&area_id=" + mcu.getMcu_area_id() + "&lat=" + mcu.getMcu_lat() + "&lng=" + mcu.getMcu_lng() + "&address=" + URLEncoder.encode("" + mcu.getAddress(), "UTF-8") + "&phone_number=" + mcu.getPhone_number() + "&serial_sim=" + mcu.getMcu_sim_serial() + "&mira_name=" + URLEncoder.encode(mcu.getMira_name(), "UTF-8");
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse updateMCU(MCU mcu) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "updateMCUInfo?mcu_id=" + mcu.getMcu_id() + "&address=" + URLEncoder.encode("" + mcu.getAddress(), "UTF-8") + "&lat=" + mcu.getMcu_lat() + "&lng=" + mcu.getMcu_lng() + "&volume=" + mcu.getVolume() + "&phone_number=" + mcu.getPhone_number() + "&serial_sim=" + mcu.getMcu_sim_serial() + "&mira_name=" + URLEncoder.encode("" + mcu.getMira_name(), "UTF-8");
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
      if (response.getCode() == 200)
        response = updateMCUVolumeFM(mcu.getMcu_id(), mcu.getMcu_fm_volume(), mcu.getFM_AutoBoolean()); 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse updateMCUVolumeFM(long mcuId, int fmVolume, boolean fmAuto) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "changeFMVolume?mcu_id=" + mcuId + "&volume=" + fmVolume + "&auto=" + (fmAuto ? 1 : 0);
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse deleteMCU(long mcuId) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "deleteMCU?mcu_id=" + mcuId;
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse rebootMCU(long mcuId) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "rebootMCU?mcu_id=" + mcuId;
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
      if (response.getCode() == 200) {
        ClientMessage.logSuccess("Khởi động lại mGateway thành công! ");
      } else {
        ClientMessage.logErr("Không thành công" + response.getCodeDescVn());
      } 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public DataResponse syncMCU(long mcuId) {
    DataResponse response = new DataResponse();
    try {
      String queryParam = "syncMCU?mcu_id=" + mcuId;
      HttpOutput output = executeGET(WS_LINK + queryParam);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
      if (response.getCode() == 200) {
        ClientMessage.logSuccess("Cập nhật lịch phát thành công!");
      } else {
        ClientMessage.logErr("Cập nhật lịch phát không thành công " + response.getCodeDescVn());
      } 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return response;
  }
  
  public List<RadioChannel> getListChannelByArea(long area_id, int mode) {
    List<RadioChannel> listChannel = new ArrayList<>();
    ListDataResponse response = new ListDataResponse();
    try {
      String WSlink = WS_LINK + "getRadioChannelByArea?area_id=" + area_id;
      HttpOutput output = executeGET(WSlink);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (ListDataResponse)objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
      Gson gson = new Gson();
      for (Object object : response.getJavaResponse()) {
        RadioChannel channel = (RadioChannel)gson.fromJson(gson.toJson(object), RadioChannel.class);
        if (mode == 2 && channel.getUrl() == null)
          continue; 
        if (mode == 3 && channel.getFrequency() == 0.0D)
          continue; 
        listChannel.add(channel);
      } 
    } catch (IOException ex) {
      Logger.getLogger(RadioWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return listChannel;
  }
  
  public List<RadioChannel> getListChannel() {
    List<RadioChannel> listChannel = new ArrayList<>();
    ListDataResponse response = new ListDataResponse();
    try {
      String WSlink = WS_LINK + "getAllRadioChannel?area_id=" + SecUser.getMainArea();
      HttpOutput output = executeGET(WSlink);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (ListDataResponse)objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
      Gson gson = new Gson();
      for (Object object : response.getJavaResponse()) {
        RadioChannel channel = (RadioChannel)gson.fromJson(gson.toJson(object), RadioChannel.class);
        listChannel.add(channel);
      } 
    } catch (IOException ex) {
      Logger.getLogger(RadioWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return listChannel;
  }
  
  public DataResponse updateRadioArea(long id, List<Long> channelID) {
    DataResponse response = new DataResponse();
    try {
      JSONObject jObj = new JSONObject();
      jObj.put("area_id", id);
      jObj.put("channels", channelID);
      String data = jObj.toString();
      HttpOutput output = executePOST("updateRadioArea", data);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
      if (response.getCode() == 200) {
        ClientMessage.logSuccess("Cấu hình kênh cho địa bàn thành công!");
      } else {
        ClientMessage.logErr("Không thành công" + response.getCodeDescVn());
      } 
    } catch (IOException ex) {
      Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (JSONException ex) {
      Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } 
    return response;
  }
  
  public MCUState getMCUStateTimeline(long mcuId, Date fromDate, Date toDate) {
    MCUState mcuState = new MCUState();
    List<RadioChannel> listChannel = new ArrayList<>();
    DataResponse response = new DataResponse();
    try {
      String WSlink = WS_LINK + "getMCUSchedule?mcu_id=" + mcuId + "&from_date=" + this.DateFormatter2.format(fromDate) + "&to_date=" + this.DateFormatter2.format(toDate);
      HttpOutput output = executeGET(WSlink);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (DataResponse)objectMapper.readValue(output.getResponseJson(), DataResponse.class);
      Gson gson = new Gson();
      mcuState = (MCUState)gson.fromJson(gson.toJson(response.getJavaResponse()), MCUState.class);
    } catch (IOException ex) {
      Logger.getLogger(RadioWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return mcuState;
  }
  
  public List<MCULog> getMCULogList(long mcuId, Date fromDate, Date toDate) {
    List<MCULog> listMCULog = new ArrayList<>();
    ListDataResponse response = new ListDataResponse();
    try {
      String WSlink = WS_LINK + "getRecordStat?mcu_id=" + mcuId + "&from_date=" + this.DateFormatter2.format(fromDate) + "&to_date=" + this.DateFormatter2.format(toDate);
      HttpOutput output = executeGET(WSlink);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (ListDataResponse)objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
      Gson gson = new Gson();
      for (Object object : response.getJavaResponse())
        listMCULog.add((MCULog)gson.fromJson(gson.toJson(object), MCULog.class)); 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return listMCULog;
  }
  
  public List<MCURecord> getMCURecordList(long logIndex) {
    List<MCURecord> listMCURecord = new ArrayList<>();
    ListDataResponse response = new ListDataResponse();
    try {
      String WSlink = WS_LINK + "getRecordStatDetail?log_index=" + logIndex;
      HttpOutput output = executeGET(WSlink);
      ObjectMapper objectMapper = new ObjectMapper();
      response = (ListDataResponse)objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);
      Gson gson = new Gson();
      for (Object object : response.getJavaResponse())
        listMCURecord.add((MCURecord)gson.fromJson(gson.toJson(object), MCURecord.class)); 
    } catch (IOException ex) {
      Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return listMCURecord;
  }
  
  
  
 ///////////////////////////////////////////////////////////////
    // Ai Camera
    public List<AiCamera> getAiCameraListByArea(long areaId) {

        List<AiCamera> listAiCameras = new ArrayList<>();
        ListDataResponse response = new ListDataResponse();

        try {
            String WSlink = WS_LINK + "getListAICamera?area_id=" + areaId;

            HttpOutput output = executeGET(WSlink);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);

            Gson gson = new Gson();

            for (Object object : response.getJavaResponse()) {
                listAiCameras.add((AiCamera) gson.fromJson(gson.toJson(object), AiCamera.class));
            }

            //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
        } catch (IOException ex) {
            Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listAiCameras;

    }

    public List<AiCameraMCU> getMCUByAICameraIdByCamId(String camId) {

        List<AiCameraMCU> listAiCameraMCUs = new ArrayList<>();
        ListDataResponse response = new ListDataResponse();

        try {
            String WSlink = WS_LINK + "getMCUByAICameraId?cam_id=" + camId;

            HttpOutput output = executeGET(WSlink);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);

            Gson gson = new Gson();

            for (Object object : response.getJavaResponse()) {
                listAiCameraMCUs.add((AiCameraMCU) gson.fromJson(gson.toJson(object), AiCameraMCU.class));
            }

            //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
        } catch (IOException ex) {
            Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listAiCameraMCUs;

    }

    public AiCamera getAICameraByCamId(String camId) {

        AiCamera aiCamera = new AiCamera();
        DataResponse response = new DataResponse();

        try {
            String WSlink = WS_LINK + "getAICamera?cam_id=" + camId;

            HttpOutput output = executeGET(WSlink);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);

            Gson gson = new Gson();

            aiCamera = gson.fromJson(output.getResponseJson(), AiCamera.class);

        } catch (IOException ex) {
            Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aiCamera;

    }

    public AiCamera getAICameraById(long id) {

        AiCamera aiCamera = new AiCamera();
        DataResponse response = new DataResponse();

        try {
            String WSlink = WS_LINK + "getAICameraById?id=" + id;

            HttpOutput output = executeGET(WSlink);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);

            Gson gson = new Gson();

            aiCamera = gson.fromJson(output.getResponseJson(), AiCamera.class);

        } catch (IOException ex) {
            Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aiCamera;

    }

    public DataResponse addAiCamera(AiCamera aiCamera) {

        DataResponse response = new DataResponse();

        try {
            String queryParam = "addAICamera?";

            queryParam += "cam_id=" + aiCamera.getCam_id()
                    + "&cam_name=" + URLEncoder.encode("" + aiCamera.getCam_name(), "UTF-8")
                    + "&came_pos=" + URLEncoder.encode("" + aiCamera.getCam_position(), "UTF-8")
                    + "&lat=" + aiCamera.getLat()
                    + "&lng=" + aiCamera.getLon()
                    + "&area_id=" + aiCamera.getAreaId()
                    + "&announce_type=" + aiCamera.getAnnounce_type()
                    + "&voice=" + aiCamera.getVoice();

            HttpOutput output = executeGET(WS_LINK + queryParam);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);

            //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
        } catch (IOException ex) {
            Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public DataResponse editAiCamera(AiCamera aiCamera) {

        DataResponse response = new DataResponse();

        try {
            String queryParam = "editAICamera?";

            queryParam += "id=" + aiCamera.getId()
                    + "&cam_id=" + aiCamera.getCam_id()
                    + "&name=" + URLEncoder.encode(aiCamera.getCam_name(), "UTF-8")
                    + "&pos=" + URLEncoder.encode(aiCamera.getCam_position(), "UTF-8")
                    + "&lat=" + aiCamera.getLat()
                    + "&lng=" + aiCamera.getLon()
                    + "&area_id=" + aiCamera.getAreaId()
                    + "&announce_type=" + aiCamera.getAnnounce_type()
                    + "&voice=" + aiCamera.getVoice();

            HttpOutput output = executeGET(WS_LINK + queryParam);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);

            //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
        } catch (IOException ex) {
            Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public DataResponse deleteAiCamera(String camId) {

        DataResponse response = new DataResponse();

        try {
            String queryParam = "deleteAICamera?cam_id=" + camId;

            HttpOutput output = executeGET(WS_LINK + queryParam);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);

        } catch (IOException ex) {
            Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public DataResponse setMCUForAICamera(String camId, List<AreaController.MCUForAICameraDirection> listMCUForAICameraDirection) {
        DataResponse response = new DataResponse();

        try {
            Gson gson = new Gson();
            JSONObject jObj = new JSONObject();
            jObj.put("cam_id", camId);
            jObj.put("mcu_direction", gson.toJson(listMCUForAICameraDirection));

            String data = jObj.toString();
            data = data.replace("\"[", "[");
            data = data.replace("]\"", "]");
            data = data.replace("\\\"", "\"");

            HttpOutput output = executePOST("setMCUForAICamera", data);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public List<AiCameraSlot> getTimeSlotByAICameraId(String camId) {

        List<AiCameraSlot> listAiCameraSlots = new ArrayList<>();
        ListDataResponse response = new ListDataResponse();

        try {
            String WSlink = WS_LINK + "getTimeSlotByAICameraId?cam_id=" + camId;

            HttpOutput output = executeGET(WSlink);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);

            Gson gson = new Gson();

            for (Object object : response.getJavaResponse()) {
                listAiCameraSlots.add((AiCameraSlot) gson.fromJson(gson.toJson(object), AiCameraSlot.class));
            }

            //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
        } catch (IOException ex) {
            Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listAiCameraSlots;

    }

    public DataResponse setTimeSlotForAICamera(String camId, List<AreaController.SlotForAiCamera> listSlotForAiCameras) {
        DataResponse response = new DataResponse();

        try {
            Gson gson = new Gson();
            JSONObject jObj = new JSONObject();
            jObj.put("cam_id", camId);
            jObj.put("time_slot", gson.toJson(listSlotForAiCameras));

            String data = jObj.toString();
            data = data.replace("\"[", "[");
            data = data.replace("]\"", "]");
            data = data.replace("\\\"", "\"");

            HttpOutput output = executePOST("setTimeSlotForAICamera", data);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(AreaWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

  

}
