package com.yzy.ebag.parents.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2017/7/7.
 */

public class PresentCollectBean implements Serializable {
    private static final long serialVersionUID = 2464189313962763491L;

    private String name;

    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
