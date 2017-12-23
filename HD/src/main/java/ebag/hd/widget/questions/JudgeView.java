package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ebag.core.http.image.SingleImageLoader;
import ebag.hd.R;

/**
 * Created by YZY on 2017/12/22.
 * 判断题
 */
public class JudgeView extends LinearLayout {
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

    private void init(Context context){
        this.context = context;
        setOrientation(VERTICAL);
        headTv = new TextView(context);
        headTv.setTextSize(getResources().getDimension(R.dimen.question_head));
        headTv.setTextColor(getResources().getColor(R.color.question_normal));

        contentTv = new TextView(context);
        LayoutParams contentParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.topMargin = (int) getResources().getDimension(R.dimen.y10);
        contentParams.bottomMargin = (int) getResources().getDimension(R.dimen.y10);
        contentTv.setTextSize(getResources().getDimension(R.dimen.question_content));
        contentTv.setTextColor(getResources().getColor(R.color.question_normal));
        contentTv.setLayoutParams(contentParams);

        contentImg = new ImageView(context);
        LayoutParams imgParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        RadioButton aRadioButton = new RadioButton(context);
        RadioButton bRadioButton = new RadioButton(context);
        aRadioButton.setId(R.id.judge_right);
        bRadioButton.setId(R.id.judge_wrong);
        aRadioButton.setLayoutParams(buttonParams);
        bRadioButton.setLayoutParams(buttonParams);
        aRadioButton.setText("正确");
        bRadioButton.setText("错误");
        aRadioButton.setTextSize(getResources().getDimension(R.dimen.question_content));
        bRadioButton.setTextSize(getResources().getDimension(R.dimen.question_content));
//        aRadioButton.setButtonDrawable(null);
//        bRadioButton.setButtonDrawable(null);
        radioGroup.addView(aRadioButton);
        radioGroup.addView(bRadioButton);
        addView(headTv);
        addView(contentTv);
        addView(contentImg);
        addView(radioGroup);

        initChild();
    }

    private void initChild(){
        headTv.setText("标题");
        contentTv.setText("内容");
        SingleImageLoader.getInstance().setImage(
                "http://img.zcool.cn/community/01902d554c0125000001bf72a28724.jpg@1280w_1l_2o_100sh.jpg",contentImg);
    }

}
