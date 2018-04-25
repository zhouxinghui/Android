package com.yzy.ebag.teacher.module.tools

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.WindowManager
import android.widget.RadioGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import ebag.core.base.BasePopupWindow
import ebag.core.http.network.RequestCallBack
import ebag.core.widget.empty.StateView
import ebag.mobile.bean.ReadRecordVersionBean
import ebag.mobile.http.EBagApi

/**
 * Created by YZY on 2018/4/18.
 */
class PreparebookversionPopup(private val mContext: Context): BasePopupWindow(mContext) {
    override fun getLayoutRes(): Int {
        return R.layout.popup_exchange_version
    }
    override fun setWidth(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT
    }

    override fun setHeight(): Int {
        return contentView.resources.getDimensionPixelSize(R.dimen.y550)
    }
    private var semesterGroup: RadioGroup = contentView.findViewById(R.id.semesterGroup)
    private var gradeRecycler: RecyclerView = contentView.findViewById(R.id.gradeRecycler)
    private var versionRecycler: RecyclerView = contentView.findViewById(R.id.versionRecycler)
    private var stateView: StateView = contentView.findViewById(R.id.stateView)

    private val versionRequest = object : RequestCallBack<ReadRecordVersionBean>(){
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: ReadRecordVersionBean?) {
            stateView.showContent()
            if (entity == null) {
                stateView.showEmpty()
                return
            }
            addCourseTextbookBean = entity
            if (entity.first.isEmpty() && entity.next.isEmpty()){
                stateView.showEmpty()
                return
            }
            if (!entity.first.isEmpty()){
                subCode = entity.first[0].subjectCode
                subName = entity.first[0].subjectName
                subjectAdapter.setNewData(entity.first)
                subjectAdapter.selectPosition = 0
                subjectAdapter.removeAllHeaderView()
                val versionList = entity.first[0].bookVersionVoList
                if (versionList != null && !versionList.isEmpty()){
                    versionAdapter.setNewData(versionList)
                    versionAdapter.removeAllHeaderView()
                }
            }
        }

