/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import vn.mobifone.loaphuong.action.Action;
import vn.mobifone.loaphuong.action.ActionModel;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.TSPermission;
import vn.mobifone.loaphuong.util.DataUtil;
import vn.mobifone.loaphuong.webservice.LoginWebservice;

/**
 * Description: RoleController Class Date: 26/08/2015 14:28:20 Version: 1.0
 *
 * @author: Telsoft
 */
@ManagedBean(name = "roleController")
@ViewScoped
public class RoleController extends TSPermission implements Serializable {

    /**
     * Fields
     */
    private Role mrole;
    private List<Role> mlistRole;
    private ActionModel mactionModel;
    private RoleModel mroleModel;
    private List<Role> mlistRoleFilter;
    private Role[] mlistRoleSelected;
    private List<Action> listModules;
    private List<Long> listModuleSelected;
    private List<Action> listActionSelected;
    private DefaultTreeNode treeModule;
    private boolean isADD = false;
    private boolean isEDIT = false;
    private boolean isVIEW = false;
    private boolean isDELETE = false;
    private TreeNode[] selectedNodes;
    //private List<Long> listModuleIdSelected;
    private List<Boolean> mlistColumn;
    private LoginWebservice loginWS;

    /**
     * Creates a new instance of RoleController
     */
    public RoleController() {
        try {
            //Initial
            mroleModel = new RoleModel();
            mactionModel = new ActionModel();
            loginWS = new LoginWebservice();
            //mlistRole = loginWS.getListRole();
            mlistRole = mroleModel.getListRole();
            listModuleSelected = new ArrayList<>();
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.toString());
        }
    }

    /**
     * build tree module
     */
    public void resetTreeModule() {
        Action rootTN = new Action();
        rootTN.setActionId(listModules.get(0).getActionId());
        rootTN.setActionName("treeNodeRoot");
        //Action rootTN = listModules.get(0);
        treeModule = new DefaultTreeNode(rootTN, null);
        buildTreeModule(listModules, treeModule);
    }

    /**
     * build tree chức năng
     *
     * @param listValue
     * @param parent
     */
    private void buildTreeModule(List<Action> listValue, TreeNode parent) {
        Action treeValue = (Action) parent.getData();
        // Add child - group
        for (Action item : listValue) {

            if (item.getParentId() == treeValue.getActionId()) {
                Action tmpModule = new Action(item);

                TreeNode tmpTN;
                tmpTN = new DefaultTreeNode(tmpModule, parent);
                if (tmpTN.getParent() != null) {
                    tmpTN.getParent().setExpanded(true);
                }

//                if (item.getLevel() <= 3) {
//                    tmpTN.setExpanded(true);
//                }
                if (!listActionSelected.isEmpty() && tmpModule.getPermitCode() != null) {
                    if (permitList.contains(tmpModule.getPermitCode())) {
                        tmpTN.setSelected(true);
                        tmpTN.getParent().setExpanded(true);
                    }
                }
//                    for(Action action: listActionSelected){
//                            if(action.getPermitCode().contains(tmpModule.getPermitCode())){
//                                  for(TreeNode chil: item){
//                                      
//                                  }
//                                 tmpTN.setSelected(true);
//                                 tmpTN.getParent().setExpanded(true);
//                                 break;
//                            }
//                    }
//                   
//                }

                buildTreeModule(listValue, tmpTN);
            }
        }
    }

