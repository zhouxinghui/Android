package ebag.hd.bean;

import java.util.List;

/**
 * Created by fansan on 2018/3/15.
 */

public class QueryOrderBean {


    /**
     * resultOrderVos : []
     * statusCount : {"0":32,"1":23,"2":23,"3":123}
     */

    private StatusCountBean statusCount;
    private List<?> resultOrderVos;

    public StatusCountBean getStatusCount() {
        return statusCount;
    }

    public void setStatusCount(StatusCountBean statusCount) {
        this.statusCount = statusCount;
    }

    public List<?> getResultOrderVos() {
        return resultOrderVos;
    }

    public void setResultOrderVos(List<?> resultOrderVos) {
        this.resultOrderVos = resultOrderVos;
    }

    public static class StatusCountBean {
        /**
         * 0 : 32
         * 1 : 23
         * 2 : 23
         * 3 : 123
         */

        private int status_0;
        private int status_1;
        private int status_2;
        private int status_3;

        public int getStatus_0() {
            return status_0;
        }

        public void setStatus_0(int status_0) {
            this.status_0 = status_0;
        }

        public int getStatus_1() {
            return status_1;
        }

        public void setStatus_1(int status_1) {
            this.status_1 = status_1;
        }

        public int getStatus_2() {
            return status_2;
        }

        public void setStatus_2(int status_2) {
            this.status_2 = status_2;
        }

        public int getStatus_3() {
            return status_3;
        }

        public void setStatus_3(int status_3) {
            this.status_3 = status_3;
        }
    }
}
