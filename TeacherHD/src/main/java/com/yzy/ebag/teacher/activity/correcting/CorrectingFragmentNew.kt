package com.yzy.ebag.teacher.activity.correcting

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.CorrectingBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.http.network.RequestCallBack
import ebag.hd.base.BaseListTabFragment

/**
 * Created by YZY on 2018/3/30.
 */
class CorrectingFragmentNew: BaseListTabFragment<List<CorrectingBean>, MultiItemEntity>() {
    companion object {
        fun newInstance(type: String): CorrectingFragmentNew{
            val fragment = CorrectingFragmentNew()
            val bundle = Bundle()
            bundle.putString("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }
    private var type = "1"
    override fun loadConfig(bundle: Bundle?) {
        type = bundle?.getString("type") ?: "1"
        setLeftWidth(resources.getDimensionPixelSize(R.dimen.x300))
    }

    override fun firstPageDataLoad(result: List<MultiItemEntity>) {
        super.firstPageDataLoad(result)
        if (adapter.itemCount > 0) {
            try {
                val position = (0 until adapter.itemCount).first { adapter.getItem(it) is CorrectingBean }
                adapter.selectSubject = (adapter.getItem(position) as CorrectingBean).subjectVos[0]
                adapter.expand(position)
            }catch (e: Exception){

            }
        }
    }

    override fun requestData(requestCallBack: RequestCallBack<List<CorrectingBean>>) {
        TeacherApi.searchPublish(type, requestCallBack)
    }

    override fun parentToList(parent: List<CorrectingBean>?): List<MultiItemEntity>? {
        return parent
    }

    private lateinit var adapter: MyAdapter
    override fun getLeftAdapter(): BaseQuickAdapter<MultiItemEntity, BaseViewHolder> {
        adapter = MyAdapter()
        return adapter
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    private lateinit var mFragment : CorrectingSubFragment
    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Fragment {
        if(adapter.itemCount > 0){
            val item = adapter.data[0]
            if(item is CorrectingBean)
                mFragment = CorrectingSubFragment.newInstance(type, item.classId, item.subjectVos[0].subCode)
            return mFragment
        }
        mFragment = CorrectingSubFragment.newInstance()
        return mFragment
    }

    override fun getViewPagerSize(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Int {
        return 1
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as MyAdapter
        val item = adapter.getItem(position)
        if(item is CorrectingBean) {
            if (item.isExpanded) {
                adapter.collapse(position)
            } else {
                adapter.expand(position)
            }
        }else{
            item as CorrectingBean.SubjectVosBean
            adapter.selectSubject = item
            mFragment.update(item.classId, item.subCode)
        }
    }

    inner class MyAdapter: BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(null){
        init {
            addItemType(1, R.layout.fragment_correct_classes_item)
            addItemType(2, R.layout.fragment_correct_subject_item)
        }
        var selectSubject: CorrectingBean.SubjectVosBean? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
            val tv = helper!!.getView<TextView>(R.id.text)
            val point = helper.getView<View>(R.id.dot)
            when(helper.itemViewType){
                Constants.LEVEL_ONE ->{
                    item as CorrectingBean
                    tv.text = item.className
                    tv.isSelected = item.isExpanded
                    point.isSelected = item.isExpanded
                }
                Constants.LEVEL_TWO ->{
                    item as CorrectingBean.SubjectVosBean
                    tv.text = item.subject
                    tv.isSelected = selectSubject == item
                }
            }
        }
    }

}