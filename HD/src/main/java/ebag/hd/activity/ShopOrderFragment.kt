package ebag.hd.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ebag.core.base.BaseFragment
import ebag.hd.R
import ebag.hd.adapter.OrderListAdapter
import kotlinx.android.synthetic.main.fragment_shoporder.*

/**
 * Created by fansan on 2018/3/15.
 */
class ShopOrderFragment : BaseFragment() {
    private var index: Int = 0

    companion object {
        fun newInstance(pageIndex: Int): ShopOrderFragment {
            val fragment = ShopOrderFragment()
            val bundle = Bundle()
            bundle.putInt("pageIndex", pageIndex)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_shoporder

    override fun getBundle(bundle: Bundle?) {
        index = bundle!!.getInt("pageIndex")
    }

    override fun initViews(rootView: View) {
        shoporder_recyclerview.layoutManager = LinearLayoutManager(activity)
        val data = mutableListOf("1", "2", "3", "5")
        val adapter = OrderListAdapter(R.layout.item_my_order, data)
        shoporder_recyclerview.adapter = adapter
        shoporder_recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        when (index) {
            1 -> shoporder_stateview.showLoading()
            2 -> shoporder_stateview.showEmpty()
            3 -> shoporder_stateview.showError()
            else -> shoporder_stateview.showContent()
        }

        adapter.setOnItemClickListener { adapter, view, position -> startActivity(Intent(activity, OrderDetailsActivity::class.java)) }


    }

    override fun onVisiable() {
        super.onVisiable()
    }


}