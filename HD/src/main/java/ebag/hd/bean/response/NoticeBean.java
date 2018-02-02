package ebag.hd.bean.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by YZY on 2018/1/23.
 */

public class NoticeBean implements Serializable {

    /**
     * classId : b1957161b7084e4681fe7443f0755e0c
     * content : 放假就几个
     * photoUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/5/1605/ht/201801222014340,http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/5/1605/ht/201801222014341
     * name : 张老师
     * headUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/usericon/default/teacher.png
     * createDate : 1516623323000
     */

    private String classId;
    private String content;
    private String photoUrl;
    private String name;
    private String headUrl;
    private long createDate;
    private List<String> photoList;

    public List<String> getPhotos() {
        if(photoList == null){
            photoList = new ArrayList<>();
            if(photoUrl == null){
                return photoList;
            }
            Collections.addAll(photoList, photoUrl.split(","));
        }
        return photoList;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
