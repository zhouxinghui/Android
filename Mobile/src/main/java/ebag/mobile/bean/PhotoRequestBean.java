package ebag.mobile.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unicho on 2018/3/5.
 */

public class PhotoRequestBean {

    /**
     * photoGroupId : 7b93cc259504467ea69e79a61301fd1a
     * groupType : 2
     * photos : ["57cc714d4219432ca9f62da58175534e"]
     */

    private String photoGroupId;
    private String groupType;
    private List<String> photos = new ArrayList<>();

    public String getPhotoGroupId() {
        return photoGroupId;
    }

    public void setPhotoGroupId(String photoGroupId) {
        this.photoGroupId = photoGroupId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public void clear(){
        this.photos.clear();
    }

    public void addPhoto(String photoId){
        this.photos.add(photoId);
    }
}
