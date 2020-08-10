/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import vn.mobifone.loaphuong.admin.entity.MenuAuthorizator;
import vn.mobifone.loaphuong.entity.AreaTree;

/**
 *
 * @author ChienDX
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String holder_name;
    @Expose
    @SerializedName("user_id")
    private long userId;
    @SerializedName("username")
    private String userName;
    private String password;
    private String name;
    private int type;
    @SerializedName("id_card")
    private String idCard;
    private String phone;
    private String email;
    @SerializedName("area_id")
    private long areaId;
    @SerializedName("household_id")
    private Long houseHouseHoldId;
    private ArrayList<Long> roles;
    @Expose
    private String areaName;
    @Expose
    private String roleName;
    @SerializedName("action_list")
    private ArrayList<String> actionList;
    @SerializedName("permit_code")
    private String permitCode;
    @SerializedName("actions")
    private ArrayList<MenuAuthorizator> actions;
    private List<AreaTree> area_branch;
    @SerializedName("is_household_owner")
    private int isHouseOwner;
    @Expose
    private boolean isHouseOwnerBool;

    public User() {
    }

    public User(User user) {

        this.userId = user.userId;
        this.userName = user.userName;
        this.password = user.password;
        this.name = user.name;
        this.type = user.type;
        this.idCard = user.idCard;
        this.phone = user.phone;
        this.email = user.email;
        this.areaId = user.areaId;
        this.houseHouseHoldId = user.houseHouseHoldId;
        this.roles = user.roles;
        this.areaName = user.areaName;
        this.roleName = user.roleName;
        this.actions = user.actions;
        this.area_branch = user.area_branch;
        this.isHouseOwner = user.isHouseOwner;
        //  this.permitCode = user.permitCode;
    }

    public int getIsHouseOwner() {
        return isHouseOwner;
    }

    public void setIsHouseOwner(int isHouseOwner) {
        this.isHouseOwner = isHouseOwner;
    }

    public boolean isIsHouseOwnerBool() {
        return isHouseOwner==1;
    }

    public void setIsHouseOwnerBool(boolean isHouseOwnerBool) {
        this.isHouseOwnerBool = isHouseOwnerBool;
        this.isHouseOwner = isHouseOwnerBool?1:0;
    }

    
    
    public List<AreaTree> getArea_branch() {
        return area_branch;
    }

    public void setArea_branch(List<AreaTree> area_branch) {
        this.area_branch = area_branch;
    }
    
    public ArrayList<MenuAuthorizator> getActions() {
        return actions;
    }

    public void setActions(ArrayList<MenuAuthorizator> actions) {
        this.actions = actions;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getHouseHouseHoldId() {
        return houseHouseHoldId;
    }

    public void setHouseHouseHoldId(Long houseHouseHoldId) {
        this.houseHouseHoldId = houseHouseHoldId;
    }

    public void addRole(long role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public ArrayList<Long> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Long> roles) {
        if (roles.isEmpty()) {
            if (this.roles != null) {
                this.roles.clear();
            }
        } else {
            if (this.roles == null) {
                this.roles = new ArrayList<>();
                this.roles.addAll(roles);
            }
        }
    }

    public String getHolder_name() {
        return holder_name;
    }

    public ArrayList<String> getActionList() {
        return actionList;
    }

    public void setAction_list(ArrayList<String> actionList) {
        this.actionList = actionList;
    }

    public String getPermitCode() {
        return permitCode;
    }

    public void setPermitCode(String permitCode) {
        this.permitCode = permitCode;
    }

    public String toJson() {
        JSONObject jsonAdd = new JSONObject(); // we need another object to store the address
        jsonAdd.put("user_id", this.userId);
        jsonAdd.put("username", this.userName);
        jsonAdd.put("password", "");
        jsonAdd.put("name", this.name);
        jsonAdd.put("phone", this.phone);
        jsonAdd.put("type", this.type);
        jsonAdd.put("id_card", this.idCard);
        jsonAdd.put("email", this.email);
        jsonAdd.put("area_id", this.areaId);
        jsonAdd.put("household_id", "");
        JSONArray jsArray = new JSONArray(this.roles);

        jsonAdd.put("roles", (Object) jsArray);

        return jsonAdd.toString();
    }

}
