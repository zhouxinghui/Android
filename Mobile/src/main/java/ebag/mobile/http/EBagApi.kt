package ebag.mobile.http

import com.alibaba.fastjson.JSON
import ebag.core.base.App
import ebag.core.bean.ResponseBean
import ebag.core.http.baseBean.RequestBean
import ebag.core.http.network.*
import ebag.mobile.bean.UserEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject


/**
 * Created by caoyu on 2017/11/9.
 */
object EBagApi {

    private val JSON_TYPE = MediaType.parse("application/json; charset=utf-8")


    /**返回的数据格式是按照我们自己定义的数据格式时调用此方法*/
    fun <T> request(ob: Observable<ResponseBean<T>>, callback: RequestCallBack<T>) {
        if (!App.mContext!!.hasNetwork()) {
            callback.onError(MsgException("500", "当前网络不可用，请检查网络设置！"))
            return
        }
        ob.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(EBagRequestObserver(callback))
    }

    /**返回的数据格式是按照我们自己定义的数据格式时调用此方法*/
    fun <T> startNormalRequest(ob: Observable<T>, callback: RequestCallBack<T>) {
        if (!App.mContext!!.hasNetwork()) {
            callback.onError(MsgException("500", "当前网络不可用，请检查网络设置！"))
            return
        }
        ob.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(NormalRequestObserver(callback))
    }

    fun <T> createBody(request: T): RequestBody {
        return RequestBody.create(JSON_TYPE, JSON.toJSONString(request))
    }

    fun createBody(jsonObject: JSONObject): RequestBody {
        return RequestBody.create(JSON_TYPE, jsonObject.toString())
    }

    fun createBody(string: String): RequestBody {
        return RequestBody.create(JSON_TYPE, string)
    }

    fun <T> getRequestBean(body: T): RequestBean<T> {
        val request = RequestBean<T>()
        request.setBody(body)
        return request
    }

    /**登录*/
    fun login(isHDorPHONE: String, account: String?, pwd: String?, loginType: String?, thirdPartyType: String?,
              roleCode: String, thirdPartyToken: String?, thirdPartyUnionid: String?, callback: RequestCallBack<UserEntity>) {
        val jsonObj = JSONObject()
        jsonObj.put("isHDorPHONE",isHDorPHONE)
        jsonObj.put("password", pwd)
        jsonObj.put("loginAccount", account)
        jsonObj.put("loginType", loginType)
        jsonObj.put("thirdPartyType", thirdPartyType)
        jsonObj.put("roleCode", roleCode)
        jsonObj.put("thirdPartyUnionid", thirdPartyUnionid)
        jsonObj.put("thirdPartyToken", thirdPartyToken)
        request(EBagClient.eBagService.login("v1", createBody(jsonObj)), callback)
    }

    /*fun bindingActivationCode(loginAccount:String,activation:String,callback: RequestCallBack<String>){
        val jsonObj = JSONObject()
        jsonObj.put("loginAccount",loginAccount)
        jsonObj.put("activation",activation)
        request(EBagClient.eBagService.bindingActivationCode("v1",createBody(jsonObj)),callback)
    }*/

    /**注册*/
    fun register(deviceCode:String,isHDorPHONE: String,name: String, headUrl: String?, sex: String?, phone: String?, verifyCode: String?, roleCode: String?, pwd: String, thirdPartyToken: String?, thirdPartyUnionid: String?, loginType: String?, thirdPartyType: String?, callback: RequestCallBack<UserEntity>) {
        val jsonObj = JSONObject()
        jsonObj.put("isHDorPHONE",isHDorPHONE)
        jsonObj.put("nickName", name)
        jsonObj.put("headUrl", headUrl)
        jsonObj.put("sex", sex)
        jsonObj.put("phone", phone)
        jsonObj.put("verifyCode", verifyCode)
        jsonObj.put("password", pwd)
        jsonObj.put("loginType", loginType)
        jsonObj.put("thirdPartyType", thirdPartyType)
        jsonObj.put("thirdPartyToken", thirdPartyToken)
        jsonObj.put("thirdPartyUnionid", thirdPartyUnionid)
        jsonObj.put("roleCode", roleCode)
        jsonObj.put("deviceCode",deviceCode)
        request(EBagClient.eBagService.register("v1", createBody(jsonObj)), callback)
    }

    /**忘记密码*/
    fun resetPassword(phone: String, ysbCode: String, code: String, password: String, callback: RequestCallBack<String>) {
        val jsonObj = JSONObject()
        jsonObj.put("ysbCode", ysbCode)
        jsonObj.put("phone", phone)
        jsonObj.put("verifyCode", code)
        jsonObj.put("password", password)
        request(EBagClient.eBagService.resetPassword("v1", createBody(jsonObj)), callback)
    }

    /**检查用户是否存在*/
    fun checkUserExist(phone: String, callback: RequestCallBack<String>) {
        val jsonObj = JSONObject()
        jsonObj.put("loginAccount", phone)
        request(EBagClient.eBagService.checkUserExist("v1", createBody(jsonObj)), callback)
    }

    /**获取验证码*/
    fun getCode(phone: String, ysbCode: String, callback: RequestCallBack<String>) {
        val jsonObject = JSONObject()
        if (phone.isNotEmpty()) {
            jsonObject.put("phone", phone)
        } else {
            jsonObject.put("ysbCode", ysbCode)
        }
        request(EBagClient.eBagService.getCheckCode("v1", createBody(jsonObject)), callback)
    }
}