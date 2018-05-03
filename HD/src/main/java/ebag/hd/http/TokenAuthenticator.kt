package ebag.hd.http

import ebag.core.base.App
import ebag.core.util.SPUtils
import ebag.hd.base.Constants
import okhttp3.*
import org.json.JSONObject

/**
 * Created by YZY on 2018/4/4.
 * token过期的时候重新调用登录接口刷新token
 */
class TokenAuthenticator: Authenticator {
    override fun authenticate(route: Route?, response: Response?): Request? {
        val account: String = SPUtils.get(App.mContext, Constants.USER_ACCOUNT, "") as String
        val pwd = SPUtils.get(App.mContext, Constants.USER_PASS_WORD, "") as String
        val loginType = SPUtils.get(App.mContext, Constants.LOGIN_TYPE, "") as String
        val roleCode = SPUtils.get(App.mContext, Constants.ROLE_CODE, "") as String
        val thirdPartyToken = SPUtils.get(App.mContext, Constants.THIRD_PARTY_TOKEN, "") as String
        val thirdPartyUnionid = SPUtils.get(App.mContext, Constants.THIRD_PARTY_UNION_ID, "") as String
        val thirdPartyType = SPUtils.get(App.mContext, Constants.THIRD_PARTY_TYPE, "") as String

        val jsonObject = JSONObject()
        jsonObject.put("deviceCode",SPUtils.get(App.mContext, ebag.core.util.Constants.IMEI, "") as String)
        jsonObject.put("isHDorPHONE","HD")
        jsonObject.put("password", pwd)
        jsonObject.put("loginAccount", account)
        jsonObject.put("loginType", loginType)
        jsonObject.put("thirdPartyType", thirdPartyType)
        jsonObject.put("roleCode", roleCode)
        jsonObject.put("thirdPartyUnionid", thirdPartyUnionid)
        jsonObject.put("thirdPartyToken", thirdPartyToken)

        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString())
        val request = Request.Builder().url("${ebag.core.util.Constants.BASE_URL}user/login/v1").post(requestBody).build()
        val mResponse = OkHttpClient().newCall(request).execute()
        val body = mResponse.body()?.string() ?: ""
        val json = com.alibaba.fastjson.JSONObject.parseObject(body)
        val data = json.getJSONObject("data")
        val token = data["token"] as String
        App.modifyToken(token)

        val url = response?.request()?.url()!!
                .newBuilder()
                .setQueryParameter("access_token", token)
                .build()
        return response.request()?.newBuilder()!!
                .url(url)
                .build()
    }
}