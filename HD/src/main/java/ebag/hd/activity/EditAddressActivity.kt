package ebag.hd.activity

import android.graphics.Color
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
import android.widget.TextView
import com.bigkoo.pickerview.OptionsPickerView
import com.google.gson.Gson
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.adapter.EditAddressAdapter
import ebag.hd.bean.CitysBean
import ebag.hd.http.EBagApi
import ebag.hd.mvp.model.EditAddressModel
import kotlinx.android.synthetic.main.activity_editaddress.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.regex.Pattern

/**
 * Created by fansan on 2018/3/14.
 */
class EditAddressActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_editaddress
    private var mName: String = ""
    private var mPhoneNum: String = ""
    private var mAddress: String = ""
    private var mPreAddress: String = ""
    private var mId: String = ""
    private lateinit var mAdapter: EditAddressAdapter
    private var update: Boolean = false
    private val datas: MutableList<EditAddressModel> = mutableListOf()
    private var options1Items = ArrayList<CitysBean>()
    private var options2Items = ArrayList<ArrayList<String>>()
    private var options3Items = ArrayList<ArrayList<ArrayList<String>>>()
    private var opt1 = 0
    private var opt2 = 0
    private var opt3 = 0
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
                    EBagApi.updateAddress(mId, name.text.toString().trim(), phone.text.toString().trim(), preAddress.text.toString().trim(), address.text.toString().trim(), "1", object : RequestCallBack<String>() {
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
                showPickerView()
            }
        }


    }

    private fun checkEmpty(name: EditText, phone: EditText, preAddress: TextView, address: EditText): Boolean {
        when {
            name.text.toString().trim().isEmpty() -> T.show(this, "姓名不能为空")
            phone.text.toString().trim().isEmpty() -> T.show(this, "电话不能为空")
            preAddress.text.toString().trim().isEmpty() -> T.show(this, "没有选择省市区")
            address.text.toString().trim().isEmpty() -> T.show(this, "地址不能为空")
            (!Pattern.matches(ebag.hd.base.Constants.MOBILENUMBER_REGEX, phone.text.toString().trim()) && phone.text.toString().trim().length == 11) -> T.show(this, "手机号格式不正确")
            else -> return true
        }

        return false
    }

    private fun initPickerView() {

        val buidle = StringBuilder()
        BufferedReader(InputStreamReader(assets.open("citys.json"))).use {
            var line: String
            while (true) {
                line = it.readLine() ?: break
                buidle.append(line)
            }
        }

        val jsonBean = paserJson(buidle.toString())
        options1Items = jsonBean

        for (i in jsonBean.indices) {
            val cityList = ArrayList<String>() //城市
            val cityId = ArrayList<Int>()
            val provinceAreaList = ArrayList<ArrayList<String>>() //地区
            val provinceAreaIdList = ArrayList<ArrayList<Int>>() //地区
            (jsonBean[i].city.indices).forEach { c ->
                val cityName = jsonBean[i].city[c].name
                cityList.add(cityName)
                cityId.add(jsonBean[i].city[c].id)
                val cityAreaList = ArrayList<String>()
                val cityAreaIntList = ArrayList<Int>()
                if (jsonBean[i].city[c].district == null || jsonBean[i].city[c].district.size == 0) {
                    cityAreaList.add("")
                } else {
                    (jsonBean[i].city[c].district.indices).forEach { d ->
                        val areaName = jsonBean[i].city[c].district[d].name
                        cityAreaList.add(areaName)
                        cityAreaIntList.add(jsonBean[i].city[c].district[d].id)
                    }
                }
                provinceAreaList.add(cityAreaList)
                provinceAreaIdList.add(cityAreaIntList)
            }
            options2Items.add(cityList)
            options3Items.add(provinceAreaList)
        }

    }

    private fun paserJson(json: String): ArrayList<CitysBean> {
        val beanList: ArrayList<CitysBean> = ArrayList()
        val jsonArray = JSONArray(json)
        (0 until jsonArray.length()).mapTo(beanList) { Gson().fromJson(jsonArray.optString(it).toString(), CitysBean::class.java) }
        return beanList
    }

    private fun showPickerView() {

        initPickerView()

        val optView = OptionsPickerView.Builder(this, { opt1, opt2, opt3, _ ->
            this.opt1 = opt1
            this.opt2 = opt2
            this.opt3 = opt3
            datas[2] = EditAddressModel("省市区:", options1Items[this.opt1].name + " " + options2Items[this.opt1][this.opt2] + " " + options3Items[this.opt1][this.opt2][this.opt3])
            mAdapter.notifyItemChanged(2)

        }).setTitleText("选择城市")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .build()

        optView.setPicker(options1Items, options2Items, options3Items)
        optView.setSelectOptions(opt1, opt2, opt3)
        optView.show()
    }
}