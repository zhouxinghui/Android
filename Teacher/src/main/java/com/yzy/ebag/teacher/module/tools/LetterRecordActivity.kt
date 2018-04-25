package com.yzy.ebag.teacher.module.tools

import android.content.Context
import android.content.Intent
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.module.homework.UnitPopupWindow
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.mobile.bean.EditionBean
import ebag.mobile.bean.UnitBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.activity_letter_record.*

/**
 * Created by YZY on 2018/4/24.
 */
class LetterRecordActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_letter_record
    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context, LetterRecordActivity::class.java))
        }
    }
    private val request = object : RequestCallBack<EditionBean>(){
        override fun onStart() {

        }

        override fun onSuccess(entity: EditionBean?) {
            if (!isVersionRequest) {
                textbookTv.text = entity?.bookVersion
                classId = entity?.classId ?: ""
                clazzTv.text = entity?.className
            }
            val unitListt = entity?.resultBookUnitOrCatalogVos as ArrayList
            unitListt.forEach {
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
            if (unitListt.isNotEmpty()) {
                unitTv.text = "${unitListt[0].name} - ${unitListt[0].resultBookUnitOrCatalogVos[0].name}"
                unitId = unitListt[0].resultBookUnitOrCatalogVos[0].unitCode
            }
            unitList.clear()
            unitList.addAll(unitListt)
            fragment.update(unitId, classId)
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(this@LetterRecordActivity)
        }
    }
    private var isVersionRequest = false
    private var classId: String = ""
    private var unitId: String = ""
    private val unitList = ArrayList<UnitBean>()
    private var subCode = "yw"
    private val fragment = LetterRecordFragment.newInstance()
    private val clazzListPopup by lazy {
        val popup = ClazzListPopup(this)
        popup.onClassSelectListener = {
            clazzTv.text = it.className
            classId = it.classId
            unitList.clear()
            unitTv.text = ""
            textbookTv.text = ""
            isVersionRequest = false
            request()
        }
        popup
    }
    private val bookVersionPopup by lazy {
        val popup = PreparebookversionPopup(this)
        popup.onConfirmClick = {versionName, versionId, semesterCode, semesterName, subCode, subName ->
            textbookTv.text = "$subName-$versionName-$semesterName"
            isVersionRequest = true
            EBagApi.getUnit(versionId, request)
        }
        popup
    }
    private val unitPopup by lazy {
        val popup = UnitPopupWindow(this)
        popup.setTotalTvGone(true)
        popup.onConfirmClick = {name, unitBean ->
            unitTv.text = name
            unitId = unitBean?.unitCode ?: ""
            fragment.update(unitId, classId)
        }
        popup
    }
    override fun initViews() {
        request()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment).commitAllowingStateLoss()

        clazzBtn.setOnClickListener {
            clazzListPopup.showAsDropDown(clazzBtn)
        }
        unitBtn.setOnClickListener {
            unitPopup.setData(unitList)
            unitPopup.showAsDropDown(unitBtn)
        }
        textbookBtn.setOnClickListener {
            bookVersionPopup.setRequest(classId, "yw")
            bookVersionPopup.showAsDropDown(textbookBtn)
        }
    }

    private fun request(){
        EBagApi.getUnit(classId, subCode, request)
    }
}