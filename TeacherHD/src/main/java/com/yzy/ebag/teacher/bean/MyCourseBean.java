package com.yzy.ebag.teacher.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by YZY on 2018/1/16.
 */

public class MyCourseBean implements Serializable {
    public MyCourseBean(String edition, String time, String item, String subject, String classX){
        this.image = image;
        this.edition = edition;
        this.time = time;
        this.item = item;
        this.subject = subject;
        this.classX = classX;
    }

    private String image = "http://pic32.nipic.com/20130827/12906030_123121414000_2.png";
    private String edition;
    private String time;
    private String item;
    private String subject;
    @JSONField(name = "class")
    private String classX;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClassX() {
        return classX;
    }

    public void setClassX(String classX) {
        this.classX = classX;
    }
}
