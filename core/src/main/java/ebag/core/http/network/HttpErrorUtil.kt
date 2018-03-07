package ebag.core.http.network
import android.content.Context
import android.net.ConnectivityManager
import ebag.core.util.T
import org.json.JSONException
import retrofit2.adapter.rxjava2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException



/**
 * Created by caoyu on 2017/11/13.
 */
fun Throwable.handleThrowable(mContext: Context, isToast: Boolean = true){
    when (this){
        is MsgException -> if (isToast) T.show(mContext, this.message.toString())
        is JSONException -> if(isToast) T.show(mContext, "服务器返回数据异常")
        is SocketTimeoutException -> if(isToast) T.show(mContext, "连接超时")
        is UnknownHostException -> if(isToast) T.show(mContext, "连接服务器失败")
        is ConnectException -> if(isToast) T.show(mContext, "连接服务器失败")
        is SocketException -> if(isToast) T.show(mContext, "连接服务器失败")
        is HttpException -> if (isToast){
            if (this.code() == 401) {
                T.show(mContext, "认证信息失效，请重新登录")

            } else
                T.show(mContext, "网络异常，请稍后重试")
        }
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