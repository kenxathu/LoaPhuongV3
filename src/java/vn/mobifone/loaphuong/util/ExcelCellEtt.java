 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.util;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ChienDX
 */
public class ExcelCellEtt implements Serializable {

    String stringValue;
    double numberValue;
    Date dateValue;
    int intValue;

    public ExcelCellEtt() {
    }

    public ExcelCellEtt(ExcelCellEtt ett) {
        this.stringValue = ett.stringValue;
        this.numberValue = ett.numberValue;
        this.dateValue = ett.dateValue;
        this.intValue = ett.intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue != null ? stringValue.trim() : null;
    }

    public double getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(double numberValue) {
        this.numberValue = numberValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
    
    
}
