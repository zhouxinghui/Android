package com.yzy.ebag.student.dialog

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.ClassListInfoBean
import kotlinx.android.synthetic.main.dialog_classes.*

/**
 * @author 曹宇
 * @date 2018/1/16
 * @description 我的班级 列表
 */
class ClassesDialog : BaseFragmentDialog() {


    companion object {
        fun newInstance(): ClassesDialog {
            return ClassesDialog()
        }
    }

    private var adapter: Adapter? = null
    private var list: List<ClassListInfoBean>? = null
    private var listener: ((ClassListInfoBean?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    fun updateData(list: List<ClassListInfoBean>?){
        if(list != null && list != this.list){
            this.list = list
            adapter?.setNewData(list)
        }

    }

    override fun getBundle(bundle: Bundle?) {
        list = bundle?.getParcelableArrayList("list")
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

        recyclerView.layoutManager = LinearLayoutManager(mContext)
        adapter = Adapter()

        recyclerView.adapter = adapter
        adapter?.setNewData(list)

        adapter?.setOnItemClickListener { _, _, position ->
            adapter?.selected = position
            listener?.invoke(adapter?.getItem(position))
        }
    }



    class Adapter: BaseQuickAdapter<ClassListInfoBean,BaseViewHolder>(R.layout.item_dialog_classes){

        var selected = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

        override fun convert(helper: BaseViewHolder, item: ClassListInfoBean?) {
            helper.setText(R.id.text,item?.className)
            helper.getView<View>(R.id.text).isSelected = helper.adapterPosition == selected
        }
    }
}