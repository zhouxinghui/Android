package com.yzy.ebag.teacher.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/12.
 */

public class BookNoteBean implements Serializable {

    /**
     * id : 14
     * bookId : 38
     * note : 今天老师说，提前放学让我们回家，不知道是不是骗我们的。
     */

    private String id;
    private String bookId;
    private String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
