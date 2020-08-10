/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib.data;

import java.sql.*;
import org.apache.commons.dbutils.DbUtils;
import org.codehaus.jackson.map.ObjectMapper;
import vn.mobifone.loaphuong.admin.entity.ActionLog;
import vn.mobifone.loaphuong.admin.model.ActionLogModel;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.lib.config.Config;

/**
 * Xử lý đóng mở Connection và ghi log
 * @author ChienDX
 */
public class DataPreprocessor {

    public Connection mConnection = null;
    public PreparedStatement mStmt = null;
    public ResultSet mRs = null;
    public ActionLogModel log;

    public Connection getConnection() throws Exception {
        return mConnection;
    }

    public DataPreprocessor() {
    }

    public void open() throws Exception {
        if (mConnection == null || mConnection.isClosed()) {
            while (1 == 1) {
                try {
                    mConnection = ConnectionFactory.getConnection();
                    break;

                } catch (Exception ex) {
                    if (!ex.toString().contains("Closed Connection")) {
                        throw ex;
                    }
                }
            }
        }
    }

    public void close() throws SQLException {
        DbUtils.closeQuietly(mConnection);
    }

    public void close(ResultSet rs, PreparedStatement stmt) throws SQLException {
        close(rs);
        close(stmt);
    }

    public void close(PreparedStatement stmt, Connection connection) throws SQLException {
        close(stmt);
        close();
    }

    public void close(Connection connection) throws SQLException {
        DbUtils.closeQuietly(connection);
    }

    public void close(PreparedStatement stmt) throws SQLException {
        DbUtils.closeQuietly(stmt);
    }

    public void close(Statement stmt) throws SQLException {
        DbUtils.closeQuietly(stmt);
    }

    public void close(ResultSet rs) throws SQLException {
        DbUtils.closeQuietly(rs);
    }

    public void close(Connection connection, PreparedStatement stmt, ResultSet rs) throws SQLException {
        close(rs);
        close(stmt);
        close(connection);
    }

    private void getLog() throws Exception {
        if (log == null) {
            log = new ActionLogModel(mConnection);

        } else if (log.getConnection().isClosed()) {
            log.setConnection(mConnection);
        }
    }

    public void logAction(String userName, String actionName, String module, String description) throws Exception {
        //Init log
        getLog();

        //Build log object
        ActionLog actionLog = new ActionLog();
        actionLog.setActionName(actionName);
        actionLog.setUserName(userName);
        actionLog.setModuleName(module);
        actionLog.setDescription(description);

        //Insert
        log.insertLog(actionLog);
    }

    public void logAction(String actionName, String module, String description) throws Exception {
        //Init log
        getLog();

        //Build log object
        ActionLog actionLog = new ActionLog();
        actionLog.setActionName(actionName);
        actionLog.setUserName(SecUser.getUserLogged().getUserName());
        actionLog.setModuleName(module);
        actionLog.setDescription(description);

        //Insert
        log.insertLog(actionLog);
    }


}
