package com.yzy.ebag.teacher.ui.activity

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SerializableUtils
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.base.Constants
import ebag.hd.bean.CurrentCityBean
import ebag.hd.bean.SchoolBean
import ebag.hd.http.EBagApi
import ebag.hd.widget.CityPickerDialog
import kotlinx.android.synthetic.main.activity_select_school.*

class SelectSchoolActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_select_school
    }

    companion object {
        fun jump(activity: Activity, currentCityBean: CurrentCityBean?){
            activity.startActivityForResult(
                    Intent(activity, SelectSchoolActivity::class.java)
                            .putExtra("cityBean",currentCityBean),
                    Constants.SELECT_SCHOOL)
        }
    }

    private val request = object: RequestCallBack<List<SchoolBean>>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@SelectSchoolActivity)
        }
        override fun onSuccess(entity: List<SchoolBean>?) {
            LoadingDialogUtil.closeLoadingDialog()
            if (entity == null || entity.isEmpty()){
                T.show(this@SelectSchoolActivity, "暂无数据！")
                adapter.data.clear()
                adapter.selectPosition = -1
                campusName.text = ""
                return
            }
            adapter.selectPosition = -1
            adapter.setNewData(entity)
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(this@SelectSchoolActivity)
        }

    }
    private val adapter by lazy { MyAdapter() }
    private lateinit var currentCityBean: CurrentCityBean
    private val cityPickerDialog by lazy {
        val dialog = CityPickerDialog(this)
        dialog.onConfirmClick = {currentCityBean ->
            this.currentCityBean = currentCityBean

            setSchoolInfo(currentCityBean)
        }
        dialog
    }
    override fun initViews() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 5)
        cityBtn.setOnClickListener {
            cityPickerDialog.show()
        }
        titleBar.setRightText("确定", {
            if (StringUtils.isEmpty(campusName.text.toString())){
                T.show(this, "请选择学校")
                return@setRightText
            }
            setResult(Constants.BACK_TO_CREATE_CLASS, Intent().putExtra(Constants.CURRENT_CITY, currentCityBean))
            SerializableUtils.setSerializable(Constants.CURRENT_CITY, currentCityBean)
            finish()
        })

        if (intent.getSerializableExtra("cityBean") != null){
            currentCityBean = intent.getSerializableExtra("cityBean") as CurrentCityBean
            setSchoolInfo(currentCityBean)
        }
    }

    private fun setSchoolInfo(currentCityBean: CurrentCityBean){
        val provinceName = currentCityBean.provinceName ?: ""
        val cityName = currentCityBean.cityName ?: ""
        val countyName = currentCityBean.countyName ?: ""
        cityNameTv.text = provinceName + cityName + countyName
        cityNameTv.isSelected = true

        EBagApi.getSchool(currentCityBean.province, currentCityBean.city, currentCityBean.county, request)
    }

    inner class MyAdapter: BaseQuickAdapter<SchoolBean, BaseViewHolder>(R.layout.item_select_school){
        var selectPosition = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        override fun convert(helper: BaseViewHolder, item: SchoolBean) {
            val position = helper.adapterPosition
            helper.itemView.setOnClickListener {
                selectPosition = position
                campusName.text = item.schoolName
                currentCityBean.schoolCode = item.id
                currentCityBean.schoolName = item.schoolName
            }
            val checkBox = helper.getView<CheckBox>(R.id.checkbox)
            checkBox.text = item.schoolName
            checkBox.isChecked = selectPosition != -1 && selectPosition == position
            if(selectPosition == -1)
                campusName.text = ""
        }

    }
}
