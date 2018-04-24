package com.yzy.ebag.teacher.module.prepare

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.PrepareSubjectBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BasePopupWindow
import ebag.core.http.network.RequestCallBack
import ebag.core.widget.empty.StateView

/**
 * Created by YZY on 2018/4/24.
 */
class PrepareSubjectPopup(private val mContext: Context): BasePopupWindow(mContext) {
    override fun getLayoutRes(): Int = R.layout.popup_prepare_subject

    override fun setHeight(): Int = contentView.resources.getDimensionPixelSize(R.dimen.y500)
    override fun setWidth(): Int = WindowManager.LayoutParams.MATCH_PARENT

    private val gradeRecycler = contentView.findViewById<RecyclerView>(R.id.gradeRecycler)
    private val subjectRecycler = contentView.findViewById<RecyclerView>(R.id.subjectRecycler)
    private val stateView = contentView.findViewById<StateView>(R.id.stateView)

    private lateinit var subjectAdapter: SubjectAdapter
    private lateinit var gradeAdapter: GradeAdapter
    private var gradeCode = "1"
    private var gradeName = "一年级"
    private var subjectCode = ""
    private var subjectName = ""
    private var lessonType = ""
    var onSubjectSelectListener: ((gradeCode: String, gradeName: String, subjectCode: String, subjectName: String) -> Unit)? = null
    private val request = object : RequestCallBack<List<PrepareSubjectBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }
        override fun onSuccess(entity: List<PrepareSubjectBean>?) {
            stateView.showContent()
            if (entity == null || entity.isEmpty()){
                stateView.showError("暂无科目信息")
                return
            }
            gradeAdapter.setNewData(entity)
            gradeAdapter.currentGradeBean = entity[0]
            gradeCode = entity[0].gradeCode ?: ""
            gradeName = entity[0].gradeName ?: ""
            subjectAdapter.setNewData(entity[0].subjects)
        }

        override fun onError(exception: Throwable) {
            stateView.showError(exception.message.toString())
        }

    }
    init {
        gradeRecycler.layoutManager = LinearLayoutManager(mContext)

        gradeAdapter = GradeAdapter()
        gradeRecycler.adapter = gradeAdapter
        subjectRecycler.layoutManager = LinearLayoutManager(mContext)

        subjectAdapter = SubjectAdapter()
        subjectRecycler.adapter = subjectAdapter
        gradeAdapter.setOnItemClickListener { adapter, view, position ->
            adapter as GradeAdapter
            val bean = adapter.data[position]
            gradeCode = bean.gradeCode
            gradeName = bean.gradeName
            adapter.currentGradeBean = bean
            subjectAdapter.setNewData(bean.subjects)
        }
        subjectAdapter.setOnItemClickListener { adapter, view, position ->
            adapter as SubjectAdapter
            val bean = adapter.data[position]
            subjectCode = bean.subCode
            subjectName = bean.subName
            adapter.currentSubjectBean = bean
            onSubjectSelectListener?.invoke(gradeCode, gradeName, subjectCode, subjectName)
            dismiss()
        }
    }

    fun requestData(lessonType: String){
        if (this.lessonType != lessonType) {
            this.lessonType = lessonType
            TeacherApi.prepareSubject(lessonType, request)
        }
    }

    inner class GradeAdapter : BaseQuickAdapter<PrepareSubjectBean, BaseViewHolder>(R.layout.item_assign_popup) {
        var currentGradeBean : PrepareSubjectBean? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: PrepareSubjectBean?) {
            val textView = helper.getView<TextView>(R.id.tv)
            textView.text = item?.gradeName
            textView.isSelected = currentGradeBean == item
        }
    }

    inner class SubjectAdapter: BaseQuickAdapter<PrepareSubjectBean.SubjectsBean, BaseViewHolder>(R.layout.item_assign_popup){
        var currentSubjectBean: PrepareSubjectBean.SubjectsBean? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: PrepareSubjectBean.SubjectsBean) {
            val textView = helper.getView<TextView>(R.id.tv)
            textView.text = item.subName
            textView.isSelected = currentSubjectBean == item
        }
    }
}