/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.entity;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author cuong.trinh
 * 
 * 

 */


public class Servey implements Serializable{
    private long id;
    private long area_id;
    private String survey_name;
    private String survey_description;
    private String created_date;
    private String updated_date;
    private int status;
    private List<Question> questions;

    public Servey() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArea_id() {
        return area_id;
    }

    public void setArea_id(long area_id) {
        this.area_id = area_id;
    }

    public String getSurvey_name() {
        return survey_name;
    }

    public void setSurvey_name(String survey_name) {
        this.survey_name = survey_name;
    }

    public String getSurvey_description() {
        return survey_description;
    }

    public void setSurvey_description(String survey_description) {
        this.survey_description = survey_description;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    



    
  

}
