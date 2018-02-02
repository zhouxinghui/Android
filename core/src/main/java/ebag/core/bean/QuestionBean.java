package ebag.core.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by YZY on 2017/5/6.
 */

public class QuestionBean implements Serializable, MultiItemEntity, Cloneable {
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
        if (title == null)
            title = "";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        if (content == null)
            content = "";
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
        return obj != null && obj instanceof QuestionBean && this.getId().equals(((QuestionBean) obj).getId());
    }

    @Override
    public Object clone(){
        try {
            return super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
            QuestionBean questionBean = new QuestionBean();
            questionBean.setChoose(isChoose);
            questionBean.setId(id);
            questionBean.setBookUnit(bookUnit);
            questionBean.setBookCatalog(bookCatalog);
            questionBean.setLevel(level);
            questionBean.setType(type);
            questionBean.setMinType(minType);
            questionBean.setTitle(title);
            questionBean.setContent(content);
            questionBean.setAnswer(answer);
            questionBean.setStudentAnswer(studentAnswer);
            questionBean.setItem(item);
            questionBean.setAnalytical(analytical);
            questionBean.setIsOpen(isOpen);
            questionBean.setErrNum(errNum);
            questionBean.setUsage(usage);
            questionBean.setAudioUrl(audioUrl);
            return questionBean;
        }
    }

    @Override
    public int getItemType() {
        return QuestionTypeUtils.getIntType(this);
    }
}
