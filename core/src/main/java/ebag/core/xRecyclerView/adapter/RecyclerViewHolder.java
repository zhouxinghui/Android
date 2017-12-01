package ebag.core.xRecyclerView.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 90323 on 2016/4/27 0027.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    protected Context mContext;
    protected OnRVItemClickListener mOnRVItemClickListener;
    protected OnRVItemLongClickListener mOnRVItemLongClickListener;
    protected ViewHolderHelper mViewHolderHelper;
    protected RecyclerView mRecyclerView;

    private int resId;


    public RecyclerViewHolder(RecyclerView recyclerView, View itemView, OnRVItemClickListener onRVItemClickListener, OnRVItemLongClickListener onRVItemLongClickListener) {
        super(itemView);
        this.mRecyclerView = recyclerView;
        this.mContext = this.mRecyclerView.getContext();
        this.mOnRVItemClickListener = onRVItemClickListener;
        this.mOnRVItemLongClickListener = onRVItemLongClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.mViewHolderHelper = new ViewHolderHelper(this.mRecyclerView, this.itemView);
        this.mViewHolderHelper.setRecyclerViewHolder(this);
    }

    public ViewHolderHelper getViewHolderHelper() {
        return this.mViewHolderHelper;
    }

    public void onClick(View v) {
        if (v.getId() == this.itemView.getId() && null != this.mOnRVItemClickListener) {
            this.mOnRVItemClickListener.onRVItemClick(this.mRecyclerView, v, this.getAdapterPosition());
        }

    }

    public boolean onLongClick(View v) {
        return (v.getId() == this.itemView.getId() && null != this.mOnRVItemLongClickListener)&&this.mOnRVItemLongClickListener.onRVItemLongClick(this.mRecyclerView, v, this.getAdapterPosition());
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
