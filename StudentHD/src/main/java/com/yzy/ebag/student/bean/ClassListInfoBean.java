package com.yzy.ebag.student.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author caoyu
 * @date 2018/1/16
 * @description
 */

public class ClassListInfoBean implements Parcelable{


    /**
     * classId : 07d4c0d1b000425291a144d3ae983f8d
     * className : 5501班
     * gradeCode : 5
     */

    private String classId;
    private String className;
    private String gradeCode;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.classId);
        dest.writeString(this.className);
        dest.writeString(this.gradeCode);
    }

    public ClassListInfoBean() {
    }

    protected ClassListInfoBean(Parcel in) {
        this.classId = in.readString();
        this.className = in.readString();
        this.gradeCode = in.readString();
    }

    public static final Creator<ClassListInfoBean> CREATOR = new Creator<ClassListInfoBean>() {
        @Override
        public ClassListInfoBean createFromParcel(Parcel source) {
            return new ClassListInfoBean(source);
        }

        @Override
        public ClassListInfoBean[] newArray(int size) {
            return new ClassListInfoBean[size];
        }
    };
}
