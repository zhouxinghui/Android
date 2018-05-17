package ebag.mobile.module.homework

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.WindowManager
import ebag.core.base.BaseActivity
import ebag.core.base.BaseDialog
import ebag.core.bean.QuestionBean
import ebag.core.bean.QuestionTypeUtils
import ebag.core.bean.TypeQuestionBean
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.mobile.R
import ebag.mobile.base.Constants
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.activity_homework_desc.*

/**
 * Created by YZY on 2018/4/28.
 */
class HomeworkDescActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_homework_desc

    companion object {
        fun jump(context: Context, homeworkId: String, workType: String, studentId: String? = null, testTime: Int = 0) {
            context.startActivity(
                    Intent(context, HomeworkDescActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("testTime", testTime)
                            .putExtra("studentId", studentId)
                            .putExtra("workType", workType)
            )
        }
    }

    private lateinit var homeworkId: String
    private var workType = ""
    private var studentId = ""
    private val typeAdapter by lazy {
        val adapter = QuestionTypeAdapter()
        adapter.showResult = true
        adapter
    }
    private lateinit var typeQuestionList: List<TypeQuestionBean?>
    private var fragments: Array<HomeworkDescFragment?>? = null

    private val typeDialog by lazy {
        TypeDialog()
    }
    private val detailRequest = object : RequestCallBack<List<TypeQuestionBean>>() {
        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: List<TypeQuestionBean>?) {
            typeQuestionList = entity ?: ArrayList()
            if (typeQuestionList.isNotEmpty()) {
                titleBar.setTitle(QuestionTypeUtils.getTitle(typeQuestionList[0]?.type))
                (0 until typeQuestionList.size).forEach {
                    typeQuestionList[it]?.initQuestionPosition(it)
                }
                stateView.showContent()
                typeAdapter.setNewData(typeQuestionList)
                fragments = arrayOfNulls(typeQuestionList.size)
                viewPager.adapter = ViewPagerAdapter(fragments)
                viewPager.offscreenPageLimit = 2
            } else {
                stateView.showEmpty()
            }
        }

        override fun onError(exception: Throwable) {
//            if(exception is MsgException)
//                stateView.showError(exception.message)
            stateView.showError()
            exception.handleThrowable(this@HomeworkDescActivity)
        }
    }

    override fun initViews() {
        homeworkId = intent.getStringExtra("homeworkId") ?: ""
        workType = intent.getStringExtra("workType") ?: ""
        studentId = intent.getStringExtra("studentId") ?: ""

        /*val footerView = layoutInflater.inflate(R.layout.homework_desc_foot_view, null) as FrameLayout
        val exchangeTv = footerView.findViewById<TextView>(R.id.tv)
        exchangeTv.setOnClickListener {
            typeDialog.show()
        }
        questionAdapter.addFooterView(footerView)*/
        titleBar.setOnRightClickListener {
            typeDialog.show()
        }

        stateView.setOnRetryClickListener {
            request()
        }
        typeAdapter.onSubItemClickListener = {parentPosition, position ->
            viewPager.setCurrentItem(parentPosition, false)
            fragments!![parentPosition]?.scrollTo(position)
            typeDialog.dismiss()
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                titleBar.setTitle(QuestionTypeUtils.getTitle(typeQuestionList[position]?.type))
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        request()

    }

    private fun request() {
        if (workType == Constants.ERROR_TOPIC_TYPE) {
            EBagApi.getErrorDetail(homeworkId, studentId, detailRequest)
        } else {
            EBagApi.getQuestions(homeworkId, workType, studentId, detailRequest)
        }
    }

    private inner class TypeDialog : BaseDialog(this) {
        override fun getLayoutRes(): Int = R.layout.recycler_view_layout

        override fun setWidth(): Int = WindowManager.LayoutParams.MATCH_PARENT
        override fun setHeight(): Int = resources.getDimensionPixelSize(R.dimen.y700)
        override fun getGravity(): Int = Gravity.BOTTOM

        init {
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this@HomeworkDescActivity)
            recyclerView.adapter = typeAdapter
        }
    }

    private inner class ViewPagerAdapter(val fragments: Array<HomeworkDescFragment?>?): FragmentPagerAdapter(supportFragmentManager){
        override fun getItem(position: Int): Fragment {
            if (fragments!![position] == null){
                fragments[position] = HomeworkDescFragment.newInstance(
                        typeQuestionList[position]?.questionVos as ArrayList<QuestionBean?>,
                        workType)
                return fragments[position]!!
            }
            return HomeworkDescFragment.newInstance(ArrayList(), workType)
        }

        override fun getCount(): Int = fragments?.size ?: 0
    }
}