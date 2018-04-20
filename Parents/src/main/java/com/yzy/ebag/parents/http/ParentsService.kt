package com.yzy.ebag.parents.http

import com.yzy.ebag.parents.bean.MyChildrenBean
import ebag.core.bean.ResponseBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ParentsService{

    @POST("user/searchMyChildren/{version}")
    fun searchChildren(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<MyChildrenBean>>>
}