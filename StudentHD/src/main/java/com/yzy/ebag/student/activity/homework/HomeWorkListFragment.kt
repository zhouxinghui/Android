package com.yzy.ebag.student.activity.homework

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.yzy.ebag.student.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.xRecyclerView.adapter.RecyclerAdapter

/**
 * Created by unicho on 2018/1/8.
 */
class HomeWorkListFragment: BaseListFragment<String>(){
    override fun getBundle(bundle: Bundle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadConfig() {
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<String>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAdapter(): RecyclerAdapter<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    class HomeWorkListAdapter: RecyclerAdapter<>(){
//
//    }
}