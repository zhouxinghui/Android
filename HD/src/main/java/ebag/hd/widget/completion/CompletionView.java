package ebag.hd.widget.completion;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ebag.core.util.HandlerUtil;
import ebag.core.util.L;
import ebag.hd.R;

/**
 * Created by 90323 on 2017/8/14.
 */

public class CompletionView extends RelativeLayout {
    private MTextView mTextView;

    private Context mContext;

    /**
     * 填写的空
     */
    private SparseArray<EditText> editTexts = new SparseArray<>();

    /**
     * 正确的答案
     */
    private String[] rightAnswers;

    /**
     * 真实答案字符所占的长度
     */
    private List<Integer> answerWith = new ArrayList<>();

    /**
     * 文字大小
     */
    private int textSize = (int) getResources().getDimension(R.dimen.question_content);

    /**
     * 文字颜色
     */
    private int textColor = getResources().getColor(R.color.question_normal);


    public CompletionView(Context context) {
        super(context);
        init(context);
    }

    public CompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CompletionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext = context;
//        mTextView = new MTextView(context);
//        mTextView.setTextSize(textSize);
//        mTextView.setTextColor(textColor);
//        LayoutParams lp = new LayoutParams(
//                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
//        );
//
//        addView(mTextView,lp);
    }

    public MTextView getTextView(){
        return mTextView;
    }

    public void setTextSize(int size){
        textSize = size;
    }

    private boolean enable = true;
    public void setText(String sourceStr, String replaceStr, boolean enable){
        if(TextUtils.isEmpty(sourceStr))
            return;

        isEditShowed = false;
        removeAllViews();
        clear();
        mTextView = new MTextView(mContext);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mTextView.setTextColor(textColor);
        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        );
        addView(mTextView,lp);

        this.enable = enable;
        SpannableString ss = convertStrs(sourceStr,replaceStr);
        mTextView.setSpaces(answerWith);
        mTextView.setMText(ss);
//        mTextView.invalidate();
        mTextView.setOnDrawedListener(new MTextView.OnDrawedListener() {
            @Override
            public void onDrawed() {
                addEditText();
            }
        });

//        do{
//            addEditText();
//        }while (mTextView.isDirty());
//        boolean isOver = true;
//        while (isOver){
//            addEditText();
//            if(mTextView.isDirty()){
//
//            }
//        }
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                addEditText();
//            }
//        },200);
    }



//    class AnswerHandler extends Handler{
//        @Override
//        public String getMessageName(Message message) {
//
//            return super.getMessageName(message);
//        }
//    }



    /**
     * 根据字符串解析成解析  增加相应的空白部分，用于填空题填空时空出的位置
     * @param sourceStr
     * @param replaceStr
     * @return
     */
    private SpannableString convertStrs(String sourceStr, String replaceStr){
        if(replaceStr == null)
            replaceStr = "";

        if(replaceStr.contains("#R#"))
            rightAnswers = replaceStr.split("#R#");
        else
            rightAnswers = replaceStr.split(",");

        if(sourceStr == null)
            sourceStr = "";

        String[] strings = sourceStr.split("#R#");
        answerWith.clear();

        int answerIndex = -1;
        sourceStr = sourceStr.replace("#R#","");
        SpannableString ssb = new SpannableString(sourceStr);
        int sum = 0;
        int splitStrLength;
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]){
                case "*":
                case "#F#":
                case "#E#":
                case "#A#":
                case "#C#":
                    if("*".equals(strings[i])){
                        splitStrLength = 1;
                    }else{
                        splitStrLength = 3;
                    }
                    sum += splitStrLength;

                    //获取图片
                    Drawable d = getResources().getDrawable(R.drawable.testbg_null);
                    if("#F#".equals(strings[i])){
                        d.setBounds(0, 0, 1, 15);
                    }else{
                        ++answerIndex;
                        if(answerIndex >= rightAnswers.length) {
                            answerWith.add(dip2px(150));
                        }else
                            answerWith.add((int) (getTextWith(mTextView
                                    , rightAnswers[answerIndex]) + sp2px(textSize * 2)));

                        d.setBounds(0, 0, answerWith.get(answerIndex), sp2px(textSize));
                    }
                    TypeImageSpan imageSpan = new TypeImageSpan(d);
                    imageSpan.setType(strings[i]);
                    ssb.setSpan(imageSpan,sum-splitStrLength,sum, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
                default:
                    sum += strings[i].length();
            }
        }
