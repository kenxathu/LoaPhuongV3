/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.action;

/**
 *
 * @author tho.nt
 */
public class Permit {
    private long permitId;
    private String permitName;
    private boolean check;
    private String permitCode;
    

    public Permit(long permitId, String permitName, boolean check,String permitCode) {
        this.permitId = permitId;
        this.permitName = permitName;
        this.check = check;
        this.permitCode = permitCode;
    }
    
    public Permit() {
    }

    public String getPermitCode() {
        return permitCode;
    }

    public void setPermitCode(String permitCode) {
        this.permitCode = permitCode;
    }
    
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    
    public long getPermitId() {
        return permitId;
    }

    public void setPermitId(int permitId) {
        this.permitId = permitId;
    }

    public String getPermitName() {
        return permitName;
    }

    public void setPermitName(String permitName) {
        this.permitName = permitName;
    }
    
    
}
