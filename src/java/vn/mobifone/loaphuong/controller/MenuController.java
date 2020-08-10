/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import vn.mobifone.loaphuong.admin.entity.MenuAuthorizator;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;
import vn.mobifone.loaphuong.action.Action;

import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.model.AreaModel;
import vn.mobifone.loaphuong.webservice.LoginWebservice;

/**
 *
 * @author ChienDX
 */
@ManagedBean(name = "MenuController")
@SessionScoped
//Xử lý menu
public class MenuController implements Serializable {

    private List<Action> mlistModule;
    private MenuModel mmenuBar;

    public MenuController() {
        try {
            mlistModule = getListModule();
            
            //Build menu
            filterMenuPermission();
            mmenuBar = new DefaultMenuModel();

            for (Action module : mlistModule) {
                if (module.getParentId() == 1 && ("1").equals(module.getType())) {
                    MenuItem menu = buildMenuItemByETT(module);
                    mmenuBar.addElement(menu);

                } 
                else if (module.getParentId() == 1) {
                    DefaultSubMenu subMenu = buildSubmenuById(mlistModule, module);
                    subMenu.setLabel(module.getActionName());
                    mmenuBar.addElement(subMenu);
                }
            }

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.toString());
        }
    }
    ////////////////////////////////////////////////////////////////////////

    //Lấy danh sách module
    public static List<Action> getListModule() throws Exception {
        try {
            //Kiểm tra chưa có trong session thì mới query DB
            if (Session.getSessionValue(SessionKeyDefine.LIST_MODULE) == null) {
                //List<Module> listModule = DataUtil.getData(MenuGuiModel.class, "getListModule");
                
                List<Action> listModule = (new LoginWebservice()).getListAction();

                //Xóa node root
//                if (listModule != null && !listModule.isEmpty()) {
//                    listModule.remove(0);
//                }

                //Đẩy vào sesion
                Session.setSessionValue(SessionKeyDefine.LIST_MODULE, listModule);
                return (List<Action>) Session.getSessionValue(SessionKeyDefine.LIST_MODULE);

            } else {
                return (List<Action>) Session.getSessionValue(SessionKeyDefine.LIST_MODULE);
            }

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            throw ex;
        }
    }

    //Hàm đệ quy tạo menu động nhiều cấp
    private DefaultSubMenu buildSubmenuById(List<Action> listModule, Action module) throws Exception {
        //Build menu item
        DefaultSubMenu returnValue;
        Action ettSubmenu = null;

        //Tạo menu item từ entity
        for (Action moduleGui : listModule) {
            if (moduleGui.getActionId()== module.getActionId()) {
                ettSubmenu = moduleGui;
                break;
            }
        }
        if (ettSubmenu == null) {
            return null;
        }
        returnValue = buildSubmenuByETT(ettSubmenu);

        //Lấy danh sách module con
        List<Action> tmpList = new ArrayList<>();
        for (Action moduleGui : listModule) {
            if (moduleGui.getParentId() == module.getActionId()) {
                tmpList.add(moduleGui);
            }
        }

        for (Action moduleGui : tmpList) {
            //Nếu là nhóm chức năng thì tạo submenu
            if ("0".equals(moduleGui.getType())) {
                returnValue.addElement(buildSubmenuById(listModule, moduleGui));

            } else {
                //Nếu là chức năng thì tạo menu item
                returnValue.addElement(buildMenuItemByETT(moduleGui));
            }
        }
        return returnValue;
    }
    ////////////////////////////////////////////////////////////////////////

    private void filterMenuPermission() throws Exception {
        //Super admin thì không kiểm tra quyền
        if (SecUser.isSuperAdmin()) {
            return;
        }

        //Kiểm tra không có quyền thì remove
        Map<String, MenuAuthorizator> mapAuth = (Map<String, MenuAuthorizator>) Session.getSessionValue(SessionKeyDefine.MAP_MODULE_AUTH);

        for (int i = 0; i < mlistModule.size(); i++) {
            if ((!mapAuth.containsKey(mlistModule.get(i).getActionUrl())) || (mlistModule.get(i).getActionId() == 7 && Integer.parseInt(SystemConfig.getConfig("viewHouseManagement"))!=1) ) {
                mlistModule.remove(i--);
            }
            
//            if (mlistModule.get(i).getActionId() == 7 && Integer.parseInt(SystemConfig.getConfig("viewHouseManagement"))!=1) {
//                mlistModule.remove(i--);
//            }
        }
        
    }

    //Tạo menu item
    private MenuItem buildMenuItemByETT(Action moduleGUI) throws Exception {
        DefaultMenuItem returnValue = new DefaultMenuItem();
        returnValue.setTitle(moduleGUI.getActionName());
        returnValue.setValue(moduleGUI.getActionName());
        returnValue.setUrl(moduleGUI.getActionUrl());
        //returnValue.setIcon(StringUtil.evl("fa fa-star-o"));
        return returnValue;
    }
    ////////////////////////////////////////////////////////////////////////

    //Tạo submenu
    private DefaultSubMenu buildSubmenuByETT(Action moduleGUI) throws Exception {
        DefaultSubMenu returnValue = new DefaultSubMenu();
        returnValue.setLabel(moduleGUI.getActionName());
       //returnValue.setIcon(StringUtil.evl(moduleGUI.getIcon(), "fa fa-star-o"));
        return returnValue;
    }
    ////////////////////////////////////////////////////////////////////////

    public MenuModel getMmenuBar() {
        return mmenuBar;
    }
}
