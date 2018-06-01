package ebag.mobile.module.homework

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.StringUtils
import ebag.mobile.R
import ebag.mobile.base.Constants
import ebag.mobile.bean.GiftTeacherBean
import ebag.mobile.bean.ReportBean
import ebag.mobile.http.EBagApi
import kotlinx.android.synthetic.main.activity_work_report.*

/**
 * Created by YZY on 2018/4/28.
 */
class WorkReportActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_work_report

    companion object {
        fun jump(context: Context, homeworkId: String, workType: String, studentId: String = "", studentName: String = "") {
            context.startActivity(
                    Intent(context, WorkReportActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("studentId", studentId)
                            .putExtra("workType", workType)
                            .putExtra("studentName", studentName)
            )
        }
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
        val scoreTv = findViewById<TextView>(R.id.score)
        if (workType == Constants.STZY_TYPE)
            scoreTv.visibility = View.VISIBLE
        else
            scoreTv.visibility = View.GONE
        fillData()
        if (workType == Constants.STZY_TYPE)
            commentLayout.visibility = View.GONE
    }

    private fun fillData() {
        if (!StringUtils.isEmpty(studentName)) {
            titleBar.setTitle(studentName)
        } else {
            titleBar.setTitle("作业报告")
        }


        titleBar.setRightText("作业详情") {
            HomeworkDescActivity.jump(this, homeworkId, workType, studentId)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false

        stateView.setOnRetryClickListener {
            getReport()
        }

        getReport()
    }

    private val adapter = Adapter()
    private val reportRequest = object : RequestCallBack<ReportBean>() {

        override fun onStart() {
            stateView.showLoading()
        }

        override fun onSuccess(entity: ReportBean?) {
            if (entity == null) {
                stateView.showEmpty("暂未生成报告，请稍后重试！")
            } else {
                stateView.showContent()
                queryGift()
                adapter.setNewData(entity.homeWorkRepDetailVos)

                scoreRound.progress = entity.totalScore.toInt()
                var spannableString = SpannableString("总分\n${entity.totalScore}")
                spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.tv_big))
                        , 3, 3 + "${entity.totalScore}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                scoreTv.text = spannableString

                heightRound.progress = entity.maxScore.toInt()
                spannableString = SpannableString("最高分\n${entity.maxScore}")
                spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.tv_big))
                        , 4, 4 + "${entity.maxScore}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                heightTv.text = spannableString

                errorRound.progress = entity.errorNum.toInt()
                spannableString = SpannableString("错题\n${entity.errorNum}")
                spannableString.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.tv_big))
                        , 3, 3 + "${entity.errorNum}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                errorTv.text = spannableString

                editParent.setText(entity.parentComment ?: "")
                editTeacher.setText(entity.teacherComment ?: "")

                if (StringUtils.isEmpty(entity.parentAutograph))
                    parentName.visibility = View.GONE
                else
                    parentName.text = entity.parentAutograph ?: ""
            }

        }

        override fun onError(exception: Throwable) {
            if (exception is MsgException) {
                exception.handleThrowable(this@WorkReportActivity)
                stateView.showError(exception.message)
            } else {
                stateView.showError()
            }

        }

    }


    private fun getReport() {
        EBagApi.homeworkReport(homeworkId, studentId, reportRequest)
    }

    private fun queryGift() {
        EBagApi.getGiftDetail(homeworkId, object : RequestCallBack<GiftTeacherBean>() {
            override fun onSuccess(entity: GiftTeacherBean?) {
                if (entity?.teacher!!.isEmpty()) {
                    gift_teacher.visibility = View.GONE
                } else {
                    entity?.teacher?.forEach {
                        when (it.giftName) {
                            "鲜花" -> {
                                flowerTeacher.visibility = View.VISIBLE
                                flowerTeacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "画板" -> {
                                paletteTeacher.visibility = View.VISIBLE
                                paletteTeacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "笔记本" -> {
                                notebookTeacher.visibility = View.VISIBLE
                                notebookTeacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "储蓄罐" -> {
                                piggyTeacher.visibility = View.VISIBLE
                                piggyTeacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "奖章" -> {
                                medalTeacher.visibility = View.VISIBLE
                                medalTeacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                        }
                    }
                }

                if (entity.parent2teacher.isNotEmpty()) {
                    entity.parent2teacher?.forEach {
                        when (it.giftName) {
                            "鲜花" -> {
                                flowerParent.visibility = View.VISIBLE
                                flowerParent.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "钢笔" -> {
                                paletteParent.visibility = View.VISIBLE
                                paletteParent.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "贺卡" -> {
                                notebookParent.visibility = View.VISIBLE
                                notebookParent.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "按摩椅" -> {
                                piggyParent.visibility = View.VISIBLE
                                piggyParent.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "台灯" -> {
                                medalParent.visibility = View.VISIBLE
                                medalParent.text = "${it.giftName} x ${it.giftNum}"
                            }
                        }
                    }
                } else {

                    gift_parent.visibility = View.GONE
                }

            }

            override fun onError(exception: Throwable) {

            }

        }, studentId)
    }

    inner class Adapter : BaseQuickAdapter<ReportBean.ReportDetailBean, BaseViewHolder>(R.layout.item_activity_report) {

        override fun convert(helper: BaseViewHolder, item: ReportBean.ReportDetailBean?) {
            helper.setText(R.id.questionType, item?.questionTypeName)
                    .setText(R.id.count, "${item?.questionNum}")
                    .setText(R.id.errorCount, "${item?.errorCount}")
                    .setBackgroundRes(R.id.layout, if (helper.adapterPosition % 2 == 0) R.color.light_blue else R.color.white)
            if (workType != Constants.STZY_TYPE) {
                helper.setGone(R.id.score, false)
            } else {
                helper.setText(R.id.score, "${String.format("%.02f", item?.questionScore)}")
                helper.setGone(R.id.score, true)
            }
        }

    }
}