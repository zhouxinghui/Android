package com.yzy.ebag.student.kdxftest;

import java.io.Serializable;
import java.util.List;

/**
 * 2018年4月27日 10:22:31
 * wy
 * 科大讯飞语音返回评测Vo
 **/
public class SpeechEvaluatorVo implements Serializable {
    /**
     * xml_result : {"read_sentence":{"lan":"en","type":"study","version":"6.5.0.1011","rec_paper":{"read_sentence":{"beg_pos":0,"content":"Sit down.","end_pos":48,"except_info":28676,"is_rejected":true,"total_score":2.5,"word_count":2,"sentence":{"beg_pos":0,"content":"sit down","end_pos":48,"index":0,"total_score":2.5,"word_count":2,"word":[{"beg_pos":0,"content":"sit","dp_message":16,"end_pos":0,"global_index":0,"index":0,"total_score":0},{"beg_pos":0,"content":"down","dp_message":16,"end_pos":0,"global_index":1,"index":1,"total_score":0}]}}}}}
     */

    private XmlResultBean xml_result;

    public XmlResultBean getXml_result() {
        return xml_result;
    }

    public void setXml_result(XmlResultBean xml_result) {
        this.xml_result = xml_result;
    }

    public static class XmlResultBean {
        /**
         * read_sentence : {"lan":"en","type":"study","version":"6.5.0.1011","rec_paper":{"read_sentence":{"beg_pos":0,"content":"Sit down.","end_pos":48,"except_info":28676,"is_rejected":true,"total_score":2.5,"word_count":2,"sentence":{"beg_pos":0,"content":"sit down","end_pos":48,"index":0,"total_score":2.5,"word_count":2,"word":[{"beg_pos":0,"content":"sit","dp_message":16,"end_pos":0,"global_index":0,"index":0,"total_score":0},{"beg_pos":0,"content":"down","dp_message":16,"end_pos":0,"global_index":1,"index":1,"total_score":0}]}}}}
         */

        private ReadSentenceBean read_sentence;

        public ReadSentenceBean getRead_sentence() {
            return read_sentence;
        }

        public void setRead_sentence(ReadSentenceBean read_sentence) {
            this.read_sentence = read_sentence;
        }

        public static class ReadSentenceBean {
            /**
             * lan : en
             * type : study
             * version : 6.5.0.1011
             * rec_paper : {"read_sentence":{"beg_pos":0,"content":"Sit down.","end_pos":48,"except_info":28676,"is_rejected":true,"total_score":2.5,"word_count":2,"sentence":{"beg_pos":0,"content":"sit down","end_pos":48,"index":0,"total_score":2.5,"word_count":2,"word":[{"beg_pos":0,"content":"sit","dp_message":16,"end_pos":0,"global_index":0,"index":0,"total_score":0},{"beg_pos":0,"content":"down","dp_message":16,"end_pos":0,"global_index":1,"index":1,"total_score":0}]}}}
             */

            private String lan;
            private String type;
            private String version;
            private RecPaperBean rec_paper;

            public String getLan() {
                return lan;
            }

