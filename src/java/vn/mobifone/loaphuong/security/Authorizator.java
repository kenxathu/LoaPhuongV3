/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.security;

import java.io.Serializable;
import java.util.List;
import vn.mobifone.loaphuong.admin.entity.MenuAuthorizator;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.config.Config;
import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
/**
 * @author ChienDX
 *
 */
//Xử lý dữ liệu xác thực
public class Authorizator implements Serializable {

    //Lấy danh sách quyền chức năng
    public synchronized static List<MenuAuthorizator> getListModuleAuthorization() throws Exception {
        if (Session.getSessionValue(SessionKeyDefine.LIST_MODULE_AUTH) == null) {
           Session.setSessionValue(SessionKeyDefine.LIST_MODULE_AUTH,SecUser.getUserLogged().getActions());
        }
        return (List<MenuAuthorizator>) Session.getSessionValue(SessionKeyDefine.LIST_MODULE_AUTH);
    }
    //Kiểm tra 
//    public synchronized static String checkAuthorizator(String strModulePath) throws Exception {
//        strModulePath = strModulePath.toUpperCase();
//
//        //Check cache in sesion
//        if (Session.getSessionValue(SessionKeyDefine.MODULE_AUTH + strModulePath) != null) {
//            return (String) Session.getSessionValue(SessionKeyDefine.MODULE_AUTH + strModulePath);
//        }
//
//        List<S> menuAuthorizatedList = getListModuleAuthorization();
//        String strReturn = "";
//
//        for (MenuAuthorizator authorizator : menuAuthorizatedList) {
//            if (authorizator.getModulePath().toUpperCase().equals(strModulePath)) {
//                strReturn += authorizator.getPermissionCode();
//            }
//        }
//        Session.setSessionValue(SessionKeyDefine.MODULE_AUTH + strModulePath, strReturn.toUpperCase());

//        return S.toUpperCase();
//    }

    //Kiểm tra quyền
    public synchronized static String checkAuthorizator() throws Exception {
        String strModulePath = Config.getCurrentPath().toUpperCase();
        //Check cache in sesion
        if (Session.getSessionValue(SessionKeyDefine.MODULE_AUTH + strModulePath) != null) {
            return (String) Session.getSessionValue(SessionKeyDefine.MODULE_AUTH + strModulePath);
        }

        List<MenuAuthorizator> menuAuthorizatedList = getListModuleAuthorization();
        String strReturn = ";";

        for (MenuAuthorizator authorizator : menuAuthorizatedList) {
            
            if (authorizator.getModulePath().toUpperCase().equals(strModulePath)) {
                strReturn += authorizator.getPermissionCode();
            }
        }
        Session.setSessionValue(SessionKeyDefine.MODULE_AUTH + strModulePath, strReturn.toUpperCase());

        return strReturn.toUpperCase();
    }
}
