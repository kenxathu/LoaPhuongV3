package vn.mobifone.loaphuong.security;

import com.google.gson.Gson;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import vn.mobifone.loaphuong.admin.entity.MenuAuthorizator;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.entity.Otp;
import vn.mobifone.loaphuong.entity.OtpResponse;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
import vn.mobifone.loaphuong.user.User;
import vn.mobifone.loaphuong.webservice.LoginWebservice;

@ManagedBean(name = "secuser")
@ViewScoped
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

    private Integer setTimeOtp;

    HashMap<String, String> hashMap = new HashMap<>();

    private final long createdMillis;

    public static boolean isShow() {
        try {
            String strShow = SystemConfig.getConfig("show");
            return (Long.parseLong(strShow) == 1L);
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            return false;
        }
    }

    public static boolean isSuperAdmin() {
        try {
            String strSuperAdminId = SystemConfig.getConfig("SuperAdminId");
            return (Long.parseLong(strSuperAdminId) == getUserLogged().getUserId());
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            return false;
        }
    }

    public static User getUserLogged() {
        return (User) Session.getSessionValue(SessionKeyDefine.USER);
    }

    public static long getMainArea() {
        return ((Long) Session.getSessionValue(SessionKeyDefine.MAIN_AREA)).longValue();
    }

    public long getRemainTime() {
        Date exprireTime = (Date) Session.getSessionValue(SessionKeyDefine.EXPIRE_TIME);
        Date nowTime = Calendar.getInstance().getTime();
        long remainTime = exprireTime.getTime() - nowTime.getTime();
        if (remainTime > 0L) {
            return remainTime / 1000L;
        }
        return 0L;
    }

    public String logout() {
        try {
            Session.destroySession();
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }
        return "app-main";
    }

    public String validateUser() throws Exception {
        Session.reloadBundle("SERVERCONFIG");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            LoginWebservice ws = new LoginWebservice();
            this.loginData = ws.executeLogin(this.username, this.password);
            Session.setSessionValue(SessionKeyDefine.LOGIN_DATA, this.loginData);
            if (this.loginData.getCode() == 200) {
                Gson gson = new Gson();
                this.loginUser = (User) gson.fromJson(gson.toJson(this.loginData.getJavaResponse()), User.class);
                boolean isTrueArea = false;
                String isOtpAuth = this.loginUser.getIsOtpAuth();
                if (this.loginUser.getAreaId() == Long.parseLong(SystemConfig.getConfig("vietnam"))) {
                    isTrueArea = true;
                    Session.setSessionValue(SessionKeyDefine.MAIN_AREA, Long.valueOf(this.loginUser.getAreaId()));
                } else {
                    List<AreaTree> areabranch = this.loginUser.getArea_branch();
                    for (AreaTree areaTree : areabranch) {
                        if (areaTree.getAreaType() > 1) {
                            this.loginUser.setAreaName(areaTree.getAreaName() + " - " + this.loginUser.getAreaName());
                        }
                        if (areaTree.getAreaType() == 1) {
                            this.loginUser.setAreaName(areaTree.getAreaName());
                            if (areaTree.getAreaId() == Long.parseLong(SystemConfig.getConfig("domainArea"))) {
                                isTrueArea = true;
                                Session.setSessionValue(SessionKeyDefine.MAIN_AREA, Long.valueOf(areaTree.getAreaId()));
                            }
                        }
                    }
                }
                if (isTrueArea) {
                    List<MenuAuthorizator> listAction = this.loginUser.getActions();
                    String permit = ";";
                    if (listAction != null && listAction.size() > 0) {
                        for (MenuAuthorizator menu : listAction) {
                            permit = permit + ((menu.getPermissionCode() == null) ? "" : menu.getPermissionCode());
                        }
                    }
                    if (isOtpAuth == null) {
                        this.loginUser.setPermitCode(permit);
                        Session.setSessionValue(SessionKeyDefine.LIST_MODULE_AUTH, listAction);
                        Session.setSessionValue(SessionKeyDefine.PERMIT_CODE, permit);
                        Session.setSessionValue(SessionKeyDefine.USER, this.loginUser);
                        Session.setSessionValue(SessionKeyDefine.LOGIN_FAIL, Integer.valueOf(0));
                        Session.setSessionValue(SessionKeyDefine.STATUS_OTP, Integer.valueOf(0));
                        this.showCaptcha = false;
                        return "app-main";
                    }
                    this.loginUser.setPermitCode(permit);
                    Session.setSessionValue(SessionKeyDefine.LIST_MODULE_AUTH, listAction);
                    Session.setSessionValue(SessionKeyDefine.PERMIT_CODE, permit);
                    Session.setSessionValue(SessionKeyDefine.USER, this.loginUser);
                    Session.setSessionValue(SessionKeyDefine.LOGIN_FAIL, Integer.valueOf(0));
                    Session.setSessionValue(SessionKeyDefine.SESSION_ID, this.loginUser.getSessionId());
                    Session.setSessionValue(SessionKeyDefine.STATUS_OTP, Integer.valueOf(1));
                    Session.setSessionValue(SessionKeyDefine.USER_NAME, this.username);
                    Date currDate = Calendar.getInstance().getTime();
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(currDate.getTime() + 120000L);
                    Session.setSessionValue(SessionKeyDefine.EXPIRE_TIME, c.getTime());
                    this.showCaptcha = false;
                    return "otp";
                }
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Đăng nhập sai domain! Tài khoản được sử dụng tại địa bàn" + this.loginUser.getAreaName());
                return null;
            }
            updateLoginFail(this.username);
            if (checkMaxLoginFaile()) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã nhập không thành công quá số lần quy định! Mời xác thực bằng Capcha");
                this.showCaptcha = true;
            } else {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, this.loginData.getCodeDescVn());
            }
        } catch (Exception ex) {
            ClientMessage.logErr("Không thành công: Server đang bảo trì hoặc đang bị lỗi");
            SystemLogger.getLogger().error(ex);
        }
        return null;
    }

    public String validateOtp() throws Exception {
        Session.reloadBundle("SERVERCONFIG");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Gson gson = new Gson();
            LoginWebservice ws = new LoginWebservice();
            Otp otp = new Otp(String.valueOf(Session.getSessionValue(SessionKeyDefine.SESSION_ID)), this.otpCode);
            this.otpData = ws.executeOtp(otp);
            if (this.otpData.getCode().equals(SystemConfig.getConfig("statusOtp"))) {
                this.loginData = (DataResponse) Session.getSessionValue(SessionKeyDefine.LOGIN_DATA);
                User loginUser = (User) gson.fromJson(gson.toJson(this.loginData.getJavaResponse()), User.class);
                boolean isTrueArea = false;
                if (loginUser.getAreaId() == Long.parseLong(SystemConfig.getConfig("vietnam"))) {
                    isTrueArea = true;
                    Session.setSessionValue(SessionKeyDefine.MAIN_AREA, Long.valueOf(loginUser.getAreaId()));
                } else {
                    List<AreaTree> areabranch = loginUser.getArea_branch();
                    for (AreaTree areaTree : areabranch) {
                        if (areaTree.getAreaType() > 1) {
                            loginUser.setAreaName(areaTree.getAreaName() + " - " + loginUser.getAreaName());
                        }
                        if (areaTree.getAreaType() == 1) {
                            loginUser.setAreaName(areaTree.getAreaName());
                            if (areaTree.getAreaId() == Long.parseLong(SystemConfig.getConfig("domainArea"))) {
                                isTrueArea = true;
                                Session.setSessionValue(SessionKeyDefine.MAIN_AREA, Long.valueOf(areaTree.getAreaId()));
                            }
                        }
                    }
                }
                if (isTrueArea) {
                    List<MenuAuthorizator> listAction = loginUser.getActions();
                    String permit = ";";
                    if (listAction != null && listAction.size() > 0) {
                        for (MenuAuthorizator menu : listAction) {
                            permit = permit + ((menu.getPermissionCode() == null) ? "" : menu.getPermissionCode());
                        }
                    }
                    loginUser.setPermitCode(permit);
                    Session.setSessionValue(SessionKeyDefine.LIST_MODULE_AUTH, listAction);
                    Session.setSessionValue(SessionKeyDefine.PERMIT_CODE, permit);
                    Session.setSessionValue(SessionKeyDefine.USER, loginUser);
                    Session.setSessionValue(SessionKeyDefine.LOGIN_FAIL, Integer.valueOf(0));
                    Session.setSessionValue(SessionKeyDefine.STATUS_OTP, Integer.valueOf(0));
                    this.showCaptcha = false;
                    return "app-main";
                }
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Đăng nhập sai domain! Tài khoản được sử dụng tại địa bàn" + loginUser.getAreaName());
                return null;
            }
            if (!this.showCaptcha) {
                updateOtpFail(this.otpCode);
                if (checkMaxOtpFaile()) {
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã nhập không thành công quá số lần quy định! Mời xác thực bằng Capcha");
                    this.showCaptcha = true;
                } else {
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã đăng nhập sai OTP! vui lòng nhập lại");
                }
            } else {
                Session.destroySession();
                return "login";
            }
        } catch (Exception ex) {
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("editCss()");
            if (!this.showCaptcha) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã nhập không thành công quá số lần quy định! Mời xác thực bằng Capcha");
                this.showCaptcha = true;
                SystemLogger.getLogger().error(ex);
            } else {
                Session.destroySession();
                return "login";
            }
        }
        return null;
    }

    public String reSendOtp() throws Exception {
        Session.reloadBundle("SERVERCONFIG");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Gson gson = new Gson();
            LoginWebservice ws = new LoginWebservice();
            String userName = String.valueOf(Session.getSessionValue(SessionKeyDefine.USER_NAME));
            String sessionId = String.valueOf(Session.getSessionValue(SessionKeyDefine.SESSION_ID));
            this.otpData = ws.reSendOtp(sessionId, userName);
            resendOtp(userName);
            if (checkMaxOtpResend()) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã chọn gửi lại OTP quá số lần quy định!");
                return "login";
            }
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã đăng nhập sai OTP! vui lòng nhập lại");
        } catch (Exception ex) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã chọn gửi lại OTP quá số lần quy định!");
            SystemLogger.getLogger().error(ex);
            Session.destroySession();
            return "login";
        }
        Date currDate = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currDate.getTime() + 120000L);
        Session.setSessionValue(SessionKeyDefine.EXPIRE_TIME, c.getTime());
        return "otp";
    }

    public String reSendOtp1() throws Exception {
        Session.reloadBundle("SERVERCONFIG");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Gson gson = new Gson();
            LoginWebservice ws = new LoginWebservice();
            String userName = String.valueOf(Session.getSessionValue(SessionKeyDefine.USER_NAME));
            String sessionId = String.valueOf(Session.getSessionValue(SessionKeyDefine.SESSION_ID));
            this.otpData = ws.reSendOtp(sessionId, userName);
            resendOtp(userName);
            if (checkMaxOtpResend()) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã chọn gửi lại OTP quá số lần quy định!");
                return "login";
            }
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn đã đăng nhập sai OTP! vui lòng nhập lại");
        } catch (Exception ex) {
            ClientMessage.logErr("Không thành công: Server đang bảo trì hoặc đang bị lỗi");
            SystemLogger.getLogger().error(ex);
            return "login";
        }
        Date currDate = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currDate.getTime() + 120000L);
        Session.setSessionValue(SessionKeyDefine.EXPIRE_TIME, c.getTime());
        return "otp";
    }

    public String resetOtp() throws Exception {
        Session.reloadBundle("SERVERCONFIG");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Session.destroySession();
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "HhilOTP!");
        } catch (Exception ex) {
            ClientMessage.logErr("Không thành công: Server đang bảo trì hoặc đang bị lỗi");
            SystemLogger.getLogger().error(ex);
        }
        return "login";
    }

    public SecUser() {
        this.createdMillis = System.currentTimeMillis();
    }

    public int getAgeInSeconds() {
        long nowMillis = System.currentTimeMillis();
        return (int) ((nowMillis - this.createdMillis) / 1000L);
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
            return (Integer.parseInt(strTmp) <= intTmp.intValue());
        }
        return false;
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
            return (Integer.parseInt(strTmp) <= intTmp.intValue());
        }
        return false;
    }

    private static boolean checkMaxOtpResend() throws Exception {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String strTmp = SystemConfig.getConfig("MaxOtpResend");
        if (!strTmp.trim().isEmpty()) {
            Integer intTmp = (Integer) Session.getSessionValue(SessionKeyDefine.RESEND_OTP_FAIL);
            if (intTmp == null) {
                return false;
            }
            return (Integer.parseInt(strTmp) <= intTmp.intValue());
        }
        return false;
    }

    private static void updateLoginFail(String userName) {
        Integer iFailureCount = (Integer) Session.getSessionValue(SessionKeyDefine.LOGIN_FAIL);
        String strUserLoginFail = (String) Session.getSessionValue(SessionKeyDefine.LOGIN_FAIL_USER);
        iFailureCount = (iFailureCount == null) ? new Integer(1) : (iFailureCount = Integer.valueOf(iFailureCount.intValue() + 1));
        strUserLoginFail = (strUserLoginFail == null) ? userName : (strUserLoginFail + ", " + userName);
        Session.setSessionValue(SessionKeyDefine.LOGIN_FAIL, iFailureCount);
        Session.setSessionValue(SessionKeyDefine.LOGIN_FAIL_USER, strUserLoginFail);
    }

    private static void updateOtpFail(String otp) {
        Integer iFailureCount = (Integer) Session.getSessionValue(SessionKeyDefine.OTP_FAIL);
        String strUserOtpFail = (String) Session.getSessionValue(SessionKeyDefine.OTP_FAIL_USER);
        iFailureCount = (iFailureCount == null) ? new Integer(1) : (iFailureCount = Integer.valueOf(iFailureCount.intValue() + 1));
        strUserOtpFail = (strUserOtpFail == null) ? otp : (strUserOtpFail + ", " + otp);
        Session.setSessionValue(SessionKeyDefine.OTP_FAIL, iFailureCount);
        Session.setSessionValue(SessionKeyDefine.OTP_FAIL_USER, strUserOtpFail);
    }

    private static void resendOtp(String userName) {
        Integer iFailureCount = (Integer) Session.getSessionValue(SessionKeyDefine.RESEND_OTP_FAIL);
        String strReSendOtpFail = (String) Session.getSessionValue(SessionKeyDefine.RESEND_OTP_FAIL_USER);
        iFailureCount = (iFailureCount == null) ? new Integer(1) : (iFailureCount = Integer.valueOf(iFailureCount.intValue() + 1));
        strReSendOtpFail = (strReSendOtpFail == null) ? userName : (strReSendOtpFail + ", " + userName);
        Session.setSessionValue(SessionKeyDefine.RESEND_OTP_FAIL, iFailureCount);
        Session.setSessionValue(SessionKeyDefine.RESEND_OTP_FAIL_USER, strReSendOtpFail);
    }

    public void updateInfoEvent(ActionEvent evt) {
        User currentUser = getUserLogged();
        this.updatedUser = new User();
        this.updatedUser.setUserId(currentUser.getUserId());
        this.updatedUser.setUserName(currentUser.getUserName());
        this.updatedUser.setName(currentUser.getName());
        this.updatedUser.setIdCard(currentUser.getIdCard());
        this.updatedUser.setPhone(currentUser.getPhone());
        this.updatedUser.setEmail(currentUser.getEmail());
        this.updatedUser.setType(currentUser.getType());
        this.updatedUser.setAreaId(currentUser.getAreaId());
        this.updatedUser.setRoles(currentUser.getRoles());
    }

    public void saveUserInfo(ActionEvent evt) {
        DataResponse response = (new LoginWebservice()).editUser(this.updatedUser);
        if (response.getCode() == 200) {
            ClientMessage.logSuccess("Cập nhật thành công");
            User currentUser = getUserLogged();
            currentUser.setName(this.updatedUser.getName());
            currentUser.setIdCard(this.updatedUser.getIdCard());
            currentUser.setPhone(this.updatedUser.getPhone());
            currentUser.setEmail(this.updatedUser.getEmail());
            Session.setSessionValue(SessionKeyDefine.USER, currentUser);
        } else {
            ClientMessage.logErr("Không thành công" + response.getCodeDescVn());
            return;
        }
    }

    public void changePassEvent(ActionEvent evt) {
        this.oldPass = null;
        this.newPass = null;
        this.newPass2 = null;
    }

    public void savePass(ActionEvent evt) {
        if (!this.newPass.equals(this.newPass2)) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Mật khẩu mới không khớp nhau! Vui lòng kiểm tra lại");
            return;
        }
        if (this.oldPass.equals(this.newPass2)) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Mật khẩu mới phải khác mật khẩu cũ! Vui lòng kiểm tra lại");
            return;
        }
        DataResponse response = (new LoginWebservice()).updatePassword(getUserLogged().getUserId(), this.oldPass, this.newPass, getUserLogged().getIdCard());
        if (response.getCode() == 200) {
            ClientMessage.logSuccess("Thay đổi mật khẩu thành công");
            logout();
        } else {
            ClientMessage.logErr("Không thành công" + response.getCodeDescVn());
            return;
        }
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = (username != null) ? username.toUpperCase() : null;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isShowCaptcha() {
        return this.showCaptcha;
    }

    public void setShowCaptcha(boolean showCaptcha) {
        this.showCaptcha = showCaptcha;
    }

    public String getOldPass() {
        return this.oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return this.newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getNewPass2() {
        return this.newPass2;
    }

    public void setNewPass2(String newPass2) {
        this.newPass2 = newPass2;
    }

    public User getUpdatedUser() {
        return this.updatedUser;
    }

    public void setUpdatedUser(User updatedUser) {
        this.updatedUser = updatedUser;
    }

    public String getOtpCode() {
        return this.otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getLoginUser() {
        return this.loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public OtpResponse getOtpData() {
        return this.otpData;
    }

    public void setOtpData(OtpResponse otpData) {
        this.otpData = otpData;
    }
}
