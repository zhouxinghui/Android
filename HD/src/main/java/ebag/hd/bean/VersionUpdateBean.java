package ebag.hd.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/7.
 */

public class VersionUpdateBean implements Serializable {

    /**
     * appName : 老师版 Android HD
     * versionName : 2.3.1
     * type : null
     * url : http://www.yun-bag.com/ebag-portal/file/app/hd/teacher/download.dhtml
     * mark : 老师版 Android HD 1.01
     * versionNumber : 18
     * versionCode : teacher
     * isUpdate : N
     */

    private String appName;
    private String versionName;
    private String type;
    private String url;
    private String mark;
    private String versionNumber;
    private String versionCode;
    private String isUpdate;
    private String isPatch;

    public String getIsPatch() {
        return isPatch;
    }

    public void setIsPatch(String isPatch) {
        this.isPatch = isPatch;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }
}