//    public String getStringDateTime(Date date) {
//        return DateUtil.convertDateTimeToString(date);
//    }
    ////////////////////////////////////////////////////////
    /**
     * Add Role
     *
     * @param evt
     */
    ////////////////////////////////////////////////////////
    public void addRole(ActionEvent evt) {
        try {
            isADD = true;
            isEDIT = false;
            mrole = new Role();
            listModules = mactionModel.getListAction();
            //listModules = new LoginWebservice().getListAction();
            mlistRole = mroleModel.getListRole();
            listActionSelected = new ArrayList<>();
            resetTreeModule();
        } catch (Exception ex) {
            Logger.getLogger(RoleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Methods
    ////////////////////////////////////////////////////////
    /**
     * Edit Role
     *
     * @param role
     *
     * @throws java.lang.Exception
     */
    ////////////////////////////////////////////////////////
    String permitList = "";

    public void editRole(Role role) throws Exception {
        isEDIT = true;
        isADD = false;
        this.mrole = role;
        listModules = mactionModel.getListAction();
        //listModules = new LoginWebservice().getListAction();
        listActionSelected = new LoginWebservice().getListActionByRoleId(mrole.getRoleId());
        permitList = "";
        for (Action action : listActionSelected) {
            permitList += action.getPermitCode();
        }
        resetTreeModule();
    }

    //Methods
    ////////////////////////////////////////////////////////
    /**
     * Prepare Delete Role
     *
     * @param role *
     */
    ////////////////////////////////////////////////////////
    public void preDeleteRole(Role role) {
        isDELETE = true;
        this.mrole = role;
        isEDIT = false;
        isADD = false;

    }

    //Methods
    ////////////////////////////////////////////////////////
    /**
     * Delete Role
     *
     */
    ////////////////////////////////////////////////////////
    public void deleteRole() {
        try {
            //Delete database
            DataUtil.performAction(RoleModel.class, "deleteRole", mrole);
            mlistRole.remove(mrole);
            mlistRoleFilter = null;
            //Message to client
            ClientMessage.logDelete();

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.toString());
        }

    }

    //Methods
    ////////////////////////////////////////////////////////
    /**
     * Save Role
     *
     * @param evt
     */
    ////////////////////////////////////////////////////////
    public void saveRole(ActionEvent evt) {
        try {
            List<Long> listActionId = new ArrayList<>();
            List<Action> listActions = new ArrayList<>();
            List<Long> par = new ArrayList<>();
            for (TreeNode node : selectedNodes) {
                TreeNode nodePar = node.getParent();
                Action parModule = (Action) nodePar.getData();
                if (!par.contains(parModule.getActionId())) {
                    par.add(parModule.getActionId());
                    
                    listActionId.add(parModule.getActionId());
                    listActions.add(parModule);
                }

                Action module = (Action) node.getData();
                listActionId.add(module.getActionId());
                listActions.add(module);
            }
//            //Update database
            if (isADD) {
                DataUtil.performAction(RoleModel.class, "addRole", mrole, listActions);
                //Message to client
                ClientMessage.logAdd();
            } else if (isEDIT) {
                DataUtil.performAction(RoleModel.class, "updateRole", mrole, listActions);
                //Message to client
                ClientMessage.logUpdate();
            }
//            //Set Status
            //mlistRole = loginWS.getListRole();
            mlistRole = mroleModel.getListRole();
            mlistRoleFilter = null;
            handCancel();
//
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.toString());
        }
    }

    //Methods
    public void handCancel() {
        isADD = false;
        isVIEW = false;
        isEDIT = false;
        isADD = false;
    }

    public void expandTree(boolean isExpand) {
        try {
            if (treeModule.getChildCount() != 0) {
                expandChild(isExpand, treeModule);
            }

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.toString());
        }
    }

    /**
     * expandChild
     *
     * @param isExpand
     * @param tn
     */
    private void expandChild(boolean isExpand, TreeNode tn) {
        for (TreeNode tmpTn : tn.getChildren()) {
            tmpTn.setExpanded(isExpand);

            if (tn.getChildCount() != 0) {
                expandChild(isExpand, tmpTn);
            }
        }
    }

    ////////////////////////////////////////////////////////
    /**
     * Back
     *
     * @param evt
     */
    ////////////////////////////////////////////////////////
    public void backRole(ActionEvent evt) {
        mrole = null;
        handCancel();
    }

    public Role getMrole() {
        return mrole;
    }

    public void setMrole(Role role) {
        this.mrole = role;
    }

    public List<Role> getMlistRole() {
        return mlistRole;
    }

    public void setmlistRole(List<Role> listRole) {
        this.mlistRole = listRole;
    }

    public Role[] getMlistRoleSelected() {
        return mlistRoleSelected;
    }

    public void setMlistRoleSelected(Role[] listRoleSelected) {
        this.mlistRoleSelected = listRoleSelected;
    }

    public List<Role> getMlistRoleFilter() {
        return mlistRoleFilter;
    }

    public void setMlistRoleFilter(List<Role> listRoleFilter) {
        this.mlistRoleFilter = listRoleFilter;
    }

    public RoleModel getMroleModel() {
        return mroleModel;
    }

    public void setMroleModel(RoleModel mroleModel) {
        this.mroleModel = mroleModel;
    }

    public DefaultTreeNode getTreeModule() {
        return treeModule;
    }

    public void setTreeModule(DefaultTreeNode treeModule) {
        this.treeModule = treeModule;
    }

    public boolean isIsADD() {
        return isADD;
    }

    public void setIsADD(boolean isADD) {
        this.isADD = isADD;
    }

    public boolean isIsEDIT() {
        return isEDIT;
    }

    public void setIsEDIT(boolean isEDIT) {
        this.isEDIT = isEDIT;
    }

    public boolean isIsVIEW() {
        return isVIEW;
    }

    public void setIsVIEW(boolean isVIEW) {
        this.isVIEW = isVIEW;
    }

    public boolean isIsDELETE() {
        return isDELETE;
    }

    public void setIsDELETE(boolean isDELETE) {
        this.isDELETE = isDELETE;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<Boolean> getMlistColumn() {
        return mlistColumn;
    }

    public void setMlistColumn(List<Boolean> mlistColumn) {
        this.mlistColumn = mlistColumn;
    }

}
