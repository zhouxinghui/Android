package ebag.core.xRecyclerView.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 90323 on 2016/4/27 0027.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener{

    protected final SparseArray<View> mViews = new SparseArray<>();

    protected Context mContext;
    private RecyclerAdapter adapter;
    private int holderPosition;


    public RecyclerViewHolder(View itemView) {
        super(itemView);
        this.mContext = this.itemView.getContext();
        this.itemView.setOnClickListener(this);
        this.itemView.setOnLongClickListener(this);
    }

    public RecyclerViewHolder addClickListener(@IdRes int viewId) {
        this.getView(viewId).setOnClickListener(this);
        return this;
    }

    public RecyclerViewHolder addLongClickListener(@IdRes int viewId) {
        this.getView(viewId).setOnLongClickListener(this);
        return this;
    }

    public RecyclerViewHolder addCheckedChangeListener(@IdRes int viewId) {
        if(this.getView(viewId) instanceof CompoundButton) {
            ((CompoundButton)this.getView(viewId)).setOnCheckedChangeListener(this);
        }
        return this;
    }

    @Override
    public void onClick(View v) {
        //如果设置了mOnRVItemClickListener的点击事件会屏蔽mOnItemChildClickListener最外层的点击事件
        if (v.getId() == this.itemView.getId() && null != this.adapter.getOnItemClickListener()) {
            this.adapter.getOnItemClickListener().onItemClick(this, v, this.getHolderPosition());
        }else if(this.adapter.getOnItemChildClickListener() != null) {
            this.adapter.getOnItemChildClickListener().onItemChildClick(this, v, this.getHolderPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        //如果设置了mOnRVItemClickListener的长按事件会屏蔽mOnItemChildClickListener最外层的长按事件
        if (v.getId() == this.itemView.getId() && null != this.adapter.getOnItemLongClickListener())
            return this.adapter.getOnItemLongClickListener().onItemLongClick(this, v, this.getHolderPosition());
        else
            return this.adapter.getOnItemChildLongClickListener() != null
                && this.adapter.getOnItemChildLongClickListener().onItemChildLongClick(this, v, this.getHolderPosition());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(this.adapter.getOnItemChildCheckedChangeListener() != null) {
            this.adapter.getOnItemChildCheckedChangeListener()
                    .onItemChildCheckedChanged(this, buttonView, this.getHolderPosition(), isChecked);
        }
    }

    public void updatePosition(int position){
        this.holderPosition = position;
    }

    private int getHolderPosition() {
        return holderPosition;
    }

    public ImageView getImageView(@IdRes int viewId) {
        return (ImageView)this.getView(viewId);
    }

    public TextView getTextView(@IdRes int viewId) {
        return (TextView)this.getView(viewId);
    }

    public View getConvertView() {
        return this.itemView;
    }

    public RecyclerViewHolder setText(@IdRes int viewId, CharSequence text) {
        TextView view = this.getView(viewId);
        view.setText(text);
        return this;
    }

    public RecyclerViewHolder setText(@IdRes int viewId, @StringRes int stringResId) {
        TextView view = this.getView(viewId);
        view.setText(stringResId);
        return this;
    }

    public RecyclerViewHolder setChecked(@IdRes int viewId, boolean checked) {
        View view = getView(viewId);
        // View unable cast to Checkable
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(checked);
        }
        return this;
    }

    public RecyclerViewHolder setTag(@IdRes int viewId, Object tag) {
        View view = this.getView(viewId);
        view.setTag(tag);
        return this;
    }

    public RecyclerViewHolder setTag(@IdRes int viewId, int key, Object tag) {
        View view = this.getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public RecyclerViewHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    public RecyclerViewHolder setGone(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.GONE : View.VISIBLE );
        return this;
    }

    public RecyclerViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = this.getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public RecyclerViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = this.getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public RecyclerViewHolder setTextColorRes(@IdRes int viewId, @ColorRes int textColorResId) {
        TextView view = this.getView(viewId);
        view.setTextColor(this.mContext.getResources().getColor(textColorResId));
        return this;
    }

    public RecyclerViewHolder setTextColor(@IdRes int viewId, int textColor) {
        TextView view = this.getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public RecyclerViewHolder setBackgroundRes(@IdRes int viewId, int backgroundResId) {
        View view = this.getView(viewId);
        view.setBackgroundResource(backgroundResId);
        return this;
    }

    public RecyclerViewHolder setBackgroundColor(@IdRes int viewId, int color) {
        View view = this.getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public RecyclerViewHolder setBackgroundColorRes(@IdRes int viewId, @ColorRes int colorResId) {
        View view = this.getView(viewId);
        view.setBackgroundColor(this.mContext.getResources().getColor(colorResId));
        return this;
    }

    public RecyclerViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = this.getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    protected void setAdapter(RecyclerAdapter adapter) {
        this.adapter = adapter;
    }


    public <T extends View> T getView(@IdRes int viewId) {
        View view = this.mViews.get(viewId);
        if (view == null) {
            view = this.itemView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }
        return (T) view;
    }
}
