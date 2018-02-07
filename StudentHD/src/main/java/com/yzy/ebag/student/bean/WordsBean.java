package com.yzy.ebag.student.bean;

/**
 * @author caoyu
 * @date 2018/2/6
 * @description
 */

public class WordsBean {
    /**
     * page : null
     * pageSize : null
     * id : null
     * unitCode : 7650
     * word : 天,地,人,你,我,他
     * pinYin : tiān,dì,rén,nǐ,wǒ,tā
     * audioUrl : null
     */

    private int page;
    private int pageSize;
    private String id;
    private String unitCode;
    private String word;
    private String pinYin;
    private String audioUrl;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
