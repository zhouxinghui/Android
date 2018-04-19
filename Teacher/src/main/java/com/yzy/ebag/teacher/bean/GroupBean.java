package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

import ebag.mobile.bean.BaseStudentBean;


/**
 * Created by YZY on 2018/1/17.
 */

public class GroupBean implements Serializable {

    /**
     * groupId : 14a8b4e4ca614b029879f84226daec77
     * groupName : 小组名称a
     * studentCount : 2
     * clazzUserVos : [{"uid":"","name":"学生A","duties":"4","rid":"1","roleName":"","headUrl":""}]
     */

    private String groupId;
    private String groupName;
    private String studentCount;
    private List<ClazzUserVosBean> clazzUserVos;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(String studentCount) {
        this.studentCount = studentCount;
    }

    public List<ClazzUserVosBean> getClazzUserVos() {
        return clazzUserVos;
    }

    public void setClazzUserVos(List<ClazzUserVosBean> clazzUserVos) {
        this.clazzUserVos = clazzUserVos;
    }

    public static class ClazzUserVosBean extends BaseStudentBean {
        /**
         * uid :
         * name : 学生A
         * duties : 4
         * rid : 1
         * roleName :
         * headUrl :
         */
        private String rid;
        private String roleName;
        private String headUrl;

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
