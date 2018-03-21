package ebag.hd.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fansan on 2018/3/15.
 */

public class QueryOrderBean {


    /**
     * resultOrderVos : [{"oid":"2018032018381541","allPrice":11,"status":"0","orderProductVOs":[]},{"oid":"2018032018430043","allPrice":11,"status":"0","orderProductVOs":[]},{"oid":"2018032019091544","allPrice":11,"status":"0","orderProductVOs":[]},{"oid":"2018032019230546","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0},{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032110043648","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032110095149","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032110110150","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032110425653","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032114282854","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032114290755","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032114293256","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032114362457","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032114505660","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032114535661","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032114541862","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032116060165","allPrice":11,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]},{"oid":"2018032116241066","allPrice":1500,"status":"0","orderProductVOs":[{"shopName":null,"price":null,"numbers":0}]}]
     * statusCount : {"0":17,"1":0,"2":0,"3":0}
     */

    private StatusCountBean statusCount;
    private List<ResultOrderVosBean> resultOrderVos;

    public StatusCountBean getStatusCount() {
        return statusCount;
    }

    public void setStatusCount(StatusCountBean statusCount) {
        this.statusCount = statusCount;
    }

    public List<ResultOrderVosBean> getResultOrderVos() {
        return resultOrderVos;
    }

    public void setResultOrderVos(List<ResultOrderVosBean> resultOrderVos) {
        this.resultOrderVos = resultOrderVos;
    }

    public static class StatusCountBean {
        /**
         * 0 : 17
         * 1 : 0
         * 2 : 0
         * 3 : 0
         */

        @SerializedName("0")
        private int status_0;
        @SerializedName("1")
        private int status_1;
        @SerializedName("2")
        private int status_2;
        @SerializedName("3")
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

    public static class ResultOrderVosBean {
        /**
         * oid : 2018032018381541
         * allPrice : 11
         * status : 0
         * orderProductVOs : []
         */

        private String oid;
        private int allPrice;
        private String status;
        private List<orderDetailsData> orderProductVOs;

        public static class orderDetailsData{

            private String shopName;
            private String price;

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getNumbers() {
                return numbers;
            }

            public void setNumbers(String numbers) {
                this.numbers = numbers;
            }

            private String numbers;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public int getAllPrice() {
            return allPrice;
        }

        public void setAllPrice(int allPrice) {
            this.allPrice = allPrice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<orderDetailsData> getOrderProductVOs() {
            return orderProductVOs;
        }

        public void setOrderProductVOs(List<orderDetailsData> orderProductVOs) {
            this.orderProductVOs = orderProductVOs;
        }
    }
}
