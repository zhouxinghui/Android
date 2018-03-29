package com.yzy.ebag.student.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/3/29.
 */

public class WordRecordBean implements Serializable {

    /**
     * total : 6
     * pages : 1
     * list : [{"versionName":"人教版 ","subJectName":"语文","gradeName":"二年级","semesterName":"下学期","unitName":"第六单元","name":"要是你在野外迷了路","createDate":"2018-02-01 18:02:03.0","words":"哈哈哈哈","wordUrl":"http://aa.com,http://bb.com,http://cc.com,http://dd.com,http://ee.com","score":"7"},{"versionName":"人教版 ","subJectName":"语文","gradeName":"二年级","semesterName":"下学期","unitName":"第六单元","name":"要是你在野外迷了路","createDate":"2018-02-01 18:01:42.0","words":"哈哈哈哈","wordUrl":"http://aa.com,http://bb.com,http://cc.com,http://dd.com,http://ee.com","score":"3"},{"versionName":"人教版 ","subJectName":"语文","gradeName":"二年级","semesterName":"下学期","unitName":"第六单元","name":"要是你在野外迷了路","createDate":"2018-02-01 17:58:08.0","words":"哈哈哈哈","wordUrl":"http://aa.com,http://bb.com,http://cc.com,http://dd.com,http://ee.com","score":"1"},{"versionName":"人教版 ","subJectName":"语文","gradeName":"二年级","semesterName":"下学期","unitName":"第六单元","name":"雷雨","createDate":"2018-02-01 17:58:05.0","words":"哈哈哈哈","wordUrl":"http://aa.com,http://bb.com,http://cc.com,http://dd.com,http://ee.com","score":"2"},{"versionName":"人教版 ","subJectName":"语文","gradeName":"二年级","semesterName":"下学期","unitName":"第六单元","name":"古诗二首","createDate":"2018-02-01 17:57:48.0","words":"哈哈哈哈","wordUrl":"http://aa.com,http://bb.com,http://cc.com,http://dd.com,http://ee.com","score":"4"},{"versionName":"人教版 ","subJectName":"语文","gradeName":"二年级","semesterName":"下学期","unitName":"第五单元","name":"口语交际：图书借阅公约","createDate":"2018-02-01 17:56:58.0","words":"哈哈哈哈","wordUrl":"http://aa.com,http://bb.com,http://cc.com,http://dd.com,http://ee.com","score":"5"}]
     */

    private int total;
    private int pages;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * versionName : 人教版
         * subJectName : 语文
         * gradeName : 二年级
         * semesterName : 下学期
         * unitName : 第六单元
         * name : 要是你在野外迷了路
         * createDate : 2018-02-01 18:02:03.0
         * words : 哈哈哈哈
         * wordUrl : http://aa.com,http://bb.com,http://cc.com,http://dd.com,http://ee.com
         * score : 7
         */

        private String versionName;
        private String subJectName;
        private String gradeName;
        private String semesterName;
        private String unitName;
        private String name;
        private long createDate;
        private String words;
        private String wordUrl;
        private String score;

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getSubJectName() {
            return subJectName;
        }

        public void setSubJectName(String subJectName) {
            this.subJectName = subJectName;
        }

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public String getSemesterName() {
            return semesterName;
        }

        public void setSemesterName(String semesterName) {
            this.semesterName = semesterName;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public String getWordUrl() {
            return wordUrl;
        }

        public void setWordUrl(String wordUrl) {
            this.wordUrl = wordUrl;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }
}
