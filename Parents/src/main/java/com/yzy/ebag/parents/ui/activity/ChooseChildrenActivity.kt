package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.common.Constants
import com.yzy.ebag.parents.http.ParentsAPI
import com.yzy.ebag.parents.mvp.ChooseChildrenContract
import com.yzy.ebag.parents.mvp.presenter.ChooseChildrenPersenter
import com.yzy.ebag.parents.ui.adapter.ChooseChildrenAdapter
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SPUtils
import ebag.core.util.SerializableUtils
import ebag.core.util.T
import ebag.mobile.bean.MyChildrenBean
import kotlinx.android.synthetic.main.activity_choosechildren.*


@Suppress("UNCHECKED_CAST")
class ChooseChildrenActivity : BaseActivity(), ChooseChildrenContract.ChooseChildrenView {


    private val mPersenter: ChooseChildrenPersenter by lazy { ChooseChildrenPersenter(this, this) }
    private lateinit var mAdapter: ChooseChildrenAdapter
    private var mData: MutableList<MyChildrenBean> = mutableListOf()
    override fun getLayoutId(): Int = R.layout.activity_choosechildren
    private lateinit var oldUid: String
    private var flag = false


    override fun initViews() {

        oldUid = SPUtils.get(this, Constants.CURRENT_CHILDREN_YSBCODE, "") as String
        flag = intent.getBooleanExtra("flag", false)
        if (flag) {
            titlebar.setLeftBtnVisible(false)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#cccccc")))
        stateview.setOnRetryClickListener { mPersenter.start() }
        mAdapter = ChooseChildrenAdapter(mData)
        mAdapter.uid = SPUtils.get(this, Constants.CURRENT_CHILDREN_YSBCODE, "") as String
        mAdapter.setHasStableIds(true)
        recyclerView.adapter = mAdapter
        mPersenter.start()

        mAdapter.setOnItemClickListener { _, view, position ->
            if (view.findViewById<View>(R.id.child_select).visibility == View.GONE) {
                SerializableUtils.setSerializable(ebag.mobile.base.Constants.CHILD_USER_ENTITY, mAdapter.getItem(position))
                SPUtils.put(this, Constants.CURRENT_CHILDREN_YSBCODE, mAdapter.getItem(position)!!.uid)
                mAdapter.uid = SPUtils.get(this, Constants.CURRENT_CHILDREN_YSBCODE, "") as String
                mAdapter.notifyItemRangeChanged(0,mData.size)
            }

            if (flag) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        stateview.setOnRetryClickListener {
            mPersenter.start()
        }

        create_ysbcode.setOnClickListener {

            if (et_childname.text.toString().isEmpty()) {
                T.show(this@ChooseChildrenActivity, "请输入孩子的名字")
                return@setOnClickListener
            }

            if (et_psw.text.toString().isEmpty()) {
                T.show(this@ChooseChildrenActivity, "请输入密码")
                return@setOnClickListener
            }

            if (et_psw.text.toString().length < 6) {
                T.show(this@ChooseChildrenActivity, "密码太短,请重新输入")
                return@setOnClickListener
            }

            if (et_relation.text.toString().isEmpty()) {
                T.show(this@ChooseChildrenActivity, "请输入和小孩的关系")
                return@setOnClickListener
            }

            ParentsAPI.createChildCode(et_psw.text.toString(), et_childname.text.toString(), object : RequestCallBack<String>() {

                override fun onStart() {
                    LoadingDialogUtil.showLoading(this@ChooseChildrenActivity, "正在请求")
                }

                override fun onSuccess(entity: String?) {
                    mPersenter.refresh()
                    et_psw.setText("")
                    et_childname.setText("")
                    et_relation.setText("")
                    LoadingDialogUtil.closeLoadingDialog()
                }

                override fun onError(exception: Throwable) {
                    exception.handleThrowable(this@ChooseChildrenActivity)
                    LoadingDialogUtil.closeLoadingDialog()
                }

            }, et_relation.text.toString())
        }

        titlebar.setOnLeftClickListener {
            backEvent()
            finish()
        }

    }

    override fun onBackPressed() {
        backEvent()
        super.onBackPressed()
    }

    private fun backEvent() {
        if (oldUid != mAdapter.uid) {
            setResult(999)
        }
    }

    override fun showLoading() {
        stateview.showLoading()
    }

    override fun showEmpty() {
        stateview.showEmpty()
    }

    override fun showError(e: Throwable?) {
        stateview.showError()
    }

    override fun <T> showContents(data: List<T>) {
        mData.addAll(data as MutableList<MyChildrenBean>)
        mAdapter.notifyDataSetChanged()
        stateview.showContent()
    }

    override fun <T> showContent(data: T?) {

    }

    override fun <T> showMoreComplete(data: List<T>) {
        mData.clear()
        mData.addAll(data as MutableList<MyChildrenBean>)
        mAdapter.notifyDataSetChanged()
    }

    override fun loadmoreEnd() {

    }

    override fun loadmoreFail() {

    }


    companion object {
        fun start(context: Context, flag: Boolean) {
            context.startActivity(Intent(context, ChooseChildrenActivity::class.java).putExtra("flag", flag))
        }
    }
}