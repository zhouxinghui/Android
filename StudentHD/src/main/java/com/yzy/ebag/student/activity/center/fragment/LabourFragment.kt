package com.yzy.ebag.student.activity.center.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.LabourBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack

/**
 * @author caoyu
 * @date 2018/1/17
 * @description
 */
class LabourFragment: BaseListFragment<ArrayList<LabourBean>, LabourBean>() {

    companion object {
        fun newInstance(): LabourFragment{
            return LabourFragment()
        }
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun loadConfig() {
        rootView.setPadding(0, resources.getDimensionPixelSize(R.dimen.x48), 0, 0)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<ArrayList<LabourBean>>) {
        StudentApi.labourTasks(page, getPageSize(), requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: ArrayList<LabourBean>?): List<LabourBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<LabourBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<LabourBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    class Adapter: BaseQuickAdapter<LabourBean,BaseViewHolder>(R.layout.item_fragment_task_labour){

        override fun convert(helper: BaseViewHolder, item: LabourBean?) {
            helper.setText(R.id.title, item?.title)
                    .setText(R.id.content, item?.content)
                    .setText(R.id.time, item?.createDate)
                    .setText(R.id.several, "${item?.sum}遍")
//                    .setText(R.id.reward, "+${item?.reward}")
                    .setVisible(R.id.complete, item?.completed?.toUpperCase() == "Y")
        }

    }
}