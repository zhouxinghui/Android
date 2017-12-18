package com.yzy.ebag.student.ui.activity.account

import android.text.Editable
import android.text.TextWatcher
import com.yzy.ebag.student.R
import com.yzy.ebag.student.ui.presenter.InvitePresenter
import com.yzy.ebag.student.ui.view.InviteView
import ebag.core.base.mvp.MVPActivity
import kotlinx.android.synthetic.main.activity_invitation.*

/**
 * Created by unicho on 2017/11/21.
 * Activity 输入注册码
 */
class InviteActivity: MVPActivity(),InviteView {

    private val iDelegate = lazy { InvitePresenter(this,this) }
    private val invitePresenter: InvitePresenter by iDelegate
    override fun destroyPresenter() {
        if(iDelegate.isInitialized())
            invitePresenter.onDestroy()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_invitation
    }

    override fun initViews() {
        codeEdit.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                nextBtn.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}