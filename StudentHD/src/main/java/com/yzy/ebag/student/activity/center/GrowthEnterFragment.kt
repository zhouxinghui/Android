package com.yzy.ebag.student.activity.center

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.GrowthBean
import ebag.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_growth_enter.*

/**
 * @author caoyu
 * @date 2018/1/27
 * @description
 */
class GrowthEnterFragment : BaseFragment() {

    companion object {
        fun newInstance(pageIndex: Int): GrowthEnterFragment {
            val fragment = GrowthEnterFragment()
            val bundle = Bundle()
            bundle.putInt("pageIndex", pageIndex)
            fragment.arguments = bundle
            return fragment
        }
    }
    private val adapter by lazy { Adapter() }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_growth_enter
    }

    var pageIndex = 0
    private val list = ArrayList<GrowthBean>()
    override fun getBundle(bundle: Bundle?) {
        pageIndex = bundle?.getInt("pageIndex") ?: 0
        list.clear()
        when(pageIndex){
            0 -> {
                list.add(GrowthBean("一年级", 1, 1))
                list.add(GrowthBean("二年级", 2, 1))
                list.add(GrowthBean("三年级", 3, 1))
                list.add(GrowthBean("四年级", 4, 1))
                list.add(GrowthBean("五年级", 5, 1))
                list.add(GrowthBean("六年级", 6, 1))
            }
            1 -> {
                list.add(GrowthBean("初一", 7, 1))
                list.add(GrowthBean("初二", 8, 2))
                list.add(GrowthBean("初三", 9, 0))
            }
            2 -> {
                list.add(GrowthBean("高一", 10, 0))
                list.add(GrowthBean("高二", 11, 0))
                list.add(GrowthBean("高三", 12, 0))
            }
        }
    }

    override fun initViews(rootView: View) {

        recyclerView.layoutManager =
                LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = adapter

        adapter.setNewData(list)

    }


    inner class Adapter: BaseQuickAdapter<GrowthBean, BaseViewHolder>(R.layout.item_fragment_growth_enter){
        override fun convert(helper: BaseViewHolder, item: GrowthBean?) {
            helper.setGone(R.id.line, helper.adapterPosition != 0)
                    .setText(R.id.tvGrade, item?.grade)
                    .setImageResource(R.id.image, getDrawableId(mContext,item?.gradeCode ?: 1, item?.status ?: 0))
        }

    }

    private fun getDrawableId(context: Context, grade: Int, status: Int): Int{
        return context.resources.getIdentifier("locus_img_grade_${grade}_$status",
                "drawable",context.packageName)
    }

}