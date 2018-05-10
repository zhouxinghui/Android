package com.yzy.ebag.parents.bean;

public class StudentSubjectBean {


    /**
     * id : 00b5d6cc87f64c1ca4280ec0dc2e81b7
     * classId : 7f087c52aa2d407483c70e471f2decfe
     * bookVersionId : 371
     * bookVersionCode : rj
     * bookVersionName : 人教版
     * bookCode : yw
     * bookName : 语文
     * gradeCode : 5
     * gradeName : 五年级
     * semeterCode : 2
     * semeterName : 下学期
     * createDate : 1524643247000
     * className : 五（1）班
     */

    private String id;
    private String classId;
    private String bookVersionId;
    private String bookVersionCode;
    private String bookVersionName;
    private String bookCode;
    private String bookName;
    private String gradeCode;
    private String gradeName;
    private String semeterCode;
    private String semeterName;
    private long createDate;
    private String className;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getBookVersionCode() {
        return bookVersionCode;
    }

    public void setBookVersionCode(String bookVersionCode) {
        this.bookVersionCode = bookVersionCode;
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

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StudentSubjectBean){
            return ((StudentSubjectBean) obj).bookName.equals(this.bookName);
        }
        return super.equals(obj);
    }
}
