package com.yzy.ebag.student.activity.tools.practise

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.student.R
import ebag.core.http.network.RequestCallBack
import ebag.core.util.StringUtils
import ebag.hd.adapter.UnitAdapter
import ebag.hd.base.BaseListTabActivity
import ebag.hd.bean.BaseClassesBean
import ebag.hd.bean.EditionBean
import ebag.hd.bean.UnitBean
import ebag.hd.http.EBagApi
import ebag.hd.widget.ClazzListPopup
import ebag.hd.widget.ReadRecordTextbookPopup
import ebag.hd.widget.drawable.DrawableTextView


/**
 * @author caoyu
 * @date 2018/1/21
 * @description
 */
class PractiseActivity: BaseListTabActivity<EditionBean, MultiItemEntity>() {

    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context, PractiseActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }

    private lateinit var tvMaterial: TextView
    private lateinit var classId: String
    private val classesRequest = object : RequestCallBack<List<BaseClassesBean>>(){
        override fun onStart() {
            showLoading()
        }

        override fun onSuccess(entity: List<BaseClassesBean>?) {
            if (entity == null || entity.isEmpty()){
                showError("暂无班级信息")
                return
            }
            classId = entity[0].classId
            classesTv.text = entity[0].className
            classes.addAll(entity)
            request()
        }

        override fun onError(exception: Throwable) {
            showError(exception.message.toString())
        }
    }
    private lateinit var classesTv: TextView
    private lateinit var textBookTv: DrawableTextView
    private val classesPopup by lazy {
        val popupWindow = ClazzListPopup(this)
        popupWindow.onClassSelectListener = {classBean ->
            classesTv.text = classBean.className
            classId = classBean.classId
            request()
        }
        popupWindow
    }
    private var bookVersionId = ""
    private var isFirstRequest = true
    private val textbookPopup by lazy {
        val popupWindow = ReadRecordTextbookPopup(this)
        popupWindow.onConfirmClick = {versionId, versionName, semesterName, subName ->
            textBookTv.text = "$subName-$versionName-$semesterName"
            bookVersionId = versionId
            isFirstRequest = false
            request()
        }
        popupWindow
    }
    private val classes = ArrayList<BaseClassesBean>()
    override fun loadConfig() {
        classId = intent.getStringExtra("classId") ?: ""
        setTitleContent("选择汉字")
        setLeftWidth(resources.getDimensionPixelSize(R.dimen.x368))
        setMiddleDistance(resources.getDimensionPixelSize(R.dimen.x20))


        /*val view = layoutInflater.inflate(R.layout.layout_practise_material_header,null)
        tvMaterial = view.findViewById(R.id.text)
        addLeftHeaderView(view)*/

        val subjectView = layoutInflater.inflate(R.layout.zixi_left_tv_head, null)
        classesTv = subjectView.findViewById(R.id.clazzTv)
        classesTv.setOnClickListener { //更换科目
            classesPopup.setData(classes)
            classesPopup.showAsDropDown(titleBar, subjectView.width + 1, 0)
        }

        val textBookView = layoutInflater.inflate(R.layout.textbook_head, null)
        textBookTv = textBookView.findViewById(R.id.textBookVersion)
        textBookTv.setOnClickListener { //更换教材
            textbookPopup.setRequest(classId, "yw")
            textbookPopup.showAsDropDown(subjectView, textBookView.width + 1, 0)
        }
        addLeftHeaderView(textBookView)
        addLeftHeaderView(subjectView)

        EBagApi.getMyClasses(classesRequest)

        titleBar.setRightText("记录"){
            RecordActivity.jump(this, classId)
        }
    }

    override fun requestData(requestCallBack: RequestCallBack<EditionBean>) {
        if (StringUtils.isEmpty(bookVersionId))
            EBagApi.getUnit(classId, "yw", requestCallBack)
        else
            EBagApi.getUnit(bookVersionId, requestCallBack)
    }

    override fun parentToList(parent: EditionBean?): List<UnitBean>? {
        if (isFirstRequest)
            textBookTv.text = parent?.bookVersion
        return parent?.resultBookUnitOrCatalogVos
    }

    override fun firstPageDataLoad(result: List<MultiItemEntity>) {
        super.firstPageDataLoad(result)
        if (adapter.itemCount > 0) {
            try {
                val position = (0 until adapter.itemCount).first { adapter.getItem(it) is UnitBean }
                adapter.selectSub = (adapter.getItem(position) as UnitBean).resultBookUnitOrCatalogVos[0]
                adapter.expand(position)
            }catch (e: Exception){

            }
        }
    }

    private lateinit var adapter: UnitAdapter
    override fun getLeftAdapter(): BaseQuickAdapter<MultiItemEntity, BaseViewHolder> {
        adapter = UnitAdapter()
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    private lateinit var fragment: PractiseFragment
    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Fragment {
        if(adapter.itemCount > 0){
            val item = adapter.getItem(0)
            if(item is UnitBean)
                fragment = PractiseFragment.newInstance(item.resultBookUnitOrCatalogVos[0].unitCode,classId)
            return fragment
        }
        fragment = PractiseFragment.newInstance("",classId)
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

            fragment.update(item?.unitCode)
        }
    }
}