package com.yzy.ebag.student.dialog

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.dialog_performance.*

/**
 * @author caoyu
 * @date 2018/1/19
 * @description
 */
class PerformanceDialog: BaseFragmentDialog() {

    companion object {
        fun newInstance(): PerformanceDialog{
            return PerformanceDialog()
        }
    }

    override fun getBundle(bundle: Bundle?) {
    }

    val adapter = Adapter()
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

    override fun getLayoutRes(): Int {
        return R.layout.dialog_performance
    }

    override fun initView(view: View) {

        btnClose.setOnClickListener {
            dismiss()
        }

        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)

        if(userEntity != null){
            ivHead.loadHead(userEntity.headUrl)
            tvName.text = userEntity.name
        }

        tvReward.setOnClickListener {
            if(!tvReward.isSelected){
                tvReward.isSelected = true
                tvCriticism.isSelected = false
                adapter.isNice = true
                adapter.setNewData(listNiceCount)
            }
        }

        tvCriticism.setOnClickListener {
            if(!tvCriticism.isSelected){
                tvReward.isSelected = false
                tvCriticism.isSelected = true
                adapter.isNice = false
                adapter.setNewData(listBadCount)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(mContext, 3)


        tvReward.isSelected = true

        adapter.isNice = true
        adapter.setNewData(listNiceCount)

    }

    inner class Adapter: BaseQuickAdapter<Int,BaseViewHolder>(R.layout.item_dialog_performance){

        var isNice = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

        override fun convert(helper: BaseViewHolder, item: Int?) {
            if(isNice){
                helper.setImageResource(R.id.image,listNiceIcon[helper.adapterPosition])
                        .setText(R.id.text,listNiceWords[helper.adapterPosition])
            }else{
                helper.setImageResource(R.id.image,listBadIcon[helper.adapterPosition])
                        .setText(R.id.text,listBadWords[helper.adapterPosition])
            }

            helper.setText(R.id.tvTag, "$item")
        }

    }
}