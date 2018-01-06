package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.questions.base.BaseQuestionView;
import ebag.hd.widget.questions.base.MathLine;

/**
 * Created by unicho on 2018/1/5.
 */

public class MathEquationView extends BaseQuestionView {

    private MathEquationAdapter adapter;

    private List<String> titleList = new ArrayList<>();
    private List<MathBean> contentList = new ArrayList<>();
    private List<String> rightAnswer = new ArrayList<>();
    private List<String> studentAnswer = new ArrayList<>();

    public MathEquationView(Context context) {
        super(context);
    }

    public MathEquationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MathEquationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        //等式
        RecyclerView recyclerView = new RecyclerView(context);
        GridLayoutManager manager = new GridLayoutManager(context,3,HORIZONTAL,false);
        adapter = new MathEquationAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setNestedScrollingEnabled(false);

        addView(recyclerView,new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                getResources().getDimensionPixelSize(R.dimen.question_math_true_width) * 3));

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

        boolean isFirstNum;
        // ①、第一个数*号开头表示先做前两个数的运算
        // ②、第一个数#号开头表示先做后两个数的运算

        if(questionBean.getQuestionContent().startsWith("#")){
            isFirstNum = false;
        }else if(questionBean.getQuestionContent().startsWith("*")){
            isFirstNum = true;
        }else{
            return;
        }

        int studentIndex = 0;
        int rightIndex = 0;

        //删除第一个判断运算规则的字符
        String[] splits = questionBean.getQuestionContent().substring(1).split(",");
        for(int i = 0; i < splits.length; i++){
            MathBean mathBean = new MathBean(splits[i]);
            if(mathBean.isEdit()){
                if(studentIndex < studentAnswer.size())
                    mathBean.answer = studentAnswer.get(studentIndex++);

                if(rightIndex < rightAnswer.size())
                    mathBean.rightAnswer = rightAnswer.get(rightIndex++);
            }
            contentList.add(mathBean);
            if(isFirstNum){
                switch (i){
                    case 0:
                        contentList.add(new MathBean(MathLine.BOTTOM_LEFT_HORN));
                        contentList.add(new MathBean(""));
                        break;
                    case 1:
                        contentList.add(new MathBean(MathLine.BOTTOM_HORIZONTAL_LINE));
                        mathBean = new MathBean("#C#");
                        if(studentIndex < studentAnswer.size())
                            mathBean.answer = studentAnswer.get(studentIndex++);

                        if(rightIndex < rightAnswer.size())
                            mathBean.rightAnswer = rightAnswer.get(rightIndex++);
                        contentList.add(mathBean);
                        break;
                    case 2:
                        contentList.add(new MathBean(MathLine.BOTTOM_RIGHT_HORN));
                        contentList.add(new MathBean(MathLine.HORIZONTAL_LINE));
                        break;
                    case 3:
                        contentList.add(new MathBean(""));
                        contentList.add(new MathBean(MathLine.HORIZONTAL_LINE));
                        break;
                    case 4:
                        contentList.add(new MathBean(MathLine.VERTICAL_LINE));
                        contentList.add(new MathBean(MathLine.RIGHT_HORN));
                        break;
                    default:
                        contentList.add(new MathBean(""));
                        contentList.add(new MathBean(""));
                }
            }else{
                switch (i){
                    case 0:
                        contentList.add(new MathBean(MathLine.VERTICAL_LINE));
                        contentList.add(new MathBean(MathLine.LEFT_HORN));
                        break;
                    case 1:
                        contentList.add(new MathBean(""));
                        contentList.add(new MathBean(MathLine.HORIZONTAL_LINE));
                        break;
                    case 2:
                        contentList.add(new MathBean(MathLine.BOTTOM_LEFT_HORN));
                        contentList.add(new MathBean(MathLine.HORIZONTAL_LINE));
                        break;
                    case 3:
                        contentList.add(new MathBean(MathLine.BOTTOM_HORIZONTAL_LINE));
                        mathBean = new MathBean("#C#");
                        if(studentIndex < studentAnswer.size())
                            mathBean.answer = studentAnswer.get(studentIndex++);

                        if(rightIndex < rightAnswer.size())
                            mathBean.rightAnswer = rightAnswer.get(rightIndex++);
                        contentList.add(mathBean);
                        break;
                    case 4:
                        contentList.add(new MathBean(MathLine.BOTTOM_RIGHT_HORN));
                        contentList.add(new MathBean(""));
                        break;
                    default:
                        contentList.add(new MathBean(""));
                        contentList.add(new MathBean(""));
                }
            }
        }

    }

    @Override
    public void show(boolean active) {
        questionActive(active);
        setTitle(titleList);
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

    private static class MathEquationAdapter extends RecyclerAdapter<MathBean>
            implements OnFocusChangeListener, TextWatcher {

        private final static int NORMAL = 1;
        private final static int EDIT = 2;
        private final static int BLANK = 3;
        private final static int LINE = 4;

        private boolean isActive = true;

        private boolean isResult = false;

        private MathBean focusMathBean;
        private RecyclerViewHolder focusHolder;

        MathEquationAdapter(){
            //空 占位
            addItemType(BLANK, R.layout.question_math_equation_blank);
            //普通  显示单个数字，或运算符
            addItemType(NORMAL, R.layout.question_math_equation_normal);
            //填空
            addItemType(EDIT, R.layout.question_math_equation_edit);
            //竖线
            addItemType(LINE, R.layout.question_math_equation_line);
        }

        @Override
        public int getItemViewType(int position) {
            switch (getItem(position).content){
                case "#G#": return BLANK;//空白
                case "#E#": return LINE;//横线
                case "#C#": return EDIT;//编辑
                default: return NORMAL;
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
            switch (getItemViewType(position)){
                case NORMAL:
                    setter.setText(R.id.view,entity.content);
                    break;
                case EDIT:
                    setter.getView(R.id.view).setEnabled(isActive);
                    setter.setText(R.id.view,entity.answer);
                    if(isResult){
                        setter.getView(R.id.view).setSelected(!entity.isRight());
                    }else{
                        setter.getView(R.id.view).setSelected(false);
                    }
                    setter.setTag(R.id.view,setter);
                    setter.getEditText(R.id.view).setOnFocusChangeListener(this);
                    setter.getEditText(R.id.view).addTextChangedListener(this);
                    break;
                case LINE:
                    MathLine line = setter.getView(R.id.view);
                    line.setStyle(entity.lineStyle);
                    break;
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                focusHolder = (RecyclerViewHolder) v.getTag();
                focusMathBean = getItem(focusHolder.getHolderPosition());
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
        String answer = "";
        String rightAnswer = "";
        int lineStyle = 1;

        MathBean(int lineStyle){
            this.content = "#E#";
            this.lineStyle = lineStyle;
        }

        MathBean(String content){
            this.content = content;
        }

        boolean isRight(){
            return rightAnswer.equals(answer);
        }

        boolean isEdit(){
            return "#C#".equals(content);
        }
    }
}
