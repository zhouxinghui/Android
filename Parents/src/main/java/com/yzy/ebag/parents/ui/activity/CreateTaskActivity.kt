package com.yzy.ebag.parents.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.bean.MyChildrenBean
import com.yzy.ebag.parents.mvp.CreateTaskContract
import com.yzy.ebag.parents.mvp.presenter.CreateTaskPersenter
import com.yzy.ebag.parents.ui.adapter.CreateTaskAdapter
import ebag.core.base.BaseActivity
import ebag.core.http.network.handleThrowable
import ebag.core.util.StringUtils
import ebag.core.util.T
import kotlinx.android.synthetic.main.activity_createtask.*


class CreateTaskActivity : BaseActivity(), CreateTaskContract.CreateTaskView {


    private var datas: ArrayList<MyChildrenBean> = arrayListOf()
    private lateinit var mAdapter: CreateTaskAdapter
    private lateinit var mPersenter: CreateTaskContract.Presenter
    private var selectedUid = ""
    override fun getLayoutId(): Int = R.layout.activity_createtask

    override fun initViews() {

        mAdapter = CreateTaskAdapter(datas)
        recyclerview.layoutManager = GridLayoutManager(this, 4)
        recyclerview.adapter = mAdapter
        mPersenter = CreateTaskPersenter(this)
        mPersenter.start()

        mAdapter.setOnItemClickListener { adapter, _, position ->

            if ((adapter.getItem(position) as MyChildrenBean).isSelected) {
                (adapter.getItem(position) as MyChildrenBean).isSelected = false
                selectedUid = ""
                adapter.notifyDataSetChanged()
            } else {
                (adapter.getItem(position) as MyChildrenBean).isSelected = true
                selectedUid = (adapter.getItem(position) as MyChildrenBean).uid
                adapter.notifyDataSetChanged()
            }
        }

        comfirm_btn.setOnClickListener {

            if (ebag.core.util.StringUtils.isEmpty(title_edit.text.toString())) {
                T.show(this, "请输入标题")
                return@setOnClickListener
            }

            if (ebag.core.util.StringUtils.isEmpty(target_edit.text.toString())) {
                T.show(this, "请输入目标内容")
                return@setOnClickListener
            }

            if (StringUtils.isEmpty(selectedUid)) {
                T.show(this, "请先选择小孩")
                return@setOnClickListener
            }

            mPersenter.createTask(title_edit.text.toString(), target_edit.text.toString(), selectedUid)
        }


    }


    override fun showLoading() {

    }

    override fun showEmpty() {

    }


    override fun <T> showContents(data: List<T>) {

        datas.addAll(data as ArrayList<MyChildrenBean>)
        mAdapter.notifyDataSetChanged()
    }

    override fun <T> showMoreComplete(data: List<T>) {

    }

    override fun loadmoreEnd() {

    }

    override fun loadmoreFail() {

    }

    override fun createSuccess() {
        setResult(999)
        finish()
    }

    override fun createFailed(e: Throwable?) {
        e!!.handleThrowable(this)
    }

    companion object {

        fun start(c: Activity) {
            c.startActivityForResult(Intent(c, CreateTaskActivity::class.java), 998)
        }
    }

    override fun showError(e: Throwable?) {
        super.showError(e)
        e!!.handleThrowable(this)
    }

}