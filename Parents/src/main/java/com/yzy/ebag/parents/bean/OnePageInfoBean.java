package com.yzy.ebag.parents.bean;

import java.io.Serializable;
import java.util.List;

public class OnePageInfoBean {


    /**
     * subCode : yw
     * subject : 语文
     * homeWorkComplete : 0
     * homeWorkInfoVos : [{"id":"167dcc196022420c814de7cf88115df6","content":"全部 (共2题)","state":"0","remark":"","endTime":"2018-4-19 23:59","questionCount":2,"questionComplete":0}]
     * homeWorkNoCompleteCount : 1
     */

    private String subCode;
    private String subject;
    private String homeWorkComplete;
    private int homeWorkNoCompleteCount;
    private List<HomeWorkInfoVosBean> homeWorkInfoVos;

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHomeWorkComplete() {
        return homeWorkComplete;
    }

    public void setHomeWorkComplete(String homeWorkComplete) {
        this.homeWorkComplete = homeWorkComplete;
    }

    public int getHomeWorkNoCompleteCount() {
        return homeWorkNoCompleteCount;
    }

    public void setHomeWorkNoCompleteCount(int homeWorkNoCompleteCount) {
        this.homeWorkNoCompleteCount = homeWorkNoCompleteCount;
    }

    public List<HomeWorkInfoVosBean> getHomeWorkInfoVos() {
        return homeWorkInfoVos;
    }

    public void setHomeWorkInfoVos(List<HomeWorkInfoVosBean> homeWorkInfoVos) {
        this.homeWorkInfoVos = homeWorkInfoVos;
    }

    public static class HomeWorkInfoVosBean implements Serializable{
        /**
         * id : 167dcc196022420c814de7cf88115df6
         * content : 全部 (共2题)
         * state : 0
         * remark :
         * endTime : 2018-4-19 23:59
         * questionCount : 2
         * questionComplete : 0
         */

        private String id;
        private String content;
        private String state;
        private String remark;
        private String endTime;
        private int questionCount;
        private int questionComplete;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getQuestionCount() {
            return questionCount;
        }

        public void setQuestionCount(int questionCount) {
            this.questionCount = questionCount;
        }

        public int getQuestionComplete() {
            return questionComplete;
        }

        public void setQuestionComplete(int questionComplete) {
            this.questionComplete = questionComplete;
        }
    }
}
