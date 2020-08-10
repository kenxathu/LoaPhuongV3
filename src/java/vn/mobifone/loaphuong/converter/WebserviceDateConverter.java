/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.converter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author DuongTM
 */
@FacesConverter("WebserviceDateConverter")
public class WebserviceDateConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null) {
            Date d;

            try {
                d = (new SimpleDateFormat("dd/MM/yyyy")).parse(value);
            } catch (Exception ex) {
                return null;
            }

            GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
            gregorianCalendar.setTime(d);
            
            return gregorianCalendar;

        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            Calendar calendar = ((XMLGregorianCalendar) value).toGregorianCalendar();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            formatter.setTimeZone(calendar.getTimeZone());
            return formatter.format(calendar.getTime());

        } else {
            return null;
        }
    }
}
