package com.yzy.ebag.teacher.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/8.
 */

public class PerformanceBean implements Serializable {

    /**
     * uid : 1592
     * name : test学生
     * headUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/11/17675/ht/201708100307302
     * teacherPraise : 6
     * teacherCriticism : 0
     */

    private String uid;
    private String name;
    private String headUrl;
    private int teacherPraise;
    private int teacherCriticism;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getTeacherPraise() {
        return teacherPraise;
    }

    public void setTeacherPraise(int teacherPraise) {
        this.teacherPraise = teacherPraise;
    }

    public int getTeacherCriticism() {
        return teacherCriticism;
    }

    public void setTeacherCriticism(int teacherCriticism) {
        this.teacherCriticism = teacherCriticism;
    }
}
