/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Validate chuỗi chỉ có số và chữ
 * @author ChienDX
 */
@FacesValidator("ValidatorAlphaNum")
public class ValidatorAlphaNum implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String pattern = "[A-Za-z0-9_]+";

        //Check valid string
        if (o != null && !((String) o).matches(pattern)) {
            FacesMessage msg = new FacesMessage("AlphaNum string validation", "AlphaNum string is not valid");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
