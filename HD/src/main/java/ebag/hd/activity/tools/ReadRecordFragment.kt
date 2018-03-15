package ebag.hd.activity.tools

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseListFragment
import ebag.core.http.network.RequestCallBack
import ebag.hd.R

/**
 * Created by unicho on 2018/3/13.
 */
class ReadRecordFragment : BaseListFragment<ArrayList<String>, String>() {
    private var unitCode = ""
    companion object {
        fun newInstance(): ReadRecordFragment {
            val fragment = ReadRecordFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getBundle(bundle: Bundle?) {
    }

    override fun loadConfig() {
    }

    /**
     * 数据更新
     */
    fun update(unitCode: String?){
        if(this.unitCode != unitCode){
            this.unitCode = unitCode ?: ""
            cancelRequest()
            onRetryClick()
        }
    }

    override fun requestData(page: Int, requestCallBack: RequestCallBack<ArrayList<String>>) {
    }

    override fun parentToList(isFirstPage: Boolean, parent: ArrayList<String>?): List<String>? {
        return parent
    }

    override fun getAdapter(): BaseQuickAdapter<String, BaseViewHolder> {
        return Adapter()
    }

    override fun getLayoutManager(adapter: BaseQuickAdapter<String, BaseViewHolder>): RecyclerView.LayoutManager? {
        return GridLayoutManager(mContext, 2)
    }

    inner class Adapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_tools_record_history){
        override fun convert(helper: BaseViewHolder?, item: String?) {
        }

    }
}