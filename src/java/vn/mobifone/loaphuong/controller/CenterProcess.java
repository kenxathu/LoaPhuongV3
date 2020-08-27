/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import vn.mobifone.loaphuong.admin.entity.MenuAuthorizator;

import vn.mobifone.loaphuong.security.Authorizator;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.config.Config;
import vn.mobifone.loaphuong.util.ResourceBundleUtil;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import vn.mobifone.loaphuong.action.Action;

@ManagedBean(name = "centerprocess")
@ViewScoped
//Xử lý đường dẫn, quyền để hiển thị chức năng
public class CenterProcess implements Serializable {

    /**
     *
     */
    private String mstrPath;
    private DefaultMenuModel mbreadcrumb;

    private enum Side {

        CLIENT, ADMIN
    }

    //Hàm khởi tạo
    public CenterProcess() throws Exception {
        try {
            //Nếu không tồn tại sesion thì bỏ qua hết
            if (SecUser.getUserLogged() == null) {
               // return ;
            }

            //Lấy danh sách quyền
            //Kiểm tra nếu trong session chưa có danh sách quyền thì mới tạo
            if (Session.getSessionValue(SessionKeyDefine.MAP_MODULE_AUTH) == null) {
                List<MenuAuthorizator> listAuth = Authorizator.getListModuleAuthorization();
                Map<String, MenuAuthorizator> mapAuth = new HashMap<String, MenuAuthorizator>();

                Session.setSessionValue(SessionKeyDefine.ALLOW_HOUSE_MANAGEMENT, false);
                for (MenuAuthorizator menuAuth : listAuth) {
                    mapAuth.put(menuAuth.getModulePath(), menuAuth);
                    if (menuAuth.getModulePath().equals("/client/holdhouse-management")) {
                        Session.setSessionValue(SessionKeyDefine.ALLOW_HOUSE_MANAGEMENT, true);
                    }
                }
                
                if (SecUser.isSuperAdmin()) {
                    Session.setSessionValue(SessionKeyDefine.ALLOW_HOUSE_MANAGEMENT, true);
                }
                
                Session.setSessionValue(SessionKeyDefine.MAP_MODULE_AUTH, mapAuth);
            }

            //Lấy đường dẫn chức năng
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String strModuleName = request.getParameter("moduleName");
            String strClientName = request.getParameter("clientName");
            String strFolder1 = request.getParameter("folder1");
            String strFolder2 = request.getParameter("folder2");
            String strFolder3 = request.getParameter("folder3");
            String strFolder4 = request.getParameter("folder4");

            if (strModuleName == null && strClientName == null) {
                mstrPath = "/modules/centers/centermain.xhtml";
               //  mstrPath = "/modules/admin/user.xhtml";
              // mstrPath = "/modules/client/contract.xhtml";
            } else if (strClientName != null) {
                mstrPath = checkAuthorization(strClientName, strFolder1, strFolder2, strFolder3, strFolder4, Side.CLIENT);

            } else {
                mstrPath = checkAuthorization(strModuleName, strFolder1, strFolder2, strFolder3, strFolder4, Side.ADMIN);
            }

            //Tạo BreadCrumb
            buildBreadcrumb();

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.toString());
        }
    }

