package com.yzy.ebag.student.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author caoyu
 * @date 2018/2/1
 * @description
 */

public class Diary implements Serializable{

    private String title;
    private String content;
    private long date;
    private List<String> photos;
    private String photoUrl;



    public Diary(String title, String content, long date, String photoUrl){
        this.title = title;
        this.content = content;
        this.date = date;
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<String> getPhotos() {
        if(photos == null){
            photos = new ArrayList<>();
            if(photoUrl == null){
                return photos;
            }
            Collections.addAll(photos, photoUrl.split(","));
        }
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
