package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ebag.core.bean.QuestionBean;
import ebag.core.http.image.SingleImageLoader;
import ebag.core.util.StringUtils;
import ebag.hd.R;
import ebag.hd.widget.questions.util.IQuestionEvent;

/**
 * Created by YZY on 2017/12/23.
 */

public class CompleteView extends LinearLayout implements IQuestionEvent {
    private Context context;
    /**
     * 题目标题
     */
    private TextView headTv;
    /**
     * 标题图片
     */
    private ImageView contentImg;
    /**
     * 题目内容
     */
    private FillBlankView fillBlankView;
    private String questionHead,imageUrl,questionContent;
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
        contentImg = new ImageView(context);
        LayoutParams imgParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParams.bottomMargin = (int) getResources().getDimension(R.dimen.y30);
        imgParams.width = (int) getResources().getDimension(R.dimen.x140);
        imgParams.height = (int) getResources().getDimension(R.dimen.x140);
        contentImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        headTv = new TextView(context);
        LayoutParams contentParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.bottomMargin = (int) getResources().getDimension(R.dimen.y30);
        headTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.question_head));
        headTv.setTextColor(getResources().getColor(R.color.question_normal));

        fillBlankView = new FillBlankView(context);
        LayoutParams fillBlankParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(contentImg,imgParams);
        addView(headTv,contentParams);
        addView(fillBlankView,fillBlankParams);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        String head = questionBean.getQuestionHead();
        if (head.startsWith("http"))
            imageUrl = head;
        else
            questionHead = head;
        questionContent = questionBean.getQuestionContent();

        studentAnswer = questionBean.getAnswer();
        rightAnswer = questionBean.getRightAnswer();
    }

    @Override
    public void show(boolean active) {
        if (StringUtils.INSTANCE.isEmpty(imageUrl)){
            headTv.setText(questionHead);
            headTv.setVisibility(VISIBLE);
            contentImg.setVisibility(GONE);
        }else{
            headTv.setVisibility(GONE);
            contentImg.setVisibility(VISIBLE);
            SingleImageLoader.getInstance().setImage(imageUrl, contentImg);
        }
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
