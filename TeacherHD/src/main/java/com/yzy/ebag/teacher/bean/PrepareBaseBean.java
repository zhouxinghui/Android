package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

import ebag.hd.bean.UnitBean;

/**
 * Created by YZY on 2018/3/5.
 */

public class PrepareBaseBean implements Serializable {

    /**
     * resultSubjectVo : {"gradeName":"一年级","gradeCode":"1","subCode":"yw","subject":"语文","semesterName":null,"semesterCode":null,"bookVersionId":null,"bookVersionName":null,"resultBookUnitOrCatalogVos":[]}
     * lessonFileInfoVos : [{"id":"1","teacherId":"1727","fileName":"天地人","fileType":".ppt","grade":"1","subjectType":"yw","unitCode":"7951","catalog":null,"length":"1M","fileUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/beike/17582/rj/yy/1/4533/17582_20171130192139986.ppt","createDate":null,"createBy":null,"updateDate":null,"updateBy":null,"remove":null,"share":"1","subCode":null}]
     */

    private ResultSubjectVoBean resultSubjectVo;
    private List<PrepareFileBean> lessonFileInfoVos;

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

    public static class ResultSubjectVoBean implements Serializable{
        /**
         * gradeName : 一年级
         * gradeCode : 1
         * subCode : yw
         * subject : 语文
         * semesterName : null
         * semesterCode : null
         * bookVersionId : null
         * bookVersionName : null
         * resultBookUnitOrCatalogVos : []
         */

        private String gradeName;
        private String gradeCode;
        private String subCode;
        private String subject;
        private String semesterName;
        private String semesterCode;
        private String bookVersionId;
        private String bookVersionName;
        private List<UnitBean> resultBookUnitOrCatalogVos;

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public String getGradeCode() {
            return gradeCode;
        }

        public void setGradeCode(String gradeCode) {
            this.gradeCode = gradeCode;
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

        public String getSemesterName() {
            return semesterName;
        }

        public void setSemesterName(String semesterName) {
            this.semesterName = semesterName;
        }

        public String getSemesterCode() {
            return semesterCode;
        }

        public void setSemesterCode(String semesterCode) {
            this.semesterCode = semesterCode;
        }

        public String getBookVersionId() {
            return bookVersionId;
        }

        public void setBookVersionId(String bookVersionId) {
            this.bookVersionId = bookVersionId;
        }

        public String getBookVersionName() {
            return bookVersionName;
        }

        public void setBookVersionName(String bookVersionName) {
            this.bookVersionName = bookVersionName;
        }

        public List<UnitBean> getResultBookUnitOrCatalogVos() {
            return resultBookUnitOrCatalogVos;
        }

        public void setResultBookUnitOrCatalogVos(List<UnitBean> resultBookUnitOrCatalogVos) {
            this.resultBookUnitOrCatalogVos = resultBookUnitOrCatalogVos;
        }
    }
}
