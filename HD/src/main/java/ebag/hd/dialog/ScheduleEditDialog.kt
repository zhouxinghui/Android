package ebag.hd.dialog

import android.content.Context
import ebag.core.base.BaseDialog
import ebag.core.util.StringUtils
import ebag.core.util.T
import ebag.hd.R
import kotlinx.android.synthetic.main.dialog_schedule_edit.*

/**
 * Created by unicho on 2018/3/9.
 */
class ScheduleEditDialog(context: Context): BaseDialog(context){

    override fun setWidth(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x500)
    }

    override fun setHeight(): Int {
        return context.resources.getDimensionPixelSize(R.dimen.x400)
    }

    override fun getLayoutRes(): Int {
        return R.layout.dialog_schedule_edit
    }
    var onConfirmClickListener: ((subject: String) -> Unit)? = null
    init {
        confirmBtn.setOnClickListener {
            val subject = countEdit.text.toString()
            if (StringUtils.isEmpty(subject)){
                T.show(context, "你没有输入任何内容")
                return@setOnClickListener
            }
            onConfirmClickListener?.invoke(subject)
        }
    }

}