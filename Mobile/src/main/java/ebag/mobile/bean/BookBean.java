package ebag.mobile.bean;

/**
 * Created by caoyu on 2018/1/8.
 */

public class BookBean {
    /**
     * bookCode : sx
     * bookName : 数学
     * bookVersionId : 39
     * bookVersionName : 人民教育出版社
     * gradeCode : 1
     * gradeName : 一年级
     * semesterCode : 1
     * semester : 上学期
     * pageImageUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/CAI/grade1/phase1/sx/page1.jpg
     * downloadUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/CAI/39/grade1/phase1/sx.zip
     * bookId : 39
     */

    private String bookCode;
    private String bookName;
    private String bookVersionId;
    private String bookVersionName;
    private String gradeCode;
    private String gradeName;
    private String semesterCode;
    private String semester;
    private String pageImageUrl;
    private String downloadUrl;
    private int bookId;

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getPageImageUrl() {
        return pageImageUrl;
    }

    public void setPageImageUrl(String pageImageUrl) {
        this.pageImageUrl = pageImageUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
