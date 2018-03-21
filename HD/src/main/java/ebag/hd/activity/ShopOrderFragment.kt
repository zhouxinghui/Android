package ebag.hd.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.widget.empty.StateView
import ebag.hd.R
import ebag.hd.adapter.OrderListAdapter
import ebag.hd.bean.QueryOrderBean
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.fragment_shoporder.*

/**
 * Created by fansan on 2018/3/15.
 */
class ShopOrderFragment : BaseFragment() {
    private var index: Int = 0
    private var mData:ArrayList<QueryOrderBean.ResultOrderVosBean> = arrayListOf()
    //private lateinit var stateView: StateView

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
        //rootView.findViewById<StateView>(R.id.shoporder_stateview)
        shoporder_recyclerview.layoutManager = LinearLayoutManager(activity)
        val adapter = OrderListAdapter(activity,R.layout.item_my_order, mData)
        shoporder_recyclerview.adapter = adapter
        shoporder_recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        //adapter.setOnItemClickListener { adapter, view, position -> startActivity(Intent(activity, OrderDetailsActivity::class.java)) }


    }

    private fun request(){
        EBagApi.queryOrder(index.toString(),object: RequestCallBack<QueryOrderBean>(){
            override fun onStart() {
                shoporder_stateview.showLoading()
            }
            override fun onSuccess(entity: QueryOrderBean?) {
                mData.addAll(entity!!.resultOrderVos)
                shoporder_stateview.showContent()
            }

            override fun onError(exception: Throwable) {
                shoporder_stateview.showError()
                exception.handleThrowable(activity)
            }


        })
    }



    override fun onVisiable() {
        super.onVisiable()
        request()
    }


}