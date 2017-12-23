package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ebag.core.bean.QuestionBean;
import ebag.core.http.image.SingleImageLoader;
import ebag.core.util.L;
import ebag.core.util.StringUtils;
import ebag.hd.R;

/**
 * Created by YZY on 2017/12/22.
 * 判断题
 */
public class JudgeView extends LinearLayout implements IQuestionEvent{
    private Context context;
    /**
     * 标题文字
     */
    private TextView headTv;
    /**
     * 内容文字
     */
    private TextView contentTv;
    /**
     * 内容图片
     */
    private ImageView contentImg;
    /**
     * 选项
     */
    private RadioGroup radioGroup;
    private RadioButton aRadioButton;
    private RadioButton bRadioButton;
    /**
     * 标题
     */
    private String questionHead;
    /**
     * 内容
     */
    private String questionContent;
    /**
     * 图片URL
     */
    private String imageUrl;
    /**
     * 正确答案
     */
    private String rightAnswer;
    /**
     * 学生答案
     */
    private String studentAnswer;
    public JudgeView(Context context) {
        super(context);
        init(context);
    }

    public JudgeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JudgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        this.context = context;
        setOrientation(VERTICAL);
        headTv = new TextView(context);
        headTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.question_head));
        headTv.setTextColor(getResources().getColor(R.color.question_normal));

        contentTv = new TextView(context);
        LayoutParams contentParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.topMargin = (int) getResources().getDimension(R.dimen.y10);
        contentParams.bottomMargin = (int) getResources().getDimension(R.dimen.y10);
        contentTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.question_content));
        contentTv.setTextColor(getResources().getColor(R.color.question_normal));
        contentTv.setLayoutParams(contentParams);

        contentImg = new ImageView(context);
        LayoutParams imgParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParams.bottomMargin = (int) getResources().getDimension(R.dimen.y10);
        imgParams.width = (int) getResources().getDimension(R.dimen.x140);
        imgParams.height = (int) getResources().getDimension(R.dimen.x140);
        contentImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        contentImg.setLayoutParams(imgParams);

        radioGroup = new RadioGroup(context);
        RadioGroup.LayoutParams groupParams = new RadioGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        radioGroup.setOrientation(HORIZONTAL);
        radioGroup.setLayoutParams(groupParams);
        RadioGroup.LayoutParams buttonParams = new RadioGroup.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.weight = 1;
        aRadioButton = new RadioButton(context);
        bRadioButton = new RadioButton(context);
        aRadioButton.setId(R.id.judge_right);
        bRadioButton.setId(R.id.judge_wrong);
        aRadioButton.setLayoutParams(buttonParams);
        bRadioButton.setLayoutParams(buttonParams);
        aRadioButton.setText("正确");
        bRadioButton.setText("错误");
        aRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.question_content));
        bRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.question_content));
        aRadioButton.setTextColor(getResources().getColor(R.color.question_normal));
        bRadioButton.setTextColor(getResources().getColor(R.color.question_normal));
        aRadioButton.setButtonDrawable(null);
        bRadioButton.setButtonDrawable(null);
        aRadioButton.setBackground(null);
        bRadioButton.setBackground(null);
        aRadioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_a, 0, 0, 0);
        bRadioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_b, 0, 0, 0);
        aRadioButton.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.x10));
        bRadioButton.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.x10));
        radioGroup.addView(aRadioButton);
        radioGroup.addView(bRadioButton);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.judge_right) {
                    studentAnswer = "对";
                    L.INSTANCE.e("判断题：用户选择了“对");
                } else {
                    studentAnswer = "错";
                    L.INSTANCE.e("判断题：用户选择了“错");
                }
            }
        });
//        radioGroup.setEnabled(false);
        addView(headTv);
        addView(contentTv);
        addView(contentImg);
        addView(radioGroup);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        imageUrl = null;
        questionHead = questionBean.getQuestionHead();
        questionContent =  questionBean.getQuestionContent();
        if (questionContent.startsWith("http")) {
            String[] split = questionContent.split("#R#");
            if (split.length > 1)
                questionContent = split[1];
            imageUrl = split[0];
        }
        rightAnswer = questionBean.getRightAnswer();
        studentAnswer = questionBean.getAnswer();
    }

    @Override
    public void show(boolean active) {
        enable(active);
        headTv.setText(questionHead);
        contentTv.setText(questionContent);
        if (StringUtils.INSTANCE.isEmpty(imageUrl))
            contentImg.setVisibility(GONE);
        else {
            contentImg.setVisibility(VISIBLE);
            SingleImageLoader.getInstance().setImage(
                    imageUrl, contentImg);
        }
    }

    @Override
    public void enable(boolean active) {
        aRadioButton.setEnabled(active);
        bRadioButton.setEnabled(active);
    }

    @Override
    public void showResult() {
        enable(false);
        if (StringUtils.INSTANCE.isEmpty(rightAnswer))
            L.INSTANCE.e("判断题：正确答案字段为空");
        else{
            if (rightAnswer.equals(studentAnswer)){
                if ("对".equals(studentAnswer)) {
                    aRadioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.question_option_correct,0,0,0);
                }
                else {
                    bRadioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.question_option_correct,0,0,0);
                }
            }else{
                if ("错".equals(studentAnswer)) {
                    bRadioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.question_option_error,0,0,0);
                }
                else {
                    aRadioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.question_option_error,0,0,0);
                }
            }
            radioGroup.clearCheck();
        }
    }

    @Override
    public String getAnswer() {
        return studentAnswer;
    }
}
