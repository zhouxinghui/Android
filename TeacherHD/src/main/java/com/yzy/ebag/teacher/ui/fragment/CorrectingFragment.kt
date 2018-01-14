package com.yzy.ebag.teacher.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.IExpandable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import ebag.core.base.BaseFragment
import ebag.core.util.L
import kotlinx.android.synthetic.main.fragment_correcting.*

/**
 * 检查作业
 * Created by YZY on 2018/1/13.
 */
class CorrectingFragment: BaseFragment() {
    companion object {
        fun newInstance(workType: String): CorrectingFragment{
            val fragment = CorrectingFragment()
            val bundle = Bundle()
            bundle.putString(Constants.WORK_TYPE, workType)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_correcting
    }

    override fun getBundle(bundle: Bundle) {

    }

    override fun initViews(rootView: View) {
        val list = ArrayList<MultiItemEntity>()
        for (i in 0..8){
            val classesBean = ClassesBean()
            classesBean.clazzName = "一年级一班"
            val subList = ArrayList<SubjectBean>()
            for (j in 0..2){
                val subjectBean = SubjectBean()
                subjectBean.subjectName = "语文"
                subList.add(subjectBean)
                classesBean.subList = subList
            }
            list.add(classesBean)
        }
        val mAdapter = MyAdapter(list)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter.expand(0)
        mAdapter.selectSubject = mAdapter.getItem(1) as SubjectBean
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position)
            if(item is ClassesBean) {
                if (item.isExpanded) {
                    adapter.collapse(position)
                } else {
                    adapter.expand(position)
                }
            }else{
                item as SubjectBean
                mAdapter.selectSubject = item
                viewPager.setCurrentItem(pagerList.indexOf(item), false)
            }
            L.e("position:  $position" )
        }
        list.forEach {
            if (it is ClassesBean)
                pagerList.addAll(it.subList!!)
        }

        viewPager.adapter = MyPagerAdapter(childFragmentManager)
    }

    private val pagerList = ArrayList<SubjectBean>()

    inner class MyAdapter(list: ArrayList<MultiItemEntity>): BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(list){
        init {
            addItemType(1, R.layout.fragment_correct_classes_item)
            addItemType(2, R.layout.fragment_correct_subject_item)
        }
        var selectSubject: SubjectBean? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
            val tv = helper!!.getView<TextView>(R.id.text)
            val point = helper.getView<View>(R.id.dot)
            when(helper.itemViewType){
                1 ->{
                    item as ClassesBean
                    tv.text = item.clazzName
                    tv.isSelected = item.isExpanded
                    point.isSelected = item.isExpanded
                }
                2 ->{
                    item as SubjectBean
                    tv.text = item.subjectName
                    tv.isSelected = selectSubject == item
                }
            }
        }
    }

    inner class MyPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager){
        override fun getItem(position: Int): Fragment {
            return CorrectingSubFragment.newInstance()
        }

        override fun getCount(): Int {
            return pagerList.size
        }

    }

    inner class ClassesBean: IExpandable<SubjectBean>, MultiItemEntity {
        override fun getSubItems(): MutableList<SubjectBean> {
            return subList!!
        }

        override fun isExpanded(): Boolean {
            return isExpand
        }

        override fun setExpanded(expanded: Boolean) {
            isExpand = expanded
        }

        override fun getItemType(): Int {
            return 1
        }

        override fun getLevel(): Int {
            return 1
        }
        var isExpand = false
        var subList: ArrayList<SubjectBean>? = null
        var clazzName: String? = null
    }

    inner class SubjectBean: MultiItemEntity{
        override fun getItemType(): Int {
            return 2
        }

        var subjectName: String? = null
    }
}