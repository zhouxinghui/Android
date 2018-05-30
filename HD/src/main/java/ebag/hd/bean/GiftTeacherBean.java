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

    public List<GiftListBean> getParent2teacher() {
        return parent2teacher;
    }

    public void setParent2teacher(List<GiftListBean> parent2teacher) {
        this.parent2teacher = parent2teacher;
    }

    private List<GiftListBean> parent2teacher;
}
