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


public class Question implements Serializable{
    private long survey_id;
    private int have_other_answer;
    private String question;
    private int question_type;
    private List<Answer> answers;

    public Question() {
        
    }

    public long getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(long survey_id) {
        this.survey_id = survey_id;
    }

    public int getHave_other_answer() {
        return have_other_answer;
    }

    public void setHave_other_answer(int have_other_answer) {
        this.have_other_answer = have_other_answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(int question_type) {
        this.question_type = question_type;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }



    
  

}
