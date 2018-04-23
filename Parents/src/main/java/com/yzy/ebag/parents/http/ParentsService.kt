package com.yzy.ebag.parents.http

import com.yzy.ebag.parents.bean.HomeworkAbstractBean
import com.yzy.ebag.parents.bean.MyChildrenBean
import com.yzy.ebag.parents.bean.OnePageInfoBean
import ebag.core.bean.ResponseBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ParentsService{
    /*查询孩子*/
    @POST("user/searchMyChildren/{version}")
    fun searchChildren(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<MyChildrenBean>>>

    /*首页作业*/
    @POST("user/getOnePageInfo/{version}")
    fun getOnePageInfo(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<List<OnePageInfoBean>>>

    /*作业报告*/
    @POST("correctHome/createHomeWorkRep/{version}")
    fun homeReport(@Path("version") version: String, @Body requestBody: RequestBody): Observable<ResponseBean<HomeworkAbstractBean>>
}