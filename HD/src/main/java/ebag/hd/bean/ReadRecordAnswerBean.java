package ebag.hd.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/22.
 */

public class ReadRecordAnswerBean implements Serializable {

    /**
     * id : 35afb5e8e6f040ccbdbec88138a3ecba
     * headUrl : 
     * name : 测试学生
     * ysbCode : 10000926
     * classId : ed6f351a1202473abc7c160a95435ab6
     * uid : 0e540878d646474a86f35ff2e0160639
     * languageId : 0fe2b6b1da47478d878c5ca44b7af4fb
     * myAudioUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/0e540878d646474a86f35ff2e0160639/read/3eeba959f1cf4538825c6a2f6307cb73.amr
     * score : null
     * dateTime : 1523415870000
     * languageEn : null
     * languageCn : null
     */

    private String id;
    private String headUrl;
    private String name;
    private String ysbCode;
    private String classId;
    private String uid;
    private String languageId;
    private String myAudioUrl;
    private String score;
    private long dateTime;
    private String languageEn;
    private String languageCn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
