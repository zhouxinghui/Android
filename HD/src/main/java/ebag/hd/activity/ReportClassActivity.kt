package ebag.hd.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.StringUtils
import ebag.hd.R
import ebag.hd.base.Constants
import ebag.hd.bean.ReportBean
import ebag.hd.homework.DoHomeworkActivity
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.activity_report_class.*

/**
 * @author caoyu
 * @date 2018/1/24
 * @description
 */
class ReportClassActivity: BaseActivity() {

    companion object {
        fun jump(context: Context, homeworkId: String, workType: String, studentId: String = "", studentName: String = ""){
            context.startActivity(
                    Intent(context, ReportClassActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("studentId", studentId)
                            .putExtra("workType", workType)
                            .putExtra("studentName", studentName)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_report_class
    }

    private lateinit var homeworkId: String
    private var studentId = ""
    private var workType = ""
    private var studentName = ""
    override fun initViews() {

        homeworkId = intent.getStringExtra("homeworkId") ?: ""
        studentId = intent.getStringExtra("studentId") ?: ""
        workType = intent.getStringExtra("workType") ?: ""
        studentName = intent.getStringExtra("studentName") ?: ""
        fillData()
    }

    private fun fillData(){
        if(!StringUtils.isEmpty(studentName)){
            titleView.setTitle(studentName)
        }else{
            titleView.setTitle("作业报告")
        }


        titleView.setRightText("作业详情"){
            DoHomeworkActivity.jump(this, homeworkId, Constants.REPORT_TYPE, workType, studentId)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        stateView.setOnRetryClickListener {
            getReport()
        }

        getReport()
    }

    private val adapter = Adapter()
    private val reportRequest = object :RequestCallBack<ReportBean>(){

        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: ReportBean?) {
            if(entity == null){
                stateView.showEmpty("暂未生成报告，请稍后重试！")
            }else{
                stateView.showContent()
                adapter.setNewData(entity.homeWorkRepDetailVos)

                scoreRound.progress = entity.totalScore.toInt()
                var spannableString = SpannableString("总分\n${entity.totalScore}")
                spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x34))
                        , 3, 3 + "${entity.totalScore}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                scoreTv.text = spannableString

                heightRound.progress = entity.maxScore.toInt()
                spannableString = SpannableString("最高分\n${entity.maxScore}")
                spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x34))
                        , 4, 4 + "${entity.maxScore}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                heightTv.text = spannableString

                errorRound.progress = entity.errorNum
                spannableString = SpannableString("错题\n${entity.errorNum}")
                spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.x34))
                        , 3, 3 + "${entity.errorNum}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                errorTv.text = spannableString
            }

        }

        override fun onError(exception: Throwable) {
            if(exception is MsgException){
                exception.handleThrowable(this@ReportClassActivity)
                stateView.showError(exception.message)
            }else{
                stateView.showError()
            }

        }

    }

    private fun getReport(){
        EBagApi.homeworkReport(homeworkId, studentId, reportRequest)
    }

    inner class Adapter: BaseQuickAdapter<ReportBean.ReportDetailBean, BaseViewHolder>(R.layout.item_activity_report_class){

        override fun convert(helper: BaseViewHolder, item: ReportBean.ReportDetailBean?) {
            helper.setText(R.id.questionType, item?.questionTypeName)
                    .setText(R.id.count, "${item?.questionNum}")
                    .setText(R.id.errorCount, "${item?.errorCount}")
                    .setText(R.id.score, "${String.format("%.02f",item?.questionScore)}")
                    .setBackgroundRes(R.id.layout,if(helper.adapterPosition % 2 == 0) R.color.light_blue else R.color.white)
        }

    }
}