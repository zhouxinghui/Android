package ebag.hd.mvp.presenter

import android.content.Context
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.hd.bean.YBCurrentBean
import ebag.hd.http.EBagApi
import ebag.hd.mvp.contract.YBCenterContract
import ebag.hd.mvp.model.YBCenterModel

/**
 * Created by fansan on 2018/3/13.
 */
class YBCenterPresenter(context: Context, view: YBCenterContract.View) : YBCenterContract.Presenter {

    private val mView: YBCenterContract.View by lazy { view }
    private val mContext: Context by lazy { context }

    override fun start() {
        request(1, 10, true, false)
    }

    override fun request(page: Int, pageSize: Int, isFirst: Boolean, isLoadmore: Boolean) {
        EBagApi.queryYBCurrent(page, pageSize, object : RequestCallBack<YBCurrentBean>() {

            override fun onStart() {
                super.onStart()
                if (isFirst) {
                    mView.showLoading()
                }
            }

            override fun onSuccess(entity: YBCurrentBean?) {
                val moneyDetails = entity?.moneyDetails
                var datas: MutableList<YBCenterModel> = mutableListOf()
                if (moneyDetails?.size != 0) {
                    for (i in moneyDetails?.indices!!) {
                        datas.add(YBCenterModel(moneyDetails[i].remark, moneyDetails[i].money, moneyDetails[i].type, moneyDetails[i].accountName, moneyDetails[i].accountType, moneyDetails[i].createDate))
                    }
                } else {
                    if (isLoadmore) {
                        mView.loadmoreEnd()
                    } else {
                        mView.showDataEmpty()
                    }
                    return
                }
                when {
                    isFirst -> mView.showSuccess(entity.remainMoney.toString(), entity.increasedMoney.toString(), entity.reduceMoney.toString(), datas)
                    isLoadmore -> mView.loadmoreComplete(datas)
                    else -> mView.dataLoadSuccess(datas)
                }

            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(mContext)
                when {
                    isFirst -> mView.showError()
                    isLoadmore -> mView.loadmoreError()
                    else -> mView.showDataError()
                }
            }

        })
    }

    override fun switch(page: Int, pageSize: Int, type: String, isLoadmore: Boolean) {

        EBagApi.queryYB(page, pageSize, type, object : RequestCallBack<YBCurrentBean>() {

            override fun onStart() {
                super.onStart()
                if (!isLoadmore)
                    mView.showDataLoading()
            }

            override fun onSuccess(entity: YBCurrentBean?) {
                var modelList: MutableList<YBCenterModel> = mutableListOf()
                val datas = entity?.moneyDetails
                if (datas?.size != 0) {
                    for (i in datas!!.indices) {
                        modelList.add(YBCenterModel(datas[i].remark, datas[i].money, datas[i].type, datas[i].accountName, datas[i].accountType, datas[i].createDate))
                    }
                    if (isLoadmore) {
                        mView.loadmoreComplete(modelList)
                    } else {
                        mView.dataLoadSuccess(modelList)
                    }
                } else {
                    if (isLoadmore) {
                        mView.loadmoreEnd()
                    } else {
                        mView.showDataEmpty()
                    }
                }
            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(mContext)
                if (isLoadmore) {
                    mView.loadmoreError()
                } else {
                    mView.showDataError()
                }
            }

        })
    }
}