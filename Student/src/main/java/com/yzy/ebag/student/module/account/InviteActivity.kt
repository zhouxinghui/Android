package com.yzy.ebag.student.module.account

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import com.yzy.ebag.student.MainActivity
import com.yzy.ebag.student.R
import com.yzy.ebag.student.http.StudentApi
import ebag.core.base.BaseActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.mobile.base.Constants
import kotlinx.android.synthetic.main.activity_invitation.*

/**
 * Created by YZY on 2018/5/28.
 */
class InviteActivity : BaseActivity(){

    companion object {
        const val CODE_INVITE = 1
        const val CODE_REGISTER = 2

        fun jump(context: Context, type: Int){
            context.startActivity(
                    Intent(context, InviteActivity::class.java)
                            .putExtra("type", type)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_invitation
    }

    private var type = 0

    override fun initViews() {

        type = intent.getIntExtra("type", CODE_INVITE)
        nextBtn.isEnabled = false
        codeEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                nextBtn.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        // 隐藏返回按钮
        titleView.hiddenTitleLeftButton()

        when(type){
            CODE_INVITE -> {// 班级邀请码
                titleView.setTitle(R.string.invite_invite_tip)
                titleView.setRightBtnVisable(false)
                codeEdit.setHint(R.string.invite_invite_tip)
                tipText1.setText(R.string.invite_invite_get_tip)
                tipText2.setText(R.string.invite_invite_get_tip_info)
            }
            CODE_REGISTER -> {// 注册码
                titleView.setTitle(R.string.invite_register_tip)
                // 联系客服
                titleView.setRightText(getString(R.string.invite_contact_customer)){

                }
                codeEdit.setHint(R.string.invite_register_tip)
                tipText1.setText(R.string.invite_register_get_tip)
                tipText2.setText(R.string.invite_register_get_tip_info)
            }
        }

        nextBtn.setOnClickListener {
            when(type){
                CODE_INVITE -> {// 班级邀请码
                    StudentApi.joinClass(codeEdit.text.toString(), inviteRequest)
                }
                CODE_REGISTER -> {// 注册码

                }
            }
        }

        changeCount.setOnClickListener {
            startActivity(
                    Intent(this, LoginActivity::class.java)
                            .putExtra(Constants.KEY_TO_MAIN,true)
            )
        }
    }

    private val inviteRequest by lazy {
        object : RequestCallBack<String>(){

            override fun onStart() {
                LoadingDialogUtil.showLoading(this@InviteActivity)
            }

            override fun onSuccess(entity: String?) {
                T.show(this@InviteActivity, "加入班级成功")
                startActivity(Intent(this@InviteActivity, MainActivity::class.java))
                LoadingDialogUtil.closeLoadingDialog()
            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(this@InviteActivity)
                LoadingDialogUtil.closeLoadingDialog()
            }

        }
    }

    //禁止返回
    override fun onBackPressed() {

    }
}