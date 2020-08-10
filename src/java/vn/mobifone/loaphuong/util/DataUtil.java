package vn.mobifone.loaphuong.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.TelsoftException;

/**
 * @author ChienDX
 */
//Util để gọi hàm trong model và dịch lỗi exception
public class DataUtil implements Serializable {

    public static Object performAction(Class<?> className, String methodName, Object... args) throws TelsoftException {
        Object object = new Object();
        Method[] methods = className.getMethods();
        boolean bSuccess = false;
        IllegalArgumentException ArgumentEx = null;

        try {
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    try {
                        method.setAccessible(true);
                        object = method.invoke(className.newInstance(), args);
                        bSuccess = true;
                        break;

                    } catch (InvocationTargetException ex) {
                        Throwable cause = ex.getCause();
                        ResourceBundleUtil bundle = new ResourceBundleUtil("PP_ORAMSG");
                        String strExceptionMessage = cause.getMessage();
                        String strMSGContent = "";

                        if (strExceptionMessage != null && strExceptionMessage.contains("ORA-")) {
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
                                throw new TelsoftException("ERR", ext);
                            }
                            throw new TelsoftException(strExceptionMessage, strMSGContent, cause);

                        } else if (strExceptionMessage != null && strExceptionMessage.toUpperCase().contains("TLS_") && strExceptionMessage.toUpperCase().contains("_TLS")) {
                            throw new TelsoftException(strExceptionMessage, TlsMessageUtil.translateMessage(strExceptionMessage), cause);
                        }

                        throw new TelsoftException("ERR", cause.getMessage(), cause);

                    } catch (IllegalArgumentException ex) {
                        ArgumentEx = ex;
                    }
                }
            }

            if (!bSuccess && ArgumentEx != null) {
                throw ArgumentEx;
            }

        } catch (InstantiationException iex) {
            SystemLogger.getLogger().error(iex);
            throw new TelsoftException("ERR", iex);

        } catch (IllegalAccessException iaex) {
            SystemLogger.getLogger().error(iaex);
            throw new TelsoftException("ERR", iaex);
        }
        return object;
    }

    @SuppressWarnings("rawtypes")
    public static List getData(Class<?> className, String methodName, Object... args) throws TelsoftException {
        return (List) performAction(className, methodName, args);
    }
    
    public static String getStringData(Class<?> className, String methodName, Object... args) throws TelsoftException {
        return (String) performAction(className, methodName, args);
    }

    public static List getData(Class<?> className, String methodName) throws TelsoftException {
        return getData(className, methodName, new Object[0]);
    }
}
