package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.bean.QuestionTypeUtils;
import ebag.hd.R;
import ebag.hd.homework.DoHomeworkActivity;
import ebag.hd.widget.keyboard.KeyBoardView;
import ebag.hd.widget.questions.base.BaseQuestionView;
import ebag.hd.widget.questions.base.LineEditText;

/**
 * Created by caoyu on 2017/12/29.
 */

public class SentenceView extends BaseQuestionView {
    private QuestionBean questionBean;
    private List<String> titleList;
    private String studentAnswer;
    private LineEditText lineEditText;


    public SentenceView(Context context) {
        super(context);
    }

    public SentenceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SentenceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        lineEditText = new LineEditText(context);
        lineEditText.setGravity(Gravity.TOP | Gravity.START);
        lineEditText.setId(R.id.multi_under_line);
        lineEditText.setLineSpacing(getResources().getDimensionPixelSize(R.dimen.x10),1);
        lineEditText.setTextColor(getResources().getColor(R.color.question_normal));
        lineEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.question_content));
        lineEditText.setBackground(null);
        lineEditText.setGravity(Gravity.TOP | Gravity.START);
        lineEditText.setMinLines(1);
        lineEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(onDoingListener != null)
                    onDoingListener.onDoing(SentenceView.this);
                SentenceView.this.questionBean.setAnswer(SentenceView.this.getAnswer());
            }
        });
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(lineEditText,layoutParams);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        this.questionBean = questionBean;
        titleList = new ArrayList<>();
        Collections.addAll(titleList,questionBean.getTitle().split("#R#"));
        //听写
        if (QuestionTypeUtils.getIntType(questionBean) == QuestionTypeUtils.QUESTIONS_CHINESE_WRITE_BY_VOICE) {
            titleList.add("#M#" + questionBean.getAudioUrl());
            lineEditText.setMinLines(2);
        } else {
            //词组或句子&应用题
            if (QuestionTypeUtils.getIntType(questionBean) == QuestionTypeUtils.QUESTIONS_CHINESE_SENTENCE
                    || QuestionTypeUtils.getIntType(questionBean) == QuestionTypeUtils.QUESTION_MATH_APPLICATION) {
                lineEditText.setMinLines(1);
                if (QuestionTypeUtils.getIntType(questionBean) == QuestionTypeUtils.QUESTION_MATH_APPLICATION && mContext instanceof DoHomeworkActivity){
                    ((DoHomeworkActivity)mContext).bindKeyBoard(lineEditText, KeyBoardView.number_keyboard);
                }
            } else //作文
                lineEditText.setMinLines(5);
            titleList.add(questionBean.getContent());
        }
        studentAnswer = questionBean.getAnswer();
    }

    @Override
    public void show(boolean active) {
        lineEditText.setEnabled(active);
        setTitle(titleList);
    }

    @Override
    public void questionActive(boolean active) {
        lineEditText.setEnabled(active);
    }

    @Override
    public boolean isQuestionActive() {
        return lineEditText.isEnabled();
    }

    @Override
    public void showResult() {
        show(false);
        lineEditText.setText(studentAnswer);
    }

    @Override
    public String getAnswer() {
        return lineEditText.getText().toString();
    }

    @Override
    public void reset() {

    }
}
