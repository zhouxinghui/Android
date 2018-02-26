package com.yzy.ebag.teacher.ui.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.AssignUnitBean
import com.yzy.ebag.teacher.http.TeacherApi
import com.yzy.ebag.teacher.ui.activity.QuestionAdapter
import com.yzy.ebag.teacher.widget.FeedbackDialog
import ebag.core.base.BaseListFragment
import ebag.core.bean.QuestionBean
import ebag.core.http.network.RequestCallBack

/**
 * Created by YZY on 2018/1/31.
 */
class QuestionFragment: BaseListFragment<List<QuestionBean>, QuestionBean>() {
    private lateinit var unitBean: AssignUnitBean.UnitSubBean
    private var difficulty: String? = null
    private lateinit var type: String
    private lateinit var previewList: ArrayList<QuestionBean>
    private var isPreview = false
    private val feedbackDialog by lazy { FeedbackDialog(mContext) }
    companion object {
        fun newInstance(previewList: ArrayList<QuestionBean>, unitBean: AssignUnitBean.UnitSubBean, difficulty: String?, type: String): QuestionFragment{
            val fragment = QuestionFragment()
            val bundle = Bundle()
            bundle.putSerializable("previewList", previewList)
            bundle.putSerializable("unitBean", unitBean)
            bundle.putString("difficulty", difficulty)
            bundle.putString("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getBundle(bundle: Bundle?) {
        unitBean = bundle?.getSerializable("unitBean") as AssignUnitBean.UnitSubBean
        difficulty = bundle.getString("difficulty")
        type = bundle.getString("type")
        previewList = bundle.getSerializable("previewList") as ArrayList<QuestionBean>
    }

    override fun isPagerFragment(): Boolean {
        return false
    }
    override fun loadConfig() {
        (mAdapter as QuestionAdapter).previewList = this.previewList
    }

    fun showPreview(list: ArrayList<QuestionBean>){
        isPreview = true
        withFirstPageData(list)
    }
    fun showSelect(list: ArrayList<QuestionBean>){
        isPreview = false
        withFirstPageData(list, true)
    }
    fun getData(): ArrayList<QuestionBean>{
        return mAdapter!!.data as ArrayList<QuestionBean>
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as QuestionAdapter
        when(view?.id){
            R.id.feedBackTv ->{
                feedbackDialog.show()
            }
            R.id.selectTv ->{
                val questionBean = adapter.getItem(position)
                if (isPreview){
                    questionBean!!.isChoose = false
                    if (adapter.previewList.contains(questionBean))
                        adapter.remove(position)
                }else {
                    if (questionBean!!.isChoose) {
                        if (adapter.previewList.contains(questionBean))
                            adapter.previewList.remove(questionBean)
                        questionBean.isChoose = false
                    } else {
                        adapter.previewList.add(questionBean)
                        questionBean.isChoose = true
                    }
                }
                onSelectClick?.invoke(questionBean)

                adapter.notifyDataSetChanged()
            }
            R.id.analyseTv ->{
                adapter.selectItem = position
                val questionBean = adapter.getItem(position)?.clone() as QuestionBean
                questionBean.answer = questionBean.rightAnswer
                onAnalyseClick?.invoke(questionBean)
            }
        }
    }
    var onAnalyseClick : ((questionBean: QuestionBean?) -> Unit)? = null
    var onSelectClick: ((questionBean: QuestionBean) -> Unit)? = null

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<QuestionBean>>) {
        TeacherApi.searchQuestion(unitBean, difficulty, type, page, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<QuestionBean>?): List<QuestionBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<QuestionBean, BaseViewHolder> {
        return QuestionAdapter(false)
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<QuestionBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }
}