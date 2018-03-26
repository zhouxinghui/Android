package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.bean.QuestionTypeUtils;
import ebag.core.util.StringUtils;
import ebag.hd.R;
import ebag.hd.widget.questions.base.BaseQuestionView;

/**
 * Created by YZY on 2018/1/4.
 */

public class RecorderView extends BaseQuestionView {
    private List<String> title;
    /**
     * 正确答案
     */
    private String rightAnswer;
    /**
     * 学生答案
     */
    private String studentAnswer;
    private ImageView recorderBtn;
    private TextView playBtn;
    private QuestionBean questionBean;
    public RecorderView(Context context) {
        super(context);
    }

    public RecorderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        //录音时间
        /*TextView timeTv = new TextView(context);
        timeTv.setQuestionId(R.id.recorder_time_tv_id);
        timeTv.setTextColor(getResources().getColor(R.color.question_normal));
        timeTv.setText("00:00:00");
        timeTv.setPadding(
                0,
                getResources().getDimensionPixelSize(R.dimen.y10),
                0,
                getResources().getDimensionPixelSize(R.dimen.y10));
        addView(timeTv);*/

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setOrientation(HORIZONTAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.y10);
        linearLayout.setLayoutParams(params);

        //录音按钮
        recorderBtn = new ImageView(context);
        recorderBtn.setId(R.id.recorder_id);
        params = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.x60),
                getResources().getDimensionPixelSize(R.dimen.x60));
        recorderBtn.setLayoutParams(params);
        recorderBtn.setBackgroundResource(R.drawable.recorder_anim);
        linearLayout.addView(recorderBtn);

        //上传录音按钮
        TextView uploadBtn = new TextView(context);
        uploadBtn.setId(R.id.recorder_upload_id);
        params = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.x50),
                getResources().getDimensionPixelSize(R.dimen.x50));
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.x30);
        uploadBtn.setLayoutParams(params);
        uploadBtn.setBackgroundResource(R.drawable.icon_upload_recorder);
        linearLayout.addView(uploadBtn);
        uploadBtn.setVisibility(View.INVISIBLE);

        //播放录音按钮
        playBtn = new TextView(context);
        playBtn.setId(R.id.recorder_play_id);
        params = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.x50),
                getResources().getDimensionPixelSize(R.dimen.x50));
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.x30);
        playBtn.setLayoutParams(params);
        playBtn.setBackgroundResource(R.drawable.recorde_play_bg);
        linearLayout.addView(playBtn);
        playBtn.setVisibility(View.INVISIBLE);

        addView(linearLayout);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        this.questionBean = questionBean;
        title = new ArrayList<>();
        switch (QuestionTypeUtils.getIntType(questionBean)) {
            case QuestionTypeUtils.QUESTIONS_FOLLOW_READ://跟读作业
                title.add(questionBean.getTitle());
                title.add("#M#" + questionBean.getAudioUrl());
                break;
            case QuestionTypeUtils.QUESTIONS_READ_ALOUD://朗读作业
                title.add(questionBean.getTitle());
                title.add(questionBean.getContent());
                break;
        }
        rightAnswer = questionBean.getRightAnswer();
        studentAnswer = questionBean.getAnswer();
    }

    @Override
    public void show(boolean active) {
        questionActive(active);
        setTitle(title);
    }

    @Override
    public void questionActive(boolean active) {
        recorderBtn.setEnabled(active);
        if (!StringUtils.INSTANCE.isEmpty(studentAnswer))
            playBtn.setVisibility(View.VISIBLE);
        else
            playBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean isQuestionActive() {
        return recorderBtn.isEnabled();
    }

    @Override
    public void showResult() {

    }

    @Override
    public String getAnswer() {
        return questionBean.getAnswer();
    }

    @Override
    public void reset() {

    }
}