// Kiểm tra trang hiện tại có phải là trang chủ hay không
//    public boolean isIsHome() {
//        return mstrPath.contains("centermain.xhtml");
//    }
    
    public void onTabChange(TabChangeEvent event) {
        String tab = event.getTab().getTitle();
        switch (tab){
            case "Thông tin địa bàn":
                buildBreadcrumbForTab("Trang chủ||Quản lý||Quản lý địa bàn");
                break;
            case "Hộ gia đình":
                buildBreadcrumbForTab("Trang chủ||Quản lý||Quản lý hộ gia đình");
                break;
            default:
                buildBreadcrumbForTab("Trang chủ||Quản lý||Quản lý thiết bị");
                break;
                
        }
    }
    
    private void buildBreadcrumbForTab(String breadCrumb) {
        mbreadcrumb = new DefaultMenuModel();

        String[] listBreadCrumbItem = StringUtils.split(breadCrumb, "||");
        for (String item : listBreadCrumbItem) {
            DefaultMenuItem menuItem = new DefaultMenuItem();
            menuItem.setValue(item);
            menuItem.setUrl("javascript:void(0)");
            mbreadcrumb.addElement(menuItem);
        }

    }

    //Tạo BreadCrumb
    private void buildBreadcrumb() {
        mbreadcrumb = new DefaultMenuModel();
        Action currentModule = Config.getCurrentModule();

        if (currentModule.getBreadCrumb() != null) {
            String[] listBreadCrumbItem = StringUtils.split(currentModule.getBreadCrumb(), "||");
            for (String item : listBreadCrumbItem) {
                DefaultMenuItem menuItem = new DefaultMenuItem();
                menuItem.setValue(item);
                menuItem.setUrl("javascript:void(0)");
                mbreadcrumb.addElement(menuItem);
            }

        } else {
            DefaultMenuItem menuItem = new DefaultMenuItem();
            menuItem.setValue("Index");
            menuItem.setUrl("javascript:void(0)");
            mbreadcrumb.addElement(menuItem);

            menuItem = new DefaultMenuItem();
            menuItem.setValue(ResourceBundleUtil.getAMObjectAsString("PP_SHARED", "home"));
            menuItem.setUrl("javascript:void(0)");
            mbreadcrumb.addElement(menuItem);
        }
    }

    //Ghép folder vào đường dẫn
     private String appendRealPath(String folder1, String folder2, String folder3, String folder4, String strModuleName) {
        String strReturn
                = strModuleName.equalsIgnoreCase("area-management")
                || strModuleName.equalsIgnoreCase("holdhouse-management")
                || strModuleName.equalsIgnoreCase("camera-management")
                || strModuleName.equalsIgnoreCase("mcu-management") ? "area-management" : strModuleName;

        if (folder4 != null) {
            strReturn = folder4 + "/" + strReturn;
        }
        if (folder3 != null) {
            strReturn = folder3 + "/" + strReturn;
        }
        if (folder2 != null) {
            strReturn = folder2 + "/" + strReturn;
        }
        if (folder1 != null) {
            strReturn = folder1 + "/" + strReturn;
        }
        return strReturn;
    }
    //Ghép folder vào đường dẫn
    private String appendPath(String folder1, String folder2, String folder3, String folder4, String strModuleName) {
        String strReturn = strModuleName;
        
        if (folder4 != null) {
            strReturn = folder4 + "/" + strReturn;
        }
        if (folder3 != null) {
            strReturn = folder3 + "/" + strReturn;
        }
        if (folder2 != null) {
            strReturn = folder2 + "/" + strReturn;
        }
        if (folder1 != null) {
            strReturn = folder1 + "/" + strReturn;
        }
        return strReturn;
    }

    //Kiểm tra quyền
    private String checkAuthorization(String strModulePath, String folder1, String folder2, String folder3, String folder4, Side side) throws Exception {
        String strTmpPath = "";String strTmpPath2 = "";
        String strdisPath = appendPath(folder1, folder2, folder3, folder4, strModulePath);
        strModulePath      = appendRealPath(folder1, folder2, folder3, folder4, strModulePath);

        switch (side) {
            case CLIENT: {
                strTmpPath = "/client/" + strModulePath;
                strTmpPath2 = "/client/" + strdisPath;
                break;
            }
            case ADMIN: {
                strTmpPath = "/module/" + strModulePath;
                strTmpPath2 = "/module/" + strdisPath;
                break;
            }
        }

        String strRealPath = "";
        Map<String, MenuAuthorizator> mapAuth = (Map<String, MenuAuthorizator>) Session.getSessionValue(SessionKeyDefine.MAP_MODULE_AUTH);

        if (SecUser.isSuperAdmin() || strModulePath.equals("user-info") || strTmpPath.equals("/") || mapAuth.containsKey(strTmpPath) || mapAuth.containsKey(strTmpPath2)) {
            switch (side) {
                case CLIENT: {
                    strRealPath = "/modules/client/" + strModulePath + ".xhtml";
                    break;
                }
                case ADMIN: {
                    strRealPath = "/modules/admin/" + strModulePath + ".xhtml";
                    break;
                }
            }
        }

        if (strRealPath.isEmpty()) {
            return "/modules/centers/permission.xhtml";
        }

        //Kiểm tra file xhtml có tồn tại chưa
        ServletContext context2 = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        File f = new File(context2.getRealPath(strRealPath));
        if (!f.exists()) {
            return "/modules/centers/nofacelet.xhtml";

        } else {
            return strRealPath;
        }
    }

    public String getMstrPath() {
        return mstrPath;
    }

    public DefaultMenuModel getMbreadcrumb() {
        return mbreadcrumb;
    }
}
