/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib;


import java.io.Serializable;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import vn.mobifone.loaphuong.util.ResourceBundleUtil;

/**
 * Xử lý ghi log
 * @author ChienDX
 */
public class SystemLogger implements Serializable {

    private static org.apache.log4j.Logger logger = Logger.getLogger(SystemLogger.class);
    public static final String LOG4J_FILE_CONFIG_NAME = "LOG4J";

    public SystemLogger() {
    }

    public static void reLoadConfig() {
        Session.reloadBundle(LOG4J_FILE_CONFIG_NAME);
        PropertyConfigurator.configure(ResourceBundleUtil.getPropertiesFile(LOG4J_FILE_CONFIG_NAME));
    }

    public static Logger getLogger() {
        return logger;
    }
}
