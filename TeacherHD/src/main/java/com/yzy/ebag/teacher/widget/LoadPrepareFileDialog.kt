package com.yzy.ebag.teacher.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.PrepareVersionBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.adapter.UnitAdapter
import ebag.hd.bean.UnitBean
import kotlinx.android.synthetic.main.dialog_load_prepare_file.*

/**
 * Created by YZY on 2018/5/24.
 */
class LoadPrepareFileDialog(mContext: Context): BaseDialog(mContext) {
    override fun getLayoutRes(): Int = R.layout.dialog_load_prepare_file

    override fun setWidth(): Int = context.resources.getDimensionPixelSize(R.dimen.x500)
    override fun setHeight(): Int = context.resources.getDimensionPixelSize(R.dimen.x500)

    private val versionAdapter = VersionAdapter()
    private val unitAdapter = UnitAdapter()
    private var bean: PrepareVersionBean? = null
    private var firstPosition = -1
    private var nextPosition = -1
    private var semesterCode = "1"
    private var semesterName = "上学期"

    private var gradeCode = ""
    private var subCode = ""
    var onConfirmClick: ((unitCode: String) -> Unit)? = null
    private var unitId = ""
    private val versionRequest = object : RequestCallBack<PrepareVersionBean>(){
        override fun onStart() {
            versionState.showLoading()
        }
        override fun onSuccess(entity: PrepareVersionBean?) {
            bean = entity
            if (entity == null){
                versionState.showEmpty()
                return
            }
            val first = entity.first
            if (first == null || first.isEmpty()){
                versionState.showEmpty()
                return
            }
            versionAdapter.setNewData(first)
            versionState.showContent()
        }

        override fun onError(exception: Throwable) {
            versionState.showError(exception.message.toString())
        }
    }
    private val unitRequest = object : RequestCallBack<List<UnitBean>>(){
        override fun onStart() {
            unitState.showLoading()
        }
        override fun onSuccess(entity: List<UnitBean>?) {
            LoadingDialogUtil.closeLoadingDialog()
            entity?.forEach {
                val subList = it.resultBookUnitOrCatalogVos
                if (subList.isEmpty()){
                    val subBean = UnitBean.ChapterBean()
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
            unitAdapter.setNewData(entity)
            unitState.showContent()
        }

        override fun onError(exception: Throwable) {
            if (exception is MsgException){
                unitState.showError(exception.message.toString())
            }else{
                unitState.showError()
                exception.handleThrowable(mContext)
            }
        }
    }
    init {
        versionRecycler.layoutManager = LinearLayoutManager(mContext)
        versionRecycler.adapter = versionAdapter
        unitRecycler.layoutManager = LinearLayoutManager(mContext)
        unitRecycler.adapter = unitAdapter
        semesterGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.semesterFirst ->{
                    semesterCode = "1"
                    semesterName = "上学期"
                    val list = bean?.first
                    if (list == null || list.isEmpty()){
                        versionState.showEmpty()
                    }else {
                        versionAdapter.setNewData(list)
                        versionAdapter.currentVersionBean = if (firstPosition == -1) null else versionAdapter.data[firstPosition]
                        versionState.showContent()
                    }
                }
                R.id.semesterSecond ->{
                    semesterCode = "2"
                    semesterName = "下学期"
                    val list = bean?.next
                    if (list == null || list.isEmpty()){
                        versionState.showEmpty()
                    }else{
                        versionAdapter.setNewData(list)
                        versionAdapter.currentVersionBean = if (nextPosition == -1) null else versionAdapter.data[nextPosition]
                        versionState.showContent()
                    }
                }
            }
        }
        versionAdapter.setOnItemClickListener { adapter, view, position ->
            adapter as VersionAdapter
            unitId = ""
            if (semesterCode == "1"){
                firstPosition = position
                nextPosition = -1
            }else{
                nextPosition = position
                firstPosition = -1
            }
            adapter.currentVersionBean = adapter.data[position]
            TeacherApi.prepareUnit(adapter.currentVersionBean?.bookVersionId!!, unitRequest)
            versionTv.text = "${adapter.currentVersionBean?.bookVersionName} $semesterName"
            versionLayout.visibility = View.GONE
            unitRecycler.visibility = View.VISIBLE
            confirmBtn.visibility = View.VISIBLE
        }

        unitAdapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position)
            if(item is UnitBean) {
                if (item.isExpanded) {
                    adapter.collapse(position)
                } else {
                    adapter.expand(position)
                }
            }else{
                item as UnitBean.ChapterBean?
                (adapter as UnitAdapter).selectSub = item
                unitId = item?.unitCode!!
            }
        }

        versionTv.setOnClickListener {
            versionLayout.visibility = View.VISIBLE
            unitRecycler.visibility = View.GONE
            confirmBtn.visibility = View.GONE
        }
        confirmBtn.setOnClickListener {
            if (StringUtils.isEmpty(unitId)){
                T.show(mContext, "未选择单元！")
                return@setOnClickListener
            }
            onConfirmClick?.invoke(unitId)
            dismiss()
        }
    }

    fun show(gradeCode: String?, subCode: String?) {
        if (this.gradeCode != gradeCode || this.subCode != subCode){
            this.gradeCode = gradeCode ?: ""
            this.subCode = subCode ?: ""
            TeacherApi.prepareVersion(this.gradeCode, "1", this.subCode, versionRequest)
        }
        super.show()
    }

    inner class VersionAdapter : BaseQuickAdapter<PrepareVersionBean.VersionBean, BaseViewHolder>(R.layout.item_prepare_subject){
        var currentVersionBean: PrepareVersionBean.VersionBean? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: PrepareVersionBean.VersionBean) {
            val textView = helper.getView<TextView>(R.id.text)
            textView.text = item.bookVersionName
            textView.isSelected = currentVersionBean == item
            helper.getView<View>(R.id.dot).isSelected = currentVersionBean == item
        }
    }
}