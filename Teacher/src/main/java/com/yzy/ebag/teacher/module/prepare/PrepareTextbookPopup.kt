package com.yzy.ebag.teacher.module.prepare

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.RadioGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.PrepareVersionBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BasePopupWindow
import ebag.core.http.network.RequestCallBack
import ebag.core.widget.empty.StateView

/**
 * Created by YZY on 2018/4/24.
 */
class PrepareTextbookPopup(private val mContext: Context): BasePopupWindow(mContext) {
    override fun getLayoutRes(): Int = R.layout.popup_prepare_textbook

    override fun setHeight(): Int = contentView.resources.getDimensionPixelSize(R.dimen.y500)
    override fun setWidth(): Int = WindowManager.LayoutParams.MATCH_PARENT

    private val stateView = contentView.findViewById<StateView>(R.id.stateView)

    private var semesterCode = "1"
    private var semesterName = "上学期"
    private val adapter = MyAdapter()
    private var bean: PrepareVersionBean? = null
    private var firstPosition = -1
    private var nextPosition = -1

    private var gradeCode = ""
    private var lessonType = ""
    private var subCode = ""

    var onTextbookSelectListener : ((versionBean: PrepareVersionBean.VersionBean, semesterCode: String, semesterName: String) -> Unit)? = null
    private val request = object : RequestCallBack<PrepareVersionBean>(){
        override fun onStart() {
            stateView.showLoading()
        }
        override fun onSuccess(entity: PrepareVersionBean?) {
            bean = entity
            if (entity == null){
                stateView.showEmpty()
                return
            }
            val first = entity.first
            if (first == null || first.isEmpty()){
                stateView.showEmpty()
                return
            }
            adapter.setNewData(first)
            stateView.showContent()
        }

        override fun onError(exception: Throwable) {
            stateView.showError(exception.message.toString())
        }
    }

    init {
        stateView.setBackgroundRes(R.color.white)
        val bookRecycler = contentView.findViewById<RecyclerView>(R.id.bookRecycler)
        val semesterGroup = contentView.findViewById<RadioGroup>(R.id.semesterGroup)

        semesterGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.semesterFirst ->{
                    semesterCode = "1"
                    semesterName = "上学期"
                    val list = bean?.first
                    if (list == null || list.isEmpty()){
                        stateView.showEmpty()
                    }else {
                        adapter.setNewData(list)
                        adapter.currentVersionBean = if (firstPosition == -1) null else adapter.data[firstPosition]
                        stateView.showContent()
                    }
                }
                R.id.semesterSecond ->{
                    semesterCode = "2"
                    semesterName = "下学期"
                    val list = bean?.next
                    if (list == null || list.isEmpty()){
                        stateView.showEmpty()
                    }else{
                        adapter.setNewData(list)
                        adapter.currentVersionBean = if (nextPosition == -1) null else adapter.data[nextPosition]
                        stateView.showContent()
                    }
                }
            }
        }

        bookRecycler.layoutManager = LinearLayoutManager(mContext)
        bookRecycler.adapter = adapter

        adapter.setOnItemClickListener { adapter, view, position ->
            adapter as MyAdapter
            if (semesterCode == "1"){
                firstPosition = position
                nextPosition = -1
            }else{
                nextPosition = position
                firstPosition = -1
            }
            adapter.currentVersionBean = adapter.data[position]
            onTextbookSelectListener?.invoke(adapter.data[position], semesterCode, semesterName)
            dismiss()
        }
    }
    fun requestData(gradeCode: String, lessonType: String, subCode: String){
        if (this.gradeCode != gradeCode || this.lessonType != lessonType || this.subCode != subCode) {
            this.gradeCode = gradeCode
            this.lessonType = lessonType
            this.subCode = subCode
            TeacherApi.prepareVersion(gradeCode, lessonType, subCode, request)
        }
    }

    inner class MyAdapter : BaseQuickAdapter<PrepareVersionBean.VersionBean, BaseViewHolder>(R.layout.item_assign_popup){
        var currentVersionBean: PrepareVersionBean.VersionBean? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: PrepareVersionBean.VersionBean) {
            val textView = helper.getView<TextView>(R.id.tv)
            textView.text = item.bookVersionName
            textView.isSelected = currentVersionBean == item
        }
    }
}