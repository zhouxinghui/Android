package ebag.hd.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import ebag.hd.R


/**
 * @author caoyu
 * @date 2018/2/5
 * @description
 */
class MsgDialogFragment: AppCompatDialogFragment() {

    private var title: String? = null

    private var message: String? = null

    private var hint: String? = null

    private var onClickListener: DialogInterface.OnClickListener? = null

    fun show(title: String?, message: String?, hint: String?, neutralCallback: DialogInterface.OnClickListener?,
             fragmentManager: FragmentManager) {
        this.title = title
        this.message = message
        this.hint = hint
        this.onClickListener = neutralCallback
        show(fragmentManager, "NeutralDialogFragment")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, R.style.NoBackgroundDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        if(title != null)
            builder.setTitle(title)
        if(message != null)
            builder.setMessage(message)
        if(hint != null)
            builder.setNeutralButton(hint, onClickListener)
        return builder.create()
    }
}