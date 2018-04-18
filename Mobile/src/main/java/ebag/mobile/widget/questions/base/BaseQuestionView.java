package ebag.mobile.widget.questions.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ebag.core.http.image.SingleImageLoader;
import ebag.core.util.StringUtils;
import ebag.core.xRecyclerView.adapter.OnItemChildClickListener;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.mobile.R;

/**
 * Created by caoyu on 2018/1/2.
 */

public abstract class BaseQuestionView extends LinearLayout implements IQuestionEvent {

    protected Context mContext;
    protected HeadAdapter headAdapter;
    protected OnDoingListener onDoingListener;
    public interface OnDoingListener{
        void onDoing(View view);
    }

    public void setOnDoingListener(OnDoingListener onDoingListener) {
        this.onDoingListener = onDoingListener;
    }

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

    /**
     * 播放音频的监听器，需要从外部传入
     */
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener){
        if (onItemChildClickListener != null)
            headAdapter.setOnItemChildClickListener(onItemChildClickListener);
    }

    public void setTitle(List<String> title){
        List<String> newTitle = new ArrayList<>();
        for (String string: title) {
            if (!StringUtils.INSTANCE.isEmpty(string))
                newTitle.add(string);
        }
        headAdapter.setDatas(newTitle);
    }

    protected abstract void addBody(Context context);


    public static class HeadAdapter extends RecyclerAdapter<String> {

        private final static int VIEW_TITLE = 1;
        private final static int VIEW_IMAGE = 2;
        private final static int VIEW_VOICE = 4;
        private final static int VIEW_SUB_TITLE = 3;

        private boolean hideTitle = false;

        HeadAdapter(){
            addItemType(VIEW_SUB_TITLE, R.layout.question_head_sub_title);
            addItemType(VIEW_IMAGE, R.layout.question_head_image);
            addItemType(VIEW_TITLE, R.layout.question_head_title);
            addItemType(VIEW_VOICE, R.layout.question_head_voice);
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
            }else if (getItem(position).startsWith("#M#")){//音频链接
                return VIEW_VOICE;
            } else{//第一个item 不是图片
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
                    if(StringUtils.INSTANCE.isEmpty(entity)){
                        setter.setGone(R.id.tvTitle,true);
                    }else{
                        setter.setText(R.id.tvTitle,entity);
                        setter.setGone(R.id.tvTitle,hideTitle);
                    }
                    break;
                case VIEW_IMAGE:
                    if(StringUtils.INSTANCE.isEmpty(entity)){
                        setter.setGone(R.id.ivImage,true);
                    }else{
                        SingleImageLoader.getInstance().setImage(entity,setter.getImageView(R.id.ivImage));
                    }
                    break;
                case VIEW_SUB_TITLE:
                    if(StringUtils.INSTANCE.isEmpty(entity)){
                        setter.setGone(R.id.tvSubTitle,true);
                    }else{
                        setter.setText(R.id.tvSubTitle,entity);
                    }
                    break;
                case VIEW_VOICE:
                    if(StringUtils.INSTANCE.isEmpty(entity)){
                        setter.setGone(R.id.play_id,true);
                    }else{
                        LinearLayout linearLayout = setter.getView(R.id.play_id);
                        ImageView imageView = setter.getImageView(R.id.image_id);
                        ProgressBar progressBar = setter.getView(R.id.progress_id);
                        AnimationDrawable drawable = (AnimationDrawable) imageView.getBackground();
                        linearLayout.setTag(R.id.image_id, drawable);
                        linearLayout.setTag(R.id.progress_id, progressBar);
                        linearLayout.setTag(R.id.play_id, entity);
                        setter.addClickListener(R.id.play_id);
                    }
                    break;
            }
        }
    }
}
