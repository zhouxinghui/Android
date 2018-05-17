package ebag.mobile.module.account

import ebag.core.http.network.RequestCallBack
import ebag.mobile.bean.YBCurrentBean
import ebag.mobile.http.EBagApi

class YBCenterPersenter: YBCenterContract.Persenter <YBCenterContract.YBCenterView>{

    private var page = 1

    override fun startFirstpage(v:YBCenterContract.YBCenterView) {

        EBagApi.queryYBCurrent(page, 10, object : RequestCallBack<YBCurrentBean>() {

            override fun onStart() {
                v.showLoading()
            }

            override fun onSuccess(entity: YBCurrentBean?) {
                v.showData(entity)
                if (entity!!.moneyDetails.isEmpty()) {
                    v.showEmpty()
                } else {
                    v.showContents(entity)
                    page++
                }


            }

            override fun onError(exception: Throwable) {
                v.showError(exception)
            }

        })
    }

    override fun loadmore(v:YBCenterContract.YBCenterView) {

        EBagApi.queryYBCurrent(page, 10, object : RequestCallBack<YBCurrentBean>() {


            override fun onSuccess(entity: YBCurrentBean?) {
                if (entity!!.moneyDetails.isEmpty()) {
                    v.loadmoreEnd()
                } else {
                    v.showLoadMoreComplete(entity)
                    page++
                }
            }

            override fun onError(exception: Throwable) {
                v.loadmoreFail()
            }

        })
    }

}