package com.yzy.ebag.student.bean.response;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by unicho on 2018/1/14.
 */

public class LetterBean implements MultiItemEntity{

    private int itemType;
    private String letters;
    private String content;
    private String mp3;

    @Override
    public int getItemType() {
        return 0;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
