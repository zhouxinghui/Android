package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

import ebag.mobile.bean.UnitBean;

/**
 * Created by YZY on 2018/1/22.
 */

public class AssignGradeBean implements Serializable {
    private String gradeCode;
    private String gradeName;
    private List<AssignClassBean> homeClazzInfoVos;
    private List<UnitBean> bookVersionOrUnitVos;

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

    public List<AssignClassBean> getHomeClazzInfoVos() {
        return homeClazzInfoVos;
    }

    public void setHomeClazzInfoVos(List<AssignClassBean> homeClazzInfoVos) {
        this.homeClazzInfoVos = homeClazzInfoVos;
    }

    public List<UnitBean> getBookVersionOrUnitVos() {
        return bookVersionOrUnitVos;
    }

    public void setBookVersionOrUnitVos(List<UnitBean> bookVersionOrUnitVos) {
        this.bookVersionOrUnitVos = bookVersionOrUnitVos;
    }
}
