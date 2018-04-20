package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/3/20.
 */

public class AddCourseTextbookBean implements Serializable {

    private List<SubjectBean> firstvo;
    private List<SubjectBean> nextvo;

    public List<SubjectBean> getFirstvo() {
        return firstvo;
    }

    public void setFirstvo(List<SubjectBean> firstvo) {
        this.firstvo = firstvo;
    }

    public List<SubjectBean> getNextvo() {
        return nextvo;
    }

    public void setNextvo(List<SubjectBean> nextvo) {
        this.nextvo = nextvo;
    }

    public static class SubjectBean {
        /**
         * subjectCode : ddyfz
         * subjectName : 道德与法治
         * gradeCode : 1
         * bookVersions : [{"versionCode":"rj","versionName":"人教版  ","bookVersionId":"3","semesterCode":"1"}]
         * bookVersionVoList : []
         */

        private String subjectCode;
        private String subjectName;
        private String gradeCode;
        private List<BookVersionsBean> bookVersions;
        private List<?> bookVersionVoList;

        public String getSubjectCode() {
            return subjectCode;
        }

        public void setSubjectCode(String subjectCode) {
            this.subjectCode = subjectCode;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getGradeCode() {
            return gradeCode;
        }

        public void setGradeCode(String gradeCode) {
            this.gradeCode = gradeCode;
        }

        public List<BookVersionsBean> getBookVersions() {
            return bookVersions;
        }

        public void setBookVersions(List<BookVersionsBean> bookVersions) {
            this.bookVersions = bookVersions;
        }

        public List<?> getBookVersionVoList() {
            return bookVersionVoList;
        }

        public void setBookVersionVoList(List<?> bookVersionVoList) {
            this.bookVersionVoList = bookVersionVoList;
        }

        public static class BookVersionsBean {
            /**
             * versionCode : rj
             * versionName : 人教版
             * bookVersionId : 3
             * semesterCode : 1
             */

            private String versionCode;
            private String versionName;
            private String bookVersionId;
            private String semesterCode;

            public String getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(String versionCode) {
                this.versionCode = versionCode;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public String getBookVersionId() {
                return bookVersionId;
            }

            public void setBookVersionId(String bookVersionId) {
                this.bookVersionId = bookVersionId;
            }

            public String getSemesterCode() {
                return semesterCode;
            }

            public void setSemesterCode(String semesterCode) {
                this.semesterCode = semesterCode;
            }
        }
    }
}
