package com.yzy.ebag.student.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author caoyu
 * @date 2018/2/1
 * @description
 */

public class Diary implements Serializable{


    /**
     * dateTimes : 2018-03
     * resultUserGrowthByPageVo : {"total":2,"pages":1,"userGrowthResultVoList":[{"id":22,"gradeName":"一年级数学","type":"4","title":"土豪哥哥","content":"吃饭方法","image":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/diary/1/1522144785077,","createBy":"3cb87cd7f1c24798a2d243eaf844807e","createDate":1522144776000,"removed":null,"uid":"3cb87cd7f1c24798a2d243eaf844807e","gradeCode":"二年级"},{"id":21,"gradeName":"一年级数学","type":"4","title":"哈哈哈","content":"停停停","image":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/diary/1/1522144042821,","createBy":"3cb87cd7f1c24798a2d243eaf844807e","createDate":1522144034000,"removed":null,"uid":"3cb87cd7f1c24798a2d243eaf844807e","gradeCode":"二年级"}]}
     */

    private String dateTimes;
    private ResultUserGrowthByPageVoBean resultUserGrowthByPageVo;

    public String getDateTimes() {
        return dateTimes;
    }

    public void setDateTimes(String dateTimes) {
        this.dateTimes = dateTimes;
    }

    public ResultUserGrowthByPageVoBean getResultUserGrowthByPageVo() {
        return resultUserGrowthByPageVo;
    }

    public void setResultUserGrowthByPageVo(ResultUserGrowthByPageVoBean resultUserGrowthByPageVo) {
        this.resultUserGrowthByPageVo = resultUserGrowthByPageVo;
    }

    public static class ResultUserGrowthByPageVoBean {
        /**
         * total : 2
         * pages : 1
         * userGrowthResultVoList : [{"id":22,"gradeName":"一年级数学","type":"4","title":"土豪哥哥","content":"吃饭方法","image":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/diary/1/1522144785077,","createBy":"3cb87cd7f1c24798a2d243eaf844807e","createDate":1522144776000,"removed":null,"uid":"3cb87cd7f1c24798a2d243eaf844807e","gradeCode":"二年级"},{"id":21,"gradeName":"一年级数学","type":"4","title":"哈哈哈","content":"停停停","image":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/diary/1/1522144042821,","createBy":"3cb87cd7f1c24798a2d243eaf844807e","createDate":1522144034000,"removed":null,"uid":"3cb87cd7f1c24798a2d243eaf844807e","gradeCode":"二年级"}]
         */

        private int total;
        private int pages;
        private List<UserGrowthResultVoListBean> userGrowthResultVoList;

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

        public List<UserGrowthResultVoListBean> getUserGrowthResultVoList() {
            return userGrowthResultVoList;
        }

        public void setUserGrowthResultVoList(List<UserGrowthResultVoListBean> userGrowthResultVoList) {
            this.userGrowthResultVoList = userGrowthResultVoList;
        }

        public static class UserGrowthResultVoListBean implements Serializable{
            /**
             * id : 22
             * gradeName : 一年级数学
             * type : 4
             * title : 土豪哥哥
             * content : 吃饭方法
             * image : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/diary/1/1522144785077,
             * createBy : 3cb87cd7f1c24798a2d243eaf844807e
             * createDate : 1522144776000
             * removed : null
             * uid : 3cb87cd7f1c24798a2d243eaf844807e
             * gradeCode : 二年级
             */

            private int id;
            private String gradeName;
            private String type;
            private String title;
            private String content;
            private String image;
            private String createBy;
            private long createDate;
            private Object removed;
            private String uid;
            private String gradeCode;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGradeName() {
                return gradeName;
            }

            public void setGradeName(String gradeName) {
                this.gradeName = gradeName;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public Object getRemoved() {
                return removed;
            }

            public void setRemoved(Object removed) {
                this.removed = removed;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getGradeCode() {
                return gradeCode;
            }

            public void setGradeCode(String gradeCode) {
                this.gradeCode = gradeCode;
            }
        }
    }
}
