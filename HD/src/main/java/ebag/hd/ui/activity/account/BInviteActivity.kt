package ebag.hd.ui.activity.account

import android.text.Editable
import android.text.TextWatcher
import ebag.core.base.mvp.MVPActivity
import ebag.hd.R
import ebag.hd.ui.presenter.InvitePresenter
import ebag.hd.ui.view.InviteView
import kotlinx.android.synthetic.main.activity_invitation.*

/**
 * Created by unicho on 2017/11/21.
 * Activity 输入注册码
 */
class BInviteActivity : MVPActivity(), InviteView {

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