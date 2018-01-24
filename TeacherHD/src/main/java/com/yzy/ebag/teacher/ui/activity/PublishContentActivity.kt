package com.yzy.ebag.teacher.ui.activity

import android.app.Activity
import android.content.Intent
import com.yzy.ebag.teacher.base.Constants
import com.yzy.ebag.teacher.http.TeacherApi
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SerializableUtils
import ebag.hd.bean.response.UserEntity
import ebag.hd.ui.activity.BPublishContentActivity

class PublishContentActivity : BPublishContentActivity() {
    private val uploadRequest by lazy { object : RequestCallBack<String>(){
        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            finish()
            setResult(Constants.PUBLISH_RESULT)
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            exception.handleThrowable(this@PublishContentActivity)
        }
    } }
    companion object {
        fun jump(context: Activity, classId: String){
            context.startActivityForResult(Intent(
                    context, PublishContentActivity::class.java).putExtra("classId", classId),
                    Constants.PUBLISH_REQUEST)
        }
    }
    override fun commit(content: String, urls: String) {
        TeacherApi.publishNotice(intent.getStringExtra("classId"), content, urls, uploadRequest)
    }

    override fun getUid(): String {
        val userEntity = SerializableUtils.getSerializable<UserEntity>(ebag.hd.base.Constants.TEACHER_USER_ENTITY)
        return userEntity.uid
    }

}
