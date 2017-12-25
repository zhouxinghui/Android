package ebag.hd.widget.questions;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ebag.core.util.L;
import ebag.hd.R;


/**
 * 填空题  一个空默认占4位
 * Created by yangle on 2017/9/2.
 */

public class FillBlankView extends FrameLayout {

    private TextView tvContent;
    private Context context;
    // 答案集合
    private List<String> answerList = new ArrayList<>();
    // 答案范围集合
    private List<AnswerRange> rangeList = new ArrayList<>();
    // 填空题内容
    private SpannableStringBuilder content;

    private boolean active = true;

    private int normal_color;

    private int error_color = Color.RED;

    private int right_color = Color.GREEN;

    public FillBlankView(Context context) {
        this(context, null);
    }

    public FillBlankView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FillBlankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {

        tvContent = new TextView(context);
        tvContent.setLineSpacing(getResources().getDimensionPixelSize(R.dimen.y5),1.0f);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.question_content));

        normal_color = tvContent.getCurrentTextColor();

        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(tvContent,layoutParams);
    }

    /**
     * 设置数据
     *
     * @param originContent   源数据
     */
    public void setData(String originContent) {
        if (TextUtils.isEmpty(originContent)) {
            return;
        }

        // 答案范围集合
        rangeList.clear();
        // 答案集合
        answerList.clear();

        String[] strings = originContent.split("#R#");
        StringBuilder stringBuilder = new StringBuilder();
        //用于计算需要增加下划线或者
        int index = 0;
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]){
                case "*"://括号,  需要把前后的增加前括号和后括号，所以index需要处理需要改变
                    rangeList.add(new AnswerRange(index,index + 4,false));
                    index = index + 4;
                    answerList.add("");
                    stringBuilder.append("    ");
                    break;
                case "#F#"://换行
                    stringBuilder.append("\n");
                    ++index;
                    break;
                case "#E#"://长线
                case "#A#"://短线
                case "#C#"://框框，计算题  填空题不会出现
                    rangeList.add(new AnswerRange(index,index + 4));
                    index = 4 + index;
                    answerList.add("");
                    stringBuilder.append("    ");
                    break;
                default:
                    index = index + strings[i].length();
                    stringBuilder.append(strings[i]);
            }
        }

        // 获取课文内容
        content = new SpannableStringBuilder(stringBuilder.toString());

        for (int i = 0; i < rangeList.size(); i++) {
            AnswerRange range = rangeList.get(i);

            // 设置下划线颜色
            if(range.line){
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(normal_color);
                content.setSpan(colorSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // 设置填空处点击事件
            BlankClickableSpan blankClickableSpan = new BlankClickableSpan(i);
            content.setSpan(blankClickableSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 设置此方法后，点击事件才能生效
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(content);
    }

    /**
     * 点击事件
     */
    private class BlankClickableSpan extends ClickableSpan {

        private int position;

        public BlankClickableSpan(int position) {
            this.position = position;
        }

        @Override
        public void onClick(final View widget) {
            if(!active)
                return;
            View view = LayoutInflater.from(context).inflate(R.layout.layout_input, null);
            final EditText etInput = (EditText) view.findViewById(R.id.et_answer);
            Button btnFillBlank = (Button) view.findViewById(R.id.btn_fill_blank);

            // 显示原有答案
            String oldAnswer = answerList.get(position);
            if (!TextUtils.isEmpty(oldAnswer)) {
                etInput.setText(oldAnswer);
                etInput.setSelection(oldAnswer.length());
            }

            final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, dp2px(40));
            // 获取焦点
            popupWindow.setFocusable(true);
            // 为了防止弹出菜单获取焦点之后，点击Activity的其他组件没有响应
            popupWindow.setBackgroundDrawable(new PaintDrawable());
            // 设置PopupWindow在软键盘的上方
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // 弹出PopupWindow
            popupWindow.showAtLocation(tvContent, Gravity.BOTTOM, 0, 0);

            btnFillBlank.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 填写答案
                    String answer = etInput.getText().toString();
                    fillAnswer(answer, position,normal_color);
                    popupWindow.dismiss();
                }
            });

            // 显示软键盘
            InputMethodManager inputMethodManager =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // 不显示下划线
            ds.setUnderlineText(false);
        }
    }

    /**
     * 填写答案
     *
     * @param answer   当前填空处答案
     * @param position 填空位置
     */
    private void fillAnswer(String answer, int position, int color) {
        // 将答案添加到集合中
        answerList.set(position, answer.trim());

        answer = " " + answer + " ";

        // 替换答案
        AnswerRange range = rangeList.get(position);
        content.replace(range.start, range.end, answer);

        // 计算新旧答案字数的差值
        int difference = range.end;

        //替换答案后的 end
        range.end = range.start + answer.length();
        difference = range.end - difference;

        // 答案设置下划线
        if(range.line)
            content.setSpan(new UnderlineSpan(),
                    range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //答案设置颜色，区分正确还是错误
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        content.setSpan(colorSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 更新内容
        tvContent.setText(content);

        //更新当前答案后的N个 范围
        for (int i = position + 1; i < rangeList.size(); i++) {
            // 更新下一个答案的范围
            rangeList.get(i).start = rangeList.get(i).start + difference;
            rangeList.get(i).end = rangeList.get(i).end + difference;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void showResult(String studentAnswer, String rightAnswer){
        if(studentAnswer == null || rightAnswer == null){
            return;
        }
        String[] studentAnswers = studentAnswer.split("#R#");

        String[] rightAnswers;
        if(rightAnswer.contains("#R#"))
            rightAnswers = rightAnswer.split("#R#");
        else
            rightAnswers = rightAnswer.split(",");

        //答案个数和需要填空的个数不相符
        if(studentAnswers.length != answerList.size() ||
                rightAnswers.length != answerList.size() ||
                studentAnswers.length != rightAnswers.length){
            return;
        }
        for(int i = 0; i < studentAnswers.length; i++){
            if(studentAnswers[i] != null)
                if(studentAnswers[i].equals(rightAnswers[i])){
                    fillAnswer(studentAnswers[i], i, right_color);
                }else{
                    fillAnswer(studentAnswers[i], i, error_color);
                }
        }
    }

    /**
     * 获取填写的答案
     * @return
     */
    public String getAnswer(String regex){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < answerList.size(); i++) {
            sb.append(answerList.get(i).trim()).append(regex);
        }
        L.INSTANCE.e("Completion","getAnswer"+sb.toString());
        return sb.toString();
    }

    /**
     * 获取答案列表
     *
     * @return 答案列表
     */
    public List<String> getAnswerList() {
        return answerList;
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private static class AnswerRange {

        public int start;
        public int end;
        public boolean line = true;

        public AnswerRange(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public AnswerRange(int start, int end, boolean line) {
            this(start,end);
            this.line = line;
        }

    }
}
