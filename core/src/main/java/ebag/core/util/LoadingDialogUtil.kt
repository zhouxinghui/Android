package ebag.core.util

import android.content.Context
import ebag.core.widget.empty.LoadingDialog

/**
 * Created by YZY on 2018/1/6.
 */
object LoadingDialogUtil {
    private var loadingDialog: LoadingDialog? = null
    fun showLoading(context: Context, message: String?){
        if (loadingDialog == null){
            loadingDialog = LoadingDialog(context)
        }
            loadingDialog!!.show(message)
    }
    fun showLoading(context: Context){
        showLoading(context, null)
    }

    fun closeLoadingDialog(){
        if (loadingDialog != null) {
            loadingDialog!!.dismiss()
            loadingDialog = null
        }
    }
}