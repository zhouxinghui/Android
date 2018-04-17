package com.yzy.ebag.teacher.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/2/23.
 */

public class TestPaperListBean implements Serializable {

    /**
     * testPaperName : 测试试卷
     * testPaperId : 1
     */

    private String testPaperName;
    private String testPaperId;

    public String getTestPaperName() {
        return testPaperName;
    }

    public void setTestPaperName(String testPaperName) {
        this.testPaperName = testPaperName;
    }

    public String getTestPaperId() {
        return testPaperId;
    }

    public void setTestPaperId(String testPaperId) {
        this.testPaperId = testPaperId;
    }
}
