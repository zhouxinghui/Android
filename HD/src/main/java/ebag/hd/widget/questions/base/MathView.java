package ebag.hd.widget.questions.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ebag.core.util.StringUtils;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.questions.MathFractionView;

/**
 * Created by caoyu on 2018/1/5.
 */

public class MathView extends RecyclerView {

    private GridLayoutManager manager;
    private MathAdapter adapter;

    private List<MathBean> contentList = new ArrayList<>();
    private List<String> rightAnswer = new ArrayList<>();
    private List<String> studentAnswer = new ArrayList<>();

    private MathType mMathType;

    private Context mContext;

    private MathFractionView.Fraction fraction;

    public MathView(Context context) {
        this(context,null);
    }

    public MathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MathView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.mContext = context;

        adapter = new MathAdapter();
        setAdapter(adapter);
        setNestedScrollingEnabled(false);
    }

    public void setData(MathFractionView.Fraction fraction, MathType mathType) {
        this.fraction = fraction;
        this.mMathType = mathType;
        contentList.clear();
        rightAnswer.clear();
        studentAnswer.clear();
        // 视图初始化
        adapter.clear();

        //初始化数据
        if(!StringUtils.INSTANCE.isEmpty(fraction.rightAnswer)){
            Collections.addAll(rightAnswer,fraction.rightAnswer.split(","));
        }

        if(!StringUtils.INSTANCE.isEmpty(fraction.studentAnswer)){
            Collections.addAll(studentAnswer,fraction.studentAnswer.split(","));
        }

        if(!StringUtils.INSTANCE.isEmpty(fraction.content)){
            switch (mathType){
                case FRACTION://分式
                    setFractionData(fraction.content);
                    break;
                case EQUATION://等式
                    break;
                case VERTICAL://竖式
                    break;
            }
        }
        questionActive(true);
        adapter.setDatas(contentList);
    }

    private int widthItems = 0;

    public int getWidthItems() {
        return widthItems;
    }

    private void setFractionData(String content){
        String[] strs = content.split(":");
        widthItems = strs.length;
        if(widthItems == 0) return;

        manager = new GridLayoutManager(mContext, 2, LinearLayoutManager.HORIZONTAL,false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItem(position).isSingleLine
                        ? manager.getSpanCount() : 1;
            }
        });
        setLayoutManager(manager);

        contentList = new ArrayList<>();
        studentIndex = 0;
        rightIndex = 0;

        for (String str : strs) {
            String[] strings = str.split(",");
            if(strings.length == 2){//分式
                contentList.add(resolve(strings[0], true));
                contentList.add(resolve(strings[1], false));
            }else{//运算符
                MathBean mathBean = MathBean.newMath(strings[0]);
                mathBean.isSingleLine = true;
                contentList.add(mathBean);
            }
        }
    }
    private int studentIndex = 0;
    private int rightIndex = 0;
    private MathBean resolve(String str, boolean isMolecule){

        if(str.length() < 2){
            return MathBean.newMath(str,isMolecule);
        }else{
            if(isMolecule){
                str = str.substring(1);
            }else{
                str = str.substring(0,str.length() -1);
            }

            MathBean mathBean = MathBean.newMath(str,isMolecule);
            if(mathBean.isEdit()){
                if(studentIndex < studentAnswer.size())
                    mathBean.studentAnswer = studentAnswer.get(studentIndex++);

                if(rightIndex < rightAnswer.size())
                    mathBean.rightAnswer = rightAnswer.get(rightIndex++);
            }
            return mathBean;
        }
    }

    public void questionActive(boolean active) {
        adapter.setActive(active);
    }

    public boolean isQuestionActive() {
        return adapter.isActive();
    }

    public void showResult(boolean showResult) {
        adapter.setResult(showResult);
    }

    public String getAnswer() {
        StringBuilder sb = new StringBuilder();
        for(MathBean mathBean : contentList){
            if(mathBean.isEdit()){
                sb.append(mathBean.studentAnswer).append(",");
            }
        }

        if(sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public void reset() {

    }

    private class MathAdapter extends RecyclerAdapter<MathBean>
            implements OnFocusChangeListener, TextWatcher {

        private static final int NORMAL= 1;
        private static final int EDIT = 2;
        private static final int OPERATOR = 3;

        private boolean isActive = true;
        private boolean isResult = false;

        private MathBean focusMathBean;

        MathAdapter(){
            addItemType(NORMAL,R.layout.question_math_fraction_normal);
            addItemType(EDIT,R.layout.question_math_fraction_edit);
            addItemType(OPERATOR,R.layout.question_math_fraction_operator);
        }

        @Override
        public int getItemViewType(int position) {
            if(getItem(position).isSingleLine)
                return OPERATOR;
            if(getItem(position).isEdit()){//编辑
                return EDIT;
            }else{//文本
                return NORMAL;
            }
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
        protected void fillData(RecyclerViewHolder setter, int position, MathBean entity) {
            if(getItemViewType(position) == OPERATOR){
                setter.setText(R.id.view,entity.content);
            }else{

                MathFraction mathFraction = setter.getView(R.id.lineView);
                if(entity.isMolecule){
                    mathFraction.setLineType(MathFraction.BOTTOM);
                }else{
                    mathFraction.setLineType(MathFraction.TOP);
                }

                switch (getItemViewType(position)){
                    case NORMAL:
                        setter.setText(R.id.view,entity.content);
                        break;
                    case EDIT:
                        setter.getView(R.id.view).setEnabled(isActive);
                        setter.setText(R.id.view,entity.studentAnswer);
                        if(isResult){
                            setter.getView(R.id.view).setSelected(!entity.isRight());
                        }else{
                            setter.getView(R.id.view).setSelected(false);
                        }
                        setter.setTag(R.id.view, position);
                        setter.getEditText(R.id.view).setOnFocusChangeListener(this);
                        setter.getEditText(R.id.view).addTextChangedListener(this);
                        break;
                }
            }

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                int position = (int) v.getTag();
                focusMathBean = getItem(position);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(focusMathBean != null){
                focusMathBean.studentAnswer = s.toString();
                fraction.studentAnswer = getAnswer();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }

    private static class MathBean{
        String content;
        String rightAnswer = "";
        String studentAnswer = "";
        boolean isSingleLine = false;
        /**
         * 需要下划线
         */
        boolean isMolecule = false;

        /**
         * 正常赋值，包括正常的  空白 #G#  正常的填空 #C#  以及其他字符
         * @param content
         */
        private MathBean(String content){
            this.content = content;
        }

        public static MathBean newMath(String content){
            return new MathBean(content);
        }

        public static MathBean newMath(String content, boolean isMolecule){
            MathBean mathBean = new MathBean(content);
            mathBean.isMolecule = isMolecule;
            return mathBean;
        }

        boolean isRight(){
            return rightAnswer.equals(studentAnswer);
        }

        boolean isEdit(){
            return "*".equals(content);
        }
    }

    public enum  MathType{
        FRACTION    (0), // 分式
        EQUATION    (1), // 等式
        VERTICAL    (2);  // 竖式

        MathType(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }
}
