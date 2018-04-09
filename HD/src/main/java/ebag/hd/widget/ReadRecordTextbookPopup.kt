package ebag.hd.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.widget.RadioGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.core.widget.empty.StateView
import ebag.hd.R
import ebag.hd.bean.ReadRecordVersionBean
import ebag.hd.http.EBagApi

/**
 * Created by YZY on 2018/4/9.
 */
class ReadRecordTextbookPopup(context: Context): PopupWindow(context)  {
    private var stateView: StateView? = null
    private val versionRequest = object : RequestCallBack<ReadRecordVersionBean>(){
        override fun onStart() {
            stateView?.showLoading()
        }

        override fun onSuccess(entity: ReadRecordVersionBean?) {
            stateView?.showContent()
            if (entity == null) {
                stateView?.showEmpty()
                return
            }
            addCourseTextbookBean = entity
            if (entity.first.isEmpty() && entity.next.isEmpty()){
                stateView?.showEmpty()
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
            stateView?.showError(exception.message.toString())
        }
    }
    private var subCode = ""
    private var subName = ""
    private var versionId = ""
    private var versionName = ""
    private var versionCode = ""
    private var semesterCode = "1"
    private var semesterName = "上学期"
    var onConfirmClick: ((
            versionId: String,
            versionName: String,
            semesterName: String,
            subName: String
    ) -> Unit)? = null
    private var addCourseTextbookBean = ReadRecordVersionBean()
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
    private var classId = ""
    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.popup_read_record_textbook, null)
        width = context.resources.getDimensionPixelSize(R.dimen.x400)
        height = context.resources.getDimensionPixelSize(R.dimen.y400)
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable())

        val semesterGroup = contentView.findViewById<RadioGroup>(R.id.semesterGroup)
        val confirmBtn = contentView.findViewById<TextView>(R.id.confirmBtn)
        val gradeRecycler = contentView.findViewById<RecyclerView>(R.id.gradeRecycler)
        val versionRecycler = contentView.findViewById<RecyclerView>(R.id.versionRecycler)
        stateView = contentView.findViewById(R.id.stateView)
        stateView?.setOnRetryClickListener {
            EBagApi.readRecordVersion(classId, versionRequest)
        }
        semesterGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.semesterFirst ->{
                    semesterCode = "1"
                    semesterName = "上学期"
                    val firstVo = addCourseTextbookBean.first
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
                    val nextVo = addCourseTextbookBean.next
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

        confirmBtn.setOnClickListener {
            if (StringUtils.isEmpty(versionName)){
                T.show(context, "请选择版本")
                return@setOnClickListener
            }
            onConfirmClick?.invoke(versionId,versionName, semesterName, subName)
            dismiss()
        }
        gradeRecycler.layoutManager = LinearLayoutManager(context)
        gradeRecycler.adapter = subjectAdapter
        subjectAdapter.setOnItemClickListener { holder, view, position ->
            if (subjectAdapter.selectPosition != position)
                subjectAdapter.selectPosition = position
        }


        versionRecycler.layoutManager = LinearLayoutManager(context)
        versionRecycler.adapter = versionAdapter
        versionAdapter.setOnItemClickListener { holder, view, position ->
            if (versionAdapter.selectPosition != position)
                versionAdapter.selectPosition = position
        }
    }

    fun setRequest(classId: String){
        if (this.classId != classId){
            this.classId = classId
            EBagApi.readRecordVersion(classId, versionRequest)
        }
    }

    inner class SubjectAdapter: BaseQuickAdapter<ReadRecordVersionBean.SubjectBean, BaseViewHolder>(R.layout.item_textbook_grade){
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
            val gradeTv = setter.getView<TextView>(R.id.gradeTv)
            gradeTv.text = entity.subjectName
            gradeTv.isSelected = selectPosition != -1 && selectPosition == setter.adapterPosition
        }
    }
    inner class VersionAdapter : BaseQuickAdapter<ReadRecordVersionBean.SubjectBean.BookVersionVoListBean, BaseViewHolder>(R.layout.item_textbook_version){
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
            val versionTv = setter.getView<TextView>(R.id.versionTv)
            versionTv.text = entity.versionName
            versionTv.isSelected = selectPosition != -1 && selectPosition == setter.adapterPosition
        }
    }
}