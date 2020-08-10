package vn.mobifone.loaphuong.admin.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Class: ActionLog Date: 27/08/2015 15:22:25 Verison: 1.0
 *
 * @author: Telsoft
 */
public class ActionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    //Fields
    private long actionId;
    private String actionName;
    private String moduleName;
    private String userName;
    private int actionType;
    private String description;
    private Date actionDate;
    private long contractId;

    //Constructor

    public ActionLog() {
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getActionType() {
        return actionType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Date getActionDate() {
        return actionDate;
    }

}
