package com.yzy.ebag.teacher.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/1/11.
 */

public class AssignmentBean implements Serializable {

    /**
     * sendHomePageClazzInfoVos : [{"gradeCode":"5","gradeName":"五年级","homeClazzInfoVos":[{"gradeCode":null,"classId":"07d4c0d1b000425291a144d3ae983f8d","className":"501班"}],"bookVersionOrUnitVos":[{"id":150,"code":"1","name":"第一单元","bookVersionId":"362","pid":"0","unitCode":"1330","resultBookUnitOrCatalogVos":[{"id":2208,"code":"1","name":"窃读记","bookVersionId":"0","pid":"150","unitCode":"1338","resultBookUnitOrCatalogVos":[]},{"id":2209,"code":"2","name":"小苗与大树的对话","bookVersionId":"0","pid":"150","unitCode":"1339","resultBookUnitOrCatalogVos":[]},{"id":2210,"code":"3","name":"走遍天下书为侣","bookVersionId":"0","pid":"150","unitCode":"1340","resultBookUnitOrCatalogVos":[]},{"id":2211,"code":"4","name":"我的\u201c长生果\u201d","bookVersionId":"0","pid":"150","unitCode":"1341","resultBookUnitOrCatalogVos":[]}]},{"id":151,"code":"2","name":"第二单元","bookVersionId":"362","pid":"0","unitCode":"1331","resultBookUnitOrCatalogVos":[{"id":2212,"code":"5","name":"泊船瓜洲&秋思&长相思","bookVersionId":"0","pid":"151","unitCode":"1342","resultBookUnitOrCatalogVos":[]},{"id":2213,"code":"6","name":"梅花魂","bookVersionId":"0","pid":"151","unitCode":"1343","resultBookUnitOrCatalogVos":[]},{"id":2214,"code":"7","name":"桂花雨","bookVersionId":"0","pid":"151","unitCode":"1344","resultBookUnitOrCatalogVos":[]},{"id":2215,"code":"8","name":"小桥流水人家","bookVersionId":"0","pid":"151","unitCode":"1345","resultBookUnitOrCatalogVos":[]}]},{"id":152,"code":"3","name":"第三单元","bookVersionId":"362","pid":"0","unitCode":"1332","resultBookUnitOrCatalogVos":[{"id":2216,"code":"9","name":"鲸","bookVersionId":"0","pid":"152","unitCode":"1346","resultBookUnitOrCatalogVos":[]},{"id":2217,"code":"10","name":"松鼠","bookVersionId":"0","pid":"152","unitCode":"1347","resultBookUnitOrCatalogVos":[]},{"id":2218,"code":"11","name":"新型玻璃","bookVersionId":"0","pid":"152","unitCode":"1348","resultBookUnitOrCatalogVos":[]},{"id":2219,"code":"12","name":"假如没有灰尘","bookVersionId":"0","pid":"152","unitCode":"1349","resultBookUnitOrCatalogVos":[]}]},{"id":153,"code":"4","name":"第四单元","bookVersionId":"362","pid":"0","unitCode":"1333","resultBookUnitOrCatalogVos":[{"id":2220,"code":"13","name":"钓鱼的启示","bookVersionId":"0","pid":"153","unitCode":"1350","resultBookUnitOrCatalogVos":[]},{"id":2221,"code":"14","name":"通往广场的路不止一条","bookVersionId":"0","pid":"153","unitCode":"1351","resultBookUnitOrCatalogVos":[]},{"id":2222,"code":"15","name":"落花生","bookVersionId":"0","pid":"153","unitCode":"1352","resultBookUnitOrCatalogVos":[]},{"id":2223,"code":"16","name":"珍珠鸟","bookVersionId":"0","pid":"153","unitCode":"1353","resultBookUnitOrCatalogVos":[]}]},{"id":154,"code":"5","name":"第五单元","bookVersionId":"362","pid":"0","unitCode":"1334","resultBookUnitOrCatalogVos":[{"id":2224,"code":"17","name":"综合性学习：遨游汉字王国","bookVersionId":"0","pid":"154","unitCode":"1354","resultBookUnitOrCatalogVos":[]},{"id":2225,"code":"18","name":"有趣的汉字","bookVersionId":"0","pid":"154","unitCode":"1355","resultBookUnitOrCatalogVos":[]},{"id":2226,"code":"19","name":"我爱你，汉字","bookVersionId":"0","pid":"154","unitCode":"1356","resultBookUnitOrCatalogVos":[]}]},{"id":155,"code":"6","name":"第六单元","bookVersionId":"362","pid":"0","unitCode":"1335","resultBookUnitOrCatalogVos":[{"id":2227,"code":"20","name":"地震中的父与子","bookVersionId":"0","pid":"155","unitCode":"1357","resultBookUnitOrCatalogVos":[]},{"id":2228,"code":"21","name":"慈母情深","bookVersionId":"0","pid":"155","unitCode":"1358","resultBookUnitOrCatalogVos":[]},{"id":2229,"code":"22","name":"\u201c精彩极了\u201d和\u201c糟糕透了\u201d","bookVersionId":"0","pid":"155","unitCode":"1359","resultBookUnitOrCatalogVos":[]},{"id":2230,"code":"23","name":"学会看病","bookVersionId":"0","pid":"155","unitCode":"1360","resultBookUnitOrCatalogVos":[]}]},{"id":156,"code":"7","name":"第七单元","bookVersionId":"362","pid":"0","unitCode":"1336","resultBookUnitOrCatalogVos":[{"id":2231,"code":"24","name":"圆明园的毁灭","bookVersionId":"0","pid":"156","unitCode":"1361","resultBookUnitOrCatalogVos":[]},{"id":2232,"code":"25","name":"狼牙山五壮士","bookVersionId":"0","pid":"156","unitCode":"1362","resultBookUnitOrCatalogVos":[]},{"id":2233,"code":"26","name":"难忘的一课","bookVersionId":"0","pid":"156","unitCode":"1363","resultBookUnitOrCatalogVos":[]},{"id":2234,"code":"27","name":"最后一分钟","bookVersionId":"0","pid":"156","unitCode":"1364","resultBookUnitOrCatalogVos":[]}]},{"id":157,"code":"8","name":"第八单元","bookVersionId":"362","pid":"0","unitCode":"1337","resultBookUnitOrCatalogVos":[{"id":2235,"code":"28","name":"七律·长征","bookVersionId":"0","pid":"157","unitCode":"1365","resultBookUnitOrCatalogVos":[]},{"id":2236,"code":"29","name":"开国大典","bookVersionId":"0","pid":"157","unitCode":"1366","resultBookUnitOrCatalogVos":[]},{"id":2237,"code":"30","name":"青山处处埋忠骨","bookVersionId":"0","pid":"157","unitCode":"1367","resultBookUnitOrCatalogVos":[]},{"id":2238,"code":"31","name":"毛主席在花山","bookVersionId":"0","pid":"157","unitCode":"1368","resultBookUnitOrCatalogVos":[]}]}]}]
     * resultAdvertisementVos : [{"adverCode":"tk","adverName":"填空","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_tx.png"},{"adverCode":"dx","adverName":"选择题","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_xuanzeti.png"},{"adverCode":"pd","adverName":"判断题","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_pdt.png"},{"adverCode":"9","adverName":"连线","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_lxt.png"},{"adverCode":"10","adverName":"分类","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_fl.png"},{"adverCode":"tx","adverName":"听写","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_tx.png"},{"adverCode":"12","adverName":"朗读","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_ldzw.png"},{"adverCode":"16","adverName":"句子部分","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_czhjz.png"},{"adverCode":"yd","adverName":"阅读理解","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_ydlj.png"},{"adverCode":"5","adverName":"排列句子","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_pljz.png"},{"adverCode":"21","adverName":"书写","adverUrl":"http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_sx.png"}]
     * resultTaughtCoursesVo : {"classId":null,"bookVersionId":"362","bookVersionName":"人教版  ","bookCode":"yw","bookName":"语文","gradeCode":"5","gradeName":"五年级","semeterCode":"1","semeterName":"上学期"}
     */

