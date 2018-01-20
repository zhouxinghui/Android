package com.yzy.ebag.student.bean;

import java.util.List;

/**
 * Created by caoyu on 2018/1/12.
 */

public class ClassesInfoBean {

    /**
     * classId : 14
     * className : 502班
     * classAdviser : 1
     * subject : null
     * teacherName : 语文老师
     * resultClazzInfoVos : [{"classId":null,"className":null,"classAdviser":null,"subject":null,"teacherName":"语文老师","resultClazzInfoVos":[]}]
     */

    private String classId;
    private String className;
    private String classAdviser;
    private String subject;
    private String teacherName;
    private List<ClassesInfoBean> resultClazzInfoVos;
    private List<ClassListInfoBean> resultAllClazzInfoVos;

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

    public String getClassAdviser() {
        return classAdviser;
    }

    public void setClassAdviser(String classAdviser) {
        this.classAdviser = classAdviser;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<ClassesInfoBean> getResultClazzInfoVos() {
        return resultClazzInfoVos;
    }

    public void setResultClazzInfoVos(List<ClassesInfoBean> resultClazzInfoVos) {
        this.resultClazzInfoVos = resultClazzInfoVos;
    }

    public List<ClassListInfoBean> getResultAllClazzInfoVos() {
        return resultAllClazzInfoVos;
    }

    public void setResultAllClazzInfoVos(List<ClassListInfoBean> resultAllClazzInfoVos) {
        this.resultAllClazzInfoVos = resultAllClazzInfoVos;
    }
}
