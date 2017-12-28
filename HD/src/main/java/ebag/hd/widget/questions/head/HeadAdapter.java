package ebag.hd.widget.questions.head;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;

/**
 * Created by unicho on 2017/12/27.
 */

public class HeadAdapter extends RecyclerAdapter<String> {


    public HeadAdapter(int itemLayoutId) {
        super(itemLayoutId);
    }

    @Override
    protected void fillData(RecyclerViewHolder setter, int position, String entity) {
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    class HAHAHA extends BaseQuickAdapter<String,BaseViewHolder>{

        public HAHAHA(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {

        }
    }
}
