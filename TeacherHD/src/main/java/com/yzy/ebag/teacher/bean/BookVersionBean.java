package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/1/22.
 */

public class BookVersionBean implements Serializable {

    /**
     * subjectCode : sx
     * subjectName : 数学
     * bookVersionVoList : [{"versionCode":"bs","versionName":"北师大版"},{"versionCode":"cj","versionName":"川教版"},{"versionCode":"gj","versionName":"赣科版"},{"versionCode":"hj","versionName":"沪教版"},{"versionCode":"jj","versionName":"冀教版"},{"versionCode":"jk","versionName":"教科版"},{"versionCode":"kp","versionName":"科普版"},{"versionCode":"lj","versionName":"鲁教版"},{"versionCode":"rj","versionName":"人教版  "},{"versionCode":"sj","versionName":"苏教版"},{"versionCode":"wy","versionName":"外研版"},{"versionCode":"yj","versionName":"粤教版"},{"versionCode":"yw","versionName":"语文版"},{"versionCode":"yws","versionName":"语文S版"},{"versionCode":"zj","versionName":"浙教版"}]
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
         * versionCode : bs
         * versionName : 北师大版
         */

        private String versionCode;
        private String versionName;
        private String bookVersionId;

        public String getBookVersionId() {
            return bookVersionId;
        }

        public void setBookVersionId(String bookVersionId) {
            this.bookVersionId = bookVersionId;
        }

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
    }
}
