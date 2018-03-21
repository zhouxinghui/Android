package ebag.hd.activity

import android.content.res.Resources
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.hd.R
import ebag.hd.bean.QueryOrderBean
import ebag.hd.http.EBagApi
import ebag.hd.ui.view.BadgeView
import kotlinx.android.synthetic.main.activity_myorder.*
import java.lang.reflect.Field

/**
 * Created by fansan on 2018/3/15.
 */
class ShopOrderActivity : BaseActivity() {

    private val pics: Array<Int> = arrayOf(R.drawable.icon_my_order_full_order,
            R.drawable.icon_my_order_pending_payment,
            R.drawable.icon_my_order_shipment_pending,
            R.drawable.icon_my_order_goods_to_be_received)
    private val badgeCountList: MutableList<Int> = mutableListOf()
    private val fragmentList: MutableList<Fragment> = mutableListOf()
    private val titleList: MutableList<String> = mutableListOf()
    private lateinit var viewPagerAdapter:ViewPagerAdapter

    override fun getLayoutId(): Int = R.layout.activity_myorder

    override fun initViews() {

        request()

    }

    private fun request() {

        EBagApi.queryOrder("1",object:RequestCallBack<QueryOrderBean>(){
            override fun onStart() {
                super.onStart()
                stateView.showLoading()
            }
            override fun onSuccess(entity: QueryOrderBean?) {
                badgeCountList.add(0)
                badgeCountList.add(entity?.statusCount!!.status_1)
                badgeCountList.add(entity.statusCount!!.status_2)
                badgeCountList.add(entity.statusCount!!.status_3)
                tab_layout_id.post { setIndicator(tab_layout_id, 80, 80) }
                initFragmentList()
                viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, fragmentList, titleList)
                view_pager.adapter = viewPagerAdapter
                view_pager.offscreenPageLimit = 3
                tab_layout_id.setupWithViewPager(view_pager)
                for (i in 0 until tab_layout_id.tabCount) {
                    val tab = tab_layout_id.getTabAt(i) ?: return
                    if (i >= pics.size) {
                        tab.setIcon(pics[0])
                        return
                    }
                    tab.setIcon(pics[i])
                }

                setUpTabBadge()
                stateView.showContent()
            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(this@ShopOrderActivity)
                stateView.showError()
            }

        })
    }

    private fun initFragmentList() {
        titleList.add("全部订单")
        fragmentList.add(ShopOrderFragment.newInstance(0))
        titleList.add("待付款")
        fragmentList.add(ShopOrderFragment.newInstance(1))
        titleList.add("待发货")
        fragmentList.add(ShopOrderFragment.newInstance(2))
        titleList.add("待收货")
        fragmentList.add(ShopOrderFragment.newInstance(3))
    }

    private fun setIndicator(tab: TabLayout, leftDip: Int, rightDip: Int) {
        val tabLayout = tab.javaClass
        val tabStrip: Field?
        tabStrip = tabLayout.getDeclaredField("mTabStrip")
        tabStrip.isAccessible = true
        val llTab: LinearLayout?
        llTab = tabStrip.get(tab) as LinearLayout
        val left = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip.toFloat(), Resources.getSystem().displayMetrics).toInt()
        val right = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip.toFloat(), Resources.getSystem().displayMetrics).toInt()
        for (i in 0 until llTab.childCount) {
            val child = llTab.getChildAt(i)
            child.setPadding(0, 0, 0, 0)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.leftMargin = left
            params.rightMargin = right
            child.layoutParams = params
            child.invalidate()
        }

    }


    inner class ViewPagerAdapter(manager: FragmentManager, fragmentList: MutableList<Fragment>, title: MutableList<String>) : FragmentPagerAdapter(manager) {

        private val mTitle: MutableList<String> by lazy { title }
        private val mFragmentList: MutableList<Fragment> by lazy { fragmentList }

        override fun getItem(position: Int): Fragment = mFragmentList[position]

        override fun getCount(): Int = mFragmentList.size

        override fun getPageTitle(position: Int): CharSequence = mTitle[position]

        fun getTabItemView(position: Int): View {
            val view = View.inflate(this@ShopOrderActivity, R.layout.tab_layout_item, null)
            val textView = view.findViewById<TextView>(R.id.tab_tv_id)
            textView.text = mTitle[position]
            val imageView = view.findViewById<ImageView>(R.id.img_id)
            imageView.setImageResource(pics[position])
            val target = view.findViewById<View>(R.id.tv_tips)
            val badgeView = BadgeView(this@ShopOrderActivity)
            badgeView.setTargetView(target)
            badgeView.textSize = 10.0f
            badgeView.badgeCount = badgeCountList[position]
            if (position == 0 || count == 0)
                badgeView.visibility = View.GONE
            else
                badgeView.visibility = View.VISIBLE

            return view

        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            super.destroyItem(container, position, `object`)
        }

    }

    private fun setUpTabBadge(){
        for (i in 0..tab_layout_id.tabCount){
            val tab = tab_layout_id.getTabAt(i)
            val view = tab?.customView
            if (view != null){
                val parent = view.parent
                if (parent!=null){
                    (parent as ViewGroup).removeView(view)
                }
            }
            tab?.customView =viewPagerAdapter.getTabItemView(i)
        }
        tab_layout_id.getTabAt(tab_layout_id.selectedTabPosition)?.customView?.isSelected = true
    }

}