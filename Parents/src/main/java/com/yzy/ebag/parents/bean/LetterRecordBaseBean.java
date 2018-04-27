package com.yzy.ebag.parents.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/19.
 */

public class LetterRecordBaseBean implements Serializable {

    /**
     * page : null
     * pageSize : null
     * createDate : 1520179200000
     * classId : c667084b5bdc4b36882a087382d7265e
     * className : 一班
     * unitId : 930
     * uid : 1592
     * words : null
     * wordUrl : null
     * score : null
     * ysbCode : null
     * name : null
     * timeLength : 1
     */

    private long createDate;
    private String classId;
    private String className;
    private String unitId;
    private String uid;
    private String words;
    private String wordUrl;
    private String score;
    private String ysbCode;
    private String name;
    private int timeLength;

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getYsbCode() {
        return ysbCode;
    }

    public void setYsbCode(String ysbCode) {
        this.ysbCode = ysbCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }
}
