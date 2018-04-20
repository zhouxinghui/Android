package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/1/15.
 */

public class SpaceBean implements Serializable {

    /**
     * classId : 07d4c0d1b000425291a144d3ae983f8d
     * inviteCode : 6MLUMCJP
     * studentCount : 5
     * clazzUserVoList : [{"uid":"1605","name":"张老师","duties":"2","rid":"2","roleName":"老师","headUrl":null}]
     * clazzName : 五年级501班
     */

    private String classId;
    private String gradeCode;
    private String inviteCode;
    private int studentCount;
    private String clazzName;
    private List<ClazzUserVoListBean> clazzUserVoList;

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public List<ClazzUserVoListBean> getClazzUserVoList() {
        return clazzUserVoList;
    }

    public void setClazzUserVoList(List<ClazzUserVoListBean> clazzUserVoList) {
        this.clazzUserVoList = clazzUserVoList;
    }

    public static class ClazzUserVoListBean {
        /**
         * uid : 1605
         * name : 张老师
         * duties : 2
         * rid : 2
         * roleName : 老师
         * headUrl : null
         */

        private String uid;
        private String name;
        private String duties;
        private String rid;
        private String roleName;
        private String headUrl;
        private String sex;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        private String phone;
        private String birthday;

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getYsbCode() {
            return ysbCode;
        }

        public void setYsbCode(String ysbCode) {
            this.ysbCode = ysbCode;
        }

        private String city;
        private String county;
        private String schoolName;
        private String ysbCode;


        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDuties() {
            return duties;
        }

        public void setDuties(String duties) {
            this.duties = duties;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }
    }
}
