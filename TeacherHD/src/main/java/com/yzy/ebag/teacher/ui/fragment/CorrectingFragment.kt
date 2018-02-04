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
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.bean.CorrectingBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
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
    private val request = object : RequestCallBack<List<CorrectingBean>>(){
        override fun onStart() {
            stateView.showLoading()
        }
        override fun onSuccess(entity: List<CorrectingBean>?) {
            if (entity == null || entity.isEmpty()){
                stateView.showEmpty()
                return
            }
            stateView.showContent()
            mAdapter.setNewData(entity)
            mAdapter.expand(0)
            mAdapter.selectSubject = mAdapter.getItem(1) as CorrectingBean.SubjectVosBean
            //不能用foreach,用了会让你怀疑人生的
            (0 until entity.size)
                    .filter {
                        //必须判断，因为if中100%会有false返回
                        entity[it] is CorrectingBean
                    }
                    .forEach { i ->
                        entity[i].subjectVos.forEach {
                            pagerList.add(it)
                        }
                    }
            pagerList.add(entity[0].subjectVos[0])
            viewPager.adapter = MyPagerAdapter(childFragmentManager, arrayOfNulls(pagerList.size))
        }

        override fun onError(exception: Throwable) {
            exception.handleThrowable(mContext)
            stateView.showError()
        }

    }
    private val mAdapter = MyAdapter()
    private var type = ""
    override fun getBundle(bundle: Bundle?) {
        type = bundle?.getString(Constants.WORK_TYPE)!!
    }

    override fun initViews(rootView: View) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position)
            if(item is CorrectingBean) {
                if (item.isExpanded) {
                    adapter.collapse(position)
                } else {
                    adapter.expand(position)
                }
            }else{
                item as CorrectingBean.SubjectVosBean
                mAdapter.selectSubject = item
                viewPager.setCurrentItem(pagerList.indexOf(item), false)
            }
            L.e("position:  $position" )
        }
        TeacherApi.searchPublish(type, request)
        stateView.setOnRetryClickListener { TeacherApi.searchPublish(type, request) }
    }

    private val pagerList = ArrayList<CorrectingBean.SubjectVosBean>()

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

    inner class MyPagerAdapter(fragmentManager: FragmentManager, private var fragments: Array<CorrectingSubFragment?>): FragmentPagerAdapter(fragmentManager){
        override fun getItem(position: Int): Fragment {
            if (fragments[position] == null)
                fragments[position] = CorrectingSubFragment.newInstance(
                        pagerList[position].homeWorkInfoVos as ArrayList<CorrectingBean.SubjectVosBean.HomeWorkInfoVosBean>,
                        type)
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return pagerList.size
        }

    }
}