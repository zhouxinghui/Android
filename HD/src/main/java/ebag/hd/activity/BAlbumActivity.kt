package ebag.hd.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import ebag.core.base.BaseActivity
import ebag.hd.R
import kotlinx.android.synthetic.main.activity_album.*

/**
 * Created by unicho on 2018/3/1.
 */
abstract class BAlbumActivity: BaseActivity() {

    abstract fun getRole(): Int
    companion object {
        const val STUDENT = 1
        const val TEACHER = 2
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_album
    }

    lateinit var classId: String
    override fun initViews() {
        classId = intent.getStringExtra("classId") ?: ""

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.rbHonorAlbum -> {// 班级荣誉
                    viewPager.setCurrentItem(0,false)
                }
                R.id.rbPersonalAlbum -> {// 个人
                    viewPager.setCurrentItem(1,false)
                }
                R.id.rbClassAlbum -> {// 班级
                    viewPager.setCurrentItem(2,false)
                }
            }
        }
        viewPager.adapter = SectionsPagerAdapter(supportFragmentManager, arrayOfNulls(3))
        viewPager.offscreenPageLimit = 1
        viewPager.setCurrentItem(1,false)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        radioGroup.check(R.id.rbHonorAlbum)
                    }
                    1 -> {
                        radioGroup.check(R.id.rbPersonalAlbum)
                    }
                    2 -> {
                        radioGroup.check(R.id.rbClassAlbum)
                    }
                }
            }

        })
    }

    inner class SectionsPagerAdapter(fm: FragmentManager, private val fragments: Array<Fragment?>) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            if (fragments[position] == null) {
                fragments[position] = when(position){
                    // 班级荣誉
                    0 -> AlbumFragment.newInstance(getRole(), classId, AlbumFragment.HONOR_TYPE)
                    // 个人
                    1 -> AlbumFragment.newInstance(getRole(), classId, AlbumFragment.PERSONAL_TYPE)
                    // 班级相册
                    else -> AlbumFragment.newInstance(getRole(), classId, AlbumFragment.CLASS_TYPE)
                }
            }
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}