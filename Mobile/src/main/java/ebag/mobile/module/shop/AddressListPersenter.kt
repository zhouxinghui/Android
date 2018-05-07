package ebag.mobile.module.shop


import android.content.Context
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.handleThrowable
import ebag.mobile.bean.AddressListBean
import ebag.mobile.http.EBagApi
/**
 * Created by fansan on 2018/3/14.
 */
class AddressListPersenter(context: Context,view:AddressContract.View): AddressContract.Persenter {


    private val mView:AddressContract.View by lazy { view }
    private val context: Context by lazy { context }

    override fun start() {
        request()
    }

    override fun request() {

        EBagApi.queryAddress(object :RequestCallBack<MutableList<AddressListBean>>(){

            override fun onStart() {
                super.onStart()
                mView.showLoading()
            }

            override fun onSuccess(entity: MutableList<AddressListBean>?) {

                if (entity?.size!=0){
                    mView.showSuccess(entity!!)
                }else{
                    mView.showEmpty()
                }
            }

            override fun onError(exception: Throwable) {
                exception.handleThrowable(context)
                mView.showError()
            }

        })
    }

    override fun deleteAddress(id:String,position:Int) {
        EBagApi.deleteAddress(id,object :RequestCallBack<String>(){
            override fun onSuccess(entity: String?) {
                mView.delete(position)
            }

            override fun onError(exception: Throwable) {

            }

        })

    }



}