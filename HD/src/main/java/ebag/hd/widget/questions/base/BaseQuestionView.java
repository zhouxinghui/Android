package ebag.hd.widget.questions.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ebag.core.http.image.SingleImageLoader;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;
import ebag.hd.widget.questions.util.IQuestionEvent;

/**
 * Created by unicho on 2018/1/2.
 */

public abstract class BaseQuestionView extends LinearLayout implements IQuestionEvent{

    protected Context mContext;
    protected HeadAdapter headAdapter;
    public BaseQuestionView(Context context) {
        super(context);
        addTitle(context);
        addBody(context);
    }

    public BaseQuestionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addTitle(context);
        addBody(context);
    }

    public BaseQuestionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTitle(context);
        addBody(context);
    }

    private void addTitle(Context context){
        this.mContext = context;
        //垂直方向
        setOrientation(VERTICAL);
        //标题
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RecyclerView headRecycler = new RecyclerView(mContext);
        headRecycler.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager headManager = new LinearLayoutManager(mContext);
        headRecycler.setLayoutManager(headManager);
        headAdapter = new HeadAdapter();
        headRecycler.setAdapter(headAdapter);
        addView(headRecycler,layoutParams);

    }

    public void setTitle(List<String> title){
        headAdapter.setDatas(title);
    }

    protected abstract void addBody(Context context);


    public static class HeadAdapter extends RecyclerAdapter<String> {

        private final static int VIEW_TITLE = 1;
        private final static int VIEW_IMAGE = 2;
        private final static int VIEW_SUB_TITLE = 3;

        private boolean hideTitle = false;

        HeadAdapter(){
            addItemType(VIEW_SUB_TITLE, R.layout.question_head_sub_title);
            addItemType(VIEW_IMAGE, R.layout.question_head_image);
            addItemType(VIEW_TITLE, R.layout.question_head_title);
        }

        public void setHideTitle(boolean hideTitle) {
            this.hideTitle = hideTitle;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if(getItem(position).startsWith("http")) {//这个Item是图片
                return VIEW_IMAGE;
            }else if(getItem(0).startsWith("http")){//如果第一个Item是图片
                if(position == 1){//第二个设置为 title，其他的为 sub title
                    return VIEW_TITLE;
                }else{
                    return VIEW_SUB_TITLE;
                }
            }else{//第一个item 不是图片
                if(position == 0){//第一个设置为 title，其他的为 sub title
                    return VIEW_TITLE;
                }else{
                    return VIEW_SUB_TITLE;
                }
            }
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, String entity) {
            switch (getItemViewType(position)){
                case VIEW_TITLE:
                    setter.setText(R.id.tvTitle,entity);
                    setter.setGone(R.id.tvTitle,hideTitle);
                    break;
                case VIEW_IMAGE:
                    SingleImageLoader.getInstance().setImage(entity,setter.getImageView(R.id.ivImage));
                    break;
                case VIEW_SUB_TITLE:
                    setter.setText(R.id.tvSubTitle,entity);
                    break;
            }
        }
    }
}
