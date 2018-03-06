package com.yzy.ebag.student.bean;

import java.io.Serializable;

/**
 * Created by unicho on 2018/3/5.
 */

public class GroupUserBean implements Serializable{
    /**
     * uid : 1592
     * name : test学生
     * subCode : null
     * duties : 3
     * rid : 1
     * roleName : null
     * headUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/11/17675/ht/201708100307302
     */

    private String uid;
    private String name;
    private Object subCode;
    private String duties;
    private String rid;
    private Object roleName;
    private String headUrl;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getSubCode() {
        return subCode;
    }

    public void setSubCode(Object subCode) {
        this.subCode = subCode;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Object getRoleName() {
        return roleName;
    }

    public void setRoleName(Object roleName) {
        this.roleName = roleName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
}
