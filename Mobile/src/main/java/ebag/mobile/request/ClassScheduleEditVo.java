package ebag.mobile.request;


import java.util.List;

import ebag.mobile.bean.ClassScheduleBean;

/**
 * Created by unicho on 2018/3/7.
 */

public class ClassScheduleEditVo {
    private List<ClassScheduleBean.ScheduleBean> scheduleCards;

    public List<ClassScheduleBean.ScheduleBean> getScheduleCards() {
        return scheduleCards;
    }

    public void setScheduleCards(List<ClassScheduleBean.ScheduleBean> scheduleCards) {
        this.scheduleCards = scheduleCards;
    }
}
