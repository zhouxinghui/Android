package ebag.hd.widget.questions;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.xRecyclerView.ItemTouchHelperAdapter;
import ebag.core.xRecyclerView.adapter.OnItemClickListener;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.core.xRecyclerView.manager.DividerItemDecoration;
import ebag.hd.R;
import ebag.hd.widget.questions.base.BaseQuestionView;

/**
 * 排序-英语
 * Created by caoyu on 2017/12/29.
 */

public class SortHorizontalView extends BaseQuestionView {

    private SortAdapter contentAdapter;
    private SortAdapter answerAdapter;
    private String questionContent;
    private List<String> titleList;
    private List<SortBean> contentList;
    private List<SortBean> answerList;
    private boolean active = true;
    private SortItemTouchHelper callback;

    private String rightAnswer;
    private String studentAnswer;

    public SortHorizontalView(Context context) {
        super(context);
    }

    public SortHorizontalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SortHorizontalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        RecyclerView contentRecycler = new RecyclerView(context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        contentRecycler.setNestedScrollingEnabled(false);
        contentRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(HORIZONTAL
                ,getResources().getDimensionPixelOffset(R.dimen.x20), Color.parseColor("#00000000"));
        contentRecycler.addItemDecoration(decoration);
        contentAdapter = new SortAdapter();
        contentRecycler.setAdapter(contentAdapter);
        contentAdapter.setAnswer(false);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.question_tag_height));
        layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.y47);
        addView(contentRecycler,layoutParams);
        contentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewHolder holder, View view, int position) {
                if(active){
                    SortBean content = contentAdapter.getItem(position);
                    contentAdapter.removeItem(content);
                    answerAdapter.addLastItem(content);
                }
            }
        });


        //选项
        RecyclerView answerRecycler = new RecyclerView(context);
        answerRecycler.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        answerRecycler.setLayoutManager(layoutManager2);
        answerRecycler.addItemDecoration(decoration);
        answerAdapter = new SortAdapter();
        answerAdapter.setAnswer(true);
        answerRecycler.setAdapter(answerAdapter);
        answerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewHolder holder, View view, int position) {
                if(active){
                    SortBean content = answerAdapter.getItem(position);
                    answerAdapter.removeItem(content);
                    contentAdapter.addLastItem(content);
                }
            }
        });

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.question_tag_height));
        addView(answerRecycler,layoutParams);

        callback = new SortItemTouchHelper(new ItemTouchHelperAdapter() {
            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                answerAdapter.moveItem(fromPosition-answerAdapter.getHeaderSize(),toPosition-answerAdapter.getHeaderSize());
            }

            @Override
            public void onItemDismiss(int position) {

            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(answerRecycler);


        View view = new View(context);
        view.setBackgroundResource(R.color.question_normal);
        LayoutParams viewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.y1));
        viewParams.topMargin = getResources().getDimensionPixelSize(R.dimen.y2);
        addView(view,viewParams);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        titleList = Arrays.asList(questionBean.getTitle().split("#R#"));
        questionContent = questionBean.getContent();
        rightAnswer = questionBean.getAnswer();
        studentAnswer = questionBean.getStudentAnswer();
    }

    @Override
    public void show(boolean active) {
        questionActive(true);
        setTitle(titleList);
        contentList = new ArrayList<>();
        String[] split = questionContent.split("#R#");
        for(String str : split)
            contentList.add(new SortBean(str));
        contentAdapter.setDatas(contentList);
        answerList = new ArrayList<>();
        answerAdapter.setDatas(answerList);
    }

    @Override
    public void questionActive(boolean active) {
        this.active = active;
        callback.setDragEnable(active);
    }

    @Override
    public boolean isQuestionActive() {
        return active;
    }

    @Override
    public void showResult() {
        questionActive(false);
        setTitle(titleList);

        contentList = new ArrayList<>();
        String[] split = questionContent.split("#R#");
        for(String str : split)
            contentList.add(new SortBean(str));
        contentAdapter.setDatas(contentList);

        String[] rights = rightAnswer.split(",");
        String[] students = studentAnswer.split(",");
        answerList = new ArrayList<>();
        for(int i = 0; i< rights.length; i++){
            if(i < students.length)
                answerList.add(new SortBean(students[i],rights[i].equals(students[i])));
            else
                answerList.add(new SortBean(" ",false));
        }
        answerAdapter.setDatas(answerList);
    }

    @Override
    public String getAnswer() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < answerAdapter.getItemCount(); i++){
            sb.append(answerAdapter.getItem(i).content);
            if(i < answerAdapter.getItemCount() - 1)
                sb.append(",");
        }
        return sb.toString();
    }

    @Override
    public void reset() {

    }


    private class SortAdapter extends RecyclerAdapter<SortBean>{

        private boolean isAnswer = false;
        SortAdapter(){
            super(R.layout.question_sort_horizontal);
        }

        public void setAnswer(boolean answer) {
            isAnswer = answer;
            notifyDataSetChanged();
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, SortBean entity) {
            setter.setText(R.id.tvContent,entity.content);
            if(isAnswer){
                setter.setTextColorRes(R.id.tvContent,R.color.white);
                if(entity.isRight == null){
                    setter.setBackgroundRes(R.id.tvContent,R.drawable.bg_question_sort_en_done);
                }else if(entity.isRight){
                    setter.setBackgroundRes(R.id.tvContent,R.drawable.bg_question_sort_en_right);
                }else{
                    setter.setBackgroundRes(R.id.tvContent,R.drawable.bg_question_sort_en_error);
                }
            }else{
                setter.setTextColorRes(R.id.tvContent,R.color.question_normal);
                setter.setBackgroundRes(R.id.tvContent,R.drawable.bg_question_sort_en_normal);
            }
        }
    }

    private class SortBean{
        String content = "";
        Boolean isRight = null;

        SortBean(String content){
            this.content = content;
        }

        SortBean(String content, boolean isRight){
            this.content = content;
            this.isRight = isRight;
        }
    }

    private class SortItemTouchHelper extends ItemTouchHelper.Callback{

        private final ItemTouchHelperAdapter mAdapter;
        /**
         * 是否可以拖拽
         */
        private boolean isCanDrag = true;

        SortItemTouchHelper(ItemTouchHelperAdapter adapter){
            mAdapter = adapter;
        }

        /**
         * 设置是否可以被拖拽
         *
         * @param canDrag 是true，否false
         */
        public void setDragEnable(boolean canDrag) {
            isCanDrag = canDrag;
        }

        public boolean dragEnable() {
            return isCanDrag;
        }


        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // Enable drag and swipe in both directions
            final int dragFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }
            // Notify the adapter of the move
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

            if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
                viewHolder.itemView.setSelected(true);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setSelected(false);
        }
    }
}
