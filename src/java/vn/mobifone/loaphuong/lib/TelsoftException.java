/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.lib;

import java.io.Serializable;
import vn.mobifone.loaphuong.util.ResourceBundleUtil;

/**
 * @author ChienDX
 */
//Xử lý exception
public class TelsoftException extends Exception implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String header = "";
    private String content = "";
    private Exception exception = null;
    private Throwable throwable = null;

    public TelsoftException() {
    }

    public TelsoftException(String errContentOrCode) {
        String strContent = "";

        try {
            strContent = ResourceBundleUtil.getAMObjectAsString("PP_ADMINMESSAGE", errContentOrCode);

        } catch (Exception ex) {
        }

        if (strContent.isEmpty()) {
            this.content = errContentOrCode;

        } else {
            this.content = strContent;
        }

        exception = new Exception(this.content);
    }

    public TelsoftException(String header, String errContent) {
        this.header = header;
        content = errContent;
        exception = new Exception(errContent);
    }

    public TelsoftException(String header, String errContent, Exception ex) {
        this.header = header;
        content = errContent;
        exception = ex;
    }

    public TelsoftException(String header, String errContent, Throwable throwable) {
        this.header = header;
        content = errContent;
        exception = new Exception(errContent);
        this.throwable = throwable;
    }

    public TelsoftException(String header, Exception errContent) {
        this.header = header;
        content = errContent.toString();
        exception = errContent;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }
}
