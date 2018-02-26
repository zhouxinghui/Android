package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.util.StringUtils;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.questions.base.BaseQuestionView;

/**
 * 排序-语文
 * Created by caoyu on 2017/12/28.
 */

public class SortVerticalView extends BaseQuestionView {

    private QuestionBean questionBean;

    private SortAdapter sortAdapter;
    private List<String> titleList;
    private List<SortBean> sortList;
    private String studentAnswer;
    private String rightAnswer;

    public SortVerticalView(Context context) {
        super(context);
    }

    public SortVerticalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SortVerticalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //选项
        RecyclerView optionRecycler = new RecyclerView(context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        optionRecycler.setNestedScrollingEnabled(false);
        optionRecycler.setLayoutManager(layoutManager);
        sortAdapter = new SortAdapter();
        optionRecycler.setAdapter(sortAdapter);

        addView(optionRecycler,layoutParams);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        this.questionBean = questionBean;
        titleList = Arrays.asList(questionBean.getTitle().split("#R#"));
        String[] split = questionBean.getContent().split("#R#");
        sortList = new ArrayList<>();
        for(int i = 0; i < split.length; i++){
            sortList.add(new SortBean(split[i]));
        }
        studentAnswer = questionBean.getAnswer();
        rightAnswer = questionBean.getRightAnswer();
    }

    @Override
    public void show(boolean active) {
        setTitle(titleList);
        sortAdapter.setResult(false);
        sortAdapter.setEnable(true);
        if(!StringUtils.INSTANCE.isEmpty(studentAnswer)){
            //学生答案
            String[] answers = studentAnswer.split(",");
            for(int i = 0; i < sortList.size(); i++){
                if(i < answers.length)
                    sortList.get(i).answer = answers[i];
                else
                    sortList.get(i).answer = "";
            }
        }
        sortAdapter.setDatas(sortList);
    }

    @Override
    public void questionActive(boolean active) {
        sortAdapter.setEnable(active);
    }

    @Override
    public boolean isQuestionActive() {
        return sortAdapter.isEnable();
    }

    @Override
    public void showResult() {
        sortAdapter.setEnable(false);
        setTitle(titleList);

        //判断当前的题目是否正确
        String[] strings = rightAnswer.split(",");
        for(int i = 0; i < strings.length; i++){
            sortAdapter.getItem(i).isRight = sortAdapter.getItem(i).answer.equals(strings[i]);
        }
        sortAdapter.setResult(true);
        sortAdapter.notifyDataSetChanged();
    }

    @Override
    public String getAnswer() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< sortAdapter.getDatas().size(); i++){
            sb.append(sortAdapter.getDatas().get(i).answer);
            if(i < sortAdapter.getDatas().size() -1)
                sb.append(",");
        }
        return sb.toString();
    }

    @Override
    public void reset() {

    }

    private class SortAdapter extends RecyclerAdapter<SortBean> implements OnFocusChangeListener, TextWatcher{

        //是否显示 结果
        private boolean isResult = false;
        private int colorNormal = 0;
        private int colorSelected = 0;

        private SortBean focusSortBean;
        private RecyclerViewHolder focusHolder;

        private boolean enable = true;

        public void setResult(boolean result) {
            isResult = result;
        }

        SortAdapter(){
            super(R.layout.question_sort_vertical);
            colorNormal = getResources().getColor(R.color.question_normal);
            colorSelected = getResources().getColor(R.color.white);
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
            notifyDataSetChanged();
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, SortBean entity) {
            setter.getEditText(R.id.tvSort).setEnabled(enable);
            setter.setText(R.id.tvContent,entity.content);
            setter.setText(R.id.tvSort,entity.answer);

            setter.setTag(R.id.tvSort, setter);
            setter.getEditText(R.id.tvSort).setOnFocusChangeListener(this);
            setter.getEditText(R.id.tvSort).addTextChangedListener(this);
            if(isResult) {
                setter.setTextColor(R.id.tvSort,colorSelected);
                setter.setBackgroundRes(R.id.tvSort,R.drawable.bg_question_sort_rightable);
                setter.getTextView(R.id.tvSort).setSelected(entity.isRight);
            }else{
                setter.setTextColor(R.id.tvSort,colorNormal);
                setter.setBackgroundRes(R.id.tvSort,R.drawable.bg_question_sort_normal);
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                focusHolder = (RecyclerViewHolder) v.getTag();
                focusSortBean = getItem(focusHolder.getHolderPosition());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(focusSortBean != null)
                focusSortBean.answer = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(onDoingListener != null)
                onDoingListener.onDoing(SortVerticalView.this);
            SortVerticalView.this.questionBean.setAnswer(SortVerticalView.this.getAnswer());
        }
    }

    private class SortBean{
        String content = "";
        String answer = "";
        boolean isRight = true;

        SortBean(String content){
            this.content = content;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            return !StringUtils.INSTANCE.isEmpty(this.content) && this.content.equals(((SortBean) obj).content);
        }
    }
}
