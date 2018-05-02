package ebag.mobile.module.account

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.mobile.R
import ebag.mobile.base.ActivityUtils
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.activity_chargeyb.*

class YBChargeActivity : BaseActivity() {

    private val labelArray: Array<String> = arrayOf("1000 Y币", "3000 Y币", "5000 Y币", "10000 Y币", "30000 Y币", "50000 Y币")
    private val moneyArray: Array<String> = arrayOf("￥ 10", "￥ 30", "￥ 50", "￥ 100", "￥ 300", "￥ 500")
    private val datasList: MutableList<ChargeBean> = mutableListOf()
    private lateinit var mAdapter: YBChargeAdapter

    override fun getLayoutId(): Int = R.layout.activity_chargeyb

    override fun initViews() {

        ActivityUtils.addActivity(this)
        val money = intent.getIntExtra("money",0)
        paymoney.text = "10"
        ybcount.text = money.toString()
        labelArray.forEachIndexed { index, s ->

            if (index == 0) {
                datasList.add(ChargeBean(s, moneyArray[index], true))
            }else{
                datasList.add(ChargeBean(s, moneyArray[index], false))
            }
        }

        recyclerview.layoutManager = GridLayoutManager(this, 3)
        mAdapter = YBChargeAdapter(datasList, this)
        recyclerview.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, view, position ->

            datasList.forEach {
                it.isSelected = false
            }

            datasList[position].isSelected = true

            mAdapter.notifyDataSetChanged()

            paymoney.text = datasList[position].money.replace("￥","")

        }

        confirm.setOnClickListener {

            EBagApi.createShopOrderNo(object : RequestCallBack<String>() {

                override fun onStart() {
                    super.onStart()
                    LoadingDialogUtil.showLoading(this@YBChargeActivity, "正在生成订单...")
                }

                override fun onSuccess(entity: String?) {
                    LoadingDialogUtil.closeLoadingDialog()
                    CashierActivity.start(this@YBChargeActivity,paymoney.text.toString(),entity!!)
                }

                override fun onError(exception: Throwable) {
                    LoadingDialogUtil.closeLoadingDialog()
                    T.show(this@YBChargeActivity, "提交失败")
                }

            })

        }
    }


    companion object {
        fun start(c: Context,money:Int){
            c.startActivity(Intent(c,YBChargeActivity::class.java).putExtra("money",money))
        }
    }

}