package ebag.mobile.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/4/9.
 */

public class ReadRecordVersionBean implements Serializable {
    private List<SubjectBean> first;
    private List<SubjectBean> next;

    public List<SubjectBean> getFirst() {
        return first;
    }

    public void setFirst(List<SubjectBean> first) {
        this.first = first;
    }

    public List<SubjectBean> getNext() {
        return next;
    }

    public void setNext(List<SubjectBean> next) {
        this.next = next;
    }

    public static class SubjectBean {
        /**
         * subjectCode : yy
         * subjectName : 英语
         * bookVersionVoList : [{"versionCode":"rj","versionName":"人教版  ","bookVersionId":"4","semesterCode":"1"},{"versionCode":"sj","versionName":"苏教版","bookVersionId":"8","semesterCode":"1"},{"versionCode":"bs","versionName":"北师大版","bookVersionId":"18","semesterCode":"1"},{"versionCode":"hj","versionName":"沪教版","bookVersionId":"24","semesterCode":"1"},{"versionCode":"lj","versionName":"鲁教版","bookVersionId":"30","semesterCode":"1"},{"versionCode":"jj","versionName":"冀教版","bookVersionId":"36","semesterCode":"1"},{"versionCode":"zj","versionName":"浙教版","bookVersionId":"39","semesterCode":"1"},{"versionCode":"wy","versionName":"外研版","bookVersionId":"48","semesterCode":"1"},{"versionCode":"yw","versionName":"语文版","bookVersionId":"54","semesterCode":"1"},{"versionCode":"kp","versionName":"科普版","bookVersionId":"60","semesterCode":"1"},{"versionCode":"yj","versionName":"粤教版","bookVersionId":"66","semesterCode":"1"},{"versionCode":"gj","versionName":"赣科版","bookVersionId":"71","semesterCode":"1"},{"versionCode":"cj","versionName":"川教版","bookVersionId":"78","semesterCode":"1"},{"versionCode":"jk","versionName":"教科版","bookVersionId":"84","semesterCode":"1"},{"versionCode":"yws","versionName":"语文S版","bookVersionId":"90","semesterCode":"1"},{"versionCode":"rjsl","versionName":"人教版SL(一起)","bookVersionId":"575","semesterCode":"1"},{"versionCode":"grzdzb","versionName":"港人子弟专版","bookVersionId":"638","semesterCode":"1"},{"versionCode":"wy1","versionName":"外研版(一起)","bookVersionId":"674","semesterCode":"1"}]
         */

        private String subjectCode;
        private String subjectName;
        private List<BookVersionVoListBean> bookVersionVoList;

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

        public List<BookVersionVoListBean> getBookVersionVoList() {
            return bookVersionVoList;
        }

        public void setBookVersionVoList(List<BookVersionVoListBean> bookVersionVoList) {
            this.bookVersionVoList = bookVersionVoList;
        }

        public static class BookVersionVoListBean {
            /**
             * versionCode : rj
             * versionName : 人教版
             * bookVersionId : 4
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
