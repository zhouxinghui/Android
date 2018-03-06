package ebag.hd.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import ebag.hd.activity.AlbumDetailActivity;

/**
 * 合并了一下外层的 有createDate 和 photoUrls两个字段
 * 真正的photo 只有Id 和 photoUrl字段
 * Created by unicho on 2018/3/2.
 */

public class PhotoBean implements MultiItemEntity {


    private String id;
    private String photoUrl;

    private String createDate;
    private List<PhotoBean> photoUrls;

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<PhotoBean> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<PhotoBean> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isPhoto(){
        return photoUrls == null && id != null;
    }
    @Override
    public int getItemType() {
        if(id == null && photoUrl == null)
            return AlbumDetailActivity.Adapter.TYPE_STICKY_HEAD;
        return AlbumDetailActivity.Adapter.TYPE_DATA;
    }
}
