package com.yzy.ebag.student.activity.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yzy.ebag.student.R
import ebag.core.util.SerializableUtils
import ebag.core.util.loadImage
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userEntity = SerializableUtils.getSerializable<UserEntity>(Constants.STUDENT_USER_ENTITY)
        tvName.text = userEntity.name
        tvId.text = userEntity.id
        ivHead.loadImage(this,userEntity.headUrl)
        getString(R.string.main_class_name,"haha")
    }
}
