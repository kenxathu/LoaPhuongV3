/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib;

import java.io.Serializable;
import vn.mobifone.loaphuong.util.ResourceBundleUtil;

/**
 * Lấy dữ liệu từ file SERVERCONFIG
 * @author ChienDX
 */
public class SystemConfig implements Serializable {

    private static final ResourceBundleUtil bundleUtil = new ResourceBundleUtil("SERVERCONFIG");

    public static String getConfig(String strKey) {
        try {
            return bundleUtil.getObjectAsString(strKey);

        } catch (Exception ex) {
            return "";
        }
    }
    public static int getConfigInt(String strKey) {
        try {
            return bundleUtil.getObjectAsIntegerPrimitive(strKey);

        } catch (Exception ex) {
            return 0;
        }
    }
}
