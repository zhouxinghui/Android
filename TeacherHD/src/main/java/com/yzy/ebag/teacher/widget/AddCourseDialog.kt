package com.yzy.ebag.teacher.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.AddCourseTextbookBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.DensityUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_textbook_version.*

/**
 * Created by YZY on 2018/3/20.
 */
class AddCourseDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_textbook_version
    }

    override fun setWidth(): Int {
        return DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.x400).toFloat())
    }

    override fun setHeight(): Int {
        return DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.y400).toFloat())
    }
    private var subCode = ""
    private var subName = ""
    private var versionId = ""
    private var versionName = ""
    private var versionCode = ""
    private var semesterCode = "1"
    private var semesterName = "上学期"
    private var gradeCode = "1"
    private var classId = ""
    private var addCourseTextbookBean = AddCourseTextbookBean()
    var onAddCourseSuccess: (() -> Unit)? = null
    private val addCourseRequest = object: RequestCallBack<String>(){
        override fun onStart() {
            T.showLong(context, "正在添加...")
        }
        override fun onSuccess(entity: String?) {
            T.show(context, "添加成功")
            onAddCourseSuccess?.invoke()
            dismiss()
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(context)
        }
    }
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
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.text = "暂无数据"
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.x24))
        textView
    }
    private val versionEmptyHead by lazy {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.text = "暂无数据"
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.x24))
        textView
    }
    private val versionRequest = object : RequestCallBack<AddCourseTextbookBean>(){
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: AddCourseTextbookBean?) {
            stateView.showContent()
            if (entity == null) {
                stateView.showEmpty()
                return
            }
            addCourseTextbookBean = entity
            if (entity.firstvo.isEmpty() && entity.nextvo.isEmpty()){
                stateView.showEmpty()
                return
            }
            if (!entity.firstvo.isEmpty()){
                subCode = entity.firstvo[0].subjectCode
                subName = entity.firstvo[0].subjectName
                subjectAdapter.setNewData(entity.firstvo)
                subjectAdapter.selectPosition = 0
                subjectAdapter.removeAllHeaderView()
                val versionList = entity.firstvo[0].bookVersions
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
    init {
        semesterGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.semesterFirst ->{
                    semesterCode = "1"
                    semesterName = "上学期"
                    val firstVo = addCourseTextbookBean.firstvo
                    if (firstVo.isEmpty()){
                        subjectAdapter.addHeaderView(subjectEmptyHead)
                        subjectAdapter.setNewData(firstVo)
                        versionAdapter.addHeaderView(versionEmptyHead)
                        versionAdapter.setNewData(null)
                    }else{
                        subjectAdapter.removeAllHeaderView()
                        subjectAdapter.setNewData(firstVo)
                        val versionList = firstVo[0].bookVersions
                        if (versionList == null || versionList.isEmpty()){
                            versionAdapter.addHeaderView(versionEmptyHead)
                        }else{
                            versionAdapter.removeAllHeaderView()
                        }
                        versionAdapter.setNewData(versionList)
                    }
                }
                R.id.semesterSecond ->{
                    semesterCode = "2"
                    semesterName = "下学期"
                    val nextVo = addCourseTextbookBean.nextvo
                    if (nextVo.isEmpty()){
                        subjectAdapter.addHeaderView(subjectEmptyHead)
                        subjectAdapter.setNewData(nextVo)
                        versionAdapter.addHeaderView(versionEmptyHead)
                        versionAdapter.setNewData(null)
                    }else{
                        subjectAdapter.removeAllHeaderView()
                        subjectAdapter.setNewData(nextVo)
                        val versionList = nextVo[0].bookVersions
                        if (versionList.isEmpty()){
                            versionAdapter.addHeaderView(versionEmptyHead)
                        }else{
                            versionAdapter.removeAllHeaderView()
                        }
                        versionAdapter.setNewData(versionList)
                    }
                }
            }
        }

        gradeRecycler.layoutManager = LinearLayoutManager(context)
        gradeRecycler.adapter = subjectAdapter
        subjectAdapter.setOnItemClickListener { _, view, position ->
            subjectAdapter.selectPosition = position
        }

        versionRecycler.layoutManager = LinearLayoutManager(context)
        versionRecycler.adapter = versionAdapter
        versionAdapter.setOnItemClickListener { _, view, position ->
            versionAdapter.selectPosition = position
        }

        confirmBtn.setOnClickListener {
            if (StringUtils.isEmpty(versionName)){
                T.show(context, "请选择版本")
                return@setOnClickListener
            }
            TeacherApi.addCourse(classId, versionId, versionCode, versionName, subCode, subName, semesterCode, semesterName, gradeCode, addCourseRequest)
        }
    }
    fun show(classId: String, gradeCode: String){
        this.gradeCode = gradeCode
        this.classId = classId
        if (addCourseTextbookBean.firstvo == null)
            TeacherApi.courseVersionData(classId, versionRequest)
        super.show()
    }
    inner class SubjectAdapter: BaseQuickAdapter<AddCourseTextbookBean.SubjectBean, BaseViewHolder>(R.layout.item_textbook_grade){
        var selectPosition = -1
            set(value) {
                field = value
                val list = data[selectPosition].bookVersions
                if (list == null || list.isEmpty()){
                    versionAdapter.addHeaderView(versionEmptyHead)
                }else{
                    versionAdapter.removeAllHeaderView()
                }
                versionAdapter.setNewData(data[selectPosition].bookVersions)
                subCode = data[selectPosition].subjectCode
                subName = data[selectPosition].subjectName
                notifyDataSetChanged()
            }
        override fun convert(setter: BaseViewHolder, entity: AddCourseTextbookBean.SubjectBean) {
            val gradeTv = setter.getView<TextView>(R.id.gradeTv)
            gradeTv.text = entity.subjectName
            gradeTv.isSelected = selectPosition != -1 && selectPosition == setter.adapterPosition
        }
    }
    inner class VersionAdapter : BaseQuickAdapter<AddCourseTextbookBean.SubjectBean.BookVersionsBean, BaseViewHolder>(R.layout.item_textbook_version){
        var selectPosition = -1
            set(value) {
                field = value
                versionId = data[selectPosition].bookVersionId
                versionName = data[selectPosition].versionName
                versionCode = data[selectPosition].versionCode
                notifyDataSetChanged()
            }
        override fun convert(setter: BaseViewHolder, entity: AddCourseTextbookBean.SubjectBean.BookVersionsBean) {
            val versionTv = setter.getView<TextView>(R.id.versionTv)
            versionTv.text = entity.versionName
            versionTv.isSelected = selectPosition != -1 && selectPosition == setter.adapterPosition
        }
    }
}