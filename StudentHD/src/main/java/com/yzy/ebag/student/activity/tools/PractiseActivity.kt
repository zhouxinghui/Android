package com.yzy.ebag.student.activity.tools

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.yzy.ebag.student.R
import com.yzy.ebag.student.activity.tools.fragment.PractiseFragment
import com.yzy.ebag.student.activity.tools.practise.RecordActivity
import com.yzy.ebag.student.base.BaseListTabActivity
import com.yzy.ebag.student.base.UnitAdapter
import com.yzy.ebag.student.base.UnitBean
import com.yzy.ebag.student.bean.EditionBean
import com.yzy.ebag.student.http.StudentApi
import ebag.core.http.network.RequestCallBack
import java.io.IOException
import java.nio.charset.Charset


/**
 * @author caoyu
 * @date 2018/1/21
 * @description
 */
class PractiseActivity: BaseListTabActivity<EditionBean, MultiItemEntity>() {

    companion object {
        fun jump(context: Context, classId: String){
            context.startActivity(
                    Intent(context,PractiseActivity::class.java)
                            .putExtra("classId", classId)
            )
        }
    }

    private lateinit var tvMaterial: TextView
    private lateinit var classId: String
    override fun loadConfig() {
        classId = intent.getStringExtra("classId") ?: ""
        setTitleContent("选择汉字")
        setLeftWidth(resources.getDimensionPixelSize(R.dimen.x368))
        setMiddleDistance(resources.getDimensionPixelSize(R.dimen.x20))


        val view = layoutInflater.inflate(R.layout.layout_practise_material_header,null)
        tvMaterial = view.findViewById(R.id.text)
        addLeftHeaderView(view)


//        val ss = getFromAsset("unit.json")
//        L.e("JSON", ss)
//        val list = JSON.parseArray(ss, UnitBean::class.java)
//        withTabData(list)

        titleBar.setRightText("记录"){
            RecordActivity.jump(this)
        }
    }

    /**
     * 从asset中获取文件并读取数据
     * @param fileName
     * @return
     */
    fun getFromAsset(fileName: String): String {
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

    override fun requestData(requestCallBack: RequestCallBack<EditionBean>) {
        StudentApi.getUint(classId, "yw", requestCallBack)
    }

    override fun parentToList(parent: EditionBean?): List<UnitBean>? {
        tvMaterial.text = parent?.bookVersion
        return parent?.resultBookUnitOrCatalogVos
    }

    override fun getLeftAdapter(): BaseQuickAdapter<MultiItemEntity, BaseViewHolder> {
        return UnitAdapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): RecyclerView.LayoutManager? {
        return null
    }

    override fun getFragment(pagerIndex: Int, adapter: BaseQuickAdapter<MultiItemEntity, BaseViewHolder>): Fragment {
        return PractiseFragment.newInstance()
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