package ebag.mobile.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/4/8.
 */

public class PersonalPerformanceBean implements Serializable {

    private List<Integer> praise;
    private List<Integer> criticize;

    public List<Integer> getPraise() {
        return praise;
    }

    public void setPraise(List<Integer> praise) {
        this.praise = praise;
    }

    public List<Integer> getCriticize() {
        return criticize;
    }

    public void setCriticize(List<Integer> criticize) {
        this.criticize = criticize;
    }
}
