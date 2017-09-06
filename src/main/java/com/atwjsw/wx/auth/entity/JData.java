package com.atwjsw.wx.auth.entity;

import java.io.Serializable;

public class JData implements Serializable{

    public String template_id;
    public String title;
    public String primary_industry;
    public String deputy_industry;
    public String content;
    public String example;

    public JData() {
    }

    public JData(String template_id, String content) {
        this.template_id = template_id;
        this.content = content;
    }

    public JData(String template_id, String title, String primary_industry, String deputy_industry, String content, String example) {
        this.template_id = template_id;
        this.title = title;
        this.primary_industry = primary_industry;
        this.deputy_industry = deputy_industry;
        this.content = content;
        this.example = example;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrimary_industry() {
        return primary_industry;
    }

    public void setPrimary_industry(String primary_industry) {
        this.primary_industry = primary_industry;
    }

    public String getDeputy_industry() {
        return deputy_industry;
    }

    public void setDeputy_industry(String deputy_industry) {
        this.deputy_industry = deputy_industry;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
