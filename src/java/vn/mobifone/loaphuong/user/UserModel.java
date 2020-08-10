/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.entity.HouseHold;
import vn.mobifone.loaphuong.entity.HttpOutput;
import vn.mobifone.loaphuong.entity.ListDataResponse;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.data.DataPreprocessor;
import vn.mobifone.loaphuong.util.MD5Encoder;
import vn.mobifone.loaphuong.webservice.CallWebservice;



/**
 * Class UserModel Date 28/08/2015 09??
 *
 * @author ChienDX
 */
//Xử lý dữ liệu danh mục quản lý NSD
public class UserModel extends DataPreprocessor {
    private static String WS_LINK = SystemConfig.getConfig("CoreWebservice");   

    //Lấy thông tin user từ username
//    public User getInfByUserName(String userName) throws Exception {
//        User returnUser = null;
////
//        try {
//            open();
//            String strSQL = "SELECT a.id, a.username, a.password, a.name, a.type, a.id_card, a.phone,\n"
//                    + "       a.email, a.area_id, a.household_id, b.role_id \n"
//                    + "  FROM users a, user_role b"
//                    + "  where upper(a.username) = upper(?) and a.id = b.user_id ";
//            mStmt = mConnection.prepareStatement(strSQL);
//            mStmt.setString(1, userName);
//            mRs = mStmt.executeQuery();
////
//            if (mRs.next()) {
//                returnUser = new User();
//                returnUser.setUserId(mRs.getLong("id"));
//                returnUser.setUserName(mRs.getString("username"));
//                returnUser.setPassword(mRs.getString("password"));
//                returnUser.setName(mRs.getString("name"));
//                returnUser.setType(mRs.getInt("type"));
//                returnUser.setIdCard(mRs.getString("id_card"));
//                returnUser.setPhone(mRs.getString("phone"));
//                returnUser.setHouseHouseHoldId(mRs.getLong("household_id"));
////                returnUser.setRoleId(mRs.getLong("role_id"));
//                returnUser.setAreaId(mRs.getLong("area_id"));
//            }
//
//        } finally {
//            close(mConnection, mStmt, mRs);
//        }
//
//        return returnUser;
//    }

    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    /**
     * Lấy danh sách user
     *
     * @param type
     * @param areaId
     * @return List - User
     * @author Telsoft
     * @throws java.lang.Exception
     */
    ////////////////////////////////////////////////////////
    

    public List<User> getAllUserInSystem(int type, long areaId) throws Exception {
        List<User> returnValue = new ArrayList<>();
        ListDataResponse response = new ListDataResponse();
        try {
            HttpOutput out = CallWebservice.executeGET(WS_LINK + "getUserByArea?area_id=" + areaId + "&type=" + type);
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(out.getResponseJson(), ListDataResponse.class);

            gson = new Gson();
            for (Object object : response.getJavaResponse()) {
                returnValue.add((User) gson.fromJson(gson.toJson(object), User.class));
            }

        } catch (Exception e) {
        }
        return returnValue;
    }
    ////////////////////////////////////////////////////////

    public User getUserDetail(long userId) throws Exception {
        User returnValue = new User();
        DataResponse response = new DataResponse();
        try {
            HttpOutput out = CallWebservice.executeGET(WS_LINK+"getUserDetail?user_id=" + userId);
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(out.getResponseJson(), DataResponse.class);

             gson = new Gson();
             returnValue = (User) gson.fromJson(gson.toJson(response.getJavaResponse()), User.class);
             

        } catch (Exception e) {
            SystemLogger.getLogger().error(e,e);
        }
        return returnValue;
    }


//
//    /**
//     * Xóa USER
//     *
//     * @param user
//     * @return int
//     * @author Telsoft
//     * @throws java.lang.Exception
//     */
//    ////////////////////////////////////////////////////////
    public void deleteUser(long userId) throws Exception {
        try {
            DataResponse response = new DataResponse();
            HttpOutput output = CallWebservice.executeGET(WS_LINK+"deleteUser?user_id=" + userId);
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
            if (response.getCode() == 200) {
                ClientMessage.logDelete();
            } else {
                ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
            }
        } catch (Exception ex) {
            ClientMessage.logErr("Không thành công: " + ex);
        }

    }
    
    //    ////////////////////////////////////////////////////////
    public void resetUserPass(long userId) throws Exception {
        try {
            DataResponse response = new DataResponse();
            HttpOutput output = CallWebservice.executeGET(WS_LINK+"resetPassword?user_id=" + userId);
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
            if (response.getCode() == 200) {
                ClientMessage.logSuccess("Đặt lại mật khẩu thành công!" );
            } else {
                ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
            }
        } catch (Exception ex) {
            ClientMessage.logErr("Không thành công: " + ex);
        }

    }
/////////////////////////////////////////////////////////
    private Gson gson;
    final GsonBuilder builder = new GsonBuilder();

    public void addUser(User user) {
        try {
            DataResponse response = new DataResponse();
            gson = builder.create();
            user.setPassword(user.getPassword()==null?"":MD5Encoder.encrypt(user.getPassword()));
            String data = gson.toJson(user);
            HttpOutput output = CallWebservice.executePOST("addUser", data);
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
            if (response.getCode() == 200) {
                ClientMessage.logAdd();
            } else {
                ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
            }
        } catch (Exception ex) {
           SystemLogger.getLogger().error("Không thành công: " + ex);
        }

    }

    public void editUser(User user) {
        try {
            DataResponse response = new DataResponse();
            gson = builder.create();
            String data = gson.toJson(user);
            HttpOutput output = CallWebservice.executePOST("updateUser", data);
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), DataResponse.class);
            if (response.getCode() == 200) {
                ClientMessage.logUpdate();
            } else {
                ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
            }
        } catch (Exception ex) {
            ClientMessage.logErr("Không thành công: " + ex);
        }

    }

    public List<HouseHold> getListHouseByArea(long areaId) {

        List<HouseHold> listHouse = new ArrayList<>();
        ListDataResponse response = new ListDataResponse();

        try {
            String WSlink = WS_LINK + "getHouseholdByArea?area_id=" + areaId;
            HttpOutput output = CallWebservice.executeGET(WSlink);

            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(output.getResponseJson(), ListDataResponse.class);

            Gson gson = new Gson();

            for (Object object : response.getJavaResponse()) {
                listHouse.add((HouseHold) gson.fromJson(gson.toJson(object), HouseHold.class));
            }
            //System.out.println("mobifone.marketplace.client.webservice.CallWebservice.main()    success");
        } catch (IOException ex) {
            Logger.getLogger(CallWebservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listHouse;
    }
}
