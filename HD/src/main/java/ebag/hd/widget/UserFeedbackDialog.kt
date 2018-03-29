package ebag.hd.widget

import android.content.Context
import android.view.WindowManager
import ebag.core.base.BaseDialog
import ebag.core.http.network.RequestCallBack
import ebag.core.util.LoadingDialogUtil
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.R
import ebag.hd.http.EBagApi
import kotlinx.android.synthetic.main.dialog_user_feedback.*

/**
 * Created by YZY on 2018/3/29.
 */
class UserFeedbackDialog(context: Context): BaseDialog(context) {
    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x500)
    }
    override fun setHeight(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT
    }
    override fun getLayoutRes(): Int {
        return R.layout.dialog_user_feedback
    }

    private val feedbackRequest = object : RequestCallBack<String>(){
        override fun onStart() {
            dismiss()
            LoadingDialogUtil.showLoading(context, "正在提交...")
        }

        override fun onSuccess(entity: String?) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(context, "反馈成功")
        }

        override fun onError(exception: Throwable) {
            LoadingDialogUtil.closeLoadingDialog()
            T.show(context, "网络请求失败，请稍后重试")
        }
    }

    init {
        confirmBtn.setOnClickListener {
            val content = feedbackEdit.text.toString()
            if (StringUtils.isEmpty(content)){
                T.show(context, "请填写你需要反馈的内容")
                return@setOnClickListener
            }
            EBagApi.userFeedback(content, feedbackRequest)
        }
    }
}