            public void setLan(String lan) {
                this.lan = lan;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public RecPaperBean getRec_paper() {
                return rec_paper;
            }

            public void setRec_paper(RecPaperBean rec_paper) {
                this.rec_paper = rec_paper;
            }

            public static class RecPaperBean {
                /**
                 * read_sentence : {"beg_pos":0,"content":"Sit down.","end_pos":48,"except_info":28676,"is_rejected":true,"total_score":2.5,"word_count":2,"sentence":{"beg_pos":0,"content":"sit down","end_pos":48,"index":0,"total_score":2.5,"word_count":2,"word":[{"beg_pos":0,"content":"sit","dp_message":16,"end_pos":0,"global_index":0,"index":0,"total_score":0},{"beg_pos":0,"content":"down","dp_message":16,"end_pos":0,"global_index":1,"index":1,"total_score":0}]}}
                 */

                private ReadChapterBean read_chapter;

                public ReadChapterBean getRead_chapter() {
                    return read_chapter;
                }

                public void setRead_chapter(ReadChapterBean read_chapter) {
                    this.read_chapter = read_chapter;
                }

                public static class ReadChapterBean {
                    /**
                     * beg_pos : 0
                     * content : Sit down.
                     * end_pos : 48
                     * except_info : 28676
                     * is_rejected : true
                     * total_score : 2.5
                     * word_count : 2
                     * sentence : {"beg_pos":0,"content":"sit down","end_pos":48,"index":0,"total_score":2.5,"word_count":2,"word":[{"beg_pos":0,"content":"sit","dp_message":16,"end_pos":0,"global_index":0,"index":0,"total_score":0},{"beg_pos":0,"content":"down","dp_message":16,"end_pos":0,"global_index":1,"index":1,"total_score":0}]}
                     */

                    private int beg_pos;
                    private String content;
                    private int end_pos;
                    private int except_info;
                    private boolean is_rejected;
                    private double total_score;
                    private int word_count;
                    private SentenceBean sentence;

                    public int getBeg_pos() {
                        return beg_pos;
                    }

                    public void setBeg_pos(int beg_pos) {
                        this.beg_pos = beg_pos;
                    }

                    public String getContent() {
                        return content;
                    }

                    public void setContent(String content) {
                        this.content = content;
                    }

                    public int getEnd_pos() {
                        return end_pos;
                    }

                    public void setEnd_pos(int end_pos) {
                        this.end_pos = end_pos;
                    }

                    public int getExcept_info() {
                        return except_info;
                    }

                    public void setExcept_info(int except_info) {
                        this.except_info = except_info;
                    }

                    public boolean isIs_rejected() {
                        return is_rejected;
                    }

                    public void setIs_rejected(boolean is_rejected) {
                        this.is_rejected = is_rejected;
                    }

                    public double getTotal_score() {
                        return total_score;
                    }

                    public void setTotal_score(double total_score) {
                        this.total_score = total_score;
                    }

                    public int getWord_count() {
                        return word_count;
                    }

                    public void setWord_count(int word_count) {
                        this.word_count = word_count;
                    }

                    public SentenceBean getSentence() {
                        return sentence;
                    }

                    public void setSentence(SentenceBean sentence) {
                        this.sentence = sentence;
                    }

                    public static class SentenceBean {
                        /**
                         * beg_pos : 0
                         * content : sit down
                         * end_pos : 48
                         * index : 0
                         * total_score : 2.5
                         * word_count : 2
                         * word : [{"beg_pos":0,"content":"sit","dp_message":16,"end_pos":0,"global_index":0,"index":0,"total_score":0},{"beg_pos":0,"content":"down","dp_message":16,"end_pos":0,"global_index":1,"index":1,"total_score":0}]
                         */

                        private int beg_pos;
                        private String content;
                        private int end_pos;
                        private int index;
                        private double total_score;
                        private int word_count;
                        private List<WordBean> word;

                        public int getBeg_pos() {
                            return beg_pos;
                        }

                        public void setBeg_pos(int beg_pos) {
                            this.beg_pos = beg_pos;
                        }

                        public String getContent() {
                            return content;
                        }

                        public void setContent(String content) {
                            this.content = content;
                        }

                        public int getEnd_pos() {
                            return end_pos;
                        }

                        public void setEnd_pos(int end_pos) {
                            this.end_pos = end_pos;
                        }

                        public int getIndex() {
                            return index;
                        }

                        public void setIndex(int index) {
                            this.index = index;
                        }

                        public double getTotal_score() {
                            return total_score;
                        }

                        public void setTotal_score(double total_score) {
                            this.total_score = total_score;
                        }

                        public int getWord_count() {
                            return word_count;
                        }

                        public void setWord_count(int word_count) {
                            this.word_count = word_count;
                        }

                        public List<WordBean> getWord() {
                            return word;
                        }

                        public void setWord(List<WordBean> word) {
                            this.word = word;
                        }

                        public static class WordBean {
                            /**
                             * beg_pos : 0
                             * content : sit
                             * dp_message : 16
                             * end_pos : 0
                             * global_index : 0
                             * index : 0
                             * total_score : 0
                             */

                            private int beg_pos;
                            private String content;
                            private int dp_message;
                            private int end_pos;
                            private int global_index;
                            private String index;
                            private double total_score;

                            public int getBeg_pos() {
                                return beg_pos;
                            }

                            public void setBeg_pos(int beg_pos) {
                                this.beg_pos = beg_pos;
                            }

                            public String getContent() {
                                return content;
                            }

                            public void setContent(String content) {
                                this.content = content;
                            }

                            public int getDp_message() {
                                return dp_message;
                            }

                            public void setDp_message(int dp_message) {
                                this.dp_message = dp_message;
                            }

                            public int getEnd_pos() {
                                return end_pos;
                            }

                            public void setEnd_pos(int end_pos) {
                                this.end_pos = end_pos;
                            }

                            public int getGlobal_index() {
                                return global_index;
                            }

                            public void setGlobal_index(int global_index) {
                                this.global_index = global_index;
                            }

                            public String getIndex() {
                                return index;
                            }

                            public void setIndex(String index) {
                                this.index = index;
                            }

                            public double getTotal_score() {
                                return total_score;
                            }

                            public void setTotal_score(double total_score) {
                                this.total_score = total_score;
                            }
                        }
                    }
                }
            }
        }
    }
}
