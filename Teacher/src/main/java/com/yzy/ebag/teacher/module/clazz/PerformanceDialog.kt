package com.yzy.ebag.teacher.module.clazz

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.teacher.R
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.core.util.loadHead
import ebag.mobile.bean.PersonalPerformanceBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.dialog_performance.*

/**
 * Created by YZY on 2018/3/8.
 */
class PerformanceDialog(context: Context): BaseDialog(context) {
    override fun getLayoutRes(): Int {
        return R.layout.dialog_performance
    }

    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x250)
    }

    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.y800)
    }
    private var uid: String? = ""
    private val adapter = MyAdapter()
    var onModifySuccess : (() -> Unit)? = null
    private val request = object : RequestCallBack<PersonalPerformanceBean>(){
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
            praiseBtn.isSelected = true
            correctionBtn.isSelected = false
            adapter.isNice = true
            adapter.setNewData(listNiceCount)
        }

        override fun onError(exception: Throwable) {
            stateView.showError(exception.message.toString())
        }
    }
    private val addRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            LoadingDialogUtil.showLoading(context, "正在提交...")
        }

        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(context, "提交成功")
            uid = ""
            onModifySuccess?.invoke()
            for (i in 0 until listNiceAdd.size){
                listNiceAdd[i] = 0
                listBadAdd[i] = 0
            }
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(context)
            for (i in 0 until listNiceCount.size){
                listNiceCount[i] = listNiceCount[i] - listNiceAdd[i]
                listBadCount[i] = listBadCount[i] - listBadAdd[i]
            }
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
        arrayListOf(0,0,0,0,0,0)
    }
    private val listNiceAdd by lazy {
        arrayListOf(0,0,0,0,0,0)
    }
    private val listBadAdd by lazy {
        arrayListOf(0,0,0,0,0,0)
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
        arrayListOf(0,0,0,0,0,0)
    }
    init {
        performanceRecycler.layoutManager = GridLayoutManager(context, 3)
        performanceRecycler.adapter = adapter

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

        adapter.setOnItemClickListener { _, view, position ->
            if (adapter.isNice){
                val count = listNiceAdd[position]
                listNiceAdd[position] = count + 1
            }else{
                listBadAdd[position]
                val count = listBadAdd[position]
                listBadAdd[position] = count + 1
            }
            adapter.notifyItemChanged(position)
        }

        confirmBtn.setOnClickListener{
            var isModify = false
            listNiceAdd.forEach {
                if (it != 0){
                    isModify = true
                    return@forEach
                }
            }
            if (!isModify){
                listBadAdd.forEach {
                    if (it != 0){
                        isModify = true
                        return@forEach
                    }
                }
            }
            if (!isModify){
                T.show(context, "没有新增课堂表现")
            }else{
                for (i in 0 until listNiceCount.size){
                    listNiceCount[i] = listNiceCount[i] + listNiceAdd[i]
                    listBadCount[i] = listBadCount[i] + listBadAdd[i]
                }
                TeacherApi.modifyPerformance(uid, listNiceCount, listBadCount, addRequest)
                dismiss()
            }
        }
    }

    fun show(uid: String?, headUrl: String?, name: String?) {
        if (this.uid != uid){
            this.uid = uid
            EBagApi.personalPerformance(request, uid)
            headImg.loadHead(headUrl)
            nameTv.text = name
            for (i in 0 until listNiceAdd.size){
                listNiceAdd[i] = 0
                listBadAdd[i] = 0
            }
        }
        super.show()
    }

    inner class MyAdapter: BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_personal_performance){
        var isNice = true
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun convert(helper: BaseViewHolder, item: Int) {
            val addCount = if(isNice){
                helper.setImageResource(R.id.image,listNiceIcon[helper.adapterPosition])
                        .setText(R.id.text,listNiceWords[helper.adapterPosition])
                        .setBackgroundRes(R.id.tvTag, R.drawable.blue_point)
                        .setTextColor(R.id.tvAdd, context.resources.getColor(R.color.blue))
                listNiceAdd[helper.adapterPosition]
            }else{
                helper.setImageResource(R.id.image,listBadIcon[helper.adapterPosition])
                        .setText(R.id.text,listBadWords[helper.adapterPosition])
                        .setBackgroundRes(R.id.tvTag, R.drawable.red_point)
                        .setTextColor(R.id.tvAdd, context.resources.getColor(R.color.red))
                listBadAdd[helper.adapterPosition]
            }
            helper.setText(R.id.tvAdd, if (addCount == 0) "" else "+$addCount")

            helper.setText(R.id.tvTag, "$item")
        }
    }
}