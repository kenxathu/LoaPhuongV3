/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.security;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.SystemLogger;

/**
 *
 * @author ChienDX
 */
@ManagedBean(name = "exception")
@SessionScoped
//Controller hiển thị exception
public class ExceptionHandler implements Serializable {

    private String mstrExceptionContent = "";

    public String getMstrExceptionContent() {
        try {
            if (SystemConfig.getConfig("ShowException").equals("1")) {
                return mstrExceptionContent;
                
            } else {
                return "An exception has ...";
            }
            
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            return "An exception has ...";
        }
    }

    public void setMstrExceptionContent(String mstrExceptionContent) {
        this.mstrExceptionContent = mstrExceptionContent;
    }
}
