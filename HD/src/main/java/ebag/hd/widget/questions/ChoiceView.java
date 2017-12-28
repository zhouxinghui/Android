package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.core.http.image.SingleImageLoader;
import ebag.core.util.StringUtils;
import ebag.core.xRecyclerView.adapter.OnItemClickListener;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.questions.util.IQuestionEvent;
import ebag.hd.widget.questions.util.QuestionTypeUtils;

/**
 * Created by unicho on 2017/12/22.
 */

public class ChoiceView extends LinearLayout implements IQuestionEvent
        , OnItemClickListener {

    private Context mContext;
    private TextView tvTitle;
    private ImageView ivTitle;
    private TextView tvContent;
    private OptionAdapter optionAdapter;

    /**
     * 标题
     */
    private String questionHead;

    /**
     * 选择题题目展示的内容
     */
    private String content;
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

    private boolean active = true;

    private int choiceType = 0;

    public ChoiceView(Context context) {
        super(context, null);
        init(context);
    }

    public ChoiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public ChoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //垂直方向
        setOrientation(VERTICAL);
        //设置padding
//        setPadding();
        this.mContext = context;
        //标题 主要用来显示看单词选图片和看图片选单词的这几个字
        tvTitle = new TextView(mContext);
        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.question_head));
        tvTitle.setTextColor(getResources().getColor(R.color.color_question_option_text));
        tvTitle.setPadding(0,0,0,getResources().getDimensionPixelSize(R.dimen.y20));
        addView(tvTitle,titleParams);
        LinearLayout.LayoutParams ivParams =
                new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.x144)
                        , getResources().getDimensionPixelSize(R.dimen.x144));
        //显示标题的图片
        ivTitle = new ImageView(mContext);
        ivTitle.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        addView(ivTitle,ivParams);

        //显示题目内容
        tvContent = new TextView(mContext);

        LinearLayout.LayoutParams contentParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.question_content));
        tvContent.setTextColor(getResources().getColor(R.color.color_question_option_text));
        tvContent.setPadding(0,0,0,getResources().getDimensionPixelSize(R.dimen.y20));

        addView(tvContent, contentParams);

        //选项
        RecyclerView optionRecycler = new RecyclerView(mContext);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext,2);

        optionRecycler.setLayoutManager(layoutManager);

        optionAdapter = new OptionAdapter();
        optionRecycler.setAdapter(optionAdapter);

        optionAdapter.setOnItemClickListener(this);

        addView(optionRecycler);

    }

    @Override
    public void setData(QuestionBean questionBean) {
        switch (QuestionTypeUtils.getIntType(questionBean)){
            case QuestionTypeUtils.QUESTIONS_CHOOSE_PIC_BY_WORD://看单词选图
                choiceType = QuestionTypeUtils.QUESTIONS_CHOOSE_PIC_BY_WORD;
                questionHead = "看单词选图";
                content = questionBean.getQuestionHead();
                break;
            case QuestionTypeUtils.QUESTIONS_CHOOSE_WORD_BY_PIC://看图选单词
                choiceType = QuestionTypeUtils.QUESTIONS_CHOOSE_WORD_BY_PIC;
                questionHead = "看图选单词";
                content = questionBean.getQuestionHead();
                break;
            case QuestionTypeUtils.QUESTIONS_CHOISE://选择题
                choiceType = QuestionTypeUtils.QUESTIONS_CHOISE;
                questionHead = questionBean.getQuestionHead();
                if (questionHead.startsWith("http")) {
                    String[] split = questionHead.split("#R#");
                    if (split.length > 1)
                        questionHead = split[1];
                    content = split[0];
                }else{
                    content = "";
                }
                break;
            case QuestionTypeUtils.QUESTIONS_CHOOSE_BY_VOICE://听录音选择
                choiceType = QuestionTypeUtils.QUESTIONS_CHOOSE_BY_VOICE;

                break;
        }
        options = Arrays.asList(questionBean.getQuestionContent().split(";"));
        rightAnswer = questionBean.getRightAnswer();
        studentAnswer = questionBean.getAnswer();
    }

    @Override
    public void show(boolean active) {
        this.active = active;
        switch (choiceType){
            case QuestionTypeUtils.QUESTIONS_CHOOSE_PIC_BY_WORD://看单词选图
                tvTitle.setVisibility(VISIBLE);
                tvTitle.setText(questionHead);
                ivTitle.setVisibility(GONE);
                tvContent.setVisibility(VISIBLE);
                tvContent.setText(content);
                break;
            case QuestionTypeUtils.QUESTIONS_CHOOSE_WORD_BY_PIC://看图选单词
                tvTitle.setVisibility(VISIBLE);
                tvTitle.setText(questionHead);
                ivTitle.setVisibility(VISIBLE);
                SingleImageLoader.getInstance().setImage(content,ivTitle);
                tvContent.setVisibility(GONE);
                break;

            case QuestionTypeUtils.QUESTIONS_CHOISE://选择题
                if(StringUtils.INSTANCE.isEmpty(questionHead)){
                    tvTitle.setText("");
                    tvTitle.setVisibility(GONE);
                }else{
                    tvTitle.setVisibility(VISIBLE);
                    tvTitle.setText(questionHead);
                }
                if (StringUtils.INSTANCE.isEmpty(content)){
                    ivTitle.setVisibility(GONE);
                }else {
                    ivTitle.setVisibility(VISIBLE);
                    SingleImageLoader.getInstance().setImage(content,ivTitle);
                }
                tvContent.setVisibility(GONE);
                break;
            case QuestionTypeUtils.QUESTIONS_CHOOSE_BY_VOICE://听录音选择
                ivTitle.setVisibility(GONE);
                tvContent.setVisibility(GONE);
                tvTitle.setVisibility(GONE);
                break;
        }
        //设置选项
        optionAdapter.setDatas(options);
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
        show(false);
        if(isRegularAnswer(studentAnswer) && isRegularAnswer(rightAnswer))
            optionAdapter.setResult(getIndex(rightAnswer), getIndex(studentAnswer));
    }

    @Override
    public String getAnswer() {
        return studentAnswer;
    }

    @Override
    public void reset(){
        this.active = true;
        optionAdapter.setResult(-1, -1);
    }

    private int getIndex(String str) {
        if(StringUtils.INSTANCE.isEmpty(str))
            return -1;
        char c = str.charAt(0);
        if(c >= 'A' && c <= 'A' + options.size())
            return c - 'A';
        else
            return -1;
    }

    private boolean isRegularAnswer(String str){
        if(StringUtils.INSTANCE.isEmpty(str))
            return false;
        else if(str.length() != 1)
            return false;
        else return 'A' <= str.charAt(0) && str.charAt(0) <= 'A' + options.size();
    }

    @Override
    public void onItemClick(RecyclerViewHolder holder, View view, int position) {
        if(!this.active) return;
        optionAdapter.setSelectedPosition(position);
        studentAnswer = String.valueOf((char)('A' + position));
    }


    private class OptionAdapter extends RecyclerAdapter<String>{

        private int selectedPosition = -1;

        private int rightPosition = -1;


        OptionAdapter() {
            super(R.layout.question_choice_option_item);
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
                SingleImageLoader.getInstance().setImage(entity,setter.getImageView(R.id.ivOptionContent));
            }else{
                setter.setGone(R.id.ivOptionContent, true);
                setter.setGone(R.id.tvOptionContent, false);
                setter.setText(R.id.tvOptionContent,entity);
            }
        }
    }
}
