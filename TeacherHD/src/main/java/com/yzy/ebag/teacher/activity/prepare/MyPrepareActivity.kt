package com.yzy.ebag.teacher.activity.prepare

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import cn.jzvd.JZUtils
import cn.jzvd.JZVideoPlayer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.PrepareBaseBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.widget.PrepareSubjectPopup
import com.yzy.ebag.teacher.widget.PrepareTextbookPopup
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.hd.adapter.UnitAdapter
import ebag.hd.base.BaseListTabActivity
import ebag.hd.bean.UnitBean

class MyPrepareActivity : BaseListTabActivity<PrepareBaseBean, MultiItemEntity>() {
    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context, MyPrepareActivity::class.java))
        }
    }
    private var type = "1"
    private lateinit var subjectTv : TextView
    private lateinit var textBookTv : TextView
    private lateinit var totalText: View
    private var parent: PrepareBaseBean? = null
    private var unitId = ""
    private var gradeCode = ""
    private var subjectCode = ""
    private var semesterCode = ""
    private var textbookCode = ""
    private val unitRequest = object : RequestCallBack<List<UnitBean>>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(this@MyPrepareActivity)
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
            adapter.setNewData(entity)
            fragment.notifyRequest(type, gradeCode, subjectCode, null)
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(this@MyPrepareActivity, "网络请求失败！")
        }

    }
    private val subjectPopup by lazy {
        val popup = PrepareSubjectPopup(this)
        popup.onSubjectSelectListener = { gradeCode, gradeName, subjectCode, SubjectName ->
            this.gradeCode = gradeCode
            this.subjectCode = subjectCode
            subjectTv.text = "$gradeName $SubjectName"
            textBookTv.text = ""
            adapter.setNewData(ArrayList())
            fragment.notifyRequest(type, gradeCode, subjectCode, null)
        }
        popup
    }
    private val textbookPopup by lazy {
        val popup = PrepareTextbookPopup(this)
        popup.onTextbookSelectListener = {versionBean, semesterCode, semesterName ->
            textBookTv.text = "${versionBean.bookVersionName} $semesterName"
            TeacherApi.prepareUnit(versionBean.bookVersionId, unitRequest)
        }
        popup
    }
    override fun loadConfig() {
        titleBar.setTitle("教学课件")
        setLeftWidth(resources.getDimensionPixelSize(R.dimen.x368))

        val textBookView = layoutInflater.inflate(R.layout.layout_prepare_textbook_head, null)
        val subjectView = layoutInflater.inflate(R.layout.layout_prepare_subject_head, null)
        subjectTv = subjectView.findViewById(R.id.subjectTv)
        subjectTv.setOnClickListener { //更换科目
            subjectPopup.requestData(type)
            subjectPopup.showAsDropDown(titleBar, subjectView.width + 1, 0)
        }

        textBookTv = textBookView.findViewById(R.id.textBookVersion)
        textBookTv.setOnClickListener { //更换教材
            textbookPopup.requestData(gradeCode, type, subjectCode)
            textbookPopup.showAsDropDown(subjectView, textBookView.width + 1, 0)
        }

        totalText = layoutInflater.inflate(R.layout.unit_total_text, null)
        totalText.isSelected = true
        totalText.setOnClickListener {
            if (!totalText.isSelected){
                adapter.selectSub = null
                totalText.isSelected = true
                fragment.notifyRequest(type, gradeCode, subjectCode, null)
            }
        }

        addLeftHeaderView(totalText)
        addLeftHeaderView(textBookView)
        addLeftHeaderView(subjectView)

        val titleView = layoutInflater.inflate(R.layout.prepare_title_layout, null)
        val titleGroup = titleView.findViewById<RadioGroup>(R.id.titleGroup)
        titleGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.schoolResource ->{
                    type = "2"
                }
                R.id.shareResource ->{
                    type = "3"
                }
            }
            request()
        }
        val schoolResourceBtn = titleView.findViewById<RadioButton>(R.id.schoolResource)
        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        titleView.layoutParams = params
        titleBar.addView(titleView)
        titleView.visibility = View.GONE
        titleBar.setRightText("资源库", {
            if (titleView.visibility == View.GONE) {
                titleView.visibility = View.VISIBLE
                titleBar.setRightText("课件")
                titleBar.setTitle("")
                type = if (type == "1" && schoolResourceBtn.isChecked)
                    "2"
                else
                    "3"
            }else{
                titleView.visibility = View.GONE
                titleBar.setRightText("资源库")
                titleBar.setTitle("教学课件")
                type = "1"
            }
            request()
        })
    }

    override fun requestData(requestCallBack: RequestCallBack<PrepareBaseBean>) {
        TeacherApi.prepareBaseData(type, requestCallBack)
    }

    override fun parentToList(parent: PrepareBaseBean?): List<MultiItemEntity>? {
        this.parent = parent
        gradeCode = parent?.resultSubjectVo!!.gradeCode
        subjectCode = parent.resultSubjectVo.subCode ?: ""
        subjectTv.text = "${parent?.resultSubjectVo?.gradeName} ${parent?.resultSubjectVo?.subject}"

        textBookTv.text = "${parent?.resultSubjectVo?.versionName} ${parent?.resultSubjectVo?.semesterName}"
        semesterCode = parent.resultSubjectVo.semesterCode
        textbookCode = parent.resultSubjectVo.versionCode
        return parent.resultBookUnitOrCatalogVos
    }

    /*override fun firstPageDataLoad(result: List<MultiItemEntity>) {
        super.firstPageDataLoad(result)
        if (adapter.itemCount > 0) {
            try {
                val position = (0 until adapter.itemCount).first { adapter.getItem(it) is UnitBean }
                adapter.selectSub = (adapter.getItem(position) as UnitBean).resultBookUnitOrCatalogVos[0]
                adapter.expand(position)
            }catch (e: Exception){

            }
        }
    }*/

    private lateinit var adapter: UnitAdapter
    override fun getLeftAdapter(): BaseQuickAdapter<MultiItemEntity, BaseViewHolder> {
        adapter = UnitAdapter()
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    private lateinit var fragment: PrepareFragment
    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Fragment {
        fragment = PrepareFragment.newInstance(parent?.lessonFileInfoVos, type)
        return fragment
    }

    override fun getViewPagerSize(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Int {
        return 1
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
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
            fragment.notifyRequest(type, gradeCode, subjectCode, unitId)
            if (totalText.isSelected){
                totalText.isSelected = false
            }
        }
    }

    //视频播放退出全屏的时候要保持横屏
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        JZUtils.setRequestedOrientation(this, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    override fun onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

}
