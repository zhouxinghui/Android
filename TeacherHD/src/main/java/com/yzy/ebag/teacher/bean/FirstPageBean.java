package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/1/8.
 */

public class FirstPageBean implements Serializable{

    private List<ResultHomeWorkVosBean> resultHomeWorkVos;
    private List<ResultAdvertisementVosBean> resultAdvertisementVos;

    public List<ResultHomeWorkVosBean> getResultHomeWorkVos() {
        return resultHomeWorkVos;
    }

    public void setResultHomeWorkVos(List<ResultHomeWorkVosBean> resultHomeWorkVos) {
        this.resultHomeWorkVos = resultHomeWorkVos;
    }

    public List<ResultAdvertisementVosBean> getResultAdvertisementVos() {
        return resultAdvertisementVos;
    }

    public void setResultAdvertisementVos(List<ResultAdvertisementVosBean> resultAdvertisementVos) {
        this.resultAdvertisementVos = resultAdvertisementVos;
    }

    public static class ResultHomeWorkVosBean {
        /**
         * studentHomeWorkCount : 3
         * homeWorkCompleteCount : 2
         * subject :
         * subCode :
         * grade :
         * gradeCode :
         * classId : 14
         * className : 5502班
         * gradeByClazzName :
         */

        private String studentHomeWorkCount;
        private String homeWorkCompleteCount;
        private String subject;
        private String subCode;
        private String grade;
        private String gradeCode;
        private String classId;
        private String className;
        private String gradeByClazzName;
        private String id;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStudentHomeWorkCount() {
            return studentHomeWorkCount;
        }

        public void setStudentHomeWorkCount(String studentHomeWorkCount) {
            this.studentHomeWorkCount = studentHomeWorkCount;
        }

        public String getHomeWorkCompleteCount() {
            return homeWorkCompleteCount;
        }

        public void setHomeWorkCompleteCount(String homeWorkCompleteCount) {
            this.homeWorkCompleteCount = homeWorkCompleteCount;
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

    public static class ResultAdvertisementVosBean {
        /**
         * adverCode : YSB_xxxvipxxx
         * adverName : 广告商xxx
         * adverUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/test/home_banner_one@1x.png
         */

        private String adverCode;
        private String adverName;
        private String adverUrl;

        public String getAdverCode() {
            return adverCode;
        }

        public void setAdverCode(String adverCode) {
            this.adverCode = adverCode;
        }

        public String getAdverName() {
            return adverName;
        }

        public void setAdverName(String adverName) {
            this.adverName = adverName;
        }

        public String getAdverUrl() {
            return adverUrl;
        }

        public void setAdverUrl(String adverUrl) {
            this.adverUrl = adverUrl;
        }
    }
}
