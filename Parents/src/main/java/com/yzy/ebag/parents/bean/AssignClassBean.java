package com.yzy.ebag.parents.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/1/22.
 */

public class AssignClassBean implements Serializable {
    private String gradeCode;
    private String classId;
    private String className;

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
