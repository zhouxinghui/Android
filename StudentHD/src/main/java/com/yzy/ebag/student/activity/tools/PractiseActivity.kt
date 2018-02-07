package com.yzy.ebag.student.activity.tools

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
import com.yzy.ebag.student.activity.tools.fragment.PractiseFragment
import com.yzy.ebag.student.activity.tools.practise.RecordActivity
import com.yzy.ebag.student.base.BaseListTabActivity
import com.yzy.ebag.student.base.UnitAdapter
import com.yzy.ebag.student.base.UnitBean
import com.yzy.ebag.student.bean.EditionBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack


/**
 * @author caoyu
 * @date 2018/1/21
 * @description
 */
class PractiseActivity: BaseListTabActivity<EditionBean, MultiItemEntity>() {

    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context,PractiseActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }

    private lateinit var tvMaterial: TextView
    private lateinit var classId: String
    override fun loadConfig() {
        classId = intent.getStringExtra("classId") ?: ""
        setTitleContent("选择汉字")
        setLeftWidth(resources.getDimensionPixelSize(R.dimen.x368))
        setMiddleDistance(resources.getDimensionPixelSize(R.dimen.x20))


        val view = layoutInflater.inflate(R.layout.layout_practise_material_header,null)
        tvMaterial = view.findViewById(R.id.text)
        addLeftHeaderView(view)
        titleBar.setRightText("记录"){
            RecordActivity.jump(this)
        }
    }

    override fun requestData(requestCallBack: RequestCallBack<EditionBean>) {
        StudentApi.getUint(classId, "yw", requestCallBack)
    }

    override fun parentToList(parent: EditionBean?): List<UnitBean>? {
        tvMaterial.text = parent?.bookVersion
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
                fragment = PractiseFragment.newInstance(item.resultBookUnitOrCatalogVos[0].unitCode)
            return fragment
        }
        fragment = PractiseFragment.newInstance("")
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