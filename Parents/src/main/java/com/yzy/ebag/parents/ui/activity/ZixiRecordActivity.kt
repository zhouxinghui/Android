package com.yzy.ebag.parents.ui.activity

import android.content.Context
import android.content.Intent
import com.yzy.ebag.parents.R
import com.yzy.ebag.parents.ui.fragment.LetterRecordFragment
import com.yzy.ebag.parents.ui.fragment.ReadRecordFragment
import com.yzy.ebag.parents.ui.widget.ClazzListPopup
import com.yzy.ebag.parents.ui.widget.PreparebookversionPopup
import com.yzy.ebag.parents.ui.widget.UnitPopupWindow

import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.SerializableUtils
import ebag.mobile.base.Constants
import ebag.mobile.bean.EditionBean
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.bean.UnitBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.activity_letter_record.*


/**
 * Created by YZY on 2018/4/24.
 */
class ZixiRecordActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_letter_record
    companion object {
        fun jump(context: Context, queryType: String){
            context.startActivity(Intent(context, ZixiRecordActivity::class.java)
                    .putExtra("queryType", queryType)
            )
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
            if (queryType == "1")
                letterFragment.update(unitId, classId)
            else
                readFragment.update(unitId, classId)
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(this@ZixiRecordActivity)
        }
    }
    private var isVersionRequest = false
    private var classId: String = (SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).classId
    private var unitId: String = ""
    private val unitList = ArrayList<UnitBean>()
    private var subCode = "yw"
    private val letterFragment = LetterRecordFragment.newInstance()
    private val readFragment = ReadRecordFragment.newInstance()
    private var clazzListPopup: ClazzListPopup? = null
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
            if (queryType == "1")
                letterFragment.update(unitId, classId)
            else
                readFragment.update(unitId, classId)
        }
        popup
    }
    private var queryType = "1"
    override fun initViews() {
        if (classId.isEmpty()){
            stateview.showEmpty("未加入班级")
            return
        }
        queryType = intent.getStringExtra("queryType")
        request()
        clazzTv.text = (SerializableUtils.getSerializable(Constants.CHILD_USER_ENTITY) as MyChildrenBean).className
        if (queryType == "1") {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentLayout, letterFragment).commitAllowingStateLoss()
            titleBar.setTitle("每日练字")
        }else{
            supportFragmentManager.beginTransaction().replace(R.id.fragmentLayout, readFragment).commitAllowingStateLoss()
            titleBar.setTitle("每日跟读")
        }
        clazzBtn.setOnClickListener {
            if(clazzListPopup == null){
                val bean = SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY)
                clazzListPopup = ClazzListPopup(this,bean.uid)
                clazzListPopup?.onClassSelectListener = {
                    clazzTv.text = it.className
                    classId = it.classId
                    unitList.clear()
                    unitTv.text = ""
                    textbookTv.text = ""
                    isVersionRequest = false
                    request()
                }
            }
            clazzListPopup?.showAsDropDown(clazzBtn)
        }
        unitBtn.setOnClickListener {
            unitPopup.setData(unitList)
            unitPopup.showAsDropDown(unitBtn)
        }
        textbookBtn.setOnClickListener {
            bookVersionPopup.setRequest(classId, if (queryType == "1") "yw" else null)
            bookVersionPopup.showAsDropDown(textbookBtn)
        }
    }

    private fun request(){
        EBagApi.getUnit(classId, if (queryType == "1") subCode else "", request)
    }
}