package com.yzy.ebag.teacher.activity.assignment

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.bean.TestPaperListBean
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack

/**
 * Created by YZY on 2018/4/21.
 */
class TestPaperFragment: BaseListFragment<List<TestPaperListBean>, TestPaperListBean>() {
    override fun getBundle(bundle: Bundle?) {

    }

    private var testPaperFlag: String = ""
    private var gradeCode: String = ""
    private var unitId: String = ""
    private var subCode: String = ""

    override fun loadConfig() {

    }

    fun requestData(testPaperFlag: String, gradeCode: String, unitId: String, subCode: String){
        this.testPaperFlag = testPaperFlag
        this.gradeCode = gradeCode
        this.unitId = unitId
        this.subCode = subCode
        onRetryClick()
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<TestPaperListBean>>) {
        TeacherApi.testPaperList(testPaperFlag, gradeCode, unitId, subCode, requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: List<TestPaperListBean>?): List<TestPaperListBean>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<TestPaperListBean, BaseViewHolder> {
        return TestAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<TestPaperListBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext, 2)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        adapter as TestAdapter

    }

    /**
     * 试卷
     */
    inner class TestAdapter: BaseQuickAdapter<TestPaperListBean, BaseViewHolder>(R.layout.item_assignment_test){
        var selectPosition = -1
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: TestPaperListBean?) {
            helper.setText(R.id.tv_id, item?.testPaperName)
            helper.getView<TextView>(R.id.tv_id).isSelected = selectPosition == helper.adapterPosition
        }
    }
}