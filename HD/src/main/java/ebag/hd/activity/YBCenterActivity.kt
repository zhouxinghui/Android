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

    private lateinit var incomeText:TextView
    private lateinit var expendText:TextView
    private val mPresenter: YBCenterPresenter by lazy { YBCenterPresenter(this,this) }
    override fun getLayoutId(): Int = R.layout.activity_ybcenter

    override fun initViews() {

        activity_ybcenter_currentyb.performClick()
        activity_ybcenter_radiogroup.setOnCheckedChangeListener(this)
        mPresenter.start()
    }

    override fun setSpannableString() {

    }

    override fun showSuccess(currentMoney: String, incomeMoney: String, expendMoney: String, data: MutableList<YBCenterModel>) {
        activity_ybcenter_currentyb.text = currentMoney
        activity_ybcenter_incomeyb.text = incomeMoney
        activity_ybcenter_expendyb.text = expendMoney
        setSpan()
        val adapter = YBCenterAdapter(R.layout.item_ybcenter,data)
        activity_ybcenter_recyclerview.layoutManager = LinearLayoutManager(this)
        activity_ybcenter_recyclerview.adapter = adapter
        activity_ybcenter_recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL,1, Color.parseColor("#e0e0e0")))
        val headerView = View.inflate(this,R.layout.item_ybcenter_header,null)
        incomeText = headerView.findViewById(R.id.ybcenter_header_income) as TextView
        expendText = headerView.findViewById(R.id.ybcenter_header_expend) as TextView
        incomeText.text = "收入 ${activity_ybcenter_incomeyb.text}"
        expendText.text = "支出 ${activity_ybcenter_expendyb.text}"
        adapter.addHeaderView(headerView)
        activity_ybcenter_stateview.showContent()
    }


    private fun setSpan() {
        val currentybSpan = SpannableString(activity_ybcenter_currentyb.text.toString())
        currentybSpan.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x30), false), 4, currentybSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val incomeybSpan = SpannableString(activity_ybcenter_incomeyb.text.toString())
        incomeybSpan.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x30), false), 4, currentybSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val expendybSpan = SpannableString(activity_ybcenter_expendyb.text.toString())
        expendybSpan.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x30), false), 4, currentybSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        activity_ybcenter_currentyb.text = currentybSpan
        activity_ybcenter_incomeyb.text = incomeybSpan
        activity_ybcenter_expendyb.text = expendybSpan
    }

    override fun showError() {

    }

    override fun showEmpty() {

    }

    override fun showLoading() {
        activity_ybcenter_stateview.showLoading()
    }

    override fun loadmoreError() {

    }

    override fun loadmoreFail() {

    }

    override fun showTimePicker() {

    }

    override fun showDataLoading() {

    }

    override fun showDataError() {

    }

    override fun showDataEmpty() {

    }

    override fun showIncome() {
        incomeText.visibility = View.VISIBLE
        expendText.visibility = View.GONE
    }

    override fun showExpend() {
        incomeText.visibility = View.GONE
        expendText.visibility = View.VISIBLE
    }

    override fun showAll() {
        incomeText.visibility = View.VISIBLE
        expendText.visibility = View.VISIBLE
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.activity_ybcenter_currentyb -> showAll()
            R.id.activity_ybcenter_incomeyb -> showIncome()
            R.id.activity_ybcenter_expendyb -> showExpend()
        }
    }
}