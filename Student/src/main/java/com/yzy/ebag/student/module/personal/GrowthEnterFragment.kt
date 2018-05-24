package com.yzy.ebag.student.module.personal

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.GrowthBean
import ebag.core.base.BaseFragment
import ebag.core.util.SPUtils
import ebag.core.util.T
import kotlinx.android.synthetic.main.fragment_growth_enter.*

/**
 * 成长轨迹
 * @author caoyu
 * @date 2018/1/27
 * @description
 */
class GrowthEnterFragment : BaseFragment() {
    private var gradeCode = 0
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
        gradeCode = SPUtils.get(activity, ebag.mobile.base.Constants.GRADE_CODE, -1) as Int
        pageIndex = bundle?.getInt("pageIndex") ?: 0
        list.clear()
        when (pageIndex) {
            0 -> {
                list.add(GrowthBean("一年级", 1, if (gradeCode < 1) 0 else 1))
                list.add(GrowthBean("二年级", 2, if (gradeCode < 2) 0 else 1))
                list.add(GrowthBean("三年级", 3, if (gradeCode < 3) 0 else 1))
                list.add(GrowthBean("四年级", 4, if (gradeCode < 4) 0 else 1))
                list.add(GrowthBean("五年级", 5, if (gradeCode < 5) 0 else 1))
                list.add(GrowthBean("六年级", 6, if (gradeCode < 6) 0 else 1))
            }
            1 -> {
                list.add(GrowthBean("初一", 7, if (gradeCode < 7) 0 else 1))
                list.add(GrowthBean("初二", 8, if (gradeCode < 8) 0 else 1))
                list.add(GrowthBean("初三", 9, if (gradeCode < 9) 0 else 1))
            }
            2 -> {
                list.add(GrowthBean("高一", 10,if (gradeCode < 10) 0 else 1))
                list.add(GrowthBean("高二", 11,if (gradeCode < 11) 0 else 1))
                list.add(GrowthBean("高三", 12,if (gradeCode < 12) 0 else 1))
            }
        }
    }

    override fun initViews(rootView: View) {

        recyclerView.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        adapter.setNewData(list)

        adapter.setOnItemClickListener { _, view, position ->
            if (adapter.getItem(position)?.status != 0) {

                GrowthTypeActivity.jump(mContext, adapter.getItem(position)?.grade
                        ?: "", adapter.getItem(position)?.gradeCode.toString())
            }else{
                T.show(activity, "无法进入更高年级")
            }
        }

    }


    inner class Adapter : BaseQuickAdapter<GrowthBean, BaseViewHolder>(R.layout.item_fragment_growth_enter) {
        override fun convert(helper: BaseViewHolder, item: GrowthBean?) {
            helper.setGone(R.id.line, helper.adapterPosition != 0)
                    .setText(R.id.tvGrade, item?.grade)
                    .setImageResource(R.id.image, getDrawableId(mContext, item?.gradeCode
                            ?: 1, item?.status ?: 0))
        }

    }

    private fun getDrawableId(context: Context, grade: Int, status: Int): Int {
        return context.resources.getIdentifier("locus_img_grade_${grade}_$status",
                "drawable", context.packageName)
    }

}