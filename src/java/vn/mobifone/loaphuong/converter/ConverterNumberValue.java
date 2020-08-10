/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.converter;

import java.text.DecimalFormat;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author ChienDX
 */
@FacesConverter("ConverterNumberValue")
//Converter cho việc hiển thị dữ liệu số thực
public class ConverterNumberValue implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        try {
            return Double.parseDouble(string);

        } catch (Exception ex) {
            return string;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        try {
            if (o instanceof String) {
                return (String) o;

            } else if (o instanceof Float) {
                return (Float) o == 0 ? "-" : new DecimalFormat("#,###.######").format((Float) o);

            } else if (o instanceof Double) {
                return (Double) o == 0 ? "-" : new DecimalFormat("#,###.######").format((Double) o);

            } else {
                return "-";
            }

        } catch (Exception ex) {
            return String.valueOf(o);
        }

    }
}
