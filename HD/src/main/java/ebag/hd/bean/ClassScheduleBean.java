package ebag.hd.bean;

import java.util.List;

/**
 * Created by unicho on 2018/3/6.
 */

public class ClassScheduleBean {

    /**
     * week : 1
     * scheduleCardVos : [{"id":"4e7556f662dc436ebfc2157aa0986e80","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"1","curriculum":"1"},{"id":"b257d7a7696944d382d70a5c0c98c620","scheduleCardId":null,"classId":null,"subject":"数学","uid":"1727","week":"1","curriculum":"2"},{"id":"77336833c0b345429a06d6546550cb5d","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"1","curriculum":"3"},{"id":"94c816bde9f946d0ae8a433a3a3950b3","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"1","curriculum":"4"},{"id":"6743de1e319c4bfc9c2bea299ae966db","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"1","curriculum":"5"}]
     */

    private String week;
    private List<ScheduleCardBean> scheduleCardVos;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<ScheduleCardBean> getScheduleCardVos() {
        return scheduleCardVos;
    }

    public void setScheduleCardVos(List<ScheduleCardBean> scheduleCardVos) {
        this.scheduleCardVos = scheduleCardVos;
    }

    public static class ScheduleCardBean {
        /**
         * id : 4e7556f662dc436ebfc2157aa0986e80
         * scheduleCardId : null
         * classId : null
         * subject : 语文
         * uid : 1727
         * week : 1
         * curriculum : 1
         */

        private String id;
        private Object scheduleCardId;
        private Object classId;
        private String subject;
        private String uid;
        private String week;
        private String curriculum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getScheduleCardId() {
            return scheduleCardId;
        }

        public void setScheduleCardId(Object scheduleCardId) {
            this.scheduleCardId = scheduleCardId;
        }

        public Object getClassId() {
            return classId;
        }

        public void setClassId(Object classId) {
            this.classId = classId;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getCurriculum() {
            return curriculum;
        }

        public void setCurriculum(String curriculum) {
            this.curriculum = curriculum;
        }
    }
}
