package ebag.core.bean;

import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author caoyu
 * @date 2018/2/2
 * @description
 */

public class TypeQuestionBean implements
        IExpandable<QuestionBean>, MultiItemEntity {

    public final static int TYPE = -1;
    public final static int ITEM = -2;
    /**
     * type : 10
     * minType : null
     */

    private String type;
    private String minType;
    private List<QuestionBean> questionVos;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinType() {
        return minType;
    }

    public void setMinType(String minType) {
        this.minType = minType;
    }

    public List<QuestionBean> getQuestionVos() {
        return questionVos;
    }

    public void setQuestionVos(List<QuestionBean> questionVos) {
        this.questionVos = questionVos;
    }

    public void initQuestionPosition(int parentPosition){
        if(questionVos != null){
            for(int i = 0; i < questionVos.size(); i++){
                if(questionVos.get(i) != null){
                    questionVos.get(i).setPosition(i);
                    questionVos.get(i).setParentPosition(parentPosition);
                }
            }
        }
    }

    private boolean isExpand;
    @Override
    public boolean isExpanded() {
        return isExpand;
    }

    @Override
    public void setExpanded(boolean expanded) {
        isExpand = expanded;
    }

    @Override
    public List<QuestionBean> getSubItems() {
        return questionVos;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }
}
