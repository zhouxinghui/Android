package com.yzy.ebag.teacher.bean;

import java.util.List;

/**
 * Created by YZY on 2018/1/8.
 */

public class FirstPageBean {

    private List<ResultHomeWorkVosBean> resultHomeWorkVos;
    private List<String> resultAdvertisementVos;

    public List<ResultHomeWorkVosBean> getResultHomeWorkVos() {
        return resultHomeWorkVos;
    }

    public void setResultHomeWorkVos(List<ResultHomeWorkVosBean> resultHomeWorkVos) {
        this.resultHomeWorkVos = resultHomeWorkVos;
    }

    public List<String> getResultAdvertisementVos() {
        return resultAdvertisementVos;
    }

    public void setResultAdvertisementVos(List<String> resultAdvertisementVos) {
        this.resultAdvertisementVos = resultAdvertisementVos;
    }

    public static class ResultHomeWorkVosBean {
        /**
         * studentHomeWorkCount : 0
         * homeWorkNoCompleteCount : 0
         * subject : null
         * subCode : null
         * grade : null
         * gradeCode : null
         * classId : null
         * className : null
         * gradeByClazzName : null
         */

        private String studentHomeWorkCount;
        private String homeWorkNoCompleteCount;
        private String subject;
        private String subCode;
        private String grade;
        private String gradeCode;
        private String classId;
        private String className;
        private String gradeByClazzName;

        public String getStudentHomeWorkCount() {
            return studentHomeWorkCount;
        }

        public void setStudentHomeWorkCount(String studentHomeWorkCount) {
            this.studentHomeWorkCount = studentHomeWorkCount;
        }

        public String getHomeWorkNoCompleteCount() {
            return homeWorkNoCompleteCount;
        }

        public void setHomeWorkNoCompleteCount(String homeWorkNoCompleteCount) {
            this.homeWorkNoCompleteCount = homeWorkNoCompleteCount;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getSubCode() {
            return subCode;
        }

        public void setSubCode(String subCode) {
            this.subCode = subCode;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

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

        public String getGradeByClazzName() {
            return gradeByClazzName;
        }

        public void setGradeByClazzName(String gradeByClazzName) {
            this.gradeByClazzName = gradeByClazzName;
        }
    }
}
