package ebag.mobile.module.account

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import ebag.core.base.BaseActivity
import ebag.mobile.R
import ebag.mobile.base.ActivityUtils
import ebag.mobile.bean.YBCurrentBean
import ebag.mobile.module.shop.YBProtocolActivity
import kotlinx.android.synthetic.main.activity_ybcenter.*

class YBCenterActivity : BaseActivity(), YBCenterContract.YBCenterView {

    private lateinit var mPersenter: YBCenterPersenter
    private lateinit var mAdapter: YBCenterAdapter
    private val datas: MutableList<YBCurrentBean.MoneyDetailsBean> = mutableListOf()
    private  var money:Int = 0

    override fun getLayoutId(): Int = R.layout.activity_ybcenter

    override fun initViews() {

        ActivityUtils.addActivity(this)
        mPersenter = YBCenterPersenter()
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        mAdapter = YBCenterAdapter(datas)
        recyclerview.adapter = mAdapter
        mAdapter.setOnLoadMoreListener({
            mPersenter.loadmore(this)

        }, recyclerview)

        mPersenter.startFirstpage(this)

        charge.setOnClickListener {

            YBChargeActivity.start(this,money)
        }

        titlebar.setOnRightClickListener {

            YBProtocolActivity.start(this)
        }

        stateview.setOnRetryClickListener {
            mPersenter.startFirstpage(this)
        }
    }

    override fun <T> showContents(data: T) {

        datas.addAll((data as YBCurrentBean).moneyDetails)
        mAdapter.notifyDataSetChanged()
        mAdapter.disableLoadMoreIfNotFullPage(recyclerview)
        stateview.showContent()
    }

    override fun <T> showData(data: T) {
        money = (data as YBCurrentBean).remainMoney
        charge.isClickable = true
        income.text = (data as YBCurrentBean).increasedMoney.toString()
        expense.text = (data as YBCurrentBean).reduceMoney.toString()
    }

    override fun showLoading() {
        stateview.showLoading()
    }

    override fun <T> showLoadMoreComplete(data: T) {
        datas.addAll((data as YBCurrentBean).moneyDetails)
        mAdapter.loadMoreComplete()
    }

    override fun loadmoreFail() {
        mAdapter.loadMoreFail()
    }

    override fun loadmoreEnd() {
        mAdapter.loadMoreEnd()
    }

    override fun showEmpty() {
        stateview.showEmpty()
    }

    override fun showError(e: Throwable?) {
        stateview.showError()
    }

    companion object {

        fun start(c: Context) {
            c.startActivity(Intent(c, YBCenterActivity::class.java))
        }
    }

}