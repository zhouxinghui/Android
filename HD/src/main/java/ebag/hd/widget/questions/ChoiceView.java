package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.ViewHolderHelper;

/**
 * Created by unicho on 2017/12/22.
 */

public class ChoiceView extends LinearLayout {

    private Context mContext;
    private TextView tvTitle;
    private ImageView ivTitle;
    private TextView tvContent;
    private RecyclerView optionRecycler;
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

        addView(optionRecycler);


    }


    private class OptionAdapter extends RecyclerAdapter<String>{

        public OptionAdapter(RecyclerView recyclerView, int itemLayoutId) {
            super(recyclerView, itemLayoutId);
        }

        @Override
        protected void fillData(ViewHolderHelper setter, int position, String entity) {

        }
    }
}
