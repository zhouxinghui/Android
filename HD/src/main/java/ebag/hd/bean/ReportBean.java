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

        private String type;
        private String typeName;
        private String questionNum;
        private int errorCount;
        private float score;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getQuestionNum() {
            return questionNum;
        }

        public void setQuestionNum(String questionNum) {
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
