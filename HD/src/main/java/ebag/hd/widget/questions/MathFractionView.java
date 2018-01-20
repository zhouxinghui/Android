package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.questions.base.BaseQuestionView;
import ebag.hd.widget.questions.base.MathView;

/**
 * Created by caoyu on 2018/1/6.
 */

public class MathFractionView extends BaseQuestionView {

    private List<String> titleList = new ArrayList<>();
    private FractionAdapter adapter;
    private List<Fraction> contentList = new ArrayList<>();
    private RecyclerView recyclerView;

    public MathFractionView(Context context) {
        super(context);
    }

    public MathFractionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MathFractionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        //等式
        recyclerView = new RecyclerView(context);
        adapter = new FractionAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        addView(recyclerView,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setData(QuestionBean questionBean) {
        titleList.clear();
        titleList.add(questionBean.getQuestionHead());

        contentList.clear();
        String[] contents = questionBean.getQuestionContent().split(";");
        String[] rights = questionBean.getRightAnswer().split(";");
        String[] students = questionBean.getAnswer().split(";");

        Fraction fraction;
        for(int i = 0; i < contents.length; i++){
            fraction = new Fraction();
            fraction.content = contents[i];
            if(i < rights.length)
                fraction.rightAnswer = rights[i];
            if(i < students.length)
                fraction.studentAnswer = students[i];
            contentList.add(fraction);
        }

        if(contents.length > 1){
            recyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext,1));
        }

    }

    @Override
    public void show(boolean active) {
        setTitle(titleList);
        adapter.setResult(false);
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
        for(Fraction fraction : contentList){
            sb.append(fraction.studentAnswer).append(";");
        }

        if(sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public void reset() {

    }

    private static class FractionAdapter extends RecyclerAdapter<Fraction>{

        private boolean isActive = true;

        private boolean isResult = false;

        FractionAdapter(){
            super(R.layout.question_math_fraction);
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
            if(isResult){
                isActive = false;
            }
            notifyDataSetChanged();
        }

        public boolean isResult() {
            return isResult;
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, Fraction entity) {
            MathView mathView = setter.getView(R.id.mathView);
            mathView.setData(entity,MathView.MathType.FRACTION);
            mathView.questionActive(isActive);
            mathView.showResult(isResult);
        }
    }

    public static class Fraction{
        public String content = "";
        public String rightAnswer = "";
        public String studentAnswer = "";
    }
}
