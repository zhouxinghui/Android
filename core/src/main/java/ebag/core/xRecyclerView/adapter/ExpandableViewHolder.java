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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ebag.core.util.ImageLoadUtilKt;

/**
 * Created by 90323 on 2016/4/27 0027.
 */
public class ExpandableViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener{

    protected final SparseArray<View> mViews = new SparseArray<>();

    protected Context mContext;
    private ExpandableAdapter adapter;


    public ExpandableViewHolder(View itemView) {
        super(itemView);
        this.mContext = this.itemView.getContext();
        this.itemView.setOnClickListener(this);
        this.itemView.setOnLongClickListener(this);
    }

    public ExpandableViewHolder addClickListener(@IdRes int viewId) {
        this.getView(viewId).setOnClickListener(this);
        return this;
    }

    public ExpandableViewHolder addLongClickListener(@IdRes int viewId) {
        this.getView(viewId).setOnLongClickListener(this);
        return this;
    }

    public ExpandableViewHolder addCheckedChangeListener(@IdRes int viewId) {
        if(this.getView(viewId) instanceof CompoundButton) {
            ((CompoundButton)this.getView(viewId)).setOnCheckedChangeListener(this);
        }
        return this;
    }

    @Override
    public void onClick(View v) {
        //如果设置了mOnRVItemClickListener的点击事件会屏蔽mOnItemChildClickListener最外层的点击事件
//        if (v.getQuestionId() == this.itemView.getQuestionId() && null != this.adapter.getOnItemClickListener()) {
//            this.adapter.getOnItemClickListener().onItemClick(this, v, this.getHolderPosition());
//        }else if(this.adapter.getOnItemChildClickListener() != null) {
//            this.adapter.getOnItemChildClickListener().onItemChildClick(this, v, this.getHolderPosition());
//        }
    }

    @Override
    public boolean onLongClick(View v) {
        //如果设置了mOnRVItemClickListener的长按事件会屏蔽mOnItemChildClickListener最外层的长按事件
//        if (v.getQuestionId() == this.itemView.getQuestionId() && null != this.adapter.getOnItemLongClickListener())
//            return this.adapter.getOnItemLongClickListener().onItemLongClick(this, v, this.getHolderPosition());
//        else
//            return this.adapter.getOnItemChildLongClickListener() != null
//                && this.adapter.getOnItemChildLongClickListener().onItemChildLongClick(this, v, this.getHolderPosition());
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if(this.adapter.getOnItemChildCheckedChangeListener() != null) {
//            this.adapter.getOnItemChildCheckedChangeListener()
//                    .onItemChildCheckedChanged(this, buttonView, this.getHolderPosition(), isChecked);
//        }
    }

    public int getHolderPosition() {
        return getAdapterPosition() - adapter.getHeaderSize();
    }

    public ImageView getImageView(@IdRes int viewId) {
        return (ImageView)this.getView(viewId);
    }

    public TextView getTextView(@IdRes int viewId) {
        return (TextView)this.getView(viewId);
    }

    public EditText getEditText(@IdRes int viewId) {
        return (EditText)this.getView(viewId);
    }

    public View getConvertView() {
        return this.itemView;
    }

    public ExpandableViewHolder loadImage(@IdRes int viewId, String url) {
        ImageLoadUtilKt.loadImage(getImageView(viewId),url);
        return this;
    }

    public ExpandableViewHolder setText(@IdRes int viewId, CharSequence text) {
        TextView view = this.getView(viewId);
        view.setText(text);
        return this;
    }

    public ExpandableViewHolder setText(@IdRes int viewId, @StringRes int stringResId) {
        TextView view = this.getView(viewId);
        view.setText(stringResId);
        return this;
    }

    public ExpandableViewHolder setChecked(@IdRes int viewId, boolean checked) {
        View view = getView(viewId);
        // View unable cast to Checkable
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(checked);
        }
        return this;
    }

    public ExpandableViewHolder setSelected(@IdRes int viewId, boolean selected) {
        getView(viewId).setSelected(selected);
        return this;
    }

    public ExpandableViewHolder setTag(@IdRes int viewId, Object tag) {
        View view = this.getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ExpandableViewHolder setTag(@IdRes int viewId, int key, Object tag) {
        View view = this.getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ExpandableViewHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    public ExpandableViewHolder setGone(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.GONE : View.VISIBLE );
        return this;
    }

    public ExpandableViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = this.getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ExpandableViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = this.getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ExpandableViewHolder setTextColorRes(@IdRes int viewId, @ColorRes int textColorResId) {
        TextView view = this.getView(viewId);
        view.setTextColor(this.mContext.getResources().getColor(textColorResId));
        return this;
    }

    public ExpandableViewHolder setTextColor(@IdRes int viewId, int textColor) {
        TextView view = this.getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ExpandableViewHolder setBackgroundRes(@IdRes int viewId, int backgroundResId) {
        View view = this.getView(viewId);
        view.setBackgroundResource(backgroundResId);
        return this;
    }

    public ExpandableViewHolder setBackgroundColor(@IdRes int viewId, int color) {
        View view = this.getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ExpandableViewHolder setBackgroundColorRes(@IdRes int viewId, @ColorRes int colorResId) {
        View view = this.getView(viewId);
        view.setBackgroundColor(this.mContext.getResources().getColor(colorResId));
        return this;
    }

    public ExpandableViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = this.getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    protected void setAdapter(ExpandableAdapter adapter) {
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
