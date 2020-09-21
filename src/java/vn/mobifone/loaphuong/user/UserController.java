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
import org.primefaces.model.Visibility;

import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.entity.HouseHold;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.TSPermission;
import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.security.Serializables;
import vn.mobifone.loaphuong.util.MD5Encoder;
import vn.mobifone.loaphuong.util.ResourceBundleUtil;
import vn.mobifone.loaphuong.util.ValidatorUtil;

/**
 * Description: UserController Class Date: 28/08/2015 09:26:16 Version: 1.0
 *
 * @author: ChienDX
 */
@ManagedBean(name = "UserController")
@ViewScoped
//Quản lý NSD
public class UserController extends TSPermission implements Serializables {

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
    private List<HouseHold> mlistHouse;
    private boolean allowHouseManagement;

    // Operation Flag -- Enum
    public static enum PersistAction {

        SELECT,
        CREATE,
        DELETE,
        RESET,
        UPDATE
    }

    /**
     * Creates a new instance of UserController
     */
     Random r = new Random(5);
    public UserController() {
        try {
            //Initial
            //allowHouseManagement = Integer.parseInt(SystemConfig.getConfig("viewHouseManagement"))==1?true:false;
            allowHouseManagement = (boolean)Session.getSessionValue(SessionKeyDefine.ALLOW_HOUSE_MANAGEMENT);
            muser = new User();
            muserModel = new UserModel();
            // mlistColumn = Arrays.asList(true, true, true, true, true,  true, false, false,false,false);
            mflag = PersistAction.SELECT;
            //Set Enable
            this.mbView = true;
            mlistUser = muserModel.getAllUserInSystem(0, SecUser.getUserLogged().getAreaId());
            
            mlistUserFilter = null;
           

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ValidatorUtil.getMessageOracleError(ex));
        }
    }
    //Tim kiếm user

//    public void search() {
//        try {
//         mlistUser = muserModel.getAllUserInSystem();
//         mlistUserFilter = null;
//        } catch (Exception ex) {
//            SystemLogger.getLogger().error(ex);
//            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.getMessage());
//        }
//    }
    public void addUser(ActionEvent evt) {
        try {
            mflag = PersistAction.CREATE;
            muser = new User();
            mbView = false;
            muser.setAreaId(SecUser.getUserLogged().getAreaId());
            muser.setHouseHouseHoldId(SecUser.getUserLogged().getHouseHouseHoldId());
            mlistHouse = muserModel.getListHouseByArea(SecUser.getUserLogged().getAreaId());
            mstrDialogHeader = ResourceBundleUtil.getAMObjectAsString("PP_SHARED", "add");
            
            //muser.setPassword((r.nextLong()+"").substring(1, 7));
            muser.setPassword("123456");

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
        muser = new User(user);
        
        muser.setType(0);
        muser.addRole(4);
        muser.setPassword("");
        mstrDialogHeader = ResourceBundleUtil.getAMObjectAsString("PP_SHARED", "edit");
        try {
            muser.setAreaId(user.getAreaId());
            muser.setHouseHouseHoldId(user.getHouseHouseHoldId());
            mlistHouse = muserModel.getListHouseByArea(SecUser.getUserLogged().getAreaId());
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ValidatorUtil.getMessageOracleError(ex));
        }
    }
    ////////////////////////////////////////////////////////

    public void viewUser(User user) {
//        mflag = PersistAction.SELECT;
//        mbView = false;
//        this.muser = user;
//        mstrDialogHeader = "Thông tin chi tiết";
//        try {
//            mlistRole = roleModel.getAll();
//            mlistRole.add(new Role(SecUser.getUserLogged().getRoleId(),SecUser.getUserLogged().getRoleName()));
//            for (Role role : mlistRole) {
//                if (role.getRoleId() == user.getRoleId()) {
//                    mrole = roleModel.getRoleByIdForUser(role, user.getUserName());
//                    break;
//                }
//            }
//        } catch (Exception ex) {
//            SystemLogger.getLogger().error(ex);
//            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ValidatorUtil.getMessageOracleError(ex));
//        }
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
            mlistUser = muserModel.getAllUserInSystem(0, SecUser.getUserLogged().getAreaId());
            mlistUserFilter = null;
            this.muser = new User();
        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Methods
    ////////////////////////////////////////////////////////
    /**
     * Click Đồng ý lưu thông tin User
     *
     * @param evt
     */
    ////////////////////////////////////////////////////////
    public void saveUser() {
        try {
//            //Update database
            if (mflag == PersistAction.CREATE) {
                muserModel.addUser(muser);
                //Message to client
                ClientMessage.logAdd();

            } else if (mflag == PersistAction.UPDATE) {
                muserModel.editUser(muser);
                //             roleModel.updateRoleOfUser(mrole,muser.getUserName(), muser.getOrgId());
                //Message to client
                ClientMessage.logUpdate();
            }

//            //Set Status
            mlistUserFilter = null;
//            orgSelect = muser.getOrgId();
//            search();
            mbView = true;
            mflag = PersistAction.SELECT;
//
        } catch (Exception ex) {

            SystemLogger.getLogger().error(ex, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ValidatorUtil.getMessageOracleError(ex));
        }

    }

    public void savelPNormalUser() {
        try {
            
            DataResponse response = new DataResponse();
            muser.setType(0);
            muser.setRoles(new ArrayList<Long>());
            muser.addRole(4);
            if (muser.getHouseHouseHoldId() != null) {
                if (muser.getHouseHouseHoldId()==0) {
                    muser.setHouseHouseHoldId(null);
                    
                }
            }

           
            if (null != mflag) {
                switch (mflag) {
                    case CREATE:
                        
                        muser.setPassword(muser.getPassword()==null?"":muser.getPassword());
                        muserModel.addUser(muser);
                        //ClientMessage.logAdd();
                        break;
                    case UPDATE:
                        muser.setPassword("");
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
            mlistUser = muserModel.getAllUserInSystem(0, SecUser.getUserLogged().getAreaId());
            mlistUserFilter = null;

        } catch (Exception ex) {
          //  Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
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
//    public boolean isAllowImport(){
//        return SecUser.getUserLogged().getShopCode()!= null && SecUser.getUserLogged().getAmCode() != null;
//    }
    public boolean allowUpdateUser(){
        return super.isIsAllowUpdate("EDIT_NUSER");
    }
//     public boolean isAllowView(){
//        return super.isIsAllowView()|| super.isIsAllowViewExtend();
//    }
    public boolean allowDeleteUser(){
        return super.isIsAllowDelete("DELETE_NUSER");
    }
    public boolean allowInsertUser(){
        return super.isIsAllowInsert("ADD_NUSER");
    }
//    public boolean isAllowUpdate(User user){
//        return super.isIsAllowUpdateExtend() || (super.isIsAllowUpdate()&& user.getRoleId() != 3 && SecUser.getUserLogged().getRoleId() == 3) ;
//    }
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

    public List<HouseHold> getMlistHouse() {
        return mlistHouse;
    }

    public void setMlistHouse(List<HouseHold> mlistHouse) {
        this.mlistHouse = mlistHouse;
    }

    public boolean isAllowHouseManagement() {
        return allowHouseManagement;
    }

}
