/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Xử lý trang exception
 * @author ChienDX
 */
public class DefaultExceptionHandler extends ExceptionHandlerWrapper implements Serializable {

    private static final Log LOG = LogFactory.getLog(DefaultExceptionHandler.class);
    public static final String MESSAGE_DETAIL_KEY = "ip.client.jsftoolkit.messageDetail";
    private ExceptionHandler wrapped;

    public DefaultExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            
            // get the exception from context
            Throwable t = context.getException();
            final FacesContext fc = FacesContext.getCurrentInstance();
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            final NavigationHandler nav = fc.getApplication().getNavigationHandler();
            
            try {
                if (t instanceof AbortProcessingException) {
                    // about AbortProcessingException see JSF 2 spec.
                    LOG.error("An unexpected exception has occurred by event listener(s)", t);
                    fc.getExternalContext().getSessionMap().put(DefaultExceptionHandler.MESSAGE_DETAIL_KEY, t.getLocalizedMessage());
                    
                } else if (t instanceof ViewExpiredException) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("View '" + ((ViewExpiredException) t).getViewId() + "' is expired", t);
                    }
                    
                } else {
                    SystemLogger.getLogger().error(t);
                    requestMap.put("exceptionMessage", t.getMessage());
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    vn.mobifone.loaphuong.security.ExceptionHandler ehl = facesContext.getApplication().evaluateExpressionGet(facesContext, "#{exception}", vn.mobifone.loaphuong.security.ExceptionHandler.class);
                    ehl.setMstrExceptionContent(ExceptionUtils.getStackTrace(t));
                    nav.handleNavigation(facesContext, null, "exceptionNavigation");
                    facesContext.renderResponse();
                    
                    try {
                        facesContext.getExternalContext().dispatch("/faces/exception.xhtml");
                        
                    } catch (IOException ex) {
                        LOG.error(ex);
                    }
                    
                    facesContext.responseComplete();
                }

            } finally {
                i.remove();
            }
            
            getWrapped().handle();
        }

    }
}
