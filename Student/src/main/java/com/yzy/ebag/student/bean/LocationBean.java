package com.yzy.ebag.student.bean;

import java.util.List;

/**
 * Created by fansan on 2018/3/27.
 */

public class LocationBean {
    /**
     * pages : 10
     * total : 20
     * userPositioningVos : [{"id":"0547e5a556db4c83b06cc346cda6d9b6","uid":"1f6b853d9cc946ca8915345ec5e4706d","address":"dadfadsfasdf2qwaw","reportDate":1522080000000,"remark":"adfadfre","longitude":"164422334sdfa","latitude":"121342343dfa","page":0,"pageSize":0}]
     */

    private int pages;
    private int total;
    private List<UserPositioningVosBean> userPositioningVos;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<UserPositioningVosBean> getUserPositioningVos() {
        return userPositioningVos;
    }

    public void setUserPositioningVos(List<UserPositioningVosBean> userPositioningVos) {
        this.userPositioningVos = userPositioningVos;
    }

    public static class UserPositioningVosBean {
        /**
         * id : 0547e5a556db4c83b06cc346cda6d9b6
         * uid : 1f6b853d9cc946ca8915345ec5e4706d
         * address : dadfadsfasdf2qwaw
         * reportDate : 1522080000000
         * remark : adfadfre
         * longitude : 164422334sdfa
         * latitude : 121342343dfa
         * page : 0
         * pageSize : 0
         */

        private String id;
        private String uid;
        private String address;
        private long reportDate;
        private String remark;
        private String longitude;
        private String latitude;
        private int page;
        private int pageSize;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getReportDate() {
            return reportDate;
        }

        public void setReportDate(long reportDate) {
            this.reportDate = reportDate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }
}
