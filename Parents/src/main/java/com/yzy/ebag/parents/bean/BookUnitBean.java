package com.yzy.ebag.parents.bean;

import java.util.List;

/**
 * Created by fansan on 2018/5/16 15:46
 */

public class BookUnitBean {


    /**
     * id : 39
     * code : 1
     * name : 第一单元
     * bookVersionId : 93
     * pid : 0
     * unitCode : 1059
     * resultBookUnitOrCatalogVos : [{"id":4096,"code":"1","name":"古诗二首","bookVersionId":"0","pid":"39","unitCode":"1067","resultBookUnitOrCatalogVos":[]},{"id":4097,"code":"2","name":"找春天","bookVersionId":"0","pid":"39","unitCode":"1068","resultBookUnitOrCatalogVos":[]},{"id":4098,"code":"3","name":"开满鲜花的小路","bookVersionId":"0","pid":"39","unitCode":"1069","resultBookUnitOrCatalogVos":[]},{"id":4099,"code":"4","name":"邓小平爷爷植树","bookVersionId":"0","pid":"39","unitCode":"1070","resultBookUnitOrCatalogVos":[]},{"id":9419,"code":"5","name":"口语交际：注意说话的语气","bookVersionId":"0","pid":"39","unitCode":"9229","resultBookUnitOrCatalogVos":[]},{"id":9420,"code":"6","name":"语文园地一 ","bookVersionId":"0","pid":"39","unitCode":"9230","resultBookUnitOrCatalogVos":[]},{"id":9421,"code":"7","name":"快乐读书吧","bookVersionId":"0","pid":"39","unitCode":"9231","resultBookUnitOrCatalogVos":[]}]
     */

    private int id;
    private String code;
    private String name;
    private String bookVersionId;
    private String pid;
    private String unitCode;
    private List<ResultBookUnitOrCatalogVosBean> resultBookUnitOrCatalogVos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookVersionId() {
        return bookVersionId;
    }

    public void setBookVersionId(String bookVersionId) {
        this.bookVersionId = bookVersionId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public List<ResultBookUnitOrCatalogVosBean> getResultBookUnitOrCatalogVos() {
        return resultBookUnitOrCatalogVos;
    }

    public void setResultBookUnitOrCatalogVos(List<ResultBookUnitOrCatalogVosBean> resultBookUnitOrCatalogVos) {
        this.resultBookUnitOrCatalogVos = resultBookUnitOrCatalogVos;
    }

    public static class ResultBookUnitOrCatalogVosBean {
        /**
         * id : 4096
         * code : 1
         * name : 古诗二首
         * bookVersionId : 0
         * pid : 39
         * unitCode : 1067
         * resultBookUnitOrCatalogVos : []
         */

        private int id;
        private String code;
        private String name;
        private String bookVersionId;
        private String pid;
        private String unitCode;
        private List<?> resultBookUnitOrCatalogVos;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBookVersionId() {
            return bookVersionId;
        }

        public void setBookVersionId(String bookVersionId) {
            this.bookVersionId = bookVersionId;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getUnitCode() {
            return unitCode;
        }

        public void setUnitCode(String unitCode) {
            this.unitCode = unitCode;
        }

        public List<?> getResultBookUnitOrCatalogVos() {
            return resultBookUnitOrCatalogVos;
        }

        public void setResultBookUnitOrCatalogVos(List<?> resultBookUnitOrCatalogVos) {
            this.resultBookUnitOrCatalogVos = resultBookUnitOrCatalogVos;
        }
    }
}
