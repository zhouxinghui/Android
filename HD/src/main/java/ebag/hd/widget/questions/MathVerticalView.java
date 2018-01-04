package ebag.hd.widget.questions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import ebag.core.bean.QuestionBean;
import ebag.hd.widget.questions.base.BaseQuestionView;

/**
 * Created by unicho on 2018/1/4.
 */

public class MathVerticalView extends BaseQuestionView{

    private List<String> titleList;
    public MathVerticalView(Context context) {
        super(context);
    }

    public MathVerticalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MathVerticalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void addBody(Context context) {

    }

    @Override
    public void setData(QuestionBean questionBean) {
        titleList = new ArrayList<>();
        titleList.add(questionBean.getQuestionHead());
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


}
