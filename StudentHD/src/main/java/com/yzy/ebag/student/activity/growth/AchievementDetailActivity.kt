package com.yzy.ebag.student.activity.growth

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.Achievement
import ebag.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_achievement_detail.*

/**
 * @author caoyu
 * @date 2018/2/1
 * @description
 */
class AchievementDetailActivity: BaseActivity() {

    companion object {
        fun jump(context: Context, achievement: Achievement?){
            context.startActivity(
                    Intent(context,AchievementDetailActivity::class.java)
                            .putExtra("achievement", achievement)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_achievement_detail
    }

    private var achievement: Achievement? = null
    private lateinit var rightView: TextView
    override fun initViews() {
        achievement = intent.getSerializableExtra("achievement") as Achievement?

        if(achievement == null){
            finish()
            return
        }

        rightView = titleBar.rightView as TextView

        if(achievement?.summary.isNullOrEmpty()){
            rightView.text = "完成"
            editText.isEnabled = true
        }else{
            rightView.text = "编辑"
            editText.isEnabled = false
        }
        editText.setText(achievement?.summary)

        rightView.setOnClickListener {
            // 完成
            if(editText.isEnabled){
                rightView.text = "编辑"
                editText.isEnabled = false
                // 这里做网络请求的操作
            }else{//编辑
                rightView.text = "完成"
                editText.isEnabled = true
            }
        }

        date.text = achievement?.date
        time.text = achievement?.time
        score.text = "${achievement?.score}分"
    }

}