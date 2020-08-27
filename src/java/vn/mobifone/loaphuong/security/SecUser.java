/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.security;

import com.google.gson.Gson;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import vn.mobifone.loaphuong.admin.entity.MenuAuthorizator;
import vn.mobifone.loaphuong.controller.AreaController;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;

import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.entity.Otp;
import vn.mobifone.loaphuong.entity.OtpResponse;
import vn.mobifone.loaphuong.entity.UserLogin;
import vn.mobifone.loaphuong.user.User;
import vn.mobifone.loaphuong.user.UserModel;
import vn.mobifone.loaphuong.webservice.CallWebservice;
import vn.mobifone.loaphuong.webservice.LoginWebservice;

/**
 *
 * @author ChienDX
 */
@ManagedBean(name = "secuser")
@ViewScoped
//Controller xử lý đăng nhập
public class SecUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private DataResponse loginData;
    private OtpResponse otpData;
    private boolean showCaptcha = false;

    private String oldPass;
    private String newPass;
    private String newPass2;
    private User updatedUser;
    private User loginUser;
    private String otpCode;
    private String sessionId;

    public SecUser() {
//        try {
//            if (checkMaxLoginFaile()) {
//                showCaptcha = true;
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(SecUser.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    //Kiểm tra user có quyền xem nhánh con
    public static boolean isShow() {
        try {
            String strShow = SystemConfig.getConfig("show");
            return Long.parseLong(strShow) == 1;

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            return false;
        }

    }

    //Kiểm tra user có phải là super admin không
    public static boolean isSuperAdmin() {
        try {
            String strSuperAdminId = SystemConfig.getConfig("SuperAdminId");
            return Long.parseLong(strSuperAdminId) == getUserLogged().getUserId();

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            return false;
        }

    }

    //Lấy enity user hiện tại
    public static User getUserLogged() {
        return (User) Session.getSessionValue(SessionKeyDefine.USER);
    }

    public static long getMainArea() {
        return (long) Session.getSessionValue(SessionKeyDefine.MAIN_AREA);
    }

    //Đăng xuất
    public String logout() {
        try {
            //DataUtil.performAction(UserModel.class, "logActionLogout", Session.getIPClient());
            Session.destroySession();

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }

        return "app-main";
    }

    //Đăng nhập
    public String validateUser() throws Exception {
        Session.reloadBundle("SERVERCONFIG");

        FacesContext context = FacesContext.getCurrentInstance();

        try {

            LoginWebservice ws = new LoginWebservice();
            loginData = ws.executeLogin(username, password);
            Session.setSessionValue(SessionKeyDefine.LOGIN_DATA, loginData);
            if (loginData.getCode() == 200) {   // login success

                Gson gson = new Gson();
                loginUser = (User) gson.fromJson(gson.toJson(loginData.getJavaResponse()), User.class);

                boolean isTrueArea = false;
                //System.out.println("loginUser ==> " + loginUser.toJson());
                String isOtpAuth = loginUser.getIsOtpAuth();
                if (loginUser.getAreaId() == Long.parseLong(SystemConfig.getConfig("vietnam"))) {
                    isTrueArea = true;
                    Session.setSessionValue(SessionKeyDefine.MAIN_AREA, loginUser.getAreaId());
                } else {
                    List<AreaTree> areabranch = loginUser.getArea_branch();
                    for (AreaTree areaTree : areabranch) {
                        if (areaTree.getAreaType() > 1) {
                            loginUser.setAreaName(areaTree.getAreaName() + " - " + loginUser.getAreaName());
                        }
                        if (areaTree.getAreaType() == 1) {
                            loginUser.setAreaName(areaTree.getAreaName());
                            if (areaTree.getAreaId() == Long.parseLong(SystemConfig.getConfig("domainArea"))) {
                                //if (areaTree.getAreaId() != 1 && areaTree.getAreaId() != 143 && areaTree.getAreaId() != 1300 ) {
                                isTrueArea = true;
                                Session.setSessionValue(SessionKeyDefine.MAIN_AREA, areaTree.getAreaId());
                            }
                        }
                    }
                }

                // Check đăng nhập đúng domain
                if (isTrueArea) {
                    List<MenuAuthorizator> listAction = loginUser.getActions();
                    String permit = ";";
                    if (listAction != null && listAction.size() > 0) {
                        for (MenuAuthorizator menu : listAction) {
                            permit += (menu.getPermissionCode() == null) ? "" : menu.getPermissionCode();
                        }
                    }
                    if (isOtpAuth == null) {
                        loginUser.setPermitCode(permit);
                        Session.setSessionValue(SessionKeyDefine.LIST_MODULE_AUTH, listAction);
                        Session.setSessionValue(SessionKeyDefine.PERMIT_CODE, permit);
                        Session.setSessionValue(SessionKeyDefine.USER, loginUser);
                        Session.setSessionValue(SessionKeyDefine.LOGIN_FAIL, 0);
                        Session.setSessionValue(SessionKeyDefine.STATUS_OTP, 0);
                        // Session.setSessionValue(SessionKeyDefine.TIME_TABLE_LIST, ws.getTimeTableByArea(loginUser.getAreaId(), loginUser.geta));
                        showCaptcha = false;
                        return "app-main";
                    } else {
                        loginUser.setPermitCode(permit);
                        Session.setSessionValue(SessionKeyDefine.LIST_MODULE_AUTH, listAction);
                        Session.setSessionValue(SessionKeyDefine.PERMIT_CODE, permit);
                        Session.setSessionValue(SessionKeyDefine.USER, loginUser);
                        Session.setSessionValue(SessionKeyDefine.LOGIN_FAIL, 0);
                        Session.setSessionValue(SessionKeyDefine.SESSION_ID, loginUser.getSessionId());
                        Session.setSessionValue(SessionKeyDefine.STATUS_OTP, 1);
//                         //Session.setSessionValue(SessionKeyDefine.TIME_TABLE_LIST, ws.getTimeTableByArea(loginUser.getAreaId(), loginUser.geta));
                        showCaptcha = false;
                        //RequestContext.getCurrentInstance().execute("showOtpForm()");
                        return "otp";
                        
                    }
                  
                } else {
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Đăng nhập sai domain! Tài khoản bạn đăng nhập được sử dụng tại địa bàn " + loginUser.getAreaName());
                    return null;
                }
               
            } else {
                updateLoginFail(username);
                if (checkMaxLoginFaile()) {
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã đăng nhập không thành công quá số lần quy định! Mời xác thực bằng Captcha");
                    showCaptcha = true;
                } else {
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, loginData.getCodeDescVn());
                }
            }
        
        } catch (Exception ex) {
            ClientMessage.logErr("Không thành công: " + "Server đang bảo trì hoặc đang bị lỗi");
            SystemLogger.getLogger().error(ex);
        } 
         
        return null;

    }

    // checkOTP
    public String validateOtp() throws Exception {
        Session.reloadBundle("SERVERCONFIG");

        FacesContext context = FacesContext.getCurrentInstance();

        try {

            Gson gson = new Gson();
            LoginWebservice ws = new LoginWebservice();
            Otp otp = new Otp(String.valueOf(Session.getSessionValue(SessionKeyDefine.SESSION_ID)), otpCode);
            // User user = (User) Session.getSessionValue(SessionKeyDefine.USER);
            otpData = ws.executeOtp(otp);

            // OtpResponse otpRes = new OtpResponse(otpData);
            if (otpData.getCode().equals(SystemConfig.getConfig("statusOtp"))) {   // login success

                loginData = (DataResponse) Session.getSessionValue(SessionKeyDefine.LOGIN_DATA);
                User loginUser = (User) gson.fromJson(gson.toJson(loginData.getJavaResponse()), User.class);

                boolean isTrueArea = false;
                if (loginUser.getAreaId() == Long.parseLong(SystemConfig.getConfig("vietnam"))) {
                    isTrueArea = true;
                    Session.setSessionValue(SessionKeyDefine.MAIN_AREA, loginUser.getAreaId());
                } else {
                    List<AreaTree> areabranch = loginUser.getArea_branch();
                    for (AreaTree areaTree : areabranch) {
                        if (areaTree.getAreaType() > 1) {
                            loginUser.setAreaName(areaTree.getAreaName() + " - " + loginUser.getAreaName());
                        }
                        if (areaTree.getAreaType() == 1) {
                            loginUser.setAreaName(areaTree.getAreaName());
                            if (areaTree.getAreaId() == Long.parseLong(SystemConfig.getConfig("domainArea"))) {
                                //if (areaTree.getAreaId() != 1 && areaTree.getAreaId() != 143 && areaTree.getAreaId() != 1300 ) {
                                isTrueArea = true;
                                Session.setSessionValue(SessionKeyDefine.MAIN_AREA, areaTree.getAreaId());
                            }
                        }
                    }
                }

                // Check đăng nhập đúng domain
                if (isTrueArea) {
                    List<MenuAuthorizator> listAction = loginUser.getActions();
                    String permit = ";";
                    if (listAction != null && listAction.size() > 0) {
                        for (MenuAuthorizator menu : listAction) {
                            permit += (menu.getPermissionCode() == null) ? "" : menu.getPermissionCode();
                        }
                    }
                    loginUser.setPermitCode(permit);
                    Session.setSessionValue(SessionKeyDefine.LIST_MODULE_AUTH, listAction);
                    Session.setSessionValue(SessionKeyDefine.PERMIT_CODE, permit);
                    Session.setSessionValue(SessionKeyDefine.USER, loginUser);
                    Session.setSessionValue(SessionKeyDefine.LOGIN_FAIL, 0);
                    Session.setSessionValue(SessionKeyDefine.STATUS_OTP, 0);
                    // Session.setSessionValue(SessionKeyDefine.TIME_TABLE_LIST, ws.getTimeTableByArea(loginUser.getAreaId(), loginUser.geta));
                    showCaptcha = false;
                    return "app-main";
                } else {
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Đăng nhập sai domain! Tài khoản bạn đăng nhập được sử dụng tại địa bàn " + loginUser.getAreaName());
                    return null;
                }
            } else {

//            } else {
                //Session.destroySession();
                updateOtpFail(otpCode);
                if (checkMaxOtpFaile()) {
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã đăng nhập không thành công quá số lần quy định! Mời xác thực bằng Captcha");
                    showCaptcha = true;
                } else {
                    //RequestContext.getCurrentInstance().execute("showAlert()");

                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã đăng nhập sai OTP! Vui lòng nhập lại");
                }
            }

//            if (checkMaxOtpFaile()) {
//                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã đăng nhập không thành công quá số lần quy định! Mời xác thực bằng Captcha");
//                showCaptcha = true;
//            } else {
//                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, loginData.getCodeDescVn());
//            }
            //           User user = new UserModel().getInfByUserName(username);
//            if (user == null) {
//                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, loginData.getCodeDescVn());
//                return null;
//            }
//            Session.setSessionValue(SessionKeyDefine.USER, user);
//            return "app-main";
        } catch (Exception ex) {
            ClientMessage.logErr("Không thành công: " + "Server đang bảo trì hoặc đang bị lỗi");
            SystemLogger.getLogger().error(ex);
        }
        return null;

    }

    //reset OTP
    public String resetOtp() throws Exception {
        Session.reloadBundle("SERVERCONFIG");

        FacesContext context = FacesContext.getCurrentInstance();

        try {

            Session.destroySession();
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Hết thời gian hiệu lực OTP! Vui lòng đăng nhập lại.");

        } catch (Exception ex) {
            ClientMessage.logErr("Không thành công: " + "Server đang bảo trì hoặc đang bị lỗi");
            SystemLogger.getLogger().error(ex);
        }
        return "login";

    }

    private final long createdMillis = System.currentTimeMillis();

    public int getAgeInSeconds() {
        long nowMillis = System.currentTimeMillis();
        return (int) ((nowMillis - this.createdMillis) / 1000);
    }

    private static boolean checkMaxLoginFaile() throws Exception {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        System.out.println("ipAddress:" + ipAddress);

        String strTmp = SystemConfig.getConfig("MaxLoginFailure");
        if (!strTmp.trim().isEmpty()) {
            Integer intTmp = (Integer) Session.getSessionValue(SessionKeyDefine.LOGIN_FAIL);

            if (intTmp == null) {
                return false;
            }

            return ((Integer.parseInt(strTmp) <= intTmp));

        } else {
            return false;
        }
    }

    private static boolean checkMaxOtpFaile() throws Exception {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        System.out.println("ipAddress:" + ipAddress);

        String strTmp = SystemConfig.getConfig("MaxOtpFailure");
        if (!strTmp.trim().isEmpty()) {
            Integer intTmp = (Integer) Session.getSessionValue(SessionKeyDefine.OTP_FAIL);

            if (intTmp == null) {
                return false;
            }

            return ((Integer.parseInt(strTmp) <= intTmp));

        } else {
            return false;
        }
    }

    private static void updateLoginFail(String userName) {
        Integer iFailureCount = (Integer) Session.getSessionValue(SessionKeyDefine.LOGIN_FAIL);
        String strUserLoginFail = (String) Session.getSessionValue(SessionKeyDefine.LOGIN_FAIL_USER);

        iFailureCount = (iFailureCount == null ? (new Integer(1)) : (++iFailureCount));
        strUserLoginFail = strUserLoginFail == null ? userName : strUserLoginFail + ", " + userName;

        Session.setSessionValue(SessionKeyDefine.LOGIN_FAIL, iFailureCount);
        Session.setSessionValue(SessionKeyDefine.LOGIN_FAIL_USER, strUserLoginFail);
    }

    private static void updateOtpFail(String otp) {
        Integer iFailureCount = (Integer) Session.getSessionValue(SessionKeyDefine.OTP_FAIL);
        String strUserOtpFail = (String) Session.getSessionValue(SessionKeyDefine.OTP_FAIL_USER);

        iFailureCount = (iFailureCount == null ? (new Integer(1)) : (++iFailureCount));
        strUserOtpFail = strUserOtpFail == null ? otp : strUserOtpFail + ", " + otp;

        Session.setSessionValue(SessionKeyDefine.OTP_FAIL, iFailureCount);
        Session.setSessionValue(SessionKeyDefine.OTP_FAIL_USER, strUserOtpFail);
    }

    //{"user_id":9,"username":"hanoi.admin","name":"Admin TP Hà Nội","type":1,"id_card":"14628372","phone":"0902191466","email":"hanoi@gmail.com","area_id":1,"household_id":0,"roles":[1]}
    public void updateInfoEvent(ActionEvent evt) {
        User currentUser = getUserLogged();
        updatedUser = new User();
        updatedUser.setUserId(currentUser.getUserId());
        updatedUser.setUserName(currentUser.getUserName());
        updatedUser.setName(currentUser.getName());
        updatedUser.setIdCard(currentUser.getIdCard());
        updatedUser.setPhone(currentUser.getPhone());
        updatedUser.setEmail(currentUser.getEmail());
        updatedUser.setType(currentUser.getType());
        updatedUser.setAreaId(currentUser.getAreaId());
        updatedUser.setRoles(currentUser.getRoles());

    }

    public void saveUserInfo(ActionEvent evt) {
        DataResponse response = (new LoginWebservice()).editUser(updatedUser);

        if (response.getCode() == 200) {
            ClientMessage.logSuccess("Cập nhật thông tin thành công!");
            User currentUser = getUserLogged();
            currentUser.setName(updatedUser.getName());
            currentUser.setIdCard(updatedUser.getIdCard());
            currentUser.setPhone(updatedUser.getPhone());
            currentUser.setEmail(updatedUser.getEmail());
            Session.setSessionValue(SessionKeyDefine.USER, currentUser);
        } else {
            ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
            return;
        }
    }

    public void changePassEvent(ActionEvent evt) {
        oldPass = null;
        newPass = null;
        newPass2 = null;
    }

    public void savePass(ActionEvent evt) {
        if (!newPass.equals(newPass2)) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Mật khẩu mới không khớp nhau! Vui lòng kiểm tra lại");
            return;
        }

        if (oldPass.equals(newPass2)) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Mật mới phải khác mật khẩu cũ! Vui lòng nhập lại");
            return;
        }

        DataResponse response = (new LoginWebservice()).updatePassword(getUserLogged().getUserId(), oldPass, newPass, getUserLogged().getIdCard());

        if (response.getCode() == 200) {
            ClientMessage.logSuccess("Thay đổi mật khẩu thành công!");
            logout();
        } else {
            ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
            return;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username != null ? username.toUpperCase() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isShowCaptcha() {
        return showCaptcha;
    }

    public void setShowCaptcha(boolean showCaptcha) {
        this.showCaptcha = showCaptcha;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getNewPass2() {
        return newPass2;
    }

    public void setNewPass2(String newPass2) {
        this.newPass2 = newPass2;
    }

    public User getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(User updatedUser) {
        this.updatedUser = updatedUser;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

//    public static void main(String[] args) {
//        SecUser sec = new SecUser();
//        
//        int time = 120;
//        // long start = System.currentTimeMillis();
//        while (true) {
//            try {
//                //System.out.println(String.valueOf(time));
//                // long seconds = (System.currentTimeMillis() - start) / 1000;
//                Thread.sleep(1000);
//                time = time - 1;
//                
//                System.out.println("countDown " + time);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }finally{
//              if(time == 100){
//                  break;
//              }
//            }
//          
//        }
//
//    }
}
