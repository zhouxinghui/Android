package com.yzy.ebag.parents.ui.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.parents.R
import ebag.core.base.App
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.util.SPUtils
import ebag.core.util.SerializableUtils
import ebag.core.util.loadHead
import ebag.mobile.base.Constants
import ebag.mobile.bean.MyChildrenBean
import ebag.mobile.bean.PersonalPerformanceBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.dialog_performance.*

class PerformanceDialog(context: Context) : BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_performance
    }

    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x250)
    }

    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y600)
    }

    private val adapter = Adapter()
    private val request = object : RequestCallBack<PersonalPerformanceBean>() {
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: PersonalPerformanceBean?) {
            stateView.showContent()
            if (entity == null || entity.praise == null || entity.praise.isEmpty() || entity.criticize == null || entity.criticize.isEmpty()) {
                stateView.showEmpty()
                return
            }
            listNiceCount.clear()
            listNiceCount.addAll(entity.praise)
            listBadCount.clear()
            listBadCount.addAll(entity.criticize)
            tvReward.isSelected = true
            tvCriticism.isSelected = false
            adapter.isNice = true
            adapter.setNewData(listNiceCount)
        }

        override fun onError(exception: Throwable) {
            stateView.showError(exception.message.toString())
        }
    }
    private val listNiceIcon by lazy {
        listOf(R.drawable.icon_performance_zxtj,
                R.drawable.icon_performance_lyzr,
                R.drawable.icon_performance_nlxx,
                R.drawable.icon_performance_tdhz,
                R.drawable.icon_performance_jjfy,
                R.drawable.icon_performance_zsjl)
    }
    private val listNiceCount by lazy {
        arrayListOf(0, 0, 0, 0, 0, 0)
    }

    private val listNiceWords by lazy {
        listOf("专心听讲",
                "乐于助人",
                "努力学习",
                "团队合作",
                "积极发言",
                "遵守纪律")
    }

    private val listBadIcon by lazy {
        listOf(R.drawable.icon_performance_skzs,
                R.drawable.icon_performance_bsjl,
                R.drawable.icon_performance_mzzy,
                R.drawable.icon_performance_lmqj,
                R.drawable.icon_performance_cxdy,
                R.drawable.icon_performance_hlsh)
    }

    private val listBadWords by lazy {
        listOf("上课走神",
                "不守纪律",
                "没做作业",
                "礼貌欠佳",
                "粗心大意",
                "胡乱说话")
    }

    private val listBadCount by lazy {
        arrayListOf(0, 0, 0, 0, 0, 0)
    }

    init {

        btnClose.setOnClickListener {
            dismiss()
        }

        val userEntity = SerializableUtils.getSerializable<MyChildrenBean>(Constants.CHILD_USER_ENTITY)

        if (userEntity != null) {
            ivHead.loadHead(userEntity.headUrl)
            tvName.text = userEntity.name
        }

        tvReward.setOnClickListener {
            if (!tvReward.isSelected) {
                tvReward.isSelected = true
                tvCriticism.isSelected = false
                adapter.isNice = true
                adapter.setNewData(listNiceCount)
            }
        }

        tvCriticism.setOnClickListener {
            if (!tvCriticism.isSelected) {
                tvReward.isSelected = false
                tvCriticism.isSelected = true
                adapter.isNice = false
                adapter.setNewData(listBadCount)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(App.mContext, 3)


        tvReward.isSelected = true
        adapter.isNice = true
        EBagApi.personalPerformance(request, SPUtils.get(context, com.yzy.ebag.parents.common.Constants.CURRENT_CHILDREN_YSBCODE, "") as String)
    }


    inner class Adapter : BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_dialog_performance) {

        var isNice = true
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun convert(helper: BaseViewHolder, item: Int?) {
            if (isNice) {
                helper.setImageResource(R.id.image, listNiceIcon[helper.adapterPosition])
                        .setText(R.id.text, listNiceWords[helper.adapterPosition])
                        .setBackgroundRes(R.id.tvTag, R.drawable.blue_point)
            } else {
                helper.setImageResource(R.id.image, listBadIcon[helper.adapterPosition])
                        .setText(R.id.text, listBadWords[helper.adapterPosition])
                        .setBackgroundRes(R.id.tvTag, R.drawable.red_point)
            }

            helper.setText(R.id.tvTag, "$item")
        }

    }
}