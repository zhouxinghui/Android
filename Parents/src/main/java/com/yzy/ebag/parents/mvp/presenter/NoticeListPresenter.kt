package com.yzy.ebag.parents.mvp.presenter

import com.yzy.ebag.parents.mvp.NoticeListContract
import ebag.core.http.network.RequestCallBack
import ebag.mobile.bean.NoticeBean
import ebag.mobile.http.EBagApi

class NoticeListPresenter(private val view:NoticeListContract.NoticeListView):NoticeListContract.Presenter{

    private var id:String = ""
    private var page:Int = 1

    override fun firstList(clazzId: String) {
        this.id = clazzId
        EBagApi.noticeList(page,10,id,object:RequestCallBack<List<NoticeBean>>(){

            override fun onStart() {
                view.showLoading()
            }

            override fun onSuccess(entity: List<NoticeBean>?) {
                if (entity!!.isEmpty()){
                    view.showEmpty()
                }else{
                    view.showContents(entity)
                }

                page++
            }

            override fun onError(exception: Throwable) {
                view.showError(exception)
            }

        })
    }

    override fun loadMore() {
        EBagApi.noticeList(page,10,id,object:RequestCallBack<List<NoticeBean>>(){

            override fun onSuccess(entity: List<NoticeBean>?) {
                if (entity!!.isEmpty()){
                    view.loadmoreEnd()
                }else{
                    view.showMoreComplete(entity)
                }

                if (entity.size < 10){
                    view.loadmoreEnd()
                }
            }

            override fun onError(exception: Throwable) {

                view.loadmoreFail()
            }

        })
    }

    override fun start() {

    }

}