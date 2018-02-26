package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.util.ImageLoadUtilKt;
import ebag.core.util.StringUtils;
import ebag.core.xRecyclerView.adapter.OnItemClickListener;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.questions.base.BaseQuestionView;
import ebag.hd.widget.questions.util.QuestionTypeUtils;

/**
 * Created by caoyu on 2017/12/22.
 */

public class ChoiceView extends BaseQuestionView implements OnItemClickListener {

    private OptionAdapter optionAdapter;
    private QuestionBean questionBean;

    private GridLayoutManager layoutManager;
    private boolean isOneLineSet;
    /**
     * 选项
     */
    private List<String> options;
    /**
     * 正确答案
     */
    private String rightAnswer;
    /**
     * 学生答案
     */
    private String studentAnswer;

    private String answer = "";

    private boolean active = true;

    private int choiceType = 0;

    private List<String> title;

    public ChoiceView(Context context) {
        super(context, null);
    }

    public ChoiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ChoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {
        //选项
        RecyclerView optionRecycler = new RecyclerView(mContext);
        optionRecycler.setNestedScrollingEnabled(false);
        layoutManager = new GridLayoutManager(mContext,2);
        optionRecycler.setLayoutManager(layoutManager);
        optionAdapter = new OptionAdapter();
        optionRecycler.setAdapter(optionAdapter);
        optionAdapter.setOnItemClickListener(this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(optionRecycler,layoutParams);
    }

    @Override
    public void setData(QuestionBean questionBean) {
        this.questionBean = questionBean;
        title = new ArrayList<>();
        switch (QuestionTypeUtils.getIntType(questionBean)){
            case QuestionTypeUtils.QUESTIONS_CHOOSE_PIC_BY_WORD://看单词选图
                choiceType = QuestionTypeUtils.QUESTIONS_CHOOSE_PIC_BY_WORD;
                title.add("看单词选图");
                title.add(questionBean.getTitle());
                break;
            case QuestionTypeUtils.QUESTIONS_CHOOSE_WORD_BY_PIC://看图选单词
                choiceType = QuestionTypeUtils.QUESTIONS_CHOOSE_WORD_BY_PIC;
                title.add("看图选单词");
                title.add(questionBean.getTitle());
                break;
            case QuestionTypeUtils.QUESTIONS_CHOISE://选择题
                choiceType = QuestionTypeUtils.QUESTIONS_CHOISE;
                String questionHead = questionBean.getTitle();
                if (questionHead.startsWith("http")) {
                    String[] split = questionHead.split("#R#");
                    title = Arrays.asList(split);
                }else{
                    title.add(questionHead);
                }
                break;
            case QuestionTypeUtils.QUESTIONS_CHOOSE_BY_VOICE://听录音选择
                choiceType = QuestionTypeUtils.QUESTIONS_CHOOSE_BY_VOICE;
                title.add("听录音选择");
                title.add("#M#" + questionBean.getTitle());
                break;
            case QuestionTypeUtils.QUESTIONS_JUDGE://判断题
                choiceType = QuestionTypeUtils.QUESTIONS_JUDGE;
                title = new ArrayList<>();
                if(!StringUtils.INSTANCE.isEmpty(questionBean.getTitle())){
                    title.add(questionBean.getTitle());
                }
                title.add(questionBean.getContent());

                break;
        }
        if(QuestionTypeUtils.getIntType(questionBean) == QuestionTypeUtils.QUESTIONS_JUDGE){
            options = new ArrayList<>();
            options.add("正确");
            options.add("错误");
        }else{
            options = Arrays.asList(questionBean.getContent().split(";"));
        }

        rightAnswer = questionBean.getRightAnswer();
        studentAnswer = questionBean.getAnswer();
    }

    @Override
    public void show(boolean active) {
        this.active = active;
        setTitle(title);
        //设置选项
        optionAdapter.setDatas(options);
        if (!StringUtils.INSTANCE.isEmpty(studentAnswer))
            if(choiceType == QuestionTypeUtils.QUESTIONS_JUDGE){//判断题
                optionAdapter.setSelectedPosition(getJudgeIndex(studentAnswer));
            }else{//选择题
                optionAdapter.setSelectedPosition(getChoiceIndex(studentAnswer));
            }
    }

    @Override
    public void questionActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isQuestionActive() {
        return this.active;
    }

    @Override
    public void showResult() {
        if(choiceType == QuestionTypeUtils.QUESTIONS_JUDGE){//判断题
            optionAdapter.setResult(getJudgeIndex(rightAnswer), getJudgeIndex(studentAnswer));
        }else{//选择题
            optionAdapter.setResult(getChoiceIndex(rightAnswer), getChoiceIndex(studentAnswer));
        }

    }

    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public void reset(){
        this.active = true;
        optionAdapter.setResult(-1, -1);
    }

    private int getChoiceIndex(String str) {
        if(StringUtils.INSTANCE.isEmpty(str) || str.length() > 1)
            return -1;
        char c = str.charAt(0);
        if(c >= 'A' && c <= 'A' + options.size())
            return c - 'A';
        else
            return -1;
    }

    private int getJudgeIndex(String str) {
        if(StringUtils.INSTANCE.isEmpty(str) || str.length() > 1)
            return -1;
        if("对".equals(str)){
            return 0;
        }else if("错".equals(str)){
            return 1;
        }else{
            return -1;
        }
    }

    @Override
    public void onItemClick(RecyclerViewHolder holder, View view, int position) {
        if(!this.active) return;
        optionAdapter.setSelectedPosition(position);

        //点击的时候设置答案
        if(choiceType == QuestionTypeUtils.QUESTIONS_JUDGE){//判断题
            if(position == 0)
                answer = "对";
            else
                answer = "错";
        }else{//选择题
            answer = String.valueOf((char)('A' + position));
        }

        if(onDoingListener != null)
            onDoingListener.onDoing(this);
        questionBean.setAnswer(answer);
    }


    public static class OptionAdapter extends RecyclerAdapter<String>{

        private int selectedPosition = -1;

        private int rightPosition = -1;


        public OptionAdapter() {
            addDefaultItem(R.layout.question_choice_option_item);
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }

        public void setResult(int rightPosition, int selectedPosition) {
            this.selectedPosition = selectedPosition;
            this.rightPosition = rightPosition;
            notifyDataSetChanged();
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, String entity) {
            setter.setText(R.id.tvOption,String.valueOf((char)('A'+position)));
            setter.getTextView(R.id.tvOption).setSelected(position == selectedPosition);
            if(rightPosition != -1){
                if( position == rightPosition && position == selectedPosition){
                    setter.getTextView(R.id.tvOption).setBackgroundResource(R.drawable.question_option_correct);
                    setter.setText(R.id.tvOption,"");
                }else if(position != rightPosition && position == selectedPosition){
                    setter.getTextView(R.id.tvOption).setBackgroundResource(R.drawable.question_option_error);
                    setter.setText(R.id.tvOption,"");
                }else {
                    setter.getTextView(R.id.tvOption).setBackgroundResource(R.drawable.question_option_selector);
                    setter.setText(R.id.tvOption, String.valueOf((char)('A'+position)));
                }
            }

            if(entity.startsWith("http")){
                setter.setGone(R.id.ivOptionContent,false);
                setter.setGone(R.id.tvOptionContent, true);
                ImageLoadUtilKt.loadImage(setter.getImageView(R.id.ivOptionContent), entity);
            }else{
                setter.setGone(R.id.ivOptionContent, true);
                TextView contentTv = setter.getTextView(R.id.tvOptionContent);
                setter.setGone(R.id.tvOptionContent, false);
                contentTv.setText(entity);
            }
        }
    }
}
