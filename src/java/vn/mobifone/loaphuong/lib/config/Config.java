/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib.config;

import com.ocpsoft.pretty.PrettyContext;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import vn.mobifone.loaphuong.action.Action;

import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.util.StringUtil;
import vn.mobifone.loaphuong.controller.MenuController;
import vn.mobifone.loaphuong.security.Serializables;

/**
 * Xử lý liên quan đến cấu hình
 *
 * @author ChienDX
 */
public class Config implements Serializables {

    ////////////////////////////////////////////////////////////////////////

    //Kiểm tra xem có trace exception không
    public static boolean isEnableTraceError() {
        return SystemConfig.getConfig("EnableTraceError").equals("1");
    }
    ////////////////////////////////////////////////////////////////////////

    //Lấy url hiện tại
    public static String getCurrentPath() {
        try {
            //Get function path
            String strFuncPath = PrettyContext.getCurrentInstance().getCurrentMapping().getPattern();

            if (strFuncPath.equals("/")) {
                return strFuncPath;
            }

            if (strFuncPath.contains("#")) {
                strFuncPath = strFuncPath.substring(0, strFuncPath.indexOf("#"));

            } else {
                return strFuncPath;
            }

            //Get faces file
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

            //Get param map
            Map<String, String[]> paramMap = request.getParameterMap();

            //Append folder
            String strExtParam = "";
            String strExtParamDefine = SystemConfig.getConfig("ExtParam");

            for (String key : paramMap.keySet()) {
                if (key.contains("folder") && paramMap.get(key)[0] != null && !paramMap.get(key)[0].isEmpty()) {
                    strFuncPath += paramMap.get(key)[0] + "/";
                }

                if (strExtParamDefine.contains(key + ",")) {
                    strExtParam += key + "=" + paramMap.get(key)[0] + "&";
                }
            }

            //Get module facelet
            if (!strExtParam.isEmpty()) {
                strExtParam = "?" + StringUtil.removeLastChar(strExtParam);
            }
            strFuncPath += StringUtil.fix(request.getParameter("moduleName")) + StringUtil.fix(request.getParameter("clientName")) + strExtParam;

            //Return current path
            return strFuncPath;

        } catch (Exception ex) {
            return "";
        }
    }
    ////////////////////////////////////////////////////////////////////////

    //Lấy tên chức năng hiện tại
    public static String getCurrentModuleName() {
       return getCurrentModule().getActionName();
   }
    ////////////////////////////////////////////////////////////////////////

    //Lấy chức năng hiện tại
    public static Action getCurrentModule() {
        try {
            String strCurrentPath = Config.getCurrentPath();
            if (strCurrentPath.contains("?")) {
                strCurrentPath = strCurrentPath.substring(0, strCurrentPath.indexOf("?"));
            }

            List<Action> listModule = MenuController.getListModule();

            if (listModule == null || listModule.isEmpty()) {
                return new Action();
            }

            for (Action tmp : listModule) {
                if (strCurrentPath.equals(tmp.getActionUrl())) {
                    return tmp;
                }
            }

            return new Action();

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }

        return new Action();
    }

}
