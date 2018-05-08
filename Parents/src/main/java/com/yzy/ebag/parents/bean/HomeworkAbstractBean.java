package com.yzy.ebag.parents.bean;

import java.util.List;

public class HomeworkAbstractBean {


    /**
     * unitName : 加法
     * parentComment :
     * teacherComment :
     * totalScore : 50
     * maxScore : 50
     * errorNum : 1
     * homeWorkRepDetailVos : [{"questionType":"dx","questionTypeName":"单选题","questionNum":1,"errorCount":0,"questionScore":"100","unitName":"加法"},{"questionType":"pd","questionTypeName":"判断题","questionNum":1,"errorCount":0,"questionScore":"100","unitName":"加法"},{"questionType":"tk","questionTypeName":"填空题","questionNum":1,"errorCount":1,"questionScore":"0","unitName":"加法"},{"questionType":"yy","questionTypeName":"应用题","questionNum":1,"errorCount":0,"questionScore":"0","unitName":"加法"}]
     */

    private String unitName;
    private String parentComment;
    private String teacherComment;
    private int totalScore;
    private int maxScore;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String remark;
    private int errorNum;
    private List<HomeWorkRepDetailVosBean> homeWorkRepDetailVos;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getParentComment() {
        return parentComment;
    }

    public void setParentComment(String parentComment) {
        this.parentComment = parentComment;
    }

    public String getTeacherComment() {
        return teacherComment;
    }

    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }

    public List<HomeWorkRepDetailVosBean> getHomeWorkRepDetailVos() {
        return homeWorkRepDetailVos;
    }

    public void setHomeWorkRepDetailVos(List<HomeWorkRepDetailVosBean> homeWorkRepDetailVos) {
        this.homeWorkRepDetailVos = homeWorkRepDetailVos;
    }

    public static class HomeWorkRepDetailVosBean {
        /**
         * questionType : dx
         * questionTypeName : 单选题
         * questionNum : 1
         * errorCount : 0
         * questionScore : 100
         * unitName : 加法
         */

        private String questionType;
        private String questionTypeName;
        private int questionNum;
        private int errorCount;
        private String questionScore;
        private String unitName;

        public String getQuestionType() {
            return questionType;
        }

        public void setQuestionType(String questionType) {
            this.questionType = questionType;
        }

        public String getQuestionTypeName() {
            return questionTypeName;
        }

        public void setQuestionTypeName(String questionTypeName) {
            this.questionTypeName = questionTypeName;
        }

        public int getQuestionNum() {
            return questionNum;
        }

        public void setQuestionNum(int questionNum) {
            this.questionNum = questionNum;
        }

        public int getErrorCount() {
            return errorCount;
        }

        public void setErrorCount(int errorCount) {
            this.errorCount = errorCount;
        }

        public String getQuestionScore() {
            return questionScore;
        }

        public void setQuestionScore(String questionScore) {
            this.questionScore = questionScore;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }
    }
}
