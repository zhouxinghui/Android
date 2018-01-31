package ebag.core.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

import ebag.core.util.StringUtils;

/**
 * Created by YZY on 2017/5/6.
 */

public class QuestionBean implements Serializable, MultiItemEntity {
    private static final long serialVersionUID = 4978312197081677925L;

    private boolean isChoose;

    private String id;

    private String bookUnit;

    private String bookCatalog;

    private String level;

    private String type;

    private String minType;

    private String title;

    private String content;

    private String answer;

    private String studentAnswer;

    private String item;

    private String analytical;

    private String isOpen;
    private String errNum;
    private String usage;
    private String audioUrl;

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookUnit() {
        return bookUnit;
    }

    public void setBookUnit(String bookUnit) {
        this.bookUnit = bookUnit;
    }

    public String getBookCatalog() {
        return bookCatalog;
    }

    public void setBookCatalog(String bookCatalog) {
        this.bookCatalog = bookCatalog;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinType() {
        return minType;
    }

    public void setMinType(String minType) {
        this.minType = minType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getAnalytical() {
        return analytical;
    }

    public void setAnalytical(String analytical) {
        this.analytical = analytical;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getErrNum() {
        return errNum;
    }

    public void setErrNum(String errNum) {
        this.errNum = errNum;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof QuestionBean && this.getId() == ((QuestionBean) obj).getId();
    }

    @Override
    public int getItemType() {
        if ("5".equals(type)){
            if (StringUtils.INSTANCE.isChineseCharacter(content)){
                return QuestionTypeUtils.QUESTIONS_CN_ORDER_SENTENCE;
            }else{
                return QuestionTypeUtils.QUESTIONS_EN_ORDER_SENTENCE;
            }
        }else{
            return QuestionTypeUtils.getIntType(this);
        }
    }
}
