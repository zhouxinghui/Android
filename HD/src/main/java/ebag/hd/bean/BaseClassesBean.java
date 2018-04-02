package ebag.hd.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/19.
 */

public class BaseClassesBean implements Serializable {

    /**
     * classId : 59bd4d71851044ab9ca4b9c5da8d7815
     * className : 二年级1b
     * gradeCode : 2
     */

    private String classId;
    private String className;
    private String gradeCode;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    private Boolean isChecked = false;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }
}
