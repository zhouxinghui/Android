package ebag.hd.bean;

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

    private int totalScore;
    private int maxScore;
    private int errorNum;
    private List<ReportDetailBean> homeWorkRepDetailVos;

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
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
        private float score;

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

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }
    }
}
