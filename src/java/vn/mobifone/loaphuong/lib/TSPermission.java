/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib;


import java.io.Serializable;
import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
import vn.mobifone.loaphuong.security.Authorizator;
import vn.mobifone.loaphuong.security.SecUser;

/**
 * Xử lý quyền
 * @author ChienDX
 */
public abstract class TSPermission implements Serializable {

    public boolean getPermission(String permissionCode) {   
        boolean bReturnValue = false;
        String permitCode  =  Session.getSessionValue(SessionKeyDefine.PERMIT_CODE) ==null ? "": Session.getSessionValue(SessionKeyDefine.PERMIT_CODE).toString();
        try {
           
            return SecUser.isSuperAdmin() || permitCode.contains(";" + permissionCode + ";");

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }

        return bReturnValue;
    }
    public boolean isIsAllowView(String permitCode) {
        return true;
    }
   
    public boolean isIsAllowInsert(String permitCode) {
        return getPermission(permitCode);
    }
    

    public boolean isIsAllowUpdate(String permitCode) {
        return getPermission(permitCode);
    }
    
    public boolean isIsAllowDelete(String permitCode) {
        return getPermission(permitCode);
    }
    
    public boolean isIsAllowApproval(String permitCode){
        return getPermission(permitCode);
    }
}
