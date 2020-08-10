/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Util về Servlet
 * @author Chien Do Xo
 */
public class ServletUtil {

    //Lấy url referer
    public static String getReferer() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getHeader("Referer");
    }

    //Lấy giá trị tham số request
    public static String getParamValue(String strParamName) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getParameter(strParamName) == null || request.getParameter(strParamName).equals("null") ? null : request.getParameter(strParamName);
    }

    //Kiểm tra xem có tham số request không
    public static boolean isHasParam() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getParameterMap().size() > 1;
    }
}
