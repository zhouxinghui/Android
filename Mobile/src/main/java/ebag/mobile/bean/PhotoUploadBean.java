package ebag.mobile.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unicho on 2018/3/5.
 */

public class PhotoUploadBean {



    /**
     * classId : c667084b5bdc4b36882a087382d7265e
     * photoGroupId : 670b3ee694ad45e2b57b39be83d1e76e
     * photoUrls : ["https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1465204028,3274624108&fm=27&gp=0.jpg","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4129006494,2238305238&fm=27&gp=0.jpg"]
     * isShare : false
     */

    private String classId;
    private String photoGroupId;
    private String comment;
    private String isShare = "false";
    private List<String> photoUrls = new ArrayList<>();

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getPhotoGroupId() {
        return photoGroupId;
    }

    public void setPhotoGroupId(String photoGroupId) {
        this.photoGroupId = photoGroupId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public void clear(){
        photoUrls.clear();
    }

    public boolean addPhoto(String path){
        return photoUrls.add(path);
    }
}
