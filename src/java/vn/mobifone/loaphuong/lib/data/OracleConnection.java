/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import oracle.jdbc.pool.OracleDataSource;

//Connection Pool
public class OracleConnection implements Serializable {

//    private OracleDataSource mods = null;
    private OracleConnectionPoolDataSource mods = null;
    //Khởi tạo pool
    public OracleConnection(String strUrl, String strUserName, String strPassword, int iMaxConnection) throws Exception {
        this(strUrl, strUserName, strPassword, iMaxConnection, 0);
    }

    //Khởi tạo pool
    public OracleConnection(String strUrl, String strUserName, String strPassword, int iMaxConnection, int iMinConnection) throws Exception {
        //mods = createConnectionPool(strUrl, strUserName, strPassword, iMaxConnection, iMinConnection);
        mods = createPoolDataSource(strUrl, strUserName, strPassword, iMinConnection);
    }

    private OracleConnectionPoolDataSource createPoolDataSource(String strUrl, String strUserName, String strPassword, int iMinConnection) throws Exception {
        OracleConnectionPoolDataSource ocpds = new OracleConnectionPoolDataSource();
        ocpds.setURL(strUrl);
        ocpds.setUser(strUserName);
        ocpds.setPassword(strPassword);
        Properties prop = new Properties();
        prop.setProperty("MinLimit", String.valueOf(iMinConnection));
        ocpds.setConnectionCacheProperties(prop);
        return ocpds;
    }
    
    //Tạo pool
    private static OracleDataSource createConnectionPool(String strUrl, String strUserName, String strPassword, int iMaxConnection, int iMinConnection) throws SQLException {
        OracleDataSource ds = new OracleDataSource();
        
        Properties prop = new Properties();
        prop.setProperty("MinLimit", String.valueOf(iMinConnection));
        prop.setProperty("MaxLimit", String.valueOf(iMaxConnection));

        ds.setURL(strUrl);
        ds.setUser(strUserName);
        ds.setPassword(strPassword);
        ds.setConnectionCachingEnabled(true);
        ds.setConnectionCacheProperties (prop);
        ds.setConnectionCacheName("MbfQtcpImplicitCache");
        
        return ds;
    }

    public Connection getConnection() throws SQLException {
        return mods.getConnection();
    }

    public void close() throws SQLException {
        mods.close();
    }

    @Override
    protected void finalize() throws Throwable {
        mods.close();        
        super.finalize();
    }
}
