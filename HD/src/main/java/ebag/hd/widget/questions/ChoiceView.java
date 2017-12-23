package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ebag.core.bean.QuestionBean;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.ViewHolderHelper;
import ebag.hd.R;

/**
 * Created by unicho on 2017/12/22.
 */

public class ChoiceView extends LinearLayout implements IQuestionEvent{

    private Context mContext;
    private TextView tvTitle;
    private ImageView ivTitle;
    private TextView tvContent;
    private RecyclerView optionRecycler;
    private OptionAdapter optionAdapter;
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

        addView(tvTitle);

        //显示标题的图片
        ivTitle = new ImageView(mContext);

        addView(ivTitle);

        //显示题目内容
        tvContent = new TextView(mContext);

        addView(tvContent);

        //选项
        optionRecycler = new RecyclerView(mContext);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext,2);

        optionRecycler.setLayoutManager(layoutManager);

        optionAdapter = new OptionAdapter(optionRecycler);
        optionRecycler.setAdapter(optionAdapter);

        addView(optionRecycler);

    }

    @Override
    public void setData(QuestionBean questionBean) {

    }

    @Override
    public void show(boolean active) {

    }

    @Override
    public void enable(boolean active) {

    }

    @Override
    public void showResult() {

    }


    private class OptionAdapter extends RecyclerAdapter<String>{

        private boolean enable = false;

        private int selectedPosition = -1;

        private int rightPosition = -1;


        OptionAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.question_choice_option_item);
        }

        @Override
        protected void fillData(ViewHolderHelper setter, int position, String entity) {

            setter.setText(R.id.tvOption,'A'+position);
            setter.getTextView(R.id.tvOption).setSelected(position == selectedPosition);

            setter.getTextView(R.id.tvOption).setEnabled(enable);

            if(rightPosition != -1){
                if( position == rightPosition && position == selectedPosition){
                    setter.getTextView(R.id.tvOption).setBackgroundResource(R.drawable.question_option_correct);
                    setter.setText(R.id.tvOption,"");
                }else if(position != rightPosition && position == selectedPosition){
                    setter.getTextView(R.id.tvOption).setBackgroundResource(R.drawable.question_option_error);
                    setter.setText(R.id.tvOption,"");
                }else {
                    setter.getTextView(R.id.tvOption).setBackgroundResource(R.drawable.question_option_selector);
                    setter.setText(R.id.tvOption, 'A' + position);
                }
            }


        }
    }
}
