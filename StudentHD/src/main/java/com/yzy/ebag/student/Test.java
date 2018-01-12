package com.yzy.ebag.student;

import android.support.v4.widget.SwipeRefreshLayout;

import ebag.core.base.BaseActivity;

/**
 * Created by unicho on 2018/1/11.
 */

public class Test extends BaseActivity{


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initViews() {
        SwipeRefreshLayout refreshLayout = new SwipeRefreshLayout(this);
    }
}
