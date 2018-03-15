package ebag.hd.activity

import android.graphics.Color
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.adapter.EditAddressAdapter
import ebag.hd.http.EBagApi
import ebag.hd.mvp.model.EditAddressModel
import ebag.hd.widget.CityPickerDialog
import kotlinx.android.synthetic.main.activity_editaddress.*

/**
 * Created by fansan on 2018/3/14.
 */
class EditAddressActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_editaddress
    private var mName: String = ""
    private var mPhoneNum: String = ""
    private var mAddress: String = ""
    private var mPreAddress: String = ""
    private var mId:String = ""
    private lateinit var mAdapter: EditAddressAdapter
    private var update: Boolean = false
    private val datas: MutableList<EditAddressModel> = mutableListOf()
    override fun initViews() {

        if (intent.extras != null) {

            update = true
            mName = intent.getStringExtra("name")
            mPhoneNum = intent.getStringExtra("phone")
            mAddress = intent.getStringExtra("address")
            mPreAddress = intent.getStringExtra("preAddress")
            mId = intent.getStringExtra("id")

        }

        activity_editaddress_save.setOnClickListener {

            val name = mAdapter.getViewByPosition(activity_editaddress_recyclerview, 0, R.id.editaddress_edittext) as EditText
            val phone = mAdapter.getViewByPosition(activity_editaddress_recyclerview, 1, R.id.editaddress_edittext) as EditText
            val preAddress = mAdapter.getViewByPosition(activity_editaddress_recyclerview, 2, R.id.editaddress_area) as TextView
            val address = mAdapter.getViewByPosition(activity_editaddress_recyclerview, 3, R.id.editaddress_edittext) as EditText

            if (update) {
                if (checkEmpty(name, phone, preAddress, address)) {
                    EBagApi.updateAddress(mId,name.text.toString().trim(),phone.text.toString().trim(),preAddress.text.toString().trim(),address.text.toString().trim(),object :RequestCallBack<String>(){
                        override fun onSuccess(entity: String?) {
                            T.show(this@EditAddressActivity, "保存成功")
                            setResult(99)
                            finish()
                        }

                        override fun onError(exception: Throwable) {
                            T.show(this@EditAddressActivity, "保存失败")
                        }

                    })
                }

            } else {

                if (checkEmpty(name, phone, preAddress, address)) {
                    EBagApi.saveAddress(name.text.toString().trim(), phone.text.toString().trim(), preAddress.text.toString().trim(), address.text.toString().trim(), object : RequestCallBack<String>() {

                        override fun onSuccess(entity: String?) {
                            T.show(this@EditAddressActivity, "保存成功")
                            setResult(99)
                            finish()
                        }

                        override fun onError(exception: Throwable) {
                            T.show(this@EditAddressActivity, "保存失败")
                        }

                    })
                }
            }

        }


        for (i in 0..3) {
            when (i) {
                0 -> datas.add(EditAddressModel("收货人:", mName))
                1 -> datas.add(EditAddressModel("手机号:", mPhoneNum))
                2 -> datas.add(EditAddressModel("省市区:", mPreAddress))
                3 -> datas.add(EditAddressModel("详细地址:", mAddress))
            }
        }
        activity_editaddress_recyclerview.layoutManager = LinearLayoutManager(this)
        mAdapter = EditAddressAdapter(R.layout.item_editaddress, datas)
        activity_editaddress_recyclerview.adapter = mAdapter
        activity_editaddress_recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        mAdapter.setOnItemChildClickListener { _, _, position ->
            if (position == 2) {
                val cityPick = CityPickerDialog(this)
                cityPick.onConfirmClick = { currentCityBean ->
                    datas[2] = EditAddressModel("省市区:", "${currentCityBean.cityName}  ${if (currentCityBean.countyName.isNullOrEmpty()) "" else currentCityBean.countyName}  ${if (currentCityBean.provinceName.isNullOrEmpty()) "" else currentCityBean.provinceName}")
                    mAdapter.notifyItemChanged(2)
                    //T.show(this,"haha")
                }
                cityPick.show()
            }
        }


    }

    private fun checkEmpty(name: EditText, phone: EditText, preAddress: TextView, address: EditText): Boolean {
        when {
            name.text.toString().trim().isEmpty() -> T.show(this, "姓名不能为空")
            phone.text.toString().trim().isEmpty() -> T.show(this, "电话不能为空")
            preAddress.text.toString().trim().isEmpty() -> T.show(this, "没有选择省市区")
            address.text.toString().trim().isEmpty() -> T.show(this, "地址不能为空")
            else -> return true
        }

        return false
    }
}