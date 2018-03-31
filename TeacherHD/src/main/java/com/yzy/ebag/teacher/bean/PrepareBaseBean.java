package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

import ebag.hd.bean.UnitBean;

/**
 * Created by YZY on 2018/3/5.
 */

public class PrepareBaseBean implements Serializable {
    private ResultSubjectVoBean resultSubjectVo;
    private List<PrepareFileBean> lessonFileInfoVos;
    private List<UnitBean> resultBookUnitOrCatalogVos;

    public ResultSubjectVoBean getResultSubjectVo() {
        return resultSubjectVo;
    }

    public void setResultSubjectVo(ResultSubjectVoBean resultSubjectVo) {
        this.resultSubjectVo = resultSubjectVo;
    }

    public List<PrepareFileBean> getLessonFileInfoVos() {
        return lessonFileInfoVos;
    }

    public void setLessonFileInfoVos(List<PrepareFileBean> lessonFileInfoVos) {
        this.lessonFileInfoVos = lessonFileInfoVos;
    }

    public List<UnitBean> getResultBookUnitOrCatalogVos() {
        return resultBookUnitOrCatalogVos;
    }

    public void setResultBookUnitOrCatalogVos(List<UnitBean> resultBookUnitOrCatalogVos) {
        this.resultBookUnitOrCatalogVos = resultBookUnitOrCatalogVos;
    }

    public static class ResultSubjectVoBean{
        private String id;// "25", // 无用字段
        private String groupCode;// null,// 无用字段
        private String groupName;// null,// 无用字段
        private String subCode;// "yy", // 科目编码
        private String subject;// "英语", // 科目
        private String gradeCode;// "1",//年级编码
        private String gradeName;// "一年级",//年级
        private String semesterCode;// "2",//学期编码
        private String semesterName;// "下学期"//学期

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroupCode() {
            return groupCode;
        }

        public void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
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

        public String getGradeCode() {
            return gradeCode;
        }

        public void setGradeCode(String gradeCode) {
            this.gradeCode = gradeCode;
        }

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public String getSemesterCode() {
            return semesterCode;
        }

        public void setSemesterCode(String semesterCode) {
            this.semesterCode = semesterCode;
        }

        public String getSemesterName() {
            return semesterName;
        }

        public void setSemesterName(String semesterName) {
            this.semesterName = semesterName;
        }
    }
}
