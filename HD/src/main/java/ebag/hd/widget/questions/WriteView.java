package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import ebag.core.bean.QuestionBean;

/**
 * Created by YZY on 2017/12/23.
 */

public class WriteView extends LinearLayout implements IQuestionEvent {
    private Context context;
    private boolean flag;
    public WriteView(Context context) {
        super(context);
        init(context);
    }

    public WriteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WriteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;

    }

    @Override
    public void setData(QuestionBean questionBean) {

    }

    @Override
    public void show(boolean active) {

    }

    @Override
    public void enable(boolean active) {

    }

    @Override
    public void showResult() {

    }

    @Override
    public String getAnswer() {
        return null;
    }
}
