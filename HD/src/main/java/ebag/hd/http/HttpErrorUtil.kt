package ebag.hd.http
import android.content.Context
import android.net.ConnectivityManager
import ebag.core.http.network.MsgException
import ebag.core.util.T
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException



/**
 * Created by unicho on 2017/11/13.
 */
fun Throwable.handleThrowable(mContext: Context, isToast: Boolean = true, callback: ((MsgException) -> Unit)? = null){
    when (this){
        is MsgException -> {
            if(callback == null) {
                if (isToast)
                    T.show(mContext, this.message.toString())
            } else {
                callback.invoke(this)
            }
        }
        is JSONException -> if(isToast) T.show(mContext, "数据异常")
        is SocketTimeoutException -> if(isToast) T.show(mContext, "连接超时")
        is UnknownHostException -> if(isToast) T.show(mContext, "服务器连接失败")
        is ConnectException -> if(isToast) T.show(mContext, "服务器连接失败")
        is SocketException -> if(isToast) T.show(mContext, "服务器连接失败")
        else -> T.show(mContext, "网络异常，请稍后重试")
    }
}

fun Context.hasNetwork() : Boolean{
        val mConnectivityManager = this
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager
                .activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable
        }
    return false
}