package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.questions.base.BaseQuestionView;

/**
 * Created by unicho on 2018/1/4.
 */

public class MathVerticalView extends BaseQuestionView{

    private MathVerticalAdapter adapter;

    private RecyclerView recyclerView;
    private GridLayoutManager manager;
    private TextView tvDivisor;
    private String divisorStr;

    /**
     * 是否是除式运算
     */
    private boolean isDivisor;

    private List<String> titleList = new ArrayList<>();
    private List<MathBean> contentList = new ArrayList<>();
    private List<String> rightAnswer = new ArrayList<>();
    private List<String> studentAnswer = new ArrayList<>();

    public MathVerticalView(Context context) {
        super(context);
    }

    public MathVerticalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MathVerticalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(HORIZONTAL);

        //除数
        tvDivisor = new TextView(mContext);
        tvDivisor.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.question_content));
        tvDivisor.setTextColor(getResources().getColor(R.color.question_normal));
        tvDivisor.setBackgroundResource(R.drawable.icon_divisor_slash);
        tvDivisor.setGravity(Gravity.CENTER);
        tvDivisor.setMinWidth(getResources().getDimensionPixelOffset(R.dimen.question_math_true_width));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                getResources().getDimensionPixelSize(R.dimen.question_math_true_width));
        layoutParams.topMargin =  getResources().getDimensionPixelSize(R.dimen.question_math_true_width)
                + getResources().getDimensionPixelSize(R.dimen.question_math_line);
        layout.addView(tvDivisor,layoutParams);

        //除式
        recyclerView = new RecyclerView(context);
        manager = new GridLayoutManager(context,2);
        adapter = new MathVerticalAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        layout.addView(recyclerView);

        addView(layout,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setData(QuestionBean questionBean) {

        //数据初始化
        rightAnswer.clear();
        contentList.clear();
        studentAnswer.clear();
        titleList.clear();
        //初始化数据
        Collections.addAll(rightAnswer,questionBean.getRightAnswer().split(","));
        Collections.addAll(studentAnswer,questionBean.getAnswer().split(","));
        titleList.add(questionBean.getQuestionHead());

        //判断这个竖式是不是除式
        isDivisor = questionBean.getQuestionContent().contains("#H#");
        //除式需要把每一行的第一个 字符默认不加载，替换成  tvDivisor
        // 除式的话由于分子  已经提取到  tvDivisor 中了 因此 竖式每行的Item 默认减一
        int startIndex = isDivisor ? 1 : 0;

        //默认每行的个数
        int horizontalItems = 2;
        int studentIndex = 0;
        int rightIndex = 0;

        boolean numNextIsDivisor = false;

        String[] split = questionBean.getQuestionContent().split("#F#,");
        for(int i = 0; i < split.length; i++){
            String[] strs = split[i].split(",");
            if(i == 0){// 每行 展示的Item 个数， 除式 由于  除数被提出，所以Item书相应减一
                horizontalItems = strs.length - startIndex;
            }

            if(isDivisor){// 如果 需要将 乘法的竖式运算中 乘号 单独作为一行 注释 else 中的内容就行
                if(strs.length == 1 && "#H#".equals(strs[0])){//除号 #H# 所在行
                    numNextIsDivisor = true;
                    //除式 #H# 运算符转化为  横线
                    contentList.add(new MathBean("#E#",true));
                    //跳出了运算符所在行，进入了运算符下面一行
                    continue;
                }

                // 除式  记录 操作符后一行的 第一个数字 作为除数
                if(numNextIsDivisor){
                    numNextIsDivisor = false;
                    divisorStr = strs[0];
                }

            }else{
                // strs[0].length() == 1 这个是 x 号所在行的判断
                if(strs.length == 1 && strs[0].length() == 1){//操作符
                    numNextIsDivisor = true;
                    // 记录运算符
                    divisorStr = strs[0];
                    //跳出了运算符所在行，进入了运算符下面一行
                    continue;
                }

                if(numNextIsDivisor){
                    numNextIsDivisor = false;
                    // 将操作符 设置到  操作符后一行的  第一个字符
                    strs[0] = divisorStr;
                    divisorStr = "";
                }
            }

            if(strs.length == 1){//横线或运算符
                contentList.add(new MathBean(strs[0],true));
            }else{
                //初始化数据， 除式的话由于分子  已经提取到  tvDivisor 中了 因此 竖式每行的Item 默认减一
                for(int j = startIndex; j < strs.length; j++){
                    MathBean mathBean = new MathBean(strs[j]);
                    if(mathBean.isEdit()){
                        if(studentIndex < studentAnswer.size())
                            mathBean.answer = studentAnswer.get(studentIndex++);

                        if(rightIndex < rightAnswer.size())
                            mathBean.rightAnswer = rightAnswer.get(rightIndex++);
                    }
                    contentList.add(mathBean);
                }
            }
        }

        //设置每行多少个Item
        manager = new GridLayoutManager(mContext,horizontalItems);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //横线和操作符，占一行
                return adapter.getItem(position).isSingleLineItem
                        ? manager.getSpanCount() : 1;
            }
        });
        //动态设置宽度
        LayoutParams layoutParams = new LayoutParams(
                (getResources().getDimensionPixelSize(R.dimen.question_math_width)
                        +getResources().getDimensionPixelSize(R.dimen.question_math_margin)) * horizontalItems,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutParams(layoutParams);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void show(boolean active) {
        questionActive(active);
        setTitle(titleList);
        if(isDivisor){//除式，显示除数，设置除数的值
            tvDivisor.setVisibility(VISIBLE);
            tvDivisor.setText(divisorStr);
        }else{
            tvDivisor.setVisibility(GONE);
        }
        adapter.setDatas(contentList);
    }

    @Override
    public void questionActive(boolean active) {
        adapter.setActive(active);
    }

    @Override
    public boolean isQuestionActive() {
        return adapter.isActive();
    }

    @Override
    public void showResult() {
        adapter.setResult(true);
    }

    @Override
    public String getAnswer() {
        StringBuilder sb = new StringBuilder();
        for(MathBean mathBean : contentList){
            if(mathBean.isEdit()){
                sb.append(mathBean.answer).append(",");
            }
        }

        if(sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public void reset() {

    }


    public static class MathVerticalAdapter extends RecyclerAdapter<MathBean>
            implements OnFocusChangeListener, TextWatcher{

        private final static int BLANK_VIEW = 1;
        private final static int NORMAL_VIEW = 2;
        private final static int EDIT_VIEW = 3;
        private final static int LINE_VIEW = 4;

        private boolean isActive = true;

        private boolean isResult = false;

        private MathBean focusMathBean;

        MathVerticalAdapter(){
            //空 占位
            addItemType(BLANK_VIEW, R.layout.question_math_blank);
            //普通  显示单个数字，或运算符
            addItemType(NORMAL_VIEW, R.layout.question_math_normal);
            //
            addItemType(EDIT_VIEW, R.layout.question_math_edit);
            addItemType(LINE_VIEW, R.layout.question_math_line);
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
            notifyDataSetChanged();
        }

        private void setResult(boolean result) {
            isResult = result;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            switch (getItem(position).content){
                case "#G#": return BLANK_VIEW;//空白
                case "#E#": return LINE_VIEW;//横线
                case "#C#": return EDIT_VIEW;//编辑
                default: return NORMAL_VIEW;
            }
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, MathBean entity) {
            switch (getItemViewType(position)){
                case NORMAL_VIEW:
                    setter.setText(R.id.view,entity.content);
                    break;
                case EDIT_VIEW:
                    setter.getView(R.id.view).setEnabled(isActive);
                    setter.setText(R.id.view,entity.answer);
                    if(isResult){
                        setter.getView(R.id.view).setSelected(!entity.isRight());
                    }else{
                        setter.getView(R.id.view).setSelected(false);
                    }
                    setter.setTag(R.id.view,position);
                    setter.getEditText(R.id.view).setOnFocusChangeListener(this);
                    setter.getEditText(R.id.view).addTextChangedListener(this);
                    break;
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            int position = (int) v.getTag();
            if(hasFocus){
                focusMathBean = getItem(position);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(focusMathBean != null)
                focusMathBean.answer = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }

    private class MathBean{
        String content;
        boolean isSingleLineItem;
        String answer = "";
        String rightAnswer = "";

        MathBean(String content){
            this(content,false);
        }

        MathBean(String content, boolean isSingleLineItem){
            this.content = content;
            this.isSingleLineItem = isSingleLineItem;
        }

        boolean isRight(){
            return rightAnswer.equals(answer);
        }

        boolean isEdit(){
            return "#C#".equals(content);
        }
    }

}
