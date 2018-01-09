package ebag.core.xRecyclerView.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import java.util.List;

import ebag.core.util.T;

/**
 * Created by unicho on 2018/1/9.
 */

public abstract class SectionAdapter extends RecyclerAdapter<SectionBean<T>>{

    protected int mSectionHeadResId;
    protected static final int SECTION_HEADER_VIEW = 0x00000444;

    /**
     *
     * @param layoutResId 分类 布局
     * @param sectionHeadResId 分类头布局
     * @param data
     */
    public SectionAdapter(int layoutResId, int sectionHeadResId, List<T> data){
        addItemType(DEFAULT_VIEW_TYPE,layoutResId);
        addItemType(SECTION_HEADER_VIEW,sectionHeadResId);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent,viewType);
    }

    @Override
    protected void fillData(@NonNull RecyclerViewHolder setter, int position, SectionBean<T> entity) {

    }
}
