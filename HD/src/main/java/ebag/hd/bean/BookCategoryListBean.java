package ebag.hd.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/3/9.
 */

public class BookCategoryListBean implements Serializable {
    /**
     * id : 38
     * toc : [{"l":"","t":"目录","type":"2","p":2},{"l":"1","t":"认识图形（二）","type":"2","p":5},{"l":"","t":"练习 一","type":0,"p":8},{"l":"2","t":"20以内的退位减法","type":"2","p":11},{"type":0,"p":14,"t":"练习 二"},{"l":"","t":"练习 三","type":0,"p":18},{"l":"","t":"练习 四","type":0,"p":21},{"type":0,"p":25,"t":"练习 五"},{"l":"","t":"整理与复习","type":0,"p":27},{"type":0,"p":28,"t":"练习 六"},{"l":"3","t":"分类与整理","type":"2","p":30},{"l":"","t":"练习 七","type":0,"p":32},{"l":"4","t":"100以内数的认识","type":"2","p":36},{"l":"","t":"练习 八","type":0,"p":41},{"l":"","t":"练习 九","type":0,"p":47},{"l":"","t":"练习  十","type":0,"p":50},{"l":"","t":"练习  十一","type":0,"p":52},{"l":"5","t":"认识人民币","type":"2","p":55},{"l":"","t":"练习  十二","type":0,"p":58},{"l":"","t":"练习  十三","type":0,"p":62},{"l":"6","t":"100以内的加法和减法（一）","type":"2","p":64},{"type":0,"p":65,"t":"整十数加、减整十数","l":"1"},{"l":"","t":"练习  十四","type":0,"p":66},{"l":"2","t":"两位数加一位数、整十数","type":0,"p":67},{"l":"","t":"练习  十五","type":0,"p":69},{"l":"3","t":"两位数减一位数、整十数","type":0,"p":72},{"l":"","t":"练习  十六","type":0,"p":74},{"l":"","t":"练习  十七","type":0,"p":78},{"l":"","t":"练习  十八","type":0,"p":82},{"l":"","t":"整理与复习","type":0,"p":84},{"l":"","t":"练习  十九","type":0,"p":85},{"l":"7","t":"找规律","type":"2","p":88},{"l":"","t":"练习  二十","type":0,"p":92},{"l":"8","t":"总复习","type":"2","p":95},{"l":"","t":"练习  二十一","type":0,"p":98}]
     */

    private String id;
    private List<TocBean> toc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TocBean> getToc() {
        return toc;
    }

    public void setToc(List<TocBean> toc) {
        this.toc = toc;
    }

    public static class TocBean implements Serializable {
        /**
         * l :
         * t : 目录
         * type : 2
         * p : 2
         */

        private String l;
        private String t;
        private String type;
        private int p;

        public String getL() {
            return l;
        }

        public void setL(String l) {
            this.l = l;
        }

        public String getT() {
            return t;
        }

        public void setT(String t) {
            this.t = t;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getP() {
            return p;
        }

        public void setP(int p) {
            this.p = p;
        }
    }
}
