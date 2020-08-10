/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.converter;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import vn.mobifone.loaphuong.action.Action;
import vn.mobifone.loaphuong.lib.Session;

/**
 *
 * @author DuongTM
 */
@FacesConverter("ModuleConverter")
public class ModuleConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            List<Action> list = (List<Action>) Session.getSessionValue("module.MlistActionValue");
            for (Action tmp : list) {
                if ((tmp.getActionName()).equals(value)) {
                    return tmp;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return String.valueOf(((Action) value).getActionName());
        } else {
            return null;
        }
    }

}
