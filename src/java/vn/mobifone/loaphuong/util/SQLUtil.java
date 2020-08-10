/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.util;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.dbutils.DbUtils;
import vn.mobifone.loaphuong.lib.SystemLogger;

/**
 * @author ChienDX
 * Util cho sql
 */
public class SQLUtil implements Serializable {
    //Lấy giá trị seqence
    public static long getSequenceValue(Connection cn, String sequenceName) throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // SQL command to sequence value
        String strSQL = "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";

        // Get query data
        try {
            stmt = cn.prepareStatement(strSQL);
            rs = stmt.executeQuery(strSQL);
            rs.next();

            return rs.getLong(1);

        } catch (Exception ex) {
            if (ex.getMessage() != null && ex.getMessage().startsWith("ORA-02289")) {
                SystemLogger.getLogger().error("sequence does not exist: " + sequenceName);
                throw ex;

            } else {
                throw ex;
            }

        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
        }
    }

    //Lấy giá trị seqence gần nhất
    public static long getSequenceValue(Connection cn, String sequenceName, String strTableName, String strField) throws Exception {
        // SQL command to sequence value
        PreparedStatement stmt = null;
        ResultSet rs = null;
        long lLastId;

        // Get query data
        try {
            //Create stmt
            stmt = cn.prepareStatement("SELECT * FROM (SELECT " + strField + " FROM " + strTableName + " ORDER BY " + strField + " DESC) WHERE ROWNUM = 1");

            //Get last id
            rs = stmt.executeQuery();

            lLastId = 0;
            while (rs.next()) {
                lLastId = rs.getLong(1);
                break;
            }
            rs.close();
            stmt.close();

            //Get sequence value
            stmt = cn.prepareStatement("SELECT " + sequenceName + ".NEXTVAL FROM DUAL");
            rs = stmt.executeQuery();
            rs.next();

            long lReturn = rs.getLong(1);
            stmt.close();

            //Check sequence
            if (lLastId >= lReturn) {
                //Update sequence
                stmt = cn.prepareStatement("ALTER sequence " + sequenceName + " increment by " + (lLastId - lReturn + 1) + " nocache");
                stmt.execute();
                stmt.close();

                //Next val
                stmt = cn.prepareStatement("SELECT " + sequenceName + ".NEXTVAL FROM dual");
                stmt.execute();
                stmt.close();

                //Update sequence
                stmt = cn.prepareStatement("ALTER sequence " + sequenceName + " increment by 1 nocache");
                stmt.executeUpdate();
                stmt.close();

                //Return
                return lLastId + 1;
            }

            //Return
            return lReturn;

        } catch (Exception ex) {
            if (ex.getMessage() != null && ex.getMessage().startsWith("ORA-02289")) {
                SystemLogger.getLogger().error("sequence does not exist: " + sequenceName);
                throw ex;

            } else {
                throw ex;
            }

        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
        }
    }
}
