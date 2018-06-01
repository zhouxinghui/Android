package ebag.hd.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import ebag.core.base.BaseActivity
import ebag.core.http.network.MsgException
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.StringUtils
import ebag.hd.R
import ebag.hd.base.Constants
import ebag.hd.bean.GiftTeacherBean
import ebag.hd.bean.ReportBean
import ebag.hd.homework.DoHomeworkActivity
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.activity_report_test.*

/**
 * @author caoyu
 * @date 2018/1/24
 * @description
 */
class ReportTestActivity : BaseActivity() {
    companion object {
        fun jump(context: Context, homeworkId: String, workType: String, studentId: String = "", studentName: String = "") {
            context.startActivity(
                    Intent(context, ReportTestActivity::class.java)
                            .putExtra("homeworkId", homeworkId)
                            .putExtra("studentId", studentId)
                            .putExtra("workType", workType)
                            .putExtra("studentName", studentName)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_report_test
    }

    private lateinit var homeworkId: String
    private var workType = ""
    private var studentId = ""
    private var studentName = ""
    override fun initViews() {
        homeworkId = intent.getStringExtra("homeworkId") ?: ""
        studentId = intent.getStringExtra("studentId") ?: ""
        workType = intent.getStringExtra("workType") ?: ""
        studentName = intent.getStringExtra("studentName") ?: ""
        showData()
    }

    private fun showData() {
        if (!StringUtils.isEmpty(studentName)) {
            titleView.setTitle(studentName)
        } else {
            titleView.setTitle("作业报告")
        }
        titleView.setRightText("作业详情") {
            DoHomeworkActivity.jump(this, homeworkId, Constants.REPORT_TYPE, workType, studentId)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        stateView.setOnRetryClickListener {
            getReport()
        }

        if (packageName.contains("student")) {
            parent2teacherLayout.visibility = View.GONE
            gift_parent2teacher.visibility = View.GONE
        } else {
            parentGiftLayout.visibility = View.GONE
            gift_parent.visibility = View.GONE
        }

        if (workType == "3") {
            tipTeacher.visibility = View.GONE
            editTeacher.visibility = View.GONE
            gift_teacher.visibility = View.GONE
            teacherGiftLayout.visibility = View.GONE
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
                queryGift()
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

                editParent.setText(entity.parentComment ?: "")
                editTeacher.setText(entity.teacherComment ?: "")

                if (StringUtils.isEmpty(entity.parentAutograph))
                    tvName.visibility = View.GONE
                else
                    tvName.text = entity.parentAutograph ?: ""
            }

        }

        override fun onError(exception: Throwable) {
            if (exception is MsgException) {
                exception.handleThrowable(this@ReportTestActivity)
                stateView.showError(exception.message)
            } else {
                stateView.showError()
            }

        }

    }

    private fun queryGift() {
        EBagApi.getGiftDetail(homeworkId, object : RequestCallBack<GiftTeacherBean>() {
            override fun onSuccess(entity: GiftTeacherBean?) {
                if (entity!!.teacher.isEmpty()) {
                    gift_teacher.visibility = View.GONE
                } else {
                    entity?.teacher?.forEach {
                        when (it.giftName) {
                            "鲜花" -> {
                                flowerTeacher.visibility = View.VISIBLE
                                flowerTeacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "笔记本" -> {
                                paletteTeacher.visibility = View.VISIBLE
                                paletteTeacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "画板" -> {
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

                if (entity!!.parent.isEmpty()) {
                    gift_parent.visibility = View.GONE
                } else {

                    entity?.parent?.forEach {
                        when (it.giftName) {
                            "鲜花" -> {
                                flowerParent.visibility = View.VISIBLE
                                flowerParent.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "笔记本" -> {
                                paletteParent.visibility = View.VISIBLE
                                paletteParent.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "画板" -> {
                                notebookParent.visibility = View.VISIBLE
                                notebookParent.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "储蓄罐" -> {
                                piggyParent.visibility = View.VISIBLE
                                piggyParent.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "奖章" -> {
                                medalParent.visibility = View.VISIBLE
                                medalParent.text = "${it.giftName} x ${it.giftNum}"
                            }
                        }
                    }
                }

                if (entity!!.parent2teacher.isEmpty()) {
                    gift_parent2teacher.visibility = View.GONE
                } else {
                    entity?.parent2teacher?.forEach {

                        when (it.giftName) {
                            "鲜花" -> {
                                flowerParent2teacher.visibility = View.VISIBLE
                                flowerParent2teacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "钢笔" -> {
                                paletteParent2teacher.visibility = View.VISIBLE
                                paletteParent2teacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "贺卡" -> {
                                notebookParent2teacher.visibility = View.VISIBLE
                                notebookParent2teacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "按摩椅" -> {
                                piggyParent2teacher.visibility = View.VISIBLE
                                piggyParent2teacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                            "台灯" -> {
                                medalParent2teacher.visibility = View.VISIBLE
                                medalParent2teacher.text = "${it.giftName} x ${it.giftNum}"
                            }
                        }

                    }
                }
            }

            override fun onError(exception: Throwable) {

            }

        }, studentId, if (packageName.contains("student")) "student" else "")
    }

    private fun getReport() {
        EBagApi.homeworkReport(homeworkId, studentId, reportRequest)
    }

    inner class Adapter : BaseQuickAdapter<ReportBean.ReportDetailBean, BaseViewHolder>(R.layout.item_activity_report_test) {

        override fun convert(helper: BaseViewHolder, item: ReportBean.ReportDetailBean?) {
            helper.setText(R.id.questionType, item?.questionTypeName)
                    .setText(R.id.count, "${item?.questionNum}")
                    .setText(R.id.errorCount, "${item?.errorCount}")
                    .setBackgroundRes(R.id.layout, if (helper.adapterPosition % 2 == 0) R.color.light_blue else R.color.white)
        }

    }
}