package com.yzy.ebag.student.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by unicho on 2018/3/5.
 */

public class GroupBean implements Serializable{

    /**
     * groupId : 72270252866d4ea7a47ad5709ebb59cd
     * groupName : 测试小组
     * studentCount : 1
     * clazzUserVos : [{"uid":"1592","name":"test学生","subCode":null,"duties":"3","rid":"1","roleName":null,"headUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/personal/11/17675/ht/201708100307302"},{"uid":"bb5c5dc8065e44229695134570c9d3c5","name":"小红","subCode":null,"duties":"3","rid":"1","roleName":null,"headUrl":null},{"uid":"5064d86cb55c4da4bf645af028cf7ea4","name":"1号","subCode":null,"duties":"3","rid":"1","roleName":null,"headUrl":null}]
     */

    private String groupId;
    private String groupName;
    private String studentCount;
    private ArrayList<GroupUserBean> clazzUserVos;

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

    public ArrayList<GroupUserBean> getClazzUserVos() {
        return clazzUserVos;
    }

    public void setClazzUserVos(ArrayList<GroupUserBean> clazzUserVos) {
        this.clazzUserVos = clazzUserVos;
    }

}
