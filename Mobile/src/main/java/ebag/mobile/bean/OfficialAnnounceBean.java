package ebag.mobile.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/4/10.
 */

public class OfficialAnnounceBean implements Serializable {

    /**
     * total : 1
     * pages : 1
     * list : [{"id":0,"appName":null,"url":null,"mark":"老师版 Android HD 1.01","versionCode":null,"versionNumber":null,"type":null,"createDate":null,"createBy":null,"versionName":"2.3.1","page":null,"pageSize":null,"total":null,"isUpdate":null}]
     */

    private int total;
    private int pages;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 0
         * appName : null
         * url : null
         * mark : 老师版 Android HD 1.01
         * versionCode : null
         * versionNumber : null
         * type : null
         * createDate : null
         * createBy : null
         * versionName : 2.3.1
         * page : null
         * pageSize : null
         * total : null
         * isUpdate : null
         */

        private int id;
        private String appName;
        private String url;
        private String mark;
        private String versionCode;
        private String versionNumber;
        private String type;
        private String createDate;
        private String createBy;
        private String versionName;
        private String page;
        private String pageSize;
        private String total;
        private String isUpdate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(String versionNumber) {
            this.versionNumber = versionNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getIsUpdate() {
            return isUpdate;
        }

        public void setIsUpdate(String isUpdate) {
            this.isUpdate = isUpdate;
        }
    }
}
