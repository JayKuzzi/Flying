package com.bb.offerapp.bean;

/**
 * Created by bb on 2017/5/28.
 */

public class QuestionBean {
    private String tittle;
    private String content;

    public QuestionBean(String tittle, String content) {
        this.tittle = tittle;
        this.content = content;
    }

    public QuestionBean() {
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
