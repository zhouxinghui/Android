package com.yzy.ebag.teacher.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.AssignClassBean
import com.yzy.ebag.teacher.bean.BookVersionBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.DensityUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder
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
    private val versionRequest = object: RequestCallBack<List<BookVersionBean>>(){
            override fun onStart() {
                stateView.showLoading()
            }

            override fun onSuccess(entity: List<BookVersionBean>?) {
                if (entity == null || entity.isEmpty()){
                    stateView.showEmpty()
                    return
                }
                stateView.showContent()
                gradeAdapter.datas = entity
                gradeAdapter.selectPosition = 0
                subCode = entity[0].subjectCode
                subName = entity[0].subjectName
            }

            override fun onError(exception: Throwable) {
                stateView.showError()
                exception.handleThrowable(context)
            }
    }
    private var subCode = ""
    private var subName = ""
    private var versionCode = ""
    private var versionName = ""
    private var semesterCode = "1"
    private var semesterName = "上学期"
    var onConfirmClick: ((
            versionName: String,
            versionCode: String,
            semesterCode: String,
            semesterName: String,
            subCode: String,
            subName: String) -> Unit)? = null
    private val gradeAdapter = GradeAdapter()
    private val versionAdapter = VersionAdapter()
    private var idList: ArrayList<String>? = null
    init {
        semesterGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.semesterFirst ->{
                    semesterCode = "1"
                    semesterName = "上学期"
                }
                R.id.semesterSecond ->{
                    semesterCode = "2"
                    semesterName = "下学期"
                }
            }
        }

        confirmBtn.setOnClickListener {
            if (StringUtils.isEmpty(versionName)){
                T.show(context, "请选择版本")
                return@setOnClickListener
            }
            onConfirmClick?.invoke(versionName, versionCode, semesterCode, semesterName, subCode, subName)
            dismiss()
        }
        gradeRecycler.layoutManager = LinearLayoutManager(context)
        gradeRecycler.adapter = gradeAdapter
        gradeAdapter.setOnItemClickListener { holder, view, position ->
            gradeAdapter.selectPosition = position
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
        TeacherApi.searchBookVersion(idList, versionRequest)
        super.show()
    }

    inner class GradeAdapter: RecyclerAdapter<BookVersionBean>(R.layout.item_textbook_grade){
        var selectPosition = -1
        set(value) {
            field = value
            versionAdapter.datas = datas[selectPosition].bookVersionVoList
            notifyDataSetChanged()
        }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: BookVersionBean) {
            val gradeTv = setter.getTextView(R.id.gradeTv)
            gradeTv.text = entity.subjectName
            gradeTv.isSelected = selectPosition != -1 && selectPosition == position
        }
    }
    inner class VersionAdapter : RecyclerAdapter<BookVersionBean.BookVersionVoListBean>(R.layout.item_textbook_version){
        var selectPosition = -1
            set(value) {
                field = value
                versionCode = versionAdapter.datas[selectPosition].versionCode
                versionName = versionAdapter.datas[selectPosition].versionName
                notifyDataSetChanged()
            }
        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: BookVersionBean.BookVersionVoListBean) {
            val versionTv = setter.getTextView(R.id.versionTv)
            versionTv.text = entity.versionName
            versionTv.isSelected = selectPosition != -1 && selectPosition == position
        }

    }
}