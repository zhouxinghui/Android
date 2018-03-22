package ebag.hd.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/22.
 */

public class ReadRecordAnswerBean implements Serializable {

    /**
     * id : afdadggdgfdfdgdfsdfhfds
     * otherName : null
     * ysbCode : 10000806
     * classId : 8d142957d4a54c9eb107f89e42d2e7d1
     * uid : c494d8e4dbed49b9ba2a9137a6ca5f49
     * languageId : 1623043cf0374667814096762014acb2
     * myAudioUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/OralLanguage/b0795a2dfb854175a97554f441e0634d18.mp3
     * score : null
     * dateTime : 1521698244000
     * languageEn : null
     * languageCn : null
     */

    private String id;
    private String otherName;
    private String ysbCode;
    private String classId;
    private String uid;
    private String languageId;
    private String myAudioUrl;
    private String score;
    private long dateTime;
    private String languageEn;
    private String languageCn;
    private String headUrl;

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getYsbCode() {
        return ysbCode;
    }

    public void setYsbCode(String ysbCode) {
        this.ysbCode = ysbCode;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

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

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getLanguageEn() {
        return languageEn;
    }

    public void setLanguageEn(String languageEn) {
        this.languageEn = languageEn;
    }

    public String getLanguageCn() {
        return languageCn;
    }

    public void setLanguageCn(String languageCn) {
        this.languageCn = languageCn;
    }
}
