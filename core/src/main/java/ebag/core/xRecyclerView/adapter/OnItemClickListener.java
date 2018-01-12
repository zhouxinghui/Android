package ebag.core.xRecyclerView.adapter;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by 90323 on 2016/4/27 0027.
 */
public interface OnItemClickListener {
    void onItemClick(@NonNull RecyclerViewHolder holder, @NonNull View view, int position);
}
