package ebag.hd.widget.questions.head;

import ebag.core.http.image.SingleImageLoader;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.R;

/**
 * Created by unicho on 2017/12/27.
 */

public class HeadAdapter extends RecyclerAdapter<String> {

    private final static int VIEW_TITLE = 1;
    private final static int VIEW_IMAGE = 2;
    private final static int VIEW_SUB_TITLE = 3;

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).startsWith("http")) {//这个Item是图片
            addItemType(VIEW_IMAGE, R.layout.question_head_image);
            return VIEW_IMAGE;
        }else if(getItem(0).startsWith("http")){//如果第一个Item是图片
            if(position == 1){//第二个设置为 title，其他的为 sub title
                addItemType(VIEW_TITLE, R.layout.question_head_title);
                return VIEW_TITLE;
            }else{
                addItemType(VIEW_SUB_TITLE, R.layout.question_head_sub_title);
                return VIEW_SUB_TITLE;
            }
        }else{//第一个item 不是图片
            if(position == 0){//第一个设置为 title，其他的为 sub title
                addItemType(VIEW_TITLE, R.layout.question_head_title);
                return VIEW_TITLE;
            }else{
                addItemType(VIEW_SUB_TITLE, R.layout.question_head_sub_title);
                return VIEW_SUB_TITLE;
            }
        }
    }

    @Override
    protected void fillData(RecyclerViewHolder setter, int position, String entity) {
        switch (getItemViewType(position)){
            case VIEW_TITLE:
                setter.setText(R.id.tvTitle,entity);
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
