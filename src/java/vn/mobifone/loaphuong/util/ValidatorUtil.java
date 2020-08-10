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
public class ValidatorUtil {

    public  static String getMessageOracleError(Exception ex) {
        ResourceBundleUtil bundle = new ResourceBundleUtil("PP_ORAMSG");
        String strExceptionMessage = ex.getMessage();
        String strMSGContent = strExceptionMessage;

        if (strExceptionMessage.contains("ORA-")) {
            //Get Oracle error code
            Pattern p;
            String strOracleErrorCode = "";
            String strOracleErrorField = "";

            if (strExceptionMessage.contains("ORA-00001")) {
                p = Pattern.compile("ORA-(\\d+).\\sunique\\sconstraint\\s\\(.+\\.(.+)\\)\\sviolated");

                Matcher m = p.matcher(strExceptionMessage);
                while (m.find()) {
                    strOracleErrorCode = m.group(1);
                    strOracleErrorField = m.group(2);
                }

            } else {
                p = Pattern.compile("ORA-(\\d+)");

                Matcher m = p.matcher(strExceptionMessage);
                while (m.find()) {
                    strOracleErrorCode = m.group(1);
                }
            }

            //Get message
            try {
                strMSGContent = bundle.getAMObjectAsString("ORA-" + strOracleErrorCode);
                if ("00001".equals(strOracleErrorCode)) {
                    strMSGContent += " : " + bundle.getAMObjectAsString(strOracleErrorField);
                }

                if (strMSGContent.equals("ORA-" + strOracleErrorCode)) {
                    strMSGContent = strExceptionMessage;
                }

            } catch (Exception ext) {
                SystemLogger.getLogger().error(ext);
            }
        }

        return strMSGContent;
    }
}
