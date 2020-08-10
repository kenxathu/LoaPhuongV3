/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author ChienDX
 */
@ManagedBean(name = "MainController")
@SessionScoped
public class MainController implements Serializable {

    private Locale locale;
    

    public MainController() {
        //Thiết lập ngôn ngữ
        if (locale == null) {
            this.locale = new Locale("vn");
            FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        }
        
        
        //Dashboard data
        //dashBoarData = (new LoginWebservice("").getListDashBoardByArea())
    }    

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    
}
