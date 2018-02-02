package com.yzy.ebag.teacher.ui.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.ui.activity.QuestionAdapter
import ebag.core.base.BaseListFragment
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T

/**
 * Created by YZY on 2018/1/31.
 */
class PreviewFragment : BaseListFragment<List<QuestionBean>, QuestionBean>() {
    private lateinit var previewList: ArrayList<QuestionBean>
    private var isSmartPush = false
    companion object {
        fun newInstance(previewList: ArrayList<QuestionBean>): PreviewFragment {
            val fragment = PreviewFragment()
            val bundle = Bundle()
            bundle.putSerializable("previewList", previewList)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getBundle(bundle: Bundle?) {
        previewList = bundle?.getSerializable("previewList") as ArrayList<QuestionBean>
    }

    override fun loadConfig() {
        (mAdapter as QuestionAdapter).previewList = previewList
        withFirstPageData(previewList)
    }
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as QuestionAdapter
        when(view?.id){
            R.id.feedBackTv ->{
                T.show(mContext, "错题反馈")
            }
            R.id.selectTv ->{
                val questionBean = adapter.getItem(position)
                questionBean!!.isChoose = false
                if (adapter.previewList.contains(questionBean))
                    adapter.remove(position)
                onSelectClick?.invoke(questionBean)

                adapter.notifyDataSetChanged()
            }
            R.id.analyseTv ->{
                adapter.selectItem = position
                val questionBean = adapter.getItem(position)?.clone() as QuestionBean
                questionBean.studentAnswer = questionBean.answer
                onAnalyseClick?.invoke(questionBean)
            }
        }
    }
    var onAnalyseClick : ((questionBean: QuestionBean?) -> Unit)? = null
    var onSelectClick: ((questionBean: QuestionBean) -> Unit)? = null

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<QuestionBean>>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<QuestionBean>?): List<QuestionBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<QuestionBean, BaseViewHolder> {
        return QuestionAdapter(true)
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<QuestionBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }
}