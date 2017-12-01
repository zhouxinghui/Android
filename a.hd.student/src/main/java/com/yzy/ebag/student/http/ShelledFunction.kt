package com.yzy.ebag.student.http

import com.yzy.ebag.student.http.baseBean.ResponseBean
import ebag.core.http.network.MsgException
import io.reactivex.functions.Function

/**
 * 返回数据的去壳操作
 * Created by unicho on 2017/11/13.
 */
class ShelledFunction<T>: Function<ResponseBean<T>,T> {
    override fun apply(t: ResponseBean<T>): T {
        if("200" == t.code)
            throw MsgException(t.code,t.message)
        return t.body
    }
}