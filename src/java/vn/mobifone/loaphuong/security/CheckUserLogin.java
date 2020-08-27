/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.security;

import java.io.Serializable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.config.Config;

/**
 *
 * @author ChienDX
 */
//Kiểm tra trạng thái đăng nhập
public class CheckUserLogin implements PhaseListener, Serializable {

    private static final String USER_LOGIN_OUTCOME = "login";

    @Override
    @SuppressWarnings("empty-statement")
    public void afterPhase(PhaseEvent pe) {

        try {
            FacesContext context = pe.getFacesContext();

            // send the user to the login view
            if (!userExists(context)) {
                reLogin(context);

            } else {
                backHome(context);
            }

        } catch (Exception ex) {
            if (!ex.toString().contains("Cannot call sendRedirect()")) {
                SystemLogger.getLogger().error(ex);
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////

    @Override
    public void beforePhase(PhaseEvent pe) {
    }
    ////////////////////////////////////////////////////////////////////////

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    ////////////////////////////////////////////////////////////////////////

    private void setReferer(String strReferer) {
        if (!strReferer.isEmpty()) {
            Session.setSessionValue("referer", strReferer);
        }
    }
    ////////////////////////////////////////////////////////////////////////

    private void backHome(FacesContext context) {
        try {
            ExternalContext extContext = context.getExternalContext();
            String path = extContext.getRequestPathInfo();
//            Boolean checkPathOTP = false;
//            if(!requestingSecureLoginView(context)){
//               checkPathOTP = true;
//               Session.setSessionValue("statusOTP", checkPathOTP);
//            }
            int statusOTP = Integer.valueOf(Session.getSessionValue(SessionKeyDefine.STATUS_OTP).toString());

           if (!requestingSecureIndexView(context) && statusOTP == 1 ) {         
                      
               
                    context.getApplication().getNavigationHandler().handleNavigation(context, null, "otp");
                    context.responseComplete();          
                    
                
            }       
            

//           if (!requestingSecureIndexView(context) && statusOTP == 1) {
//                context.getApplication().getNavigationHandler().handleNavigation(context, null, "otp");
//                context.responseComplete();
//            }
//            if ("/".equals(path)) {
//                context.getApplication().getNavigationHandler().handleNavigation(context, null, "login");
//                context.responseComplete();
//            }
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
        }
    }
    ////////////////////////////////////////////////////////////////////////

    private void reLogin(FacesContext context) {
        //Remove sesion
        Session.removeSession(SessionKeyDefine.USER);

        // send the user to the login view
        if (requestingSecureView(context)) {
            String strCurrentPath = Config.getCurrentPath();
            setReferer(strCurrentPath);

            context.getApplication().getNavigationHandler().handleNavigation(context, null, USER_LOGIN_OUTCOME);
            context.responseComplete();
        }
    }
    ////////////////////////////////////////////////////////////////////////

    private boolean userExists(FacesContext context) {
        // Need re-check authenticator here.
        // Check user exist
        ExternalContext extContext = context.getExternalContext();
        return (extContext.getSessionMap().containsKey(SessionKeyDefine.USER));
    }
    ////////////////////////////////////////////////////////////////////////

    private boolean requestingSecureView(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        String path = extContext.getRequestPathInfo();
        return (!"/login.xhtml".equals(path));
    }

    ////////////////////////////////////////////////////////////////
    private boolean requestingSecureLoginView(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        String path = extContext.getRequestPathInfo();

        return (!"/otp.xhtml".equals(path));
    }
    
      ////////////////////////////////////////////////////////////////
    private boolean requestingSecureIndexView(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        String path = extContext.getRequestPathInfo();

        return (!"/index.xhtml".equals(path));
    }
}
