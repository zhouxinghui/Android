package ebag.hd.bean;

import java.util.List;

/**
 * Created by fansan on 2018/5/25 15:26
 */

public class GiftTeacherBean {

    private List<GiftListBean> teacher;

    public List<GiftListBean> getTeacher() {
        return teacher;
    }

    public void setTeacher(List<GiftListBean> teacher) {
        this.teacher = teacher;
    }

    public List<GiftListBean> getParent() {
        return parent;
    }

    public void setParent(List<GiftListBean> parent) {
        this.parent = parent;
    }

    private List<GiftListBean> parent;
}
