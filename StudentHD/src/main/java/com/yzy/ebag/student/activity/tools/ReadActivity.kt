package com.yzy.ebag.student.activity.tools

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.student.R
import com.yzy.ebag.student.TestFragment
import com.yzy.ebag.student.base.BaseListTabActivity
import com.yzy.ebag.student.base.UnitAdapter
import com.yzy.ebag.student.base.UnitBean
import ebag.core.http.network.RequestCallBack
import ebag.core.util.L
import java.io.IOException
import java.nio.charset.Charset


/**
 * @author caoyu
 * @date 2018/1/21
 * @description
 */
class ReadActivity : BaseListTabActivity<String, MultiItemEntity>() {

    companion object {
        fun jump(context: Context){
            context.startActivity(Intent(context, ReadActivity::class.java))
        }
    }

    private lateinit var tvMaterial: TextView
    override fun loadConfig() {
        setTitleContent("跟读")
        setLeftWidth(resources.getDimensionPixelSize(R.dimen.x368))
        val ss = getFromAsset("unit.json")
        L.e("JSON", ss)
        val list = JSON.parseArray(ss, UnitBean::class.java)

        val view = layoutInflater.inflate(R.layout.layout_read_header,null)
        tvMaterial = view.findViewById(R.id.text)
        tvMaterial.text = "这是教材名字"
        addLeftHeaderView(view)

        withTabData(list)
    }

    /**
     * 从asset中获取文件并读取数据
     * @param fileName
     * @return
     */
    private fun getFromAsset(fileName: String): String {
        var result = ""
        try {
            val input = resources.assets.open(fileName)//从Assets中的文件获取输入流
            val length = input.available()                           //获取文件的字节数
            val buffer = ByteArray(length)                      //创建byte数组
            input.read(buffer)                                    //将文件中的数据读取到byte数组中
            result = String(buffer, Charset.forName("UTF-8"))         //将byte数组转换成指定格式的字符串
            input.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return result
    }

    override fun requestData(requestCallBack: RequestCallBack<String>) {
    }

    override fun parentToList(parent: String?): List<UnitBean>? {
        return null
    }

    override fun getLeftAdapter(): BaseQuickAdapter<MultiItemEntity, BaseViewHolder> {
        return UnitAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Fragment {
        return TestFragment.newInstance()
    }

    override fun getViewPagerSize(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Int {
        return 1
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        val item = adapter.getItem(position)
        if(item is UnitBean) {
            if (item.isExpanded) {
                adapter.collapse(position)
            } else {
                adapter.expand(position)
            }
        }else{
            item as UnitBean.ChapterBean
            (adapter as UnitAdapter).selectSub = item
        }
    }
}