package ebag.mobile.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/1/18.
 */

public class ClassMemberBean implements Serializable {

    private List<StudentsBean> students;
    private List<TeachersBean> teachers;
    private List<ParentsBean> parents;

    public List<StudentsBean> getStudents() {
        return students;
    }

    public void setStudents(List<StudentsBean> students) {
        this.students = students;
    }

    public List<TeachersBean> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeachersBean> teachers) {
        this.teachers = teachers;
    }

    public List<ParentsBean> getParents() {
        return parents;
    }

    public void setParents(List<ParentsBean> parents) {
        this.parents = parents;
    }

    public static class StudentsBean extends BaseStudentBean {
        /**
         * uid : 1730
         * name : 学生A
         * duties : 3
         * rid : 1
         * roleName : 学生
         * headUrl :
         * hasGroup ：false
         */

        private String rid;
        private String roleName;
        private String headUrl;
        private String subCode;
        private String duties;
        private String sex;
        private String birthday;
        private String city;
        private String county;
        private String schoolName;
        private String ysbCode;
        private boolean hasGroup;

        public boolean isHasGroup() {
            return hasGroup;
        }

        public void setHasGroup(boolean hasGroup) {
            this.hasGroup = hasGroup;
        }

        public String getSubCode() {
            return subCode;
        }

        public void setSubCode(String subCode) {
            this.subCode = subCode;
        }

        @Override
        public String getDuties() {
            return duties;
        }

        @Override
        public void setDuties(String duties) {
            this.duties = duties;
        }

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        private String phone;


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

    public static class TeachersBean {
        /**
         * uid : 1605
         * name : 张老师
         * duties : 2
         * rid : 2
         * roleName : 老师
         * headUrl :
         */

        private String uid;
        private String name;
        private String duties;
        private String rid;
        private String roleName;
        private String headUrl;

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

    public static class ParentsBean {
        /**
         * uid : 1629
         * name : Jeff
         * duties : 1
         * rid : 2
         * roleName : 老师
         * headUrl :
         */

        private String uid;
        private String name;
        private String duties;
        private String rid;
        private String roleName;
        private String headUrl;

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
