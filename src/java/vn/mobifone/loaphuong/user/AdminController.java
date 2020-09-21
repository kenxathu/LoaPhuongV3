/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.Visibility;

import vn.mobifone.loaphuong.controller.DocumentService;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.TSPermission;
import vn.mobifone.loaphuong.role.Role;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.security.Serializables;
import vn.mobifone.loaphuong.util.ResourceBundleUtil;
import vn.mobifone.loaphuong.util.ValidatorUtil;
import vn.mobifone.loaphuong.webservice.AreaWebservice;
import vn.mobifone.loaphuong.webservice.LoginWebservice;

/**
 * Description: adminController Class Date: 28/08/2015 09:26:16 Version: 1.0
 *
 * @author: ChienDX
 */
@ManagedBean(name = "adminController")
@ViewScoped
//Quản lý NSD
public class AdminController extends TSPermission implements Serializables {

    /**
     * Fields
     */
    private User muser;
    private List<User> mlistUser;
    private UserModel muserModel;
    private List<User> mlistUserFilter;
    private boolean mbView;
    private PersistAction mflag;
    private String mstrDialogHeader;
    private List<Boolean> mlistColumn;
    private long orgSelect;
    private List<Role> mlistRole;
    private Long[] selectRoles;
    private List<AreaTree> mlistArea;
    private AreaWebservice areaWebservice;
    
    private AreaTree areaRoot;
    private AreaTree selectedArea;
    private DocumentService service;
    private boolean isCityMode = true;
    // private mlistRole

    // Operation Flag -- Enum
    Random r = new Random(5);
    public static enum PersistAction {

        SELECT,
        CREATE,
        DELETE,
        UPDATE,
        RESET
    }

