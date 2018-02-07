package com.yzy.ebag.student.activity.tools.read

import android.content.Context
import android.content.Intent
import com.yzy.ebag.student.R
import com.yzy.ebag.student.bean.ReadOutBean
import ebag.core.base.BaseActivity

/**
 * @author caoyu
 * @date 2018/2/7
 * @description
 */
class ReadDetailActivity: BaseActivity() {

    companion object {
        fun jump(context: Context, readBean: ReadOutBean.OralLanguageBean){
            context.startActivity(
                    Intent(context, ReadDetailActivity::class.java)
                            .putExtra("oralLanguageBean", readBean)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_read_detail
    }

    private var readBean: ReadOutBean.OralLanguageBean? = null
    override fun initViews() {
        readBean = intent.getSerializableExtra("oralLanguageBean") as ReadOutBean.OralLanguageBean?

        if(readBean == null){

        }else{

        }

    }
}