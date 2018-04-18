package com.yzy.ebag.teacher.module.homework

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
import com.yzy.ebag.teacher.bean.AssignClassBean
import com.yzy.ebag.teacher.bean.BookVersionBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BasePopupWindow
import ebag.core.http.network.RequestCallBack
import ebag.core.widget.empty.StateView

/**
 * Created by YZY on 2018/4/18.
 */
class ExchangeVersionPopup(private val mContext: Context): BasePopupWindow(mContext) {
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
                        val versionList = firstVo[subjectAdapter.selectPosition].bookVersionVoList
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
                    val nextVo = addCourseTextbookBean.nextVo
                    if (nextVo.isEmpty()){
                        subjectAdapter.addHeaderView(subjectEmptyHead)
                        subjectAdapter.setNewData(nextVo)
                        versionAdapter.addHeaderView(versionEmptyHead)
                        versionAdapter.setNewData(null)
                    }else{
                        subjectAdapter.removeAllHeaderView()
                        subjectAdapter.setNewData(nextVo)
                        val versionList = nextVo[subjectAdapter.selectPosition].bookVersionVoList
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

        stateView.setOnRetryClickListener {
            TeacherApi.searchBookVersion(idList!!, versionRequest)
        }
    }

    fun setData(list: List<AssignClassBean>){
        val idList = ArrayList<String>()
        list.forEach { idList.add(it.classId) }
        if (this.idList != null && this.idList!!.size == idList.size && this.idList!!.containsAll(idList)){
            return
        }
        this.idList = idList
        TeacherApi.searchBookVersion(idList, versionRequest)
    }

    inner class SubjectAdapter: BaseQuickAdapter<BookVersionBean.SubjectBean, BaseViewHolder>(R.layout.item_assign_popup){
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
        override fun convert(setter: BaseViewHolder, entity: BookVersionBean.SubjectBean) {
            val gradeTv = setter.getView<TextView>(R.id.tv)
            gradeTv.text = entity.subjectName
            gradeTv.isSelected = selectPosition != -1 && selectPosition == setter.adapterPosition
        }
    }
    inner class VersionAdapter : BaseQuickAdapter<BookVersionBean.SubjectBean.BookVersionVoListBean, BaseViewHolder>(R.layout.item_assign_popup){
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
        override fun convert(setter: BaseViewHolder, entity: BookVersionBean.SubjectBean.BookVersionVoListBean) {
            val versionTv = setter.getView<TextView>(R.id.tv)
            versionTv.text = entity.versionName
            versionTv.isSelected = selectPosition != -1 && selectPosition == setter.adapterPosition
        }
    }
}