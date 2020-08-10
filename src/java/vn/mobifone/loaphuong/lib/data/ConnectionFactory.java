/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import vn.mobifone.loaphuong.security.EncryptionService;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.util.StringUtil;

/**
 * Xử lý kết nối Database
 * @author ChienDX
 */
public class ConnectionFactory implements Serializable {

    public static OracleConnection mOraclePool;
    public static OracleConnection mOraclePoolClient;
    private static ETT_DBConnect mConnect;

    //Giải mã mật khẩu
    private static String decode(String string) {
        EncryptionService es;
        String strPassword = null;

        try {
            es = new EncryptionService();
            strPassword = es.decrypt(string);

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }

        return strPassword;
    }

    //Lấy thông tin từ file SERVERCONFIG
    private static void getAMConnectConfig() throws Exception {
        String strMax = SystemConfig.getConfig("DBPoolMax");
        String strMin = SystemConfig.getConfig("DBPoolMin");

        mConnect = new ETT_DBConnect();
        mConnect.mstrUserName = SystemConfig.getConfig("DBUser");
        mConnect.mstrPassWord = decode(SystemConfig.getConfig("DBPass"));
        mConnect.mstrURL = SystemConfig.getConfig("DBUrl");
        mConnect.miMaxConnection = StringUtil.isNumberString(strMax) ? Integer.parseInt(SystemConfig.getConfig("DBPoolMax")) : 0;
        mConnect.miMinConnection = StringUtil.isNumberString(strMin) ? Integer.parseInt(SystemConfig.getConfig("DBPoolMin")) : 0;
    }

    //Lấy connection
    public static Connection getConnection() throws Exception {
        getAMConnectConfig();

        if (mOraclePoolClient == null) {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            mOraclePoolClient = new OracleConnection(mConnect.mstrURL, mConnect.mstrUserName, mConnect.mstrPassWord, mConnect.miMaxConnection, mConnect.miMinConnection);
            return mOraclePoolClient.getConnection();

        } else {
            return mOraclePoolClient.getConnection();
        }
    }
    
    
}

class ETT_DBConnect {

    public String mstrURL;
    public String mstrUserName;
    public String mstrPassWord;
    public int miMaxConnection;
    public int miMinConnection;
}
