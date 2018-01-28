package com.yzy.ebag.student.bean;

/**
 * @author caoyu
 * @date 2018/1/27
 * @description
 */

public class GrowthBean {

    public GrowthBean(String grade, int gradeCode, int status){
        this.grade = grade;
        this.gradeCode = gradeCode;
        this.status = status;
    }

    private int gradeCode = 8;
    private String grade = "八年级";
    /**
     * 2 当前 0 未经历 1 已经李
     */
    private int status = 1;

    public int getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(int gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
