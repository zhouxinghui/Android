package ebag.hd.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/5.
 */

public class PrepareFileBean implements Serializable{
    private String id;// "028267192efe46cfb6ccac80dcfcefff", // 文件唯一编码
    private String teacherId;// "9b4d190adac04296a21d0a8eb08ee443",// 老师唯一标识
    private String fileName;// "templet",  // 文件名称
    private String fileType;// "csv", // 文件类型
    private String grade;// null,  // 年级
    private String gradeCode;// "1",   //年级编码
    private String subjectType;// null,//科目类型
    private String unitCode;// "5214",//单元唯一编码
    private String catalog;// null,//章节唯一编码
    private String length;// "0",
    private String fileUrl;// "http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/beike/9b4d190adac04296a21d0a8eb08ee443///1/5214/9b4d190adac04296a21d0a8eb08ee443_20180329175505789.csv", // 文件路径
    private String createDate;// "2018-03-29 17:54:46.0", // 创建时间
    private String createBy;// "9b4d190adac04296a21d0a8eb08ee443", // 创建人
    private String updateDate;// "2018-03-29 17:54:46.0", // 修改时间
    private String updateBy;// null,   // 修改人
    private String remove;// "Y",  //是否删除
    private String share;// "0", // 1  班级共享 2 校园共享 3 全局共享 0 个人
    private String subCode;// "yy", // 科目
    private int page;// 0,
    private int pageSize;// 0

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getRemove() {
        return remove;
    }

    public void setRemove(String remove) {
        this.remove = remove;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
