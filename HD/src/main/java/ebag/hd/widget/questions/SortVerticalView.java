package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.xRecyclerView.ItemTouchHelperAdapter;
import ebag.core.xRecyclerView.SimpleItemTouchHelperCallback;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.questions.base.BaseQuestionView;

/**
 * 排序-语文
 * Created by unicho on 2017/12/28.
 */

public class SortVerticalView extends BaseQuestionView {

    private SortAdapter sortAdapter;
    private List<String> titleList;
    private List<SortBean> sortList;
    private String studentAnswer;
    private String rightAnswer;
    private boolean isMoved = false;
    private SimpleItemTouchHelperCallback callback;

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

        callback = new SimpleItemTouchHelperCallback(new ItemTouchHelperAdapter() {
            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                isMoved = true;
                sortAdapter.moveItem(fromPosition-sortAdapter.getHeaderSize(),toPosition-sortAdapter.getHeaderSize());
            }

            @Override
            public void onItemDismiss(int position) {

            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(optionRecycler);

        addView(optionRecycler,layoutParams);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        titleList = Arrays.asList(questionBean.getQuestionHead().split("#R#"));
        String[] split = questionBean.getQuestionContent().split("#R#");
        sortList = new ArrayList<>();
        for(int i = 0; i < split.length; i++){
            sortList.add(new SortBean(String.valueOf(i+1),split[i]));
        }
        studentAnswer = questionBean.getAnswer();
        rightAnswer = questionBean.getRightAnswer();
    }

    @Override
    public void show(boolean active) {
        callback.setDragEnable(active);
        setTitle(titleList);
        sortAdapter.setResult(false);

//        if(!StringUtils.INSTANCE.isEmpty(studentAnswer)){
//            //学生答案
//            String[] answers = studentAnswer.split(",");
//            for(int i = 0; i < sortList.size(); i++){
//                if(i < answers.length)
//                    sortList.get(i).answer = answers[i];
//                else
//                    sortList.get(i).answer = "";
//            }
//
//        }else{
            sortAdapter.setDatas(sortList);
//        }
    }

    @Override
    public void questionActive(boolean active) {
        callback.setDragEnable(active);
    }

    @Override
    public boolean isQuestionActive() {
        return callback.dragEnable();
    }

    @Override
    public void showResult() {
        callback.setDragEnable(false);
        setTitle(titleList);
        //学生答案
        String[] answers = studentAnswer.split(",");
        for(int i = 0; i < sortList.size(); i++){
            if(i < answers.length)
                sortList.get(i).answer = answers[i];
            else
                sortList.get(i).answer = "";
        }

        //判断当前的题目是否正确
        String[] strings = rightAnswer.split(",");
        for(int i = 0; i < strings.length; i++){
            sortAdapter.getItem(i).isRight = sortAdapter.getItem(i).position.equals(strings[i]);
        }
        sortAdapter.setResult(true);
        sortAdapter.notifyDataSetChanged();
    }

    @Override
    public String getAnswer() {
        if(isMoved){
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i< sortAdapter.getDatas().size(); i++){
                sb.append(sortAdapter.getDatas().get(i).position);
                if(i < sortAdapter.getDatas().size() -1)
                    sb.append(",");
            }
            return sb.toString();
        }else
            return "";
    }

    @Override
    public void reset() {

    }

    private class SortAdapter extends RecyclerAdapter<SortBean>{

        //是否显示 结果
        private boolean isResult = false;
        private int colorNormal = 0;
        private int colorSelected = 0;

        public void setResult(boolean result) {
            isResult = result;
        }

        SortAdapter(){
            super(R.layout.question_sort_vertical);
            colorNormal = getResources().getColor(R.color.question_normal);
            colorSelected = getResources().getColor(R.color.white);
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, SortBean entity) {
            setter.setText(R.id.tvContent,entity.content);
            if(isResult) {
                setter.setTextColor(R.id.tvSort,colorSelected);
                setter.setText(R.id.tvSort,entity.answer);
                setter.setBackgroundRes(R.id.tvSort,R.drawable.bg_question_sort_rightable);
                setter.getTextView(R.id.tvSort).setSelected(entity.isRight);
            }else{
                setter.setTextColor(R.id.tvSort,colorNormal);
                setter.setBackgroundRes(R.id.tvSort,R.drawable.bg_question_sort_normal);
                setter.setText(R.id.tvSort,entity.position);
            }
        }
    }

    private class SortBean{
        String position = "";
        String content = "";
        String answer = "";
        boolean isRight = true;

        SortBean(String position, String content){
            this.position = position;
            this.content = content;
        }
    }
}