    private ResultTaughtCoursesVoBean resultTaughtCoursesVo;
    private List<SendHomePageClazzInfoVosBean> sendHomePageClazzInfoVos;
    private List<ResultAdvertisementVosBean> resultAdvertisementVos;

    public ResultTaughtCoursesVoBean getResultTaughtCoursesVo() {
        return resultTaughtCoursesVo;
    }

    public void setResultTaughtCoursesVo(ResultTaughtCoursesVoBean resultTaughtCoursesVo) {
        this.resultTaughtCoursesVo = resultTaughtCoursesVo;
    }

    public List<SendHomePageClazzInfoVosBean> getSendHomePageClazzInfoVos() {
        return sendHomePageClazzInfoVos;
    }

    public void setSendHomePageClazzInfoVos(List<SendHomePageClazzInfoVosBean> sendHomePageClazzInfoVos) {
        this.sendHomePageClazzInfoVos = sendHomePageClazzInfoVos;
    }

    public List<ResultAdvertisementVosBean> getResultAdvertisementVos() {
        return resultAdvertisementVos;
    }

    public void setResultAdvertisementVos(List<ResultAdvertisementVosBean> resultAdvertisementVos) {
        this.resultAdvertisementVos = resultAdvertisementVos;
    }

    public static class ResultTaughtCoursesVoBean {
        /**
         * classId : null
         * bookVersionId : 362
         * bookVersionName : 人教版
         * bookCode : yw
         * bookName : 语文
         * gradeCode : 5
         * gradeName : 五年级
         * semeterCode : 1
         * semeterName : 上学期
         */

