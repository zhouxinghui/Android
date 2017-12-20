package ebag.hd.http
import ebag.core.http.network.RequestCallBack
import ebag.core.http.network.RequestSubscriber
import ebag.hd.http.baseBean.QuestionErrEntity
import ebag.hd.http.baseBean.RequestBean
import ebag.hd.http.baseBean.ResponseBean
import ebag.hd.http.baseBean.ResponseEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by unicho on 2017/11/9.
 */
object EBagApiBac {


    /**返回的数据格式是按照我们自己定义的数据格式时*/
    private fun <T> request(ob: Observable<ResponseBean<T>>, callBack: RequestCallBack<T>){
        ob.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(ShelledFunction<T>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RequestSubscriber(callBack))
    }

    /**返回的数据格式是按照我们自己定义的数据格式时*/
    private fun <T> startRequest(ob: Observable<T>, callBack: RequestCallBack<T>){
        ob.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RequestSubscriber(callBack))
    }

    private fun <T> getRequestBean(body: T): RequestBean<T> {
        val request = RequestBean<T>()
        request.setBody(body)
        return request
    }

    /**一个测试的方法*/
    fun test(homeWorkId: String, questionErrId: String, callBack: RequestCallBack<List<ResponseEntity>>) {
        val errEntity = QuestionErrEntity()
        errEntity.homeWorkId = homeWorkId
        errEntity.questionErrId = questionErrId
        request(EBagClient.eBagService.getSmscode(getRequestBean(errEntity)), callBack)
    }


    /**文件上传的测试写法*/
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
    fun login(account: String, pwd: String, callBack: RequestCallBack<String>){

    }

    /**
     * 注册
     */
    fun register(name: String, phone: String, code: String, pwd: String, callBack: RequestCallBack<String>){

    }

    /**
     * 获取验证码
     */
    fun getCode(phone: String, callBack: RequestCallBack<String>){

    }

}