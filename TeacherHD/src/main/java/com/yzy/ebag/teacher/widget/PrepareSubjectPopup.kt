package com.yzy.ebag.teacher.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.BaseSubjectBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.T
import java.io.Serializable

/**
 * Created by YZY on 2018/3/5.
 */
class PrepareSubjectPopup(context: Context): PopupWindow(context) {
    private val gradeList by lazy {
        val list = ArrayList<MyGradeBean>()
        val gradeBean1 = MyGradeBean()
        gradeBean1.gradeCode = "1"
        gradeBean1.gradeName = "一年级"
        val gradeBean2 = MyGradeBean()
        gradeBean2.gradeCode = "2"
        gradeBean2.gradeName = "二年级"
        val gradeBean3 = MyGradeBean()
        gradeBean3.gradeCode = "3"
        gradeBean3.gradeName = "三年级"
        val gradeBean4 = MyGradeBean()
        gradeBean4.gradeCode = "4"
        gradeBean4.gradeName = "四年级"
        val gradeBean5 = MyGradeBean()
        gradeBean5.gradeCode = "5"
        gradeBean5.gradeName = "五年级"
        val gradeBean6 = MyGradeBean()
        gradeBean6.gradeCode = "6"
        gradeBean6.gradeName = "六年级"
        val gradeBean7 = MyGradeBean()
        gradeBean7.gradeCode = "7"
        gradeBean7.gradeName = "七年级"
        val gradeBean8 = MyGradeBean()
        gradeBean8.gradeCode = "8"
        gradeBean8.gradeName = "八年级"
        val gradeBean9 = MyGradeBean()
        gradeBean9.gradeCode = "9"
        gradeBean9.gradeName = "九年级"
        list.add(gradeBean1)
        list.add(gradeBean2)
        list.add(gradeBean3)
        list.add(gradeBean4)
        list.add(gradeBean5)
        list.add(gradeBean6)
        list.add(gradeBean7)
        list.add(gradeBean8)
        list.add(gradeBean9)
        list
    }
    private lateinit var subjectAdapter: SubjectAdapter
    private var gradeCode = "1"
    private var gradeName = "一年级"
    private var subjectCode = ""
    private var subjectName = ""
    var onSubjectSelectListener: ((gradeCode: String, gradeName: String, subjectCode: String, subjectName: String) -> Unit)? = null
    private val subjectRequest = object : RequestCallBack<List<BaseSubjectBean>>(){
        override fun onSuccess(entity: List<BaseSubjectBean>?) {
            if (entity == null || entity.isEmpty()){
                T.show(context, "暂无科目信息")
                return
            }
            subjectAdapter.setNewData(entity)
            subjectAdapter.currentSubjectBean = null
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(context)
        }

    }
    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.prepare_subject_popup, null)
        width = context.resources.getDimensionPixelSize(R.dimen.x400)
        height = context.resources.getDimensionPixelSize(R.dimen.y300)
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable())

        val gradeRecycler = contentView.findViewById<RecyclerView>(R.id.gradeRecycler)
        val subjectRecycler = contentView.findViewById<RecyclerView>(R.id.subjectRecycler)
        gradeRecycler.layoutManager = LinearLayoutManager(context)

        val gradeAdapter = GradeAdapter()
        gradeRecycler.adapter = gradeAdapter
        subjectRecycler.layoutManager = LinearLayoutManager(context)

        subjectAdapter = SubjectAdapter()
        subjectRecycler.adapter = subjectAdapter
        gradeAdapter.setNewData(gradeList)
        gradeAdapter.currentGradeBean = gradeList[0]
        gradeAdapter.setOnItemClickListener { adapter, view, position ->
            adapter as GradeAdapter
            val bean = adapter.data[position]
            gradeCode = bean.gradeCode
            gradeName = bean.gradeName
            adapter.currentGradeBean = bean
        }
        subjectAdapter.setOnItemClickListener { adapter, view, position ->
            adapter as SubjectAdapter
            val bean = adapter.data[position]
            subjectCode = bean.keyCode
            subjectName = bean.keyValue
            adapter.currentSubjectBean = bean
            onSubjectSelectListener?.invoke(gradeCode, gradeName, subjectCode, subjectName)
            dismiss()
        }
    }

    inner class GradeAdapter : BaseQuickAdapter<MyGradeBean, BaseViewHolder>(R.layout.item_prepare_grade) {
        var currentGradeBean : MyGradeBean? = null
        set(value) {
            field = value
            notifyDataSetChanged()
            TeacherApi.getSubject(currentGradeBean!!.gradeCode, subjectRequest)
        }
        override fun convert(helper: BaseViewHolder, item: MyGradeBean) {
            val textView = helper.getView<TextView>(R.id.text)
            textView.text = item.gradeName
            textView.isSelected = currentGradeBean == item
        }
    }

    inner class SubjectAdapter: BaseQuickAdapter<BaseSubjectBean, BaseViewHolder>(R.layout.item_prepare_subject){
        var currentSubjectBean: BaseSubjectBean? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: BaseSubjectBean) {
            val textView = helper.getView<TextView>(R.id.text)
            textView.text = item.keyValue
            textView.isSelected = currentSubjectBean == item
            helper.getView<View>(R.id.dot).isSelected = currentSubjectBean == item
        }
    }
    inner class MyGradeBean: Serializable {
        var gradeCode = ""
        var gradeName = ""
    }
}