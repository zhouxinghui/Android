package ebag.mobile.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YZY on 2018/3/19.
 */

public class LetterDescBean implements Serializable, MultiItemEntity {

    /**
     * Word : 诞,争,赌,驯,置,横,端,赢,束,固,黎,卢,标,州,始,摄,由,剪,差,引,异,图,制
     * NewWords : [{"page":null,"pageSize":null,"id":"3986706dc15541c1bdbdd531bee1ced4","createDate":1520221823000,"classId":null,"className":null,"unitId":null,"uid":null,"words":"郑,剑,锋","wordUrl":"www.yun-bag.com","score":null,"ysbCode":"1000857","name":"李静","timeLength":null}]
     */

    private String Word;
    private List<NewWordsBean> NewWords;

    public String getWord() {
        return Word;
    }

    public void setWord(String Word) {
        this.Word = Word;
    }

    public List<NewWordsBean> getNewWords() {
        return NewWords;
    }

    public void setNewWords(List<NewWordsBean> NewWords) {
        this.NewWords = NewWords;
    }

    @Override
    public int getItemType() {
        return 1;
    }

    public static class NewWordsBean implements Serializable, MultiItemEntity{
        /**
         * page : null
         * pageSize : null
         * id : 3986706dc15541c1bdbdd531bee1ced4
         * createDate : 1520221823000
         * classId : null
         * className : null
         * unitId : null
         * uid : null
         * words : 郑,剑,锋
         * wordUrl : www.yun-bag.com
         * score : null
         * ysbCode : 1000857
         * name : 李静
         * timeLength : null
         */

        private String page;
        private String pageSize;
        private String id;
        private long createDate;
        private String classId;
        private String className;
        private String unitId;
        private String uid;
        private String words;
        private String wordUrl;
        private String score;
        private String ysbCode;
        private String name;
        private String timeLength;
        private String headUrl;
        private boolean bscore;

        public boolean isBscore() {
            return bscore;
        }

        public void setBscore(boolean bscore) {
            this.bscore = bscore;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
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

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public String getWordUrl() {
            return wordUrl;
        }

        public void setWordUrl(String wordUrl) {
            this.wordUrl = wordUrl;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getYsbCode() {
            return ysbCode;
        }

        public void setYsbCode(String ysbCode) {
            this.ysbCode = ysbCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTimeLength() {
            return timeLength;
        }

        public void setTimeLength(String timeLength) {
            this.timeLength = timeLength;
        }

        @Override
        public int getItemType() {
            return 2;
        }
    }
}
