package com.yzy.ebag.teacher.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.AssignClassBean
import com.yzy.ebag.teacher.bean.BookVersionBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.util.DensityUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import kotlinx.android.synthetic.main.dialog_textbook_version.*

/**
 * Created by YZY on 2018/1/10.
 */
class ExchangeTextbookDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_textbook_version
    }

    override fun setWidth(): Int {
        return DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.x400).toFloat())
    }

    override fun setHeight(): Int {
        return DensityUtil.dip2px(context, context.resources.getDimensionPixelSize(R.dimen.y400).toFloat())
    }
    private val versionRequest = object : RequestCallBack<BookVersionBean>(){
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: BookVersionBean?) {
            stateView.showContent()
            if (entity == null) {
                stateView.showEmpty()
                return
            }
            addCourseTextbookBean = entity
            if (entity.firstVo.isEmpty() && entity.nextVo.isEmpty()){
                stateView.showEmpty()
                return
            }
            if (!entity.firstVo.isEmpty()){
                subCode = entity.firstVo[0].subjectCode
                subName = entity.firstVo[0].subjectName
                subjectAdapter.setNewData(entity.firstVo)
                subjectAdapter.selectPosition = 0
                subjectAdapter.removeAllHeaderView()
                val versionList = entity.firstVo[0].bookVersionVoList
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
    private var addCourseTextbookBean = BookVersionBean()
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
    private var idList: ArrayList<String>? = null
    init {
        semesterGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.semesterFirst ->{
                    semesterCode = "1"
                    semesterName = "上学期"
                    val firstVo = addCourseTextbookBean.firstVo
                    if (firstVo.isEmpty()){
                        subjectAdapter.addHeaderView(subjectEmptyHead)
                        subjectAdapter.setNewData(firstVo)
                        versionAdapter.addHeaderView(versionEmptyHead)
                        versionAdapter.setNewData(null)
                    }else{
                        subjectAdapter.removeAllHeaderView()
                        subjectAdapter.setNewData(firstVo)
                        val versionList = firstVo[0].bookVersionVoList
                        if (versionList.isEmpty()){
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
                    val nextVo = addCourseTextbookBean.nextVo
                    if (nextVo.isEmpty()){
                        subjectAdapter.addHeaderView(subjectEmptyHead)
                        subjectAdapter.setNewData(nextVo)
                        versionAdapter.addHeaderView(versionEmptyHead)
                        versionAdapter.setNewData(null)
                    }else{
                        subjectAdapter.removeAllHeaderView()
                        subjectAdapter.setNewData(nextVo)
                        val versionList = nextVo[0].bookVersionVoList
                        if (versionList == null || versionList.isEmpty()){
                            versionAdapter.addHeaderView(versionEmptyHead)
                        }else{
                            versionAdapter.removeAllHeaderView()
                        }
                        versionAdapter.setNewData(versionList)
                    }
                }
            }
        }

        confirmBtn.setOnClickListener {
            if (StringUtils.isEmpty(versionName)){
                T.show(context, "请选择版本")
                return@setOnClickListener
            }
            onConfirmClick?.invoke(versionName, versionId, semesterCode, semesterName, subCode, subName)
            dismiss()
        }
        gradeRecycler.layoutManager = LinearLayoutManager(context)
        gradeRecycler.adapter = subjectAdapter
        subjectAdapter.setOnItemClickListener { holder, view, position ->
            subjectAdapter.selectPosition = position
        }


        versionRecycler.layoutManager = LinearLayoutManager(context)
        versionRecycler.adapter = versionAdapter
        versionAdapter.setOnItemClickListener { holder, view, position ->
            versionAdapter.selectPosition = position
        }

        stateView.setOnRetryClickListener {
            TeacherApi.searchBookVersion(idList!!, versionRequest)
        }
    }

    fun show(list: List<AssignClassBean>) {
        val idList = ArrayList<String>()
        if (list.isEmpty()){
            T.show(context, "请选择班级")
            return
        }
        list.forEach { idList.add(it.classId) }
        this.idList = idList
        if (addCourseTextbookBean.firstVo == null)
            TeacherApi.searchBookVersion(idList, versionRequest)
        super.show()
    }

    inner class SubjectAdapter: BaseQuickAdapter<BookVersionBean.SubjectBean, BaseViewHolder>(R.layout.item_textbook_grade){
        var selectPosition = -1
            set(value) {
                field = value
                val list = data[selectPosition].bookVersionVoList
                if (list != null && !list.isEmpty()){
                    versionAdapter.removeAllHeaderView()
                }
                versionAdapter.setNewData(data[selectPosition].bookVersionVoList)
                subCode = data[selectPosition].subjectCode
                subName = data[selectPosition].subjectName
                notifyDataSetChanged()
            }
        override fun convert(setter: BaseViewHolder, entity: BookVersionBean.SubjectBean) {
            val gradeTv = setter.getView<TextView>(R.id.gradeTv)
            gradeTv.text = entity.subjectName
            gradeTv.isSelected = selectPosition != -1 && selectPosition == setter.adapterPosition
        }
    }
    inner class VersionAdapter : BaseQuickAdapter<BookVersionBean.SubjectBean.BookVersionVoListBean, BaseViewHolder>(R.layout.item_textbook_version){
        var selectPosition = -1
            set(value) {
                field = value
                versionId = data[selectPosition].bookVersionId
                versionName = data[selectPosition].versionName
                versionCode = data[selectPosition].versionCode
                notifyDataSetChanged()
            }
        override fun convert(setter: BaseViewHolder, entity: BookVersionBean.SubjectBean.BookVersionVoListBean) {
            val versionTv = setter.getView<TextView>(R.id.versionTv)
            versionTv.text = entity.versionName
            versionTv.isSelected = selectPosition != -1 && selectPosition == setter.adapterPosition
        }
    }
}