        override fun onError(exception: Throwable) {
            stateView.showError(exception.message.toString())
        }
    }

    private var subCode = ""
    private var subName = ""
    private var versionId = ""
    private var versionName = ""
    private var versionCode = ""
    private var semesterCode = "1"
    private var semesterName = "上学期"
    private var addCourseTextbookBean = ReadRecordVersionBean()
    var onConfirmClick: ((
            versionName: String,
            versionId: String,
            semesterCode: String,
            semesterName: String,
            subCode: String,
            subName: String) -> Unit)? = null
    private val subjectAdapter by lazy {
        val adapter = SubjectAdapter()
        adapter.addHeaderView(subjectEmptyHead)
        adapter
    }
    private val versionAdapter by lazy {
        val adapter = VersionAdapter()
        adapter.addHeaderView(versionEmptyHead)
        adapter
    }
    private val subjectEmptyHead by lazy {
        val textView = TextView(mContext)
        textView.gravity = Gravity.CENTER
        textView.text = "暂无数据"
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.resources.getDimension(R.dimen.tv_big))
        textView
    }
    private val versionEmptyHead by lazy {
        val textView = TextView(mContext)
        textView.gravity = Gravity.CENTER
        textView.text = "暂无数据"
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.resources.getDimension(R.dimen.tv_big))
        textView
    }
    private var classId: String? = ""
    private var requestSubCode: String? = ""
    init {
        semesterGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.semesterFirst ->{
                    semesterCode = "1"
                    semesterName = "上学期"
                    val first = addCourseTextbookBean.first
                    if (first.isEmpty()){
                        subjectAdapter.addHeaderView(subjectEmptyHead)
                        subjectAdapter.setNewData(first)
                        versionAdapter.addHeaderView(versionEmptyHead)
                        versionAdapter.setNewData(null)
                    }else{
                        subjectAdapter.removeAllHeaderView()
                        subjectAdapter.setNewData(first)
                        val versionList = first[subjectAdapter.selectPosition].bookVersionVoList
                        if (versionList.isEmpty()){
                            versionAdapter.addHeaderView(versionEmptyHead)
                        }else{
                            versionAdapter.removeAllHeaderView()
                        }
                        versionAdapter.setNewData(versionList)
                        versionAdapter.selectPosition = versionAdapter.selectPosition
                    }
                }
                R.id.semesterSecond ->{
                    semesterCode = "2"
                    semesterName = "下学期"
                    val next = addCourseTextbookBean.next
                    if (next.isEmpty()){
                        subjectAdapter.addHeaderView(subjectEmptyHead)
                        subjectAdapter.setNewData(next)
                        versionAdapter.addHeaderView(versionEmptyHead)
                        versionAdapter.setNewData(null)
                    }else{
                        subjectAdapter.removeAllHeaderView()
                        subjectAdapter.setNewData(next)
                        val versionList = next[subjectAdapter.selectPosition].bookVersionVoList
                        if (versionList == null || versionList.isEmpty()){
                            versionAdapter.addHeaderView(versionEmptyHead)
                        }else{
                            versionAdapter.removeAllHeaderView()
                        }
                        versionAdapter.setNewData(versionList)
                        versionAdapter.selectPosition = versionAdapter.selectPosition
                    }
                }
            }
        }
        gradeRecycler.layoutManager = LinearLayoutManager(mContext)
        gradeRecycler.adapter = subjectAdapter
        subjectAdapter.setOnItemClickListener { holder, view, position ->
            if (subjectAdapter.selectPosition != position)
                subjectAdapter.selectPosition = position
        }


        versionRecycler.layoutManager = LinearLayoutManager(mContext)
        versionRecycler.adapter = versionAdapter
        versionAdapter.setOnItemClickListener { holder, view, position ->
            versionAdapter.selectPosition = position
            onConfirmClick?.invoke(versionName, versionId, semesterCode, semesterName, subCode, subName)
            dismiss()
        }
        stateView.setBackgroundRes(R.color.white)
        stateView.setOnRetryClickListener {
            EBagApi.readRecordVersion(classId, requestSubCode, versionRequest)
        }
    }

    fun setRequest(classId: String?, subCode: String? = null){
        if (this.classId != classId){
            this.classId = classId
            this.requestSubCode = subCode
            EBagApi.readRecordVersion(classId, subCode, versionRequest)
        }
    }

    inner class SubjectAdapter: BaseQuickAdapter<ReadRecordVersionBean.SubjectBean, BaseViewHolder>(R.layout.item_assign_popup){
        var selectPosition = -1
            set(value) {
                field = value
                versionAdapter.selectPosition = -1
                val list = data[selectPosition].bookVersionVoList
                if (list != null && !list.isEmpty()){
                    versionAdapter.removeAllHeaderView()
                }
                versionAdapter.setNewData(data[selectPosition].bookVersionVoList)
                subCode = data[selectPosition].subjectCode
                subName = data[selectPosition].subjectName
                notifyDataSetChanged()
            }
        override fun convert(setter: BaseViewHolder, entity: ReadRecordVersionBean.SubjectBean) {
            val gradeTv = setter.getView<TextView>(R.id.tv)
            gradeTv.text = entity.subjectName
            gradeTv.isSelected = selectPosition != -1 && selectPosition == setter.adapterPosition
        }
    }
    inner class VersionAdapter : BaseQuickAdapter<ReadRecordVersionBean.SubjectBean.BookVersionVoListBean, BaseViewHolder>(R.layout.item_assign_popup){
        var selectPosition = -1
            set(value) {
                field = value
                if(selectPosition != -1 && data.isNotEmpty()) {
                    versionId = data[selectPosition].bookVersionId
                    versionName = data[selectPosition].versionName
                    versionCode = data[selectPosition].versionCode
                }else{
                    versionName = ""
                }
                notifyDataSetChanged()
            }
        override fun convert(setter: BaseViewHolder, entity: ReadRecordVersionBean.SubjectBean.BookVersionVoListBean) {
            val versionTv = setter.getView<TextView>(R.id.tv)
            versionTv.text = entity.versionName
            versionTv.isSelected = selectPosition != -1 && selectPosition == setter.adapterPosition
        }
    }
}