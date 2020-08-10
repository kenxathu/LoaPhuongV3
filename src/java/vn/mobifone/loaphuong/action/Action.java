/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.action;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tho.nt
 */
//action_url":"QL","breadcrumb":"Trang chủ||Quản lý","parent_id":"1","name":"Quản lý","id":"3","type":"0","status":"1"
//{"action_url":"/client/holdhouse-management","parent_id":"3","name":"Quản lý hộ gia đình","permit_code":"EDIT_HOUSEHOLD;DELETE_HOUSEHOLD;ADD_HOUSEHOLD;","id":"7","type":"1"}
public class Action {
    private long id;
    private String name;
    private String permit_code;
    private String action_url;
    private long parent_id;
    private int status;
    private String type;
    private String parentName;
    private String typeView;
    private boolean isCheckValid;
    private List<Permit> listPermit;
    private String breadcrumb;
    public Action() {
        
    }

    public String getBreadCrumb() {
        return breadcrumb;
    }

    public void setBreadCrumb(String breadCrumb) {
        this.breadcrumb = breadCrumb;
    }
    
    public Action(long actionId, String permitCode, String actionUrl, boolean isCheckValid) {
        this.id = actionId;
        this.permit_code = permitCode;
        this.action_url = actionUrl;
        this.isCheckValid = isCheckValid;
    }
    
    public Action( Action action) {
        this.id = action.id;
        this.name = action.name;
        this.action_url = action.action_url;
        this.parent_id = action.parent_id;
        this.status = action.status;
        this.type = action.type;
        this.parentName = action.parentName;
        this.permit_code = action.permit_code;
        this.typeView = action.typeView;
        this.isCheckValid = action.isCheckValid;
        this.listPermit = action.getListPermit();
    }

    public List<Permit> getListPermit() {
        if(listPermit==null) 
            return new ArrayList<>();
        return listPermit;
    }

    public String getPermitCode() {
        return permit_code;
    }

    public void setPermitCode(String permitCode) {
        this.permit_code = permitCode;
    }
    
    public void setListPermit(List<Permit> listPermit) {
        this.listPermit = listPermit;
    }
    
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getTypeView() {
        return typeView;
    }

    public void setTypeView(String typeView) {
        this.typeView = typeView;
    }
    
    public boolean isIsCheck() {
        return isCheckValid;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheckValid = isCheck;
    }
    
    public long getActionId() {
        return id;
    }

    public void setActionId(long actionId) {
        this.id = actionId;
    }

    public String getActionName() {
        return name;
    }

    public void setActionName(String actionName) {
        this.name = actionName;
    }

    public boolean isIsCheckValid() {
        return isCheckValid;
    }

    public void setIsCheckValid(boolean isCheckValid) {
        this.isCheckValid = isCheckValid;
    }
    
   

    public String getActionUrl() {
        return action_url;
    }

    public void setActionUrl(String actionUrl) {
        this.action_url = actionUrl;
    }

    public long getParentId() {
        return parent_id;
    }

    public void setParentId(long parentId) {
        this.parent_id = parentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

   
    
    public void addPermit(Permit p){
        if(listPermit == null) listPermit = new ArrayList<>();
        listPermit.add(p);
    }

   
}
