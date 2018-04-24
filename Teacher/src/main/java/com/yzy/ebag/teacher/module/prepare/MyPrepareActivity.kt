package com.yzy.ebag.teacher.module.prepare

import android.view.View
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.PrepareBaseBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.module.homework.UnitPopupWindow
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.mobile.bean.UnitBean
import kotlinx.android.synthetic.main.activity_my_prepare.*

/**
 * Created by YZY on 2018/4/24.
 */
class MyPrepareActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_my_prepare
    private val request = object : RequestCallBack<PrepareBaseBean>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@MyPrepareActivity)
        }

        override fun onSuccess(entity: PrepareBaseBean?) {
            LoadingDialogUtil.closeLoadingDialog()
            gradeCode = entity?.resultSubjectVo!!.gradeCode
            subjectCode = entity.resultSubjectVo.subCode ?: ""
            subjectTv.text = "${entity?.resultSubjectVo?.gradeName} ${entity?.resultSubjectVo?.subject}"

            textbookTv.text = "${entity?.resultSubjectVo?.versionName} ${entity?.resultSubjectVo?.semesterName}"
            semesterCode = entity.resultSubjectVo.semesterCode
            textbookCode = entity.resultSubjectVo.versionCode

            unitList.clear()
            unitList.addAll(entity.resultBookUnitOrCatalogVos)
            fragment.notifyRequest(type, gradeCode, subjectCode, unitId)
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(this@MyPrepareActivity)
        }
    }
    private var type = "1"
    private var unitId = ""
    private var gradeCode = ""
    private var subjectCode = ""
    private var semesterCode = ""
    private var textbookCode = ""
    private val unitList = ArrayList<UnitBean>()
    private val fragment = PrepareFragment.newInstance()
    private val unitRequest = object : RequestCallBack<List<UnitBean>>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@MyPrepareActivity)
        }
        override fun onSuccess(entity: List<UnitBean>?) {
            LoadingDialogUtil.closeLoadingDialog()
            entity?.forEach {
                val subList = it.resultBookUnitOrCatalogVos
                if (subList.isEmpty()){
                    val subBean = UnitBean.UnitSubBean()
                    subBean.id = it.id
                    subBean.code = it.code
                    subBean.name = it.name
                    subBean.bookVersionId = it.bookVersionId
                    subBean.pid = it.pid
                    subBean.unitCode = it.unitCode
                    subBean.isUnit = true
                    it.resultBookUnitOrCatalogVos.add(subBean)
                }
            }
            unitList.clear()
            unitList.addAll(entity!!)
            fragment.notifyRequest(type, gradeCode, subjectCode, null)
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(this@MyPrepareActivity, "网络请求失败！")
        }

    }
    private val subjectPopup by lazy { 
        val popup = PrepareSubjectPopup(this)
        popup.onSubjectSelectListener = {gradeCode, gradeName, subjectCode, subjectName ->
            this.gradeCode = gradeCode
            this.subjectCode = subjectCode
            subjectTv.text = "$gradeName $subjectName"
            textbookTv.text = ""
            unitTv.text = "请选择单元"
            unitList.clear()
            fragment.notifyRequest(type, gradeCode, subjectCode, null)
        }
        popup
    }
    private val textbookPopup by lazy {
        val popup = PrepareTextbookPopup(this)
        popup.onTextbookSelectListener = {versionBean, semesterCode, semesterName ->
            textbookTv.text = "${versionBean.bookVersionName} $semesterName"
            unitList.clear()
            TeacherApi.prepareUnit(versionBean.bookVersionId, unitRequest)
        }
        popup
    }
    private val unitPopup by lazy {
        val popup = UnitPopupWindow(this)
        popup.onConfirmClick = {name, unitBean ->
            unitTv.text = name
            fragment.notifyRequest(type, gradeCode, subjectCode, unitBean?.unitCode)
        }
        popup
    }
    override fun initViews() {
        supportFragmentManager.beginTransaction().replace(R.id.fileListLayout, fragment).commitAllowingStateLoss()
        request()

        subjectBtn.setOnClickListener {
            subjectPopup.requestData(type)
            subjectPopup.showAsDropDown(subjectBtn)
        }
        textbookBtn.setOnClickListener {
            textbookPopup.requestData(gradeCode, type, subjectCode)
            textbookPopup.showAsDropDown(textbookBtn)
        }
        unitBtn.setOnClickListener{
            unitPopup.setData(unitList)
            unitPopup.showAsDropDown(unitBtn)
        }

        titleGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.schoolResource ->{
                    type = "2"
                }
                R.id.shareResource ->{
                    type = "3"
                }
            }
            request()
        }

        titleBar.setRightText("资源库", {
            if (titleGroup.visibility == View.GONE) {
                titleGroup.visibility = View.VISIBLE
                titleBar.setRightText("课件")
                titleBar.setTitle("")
                type = if (type == "1" && schoolResource.isChecked)
                    "2"
                else
                    "3"
            }else{
                titleGroup.visibility = View.GONE
                titleBar.setRightText("资源库")
                titleBar.setTitle("教学课件")
                type = "1"
            }
            request()
        })
    }

    private fun request(){
        TeacherApi.prepareBaseData(type, request)
    }
}