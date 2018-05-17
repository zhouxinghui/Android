package ebag.mobile.bean.request;

/**
 * Created by unicho on 2018/2/26.
 */

public class QuestionVo {

    public QuestionVo(String questionId, String answer, String questionType){
        this.questionId = questionId;
        this.answer = answer;
        this.questionType = questionType;
    }

    /**
     * questionId : 1000
     * answer : ess#R#yellow
     * questionType : 3
     */

    private String questionId;
    private String answer;
    private String questionType;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
}
