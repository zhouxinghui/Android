package ebag.mobile.bean;

import java.util.List;

/**
 * Created by fansan on 2018/3/13.
 */

public class YBCurrentBean {
    /**
     * uid : 1592
     * remainMoney : 1700
     * increasedMoney : 500
     * reduceMoney : 2200
     * createDate : 1111-11-11
     * moneyDetails : [{"uid":null,"remark":"老师赠送获得200云币","money":200,"type":"1","accountName":"云币","accountType":"礼物","createDate":-27079776000000}]
     */

    private String uid;
    private int remainMoney;
    private int increasedMoney;
    private int reduceMoney;
    private String createDate;
    private List<MoneyDetailsBean> moneyDetails;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(int remainMoney) {
        this.remainMoney = remainMoney;
    }

    public int getIncreasedMoney() {
        return increasedMoney;
    }

    public void setIncreasedMoney(int increasedMoney) {
        this.increasedMoney = increasedMoney;
    }

    public int getReduceMoney() {
        return reduceMoney;
    }

    public void setReduceMoney(int reduceMoney) {
        this.reduceMoney = reduceMoney;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<MoneyDetailsBean> getMoneyDetails() {
        return moneyDetails;
    }

    public void setMoneyDetails(List<MoneyDetailsBean> moneyDetails) {
        this.moneyDetails = moneyDetails;
    }

    public static class MoneyDetailsBean {
        /**
         * uid : null
         * remark : 老师赠送获得200云币
         * money : 200
         * type : 1
         * accountName : 云币
         * accountType : 礼物
         * createDate : -27079776000000
         */

        private Object uid;
        private String remark;
        private int money;
        private String type;
        private String accountName;
        private String accountType;
        private long createDate;

        public Object getUid() {
            return uid;
        }

        public void setUid(Object uid) {
            this.uid = uid;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }
    }
}
