package com.yzy.ebag.teacher.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/1/20.
 */

public class BaseSubjectBean implements Serializable {
    /**
     * id : 4862
     * groupCode : grade_subject
     * groupName : 年级_科目
     * keyCode : yw
     * keyValue : 语文
     * parentCode : 1
     */

    private int id;
    private String groupCode;
    private String groupName;
    private String keyCode;
    private String keyValue;
    private String parentCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);

    }
}
