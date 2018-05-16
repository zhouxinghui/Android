package com.yzy.ebag.student.bean;

import java.io.Serializable;

public class Practise implements Serializable{
    private String pinyin;
    private String hanzi;
    private String audio;
    private boolean selected = false;
    private String localPath;

    public Practise(String pinyin, String hanzi, String audio){
        this.pinyin = pinyin;
        this.hanzi = hanzi;
        this.audio = audio;
    }

    public String getAudio() {
        if(audio == null) audio = "";
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPinyin() {
        if (pinyin == null) pinyin = "";
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getHanzi() {
        if(hanzi == null) hanzi = "";
        return hanzi;
    }

    public void setHanzi(String hanzi) {
        this.hanzi = hanzi;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}