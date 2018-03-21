package ebag.hd.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/21.
 */

public class ReadRecordVoiceBean implements Serializable {

    /**
     * id : null
     * languageDetailId : 8d7c30d3cb6442788118990cd5a6dd22
     * languageId : 86ccb2292f4a4a5b92fd2011c729697b
     * languageCn : 你看到了什么？
     * languageEn : What do you see?
     * languageUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/OralLanguage/435a54adedd04e448bccb249a182df4e1.mp3
     * level : 1
     * checkLanguage : N
     * uid : b7f7ec79f49a4a26ac86e21432ec628b
     */

    private String id;
    private String languageDetailId;
    private String languageId;
    private String languageCn;
    private String languageEn;
    private String languageUrl;
    private String level;
    private String checkLanguage;
    private String uid;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
