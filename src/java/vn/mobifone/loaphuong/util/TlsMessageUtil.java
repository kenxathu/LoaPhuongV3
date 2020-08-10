/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import vn.mobifone.loaphuong.lib.SystemLogger;

/**
 * Dịch mã lỗi
 * @author ChienDX
 */
public class TlsMessageUtil {

    public static String translateMessage(Exception ex) {
        return translateMessage(ex.toString());
    }    
    
    public static String translateMessage(String strException) {
        String returnVal = strException;
        strException = strException.toUpperCase();
        
        if(!strException.contains("TLS_") || !strException.contains("_TLS")) {
            return strException;
        }
        
        try {
            Pattern p;
            p = Pattern.compile("TLS_(.+)_TLS");
            Matcher m = p.matcher(strException);
            
            while (m.find()) {
                returnVal = ResourceBundleUtil.getAMObjectAsString("PP_ADMINMESSAGE", m.group(1).toUpperCase());
            }

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }

        return returnVal;
    }
}
