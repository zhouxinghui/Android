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
    private BookVersionOrUnitVo bookVersionOrUnitVo;
    private List<PrepareFileBean> lessonFileInfoVos;
    private List<UnitBean> resultBookUnitOrCatalogVos;

    public ResultSubjectVoBean getResultSubjectVo() {
        return resultSubjectVo;
    }

    public void setResultSubjectVo(ResultSubjectVoBean resultSubjectVo) {
        this.resultSubjectVo = resultSubjectVo;
    }

    public BookVersionOrUnitVo getBookVersionOrUnitVo() {
        return bookVersionOrUnitVo;
    }

    public void setBookVersionOrUnitVo(BookVersionOrUnitVo bookVersionOrUnitVo) {
        this.bookVersionOrUnitVo = bookVersionOrUnitVo;
    }

    public List<UnitBean> getResultBookUnitOrCatalogVos() {
        return resultBookUnitOrCatalogVos;
    }

    public void setResultBookUnitOrCatalogVos(List<UnitBean> resultBookUnitOrCatalogVos) {
        this.resultBookUnitOrCatalogVos = resultBookUnitOrCatalogVos;
    }

    public List<PrepareFileBean> getLessonFileInfoVos() {
        return lessonFileInfoVos;
    }

    public void setLessonFileInfoVos(List<PrepareFileBean> lessonFileInfoVos) {
        this.lessonFileInfoVos = lessonFileInfoVos;
    }

    public static class ResultSubjectVoBean implements Serializable{

        /**
         * id : 19
         * groupCode : 1
         * groupName : null
         * subCode : yy
         * subject : 英语
         * gradeCode : 1
         * gradeName : 一年级
         */

        private String id;
        private String groupCode;
        private String groupName;
        private String subCode;
        private String subject;
        private String gradeCode;
        private String gradeName;

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
    }

    public static class BookVersionOrUnitVo implements Serializable{

        /**
         * id : 1
         * versionCode : rj
         * versionName : 人教版  
         * subjectCode : sx
         * subjectName : 数学
         * gradeCode : 1
         * gradeName : null
         * semesterCode : 2
         * semesterName : 下学期
         * bookCode : 736
         * code : null
         * name : null
         * bookVersionId : null
         * pid : null
         * unitCode : null
         */

        private String id;
        private String versionCode;
        private String versionName;
        private String subjectCode;
        private String subjectName;
        private String gradeCode;
        private String gradeName;
        private String semesterCode;
        private String semesterName;
        private String bookCode;
        private String code;
        private String name;
        private String bookVersionId;
        private String pid;
        private String unitCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getSubjectCode() {
            return subjectCode;
        }

        public void setSubjectCode(String subjectCode) {
            this.subjectCode = subjectCode;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
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

        public String getBookCode() {
            return bookCode;
        }

        public void setBookCode(String bookCode) {
            this.bookCode = bookCode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBookVersionId() {
            return bookVersionId;
        }

        public void setBookVersionId(String bookVersionId) {
            this.bookVersionId = bookVersionId;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getUnitCode() {
            return unitCode;
        }

        public void setUnitCode(String unitCode) {
            this.unitCode = unitCode;
        }
    }
}
