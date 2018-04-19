package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.hd.widget.questions.base.BaseQuestionView;
import ebag.hd.widget.questions.base.FillBlankView;

/**
 * Created by caoyu on 2018/1/2.
 */

public class UnderstandView extends BaseQuestionView implements FillBlankView.OnBlankChangedListener {

    private QuestionBean questionBean;
    /**
     * 题目内容
     */
    private FillBlankView fillBlankView;

    private List<String> titleList;
    private String questionContent;
    private String rightAnswer;
    private String studentAnswer;

    public UnderstandView(Context context) {
        super(context);
    }

    public UnderstandView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UnderstandView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        fillBlankView = new FillBlankView(context);
        LayoutParams fillBlankParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(fillBlankView,fillBlankParams);
        fillBlankView.setOnBlankChangedListener(this);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        this.questionBean = questionBean;
        titleList = new ArrayList<>();
        if (questionBean.getTitle().startsWith("#W#")) {
            int index = questionBean.getTitle().indexOf("#R##Z#");
            String title = questionBean.getTitle().substring(0, index).replace("#W##R#", "");
            titleList.add(title);
            titleList.add(questionBean.getTitle().substring(index + 3).replace("#R#", "").replace("#F#", "\n").replace("#Z#","\u3000\u3000"));
        } else {
            titleList.add("阅读下面短文,回答问题");
            titleList.add(questionBean.getTitle().replace("#R#", "").replace("#F#", "\n").replace("#Z#", "\u3000\u3000"));
        }

        questionContent = questionBean.getContent();
        rightAnswer = questionBean.getRightAnswer();
        studentAnswer = questionBean.getAnswer();
    }

    @Override
    public void show(boolean active) {
        setTitle(titleList);
        fillBlankView.setActive(active);
        fillBlankView.setData(questionContent);
    }

    @Override
    public void questionActive(boolean active) {
        fillBlankView.setActive(active);
    }

    @Override
    public boolean isQuestionActive() {
        return fillBlankView.isActive();
    }

    @Override
    public void showResult() {
        show(false);
        fillBlankView.showResult(studentAnswer,rightAnswer);
    }

    @Override
    public String getAnswer() {
        return fillBlankView.getAnswer("#R#");
    }

    @Override
    public void reset() {
        questionActive(true);
        fillBlankView.setData(questionContent);
    }

    @Override
    public void onAnswerChanged() {
        if(onDoingListener != null)
            onDoingListener.onDoing(this);
        this.questionBean.setAnswer(getAnswer());
    }
}