//        if(fillAnswers != null){
//            fillAnswers = new String[answerIndex+1];
//        }
        return ssb;
    }

    /**
     * 获取相应Text的宽度
     * @param textView
     * @param str
     * @return
     */
    private float getTextWith(TextView textView, String str){
        Rect bounds = new Rect();
        TextPaint paint = textView.getPaint();
        paint.getTextBounds(str, 0, str.length(), bounds);
        return paint.measureText(str);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int sp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 在界面上添加填写的空白
     */
    private void addEditText() {
        SparseArray<MTextView.ISAttributes> map = mTextView.getListMap();
        MTextView.ISAttributes attributes;
        int index = -1;
        for (int i = 0, j = -1; i < map.size(); i++) {
            attributes = map.get(i);
            if(!"#F#".equals(attributes.getType())){
                EditText editText = new EditText(mContext);
                editText.setTextColor(textColor);
                editText.setPadding(10,0,10,0);
                editText.setSingleLine();
                editText.setGravity(Gravity.CENTER);
                editText.setTag(i);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                editText.setEnabled(enable);
                switch (attributes.getType()){
                    case "#C#":
                        editText.setBackgroundResource(R.drawable.edit_bac);
                        break;
                    case "*":
                        editText.setBackgroundColor(Color.parseColor("#00000000"));
                        break;
                    default:
                        editText.setBackgroundResource(R.drawable.edit_line_bac);

                }
                LayoutParams rl;
                rl = new LayoutParams(
                        answerWith.get(++j), attributes.getBottom() - attributes.getTop());
                rl.leftMargin = attributes.getLeft();
                rl.topMargin = attributes.getTop();
                editTexts.append(++index, editText);
                addView(editText, rl);
            }

        }

        if(editTexts.size() > 0){
            mTextView.setOnDrawedListener(null);
            L.INSTANCE.e("Completion","message----showed---");
            handler.sendEmptyMessage(EDIT_SHOWED);
        }
    }

    public void setEnable(boolean enable){
        for (int i = 0; i < editTexts.size(); i ++){
            EditText editText = editTexts.get(i);
            editText.setEnabled(enable);
        }
    }

    public void showRightAnswer(){
        L.INSTANCE.e("Completion","message----right---");
        handler.sendEmptyMessage(SHOW_ANSWER);
    }

    private void showAnswer(){
        L.INSTANCE.e("Completion","showRightAnswer---");
        for (int i = 0; i < rightAnswers.length; i++) {
            if (i == editTexts.size())
                break;
            editTexts.get(i).setText(rightAnswers[i]);
            editTexts.get(i).setEnabled(false);
        }
        isShowAnswer = false;
    }

    /**
     * 获取填写的答案
     * @return
     */
    public String getAnswer(String regex){
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        String answer;
        for (int i = 0; i < editTexts.size(); i++) {
            answer = editTexts.get(i).getText().toString().trim();
//            if(TextUtils.isEmpty(answer)) {
//                LogApi.e("Completion","getAnswer");
//                return "";
//            }else {
                if(i != editTexts.size() - 1) {
                    //写两个sb的原因呢，是因为要判断学生是否作答了，如果answer没有的话那么两个sb拼的都是regex
                    sb.append(answer).append(regex);
                    sb1.append(regex);
                }else
                    sb.append(answer);
//            }
        }

        L.INSTANCE.e("Completion","getAnswer"+sb.toString());
        return sb.toString().equals(sb1.toString()) ? "" : sb.toString();
    }

    public void setWrong(){
        L.INSTANCE.e("Completion","message----wrong---");
        handler.sendEmptyMessage(SET_WRONG);
    }

    private void showWrong(){
        L.INSTANCE.e("Completion","setWrong---");
        for (int i = 0; i < editTexts.size(); i++) {
            if(editTexts.get(i).getText().toString().trim().equals(rightAnswers[i]))
                editTexts.get(i).setTextColor(Color.GREEN);
            else
                editTexts.get(i).setTextColor(Color.RED);
        }
        isSetWrong = false;
    }

    private String answerStr,regex;
    /**
     * setAnswers
     * @param answerStr
     * @param regex
     */
    public void setAnswer(String answerStr, String regex){
        L.INSTANCE.e("Completion","message----setAnswer---");
        this.answerStr = answerStr;
        this.regex = regex;
        handler.sendEmptyMessage(SET_ANSWER);
    }

    private void fillAnswer(){
        if(!TextUtils.isEmpty(answerStr)){
            if(TextUtils.isEmpty(regex)){
                regex = "#R#";
            }
            Log.e("Completion","setAnswer---" + answerStr);
            if (!TextUtils.isEmpty(answerStr)) {
                String[] split = answerStr.split(regex);
                for (int i = 0; i < split.length; i++) {
                    if (i == editTexts.size())
                        break;
                    editTexts.get(i).setText(split[i]);
                }
            }
        }
        isSetAnswer = false;
    }

    private static final int SET_ANSWER = 111;
    private static final int SHOW_ANSWER = 112;
    private static final int SET_WRONG  = 113;
    private static final int EDIT_SHOWED = 114;

    private boolean isEditShowed = false;
    private boolean isSetAnswer = false;
    private boolean isShowAnswer = false;
    private boolean isSetWrong = false;
    /**
     * 保证对EditText的操作 一定在其被添加进 父控件后
     */
    private AnswerHandler handler = new CompletionView.AnswerHandler(this);

    private static class AnswerHandler extends HandlerUtil<CompletionView> {

        AnswerHandler(CompletionView completionView){
            super(completionView);
        }

        @Override
        public void handleMessage(CompletionView completionView, Message message) {
            if(message.what == EDIT_SHOWED)
                completionView.isEditShowed = true;

            if(message.what == SET_ANSWER)
                completionView.isSetAnswer = true;

            if(message.what == SHOW_ANSWER)
                completionView.isShowAnswer = true;

            if(message.what == SET_WRONG)
                completionView.isSetWrong = true;

            switch (message.what){
                case SET_ANSWER:
                    if(completionView.isEditShowed)
                        completionView.fillAnswer();
                    break;
                case SHOW_ANSWER:
                    if(completionView.isEditShowed)
                        completionView.showAnswer();
                    break;
                case SET_WRONG:
                    if(completionView.isEditShowed)
                        completionView.showWrong();
                    break;
                case EDIT_SHOWED:
                    if(completionView.isShowAnswer)
                        completionView.showAnswer();

                    if(completionView.isSetAnswer)
                        completionView.fillAnswer();

                    if(completionView.isSetWrong)
                        completionView.showWrong();
                    break;
            }
        }
    }

    public void clear(){
        editTexts.clear();
        rightAnswers = null;
        answerWith.clear();
    }
}
