package ebag.hd.http.baseBean;

import java.io.Serializable;

/**
 * Created by caoyu on 2017/8/28.
 */

public class ResponseEntity implements Serializable {


    /**
     * pageSize : 0
     * page : 0
     * id : 3036
     * disabled : N
     * removed : N
     * subjectType : yw
     * questionType : tk
     * questionHead :
     * questionContent : 阅(yuè) 读(dú) 欢(huān) 乐(lè) 岛(dǎo)。#R##F##R#两条小鱼#R##F##R# 两条向往大海的小鱼，历尽了千辛万苦，终于从小溪来到了大海。#R##F##R# 大海波涛汹涌，两条小鱼一下子被冲散了。过了很久，它们又相遇了，彼此谈起了感想：#R##F##R# “大海虽然浩瀚广阔，令人大开眼界，但它处处有凶险，我想回到小溪去。”一条小鱼说。#R##F##R# “大海虽然处处有凶险，但它浩瀚广阔，令我大开眼界。我愿意留在这里生活。”另一条小鱼说。#R##F##R#1．本(běn) 文(wén) 有(yǒu)#R##E##R#个(gè) 自(zì) 然(rán) 段(duàn)。#R##F##R#2．补(bǔ) 充(chōng) 词(cí) 语(yǔ)。#R##F##R# #R##E##R#辛(xīn)#R##E##R#苦(kǔ)#R##F##R# #R##E##R#涛(tāo) 汹(xiōng) 涌(yǒng)#R##F##R# #R##E##R##R##E##R#眼(yǎn) 界(jiè)#R##F##R#3．判(pàn) 断(duàn) 对(duì) 错(cuò)，对(duì) 的(de) 画(huà)“√”， 错(cuò) 的(de) 画(huà)“×”。#R##F##R#（1）两(liǎng) 条(tiáo) 小(xiǎo) 鱼(yú) 都(dōu) 想(xiǎng) 回(huí) 到(dào) 小(xiǎo) 溪(xī) 里(lǐ)。 #R##E##R##R##F##R#（2）一(yì) 条(tiáo) 小(xiǎo) 鱼(yú) 非(fēi) 常(cháng) 勇(yǒng) 敢(gǎn)，它(tā) 不(bù) 怕(pà) 大(dà) 海(hǎi) 处(chù) 处(chù) 有(yǒu) 凶(xiōng) 险(xiǎn)，喜(xǐ) 欢(huan) 生(shēng) 活(huó) 在(zài) 大(dà) 海(hǎi) 里(lǐ)。 #R##E##R##R##F##R##R##F##R#
     * rightAnswer : 4#R#千#R#万#R#波#R#大#R#开#R#×#R#√
     * analytical : 1．42．千&nbsp; 万&nbsp; 波&nbsp; 大&nbsp; 开3．（1）×&nbsp; （2）√&nbsp;
     * author : 0
     * answer : 三#R#千#R#万#R#波#R#大#R#开#R##R#生活
     * count : 0
     * correctionAnswer : 4#R#千#R#万#R#波#R#大#R#开#R#×#R#√
     */

    private int pageSize;
    private int page;
    private int id;
    private String disabled;
    private String removed;
    private String subjectType;
    private String questionType;
    private String questionHead;
    private String questionContent;
    private String rightAnswer;
    private String analytical;
    private int author;
    private String answer;
    private int count;
    private String correctionAnswer;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionHead() {
        return questionHead;
    }

    public void setQuestionHead(String questionHead) {
        this.questionHead = questionHead;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getAnalytical() {
        return analytical;
    }

    public void setAnalytical(String analytical) {
        this.analytical = analytical;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCorrectionAnswer() {
        return correctionAnswer;
    }

    public void setCorrectionAnswer(String correctionAnswer) {
        this.correctionAnswer = correctionAnswer;
    }
}
