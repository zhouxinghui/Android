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


        private int staus_0;
        private int staus_1;
        private int staus_2;

        public int getStaus_0() {
            return staus_0;
        }

        public void setStaus_0(int staus_0) {
            this.staus_0 = staus_0;
        }

        public int getStaus_1() {
            return staus_1;
        }

        public void setStaus_1(int staus_1) {
            this.staus_1 = staus_1;
        }

        public int getStaus_2() {
            return staus_2;
        }

        public void setStaus_2(int staus_2) {
            this.staus_2 = staus_2;
        }

        public int getStaus_3() {
            return staus_3;
        }

        public void setStaus_3(int staus_3) {
            this.staus_3 = staus_3;
        }

        private int staus_3;
    }

    public static class ResultOrderVosBean {
        /**
         * oid : 2018032018381541
         * allPrice : 11
         * status : 0
         * orderProductVOs : []
         * address
         * freight
         */

        private String oid;
        private int allPrice;
        private String status;
        private List<orderDetailsData> orderProductVOs;
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getFreight() {
            return freight;
        }

        public void setFreight(String freight) {
            this.freight = freight;
        }

        private String freight;

        public static class     orderDetailsData{

            private String shopName;
            private String price;
            private String discountPrice;
            private String shopImg;

            public String getDiscountPrice() {
                return discountPrice;
            }

            public void setDiscountPrice(String discountPrice) {
                this.discountPrice = discountPrice;
            }

            public String getShopImg() {
                return shopImg;
            }

            public void setShopImg(String shopImg) {
                this.shopImg = shopImg;
            }

            public String getYsbMoney() {
                return ysbMoney;
            }

            public void setYsbMoney(String ysbMoney) {
                this.ysbMoney = ysbMoney;
            }

            private String ysbMoney;

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
