package ebag.mobile.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/1/18.
 */

public class BaseStudentBean implements Serializable {
    private String uid;
    private String duties;
    private String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseStudentBean)
            return uid.equals(((BaseStudentBean) obj).uid);
        return super.equals(obj);
    }
}
