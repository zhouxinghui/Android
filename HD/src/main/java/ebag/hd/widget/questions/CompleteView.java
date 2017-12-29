package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.hd.widget.questions.base.FillBlankView;
import ebag.hd.widget.questions.head.HeadAdapter;
import ebag.hd.widget.questions.util.IQuestionEvent;

/**
 * Created by YZY on 2017/12/23.
 */

public class CompleteView extends LinearLayout implements IQuestionEvent {
    private Context context;
    private HeadAdapter headAdapter;
    private List<String> title;
    /**
     * 题目内容
     */
    private FillBlankView fillBlankView;
    private String questionContent;
    private String rightAnswer;
    private String studentAnswer;
    public CompleteView(Context context) {
        super(context);
        init(context);
    }

    public CompleteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CompleteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOrientation(VERTICAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RecyclerView headRecycler = new RecyclerView(context);
        headRecycler.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager headManager = new LinearLayoutManager(context);
        headRecycler.setLayoutManager(headManager);
        headAdapter = new HeadAdapter();
        headRecycler.setAdapter(headAdapter);
        addView(headRecycler,layoutParams);

        fillBlankView = new FillBlankView(context);
        LayoutParams fillBlankParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(fillBlankView,fillBlankParams);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        title = Arrays.asList(questionBean.getQuestionHead().split("#R#"));
        questionContent = questionBean.getQuestionContent();

        studentAnswer = questionBean.getAnswer();
        rightAnswer = questionBean.getRightAnswer();
    }

    @Override
    public void show(boolean active) {
        headAdapter.setDatas(title);
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
}
