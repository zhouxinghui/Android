package com.yzy.ebag.student.activity.center

import com.yzy.ebag.student.R
import ebag.core.base.mvp.MVPActivity
import ebag.core.util.SerializableUtils
import ebag.core.util.loadImage
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.activity_personal.*

/**
 * Created by unicho on 2018/1/12.
 */
class PersonalActivity: MVPActivity() {

    override fun destroyPresenter() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_personal
    }

    override fun initViews() {
        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        if(userEntity != null){
            tvName.text = userEntity.name
            tvId.text = userEntity.ysbCode
            ivHead.loadImage(userEntity.headUrl)
        }

        tvMain.setOnClickListener {
            finish()
        }
    }
}