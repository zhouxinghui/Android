package com.yzy.ebag.teacher.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseDialog
import ebag.core.util.loadHead
import kotlinx.android.synthetic.main.dialog_performance.*

/**
 * Created by YZY on 2018/3/8.
 */
class PerformanceDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_performance
    }

    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x600)
    }

    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y640)
    }
    val listNiceIcon by lazy {
        listOf(R.drawable.icon_performance_zxtj,
                R.drawable.icon_performance_lyzr,
                R.drawable.icon_performance_nlxx,
                R.drawable.icon_performance_tdhz,
                R.drawable.icon_performance_jjfy,
                R.drawable.icon_performance_zsjl)
    }
    val listNiceCount by lazy {
        listOf(1,2,3,4,5,6)
    }

    val listNiceWords by lazy {
        listOf("专心听讲",
                "乐于助人",
                "努力学习",
                "团队合作",
                "积极发言",
                "遵守纪律")
    }

    val listBadIcon by lazy {
        listOf(R.drawable.icon_performance_skzs,
                R.drawable.icon_performance_bsjl,
                R.drawable.icon_performance_mzzy,
                R.drawable.icon_performance_lmqj,
                R.drawable.icon_performance_cxdy,
                R.drawable.icon_performance_hlsh)
    }

    val listBadWords by lazy {
        listOf("上课走神",
                "不守纪律",
                "没做作业",
                "礼貌欠佳",
                "粗心大意",
                "胡乱说话")
    }
    val listBadCount by lazy {
        listOf(1,2,3,4,5,6)
    }
    init {
        headImg.loadHead("")
        nameTv.text = "小明同学"

        performanceRecycler.layoutManager = GridLayoutManager(context, 3)
        val adapter = MyAdapter()
        performanceRecycler.adapter = adapter
        adapter.setNewData(listNiceCount)

        adapter.isNice = true
        praiseBtn.isSelected = true
        praiseBtn.setOnClickListener {
            if(!praiseBtn.isSelected){
                praiseBtn.isSelected = true
                correctionBtn.isSelected = false
                adapter.isNice = true
                adapter.setNewData(listNiceCount)
            }
        }
        correctionBtn.setOnClickListener {
            if(!correctionBtn.isSelected){
                praiseBtn.isSelected = false
                correctionBtn.isSelected = true
                adapter.isNice = false
                adapter.setNewData(listBadCount)
            }
        }

        adapter.setOnItemClickListener { adapter, view, position ->

        }
    }

    inner class MyAdapter: BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_personal_performance){
        var isNice = true
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        override fun convert(helper: BaseViewHolder, item: Int) {
            if(isNice){
                helper.setImageResource(R.id.image,listNiceIcon[helper.adapterPosition])
                        .setText(R.id.text,listNiceWords[helper.adapterPosition])
                        .setBackgroundRes(R.id.tvTag, R.drawable.blue_point)
            }else{
                helper.setImageResource(R.id.image,listBadIcon[helper.adapterPosition])
                        .setText(R.id.text,listBadWords[helper.adapterPosition])
                        .setBackgroundRes(R.id.tvTag, R.drawable.red_point)
            }

            helper.setText(R.id.tvTag, "$item")
        }
    }
}