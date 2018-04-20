package com.yzy.ebag.parents.http

import com.yzy.ebag.parents.bean.MyChildrenBean
import ebag.core.http.network.RequestCallBack
import ebag.mobile.http.EBagApi
import ebag.mobile.http.EBagClient
import org.json.JSONObject

object ParentsAPI {

    private val parentsService: ParentsService by lazy {
        EBagClient.createRetrofitService(ParentsService::class.java)
    }


    fun searchMyChildren(callback: RequestCallBack<List<MyChildrenBean>>) {
        val jsonObject = JSONObject()
        EBagApi.request(parentsService.searchChildren("v1", EBagApi.createBody(jsonObject)), callback)
    }
}