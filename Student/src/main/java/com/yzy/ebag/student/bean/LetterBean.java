package com.yzy.ebag.student.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by caoyu on 2018/1/14.
 */

public class LetterBean implements MultiItemEntity{

    public static final int GROUP_TYPE = 1;
    public static final int NORMAL_TYPE = 0;

    private int itemType = NORMAL_TYPE;
    private String letters;
    private String content;
    private String mp3;

    public LetterBean(String letters, String content, String mp3){
        this.letters = letters;
        this.content = content;
        this.mp3 = mp3;
    }

    public LetterBean(String content){
        this.content = content;
        this.itemType = GROUP_TYPE;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }
}
