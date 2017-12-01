package ebag.core.xRecyclerView.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ebag.core.xRecyclerView.XRecyclerView;


/**
 * Created by 90323 on 2016/4/27 0027.
 */
public abstract class RecyclerAdapter<M> extends RecyclerView.Adapter<RecyclerViewHolder>{
    protected final int mItemLayoutId;
    protected Context mContext;
    protected List<M> mDatas = new ArrayList<>();
    protected OnItemChildClickListener mOnItemChildClickListener;
    protected OnItemChildLongClickListener mOnItemChildLongClickListener;
    protected OnItemChildCheckedChangeListener mOnItemChildCheckedChangeListener;
    protected OnRVItemClickListener mOnRVItemClickListener;
    protected OnRVItemLongClickListener mOnRVItemLongClickListener;
    protected RecyclerView mRecyclerView;

    public RecyclerAdapter(RecyclerView recyclerView, int itemLayoutId) {
        this.mRecyclerView = recyclerView;
        this.mContext = this.mRecyclerView.getContext();
        this.mItemLayoutId = itemLayoutId;
        this.mDatas = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return createHolder(parent,this.mItemLayoutId);
    }

    protected RecyclerViewHolder createHolder(ViewGroup parent, int mItemLayoutId){
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(this.mRecyclerView
                , LayoutInflater.from(this.mContext).inflate(mItemLayoutId, parent, false)
                , this.mOnRVItemClickListener, this.mOnRVItemLongClickListener);
        viewHolder.getViewHolderHelper().setOnItemChildClickListener(this.mOnItemChildClickListener);
        viewHolder.getViewHolderHelper().setOnItemChildLongClickListener(this.mOnItemChildLongClickListener);
        viewHolder.getViewHolderHelper().setOnItemChildCheckedChangeListener(this.mOnItemChildCheckedChangeListener);
        this.setItemChildListener(viewHolder.getViewHolderHelper());
        return viewHolder;
    }

    protected void setItemChildListener(ViewHolderHelper viewHolderHelper) {
    }

    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        this.fillData(viewHolder.getViewHolderHelper(), position, this.getXItem(position));
    }

    protected abstract void fillData(ViewHolderHelper setter, int position, M entity);

    public void setOnRVItemClickListener(OnRVItemClickListener onRVItemClickListener) {
        this.mOnRVItemClickListener = onRVItemClickListener;
    }

    public void setOnRVItemLongClickListener(OnRVItemLongClickListener onRVItemLongClickListener) {
        this.mOnRVItemLongClickListener = onRVItemLongClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener) {
        this.mOnItemChildLongClickListener = onItemChildLongClickListener;
    }

    public void setOnItemChildCheckedChangeListener(OnItemChildCheckedChangeListener onItemChildCheckedChangeListener) {
        this.mOnItemChildCheckedChangeListener = onItemChildCheckedChangeListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected M getXItem(int position) {
        if(position >= 0 && position < getItemCount()){
            return mDatas.get(position);
        }else{
            return null;
        }
    }

    public M getItem(int position) {
        if(mRecyclerView instanceof XRecyclerView){
            int headerSize = ((XRecyclerView) mRecyclerView).getHeaderSize();
            if(position >= headerSize
                    && position < getItemCount() + headerSize){
                return mDatas.get(position - headerSize);
            }else{
                return null;
            }
        }else{
            if(position >= 0 && position < getItemCount()){
                return mDatas.get(position);
            }else{
                return null;
            }
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
            if(mRecyclerView instanceof XRecyclerView){
                this.notifyItemRangeInserted(this.mDatas.size() + ((XRecyclerView)mRecyclerView).getHeaderSize()
                        , datas.size());
            }else{
                this.notifyItemRangeInserted(this.mDatas.size(), datas.size());
            }

        }
    }

    /**
     * 从头部添加
     * @param datas
     */
    public void addFirstDatas(List<M> datas) {
        if(datas != null) {
            this.mDatas.addAll(0, datas);
            if(mRecyclerView instanceof XRecyclerView){
                this.notifyItemRangeInserted(((XRecyclerView)mRecyclerView).getHeaderSize()
                        , datas.size());
            }else{
                this.notifyItemRangeInserted(0, datas.size());
            }

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
        if(mRecyclerView instanceof XRecyclerView)
            this.mDatas.remove(position - ((XRecyclerView)mRecyclerView).getHeaderSize());
        else
            this.mDatas.remove(position);
        this.notifyItemRemoved(position);
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
        mDatas.remove(i);
        if(mRecyclerView instanceof XRecyclerView){
            this.notifyItemRemoved(i+((XRecyclerView)mRecyclerView).getHeaderSize());
        }else{
            this.notifyItemRemoved(i);
        }

    }

    /**
     * 具体位置添加一个item
     * @param position
     * @param model
     */
    public void addItem(int position, M model) {
        if(mRecyclerView instanceof XRecyclerView)
            this.mDatas.add(position - ((XRecyclerView)mRecyclerView).getHeaderSize(), model);
        else
            this.mDatas.add(position, model);
        this.notifyItemInserted(position);
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
        if(mRecyclerView instanceof XRecyclerView){
            this.notifyItemInserted(mDatas.size()+((XRecyclerView)mRecyclerView).getHeaderSize());
        }else{
            this.notifyItemInserted(mDatas.size());
        }
    }


    /**
     * 替换相应位置的item
     * @param location
     * @param newModel
     */
    public void setItem(int location, M newModel) {
        if(mRecyclerView instanceof XRecyclerView){
            this.mDatas.set(location - ((XRecyclerView)mRecyclerView).getHeaderSize(), newModel);
        }else{
            this.mDatas.set(location, newModel);
        }
        this.notifyItemChanged(location);
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
        if(mRecyclerView instanceof XRecyclerView)
            this.mDatas.add(toPosition - ((XRecyclerView)mRecyclerView).getHeaderSize()
                    , this.mDatas.remove(fromPosition - ((XRecyclerView)mRecyclerView).getHeaderSize()));
        else
            this.mDatas.add(toPosition, this.mDatas.remove(fromPosition));

        this.notifyItemMoved(fromPosition, toPosition);

    }
}
