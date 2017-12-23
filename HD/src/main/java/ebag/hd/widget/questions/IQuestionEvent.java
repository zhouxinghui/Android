package ebag.hd.widget.questions;

import ebag.core.bean.QuestionBean;

/**
 * Created by unicho on 2017/12/23.
 */

public interface IQuestionEvent {

    /**
     * 给当前View 绑定问题
     * @param questionBean
     */
    void setData(QuestionBean questionBean);

    /**
     * 展示题目
     * @param active 这个题目是纯粹展示的，还是给人做的
     *               比如老师选题时是不能操作的
     */
    void show(boolean active);

    /**
     * 这个View可不可以操作，老师选题，和学生查看试卷时不能操作
     * @param active
     */
    void enable(boolean active);

    /**
     * 展示做题结果，做对了还是做错了，也是不能操作的
     */
    void showResult();
}
