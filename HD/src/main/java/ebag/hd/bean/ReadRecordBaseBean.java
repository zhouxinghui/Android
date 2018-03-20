package ebag.hd.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/19.
 */

public class ReadRecordBaseBean implements Serializable {

    /**
     * dateTime : 1521475200000
     * studentCount : 2
     * classId : 8d142957d4a54c9eb107f89e42d2e7d1
     * className : B班
     */

    private long dateTime;
    private String studentCount;
    private String classId;
    private String className;
    private String languageId;

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(String studentCount) {
        this.studentCount = studentCount;
    }

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
}
