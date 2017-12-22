package ebag.core.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zdw on 2017/5/4.
 * 英语-分类-答案解析bean
 */

public class Parse implements Serializable{

    private static final long serialVersionUID = 7850982579414308052L;
    private String title;//分类标题
    private List<String> answerList;//正确答案

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }
}
