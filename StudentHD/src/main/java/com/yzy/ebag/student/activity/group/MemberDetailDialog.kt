package com.yzy.ebag.student.activity.group

import android.os.Bundle
import android.view.View
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.GroupUserBean
import ebag.core.util.loadHead
import ebag.hd.base.BaseFragmentDialog
import kotlinx.android.synthetic.main.dialog_member_detail.*

/**
 * Created by unicho on 2018/3/6.
 */
class MemberDetailDialog: BaseFragmentDialog() {

    private var isDataUpdate = false
    private var groupUserBean: GroupUserBean? = null

    companion object {
        fun newInstance(): MemberDetailDialog{
            return MemberDetailDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun getBundle(bundle: Bundle?) {
    }


    fun updateData(groupUserBean: GroupUserBean?){
        this.groupUserBean = groupUserBean
        this.isDataUpdate = true
    }

    override fun onResume() {
        super.onResume()
        if(this.isDataUpdate){
            this.isDataUpdate = false
            showData()
        }
    }
    override fun getLayoutRes(): Int {
        return R.layout.dialog_member_detail
    }

    override fun initView(view: View) {
        btnClose.setOnClickListener {
            dismiss()
        }
    }

    fun showData(){
        imgHead.loadHead(groupUserBean?.headUrl)
        tvName.text = groupUserBean?.name
        tvBaseInfo.text = ""
    }
}