/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.admin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import vn.mobifone.loaphuong.admin.entity.MenuAuthorizator;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.data.DataPreprocessor;

/**
 *
 * @author ChienDX
 */
//Xử lý quyền
public class AuthorizatorModel extends DataPreprocessor implements Serializable {

    //Lấy danh sách quyền
    public List<MenuAuthorizator> getListModuleAuthorization_Old(long userId) throws Exception {
        List<MenuAuthorizator> returnValue = new ArrayList();

        try {
            open();
            String strSQL = ""
                    + "  SELECT b.module_id, "
                    + "         b.action_url, "
                    + "         LISTAGG ('[' || a.permit_code || ']', '') "
                    + "             WITHIN GROUP (ORDER BY a.permit_code) "
                    + "             AS permission "
                    + "    FROM user_module a, module b "
                    + "   WHERE a.role_id = ? AND a.module_id = b.module_id and a.user_id  = 0 > 0 "
                    + "GROUP BY a.module_id, b.action_url, b.module_id  ";

            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setLong(1, userId);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                MenuAuthorizator menuAuth = new MenuAuthorizator();
                menuAuth.setModulePath(mRs.getString("action_url"));
                menuAuth.setPermissionCode(mRs.getString("permission"));
                returnValue.add(menuAuth);
            }

            return returnValue;

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex);

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }
    }
    //Lấy danh sách quyền mới

    public List<MenuAuthorizator> getListModuleAuthorization(long roleId, long orgId, String userName) throws Exception {
        List<MenuAuthorizator> returnValue = new ArrayList();

        try {
            open();
            String strSQL
                    //= "  select a.module_id, a.module_name,a.parent_id,a.action_url ,"
                    = "  select  a.action_url,om.permission,om.extend_permit "
                    + "  from module a "
                    + "  inner join( "
                    + "     select a.module_id, a.role_id,a.org_id,"
                    + "         nvl(b.spermit_code,a.permit_code) permission "
                    + "         , nvl(b.extend_permit,a.extend_permit) extend_permit "
                    + "         from organization_module a "
                    + "         left join user_module b "
                    + "         on (a.module_id = b.module_id  and b.user_name = ? )"
                    + "     where a.role_id = ? "
                    + "         and a.org_id = ? "
                    + "         and nvl(b.spermit_code,a.permit_code) <> '0;0;0;0;'\n"
                    + "         )om "
                    + "  on om.module_id = a.module_id  "
                    + "  where a.status = 1";

            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setLong(2, roleId);
            mStmt.setString(1, userName);
            mStmt.setLong(3, orgId);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                MenuAuthorizator menuAuth = new MenuAuthorizator();
                menuAuth.setModulePath(mRs.getString("action_url"));
                menuAuth.setPermissionCode(mRs.getString("extend_permit") == null? 
                        mRs.getString("permission") : (mRs.getString("permission")+mRs.getString("extend_permit")));
                returnValue.add(menuAuth);
            }

            return returnValue;

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex);

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }
    }
}
