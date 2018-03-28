package ebag.hd.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.adapter.GoodsParamAdapter
import ebag.hd.bean.GoodsDetailsBean
import ebag.hd.bean.SaveOrderPBean
import ebag.hd.bean.ShopListBean
import ebag.hd.http.EBagApi
import ebag.hd.util.ActivityUtils
import ebag.hd.util.GlideImageLoader
import kotlinx.android.synthetic.main.activity_goods_details.*
import kotlinx.android.synthetic.main.activity_shop.*


/**
 * Created by fansan on 2018/3/16.
 */
class GoodsDetailActivity : BaseActivity() {
    private lateinit var id: String
    private var carCount: Int = 0
    private lateinit var _intent: Intent
    private lateinit var bean: GoodsDetailsBean
    override fun getLayoutId(): Int = R.layout.activity_goods_details
    private var data: MutableList<GoodsDetailsBean.ProductParametersVoBean.ResultGruopVOSBean> = mutableListOf()
    private lateinit var mAdapter: GoodsParamAdapter
    private var bannerList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUtils.addActivity(this)
    }

    override fun initViews() {

        id = intent.getStringExtra("id")
        mAdapter = GoodsParamAdapter(this, data)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = mAdapter
        recyclerview.addItemDecoration(ebag.core.xRecyclerView.manager.DividerItemDecoration(DividerItemDecoration.VERTICAL, 1, Color.parseColor("#e0e0e0")))
        recyclerview.isNestedScrollingEnabled = false
        initListener()
        request()
    }


    private fun initListener() {
        to_buy.setOnClickListener {
            val data = ShopListBean.ListBean()
            data.id = bean.id
            data.shoppingName = bean.shoppingName
            data.price = bean.price
            data.discountPrice = bean.discountPrice
            data.ysbMoney = bean.ysbMoney
            data.numbers = 1
            data.imgUrls = bean.imgUrls
            val datas: ArrayList<ShopListBean.ListBean> = arrayListOf()
            datas.add(data)
            _intent = Intent(this, OrderDetailsActivity::class.java)
            _intent.putExtra("datas", datas)
            createOrderNo()

        }

        add_to_car.setOnClickListener {

            EBagApi.addGoods2Car(id, "1", object : RequestCallBack<String>() {

                override fun onSuccess(entity: String?) {
                    T.show(this@GoodsDetailActivity, "添加成功")
                    queryShopCar()

                }

                override fun onError(exception: Throwable) {
                    T.show(this@GoodsDetailActivity, "添加失败")
                }

            })
        }

        fl_shop_car.setOnClickListener {
            startActivity(Intent(this, ShopCarActivity::class.java))
        }
    }

    private fun request() {

        EBagApi.shopDetails(id, object : RequestCallBack<GoodsDetailsBean>() {

            override fun onStart() {
                super.onStart()
                stateview.showLoading()
            }

            override fun onSuccess(entity: GoodsDetailsBean?) {
                bean = entity!!
                details_titlebar.setTitle(entity.shoppingName)
                tv_name.text = entity.shoppingName
                goods_old_price.text = "￥ ${entity.price}"
                goods_old_price.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                tv_price.text = "￥${entity.discountPrice}"
                details_yb.text = "云币:${entity.ysbMoney}"
                yunfei.text = "运费:${entity.freight}"
                yuexiaoliang.text = "月销量:${entity.saleVolume}"
                data.addAll(entity.productParametersVo!!.resultGruopVOS)
                mAdapter.notifyDataSetChanged()
                banner.setImageLoader(GlideImageLoader())
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                banner.setBannerAnimation(Transformer.Default)
                banner.setDelayTime(3000)
                banner.setIndicatorGravity(BannerConfig.CENTER)
                banner.setImages(entity.imgUrls)
                banner.start()
                stateview.showContent()

            }

            override fun onError(exception: Throwable) {
                stateview.showError()
            }

        })
    }

    private fun queryShopCar() {
        EBagApi.queryShopCar(object : RequestCallBack<MutableList<ShopListBean.ListBean>>() {

            override fun onSuccess(entity: MutableList<ShopListBean.ListBean>?) {
                if (entity!!.size > 0) {
                    tv_tips.visibility = View.VISIBLE
                }
                tv_tips.text = entity.size.toString()
            }


            override fun onError(exception: Throwable) {
                exception.handleThrowable(this@GoodsDetailActivity)
            }

        })
    }


    private fun createOrderNo() {
        EBagApi.createShopOrderNo(object : RequestCallBack<String>() {

            override fun onStart() {
                super.onStart()
                LoadingDialogUtil.showLoading(this@GoodsDetailActivity, "正在生成订单...")
            }

            override fun onSuccess(entity: String?) {
                LoadingDialogUtil.closeLoadingDialog()
                _intent.putExtra("number", entity)
                startActivity(_intent)
            }

            override fun onError(exception: Throwable) {
                LoadingDialogUtil.closeLoadingDialog()
                T.show(this@GoodsDetailActivity, "提交失败")
            }

        })
    }


    override fun onStart() {
        super.onStart()
        banner.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        banner.stopAutoPlay()
    }

    override fun onResume() {
        super.onResume()
        queryShopCar()
    }

}