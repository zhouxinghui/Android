package ebag.hd.mvp.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.core.util.T
import ebag.hd.bean.VersionUpdateBean
import ebag.hd.bean.YBCurrentBean
import ebag.hd.http.EBagApi
import ebag.hd.mvp.contract.YBCenterContract
import ebag.hd.mvp.model.YBCenterModel

/**
 * Created by fansan on 2018/3/13.
 */
class YBCenterPresenter(context: Context,view:YBCenterContract.View ):YBCenterContract.Presenter {

    private val mView: YBCenterContract.View by lazy { view }
    private val mContext: Context by lazy { context }

    override fun start() {
        request()
    }

    override fun request() {
        EBagApi.queryYBCurrent(object : RequestCallBack<YBCurrentBean>(){

            override fun onStart() {
                super.onStart()
                mView.showLoading()
            }

            override fun onSuccess(entity: YBCurrentBean?) {
                val moneyDetails = entity?.moneyDetails
                var datas:MutableList<YBCenterModel> = mutableListOf()
                if (moneyDetails?.size!= 0){
                    for (i in moneyDetails?.indices!!){
                        datas.add(YBCenterModel(moneyDetails[i].remark,moneyDetails[i].money,moneyDetails[i].type,moneyDetails[i].accountName,moneyDetails[i].accountType,moneyDetails[i].createDate))
                    }
                }

                mView.showSuccess(entity.remainMoney.toString(),entity.increasedMoney.toString(),entity.reduceMoney.toString(),datas)
            }

            override fun onError(exception: Throwable) {
                Log.d("fansan","error")
                exception.handleThrowable(mContext)
            }

        })
    }
}