        private Object classId;
        private String bookVersionId;
        private String bookVersionName;
        private String bookCode;
        private String bookName;
        private String gradeCode;
        private String gradeName;
        private String semeterCode;
        private String semeterName;

        public Object getClassId() {
            return classId;
        }

        public void setClassId(Object classId) {
            this.classId = classId;
        }

        public String getBookVersionId() {
            return bookVersionId;
        }

        public void setBookVersionId(String bookVersionId) {
            this.bookVersionId = bookVersionId;
        }

        public String getBookVersionName() {
            return bookVersionName;
        }

        public void setBookVersionName(String bookVersionName) {
            this.bookVersionName = bookVersionName;
        }

        public String getBookCode() {
            return bookCode;
        }

        public void setBookCode(String bookCode) {
            this.bookCode = bookCode;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }

        public String getGradeCode() {
            return gradeCode;
        }

        public void setGradeCode(String gradeCode) {
            this.gradeCode = gradeCode;
        }

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public String getSemeterCode() {
            return semeterCode;
        }

        public void setSemeterCode(String semeterCode) {
            this.semeterCode = semeterCode;
        }

        public String getSemeterName() {
            return semeterName;
        }

        public void setSemeterName(String semeterName) {
            this.semeterName = semeterName;
        }
    }

    public static class SendHomePageClazzInfoVosBean {
        /**
         * gradeCode : 5
         * gradeName : 五年级
         * homeClazzInfoVos : [{"gradeCode":null,"classId":"07d4c0d1b000425291a144d3ae983f8d","className":"501班"}]
         * bookVersionOrUnitVos : [{"id":150,"code":"1","name":"第一单元","bookVersionId":"362","pid":"0","unitCode":"1330","resultBookUnitOrCatalogVos":[{"id":2208,"code":"1","name":"窃读记","bookVersionId":"0","pid":"150","unitCode":"1338","resultBookUnitOrCatalogVos":[]},{"id":2209,"code":"2","name":"小苗与大树的对话","bookVersionId":"0","pid":"150","unitCode":"1339","resultBookUnitOrCatalogVos":[]},{"id":2210,"code":"3","name":"走遍天下书为侣","bookVersionId":"0","pid":"150","unitCode":"1340","resultBookUnitOrCatalogVos":[]},{"id":2211,"code":"4","name":"我的\u201c长生果\u201d","bookVersionId":"0","pid":"150","unitCode":"1341","resultBookUnitOrCatalogVos":[]}]},{"id":151,"code":"2","name":"第二单元","bookVersionId":"362","pid":"0","unitCode":"1331","resultBookUnitOrCatalogVos":[{"id":2212,"code":"5","name":"泊船瓜洲&秋思&长相思","bookVersionId":"0","pid":"151","unitCode":"1342","resultBookUnitOrCatalogVos":[]},{"id":2213,"code":"6","name":"梅花魂","bookVersionId":"0","pid":"151","unitCode":"1343","resultBookUnitOrCatalogVos":[]},{"id":2214,"code":"7","name":"桂花雨","bookVersionId":"0","pid":"151","unitCode":"1344","resultBookUnitOrCatalogVos":[]},{"id":2215,"code":"8","name":"小桥流水人家","bookVersionId":"0","pid":"151","unitCode":"1345","resultBookUnitOrCatalogVos":[]}]},{"id":152,"code":"3","name":"第三单元","bookVersionId":"362","pid":"0","unitCode":"1332","resultBookUnitOrCatalogVos":[{"id":2216,"code":"9","name":"鲸","bookVersionId":"0","pid":"152","unitCode":"1346","resultBookUnitOrCatalogVos":[]},{"id":2217,"code":"10","name":"松鼠","bookVersionId":"0","pid":"152","unitCode":"1347","resultBookUnitOrCatalogVos":[]},{"id":2218,"code":"11","name":"新型玻璃","bookVersionId":"0","pid":"152","unitCode":"1348","resultBookUnitOrCatalogVos":[]},{"id":2219,"code":"12","name":"假如没有灰尘","bookVersionId":"0","pid":"152","unitCode":"1349","resultBookUnitOrCatalogVos":[]}]},{"id":153,"code":"4","name":"第四单元","bookVersionId":"362","pid":"0","unitCode":"1333","resultBookUnitOrCatalogVos":[{"id":2220,"code":"13","name":"钓鱼的启示","bookVersionId":"0","pid":"153","unitCode":"1350","resultBookUnitOrCatalogVos":[]},{"id":2221,"code":"14","name":"通往广场的路不止一条","bookVersionId":"0","pid":"153","unitCode":"1351","resultBookUnitOrCatalogVos":[]},{"id":2222,"code":"15","name":"落花生","bookVersionId":"0","pid":"153","unitCode":"1352","resultBookUnitOrCatalogVos":[]},{"id":2223,"code":"16","name":"珍珠鸟","bookVersionId":"0","pid":"153","unitCode":"1353","resultBookUnitOrCatalogVos":[]}]},{"id":154,"code":"5","name":"第五单元","bookVersionId":"362","pid":"0","unitCode":"1334","resultBookUnitOrCatalogVos":[{"id":2224,"code":"17","name":"综合性学习：遨游汉字王国","bookVersionId":"0","pid":"154","unitCode":"1354","resultBookUnitOrCatalogVos":[]},{"id":2225,"code":"18","name":"有趣的汉字","bookVersionId":"0","pid":"154","unitCode":"1355","resultBookUnitOrCatalogVos":[]},{"id":2226,"code":"19","name":"我爱你，汉字","bookVersionId":"0","pid":"154","unitCode":"1356","resultBookUnitOrCatalogVos":[]}]},{"id":155,"code":"6","name":"第六单元","bookVersionId":"362","pid":"0","unitCode":"1335","resultBookUnitOrCatalogVos":[{"id":2227,"code":"20","name":"地震中的父与子","bookVersionId":"0","pid":"155","unitCode":"1357","resultBookUnitOrCatalogVos":[]},{"id":2228,"code":"21","name":"慈母情深","bookVersionId":"0","pid":"155","unitCode":"1358","resultBookUnitOrCatalogVos":[]},{"id":2229,"code":"22","name":"\u201c精彩极了\u201d和\u201c糟糕透了\u201d","bookVersionId":"0","pid":"155","unitCode":"1359","resultBookUnitOrCatalogVos":[]},{"id":2230,"code":"23","name":"学会看病","bookVersionId":"0","pid":"155","unitCode":"1360","resultBookUnitOrCatalogVos":[]}]},{"id":156,"code":"7","name":"第七单元","bookVersionId":"362","pid":"0","unitCode":"1336","resultBookUnitOrCatalogVos":[{"id":2231,"code":"24","name":"圆明园的毁灭","bookVersionId":"0","pid":"156","unitCode":"1361","resultBookUnitOrCatalogVos":[]},{"id":2232,"code":"25","name":"狼牙山五壮士","bookVersionId":"0","pid":"156","unitCode":"1362","resultBookUnitOrCatalogVos":[]},{"id":2233,"code":"26","name":"难忘的一课","bookVersionId":"0","pid":"156","unitCode":"1363","resultBookUnitOrCatalogVos":[]},{"id":2234,"code":"27","name":"最后一分钟","bookVersionId":"0","pid":"156","unitCode":"1364","resultBookUnitOrCatalogVos":[]}]},{"id":157,"code":"8","name":"第八单元","bookVersionId":"362","pid":"0","unitCode":"1337","resultBookUnitOrCatalogVos":[{"id":2235,"code":"28","name":"七律·长征","bookVersionId":"0","pid":"157","unitCode":"1365","resultBookUnitOrCatalogVos":[]},{"id":2236,"code":"29","name":"开国大典","bookVersionId":"0","pid":"157","unitCode":"1366","resultBookUnitOrCatalogVos":[]},{"id":2237,"code":"30","name":"青山处处埋忠骨","bookVersionId":"0","pid":"157","unitCode":"1367","resultBookUnitOrCatalogVos":[]},{"id":2238,"code":"31","name":"毛主席在花山","bookVersionId":"0","pid":"157","unitCode":"1368","resultBookUnitOrCatalogVos":[]}]}]
         */

