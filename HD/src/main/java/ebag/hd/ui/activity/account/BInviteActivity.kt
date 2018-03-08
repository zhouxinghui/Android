package ebag.hd.ui.activity.account

import android.text.Editable
import android.text.TextWatcher
import ebag.core.base.mvp.MVPActivity
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.http.EBagApi
import ebag.hd.ui.presenter.InvitePresenter
import ebag.hd.ui.view.InviteView
import kotlinx.android.synthetic.main.activity_invitation.*

/**
 * Created by caoyu on 2017/11/21.
 * Activity 输入注册码
 */
abstract class BInviteActivity : MVPActivity(), InviteView {

    companion object {
        const val CODE_INVITE = 1
        const val CODE_REGISTER = 2
    }

    private val iDelegate = lazy { InvitePresenter(this,this) }
    private val invitePresenter: InvitePresenter by iDelegate
    override fun destroyPresenter() {
        if(iDelegate.isInitialized())
            invitePresenter.onDestroy()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_invitation
    }

    private var type = 0

    override fun initViews() {

        type = intent.getIntExtra("type", CODE_INVITE)
        nextBtn.isEnabled = false
        codeEdit.addTextChangedListener(object : TextWatcher{
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
                    EBagApi.joinClass(codeEdit.text.toString(), inviteRequest)
                }
                CODE_REGISTER -> {// 注册码

                }
            }
        }
    }

    private val inviteRequest by lazy {
        object :RequestCallBack<String>(){

            override fun onStart() {
                LoadingDialogUtil.showLoading(this@BInviteActivity)
            }

            override fun onSuccess(entity: String?) {
                T.show(this@BInviteActivity, "加入班级成功")
                inviteSuccess()
                LoadingDialogUtil.closeLoadingDialog()
            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(this@BInviteActivity)
                LoadingDialogUtil.closeLoadingDialog()
            }

        }
    }

    abstract fun inviteSuccess()

    // 禁止返回
//    override fun onBackPressed() {
//    }
}