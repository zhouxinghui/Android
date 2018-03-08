package ebag.hd.bean;

import java.util.List;

/**
 * Created by unicho on 2018/3/6.
 */

public class ClassScheduleBean {

    /**
     * currentWeek : 3
     * moningList : [{"week":"1","scheduleCardVos":[{"id":"4e7556f662dc436ebfc2157aa0986e80","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"1","type":"上午","curriculum":"1"},{"id":"b257d7a7696944d382d70a5c0c98c620","scheduleCardId":null,"classId":null,"subject":"数学","uid":"1727","week":"1","type":"上午","curriculum":"2"},{"id":"77336833c0b345429a06d6546550cb5d","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"1","type":"上午","curriculum":"3"},{"id":"94c816bde9f946d0ae8a433a3a3950b3","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"1","type":"上午","curriculum":"4"}]},{"week":"2","scheduleCardVos":[{"id":"4fc24a3b63004e77a8c7b9d5753e8251","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"2","type":"上午","curriculum":"1"},{"id":"a586f50ca34c411690e282e0eb8b0501","scheduleCardId":null,"classId":null,"subject":"数学","uid":"1727","week":"2","type":"上午","curriculum":"2"},{"id":"d532abf2d1c54e02b949341632791baa","scheduleCardId":null,"classId":null,"subject":"物理","uid":"1727","week":"2","type":"上午","curriculum":"3"},{"id":"636dea0af06c499885d4e09a6c621400","scheduleCardId":null,"classId":null,"subject":"生物","uid":"1727","week":"2","type":"上午","curriculum":"4"}]},{"week":"3","scheduleCardVos":[{"id":"b17b7c9894ea47239464130571533e5b","scheduleCardId":null,"classId":null,"subject":"体育","uid":"1727","week":"3","type":"上午","curriculum":"1"},{"id":"54715d6a109d433fafd4f867959cb9a2","scheduleCardId":null,"classId":null,"subject":"音乐","uid":"1727","week":"3","type":"上午","curriculum":"2"},{"id":"baa48ddbb1114e9494a2e348c7ce24dd","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"3","type":"上午","curriculum":"3"},{"id":"820412e273504f25952fedbe48022ed2","scheduleCardId":null,"classId":null,"subject":"地理","uid":"1727","week":"3","type":"上午","curriculum":"4"}]},{"week":"4","scheduleCardVos":[{"id":"8e14e10e264d4a7e88fe8e6e87c80469","scheduleCardId":null,"classId":null,"subject":"美术","uid":"1727","week":"4","type":"上午","curriculum":"1"},{"id":"34105f9f249347e8877bfefac6b920e6","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"4","type":"上午","curriculum":"2"},{"id":"33e29d4ca9794c9ebe289a85136a3ec8","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"4","type":"上午","curriculum":"3"},{"id":"f923313c5f9c479da26d3183c4cfc1ba","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"4","type":"上午","curriculum":"4"}]},{"week":"5","scheduleCardVos":[{"id":"bde69eeabe4e48a7b4c275bfa744910b","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"5","type":"上午","curriculum":"1"},{"id":"73373354ef3b4817bccf82d3dfe9c60a","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"5","type":"上午","curriculum":"2"},{"id":"2d4c37083c4441b9bf0fd2894582ddcc","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"5","type":"上午","curriculum":"3"},{"id":"ae85bb8950924ceaba76713e5cda0885","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"5","type":"上午","curriculum":"4"}]}]
     * afternoonList : [{"week":"1","scheduleCardVos":[{"id":"6743de1e319c4bfc9c2bea299ae966db","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"1","type":"下午","curriculum":"5"}]},{"week":"2","scheduleCardVos":[{"id":"2ce0097a776f46d48c39cf2bbe2be00f","scheduleCardId":null,"classId":null,"subject":"历史","uid":"1727","week":"2","type":"下午","curriculum":"5"}]},{"week":"3","scheduleCardVos":[{"id":"460ba8631f0540338e5c5829f041a928","scheduleCardId":null,"classId":null,"subject":"化学","uid":"1727","week":"3","type":"下午","curriculum":"5"}]},{"week":"4","scheduleCardVos":[{"id":"a77a76bfb4ce4f8fb0e76fd9aa3c4d54","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"4","type":"下午","curriculum":"5"}]},{"week":"5","scheduleCardVos":[{"id":"549fae9cdf4147fda303ef2fb48a2918","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"5","type":"下午","curriculum":"5"}]}]
     */

    private String currentWeek;
    private String duties = "";
    private List<ScheduleListBean> scheduleList;

    public String getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(String currentWeek) {
        this.currentWeek = currentWeek;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public List<ScheduleListBean> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<ScheduleListBean> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public static class ScheduleListBean {
        /**
         * week : 1
         * scheduleCardVos : [{"id":"4e7556f662dc436ebfc2157aa0986e80","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"1","type":"上午","curriculum":"1"},{"id":"b257d7a7696944d382d70a5c0c98c620","scheduleCardId":null,"classId":null,"subject":"数学","uid":"1727","week":"1","type":"上午","curriculum":"2"},{"id":"77336833c0b345429a06d6546550cb5d","scheduleCardId":null,"classId":null,"subject":"英语","uid":"1727","week":"1","type":"上午","curriculum":"3"},{"id":"94c816bde9f946d0ae8a433a3a3950b3","scheduleCardId":null,"classId":null,"subject":"语文","uid":"1727","week":"1","type":"上午","curriculum":"4"}]
         */

        private String week;
        private List<ScheduleBean> scheduleCardVos;

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public List<ScheduleBean> getScheduleCardVos() {
            return scheduleCardVos;
        }

        public void setScheduleCardVos(List<ScheduleBean> scheduleCardVos) {
            this.scheduleCardVos = scheduleCardVos;
        }
    }

    public static class ScheduleBean {
        /**
         * id : 4e7556f662dc436ebfc2157aa0986e80
         * scheduleCardId : null
         * classId : null
         * subject : 语文
         * uid : 1727
         * week : 1
         * type : 上午
         * curriculum : 1
         */

        private String id;
        private String scheduleCardId;
        private String classId;
        private String subject;
        private String uid;
        private String week;
        private String type;
        private String curriculum;

        public ScheduleBean(){

        }

        public ScheduleBean(String week, String curriculum, String classId){
            this.week = week;
            this.curriculum = curriculum;
            this.classId = classId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getScheduleCardId() {
            return scheduleCardId;
        }

        public void setScheduleCardId(String scheduleCardId) {
            this.scheduleCardId = scheduleCardId;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCurriculum() {
            return curriculum;
        }

        public void setCurriculum(String curriculum) {
            this.curriculum = curriculum;
        }

        public ScheduleBean addClassId(String classId){
            this.classId = classId;
            return this;
        }
    }
}
