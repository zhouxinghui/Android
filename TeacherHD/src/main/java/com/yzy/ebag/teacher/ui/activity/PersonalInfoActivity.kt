package com.yzy.ebag.teacher.ui.activity

import android.view.View
import com.yzy.ebag.teacher.R
import ebag.core.base.BaseActivity
import ebag.core.util.T
import ebag.core.util.loadHead
import kotlinx.android.synthetic.main.activity_personal_info.*

class PersonalInfoActivity : BaseActivity(), View.OnClickListener{
    override fun getLayoutId(): Int {
        return R.layout.activity_personal_info
    }

    override fun initViews() {
        headImage.loadHead("")

        headImageBtn.setOnClickListener(this)
        nameBtn.setOnClickListener(this)
        bagBtn.setOnClickListener(this)
        sexBtn.setOnClickListener(this)
        contactBtn.setOnClickListener(this)
        addressBtn.setOnClickListener(this)
        schoolBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.headImageBtn ->{
                T.show(this, "头像")
            }
            R.id.nameBtn ->{
                T.show(this, "姓名")
            }
            R.id.bagBtn ->{
                T.show(this, "书包号")
            }
            R.id.sexBtn ->{
                T.show(this, "性别")
            }
            R.id.contactBtn ->{
                T.show(this, "联系方式")
            }
            R.id.addressBtn ->{
                T.show(this, "家庭住址")
            }
            R.id.schoolBtn ->{
                T.show(this, "所在学校")
            }
        }
    }
}
