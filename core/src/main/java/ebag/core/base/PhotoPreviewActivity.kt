package ebag.core.base

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import com.github.chrisbanes.photoview.PhotoView
import com.github.chrisbanes.photoview.PhotoViewAttacher
import ebag.core.R
import ebag.core.util.loadPhoto
import kotlinx.android.synthetic.main.activity_photo_preview.*


class PhotoPreviewActivity : BaseActivity() {
    private val urlList by lazy { intent.getSerializableExtra("urlList") as ArrayList<String>}
    private val currentPosition by lazy { intent.getIntExtra("position", 0) }
    companion object {
        fun jump(context: Context, urlList: List<String>, position: Int){
            context.startActivity(Intent(context, PhotoPreviewActivity::class.java)
                    .putExtra("urlList", urlList as ArrayList<String>)
                    .putExtra("position", position))
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_photo_preview
    }

    override fun initViews() {
        viewPager.adapter = ViewPagerAdapter(urlList)
        viewPager.setCurrentItem(currentPosition, false)
        positionTv.text = "${currentPosition + 1}/${urlList.size}"
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                positionTv.text = "${position + 1}/${urlList.size}"
            }
        })
    }

    inner class ViewPagerAdapter(private val urlList: List<String>): PagerAdapter(){
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return urlList.size
        }

        /**
         * 实例化 一个 页卡
         */
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            // 添加一个 页卡
            val photoView = PhotoView(container.context)
            val attacher = PhotoViewAttacher(photoView)
            photoView.loadPhoto(urlList[position])
            attacher.update()
            attacher.setOnPhotoTapListener{_, _, _ ->  finish()}
            container.addView(photoView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            return photoView
        }

        /**
         * 销毁 一个 页卡
         */
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            // 删除
            container.removeView(`object` as View)
        }
    }
}
