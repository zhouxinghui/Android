//package com.yzy.ebag.teacher.ui.fragment
//
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v7.widget.RecyclerView
//import android.view.View
//import com.chad.library.adapter.base.BaseQuickAdapter
//import com.chad.library.adapter.base.BaseViewHolder
//import com.chad.library.adapter.base.entity.IExpandable
//import com.chad.library.adapter.base.entity.MultiItemEntity
//import com.yzy.ebag.teacher.base.Constants
//import ebag.core.base.BaseListTabFragment
//import ebag.core.http.network.RequestCallBack
//
///**
// * 检查作业
// * Created by YZY on 2018/1/13.
// */
//class CorrectingFragment2 : BaseListTabFragment<String, CorrectingFragment2.ClassesBean>() {
//    override fun loadConfig() {
//
//    }
//
//    override fun requestData(requestCallBack: RequestCallBack<String>) {
//
//    }
//
//    override fun parentToList(parent: String?): List<ClassesBean>? {
//        return null
//    }
//
//    override fun getLeftAdapter(): BaseQuickAdapter<ClassesBean, BaseViewHolder> {
//         return
//    }
//
//    override fun getBundle(bundle: Bundle) {
//
//    }
//
//    override fun getLayoutManager(): RecyclerView.LayoutManager? {
//
//    }
//
//    override fun getFragment(index: Int, item: ClassesBean?): Fragment {
//
//    }
//
//    override fun leftItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
//
//    }
//
//    companion object {
//        fun newInstance(workType: String): CorrectingFragment2 {
//            val fragment = CorrectingFragment2()
//            val bundle = Bundle()
//            bundle.putString(Constants.WORK_TYPE, workType)
//            fragment.arguments = bundle
//            return fragment
//        }
//    }
//    inner class ClassesBean: IExpandable<SubjectBean>, MultiItemEntity {
//        override fun getSubItems(): MutableList<SubjectBean> {
//            return subList!!
//        }
//
//        override fun isExpanded(): Boolean {
//            return isExpand
//        }
//
//        override fun setExpanded(expanded: Boolean) {
//            isExpand = expanded
//        }
//
//        override fun getItemType(): Int {
//            return 1
//        }
//
//        override fun getLevel(): Int {
//            return 1
//        }
//        var isExpand = false
//        var subList: ArrayList<SubjectBean>? = null
//        var clazzName: String? = null
//    }
//
//    inner class SubjectBean: MultiItemEntity{
//        override fun getItemType(): Int {
//            return 2
//        }
//
//        var subjectName: String? = null
//    }
//}