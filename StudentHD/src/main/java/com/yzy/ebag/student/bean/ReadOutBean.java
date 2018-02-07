package com.yzy.ebag.student.bean;

import java.util.List;

/**
 * @author caoyu
 * @date 2018/2/6
 * @description
 */

public class ReadOutBean {


    /**
     * total : 4
     * pageCount : 1
     * oralLanguageVos : [{"id":"0fe2b6b1da47478d878c5ca44b7af4fb","createBy":null,"craeteDate":null,"updateBy":null,"updateDate":null,"disabled":"N","removed":"N","page":null,"pageSize":null,"total":null,"unitCode":"5198","fileName":"U1 Let's enjoy 4","languageUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/OralLanguage/U1 Let's enjoy 4.mp4","coveUrl":null,"type":"video"},{"id":"ce7305b9dc30485cbb73b13fe1db9236","createBy":null,"craeteDate":null,"updateBy":null,"updateDate":null,"disabled":"N","removed":"N","page":null,"pageSize":null,"total":null,"unitCode":"5198","fileName":"U1 Let's play 3","languageUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/OralLanguage/U1 Let's play 3.mp4","coveUrl":null,"type":"video"},{"id":"e8603d7c4bef42d09d90b22ad0f270ba","createBy":null,"craeteDate":null,"updateBy":null,"updateDate":null,"disabled":"N","removed":"N","page":null,"pageSize":null,"total":null,"unitCode":"5198","fileName":"U1 let's talk 1","languageUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/OralLanguage/U1 let's talk 1.mp4","coveUrl":null,"type":"video"},{"id":"b0ca370a6ba7445f85bf3ea9ff00df04","createBy":null,"craeteDate":null,"updateBy":null,"updateDate":null,"disabled":"N","removed":"N","page":null,"pageSize":null,"total":null,"unitCode":"5198","fileName":"U1 A school day 2","languageUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/OralLanguage/U1 A school day 2.mp4","coveUrl":null,"type":"video"}]
     */

    private int total;
    private int pageCount;
    private List<OralLanguageBean> oralLanguageVos;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<OralLanguageBean> getOralLanguageVos() {
        return oralLanguageVos;
    }

    public void setOralLanguageVos(List<OralLanguageBean> oralLanguageVos) {
        this.oralLanguageVos = oralLanguageVos;
    }

    public static class OralLanguageBean {
        /**
         * id : 0fe2b6b1da47478d878c5ca44b7af4fb
         * createBy : null
         * craeteDate : null
         * updateBy : null
         * updateDate : null
         * disabled : N
         * removed : N
         * page : null
         * pageSize : null
         * total : null
         * unitCode : 5198
         * fileName : U1 Let's enjoy 4
         * languageUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/OralLanguage/U1 Let's enjoy 4.mp4
         * coveUrl : null
         * type : video
         */

        private String id;
        private Object createBy;
        private Object craeteDate;
        private Object updateBy;
        private Object updateDate;
        private String disabled;
        private String removed;
        private Object page;
        private Object pageSize;
        private Object total;
        private String unitCode;
        private String fileName;
        private String languageUrl;
        private String coveUrl;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Object createBy) {
            this.createBy = createBy;
        }

        public Object getCraeteDate() {
            return craeteDate;
        }

        public void setCraeteDate(Object craeteDate) {
            this.craeteDate = craeteDate;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public Object getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Object updateDate) {
            this.updateDate = updateDate;
        }

        public String getDisabled() {
            return disabled;
        }

        public void setDisabled(String disabled) {
            this.disabled = disabled;
        }

        public String getRemoved() {
            return removed;
        }

        public void setRemoved(String removed) {
            this.removed = removed;
        }

        public Object getPage() {
            return page;
        }

        public void setPage(Object page) {
            this.page = page;
        }

        public Object getPageSize() {
            return pageSize;
        }

        public void setPageSize(Object pageSize) {
            this.pageSize = pageSize;
        }

        public Object getTotal() {
            return total;
        }

        public void setTotal(Object total) {
            this.total = total;
        }

        public String getUnitCode() {
            return unitCode;
        }

        public void setUnitCode(String unitCode) {
            this.unitCode = unitCode;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getLanguageUrl() {
            return languageUrl;
        }

        public void setLanguageUrl(String languageUrl) {
            this.languageUrl = languageUrl;
        }

        public String getCoveUrl() {
            return coveUrl;
        }

        public void setCoveUrl(String coveUrl) {
            this.coveUrl = coveUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
