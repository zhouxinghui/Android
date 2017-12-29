package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import ebag.core.bean.QuestionBean;
import ebag.core.xRecyclerView.adapter.RecyclerAdapter;
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder;
import ebag.hd.widget.questions.util.IQuestionEvent;

/**
 * Created by YZY on 2017/12/29.
 */

public class ConnectionView extends LinearLayout implements IQuestionEvent {
    public ConnectionView(Context context) {
        super(context);
    }

    public ConnectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ConnectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(QuestionBean questionBean) {

    }

    @Override
    public void show(boolean active) {

    }

    @Override
    public void questionActive(boolean active) {

    }

    @Override
    public boolean isQuestionActive() {
        return false;
    }

    @Override
    public void showResult() {

    }

    @Override
    public String getAnswer() {
        return null;
    }

    @Override
    public void reset() {

    }

    private class MyAdapter extends RecyclerAdapter<String>{
        public MyAdapter() {
            super(0);
        }

        @Override
        protected void fillData(RecyclerViewHolder setter, int position, String entity) {

        }
    }
}
