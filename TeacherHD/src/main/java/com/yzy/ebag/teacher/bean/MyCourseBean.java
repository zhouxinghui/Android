package com.yzy.ebag.teacher.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/1/16.
 */

public class MyCourseBean implements Serializable {

    /**
     * classId : null
     * bookVersionId : 19
     * bookVersionName : 北师大版
     * bookCode : yy
     * bookName : 英语
     * gradeCode : 1
     * gradeName : 一年级
     * semeterCode : 1
     * semeterName : 上学期
     */

    private String classId;
    private String bookVersionId;
    private String bookVersionName;
    private String bookCode;
    private String bookName;
    private String gradeCode;
    private String gradeName;
    private String semeterCode;
    private String semeterName;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getBookVersionId() {
        return bookVersionId;
    }

    public void setBookVersionId(String bookVersionId) {
        this.bookVersionId = bookVersionId;
    }

    public String getBookVersionName() {
        return bookVersionName;
    }

    public void setBookVersionName(String bookVersionName) {
        this.bookVersionName = bookVersionName;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getSemeterCode() {
        return semeterCode;
    }

    public void setSemeterCode(String semeterCode) {
        this.semeterCode = semeterCode;
    }

    public String getSemeterName() {
        return semeterName;
    }

    public void setSemeterName(String semeterName) {
        this.semeterName = semeterName;
    }
}
