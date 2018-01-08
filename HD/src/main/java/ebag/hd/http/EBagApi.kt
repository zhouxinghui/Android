package ebag.hd.http
import com.alibaba.fastjson.JSON
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.RequestSubscriber
import ebag.hd.bean.response.CodeEntity
import ebag.hd.bean.response.UserEntity
import ebag.hd.http.baseBean.RequestBean
import ebag.hd.http.baseBean.ResponseBean
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File


/**
 * Created by unicho on 2017/11/9.
 */
object EBagApi {

    private val JSON_TYPE = MediaType.parse("application/json; charset=utf-8")


    /**返回的数据格式是按照我们自己定义的数据格式时调用此方法*/
    fun <T> request(ob: Observable<ResponseBean<T>>, callback: RequestCallBack<T>){
        ob.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .map(ShelledFunction())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(RequestSubscriber(callback))
    }

    /**返回的数据格式是按照我们自己定义的数据格式时调用此方法*/
    fun <T> startRequest(ob: Observable<T>, callback: RequestCallBack<T>){
        ob.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(RequestSubscriber(callback))
    }

    fun <T> createBody(request: T): RequestBody {
        return RequestBody.create(JSON_TYPE, JSON.toJSONString(request))
    }

    fun createBody(jsonObject: JSONObject): RequestBody {
        return RequestBody.create(JSON_TYPE, jsonObject.toString())
    }

    fun <T> getRequestBean(body: T): RequestBean<T> {
        val request = RequestBean<T>()
        request.setBody(body)
        return request
    }

    /**
     * 文件上传的测试写法
     *
     * @param test
     * @param file
     * @param callBack
     */
    fun testFile(test: String, file: File, callBack: RequestCallBack<String>) {

        val requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file)

        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name, requestFile)
                .addFormDataPart("test", test)
                .build()

        startRequest(EBagClient.eBagService.uploadHead(multipartBody), callBack)
    }

    /**
     * 登录
     */
    fun login(account: String, pwd: String, roleCode: String, callback: RequestCallBack<UserEntity>){
        val jsonObj = JSONObject()
        jsonObj.put("loginAccount",account)
        jsonObj.put("password",pwd)
        jsonObj.put("loginType",1)
        jsonObj.put("roleCode",roleCode)
        request(EBagClient.eBagService.login("v1", createBody(jsonObj)),callback)
    }

    /**
     * 注册
     */
    fun register(name: String, phone: String, code: String, pwd: String, callback: RequestCallBack<UserEntity>){
    }

    /**
     * 获取验证码
     */
    fun getCode(phone: String, callback: RequestCallBack<CodeEntity>){

    }

}