package ebag.hd.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unicho on 2018/3/5.
 */

public class PhotoUploadBean {

    private String photoGroupId;
    private String classId;
    private List<String> photoUrls = new ArrayList<>();
    private boolean isShare;

    public String getPhotoGroupId() {
        return photoGroupId;
    }

    public void setPhotoGroupId(String photoGroupId) {
        this.photoGroupId = photoGroupId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public void clear(){
        photoUrls.clear();
    }

    public boolean addPhoto(String path){
        return photoUrls.add(path);
    }
}
