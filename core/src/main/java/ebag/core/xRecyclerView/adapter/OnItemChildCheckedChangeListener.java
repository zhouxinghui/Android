package ebag.core.xRecyclerView.adapter;

import android.widget.CompoundButton;

/**
 * Created by 90323 on 2016/4/27 0027.
 */
public interface OnItemChildCheckedChangeListener {
    void onItemChildCheckedChanged(RecyclerViewHolder holder, CompoundButton buttonView, int position, boolean isChecked);
}
