package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/3/31.
 */

public class PrepareSubjectBean implements Serializable {

    /**
     * gradeCode : 1
     * gradeName : null
     * subjects : [{"subCode":"sx","subName":"数学"},{"subCode":"yw","subName":"语文"},{"subCode":"yy","subName":"英语"}]
     */

    private String gradeCode;
    private String gradeName;
    private List<SubjectsBean> subjects;

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

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }

    public static class SubjectsBean {
        /**
         * subCode : sx
         * subName : 数学
         */

        private String subCode;
        private String subName;

        public String getSubCode() {
            return subCode;
        }

        public void setSubCode(String subCode) {
            this.subCode = subCode;
        }

        public String getSubName() {
            return subName;
        }

        public void setSubName(String subName) {
            this.subName = subName;
        }
    }
}
