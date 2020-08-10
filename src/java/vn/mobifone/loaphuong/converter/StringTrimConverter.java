/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.converter;

import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author ChienDX
 */
@FacesConverter(forClass = String.class)
//Tự động trim string
public class StringTrimConverter implements Serializable, javax.faces.convert.Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent cmp, String value) {

        if (value != null && cmp instanceof HtmlInputText) {
            return value.trim();
        }

        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent cmp, Object value) {

        if (value != null) {
            return value.toString();
        }
        return null;
    }
}
