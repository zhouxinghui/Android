package com.yzy.ebag.teacher.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by YZY on 2018/2/28.
 */

public class CorrectAnswerBean implements Serializable,MultiItemEntity {

    /**
     * ysbCode : 1000722
     * uid : 1593
     * studentAnswer : 话#R#高#R#说#R#话
     * headUrl : null
     * studentName : jeff.zheng
     * endTime : 2018-02-27 16:55:34.0
     * homeWorkState : 2
     * questionScore : 100
     * qid : 1
     */

    private String ysbCode;
    private String uid;
    private String studentAnswer;
    private String headUrl;
    private String studentName;
    private Long endTime;
    private String homeWorkState;
    private String questionScore;
    private String qid;
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public String getYsbCode() {
        return ysbCode;
    }

    public void setYsbCode(String ysbCode) {
        this.ysbCode = ysbCode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getHomeWorkState() {
        return homeWorkState;
    }

    public void setHomeWorkState(String homeWorkState) {
        this.homeWorkState = homeWorkState;
    }

    public String getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(String questionScore) {
        this.questionScore = questionScore;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
