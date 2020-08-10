/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationResourceBundle;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ChienDX
 */
//Lấy các thông tin về session
public class Session implements Serializable {

    //Lấy url request
    public static String getRequestURL(FacesContext context) {
        Object request = context.getExternalContext().getRequest();
        if (request instanceof HttpServletRequest) {
            return ((HttpServletRequest) request).getRequestURL().toString();
        } else {
            return "";
        }
    }
    ////////////////////////////////////////////////////////////////////////

    //Lấy context path
    public static String getContextPath(FacesContext context) {
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String str = request.getServletPath();
        return request.getContextPath();
    }
    ////////////////////////////////////////////////////////////////////////

    //Lấy request function
    public static String getRequestFunction(FacesContext context) {
        Object request = context.getExternalContext().getRequest();
        if (request instanceof HttpServletRequest) {
            String strReturn = ((HttpServletRequest) request).getRequestURL().toString();
            strReturn = strReturn.substring(strReturn.lastIndexOf("/"), strReturn.length());
            return strReturn;
        } else {
            return "";
        }
    }
    ////////////////////////////////////////////////////////////////////////

    //Lấy ip 
    public static String getIPClient(FacesContext context) {
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getRemoteHost();
    }
    ////////////////////////////////////////////////////////////////////////

    //Lấy ip 
    public static String getIPClient() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getRemoteHost();
    }
    ////////////////////////////////////////////////////////////////////////

    //Reload config file
    public static void reloadBundle(String strFileName) {
        ResourceBundle.clearCache(Thread.currentThread().getContextClassLoader());
        ApplicationResourceBundle appBundle = ApplicationAssociate.getCurrentInstance().getResourceBundles().get(strFileName);
        Map<Locale, ResourceBundle> resources = getFieldValue(appBundle, "resources");
        resources.clear();
    }
    ////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    private static <T> T getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////

    //Lấy session id
    public static String getSessionId() {
        FacesContext fCtx = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
        return session.getId();
    }
    ////////////////////////////////////////////////////////////////////////

    //Xóa object trên session
    public static Object removeSession(String strKey) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().remove(strKey);
    }
    ////////////////////////////////////////////////////////////////////////

    //Lấy object trên session
    public static Object getSessionValue(String strKey) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get(strKey);
    }
    ////////////////////////////////////////////////////////////////////////

    //Đẩy object vào session
    public static void setSessionValue(String strKey, Object objValue) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().getSessionMap().put(strKey, objValue);
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }
    }
    ////////////////////////////////////////////////////////////////////////

    //Hủy session
    public static void destroySession() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    ////////////////////////////////////////////////////////////////////////

    //Lấy response
    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }
}
