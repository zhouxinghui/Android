package com.yzy.ebag.teacher.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.teacher.R
import ebag.core.http.network.RequestCallBack
import ebag.hd.adapter.UnitAdapter
import ebag.hd.base.BaseListTabActivity

class MyPrepareActivity : BaseListTabActivity<List<MultiItemEntity>, MultiItemEntity>() {
    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context, MyPrepareActivity::class.java))
        }
    }
    override fun loadConfig() {
        titleBar.setTitle("教学课件")
        titleBar.setRightText("资源库", {

        })

        val changeGradeTv = TextView(this)
        changeGradeTv.text = "更换\n班级"
        changeGradeTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.tv_normal))
        changeGradeTv.setTextColor(resources.getColor(R.color.white))
        changeGradeTv.setBackgroundResource(R.drawable.blue_oval)
        changeGradeTv.gravity = Gravity.CENTER
        val params = RelativeLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.x80), resources.getDimensionPixelSize(R.dimen.x80))
        params.addRule(RelativeLayout.ALIGN_PARENT_END)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params.rightMargin = resources.getDimensionPixelSize(R.dimen.x20)
        params.bottomMargin = resources.getDimensionPixelSize(R.dimen.y20)
        changeGradeTv.layoutParams = params
        addExtraView(changeGradeTv, 2)

        setLeftWidth(resources.getDimensionPixelSize(R.dimen.x368))
    }

    override fun requestData(requestCallBack: RequestCallBack<List<MultiItemEntity>>) {

    }

    override fun parentToList(parent: List<MultiItemEntity>?): List<MultiItemEntity>? {
        return parent
    }

    override fun getLeftAdapter(): BaseQuickAdapter<MultiItemEntity, BaseViewHolder> {
        return UnitAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Fragment {
        return Fragment()
    }

    override fun getViewPagerSize(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Int {
        return 1
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

    }

}
