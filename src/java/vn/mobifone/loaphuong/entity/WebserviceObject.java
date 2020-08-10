/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

/**
 *
 * @author cuong.trinh
 */
public class WebserviceObject {
    private int type;//-1: Đăng kí mới dịch - 2: Cập nhật thông tin dịch vu- 3: Tạm dừng dịch vụ -4: Kích hoạt lại dịch vụ (sau khi bị tạm dừng)-5: Hủy dịch vụ

    private String username; //tu product
    private String password; //tu product
    private String timeStamp; // time hien tai
    private String customerId; // customerId -> 1
    private String customerName ;// customerName
    private String customerEmail;//customerEmailtest@gmail.com
    private String customerTel;//090000000
    private String licenseNo;//
    private String taxCode;
    private String customerAddress;
    private String adminName;
    private String adminPosition;
    private String adminTel;
    private String adminEmail;
    private String contractID;
    private String contractNo;
    private String contractDate;
    private String startDate;
    private String aMID ;
    private String aMName;
    private String agencyID;//1
    private String agencyName;//Cong ty khu vuc 1
    private String regionID;//abcd
    private String regionName ;

   

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getCusAddress() {
        return customerAddress;
    }

    public void setCusAddress(String address) {
        this.customerAddress = address;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPosition() {
        return adminPosition;
    }

    public void setAdminPosition(String adminPosition) {
        this.adminPosition = adminPosition;
    }

    public String getAdminTel() {
        return adminTel;
    }

    public void setAdminTel(String adminTel) {
        this.adminTel = adminTel;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getaMID() {
        return aMID;
    }

    public void setaMID(String aMID) {
        this.aMID = aMID;
    }

    public String getaMName() {
        return aMName;
    }

    public void setaMName(String aMName) {
        this.aMName = aMName;
    }

    public String getAgencyID() {
        return agencyID;
    }

    public void setAgencyID(String agencyID) {
        this.agencyID = agencyID;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getRegionID() {
        return regionID;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    
    
}
