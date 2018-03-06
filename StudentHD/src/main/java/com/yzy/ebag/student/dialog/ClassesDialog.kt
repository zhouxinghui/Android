package com.yzy.ebag.student.dialog

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.ClassListInfoBean
import ebag.hd.base.BaseFragmentDialog
import kotlinx.android.synthetic.main.dialog_classes.*

/**
 * @author caoyu
 * @date 2018/1/16
 * @description 我的班级 列表
 */
class ClassesDialog : BaseFragmentDialog() {


    companion object {
        fun newInstance(): ClassesDialog {
            return ClassesDialog()
        }
    }

    private val adapter = Adapter()
    private var list: List<ClassListInfoBean>? = null
    private var listener: ((ClassListInfoBean?) -> Unit)? = null
    private var isListUpdate = false
    private var classId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    fun updateData(list: List<ClassListInfoBean>?, classId: String){
        if(list != null && list != this.list){
            this.list = list
            if(classId.isBlank() && list.isNotEmpty())
                this.classId = list[0].classId
            else
                this.classId = classId
            isListUpdate = true
        }
    }

    override fun onResume() {
        super.onResume()
        if(isListUpdate){
            isListUpdate = false
            adapter.setNewData(list)
        }
    }

    override fun getBundle(bundle: Bundle?) {
    }

    override fun getLayoutRes(): Int {
        return R.layout.dialog_classes
    }

    fun setOnClassChooseListener(listener: (ClassListInfoBean?) -> Unit){
        this.listener = listener
    }

    override fun initView(view: View) {
//        tvConfirm.setOnClickListener {
//            if(etCode.text.isNullOrBlank()){
//                T.show(mContext,"请输入班级邀请码")
//            }
//        }

        btnClose.setOnClickListener {
            dismiss()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(mContext)

        adapter.setOnItemClickListener { _, _, position ->
            this.classId = adapter.getItem(position)?.classId ?: ""
            adapter.notifyDataSetChanged()
            listener?.invoke(adapter.getItem(position))
        }
    }



    inner class Adapter: BaseQuickAdapter<ClassListInfoBean,BaseViewHolder>(R.layout.item_dialog_classes){

        override fun convert(helper: BaseViewHolder, item: ClassListInfoBean?) {
            helper.setText(R.id.text,item?.className)
            helper.getView<View>(R.id.text).isSelected = item?.classId == classId
        }
    }
}