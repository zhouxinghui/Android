package com.yzy.ebag.teacher.bean;

import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yzy.ebag.teacher.base.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZY on 2018/2/4.
 */

public class CorrectingBean implements IExpandable<CorrectingBean.SubjectVosBean>, MultiItemEntity, Serializable{
    private boolean isExpand = false;
    /**
     * classId : 226d966a5f954180b21fddba11082082
     * className : 一班
     * subjectVos : [{"subCode":"yw","subject":"语文","homeWorkInfoVos":[{"id":"1d2bd7bbc15b44d0aca85efabbe4c47b","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"2af0fe4de9aa43f88bdb42999e163e79","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"2bc1b70dece14d78818e8e0b1d17a3c4","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"62a8c1dfc8a84863a6f26b2255e4ccba","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"7c55aaaf050c42e3b7428bd0e86eeefa","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"7e121e4f777a46089585c56a11d64c1f","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"94991c3b12c44cdb8ad08610757b9ab1","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"d20d7d7120314564a36620f605a23c13","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"fb138b5ca6f04613877ff9b0857a13e7","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null}]}]
     */

    private String classId;
    private String className;
    private ArrayList<SubjectVosBean> subjectVos;

    @Override
    public boolean isExpanded() {
        return isExpand;
    }

    @Override
    public void setExpanded(boolean expanded) {
        isExpand = expanded;
    }

    @Override
    public List<SubjectVosBean> getSubItems() {
        return subjectVos;
    }

    @Override
    public int getLevel() {
        return Constants.INSTANCE.getLEVEL_ONE();
    }

    @Override
    public int getItemType() {
        return Constants.INSTANCE.getLEVEL_ONE();
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

    public ArrayList<SubjectVosBean> getSubjectVos() {
        for (SubjectVosBean subBean : subjectVos) {
            subBean.setClassId(classId);
        }
        return subjectVos;
    }

    public void setSubjectVos(ArrayList<SubjectVosBean> subjectVos) {
        this.subjectVos = subjectVos;
    }


    public static class SubjectVosBean implements MultiItemEntity, Serializable{
        /**
         * subCode : yw
         * subject : 语文
         * homeWorkInfoVos : [{"id":"1d2bd7bbc15b44d0aca85efabbe4c47b","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"2af0fe4de9aa43f88bdb42999e163e79","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"2bc1b70dece14d78818e8e0b1d17a3c4","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"62a8c1dfc8a84863a6f26b2255e4ccba","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"7c55aaaf050c42e3b7428bd0e86eeefa","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"7e121e4f777a46089585c56a11d64c1f","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"94991c3b12c44cdb8ad08610757b9ab1","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"d20d7d7120314564a36620f605a23c13","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null},{"id":"fb138b5ca6f04613877ff9b0857a13e7","content":"","className":"一班","groupName":null,"state":"0","endTime":null,"studentCount":null,"homeWorkCompleteCount":null}]
         */

        private String subCode;
        private String subject;
        private String classId;
        private List<HomeWorkInfoVosBean> homeWorkInfoVos;

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getSubCode() {
            return subCode;
        }

        public void setSubCode(String subCode) {
            this.subCode = subCode;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public List<HomeWorkInfoVosBean> getHomeWorkInfoVos() {
            return homeWorkInfoVos;
        }

        public void setHomeWorkInfoVos(List<HomeWorkInfoVosBean> homeWorkInfoVos) {
            this.homeWorkInfoVos = homeWorkInfoVos;
        }

        @Override
        public int getItemType() {
            return Constants.INSTANCE.getLEVEL_TWO();
        }

        public static class HomeWorkInfoVosBean implements Serializable{
            /**
             * id : 1d2bd7bbc15b44d0aca85efabbe4c47b
             * content : 
             * className : 一班
             * groupName : null
             * state : 0
             * endTime : null
             * studentCount : null
             * homeWorkCompleteCount : null
             */

            private String id;
            private String content;
            private String className;
            private String groupName;
            private String state;
            private String endTime;
            private String studentCount;
            private String homeWorkCompleteCount;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getStudentCount() {
                return studentCount;
            }

            public void setStudentCount(String studentCount) {
                this.studentCount = studentCount;
            }

            public String getHomeWorkCompleteCount() {
                return homeWorkCompleteCount;
            }

            public void setHomeWorkCompleteCount(String homeWorkCompleteCount) {
                this.homeWorkCompleteCount = homeWorkCompleteCount;
            }
        }
    }
}
