package com.yzy.ebag.teacher.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/1.
 */

public class CommentBean implements Serializable {

    /**
     * uid : 1592
     * studentName : test学生
     * headUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/11/17675/ht/201708100307302
     * comment : 你的作业完成的很好，希望继续保持，加油！！！
     * totalScore : 0.00
     * correctState : 3
     */

    private String uid;
    private String studentName;
    private String headUrl;
    private String comment;
    private String totalScore;
    private String correctState;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getCorrectState() {
        return correctState;
    }

    public void setCorrectState(String correctState) {
        this.correctState = correctState;
    }
}
