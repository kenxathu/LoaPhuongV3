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
import vn.mobifone.loaphuong.util.StringUtil;

/**
 * Validate chuỗi theo định dạng email
 * @author ChienDX
 */
@FacesValidator("ValidatorUserEmail")
public class ValidatorUserEmail implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object o) throws ValidatorException {
        //Check valid email
        if (o != null && !StringUtil.isEmailString(o.toString())) {
            FacesMessage msg = new FacesMessage("User email validation", "User email is not valid");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

}
