package com.yzy.ebag.student.bean;

/**
 * @author caoyu
 * @date 2018/2/8
 * @description
 */

public class ReadDetailBean {


    /**
     * id : null
     * languageDetailId : 11f6d85ac57b440389476b2bf1f4ac51
     * languageId : 0fe2b6b1da47478d878c5ca44b7af4fb
     * languageCn : 早上好
     * languageEn : Good morning
     * languageUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/OralLanguage/1goodmorning.mp3
     * level : 1
     */

    private String id;
    private String languageDetailId;
    private String languageId;
    private String languageCn;
    private String languageEn;
    private String languageUrl;
    private String checkLanguage;
    private String level;
    private String localPath;
    private int position;
    private String score;
    private String myAudioUrl;

    public String getMyAudioUrl() {
        return myAudioUrl;
    }

    public void setMyAudioUrl(String myAudioUrl) {
        this.myAudioUrl = myAudioUrl;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguageDetailId() {
        return languageDetailId;
    }

    public void setLanguageDetailId(String languageDetailId) {
        this.languageDetailId = languageDetailId;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getLanguageCn() {
        return languageCn;
    }

    public void setLanguageCn(String languageCn) {
        this.languageCn = languageCn;
    }

    public String getLanguageEn() {
        return languageEn;
    }

    public void setLanguageEn(String languageEn) {
        this.languageEn = languageEn;
    }

    public String getLanguageUrl() {
        return languageUrl;
    }

    public void setLanguageUrl(String languageUrl) {
        this.languageUrl = languageUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCheckLanguage() {
        return checkLanguage;
    }

    public void setCheckLanguage(String checkLanguage) {
        this.checkLanguage = checkLanguage;
    }
}
