package ebag.core.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2017/5/6.
 */

public class QuestionBean implements Serializable{
    private static final long serialVersionUID = 4978312197081677925L;

    private boolean isChoose;

    private String questionType;

    private String questionContent;

    private long id;

    private int pageSize;

    private int page;

    private String disabled;

    private String removed;

    private String questionHead;

    private String rightAnswer;

    private int author;

    private String analytical;
    private String questionTypeSx;
    /**朗读作业和书写作业的内容*/
    private String remark;

    private String correctionAnswer;
    private String questionScore;
    private String answer;

    public String getCorrectionAnswer() {
        return correctionAnswer;
    }

    public void setCorrectionAnswer(String correctionAnswer) {
        this.correctionAnswer = correctionAnswer;
    }

    public String getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(String questionScore) {
        this.questionScore = questionScore;
    }

    public String getAnswer() {
        if(answer == null) answer = "";
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /*-------------------------------------连线----------------------------------------------------*/
    private List<String> wordList;
    public List<String> getWordList() {
        return wordList;
    }
    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }
    /*-------------------------------------连线----------------------------------------------------*/
    /*-------------------------------------分类----------------------------------------------------*/
    private List<String> titleList;//分类列表

    private List<Parse> parseList;//答案解析集合

    public List<Parse> getParseList() {
        return parseList;
    }

    public void setParseList(List<Parse> parseList) {
        this.parseList = parseList;
    }

    public List<String> getTitleList() {
        return titleList;
    }
    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    /*-------------------------------------分类----------------------------------------------------*/

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionTypeSx() {
        return questionTypeSx;
    }

    public void setQuestionTypeSx(String questionTypeSx) {
        this.questionTypeSx = questionTypeSx;
    }

    public String getAnalytical() {
        return analytical;
    }

    public void setAnalytical(String analytical) {
        this.analytical = analytical;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getRemoved() {
        return removed;
    }

    public void setRemoved(String removed) {
        this.removed = removed;
    }

    public String getQuestionHead() {
        return questionHead;
    }

    public void setQuestionHead(String questionHead) {
        this.questionHead = questionHead;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getQuestionContent() {
        if(questionContent == null)
            questionContent = "";
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof QuestionBean && this.getId() == ((QuestionBean) obj).getId();
    }
}
