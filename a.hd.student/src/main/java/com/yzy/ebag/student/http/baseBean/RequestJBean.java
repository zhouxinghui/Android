package com.yzy.ebag.student.http.baseBean;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

/**
 * Created by caoyu on 2017/8/28.
 */

public class RequestJBean<T> extends JSONObject {

    public RequestJBean(T t,String token){

    }
    private String token;
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

    public T getBody() {
        return (T) JSON.parse(body);
    }

    public void setBody(T body) {
        this.body = JSON.toJSONString(body);
    }
}
