package com.yzy.ebag.teacher.widget

import android.content.Context
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.bean.CurrentCityBean
import kotlinx.android.synthetic.main.dialog_create_school.*

/**
 * Created by YZY on 2018/4/11.
 */
class CreateSchoolDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_create_school
    }
    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x600)
    }
    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y400)
    }
    var onCreateSuccess: ((currentCityBean: CurrentCityBean) -> Unit)? = null
    private val createRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            dismiss()
            LoadingDialogUtil.showLoading(context, "正在创建...")
        }

        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(context, "创建成功")
            onCreateSuccess?.invoke(currentCityBean)
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(context)
        }
    }
    private lateinit var currentCityBean: CurrentCityBean
    init {
        title_tv.text = "创建学校"
        confirmBtn.setOnClickListener {
            val schoolName = countEdit.text.toString()
            if (!StringUtils.isEmpty(schoolName)){
                TeacherApi.createSchool(schoolName, currentCityBean.province, currentCityBean.city, currentCityBean.county, createRequest)
            }else{
                T.show(context, "请输入学校名称")
            }
        }
    }

    fun show(currentCityBean: CurrentCityBean) {
        this.currentCityBean = currentCityBean
        super.show()
    }
}