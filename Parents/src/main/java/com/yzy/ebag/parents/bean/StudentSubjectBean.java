package com.yzy.ebag.parents.bean;

import java.util.List;

public class StudentSubjectBean {


    /**
     * subCode : yy
     * subName : 英语
     * subjectList : [{"versionCode":"rjsl","versionName":"人教版SL(一起)","bookversionList":[{"semeter":"2","bookVersionId":"582"}]}]
     */

    private String subCode;
    private String subName;
    private List<SubjectListBean> subjectList;

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

    public List<SubjectListBean> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectListBean> subjectList) {
        this.subjectList = subjectList;
    }

    public static class SubjectListBean {
        /**
         * versionCode : rjsl
         * versionName : 人教版SL(一起)
         * bookversionList : [{"semeter":"2","bookVersionId":"582"}]
         */

        private String versionCode;
        private String versionName;
        private List<BookversionListBean> bookversionList;

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

        public List<BookversionListBean> getBookversionList() {
            return bookversionList;
        }

        public void setBookversionList(List<BookversionListBean> bookversionList) {
            this.bookversionList = bookversionList;
        }

        public static class BookversionListBean {
            /**
             * semeter : 2
             * bookVersionId : 582
             */

            private String semeter;
            private String bookVersionId;

            public String getSemeter() {
                return semeter;
            }

            public void setSemeter(String semeter) {
                this.semeter = semeter;
            }

            public String getBookVersionId() {
                return bookVersionId;
            }

            public void setBookVersionId(String bookVersionId) {
                this.bookVersionId = bookVersionId;
            }
        }
    }
}
