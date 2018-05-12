package com.yzy.ebag.student.bean;

import java.io.Serializable;

/**
 * Created by unicho on 2018/3/16.
 */

public class SpeechRecognizeVo implements Serializable{

    // 语音压缩的格式
    private String format = "amr";

    /**
     * 注意，采样率的数据类型一定是 int，不能是 String
     */
    // 采样率，支持 8000 或者 16000，在我们的项目中，写 16000
    private int rate = 8000;
    // 声道数，仅支持单声道，请填写 1
    private String channel = "1";

    private int dev_pid = 1737;
    // 开发者身份验证密钥
    private String token;
    // 用户 ID，推荐使用设备 mac 地址 手机 IMEI 等设备唯一性参数
    // todo 貌似可以随意填写，唯一即可
    private String cuid = "6985075029673953359";
//    private String cuid = "6985075029673953359";

    /**
     * 注意：这里填写的是原始语音的长度，不是使用 base64 编码的语音长度
     */
    // 原始语音长度，单位字节
    private long len;
    // 真实的语音数据，需要进行 base64 编码
    private String speech;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getDev_pid() {
        return dev_pid;
    }

    public void setDev_pid(int dev_pid) {
        this.dev_pid = dev_pid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public long getLen() {
        return len;
    }

    public void setLen(long len) {
        this.len = len;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }
}
