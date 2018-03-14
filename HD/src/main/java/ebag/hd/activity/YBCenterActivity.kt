package ebag.hd.activity

import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import ebag.core.base.BaseActivity
import ebag.hd.R
import ebag.hd.adapter.YBCenterAdapter
import ebag.hd.mvp.contract.YBCenterContract
import ebag.hd.mvp.model.YBCenterModel
import ebag.hd.mvp.presenter.YBCenterPresenter
import kotlinx.android.synthetic.main.activity_ybcenter.*

/**
 * Created by fansan on 2018/3/13.
 */
class YBCenterActivity : BaseActivity(),YBCenterContract.View,RadioGroup.OnCheckedChangeListener{


    private val mPresenter: YBCenterPresenter by lazy { YBCenterPresenter(this,this) }
    override fun getLayoutId(): Int = R.layout.activity_ybcenter
    private lateinit var mAdapter: YBCenterAdapter
    private var mData: MutableList<YBCenterModel> = mutableListOf()
    private var page:Int = 1
    private val CURRENT = 0
    private val INCOME = 1
    private val EXPEND = 2
    private var nowSelected = CURRENT
    override fun initViews() {

        activity_ybcenter_currentyb.performClick()
        activity_ybcenter_radiogroup.setOnCheckedChangeListener(this)
        activity_ybcenter_datastateview.setOnRetryClickListener {

        }
        mAdapter = YBCenterAdapter(R.layout.item_ybcenter,mData)
        activity_ybcenter_recyclerview.layoutManager = LinearLayoutManager(this)
        activity_ybcenter_recyclerview.adapter = mAdapter
        activity_ybcenter_recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL,1, Color.parseColor("#e0e0e0")))
        mAdapter.disableLoadMoreIfNotFullPage(activity_ybcenter_recyclerview)
        mAdapter.setOnLoadMoreListener({
            when(nowSelected){
                CURRENT -> mPresenter.request(page,10,false,true)
                INCOME -> mPresenter.switch(page,10,"1",true)
                EXPEND -> mPresenter.switch(page,10,"2",true)
            }
        },activity_ybcenter_recyclerview)
        mPresenter.start()
    }


    override fun showSuccess(currentMoney: String, incomeMoney: String, expendMoney: String, data: MutableList<YBCenterModel>) {
        activity_ybcenter_currentyb.text = "${currentMoney}YB"
        activity_ybcenter_incomeyb.text = "${incomeMoney}YB"
        activity_ybcenter_expendyb.text = "${expendMoney}YB"
        setSpan()
        /*if (data.size<10){
            loadmoreEnd()
        }*/
        mAdapter.setNewData(data)
        ybcenter_header_income.text = "收入 ${activity_ybcenter_incomeyb.text}"
        ybcenter_header_expend.text = "支出 ${activity_ybcenter_expendyb.text}"
        activity_ybcenter_stateview.showContent()
        page+=1
    }


    private fun setSpan() {
        val currentybSpan = SpannableString(activity_ybcenter_currentyb.text.toString())
        currentybSpan.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x26), false), currentybSpan.length-2, currentybSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val incomeybSpan = SpannableString(activity_ybcenter_incomeyb.text.toString())
        incomeybSpan.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x26), false), incomeybSpan.length-2, incomeybSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val expendybSpan = SpannableString(activity_ybcenter_expendyb.text.toString())
        expendybSpan.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x26), false), expendybSpan.length-2, expendybSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        activity_ybcenter_currentyb.text = currentybSpan
        activity_ybcenter_incomeyb.text = incomeybSpan
        activity_ybcenter_expendyb.text = expendybSpan
    }

    override fun showError() {
        activity_ybcenter_stateview.showError()
    }


    override fun showLoading() {
        activity_ybcenter_stateview.showLoading()
    }

    override fun loadmoreError() {
        mAdapter.loadMoreFail()
    }

    override fun loadmoreComplete(data: MutableList<YBCenterModel>) {
        mData.addAll(data)
        mAdapter.loadMoreComplete()
        page+=1
    }


    override fun showTimePicker() {

    }

    override fun showDataLoading() {
        activity_ybcenter_datastateview.showLoading()
    }

    override fun showDataError() {
        activity_ybcenter_datastateview.showError()
    }

    override fun showDataEmpty() {
        activity_ybcenter_datastateview.showEmpty()
    }

    override fun showIncome() {
        ybcenter_header_income.visibility = View.VISIBLE
        ybcenter_header_expend.visibility = View.GONE
    }

    override fun showExpend() {
        ybcenter_header_income.visibility = View.GONE
        ybcenter_header_expend.visibility = View.VISIBLE
    }

    override fun showAll() {
        ybcenter_header_income.visibility = View.VISIBLE
        ybcenter_header_expend.visibility = View.VISIBLE
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.activity_ybcenter_currentyb -> {
                showAll()
                page = 0
                nowSelected = CURRENT
                mPresenter.request(page,10,false,false)
            }
            R.id.activity_ybcenter_incomeyb -> {showIncome()
                page = 0
                mPresenter.switch(page,10,"1",false)
                nowSelected = INCOME
            }
            R.id.activity_ybcenter_expendyb -> {
                showExpend()
                page = 0
                mPresenter.switch(page,10,"2",false)
                nowSelected = EXPEND
            }
        }
    }

    override fun dataLoadSuccess(data: MutableList<YBCenterModel>) {
        /*if (data.size<10){
            loadmoreEnd()
        }*/
        mAdapter.setNewData(data)
        page+=1
        activity_ybcenter_datastateview.showContent()
    }

    override fun loadmoreEnd() {
        mAdapter.loadMoreEnd()
    }


}