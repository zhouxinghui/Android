package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    }

    public JudgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context){
        this.context = context;
        setOrientation(VERTICAL);
        headTv = new TextView(context);
        headTv.setTextSize(getResources().getDimension(R.dimen.question_head));
        headTv.setTextColor(getResources().getColor(R.color.question_normal));
        contentTv = new TextView(context);
        contentTv.setTextSize(getResources().getDimension(R.dimen.question_content));
        contentTv.setTextColor(getResources().getColor(R.color.question_normal));
        contentImg = new ImageView(context);
    }

}
