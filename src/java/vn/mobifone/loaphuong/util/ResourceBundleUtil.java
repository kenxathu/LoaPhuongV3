/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.util;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;

/**
 * @author ChienDX
 * Lấy dữ liệu từ các file ngon ngữ
 */
public class ResourceBundleUtil implements Serializable {

    private String strBundleFileName = "";
    public static String getCurrentPath() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String strXhtmlFile = StringUtil.fix(request.getParameter("moduleName")) + StringUtil.fix(request.getParameter("clientName"));
        return  strXhtmlFile.toUpperCase();
    }
    //Hàm khởi tạo mặc định lấy file ngôn ngữ ứng với chức năng hiện tại
    public ResourceBundleUtil() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String strXhtmlFile = StringUtil.fix(request.getParameter("moduleName")) + StringUtil.fix(request.getParameter("clientName"));
        strBundleFileName = SystemConfig.getConfig("BundlePackage") + "." + "PP_" + strXhtmlFile.toUpperCase();
    }

    //Hàm khởi tạo lấy file tùy chọn
    public ResourceBundleUtil(String bundleFileName) {
        strBundleFileName = bundleFileName;
    }

    //Lấy giá trị từ key
    public String getObjectAsString(String objectName) {
        String strReturn = objectName;

        try {
            if (FacesContext.getCurrentInstance() == null) {
                return null;
            }

            return ResourceBundle.getBundle(strBundleFileName, FacesContext.getCurrentInstance().getViewRoot().getLocale()).getObject(objectName).toString();

        } catch (Exception ex) {
            //SystemLogger.getLogger().error(ex);
            strReturn = null;
        }

        return strReturn;
    }

    //Lấy giá trị từ key
    //File ngôn ngữ của admin
    public String getAMObjectAsString(String objectName) {
        String strReturn = objectName;

        try {
            if (FacesContext.getCurrentInstance() == null) {
                return null;
            }

            strReturn = ResourceBundle.getBundle("vn.mobifone.loaphuong.bundle." + strBundleFileName, FacesContext.getCurrentInstance().getViewRoot().getLocale()).getObject(objectName).toString();

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }
        return strReturn;
    }
    //Lấy giá trị từ key
    //File ngôn ngữ của admin
    public String getCLObjectAsString(String objectName) {
        String strReturn = objectName;

        try {
            if (FacesContext.getCurrentInstance() == null) {
                return null;
            }

            strReturn = ResourceBundle.getBundle("vn.mobifone.loaphuong.bundle." + strBundleFileName).getObject(objectName).toString();

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }
        return strReturn;
    }
    //Lấy giá trị integer
    public int getObjectAsIntegerPrimitive(String objectName) {
        return Integer.parseInt(getObjectAsString(objectName));
    }

    public static String getAMObjectAsString(String bundleFileName, String objectName) {
        ResourceBundleUtil resource = new ResourceBundleUtil(bundleFileName);
        return resource.getAMObjectAsString(objectName);
    }

    public static ResourceBundle getResourceBundle(String strResourceName) {
        return ResourceBundle.getBundle("vn.mobifone.loaphuong.bundle." + strResourceName);
    }

    public static ResourceBundle getDefaultResourceBundle(String strResourceName) {
        return ResourceBundle.getBundle(strResourceName);
    }

    public static Properties getPropertiesFile(String strFileName) {
        return convertResourceBundleToProperties(getDefaultResourceBundle(strFileName));
    }

    public static Properties convertResourceBundleToProperties(ResourceBundle resource) {
        Properties properties = new Properties();
        Enumeration<String> keys = resource.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            properties.put(key, resource.getString(key));
        }
        return properties;
    }
}
