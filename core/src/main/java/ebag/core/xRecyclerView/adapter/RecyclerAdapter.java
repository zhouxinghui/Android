package ebag.core.xRecyclerView.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ebag.core.xRecyclerView.XRecyclerView;


/**
 * Created by 90323 on 2016/4/27 0027.
 */
public abstract class RecyclerAdapter<M> extends RecyclerView.Adapter<RecyclerViewHolder> implements
        XRecyclerView.OnHeaderSizeChangedListener{

    protected static final int DEFAULT_VIEW_TYPE = -0xff;
    protected static final int VIEW_NOT_FIND = -404;

    private List<M> mDatas = new ArrayList<>();

    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemChildLongClickListener mOnItemChildLongClickListener;
    private OnItemChildCheckedChangeListener mOnItemChildCheckedChangeListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private SparseIntArray layouts = new SparseIntArray();

    private int headerSize = 0;

    public RecyclerAdapter() {}

    public RecyclerAdapter(int itemLayoutId) {
        addItemType(DEFAULT_VIEW_TYPE,itemLayoutId);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(getLayoutId(viewType) == VIEW_NOT_FIND){
            throw  new IllegalArgumentException("多布局 时请使用 addItemType 方法添加布局");
        }

        return createBaseViewHolder(parent,viewType);
    }

    protected RecyclerViewHolder createBaseViewHolder(ViewGroup parent, int viewType){
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false));
        viewHolder.setAdapter(this);
        return viewHolder;
    }

    /**
     * 多布局时使用，建议在getItemViewType 方法中做
     * @param layoutResId
     */
    protected void addDefaultItem(@LayoutRes int layoutResId) {
        addItemType(DEFAULT_VIEW_TYPE,layoutResId);
    }

    /**
     * 多布局时使用，建议在getItemViewType 方法中做
     * @param type
     * @param layoutResId
     */
    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if(layouts.get(type) == 0)
            layouts.put(type, layoutResId);
    }

    @Override
    public int getItemViewType(int position) {
        return DEFAULT_VIEW_TYPE;
    }

    /**
     * 当没有多布局设置时，默认使用
     * @param viewType
     * @return
     */
    private int getLayoutId(int viewType) {
        return layouts.get(viewType,VIEW_NOT_FIND);
    }

    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        this.fillData(viewHolder, position, mDatas.get(position));
    }

    protected abstract void fillData(@NonNull RecyclerViewHolder setter, int position, M entity);

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public M getItem(int position) {
        if(position >= 0 && position < getItemCount()){
            return mDatas.get(position);
        }else{
            return null;
        }
    }

    public List<M> getDatas() {
        return this.mDatas;
    }

    /**
     * 添加到末尾
     * @param datas
     */
    public void addMoreDatas(List<M> datas) {
        if(datas != null) {
            this.mDatas.addAll(datas);
            this.notifyItemRangeInserted(this.mDatas.size() + headerSize, datas.size());
        }
    }

    /**
     * 从头部添加
     * @param datas
     */
    public void addFirstDatas(List<M> datas) {
        if(datas != null) {
            this.mDatas.addAll(0, datas);
            this.notifyItemRangeInserted(headerSize, datas.size());
        }
    }

    /**
     * 更新数据源
     * @param datas
     */
    public void setDatas(List<M> datas) {
        if(datas != null) {
            this.mDatas = datas;
        } else {
            this.mDatas.clear();
        }
        this.notifyDataSetChanged();

    }

    /**
     * 清除数据
     */
    public void clear() {
        this.mDatas.clear();
        this.notifyDataSetChanged();
    }

    /**
     * 移除相应位置的item
     * @param position
     */
    public void removeItem(int position) {
        this.mDatas.remove(position);
        this.notifyItemRemoved(position + headerSize);
    }

    /**
     * 移除一段item
     * @param datas
     */
    public void removeItems(List<M> datas){
        this.mDatas.removeAll(datas);
        this.notifyAll();
    }

    /**
     * 移除相应的item
     * @param model
     */
    public void removeItem(M model) {
        int i = this.mDatas.indexOf(model);
        if(i >= 0) {
            mDatas.remove(i);
            this.notifyItemRemoved(i + headerSize);
        }
    }

    /**
     * 具体位置添加一个item
     * @param position
     * @param model
     */
    public void addItem(int position, M model) {
        this.mDatas.add(position, model);
        this.notifyItemInserted(position + headerSize);
    }

    /**
     * 添加到第一个位置
     * @param model
     */
    public void addFirstItem(M model) {
        this.addItem(0, model);
    }

    /**
     * 添加到最后一个位置
     * @param model
     */
    public void addLastItem(M model) {
        this.mDatas.add( model);
        this.notifyItemInserted(mDatas.size() + headerSize);
    }


    /**
     * 替换相应位置的item
     * @param location
     * @param newModel
     */
    public void setItem(int location, M newModel) {
        this.mDatas.set(location, newModel);
        this.notifyItemChanged(location + headerSize);
    }

    /**
     * 替换相应的item
     * @param oldModel
     * @param newModel
     */
    public void setItem(M oldModel, M newModel) {
        this.setItem(this.mDatas.indexOf(oldModel), newModel);
    }

    /**
     * 交换两个位置的item
     * @param fromPosition
     * @param toPosition
     */
    public void moveItem(int fromPosition, int toPosition) {
        Collections.swap(mDatas, fromPosition, toPosition);
        this.notifyItemMoved(fromPosition + headerSize, toPosition + headerSize);
        this.notifyItemChanged(fromPosition + headerSize);
        this.notifyItemChanged(toPosition + headerSize);
    }

    /**
     * 获取子控件点击事件
     * @return
     */
    public OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    /**
     * 设置子控件的点击事件
     * @param mOnItemChildClickListener
     */
    public void setOnItemChildClickListener(OnItemChildClickListener mOnItemChildClickListener) {
        this.mOnItemChildClickListener = mOnItemChildClickListener;
    }

    /**
     * 获取子控件的长按事件
     * @return
     */
    public OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return mOnItemChildLongClickListener;
    }

    /**
     * 设置子控件的长按事件
     * @param mOnItemChildLongClickListener
     */
    public void setOnItemChildLongClickListener(OnItemChildLongClickListener mOnItemChildLongClickListener) {
        this.mOnItemChildLongClickListener = mOnItemChildLongClickListener;
    }

    /**
     * 获取子控件的checked changed 事件
     * @return
     */
    public OnItemChildCheckedChangeListener getOnItemChildCheckedChangeListener() {
        return mOnItemChildCheckedChangeListener;
    }

    /**
     * 设置子控件的checked changed 事件
     * @param mOnItemChildCheckedChangeListener
     */
    public void setOnItemChildCheckedChangeListener(OnItemChildCheckedChangeListener mOnItemChildCheckedChangeListener) {
        this.mOnItemChildCheckedChangeListener = mOnItemChildCheckedChangeListener;
    }

    /**
     * 获取item点击事件
     * @return
     */
    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    /**
     * 设置item点击事件
     * @param mOnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 获取item长按事件
     * @return
     */
    public OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    /**
     * 设置item长按事件
     * @param mOnItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
}
