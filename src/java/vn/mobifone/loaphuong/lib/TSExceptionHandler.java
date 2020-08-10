/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib;

import java.io.Serializable;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Xử lý exception
 * @author ChienDX
 */
public class TSExceptionHandler extends ExceptionHandlerFactory  implements Serializable{

    private ExceptionHandlerFactory parent;

    public TSExceptionHandler(final ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler eh = parent.getExceptionHandler();
        eh = new DefaultExceptionHandler(eh);
        return eh;
    }
}
