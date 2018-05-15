package com.yzy.ebag.student.module.mission

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
 * Created by YZY on 2018/5/15.
 */
class LabourMissionFragment: BaseListFragment<ArrayList<LabourBean>, LabourBean>() {
    companion object {
        fun newInstance(): LabourMissionFragment{
            return LabourMissionFragment()
        }
    }
    override fun getBundle(bundle: Bundle?) {
    }

    override fun loadConfig() {
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

    override fun getLayoutManager(adapter: BaseQuickAdapter<LabourBean, BaseViewHolder>): RecyclerView.LayoutManager? = null

    private inner class Adapter: BaseQuickAdapter<LabourBean,BaseViewHolder>(R.layout.item_fragment_task_labour){

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