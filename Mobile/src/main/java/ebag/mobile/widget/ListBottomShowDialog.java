package ebag.mobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import ebag.mobile.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by liyimin on 2017/9/6.
 */

public abstract class ListBottomShowDialog<SourceData> extends Dialog implements BaseQuickAdapter.OnItemClickListener {
    protected View contentView;
    protected Context context;
    private MyAdapter adapter;
    private OnDialogItemClickListener<SourceData> onDialogItemClickListener;
    public ListBottomShowDialog(@NonNull Context context, List<SourceData> mData) {
        super(context, R.style.ActionSheetDialogStyle);//动画
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(R.layout.list_dialog, null);
        setContentView(contentView);

        Window dialogWindow = getWindow();
        //对话框显示位置
        getWindow().setBackgroundDrawable(new ColorDrawable());
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //设置对话框宽高
        lp.width = context.getResources().getDimensionPixelOffset(R.dimen.x180);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);

        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        adapter = new MyAdapter(mData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        contentView.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        adapter.setOnItemClickListener(this);
    }

    private class MyAdapter extends BaseQuickAdapter<SourceData, BaseViewHolder>{

        public MyAdapter(@Nullable List<SourceData> data) {
            super(R.layout.item_list_dialog, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SourceData item) {
            TextView textView = helper.getView(R.id.tv_id);
            textView.setText(setText(item));
            int position = helper.getAdapterPosition();
            if (position == 0){
                textView.setBackgroundResource(R.drawable.list_dialog_first_tv_bg);
            }else if (position == mData.size() - 1){
                textView.setBackgroundResource(R.drawable.list_dialog_last_tv_bg);
            }else{
                textView.setBackgroundResource(R.drawable.list_dialog_tv_bg);
            }
        }
    }

    public abstract String setText(SourceData data);


    @Override
    public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
        if (onDialogItemClickListener != null){
            onDialogItemClickListener.onItemClick(this, adapter.getData().get(position), position);
        }
    }

    public interface OnDialogItemClickListener<SourceData>{
        void onItemClick(ListBottomShowDialog dialog, SourceData data, int position);
    }

    public ListBottomShowDialog setOnDialogItemClickListener(OnDialogItemClickListener<SourceData> onDialogItemClickListener){
        this.onDialogItemClickListener = onDialogItemClickListener;
        return this;
    }

    public void setData(List<SourceData> mData){
        adapter.setNewData(mData);
    }

    public void show(List<SourceData> mData) {
        adapter.setNewData(mData);
        super.show();
    }

    public void show() {
        super.show();
    }

    /**
     * 点击空白位置 隐藏软键盘
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            InputMethodManager mInputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
