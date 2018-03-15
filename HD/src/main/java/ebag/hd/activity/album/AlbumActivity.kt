package ebag.hd.activity.album

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import ebag.core.base.BaseActivity
import ebag.hd.R
import ebag.hd.base.Constants
import kotlinx.android.synthetic.main.activity_album.*

/**
 * Created by unicho on 2018/3/1.
 */
class AlbumActivity : BaseActivity() {


    lateinit var classId: String
    var role = Constants.ROLE_STUDENT
    private val fragments: Array<AlbumFragment?> = arrayOfNulls(3)


    companion object {
        fun jump(context: Context, classId: String, role: Int = Constants.ROLE_STUDENT){
            context.startActivity(
                    Intent(context, AlbumActivity::class.java)
                            .putExtra("classId", classId)
                            .putExtra("role", role)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_album
    }

    override fun initViews() {
        classId = intent.getStringExtra("classId") ?: ""
        role = intent.getIntExtra("role", Constants.ROLE_STUDENT)
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
        viewPager.adapter = SectionsPagerAdapter(supportFragmentManager)
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

                if(fragments[position]?.isRequested == true)
                    fragments[position]?.onRefresh()
//                if(fragments[position]?.isLoaded() == true){
//                    fragments[position]?.onRefresh()
//                }
            }

        })
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            if (fragments[position] == null) {
                fragments[position] = when(position){
                    // 班级荣誉
                    0 -> AlbumFragment.newInstance(role, classId, Constants.HONOR_TYPE)
                    // 个人
                    1 -> AlbumFragment.newInstance(role, classId, Constants.PERSONAL_TYPE)
                    // 班级相册
                    else -> AlbumFragment.newInstance(role, classId, Constants.CLASS_TYPE)
                }
            }
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}