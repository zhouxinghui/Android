package ebag.hd.bean;

import java.io.Serializable;

/**
 * Created by YZY on 2018/3/9.
 */

public class BookCategoryBean implements Serializable {
    private String bookChapter;

    public String getBookChapter() {
        return bookChapter;
    }

    public void setBookChapter(String bookChapter) {
        this.bookChapter = bookChapter;
    }
}