        private String gradeCode;
        private String gradeName;
        private List<HomeClazzInfoVosBean> homeClazzInfoVos;
        private List<BookVersionOrUnitVosBean> bookVersionOrUnitVos;

        public String getGradeCode() {
            return gradeCode;
        }

        public void setGradeCode(String gradeCode) {
            this.gradeCode = gradeCode;
        }

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public List<HomeClazzInfoVosBean> getHomeClazzInfoVos() {
            return homeClazzInfoVos;
        }

        public void setHomeClazzInfoVos(List<HomeClazzInfoVosBean> homeClazzInfoVos) {
            this.homeClazzInfoVos = homeClazzInfoVos;
        }

        public List<BookVersionOrUnitVosBean> getBookVersionOrUnitVos() {
            return bookVersionOrUnitVos;
        }

        public void setBookVersionOrUnitVos(List<BookVersionOrUnitVosBean> bookVersionOrUnitVos) {
            this.bookVersionOrUnitVos = bookVersionOrUnitVos;
        }

        public static class HomeClazzInfoVosBean {
            /**
             * gradeCode : null
             * classId : 07d4c0d1b000425291a144d3ae983f8d
             * className : 501班
             */

            private Object gradeCode;
            private String classId;
            private String className;

            public Object getGradeCode() {
                return gradeCode;
            }

            public void setGradeCode(Object gradeCode) {
                this.gradeCode = gradeCode;
            }

            public String getClassId() {
                return classId;
            }

            public void setClassId(String classId) {
                this.classId = classId;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }
        }
    }

    public static class ResultAdvertisementVosBean {
        /**
         * adverCode : tk
         * adverName : 填空
         * adverUrl : http://ebag-public-resource.oss-cn-shenzhen.aliyuncs.com/adver/yw/icon_tx.png
         */

        private String adverCode;
        private String adverName;
        private String adverUrl;

        public String getAdverCode() {
            return adverCode;
        }

        public void setAdverCode(String adverCode) {
            this.adverCode = adverCode;
        }

        public String getAdverName() {
            return adverName;
        }

        public void setAdverName(String adverName) {
            this.adverName = adverName;
        }

        public String getAdverUrl() {
            return adverUrl;
        }

        public void setAdverUrl(String adverUrl) {
            this.adverUrl = adverUrl;
        }
    }
}