    /**
     * Creates a new instance of UserController
     */
    public AdminController() {
        try {
            //Initial
            muser = new User();
            muser.setName(mstrDialogHeader);
            muserModel = new UserModel();
            // mlistColumn = Arrays.asList(true, true, true, true, true,  true, false, false,false,false);
            mflag = PersistAction.SELECT;
            //Set Enable
            this.mbView = true;
            mlistUser = muserModel.getAllUserInSystem(1, SecUser.getUserLogged().getAreaId());

            mlistUserFilter = null;
            areaWebservice = new AreaWebservice();
            
            service = new DocumentService();
            areaRoot = service.createAreaTree();
            isCityMode = Integer.parseInt(SystemConfig.getConfig("areaMode"))==2?true:false;
            //areaRoot.getChildren().get(0).setSelected(true);
            //areaRoot.getChildren().get(0).setExpanded(true);
            //selectedArea = (AreaTree) areaRoot.getChildren().get(0);

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ValidatorUtil.getMessageOracleError(ex));
        }
    }
    //Tim kiếm user

    public void reset() {
        try {
            mlistUser = muserModel.getAllUserInSystem(1, SecUser.getUserLogged().getAreaId());
            mlistUserFilter = null;
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.getMessage());
        }
    }
    
    public void resetTreeState(TreeNode node) {
        node.setSelected(false);
        node.setExpanded(false);
        for (TreeNode child : node.getChildren()) {
            resetTreeState(child);
        }
    }
    
    public void setAllParentExpanded(TreeNode node) {
        node.setExpanded(true);
        if (node.getParent() != null) {
            setAllParentExpanded(node.getParent());
        }
    }
    
    public void resetTree(TreeNode node, long selectedId) {
        for (TreeNode treeNode : node.getChildren()) {
                    if (((AreaTree)treeNode).getAreaId() == selectedId) {
                        treeNode.setSelected(true);
                        setAllParentExpanded(treeNode);
                        selectedArea = (AreaTree)treeNode;
                        return;
                    } 
            resetTree(treeNode, selectedId);
        }
    }

    public void addUser(ActionEvent evt) {
        try {
            mflag = PersistAction.CREATE;
            mbView = false;
            muser = new User();
            muser.setType(1);
           // muser.setAreaId(SecUser.getUserLogged().getAreaId());
            muser.setHouseHouseHoldId(0L);
            mlistRole = new LoginWebservice().getListRole();
            if (!SecUser.isSuperAdmin() && mlistRole.size()>0) {
                mlistRole.remove(0);
            }
            mlistArea = areaWebservice.getListArea(SecUser.getUserLogged().getAreaId());
            muser.setPassword("123456");
//            muser.setPassword((r.nextLong()+"").substring(1, 6));
//            for (Role r : mlistRole) {
//                if (r.getRoleId() == 4) {
//                    mlistRole.remove(r);
//                    break;
//                }
//            }
            selectRoles = null;
            
            mstrDialogHeader = ResourceBundleUtil.getAMObjectAsString("PP_SHARED", "add");
            resetTreeState(areaRoot);
            areaRoot.getChildren().get(0).setExpanded(true);
            selectedArea = null;

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ValidatorUtil.getMessageOracleError(ex));
        }
    }

    //Methods
    ////////////////////////////////////////////////////////
    /**
     * Click Sửa User
     *
     * @param user *
     */
    ////////////////////////////////////////////////////////
    public void editUser(User user) {
        mflag = PersistAction.UPDATE;
        mbView = false;
        mstrDialogHeader = ResourceBundleUtil.getAMObjectAsString("PP_SHARED", "edit");
        try {
            muser = muserModel.getUserDetail(user.getUserId());
            muser.setType(1);
            muser.setHouseHouseHoldId(0L);
            mlistRole = new LoginWebservice().getListRole();
            if (!SecUser.isSuperAdmin() && mlistRole.size()>0) {
                mlistRole.remove(0);
            }
            for (Role r : mlistRole) {
                if (r.getRoleId() == 4) {
                    mlistRole.remove(r);
                    break;
                }
            }
            // Set select role
            if (muser.getRoles() != null && muser.getRoles().size() > 0) {
                selectRoles = new Long[muser.getRoles().size()];
                for (int i = 0; i < muser.getRoles().size(); i++) {
                    selectRoles[i] = muser.getRoles().get(i);
                }
            }
            
            //mlistArea = areaWebservice.getListArea(SecUser.getUserLogged().getAreaId());
            resetTreeState(areaRoot);
            resetTree(areaRoot, muser.getAreaId());

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ValidatorUtil.getMessageOracleError(ex));
        }
    }
    ////////////////////////////////////////////////////////

    public void viewUser(User user) {
        mflag = PersistAction.SELECT;
        mbView = false;
        //this.muser = new User(user);
        mstrDialogHeader = "Thông tin chi tiết";

        try {
            muser = muserModel.getUserDetail(user.getUserId());
            mlistRole = new LoginWebservice().getListRole();
            if (!SecUser.isSuperAdmin() && mlistRole.size()>0) {
                mlistRole.remove(0);
            }
            mlistArea = areaWebservice.getListArea(muser.getAreaId());
            for (Role r : mlistRole) {
                if (r.getRoleId() == 4) {
                    mlistRole.remove(r);
                    break;
                }
            }
            // Set select role
            if (muser.getRoles() != null && muser.getRoles().size() > 0) {
                selectRoles = new Long[muser.getRoles().size()];
                for (int i = 0; i < muser.getRoles().size(); i++) {
                    selectRoles[i] = muser.getRoles().get(i);
                }
            }
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ValidatorUtil.getMessageOracleError(ex));
        }
    }

    //Methods
    ////////////////////////////////////////////////////////
    /**
     * Click Xóa User
     *
     * @param user *
     */
    ////////////////////////////////////////////////////////
    public void preDeleteUser(User user) {
        mflag = PersistAction.DELETE;
        this.muser = user;

    }
    
    public void preResetPass(User user) {
        mflag = PersistAction.RESET;
        this.muser = user;

    }
    ////////////////////////////////////////////////////////

    public void preCancel() {
        try {
            mflag = PersistAction.SELECT;
            reset();
            this.muser = new User();
        } catch (Exception ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Methods
    ////////////////////////////////////////////////////////
    /**
     * Click Đồng ý lưu thông tin User
     *
     * @param evt
     */
    public void saveUser() {
        try {
            DataResponse response = new DataResponse();
            muser.setRoles(new ArrayList<Long>());
            if (selectRoles != null && selectRoles.length > 0) {
                for (int i = 0; i < selectRoles.length; i++) {
                    muser.addRole(selectRoles[i]);
                }
            }
            
           
            if (null != mflag) {
                switch (mflag) {
                    case CREATE:
                        muser.setAreaId(selectedArea.getAreaId());
                        muserModel.addUser(muser);
                        // Gui mail toi nguoi dung la quan tri
                        // EmailUtils.sendMail(null, "tho.nt", "");
                        //ClientMessage.logAdd();
                        break;
                    case UPDATE:
                        muser.setAreaId(selectedArea.getAreaId());
                        muserModel.editUser(muser);
                        // ClientMessage.logUpdate();
                        break;
                    case DELETE:
                        muserModel.deleteUser(muser.getUserId());
                        //ClientMessage.logDelete();
                        break;
                    case RESET:
                        muserModel.resetUserPass(muser.getUserId());
                        //ClientMessage.logDelete();
                        break;
                    default:
                        break;
                }
            }
            reset();

        } catch (Exception ex) {
       //     Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void eventChangeRole() {
//        try {
//            for (Role r : mlistRole) {
//                if (muser.getRoleId() == r.getRoleId()) {
//                    mrole.setRoleId(r.getRoleId());
//                    mrole = roleModel.getRoleByIdForUser(r, muser.getUserName());
//                    break;
//                }
//            }
//        } catch (Exception ex) {
//            SystemLogger.getLogger().error(ex);
//            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ValidatorUtil.getMessageOracleError(ex));
//        }
    }
   // set quyen cho các trang
    public boolean isAllowUpdateAdmin(){
        return super.isIsAllowUpdate("EDIT_USER") ;
    }
    public boolean isAllowInsertAdmin(){
        return super.isIsAllowInsert("ADD_USER") ;
    }
    public boolean isAllowDeleteAdmin(){
        return super.isIsAllowUpdate("DELETE_USER") ;
    }

    //Methods
    ////////////////////////////////////////////////////////

    /**
     * Quay lại
     *
     * @param evt
     */
    ////////////////////////////////////////////////////////
    public void backUser(ActionEvent evt) {
        mflag = PersistAction.SELECT;
        muser = null;

        mbView = true;
    }
    ////////////////////////////////////////////////////////////////////////

    public long getOrgSelect() {
        return orgSelect;
    }

    public void setOrgSelect(long orgSelect) {
        this.orgSelect = orgSelect;
    }

    public void onToggle(ToggleEvent e) {
        mlistColumn.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    //Getter and Setter
    public PersistAction getMflag() {
        return mflag;
    }

    public void setMflag(PersistAction mflag) {
        this.mflag = mflag;
    }

    public boolean isMbView() {
        return mbView;
    }

    public void setMbView(boolean bView) {
        this.mbView = bView;
    }

    public User getMuser() {
        return muser;
    }

    public void setMuser(User user) {
        this.muser = user;
    }

    public List<User> getMlistUser() {
        return mlistUser;
    }

    public void setmlistUser(List<User> listUser) {
        this.mlistUser = listUser;
    }

    public List<User> getMlistUserFilter() {
        return mlistUserFilter;
    }

    public void setMlistUserFilter(List<User> listUserFilter) {
        this.mlistUserFilter = listUserFilter;
    }

    public String getMstrDialogHeader() {
        return mstrDialogHeader;
    }

    public List<Role> getMlistRole() {
        return mlistRole;
    }

    public void setMlistRole(List<Role> mlistRole) {
        this.mlistRole = mlistRole;
    }

    public Long[] getSelectRoles() {
        return selectRoles;
    }

    public void setSelectRoles(Long[] selectRoles) {
        this.selectRoles = selectRoles;
    }

    public List<AreaTree> getMlistArea() {
        return mlistArea;
    }

    public void setMlistArea(List<AreaTree> mlistArea) {
        this.mlistArea = mlistArea;
    }

    public AreaTree getAreaRoot() {
        return areaRoot;
    }

    public void setAreaRoot(AreaTree areaRoot) {
        this.areaRoot = areaRoot;
    }

    public AreaTree getSelectedArea() {
        return selectedArea;
    }

    public void setSelectedArea(AreaTree selectedArea) {
        this.selectedArea = selectedArea;
    }

    public boolean isIsCityMode() {
        return isCityMode;
    }
    
}
