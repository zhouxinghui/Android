package com.yzy.ebag.teacher.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View
import com.yzy.ebag.teacher.MainActivity
import com.yzy.ebag.teacher.R
import ebag.core.base.App
import ebag.core.base.BaseActivity
import ebag.core.base.mvp.OnToastListener
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.SerializableUtils
import ebag.hd.base.Constants
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.activity_binding.*
import ebag.hd.ui.presenter.LoginPresenter
import ebag.hd.ui.view.LoginView

class BindingActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_binding;
    }

    override fun initViews() {
       var name = intent.getStringExtra("name")
        if (name == "b"){
//            loginPresenter!!.login("1", "2", "teacher",null,null)
            btn_binding.setOnClickListener {
//            loginPresenter.login("1000857", "ysb123456", "teacher",null,null)
                EBagApi.login("1000857", "ysb123456", 1,"teacher",null,null,object : RequestCallBack<UserEntity>(){
                    override fun onSuccess(entity: UserEntity?) {
                        Log.d("wy",entity.toString())
                        LoadingDialogUtil.closeLoadingDialog()
                        if (entity != null) {
                            App.modifyToken(entity.token)
                        }
                        if (entity != null) {
                            entity.roleCode = "teacher"
                        }
                        SerializableUtils.deleteSerializable("teacher")
                        SerializableUtils.setSerializable("teacher", entity)
//                        setResult(Constants.CODE_LOGIN_RESULT)
                        startActivity(Intent(this@BindingActivity, MainActivity::class.java))
                        finish()

                    }

                    override fun onError(exception: Throwable) {
                        Log.d("wy","错误")
                    }
                })
            }

        }
        if (name == "x") {
            et_user.visibility = View.GONE
        }
    }

}
