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
    private var mData: ArrayList<QueryOrderBean.ResultOrderVosBean> = arrayListOf()
    private lateinit var stateView: StateView
    private var flag = false
    private var isLoaded = false

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
        stateView = rootView.findViewById(R.id.shoporder_stateview)
        shoporder_recyclerview.layoutManager = LinearLayoutManager(activity)
        val adapter = OrderListAdapter(activity, R.layout.item_my_order, mData)
        shoporder_recyclerview.adapter = adapter
        shoporder_recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        //adapter.setOnItemClickListener { adapter, view, position -> startActivity(Intent(activity, OrderDetailsActivity::class.java)) }
        flag = true
        if (index == 0 && !isLoaded){
            request()
        }

    }

    private fun request() {
        EBagApi.queryOrder(index.toString(), object : RequestCallBack<QueryOrderBean>() {
            override fun onStart() {
                stateView.showLoading()
            }

            override fun onSuccess(entity: QueryOrderBean?) {
                if (entity?.resultOrderVos!!.size == 0) {
                    stateView.showEmpty()
                } else {
                    mData.addAll(entity!!.resultOrderVos)
                    stateView.showContent()
                }
                isLoaded = true
            }

            override fun onError(exception: Throwable) {
                stateView.showError()
                exception.handleThrowable(activity)
            }


        })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (flag and !isLoaded) {
                request()
            }
        } else {

        }
    }


}