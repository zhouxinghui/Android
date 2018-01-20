package com.yzy.ebag.student.activity.center.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack

/**
 * @author caoyu
 * @date 2018/1/17
 * @description
 */
class LabourFragment: BaseListFragment<String, LabourFragment.Task>() {

    companion object {
        fun newInstance(classId: String): LabourFragment{
            val fragment = LabourFragment()
            val bundle =Bundle()
            bundle.putString("classId",classId)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var classId: String
    override fun getBundle(bundle: Bundle?) {
        classId = bundle?.getString("classId") ?: ""
    }

    override fun loadConfig() {
        val list = ArrayList<Task>()
        list.add(Task())
        list.add(Task())
        list.add(Task())
        list.add(Task())
        list.add(Task())
        list.add(Task())
        list.add(Task())
        list.add(Task())
        list.add(Task())
        list.add(Task())
        withFirstPageData(list)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<String>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: String?): List<Task>? {
        return null
    }

    override fun getAdapter(): BaseQuickAdapter<Task, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<Task, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    class Adapter: BaseQuickAdapter<Task,BaseViewHolder>(R.layout.item_fragment_task_labour){

        override fun convert(helper: BaseViewHolder, item: Task?) {
            helper.setText(R.id.title, item?.title)
                    .setText(R.id.content, item?.content)
                    .setText(R.id.time, item?.time)
                    .setText(R.id.several, "${item?.times}遍")
                    .setText(R.id.reward, "+${item?.reward}")
                    .setVisible(R.id.complete, item?.status == 1)
        }

    }


    data class Task(
            val title: String = "打扫卫生",
            val content: String = "把自己的脸扫干净",
            val time: String = "2017-01-02",
            val times: Int = 1,
            val reward: Int = 100,
            val status: Int = 1
    )
}