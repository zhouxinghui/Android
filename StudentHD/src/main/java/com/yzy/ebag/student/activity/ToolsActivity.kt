package com.yzy.ebag.student.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.yzy.ebag.student.R
import com.yzy.ebag.student.base.BaseListActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.util.T
import ebag.core.xRecyclerView.adapter.RecyclerAdapter
import ebag.core.xRecyclerView.adapter.RecyclerViewHolder

/**
 * Created by unicho on 2018/1/8.
 */
class ToolsActivity : BaseListActivity<Int>() {

    private val list = intArrayOf(R.drawable.tool_btn_calligraphy,R.drawable.tool_btn_read,
            R.drawable.tool_btn_pinyin,R.drawable.tool_btn_letter,R.drawable.tool_btn_formula,
            R.drawable.tool_btn_song)

    override fun loadConfig(intent: Intent) {
        onlyView(true)

    }

    override fun initViews() {
        super.initViews()
        //添加插画
        val params = RelativeLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.x172),resources.getDimensionPixelSize(R.dimen.x165))
        val imageView = ImageView(this)
        imageView.setImageResource(R.drawable.tool_img_corner)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE)
        params.bottomMargin = resources.getDimensionPixelSize(R.dimen.x24)
        params.rightMargin = resources.getDimensionPixelSize(R.dimen.x24)
        rootLayout.addView(imageView,params)
        titleBar.setTitle(R.string.tool)
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<List<Int>>) {
    }

    override fun getAdapter(): RecyclerAdapter<Int> {
        val adapter = ToolsAdapter()
        adapter.datas = list.asList()
        return adapter
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return GridLayoutManager(this,4)
    }


    class ToolsAdapter : RecyclerAdapter<Int>(R.layout.activity_tools_item) {

        override fun fillData(setter: RecyclerViewHolder, position: Int, entity: Int) {
            setter.setImageResource(R.id.image,entity)
        }
    }

    override fun onItemClick(holder: RecyclerViewHolder, view: View, position: Int) {
        T.show(this,"点击了第${position + 1}个条目")
    }
}