package ebag.core.xRecyclerView.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 90323 on 2016/4/27 0027.
 */
public class ViewHolderHelper implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    protected final SparseArray<View> mViews = new SparseArray();
    protected OnItemChildClickListener mOnItemChildClickListener;
    protected OnItemChildLongClickListener mOnItemChildLongClickListener;
    protected OnItemChildCheckedChangeListener mOnItemChildCheckedChangeListener;
    protected View mConvertView;
    protected Context mContext;
    protected int mPosition;
    protected RecyclerViewHolder mRecyclerViewHolder;
    protected RecyclerView mRecyclerView;
    protected ViewGroup mAdapterView;
    protected Object mObj;

    public ViewHolderHelper(ViewGroup adapterView, View convertView) {
        this.mAdapterView = adapterView;
        this.mConvertView = convertView;
        this.mContext = convertView.getContext();
    }

    public ViewHolderHelper(RecyclerView recyclerView, View convertView) {
        this.mRecyclerView = recyclerView;
        this.mConvertView = convertView;
        this.mContext = convertView.getContext();
    }

    public void setRecyclerViewHolder(RecyclerViewHolder recyclerViewHolder) {
        this.mRecyclerViewHolder = recyclerViewHolder;
    }

    public RecyclerViewHolder getRecyclerViewHolder() {
        return this.mRecyclerViewHolder;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public int getPosition() {
        return this.mRecyclerViewHolder != null?this.mRecyclerViewHolder.getAdapterPosition():this.mPosition;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setItemChildClickListener(@IdRes int viewId) {
        this.getView(viewId).setOnClickListener(this);
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener) {
        this.mOnItemChildLongClickListener = onItemChildLongClickListener;
    }

    public void setItemChildLongClickListener(@IdRes int viewId) {
        this.getView(viewId).setOnLongClickListener(this);
    }

    public void setOnItemChildCheckedChangeListener(OnItemChildCheckedChangeListener onItemChildCheckedChangeListener) {
        this.mOnItemChildCheckedChangeListener = onItemChildCheckedChangeListener;
    }

    public void setItemChildCheckedChangeListener(@IdRes int viewId) {
        if(this.getView(viewId) instanceof CompoundButton) {
            ((CompoundButton)this.getView(viewId)).setOnCheckedChangeListener(this);
        }

    }

    public void onClick(View v) {
        if(this.mOnItemChildClickListener != null) {
            if(this.mRecyclerView != null) {
                this.mOnItemChildClickListener.onItemChildClick(this.mRecyclerView, v, this.getPosition());
            } else if(this.mAdapterView != null) {
                this.mOnItemChildClickListener.onItemChildClick(this.mAdapterView, v, this.getPosition());
            }
        }

    }

    public boolean onLongClick(View v) {
        if(this.mOnItemChildLongClickListener != null) {
            if(this.mRecyclerView != null) {
                return this.mOnItemChildLongClickListener.onItemChildLongClick(this.mRecyclerView, v, this.getPosition());
            }

            if(this.mAdapterView != null) {
                return this.mOnItemChildLongClickListener.onItemChildLongClick(this.mAdapterView, v, this.getPosition());
            }
        }

        return false;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(this.mOnItemChildCheckedChangeListener != null) {
            if(this.mRecyclerView != null) {
                this.mOnItemChildCheckedChangeListener.onItemChildCheckedChanged(this.mRecyclerView, buttonView, this.getPosition(), isChecked);
            } else if(this.mAdapterView != null) {
                this.mOnItemChildCheckedChangeListener.onItemChildCheckedChanged(this.mAdapterView, buttonView, this.getPosition(), isChecked);
            }
        }

    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = this.mViews.get(viewId);
        if(view == null) {
            view = this.mConvertView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }

        return (T) view;
    }

    public ImageView getImageView(@IdRes int viewId) {
        return (ImageView)this.getView(viewId);
    }

    public TextView getTextView(@IdRes int viewId) {
        return (TextView)this.getView(viewId);
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public void setObj(Object obj) {
        this.mObj = obj;
    }

    public Object getObj() {
        return this.mObj;
    }

    public ViewHolderHelper setText(@IdRes int viewId, CharSequence text) {
        TextView view = this.getView(viewId);
        view.setText(text);
        return this;
    }

    public ViewHolderHelper setText(@IdRes int viewId, @StringRes int stringResId) {
        TextView view = this.getView(viewId);
        view.setText(stringResId);
        return this;
    }

    public ViewHolderHelper setHtml(@IdRes int viewId, String source) {
        TextView view = this.getView(viewId);
        view.setText(Html.fromHtml(source));
        return this;
    }

    public ViewHolderHelper setChecked(@IdRes int viewId, boolean checked) {
        Checkable view = this.getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public ViewHolderHelper setTag(@IdRes int viewId, Object tag) {
        View view = this.getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolderHelper setTag(@IdRes int viewId, int key, Object tag) {
        View view = this.getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolderHelper setVisibility(@IdRes int viewId, int visibility) {
        View view = this.getView(viewId);
        view.setVisibility(visibility);
        return this;
    }
    public ViewHolderHelper setVisible(@IdRes int viewId, boolean visibility) {
        View view = this.getView(viewId);
        if (visibility)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
        return this;
    }

    public ViewHolderHelper setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = this.getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolderHelper setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = this.getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolderHelper setTextColorRes(@IdRes int viewId, @ColorRes int textColorResId) {
        TextView view = this.getView(viewId);
        view.setTextColor(this.mContext.getResources().getColor(textColorResId));
        return this;
    }

    public ViewHolderHelper setTextColor(@IdRes int viewId, int textColor) {
        TextView view = this.getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolderHelper setBackgroundRes(@IdRes int viewId, int backgroundResId) {
        View view = this.getView(viewId);
        view.setBackgroundResource(backgroundResId);
        return this;
    }

    public ViewHolderHelper setBackgroundColor(@IdRes int viewId, int color) {
        View view = this.getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolderHelper setBackgroundColorRes(@IdRes int viewId, @ColorRes int colorResId) {
        View view = this.getView(viewId);
        view.setBackgroundColor(this.mContext.getResources().getColor(colorResId));
        return this;
    }

    public ViewHolderHelper setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = this.getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public ViewHolderHelper showView(@IdRes int viewId){
        getView(viewId).setVisibility(View.VISIBLE);
        return this;
    }

    public ViewHolderHelper hideView(@IdRes int viewId){
        getView(viewId).setVisibility(View.INVISIBLE);
        return this;
    }

    public ViewHolderHelper goneView(@IdRes int viewId){
        getView(viewId).setVisibility(View.GONE);
        return this;
    }

}
