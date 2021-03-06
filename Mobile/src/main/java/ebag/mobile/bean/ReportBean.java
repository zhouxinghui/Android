package ebag.mobile.bean;

import java.util.List;

/**
 * Created by unicho on 2018/2/27.
 */

public class ReportBean {


    /**
     * totalScore : 0
     * maxScore : 0
     * errorNum : 3
     * homeWorkRepDetailVos : [{"type":"10","typeName":null,"questionNum":"3","errorCount":3}]
     */

    private float totalScore;
    private float maxScore;
    private int errorNum;
    private String teacherComment;
    private String parentComment;
    private List<ReportDetailBean> homeWorkRepDetailVos;

    private String parentAutograph;

    public String getParentAutograph() {
        return parentAutograph;
    }

    public void setParentAutograph(String parentAutograph) {
        this.parentAutograph = parentAutograph;
    }

    public String getTeacherComment() {
        return teacherComment;
    }

    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }

    public String getParentComment() {
        return parentComment;
    }

    public void setParentComment(String parentComment) {
        this.parentComment = parentComment;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }

    public List<ReportDetailBean> getHomeWorkRepDetailVos() {
        return homeWorkRepDetailVos;
    }

    public void setHomeWorkRepDetailVos(List<ReportDetailBean> homeWorkRepDetailVos) {
        this.homeWorkRepDetailVos = homeWorkRepDetailVos;
    }

    public static class ReportDetailBean {
        /**
         * type : 10
         * typeName : null
         * questionNum : 3
         * errorCount : 3
         */

        private String questionType;
        private String questionTypeName;
        private int questionNum;
        private int errorCount;

        public float getQuestionScore() {
            return questionScore;
        }

        public void setQuestionScore(float questionScore) {
            this.questionScore = questionScore;
        }

        private float questionScore ;

        public String getQuestionType() {
            return questionType;
        }

        public void setQuestionType(String questionType) {
            this.questionType = questionType;
        }

        public String getQuestionTypeName() {
            return questionTypeName;
        }

        public void setQuestionTypeName(String questionTypeName) {
            this.questionTypeName = questionTypeName;
        }

        public int getQuestionNum() {
            return questionNum;
        }

        public void setQuestionNum(int questionNum) {
            this.questionNum = questionNum;
        }

        public int getErrorCount() {
            return errorCount;
        }

        public void setErrorCount(int errorCount) {
            this.errorCount = errorCount;
        }

    }
}
