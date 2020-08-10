/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import org.primefaces.event.SelectEvent;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.util.DataUtil;
import vn.mobifone.loaphuong.util.StringUtil;

/**
 *
 * @author TungLM
 */
@ManagedBean(name = "actionController")
@ViewScoped
public class ActionController implements Serializable {

    public Action ett;
    public List<Action> mlistActionValue;
    private DefaultTreeNode mtreeActionValues;
    private DefaultTreeNode mselectedActionValues;
    private Action ett_selected;
    private Action ett_backup;
    private List<Action> cboParent;
    private PersistAction mflag;
    private boolean enableType;
    private int checkAction;
    private Action ettFilter;
    private DefaultTreeNode nodeActionSelect;
    private List<Permit> listPermit;
    private Permit mpermit;

    public static enum PersistAction {

        SELECT,
        CREATE,
        DELETE,
        UPDATE
    }

    /**
     * Creates a new instance of ActionController
     */
    public ActionController() {
        try {
            mselectedActionValues = new DefaultTreeNode();
            cboParent = new ArrayList<>();
            resetTreeAction();
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.getMessage());
        }
    }

    /**
     * resetTreeAction
     */
    private void resetTreeAction() {
        try {
            mlistActionValue = DataUtil.getData(ActionModel.class, "getActionTree");
            //Session.setSessionValue("action.MlistActionValue", mlistActionValue);
            Action rootTN = new Action();
            rootTN.setActionId(mlistActionValue.get(0).getActionId());
            rootTN.setActionName("treeNodeRoot");

            mtreeActionValues = new DefaultTreeNode(rootTN, null);
            buildTreeAction(mlistActionValue, mtreeActionValues);
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.getMessage());
        }
    }

    /**
     * completeCategoryFilter
     *
     * @param query
     * @return List - Group
     * @throws Exception
     */
    public List<Action> completeActionFilter(String query) throws Exception {
        List<Action> filteredOrg = new ArrayList<Action>();
        for (Action skin : mlistActionValue) {
            if ((skin.getActionName()) != null && StringUtil.removeSign((skin.getActionName()).trim().toLowerCase()).contains(StringUtil.removeSign(query.trim().toLowerCase()))) {
                filteredOrg.add(skin);
            }
        }
        return filteredOrg;
    }

    /**
     * searchNodeCategory
     *
     * @param treeNode
     */
    public void searchNodeCategory(DefaultTreeNode treeNode) {
        for (TreeNode children : treeNode.getChildren()) {
            Action org = (Action) children.getData();
            if ((org.getActionName()).equals(ettFilter.getActionName())) {
                children.setSelected(true);
                nodeActionSelect = (DefaultTreeNode) children;
            }
            searchNodeCategory((DefaultTreeNode) children);
        }
    }

    /**
     * searchCategory
     *
     * @param event
     * @throws Exception
     */
    public void searchCategory(SelectEvent event) throws Exception {
        try {
            if (nodeActionSelect != null) {
                nodeActionSelect.setSelected(false);
            }
            searchNodeCategory(mtreeActionValues);
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.getMessage());
        }
    }

    /**
     * buildTreeAction
     *
     * @param listValue
     * @param parent
     */
    private void buildTreeAction(List<Action> listValue, TreeNode parent) {
        Action treeValue = (Action) parent.getData();
        // Add child - group
        for (Action item : listValue) {
            if (item.getParentId() == treeValue.getActionId()) {
                Action tmpAction = new Action(item);

                TreeNode tmpTN;
                tmpTN = new DefaultTreeNode(tmpAction, parent);
                tmpTN.setExpanded(true);
                buildTreeAction(listValue, tmpTN);
            }
        }
    }

    /**
     * edit
     *
     * @param nodeSelect - Sửa chuc năng
     * @throws Exception
     */
    public void edit(Action nodeSelect) throws Exception {
        try {
            mflag = PersistAction.UPDATE;
            checkAction = 1;
            ett_selected = new Action(nodeSelect);
            listPermit = DataUtil.getData(ActionModel.class, "getPermitByActionId", nodeSelect.getActionId());
            if (listPermit.size() == 0) {
                listPermit.add(new Permit(5, "Xem", false, "VIEW_"));
                listPermit.add(new Permit(1, "Thêm", false, "INSERT_"));
                listPermit.add(new Permit(2, "Sửa", false, "EDIT_"));
                listPermit.add(new Permit(3, "Xóa", false, "DELETE_"));
                listPermit.add(new Permit(4, "Duyệt", false, "APPROVAL_"));
            }
            ett_selected.setListPermit(listPermit);
            cboParent = DataUtil.getData(ActionModel.class, "loadComboParent");
            enableType = (Boolean) DataUtil.performAction(ActionModel.class, "checkNode", ett_selected.getActionId());
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.getMessage());
        }
    }

    /**
     * beforeDelete
     *
     * @param nodeSelect
     * @throws Exception
     */
    public void beforeDelete(Action nodeSelect) throws Exception {
        try {
            mflag = PersistAction.DELETE;
            ett_selected = new Action(nodeSelect);
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.getMessage());
        }
    }

    /**
     * beforeAdd them chuc nang
     */
    public void beforeAdd() {
        try {
            mflag = PersistAction.CREATE;
            checkAction = 0;
            ett_selected = new Action();
            mpermit = new Permit();
            if (listPermit == null) {
                listPermit = new ArrayList<>();
            } else {
                listPermit.clear();
            }
            listPermit.add(new Permit(5, "Xem", false, "VIEW_"));
            listPermit.add(new Permit(1, "Thêm", false, "INSERT_"));
            listPermit.add(new Permit(2, "Sửa", false, "EDIT_"));
            listPermit.add(new Permit(3, "Xóa", false, "DELETE_"));
            listPermit.add(new Permit(4, "Duyệt", false, "APPROVAL_"));

            ett_selected.setListPermit(listPermit);
            cboParent = DataUtil.getData(ActionModel.class, "loadComboParent");
            enableType = false;

        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.toString());
        }
    }

    /**
     * submitDelete
     */
    public void submitDelete() {
        try {
            String strReturn = DataUtil.getStringData(ActionModel.class, "deleteAction", ett_selected.getActionId());
            if (strReturn.equals("false")) {
                ClientMessage.logErr("Tồn tại dữ liệu liên quan !");
            } else {
                ClientMessage.logDelete();
                resetTreeAction();
            }
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.toString());
        }
    }

    /**
     * submitOk
     */
    public void submitOk() {

        try {
            if (mflag == PersistAction.UPDATE) {
                DataUtil.performAction(ActionModel.class, "editAction", ett_selected);
                ClientMessage.logUpdate();
            }
            if (mflag == PersistAction.CREATE) {
                DataUtil.performAction(ActionModel.class, "addAction", ett_selected);
                ClientMessage.logAdd();
            }
            resetTreeAction();
            RequestContext.getCurrentInstance().execute("PF('ActionDialog').hide();");
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, ex.toString());
        }
    }

    /**
     * expandTree
     *
     * @param isExpand
     */
    public void expandTree(boolean isExpand) {
        try {
            if (mtreeActionValues.getChildCount() != 0) {
                expandChild(isExpand, mtreeActionValues);
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

    public void addPermit(Permit p) {
        if (ett == null) {
            ett = new Action();
        }
        this.ett.getListPermit().add(p);

    }
    //Getter and Setter

    public Action getEtt_selected() {
        return ett_selected;
    }

    public void setEtt_selected(Action ett_selected) {
        this.ett_selected = ett_selected;
    }

    public Action getEtt() {
        return ett;
    }

    public void setEtt(Action ett) {
        this.ett = ett;
    }

    public List<Action> getMlistActionValue() {
        return mlistActionValue;
    }

    public void setMlistActionValue(List<Action> mlistActionValue) {
        this.mlistActionValue = mlistActionValue;
    }

    public DefaultTreeNode getMtreeActionValues() {
        return mtreeActionValues;
    }

    public void setMtreeActionValues(DefaultTreeNode mtreeActionValues) {
        this.mtreeActionValues = mtreeActionValues;
    }

    public DefaultTreeNode getMselectedActionValues() {
        return mselectedActionValues;
    }

    public void setMselectedActionValues(DefaultTreeNode mselectedActionValues) {
        this.mselectedActionValues = mselectedActionValues;
    }

    public boolean isEnableType() {
        return enableType;
    }

    public void setEnableType(boolean enableType) {
        this.enableType = enableType;
    }

    public PersistAction getMflag() {
        return mflag;
    }

    public void setMflag(PersistAction mflag) {
        this.mflag = mflag;
    }

    public int getCheckAction() {
        return checkAction;
    }

    public void setCheckAction(int checkAction) {
        this.checkAction = checkAction;
    }

    public Action getEtt_backup() {
        return ett_backup;
    }

    public void setEtt_backup(Action ett_backup) {
        this.ett_backup = ett_backup;
    }

    public List<Action> getCboParent() {
        return cboParent;
    }

    public void setCboParent(List<Action> cboParent) {
        this.cboParent = cboParent;
    }

    public DefaultTreeNode getNodeActionSelect() {
        return nodeActionSelect;
    }

    public void setNodeActionSelect(DefaultTreeNode nodeActionSelect) {
        this.nodeActionSelect = nodeActionSelect;
    }

    public Action getEttFilter() {
        return ettFilter;
    }

    public void setEttFilter(Action ettFilter) {
        this.ettFilter = ettFilter;
    }

    public List<Permit> getListPermit() {
        return listPermit;
    }

    public void setListPermit(List<Permit> listPermit) {
        this.listPermit = listPermit;
    }

    public Permit getMpermit() {
        return mpermit;
    }

    public void setMpermit(Permit mpermit) {
        this.mpermit = mpermit;
    }

}
