package com.yzy.ebag.student.http

import android.content.Context
import ebag.core.http.network.MsgException
import ebag.core.util.T
import org.apache.http.conn.ConnectTimeoutException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by unicho on 2017/11/13.
 */
object HttpErrorUtil {

    fun handleThrowable(throwable: Throwable, mContext: Context, isToast: Boolean) {

        when (throwable.cause){
            is SocketTimeoutException -> if(isToast) T.show(mContext, "")
            is UnknownHostException -> if(isToast) T.show(mContext, "")
            is SocketException -> if(isToast) T.show(mContext, "")
            is ConnectTimeoutException -> if(isToast) T.show(mContext, "")
            is MsgException -> doMsgError(throwable.cause as MsgException,isToast)
            else -> T.show(mContext, "")
        }
    }

    /**
     * 自定义的异常处理方式
     */
    private fun doMsgError(exception: MsgException, isToast: Boolean){

    }
}