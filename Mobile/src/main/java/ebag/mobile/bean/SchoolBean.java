package ebag.mobile.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/1/19.
 */

public class SchoolBean implements Serializable {

    /**
     * id : 1037
     * schoolName : 龙岗兴泰实验学校
     * phase :
     */

    private String id;
    private String schoolName;
    private String phase;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }
}
