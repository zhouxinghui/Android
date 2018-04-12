package com.yzy.ebag.student.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/4/12.
 */

public class ReadUploadResponseBean implements Serializable {

    /**
     * score : 11.22
     * hightingString :
     */

    private String score;
    private String hightingString;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHightingString() {
        return hightingString;
    }

    public void setHightingString(String hightingString) {
        this.hightingString = hightingString;
    }
}
