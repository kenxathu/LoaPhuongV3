//package vn.mobifone.loaphuong.admin.entity;
//
//import java.io.Serializable;
//import java.util.Date;
//
///**
// * Class: Module Date: 26/08/2015 14:36:23 Verison: 1.0
// *
// * @author: Telsoft
// */
//public class Module implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    //Fields
//    private long moduleId;
//    private String moduleName;
//    private String type;
//    private long parentId;
//    private String actionUrl;
//    private int status;
//    private String description;
//    private Date createdDate;
//    private Date updatedDate;
//    private String updatedUser;
//    private String order;
//    private String icon;
//    private String parentName;
//    private String typeView;
//    private int level;
//    private boolean checkStatus;
//    private int permitCode;
//    private String breadCrumb;
//    private long orderId;
//    private String[] display;
//    private int count;
//    
//    // Nhóm quyền
//    private int addCode;
//    private int editCode;
//    private int deleteCode;
//    private int viewCode;
//    private String  roles;
//
//    //Constructor
//    public Module() {
//        permitCode = 0;
//        count = 0;
//    }
//
//    public String getRoles() {
//        return roles;
//    }
//
//    public void setRoles(String roles) {
//        this.roles = roles;
//    }
//    
//    public int getAddCode() {
//        return addCode;
//    }
//
//    public void setAddCode(int addCode) {
//        this.addCode = addCode;
//    }
//
//    public int getEditCode() {
//        return editCode;
//    }
//
//    public void setEditCode(int editCode) {
//        this.editCode = editCode;
//    }
//
//    public int getDeleteCode() {
//        return deleteCode;
//    }
//
//    public void setDeleteCode(int deleteCode) {
//        this.deleteCode = deleteCode;
//    }
//
//    public int getViewCode() {
//        return viewCode;
//    }
//
//    public void setViewCode(int viewCode) {
//        this.viewCode = viewCode;
//    }
//
//    public String[] getDisplay() {
//        return display;
//    }
//
//    public void setDisplay(String[] display) {
//        this.display = display;
//    }
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
//    
//    
//    
//    public Module(Module ett) {
//        this.moduleId = ett.getModuleId();
//        this.moduleName = ett.getModuleName();
//        this.type = ett.getType();
//        this.parentId = ett.getParentId();
//        this.actionUrl = ett.actionUrl;
//        this.status = ett.getStatus();
//        this.description = ett.getDescription();
//        this.createdDate = ett.getCreatedDate();
//        this.updatedDate = ett.getUpdatedDate();
//        this.updatedUser = ett.getUpdatedUser();
//        this.icon = ett.getIcon();
//        this.order = ett.getOrder();
//        this.typeView = ett.getTypeView();
//        this.parentName = ett.getParentName();
//        this.level = ett.level;
//        this.checkStatus = ett.checkStatus;
//        this.orderId=ett.orderId;
//        this.roles = ett.roles;
//    }
//
//    //Accessors
//    public void setModuleId(long moduleId) {
//        this.moduleId = moduleId;
//    }
//
//    public long getModuleId() {
//        return moduleId;
//    }
//
//    public void setModuleName(String moduleName) {
//        this.moduleName = moduleName;
//    }
//
//    public String getModuleName() {
//        return moduleName;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setParentId(long parentId) {
//        this.parentId = parentId;
//    }
//
//    public long getParentId() {
//        return parentId;
//    }
//
//    public void setActionUrl(String actionUrl) {
//        this.actionUrl = actionUrl;
//    }
//
//    public String getActionUrl() {
//        return actionUrl;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public int getStatus() {
//        return status;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public Date getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setUpdatedDate(Date updatedDate) {
//        this.updatedDate = updatedDate;
//    }
//
//    public Date getUpdatedDate() {
//        return updatedDate;
//    }
//
//    public void setUpdatedUser(String updatedUser) {
//        this.updatedUser = updatedUser;
//    }
//
//    public String getUpdatedUser() {
//        return updatedUser;
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }
//
//    public String getParentName() {
//        return parentName;
//    }
//
//    public void setParentName(String parentName) {
//        this.parentName = parentName;
//    }
//
//    public int getLevel() {
//        return level;
//    }
//
//    public void setLevel(int level) {
//        this.level = level;
//    }
//
//    public boolean isCheckStatus() {
//        return checkStatus;
//    }
//
//    public void setCheckStatus(boolean checkStatus) {
//        this.checkStatus = checkStatus;
//    }
//
//    public String getOrder() {
//        return order;
//    }
//
//    public void setOrder(String order) {
//        this.order = order;
//    }
//
//    public String getTypeView() {
//        return typeView;
//    }
//
//    public void setTypeView(String typeView) {
//        this.typeView = typeView;
//    }
//
//    public int getPermitCode() {
//        return permitCode;
//    }
//
//    public void setPermitCode(int permitCode) {
//        this.permitCode = permitCode;
//    }
//
//    public String getBreadCrumb() {
//        return breadCrumb;
//    }
//
//    public void setBreadCrumb(String breadCrumb) {
//        this.breadCrumb = breadCrumb;
//    }
//
//    public long getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(long orderId) {
//        this.orderId = orderId;
//    }
// 
//   
//    
//}
