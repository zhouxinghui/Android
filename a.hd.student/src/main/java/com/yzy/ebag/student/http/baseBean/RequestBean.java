package com.yzy.ebag.student.http.baseBean;

import com.alibaba.fastjson.JSON;

/**
 * Created by caoyu on 2017/8/28.
 */

public class RequestBean<T> {

    private String token = "ee60ZT8U5lPeAc6Op94EFE4xToQxbCV1ttod7KGLcL2rCtPtqfhu4qhNhVkgKXqyfq2bPF\\/Oho8=";
    private String millis = String.valueOf(System.currentTimeMillis());
    private String body;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMillis() {
        return millis;
    }

    public void setMillis(String millis) {
        this.millis = millis;
    }

    public String getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = JSON.toJSONString(body);
    }
}
