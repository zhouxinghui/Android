package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.util.L;
import ebag.core.util.StringUtils;
import ebag.hd.R;
import ebag.hd.widget.questions.head.HeadAdapter;
import ebag.hd.widget.questions.util.IQuestionEvent;

/**
 * Created by YZY on 2017/12/22.
 * 判断题
 */
public class JudgeView extends LinearLayout implements IQuestionEvent {
    private Context context;
    /**
     * 选项
     */
    private RadioGroup radioGroup;
    private RadioButton aRadioButton;
    private RadioButton bRadioButton;
    private HeadAdapter headAdapter;

    private List<String> title;
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

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RecyclerView headRecycler = new RecyclerView(context);
        headRecycler.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager headManager = new LinearLayoutManager(context);
        headRecycler.setLayoutManager(headManager);
        headAdapter = new HeadAdapter();
        headRecycler.setAdapter(headAdapter);
        addView(headRecycler,layoutParams);

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
        addView(radioGroup);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        /*if (questionContent.startsWith("http")) {
            String[] split = questionContent.split("#R#");
            if (split.length > 1)
                questionContent = split[1];
            imageUrl = split[0];
        }*/

        title = new ArrayList<>();
        if(!StringUtils.INSTANCE.isEmpty(questionBean.getQuestionHead())){
            title.add(questionBean.getQuestionHead());
        }
        title.add(questionBean.getQuestionContent());


        rightAnswer = questionBean.getRightAnswer();
        studentAnswer = questionBean.getAnswer();
    }

    @Override
    public void show(boolean active) {
        questionActive(active);
        headAdapter.setDatas(title);
    }

    @Override
    public void questionActive(boolean active) {
        aRadioButton.setEnabled(active);
        bRadioButton.setEnabled(active);
    }

    @Override
    public boolean isQuestionActive() {
        return this.aRadioButton.isEnabled();
    }

    @Override
    public void showResult() {
        show(false);
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

    @Override
    public void reset() {

    }
}
