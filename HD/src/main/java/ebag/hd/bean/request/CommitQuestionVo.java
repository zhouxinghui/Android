package ebag.hd.bean.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unicho on 2018/2/26.
 */

public class CommitQuestionVo {

    private String homeWorkId;
    private List<QuestionVo> homeWorkQuestionVos = new ArrayList<>();

    public String getHomeWorkId() {
        return homeWorkId;
    }

    public void setHomeWorkId(String homeWorkId) {
        this.homeWorkId = homeWorkId;
    }

    public List<QuestionVo> getHomeWorkQuestionVos() {
        return homeWorkQuestionVos;
    }

    public void setHomeWorkQuestionVos(List<QuestionVo> homeWorkQuestionVos) {
        this.homeWorkQuestionVos.clear();
        this.homeWorkQuestionVos.addAll(homeWorkQuestionVos);
    }

    public boolean add(QuestionVo questionVo){
        return this.homeWorkQuestionVos.add(questionVo);
    }

    public void clear(){
        if(homeWorkQuestionVos != null)
            homeWorkQuestionVos.clear();
    }
}
