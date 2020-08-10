/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.admin.model;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import vn.mobifone.loaphuong.admin.entity.ActionLog;
import vn.mobifone.loaphuong.lib.SystemLogger;

/**
 *
 * @author ChienDX
 */
//Xử lý lưu log db
public class ActionLogModel implements Serializable {

    private Connection mcn;

    public ActionLogModel() {
    }

    public ActionLogModel(Connection mcn) {
        this.mcn = mcn;
    }

    //Insert DB
    public void insertLog(ActionLog actionLog) throws Exception {
        CallableStatement cs = null;

        try {           
            String strSQL = "call INSERT_ACTION_LOG(?, ?, ?, ?,?)";

            cs = mcn.prepareCall(strSQL);
            cs.setString(1, actionLog.getActionName());
            cs.setString(2, actionLog.getModuleName());
            cs.setString(3, actionLog.getUserName());
            cs.setString(4, actionLog.getDescription());
            cs.setLong(5,actionLog.getContractId());
            cs.execute();

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            throw ex;

        } finally {
            cs.close();
        }
    }

    public void setConnection(Connection cn) {
        mcn = cn;
    }

    public Connection getConnection() {
        return mcn;
    }
}
