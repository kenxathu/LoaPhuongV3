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
 * Validate chuỗi user name
 * @author ChienDX
 */
@FacesValidator("ValidatorUserName")
public class ValidatorUserName implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        String pattern = "[a-zA-Z0-9_\\.\\-@]+";

        //Check valid username
        if (o != null && !((String) o).matches(pattern)) {
            FacesMessage msg = new FacesMessage("Username validation failed", "Username is not valid");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
