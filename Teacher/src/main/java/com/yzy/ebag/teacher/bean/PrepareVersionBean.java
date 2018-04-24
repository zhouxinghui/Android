package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/3/31.
 */

public class PrepareVersionBean implements Serializable {

    private List<VersionBean> next;
    private List<VersionBean> first;

    public List<VersionBean> getNext() {
        return next;
    }

    public void setNext(List<VersionBean> next) {
        this.next = next;
    }

    public List<VersionBean> getFirst() {
        return first;
    }

    public void setFirst(List<VersionBean> first) {
        this.first = first;
    }

    public static class VersionBean{
        /**
         * bookVersionId : 7
         * bookVersionCode : rj
         * bookVersionName : 人教版
         */

        private String bookVersionId;
        private String bookVersionCode;
        private String bookVersionName;

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
    }
}
