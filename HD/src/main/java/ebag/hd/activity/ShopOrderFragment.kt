package ebag.hd.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import ebag.core.base.BaseFragment
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.T
import ebag.core.widget.empty.StateView
import ebag.hd.R
import ebag.hd.adapter.OrderListAdapter
import ebag.hd.bean.QueryOrderBean
import ebag.hd.bean.ShopListBean
import ebag.hd.bean.ShopStatusBean
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
    private lateinit var mAdapter: OrderListAdapter

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
        mAdapter = OrderListAdapter(activity, R.layout.item_my_order, mData)
        shoporder_recyclerview.adapter = mAdapter
        shoporder_recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        refreshlayout.setOnRefreshListener {
            request()
        }

        shoporder_stateview.setOnRetryClickListener {
            request()
        }
        //adapter.setOnItemClickListener { adapter, view, position -> startActivity(Intent(activity, OrderDetailsActivity::class.java)) }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->

            when {
                (view as TextView).text == "确认收货" -> {
                    val oid = mData[position].oid
                    val orderProductVOs = mData[position].orderProductVOs
                    val list: MutableList<ShopStatusBean> = mutableListOf()
                    orderProductVOs.forEach {
                        val s = ShopStatusBean(it.shopId, it.numbers)
                        list.add(s)
                    }
                    EBagApi.updateShopOrderStaus(oid, list, object : RequestCallBack<String>() {


                        override fun onSuccess(entity: String?) {
                            T.show(activity, "收货成功")
                            mAdapter.remove(position)
                            mAdapter.notifyItemRemoved(position)
                            if (mData.isEmpty()) {
                                stateView.showEmpty()
                            }
                        }

                        override fun onError(exception: Throwable) {
                            exception.handleThrowable(activity)
                        }

                    })

                }
                view.text == "付款" -> {
                    val intent = Intent(activity, OrderDetailsActivity::class.java)
                    intent.putExtra("number", mData[position].oid)
                    val data: ArrayList<ShopListBean.ListBean> = arrayListOf()
                    mData[position].orderProductVOs.forEach {
                        val bean = ShopListBean.ListBean()
                        bean.discountPrice = it.price
                        bean.shoppingName = it.shopName
                        bean.numbers = it.numbers.toInt()
                        bean.ysbMoney = it.ysbMoney
                        bean.shopUrl = it.shopImg
                        data.add(bean)
                    }
                    intent.putExtra("datas", data)
                    intent.putExtra("which", 1)
                    intent.putExtra("freight", mData[position].freight ?: "0")
                    intent.putExtra("addressStr", mData[position].address)
                    startActivity(intent)
                }
            }
        }
        flag = true
        if (index == -1 && !isLoaded) {
            request()
        }

    }

    private fun request() {
        EBagApi.queryOrder(if (index < 0) {
            ""
        } else {
            index.toString()
        }, object : RequestCallBack<QueryOrderBean>() {
            override fun onStart() {
                if (refreshlayout != null && !refreshlayout.isRefreshing)
                    stateView.showLoading()
            }

            override fun onSuccess(entity: QueryOrderBean?) {
                if (entity?.resultOrderVos!!.size == 0) {
                    stateView.showEmpty()
                } else {
                    if (refreshlayout != null && refreshlayout.isRefreshing) {
                        mAdapter.setNewData(entity.resultOrderVos)
                        refreshlayout?.isRefreshing = false
                    } else {
                        mData.addAll(entity!!.resultOrderVos)
                        mAdapter.notifyDataSetChanged()
                        stateView.showContent()
                    }
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
            } else {
                if (index < 0) {
                    refreshlayout?.isRefreshing = true
                    request()
                }
            }
        } else {

        }
    }

}