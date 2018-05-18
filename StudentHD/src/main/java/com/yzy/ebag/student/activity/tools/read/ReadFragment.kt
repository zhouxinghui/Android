package com.yzy.ebag.student.activity.tools.read

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.ReadOutBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.util.loadImage

/**
 * @author caoyu
 * @date 2018/1/22
 * @description
 */
class ReadFragment: BaseListFragment<ReadOutBean, ReadOutBean.OralLanguageBean>() {


    companion object {
        fun newInstance(classId: String, unitCode: String?): ReadFragment {
            val fragment = ReadFragment()
            val bundle = Bundle()
            bundle.putString("unitCode", unitCode)
            bundle.putString("classId", classId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun isPagerFragment(): Boolean {
        return false
    }

    private lateinit var unitCode: String
    private lateinit var classId: String
    override fun getBundle(bundle: Bundle?) {
        unitCode = bundle?.getString("unitCode") ?: ""
        classId = bundle?.getString("classId") ?: ""
    }

    fun update(unitCode: String?){
        if(this.unitCode != unitCode){
            this.unitCode = unitCode ?: ""
            cancelRequest()
            onRetryClick()
        }
    }

    override fun loadConfig() {
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<ReadOutBean>) {
        StudentApi.getReadList(unitCode, page, getPageSize(), requestCallBack)
    }

    override fun parentToList(isFirstPage: Boolean, parent: ReadOutBean?): List<ReadOutBean.OralLanguageBean>? {
        return parent?.oralLanguageVos
    }

    override fun getAdapter(): BaseQuickAdapter<ReadOutBean.OralLanguageBean, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<ReadOutBean.OralLanguageBean, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext, 2)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        FollowReadActivity.jump(mContext, classId, adapter?.getItem(position) as ReadOutBean.OralLanguageBean?)
    }

    class Adapter: BaseQuickAdapter<ReadOutBean.OralLanguageBean, BaseViewHolder>(R.layout.item_fragment_tools_read){
        override fun convert(helper: BaseViewHolder, item: ReadOutBean.OralLanguageBean?) {
            helper.setText(R.id.text, item?.fileName)
                    .getView<ImageView>(R.id.image).loadImage(item?.coveUrl)
        }
    }
}