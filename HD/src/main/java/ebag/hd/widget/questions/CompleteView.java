package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.util.StringUtils;
import ebag.hd.R;
import ebag.hd.widget.questions.base.BaseQuestionView;
import ebag.hd.widget.questions.base.FillBlankView;
import ebag.hd.widget.questions.util.QuestionTypeUtils;

/**
 * Created by caoyu on 2017/12/23.
 */

public class CompleteView extends BaseQuestionView implements FillBlankView.OnBlankChangedListener {
    private List<String> title;
    private QuestionBean questionBean;
    /**
     * 题目内容
     */
    private FillBlankView fillBlankView;
    private String questionContent;
    private String rightAnswer;
    private String studentAnswer;
    public CompleteView(Context context) {
        super(context);
    }

    public CompleteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CompleteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        fillBlankView = new FillBlankView(context);
        LayoutParams fillBlankParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fillBlankParams.topMargin = getResources().getDimensionPixelSize(R.dimen.y10);
        addView(fillBlankView,fillBlankParams);
        fillBlankView.setOnBlankChangedListener(this);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        this.questionBean = questionBean;
        title = new ArrayList<>();
        switch (QuestionTypeUtils.getIntType(questionBean)) {
            case QuestionTypeUtils.QUESTIONS_COMPLETION_BY_VOICE://听录音填空
                title.add("听录音填空");
                title.add("#M#" + questionBean.getTitle());
                break;
            case QuestionTypeUtils.QUESTIONS_COMPLETION://填空题
            case QuestionTypeUtils.QUESTIONS_WRITE_WORD_BY_PIC://看图写单词
                title = (Arrays.asList(questionBean.getTitle().split("#R#")));
                break;
        }

        questionContent = questionBean.getContent();

        studentAnswer = questionBean.getStudentAnswer();
        rightAnswer = questionBean.getAnswer();
    }

    @Override
    public void show(boolean active) {
        setTitle(title);
        fillBlankView.setActive(active);
        fillBlankView.setData(questionContent);
        if (!StringUtils.INSTANCE.isEmpty(studentAnswer))
            fillBlankView.showResult(studentAnswer);
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
        questionActive(false);
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
        this.questionBean.setStudentAnswer(fillBlankView.getAnswer("#R#"));
    }
}
