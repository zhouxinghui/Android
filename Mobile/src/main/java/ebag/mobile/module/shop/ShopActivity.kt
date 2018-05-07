package ebag.mobile.module.shop

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.T
import ebag.mobile.R
import ebag.mobile.base.ActivityUtils
import ebag.mobile.bean.ShopListBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.activity_shop.*

/**
 * Created by fansan on 2018/3/16.
 */
class ShopActivity : BaseActivity() {

    private var page = 1
    private lateinit var mAdapter: ShopAdapter
    private val mData: MutableList<ShopListBean.ListBean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUtils.addActivity(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_shop

    override fun initViews() {


        shop_recyclerview.layoutManager = GridLayoutManager(this, 2)
        mAdapter = ShopAdapter(this, R.layout.item_shop_recyclerview, mData)
        shop_recyclerview.adapter = mAdapter
        shop_recyclerview.addItemDecoration(GridSpacingItemDecoration(5, resources.getDimensionPixelOffset(R.dimen.x12), true))
        initListener()
        request()
        queryShopCar()

    }

    private fun request() {

        EBagApi.getShopList(page, object : RequestCallBack<ShopListBean>() {

            override fun onStart() {
                super.onStart()
                shop_stateview.showLoading()
            }

            override fun onSuccess(entity: ShopListBean?) {
                mData.addAll(entity?.list as MutableList)
                mAdapter.notifyDataSetChanged()
                shop_stateview.showContent()
                page += 1
            }

            override fun onError(exception: Throwable) {
                shop_stateview.showError()
            }

        })

    }

    private fun queryShopCar() {
        EBagApi.queryShopCar(object : RequestCallBack<MutableList<ShopListBean.ListBean>>() {

            override fun onSuccess(entity: MutableList<ShopListBean.ListBean>?) {
                shop_carcount.text = entity?.size.toString()
            }


            override fun onError(exception: Throwable) {
                exception.handleThrowable(this@ShopActivity)
            }

        })
    }

    private fun initListener() {

        shop_stateview.setOnRetryClickListener {
            request()
        }


        mAdapter.setOnItemChildClickListener { _, _, position ->
            EBagApi.addGoods2Car(mData[position].id.toString(), "1", object : RequestCallBack<String>() {

                override fun onSuccess(entity: String?) {
                    T.show(this@ShopActivity, "添加成功")
                    queryShopCar()
                }

                override fun onError(exception: Throwable) {
                    T.show(this@ShopActivity, "添加失败")
                }

            })
        }

        relativeLayout.setOnClickListener {
            startActivity(Intent(this, ShopCarActivity::class.java))
        }

        mAdapter.setOnLoadMoreListener({

            EBagApi.getShopList(page, object : RequestCallBack<ShopListBean>() {

                override fun onSuccess(entity: ShopListBean?) {
                    if (entity?.list?.size != 0) {
                        mData.addAll(entity?.list as MutableList)
                        mAdapter.loadMoreComplete()
                        page += 1
                    } else {
                        mAdapter.loadMoreEnd()
                    }
                }

                override fun onError(exception: Throwable) {
                    mAdapter.loadMoreFail()
                }

            })

        }, shop_recyclerview)


        mAdapter.setOnItemClickListener { adapter, view, position ->

            val intent = Intent(this, GoodsDetailActivity::class.java)
            intent.putExtra("id", mData[position].id.toString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        queryShopCar()
    }

}