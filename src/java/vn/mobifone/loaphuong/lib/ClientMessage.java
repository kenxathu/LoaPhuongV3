/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib;


import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import vn.mobifone.loaphuong.util.ResourceBundleUtil;

/**
 * @author ChienDX
 */
public class ClientMessage implements Serializable {

    private static ResourceBundleUtil resourceBundleUtil;

    //Hàm khởi tạo
    public ClientMessage() {
        //Khởi tạo lấy dữ liệu từ PP_ADMINMESSAGE
        resourceBundleUtil = new ResourceBundleUtil("vn.mobifone.loaphuong.bundle.PP_ADMINMESSAGE");
    }

    public enum MESSAGE_TYPE {

        ERR, ADD, UPDATE, DELETE, INF;

        @Override
        public String toString() {
            String s = super.toString();
            return s.substring(0, 1) + s.substring(1).toUpperCase();
        }
    }

    //Bắn thông báo ra client
    public static synchronized void log(MESSAGE_TYPE func_name) {
        String strFuncName = getMessage("TS-10014");
        String strDetail = "";
        switch (func_name) {
            case INF: {
                break;
            }
            case ADD: {
                strDetail = getMessage("TS-10015");
                break;
            }
            case UPDATE: {
                strDetail = getMessage("TS-10016");
                break;
            }
            default: {
                strDetail = getMessage("TS-10017");
                break;
            }
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, strFuncName, strDetail);
        backReponse(msg, false);
    }

    //Bắn thông báo ra client khi thêm thành công
    public static synchronized void logAdd(String strFileName, String strKey) {
        log(MESSAGE_TYPE.ADD, ResourceBundleUtil.getAMObjectAsString(strFileName, strKey));
    }
 //Bắn thông báo ra client khi thanh cong
    public static synchronized void logSuccess(String message) {
        log(MESSAGE_TYPE.ADD, message);
    }
    //Bắn thông báo ra client khi thêm thành công
    public static synchronized void logAdd() {
        log(MESSAGE_TYPE.ADD);
    }

    //Bắn thông báo ra client khi sửa thành công
    public static synchronized void logUpdate() {
        log(MESSAGE_TYPE.UPDATE);
    }

    //Bắn thông báo ra client khi xóa thành công
    public static synchronized void logDelete() {
        log(MESSAGE_TYPE.DELETE);
    }

    //Bắn thông báo ra client khi xóa không thành công
    public static synchronized void logErrDelete() {
        logErr(MESSAGE_TYPE.DELETE);
    }

    //Bắn thông báo ra client
    public static synchronized void log(String strCode) {
        log(MESSAGE_TYPE.INF, getMessage(strCode));
    }

    //Bắn thông báo ra client khi sửa thành công
    public static synchronized void logPUpdate(String strFileName, String strKey) {
        log(MESSAGE_TYPE.UPDATE, ResourceBundleUtil.getAMObjectAsString(strFileName, strKey));
    }

    //Bắn thông báo ra client khi xóa thành công
    public static synchronized void logPDelete(String strFileName, String strKey) {
        log(MESSAGE_TYPE.DELETE, ResourceBundleUtil.getAMObjectAsString(strFileName, strKey));
    }

    //Bắn thông báo ra client khi xóa không thành công
    public static synchronized void logPErrDel(String strFileName, String strKey) {
        logErr(MESSAGE_TYPE.DELETE, ResourceBundleUtil.getAMObjectAsString(strFileName, strKey));
    }

    //Bắn thông báo ra client khi thêm không thành công
    public static synchronized void logPErrAdd(String strFileName, String strKey) {
        logErr(MESSAGE_TYPE.ADD, ResourceBundleUtil.getAMObjectAsString(strFileName, strKey));
    }

    //Bắn thông báo ra client khi sửa không thành công
    public static synchronized void logPErrUpdate(String strFileName, String strKey) {
        logErr(MESSAGE_TYPE.UPDATE, ResourceBundleUtil.getAMObjectAsString(strFileName, strKey));
    }

    //Bắn thông báo ra client
    public static synchronized void logPAdminMessage(String strKey) {
        logErr(MESSAGE_TYPE.ERR, getMessage(strKey));
    }

    //Bắn thông báo ra client
    public static synchronized void log(MESSAGE_TYPE func_name, String strDetail) {
        String strFuncName = getMessage("TS-10014");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, strFuncName, strDetail);
        backReponse(msg, false);
    }

    //Bắn thông báo lỗi ra client
    public synchronized static void logErr(String strContent) {
        String strFuncName = getMessage("TS-10018");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, strFuncName, strContent);
        backReponse(msg, true);
    }

    //Bắn thông báo lỗi ra client
    public synchronized static void logPErr(String strFileName, String strKeyMessage) {
        logErr(ResourceBundleUtil.getAMObjectAsString(strFileName, strKeyMessage));
    }

    //Bắn thông báo lỗi ra client
    public synchronized static void logErr(MESSAGE_TYPE func_name) {
        String strFuncName = getMessage("TS-10018");
        String strDetail = "";
        switch (func_name) {
            case ERR: {
                break;
            }
            case ADD: {
                strDetail = getMessage("TS-10019");
                break;
            }
            case UPDATE: {
                strDetail = getMessage("TS-10020");
                break;
            }
            default: {
                strDetail = getMessage("TS-10021");
                break;
            }
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, strFuncName, strDetail);
        backReponse(msg, true);
    }

    //Bắn thông báo lỗi ra client
    public synchronized static void logErr(MESSAGE_TYPE func_name, String strDetail) {
        String strFuncName = getMessage("TS-10018");
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, strFuncName, strDetail);
        backReponse(msg, true);
    }

    static public synchronized String getMessage(String obj) {
        if (resourceBundleUtil == null) {
            resourceBundleUtil = new ResourceBundleUtil("PP_ADMINMESSAGE");
        }
        return resourceBundleUtil.getAMObjectAsString(obj);
    }

    //Xử lý bắn thông báo về client
    private synchronized static void backReponse(FacesMessage msg, boolean bValidationFailed) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext.getCurrentInstance().addMessage(null, msg);
        if (context == null) {
            return;
        }
        context.addCallbackParam("errorProcess", true);
        context.addCallbackParam("validationFailed", bValidationFailed);
    }
    
    public static void info(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Chúc mừng", message));
    }
     
    public static void warn(String message) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Chú ý", message));

     }
     
    public static void error(String message) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Rất tiếc", message));
    }
     
  
}
